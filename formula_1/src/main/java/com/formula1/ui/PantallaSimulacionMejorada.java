package com.formula1.ui;

import com.formula1.almacenamiento.HistorialResultados;
import com.formula1.modelo.Circuito;
import com.formula1.simulacion.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;

/**
 * Pantalla de simulacion avanzada con narracion en vivo.
 * Ejecuta la carrera en un hilo secundario mostrando comentarios del sistema
 * Comentarista, cambios de clima durante la carrera, barra de progreso y
 * clasificacion parcial. Al finalizar permite abrir un panel modal con
 * estadisticas detalladas (PanelEstadisticasCarrera).
 */
import javafx.stage.Stage;

import java.util.List;

/**
 * Pantalla de simulación con comentarios en vivo y estadísticas detalladas
 */
public class PantallaSimulacionMejorada {
    private AppFX app;
    private GestorTemporada gestorTemporada;
    private SimuladorClasificacion simulador;
    private HistorialResultados historial;
    private Scene scene;
    private TextArea areaComentarios;
    private VBox areaResultados;
    private ProgressBar progressBar;
    private Button btnSimular;
    private Button btnVerEstadisticas;
    private GestorTemporada.ResultadoCarrera ultimoResultadoCarrera;

    public PantallaSimulacionMejorada(AppFX app, GestorTemporada gestorTemporada,
                                      SimuladorClasificacion simulador, HistorialResultados historial) {
        this.app = app;
        this.gestorTemporada = gestorTemporada;
        this.simulador = simulador;
        this.historial = historial;
        this.scene = crearEscena();
    }

    private Scene crearEscena() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-screen");

        // Franja superior
        Region franja = new Region();
        franja.getStyleClass().add("franja-cuadros");
        franja.setPrefHeight(6);

        VBox sidebar = crearSidebar();
        VBox contenidoCentral = crearContenido();

        VBox contenedorDerecho = new VBox(franja, contenidoCentral);
        VBox.setVgrow(contenidoCentral, Priority.ALWAYS);

        root.setLeft(sidebar);
        root.setCenter(contenedorDerecho);

        Scene scene = new Scene(root);
        Estilos.aplicar(scene);
        return scene;
    }

    private VBox crearSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(240);
        sidebar.setMinWidth(240);

        Label logo = new Label("F1 SIMULATOR");
        logo.getStyleClass().add("sidebar-logo");
        Label logoSub = new Label("SIMULACIÓN");
        logoSub.getStyleClass().add("sidebar-logo-sub");

        VBox logoBox = new VBox(4, logo, logoSub);
        logoBox.setPadding(new Insets(30, 24, 30, 24));

        Region espaciador = new Region();
        VBox.setVgrow(espaciador, Priority.ALWAYS);

        Button btnVolver = new Button("← Volver al Inicio");
        btnVolver.getStyleClass().add("nav-boton-salir");
        btnVolver.setMaxWidth(Double.MAX_VALUE);
        btnVolver.setOnAction(e -> app.volverAlInicio());

        VBox espaciadorInferior = new VBox();
        espaciadorInferior.setPadding(new Insets(16));
        espaciadorInferior.getChildren().add(btnVolver);

        sidebar.getChildren().addAll(logoBox, espaciador, espaciadorInferior);
        return sidebar;
    }

    private VBox crearContenido() {
        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(40));

        if (gestorTemporada.temporadaFinalizada()) {
            Label mensaje = new Label("¡LA TEMPORADA HA FINALIZADO!");
            mensaje.getStyleClass().add("hero-titulo");
            mensaje.setAlignment(Pos.CENTER);

            Label submensaje = new Label("No hay más carreras disponibles");
            submensaje.getStyleClass().add("hero-subtitulo");

            VBox mensajeBox = new VBox(15, mensaje, submensaje);
            mensajeBox.setAlignment(Pos.CENTER);

            contenido.getChildren().add(mensajeBox);
            return contenido;
        }

        // Información de la carrera
        VBox infoCarrera = crearInfoCarrera();

        // Área de comentarios
        Label lblComentarios = new Label("📻 COMENTARIOS EN VIVO");
        lblComentarios.getStyleClass().add("hero-subtitulo");

        areaComentarios = new TextArea();
        areaComentarios.setEditable(false);
        areaComentarios.setWrapText(true);
        areaComentarios.setPrefHeight(250);
        areaComentarios.getStyleClass().add("text-area");
        areaComentarios.setText("Esperando inicio de la carrera...");

        VBox.setVgrow(areaComentarios, Priority.ALWAYS);

        // Progress bar
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(Double.MAX_VALUE);
        progressBar.getStyleClass().add("progress-bar");
        progressBar.setVisible(false);

        // Botones
        HBox botonesBox = new HBox(15);
        botonesBox.setAlignment(Pos.CENTER);

        btnSimular = new Button("🏁 INICIAR CARRERA");
        btnSimular.getStyleClass().add("boton-cta");
        btnSimular.setOnAction(e -> simularCarrera());

        btnVerEstadisticas = new Button("📊 VER ESTADÍSTICAS DETALLADAS");
        btnVerEstadisticas.getStyleClass().add("button");
        btnVerEstadisticas.setVisible(false);
        btnVerEstadisticas.setOnAction(e -> mostrarEstadisticasDetalladas());

        botonesBox.getChildren().addAll(btnSimular, btnVerEstadisticas);

        // Área de resultados
        areaResultados = new VBox(10);
        areaResultados.setVisible(false);

        ScrollPane scrollResultados = new ScrollPane(areaResultados);
        scrollResultados.setFitToWidth(true);
        scrollResultados.getStyleClass().add("scroll-pane");
        scrollResultados.setVisible(false);
        VBox.setVgrow(scrollResultados, Priority.ALWAYS);

        contenido.getChildren().addAll(
                infoCarrera,
                lblComentarios,
                areaComentarios,
                progressBar,
                botonesBox,
                scrollResultados
        );

        return contenido;
    }

    private VBox crearInfoCarrera() {
        VBox info = new VBox(15);
        info.getStyleClass().add("stats-box");

        int numeroCarrera = gestorTemporada.getCarreraActual() + 1;
        Circuito circuito = gestorTemporada.getSiguienteCircuito();

        Label titulo = new Label("CARRERA #" + numeroCarrera + " DE 24");
        titulo.getStyleClass().add("hero-titulo");

        Label nombreCircuito = new Label(circuito.getNombre());
        nombreCircuito.getStyleClass().add("hero-subtitulo");

        HBox detalles = new HBox(30);
        detalles.setAlignment(Pos.CENTER);
        detalles.getChildren().addAll(
                crearDetalle("📍", circuito.getPais()),
                crearDetalle("📏", String.format("%.2f km", circuito.getLongitudKm())),
                crearDetalle("🔄", circuito.getVueltas() + " vueltas"),
                crearDetalle("🏁", String.format("%.2f km total", circuito.getLongitudKm() * circuito.getVueltas()))
        );

        info.getChildren().addAll(titulo, nombreCircuito, detalles);
        return info;
    }

    private VBox crearDetalle(String icono, String texto) {
        VBox detalle = new VBox(5);
        detalle.setAlignment(Pos.CENTER);

        Label lblIcono = new Label(icono);
        lblIcono.setStyle("-fx-font-size: 24px;");

        Label lblTexto = new Label(texto);
        lblTexto.getStyleClass().add("tarjeta-label-detalle");

        detalle.getChildren().addAll(lblIcono, lblTexto);
        return detalle;
    }

    private void simularCarrera() {
        btnSimular.setDisable(true);
        btnVerEstadisticas.setVisible(false);
        progressBar.setVisible(true);
        progressBar.setProgress(0);
        areaComentarios.clear();
        areaResultados.getChildren().clear();
        areaResultados.setVisible(false);

        // Simular en un hilo separado
        new Thread(() -> {
            try {
                Circuito circuito = gestorTemporada.getSiguienteCircuito();
                Clima climaInicial = GeneradorClima.generarClimaPorCircuito(circuito.getNombre());

                // Comentario de inicio
                agregarComentario(Comentarista.comentarInicio(circuito, climaInicial));
                Thread.sleep(1500);

                agregarComentario("\n🏁 ¡Las luces se apagan! ¡ARRANCA LA CARRERA!\n");
                Thread.sleep(1000);

                updateProgress(0.3);

                // Simular usando el método del gestor (que ya registra todo)
                GestorTemporada.ResultadoCarrera resultadoCarrera =
                        gestorTemporada.simularSiguienteCarrera(simulador);

                ultimoResultadoCarrera = resultadoCarrera;

                updateProgress(0.5);

                List<SimuladorClasificacion.ResultadoVuelta> resultados = resultadoCarrera.getClasificacion();

                // Simular cambio de clima aleatorio (30% de probabilidad)
                Clima climaFinal = climaInicial;
                if (Math.random() < 0.3) {
                    climaFinal = cambiarClimaAleatorio(climaInicial);
                    if (climaFinal != climaInicial) {
                        agregarComentario(Comentarista.comentarCambioClima(climaInicial, climaFinal));
                        Thread.sleep(1000);
                    }
                }

                // Comentar líder
                if (!resultados.isEmpty() && !resultados.get(0).isDnf()) {
                    agregarComentario(Comentarista.comentarLider(resultados.get(0).getNombrePiloto()));
                    Thread.sleep(1000);
                }

                updateProgress(0.7);

                // Comentar eventos DNF
                int comentariosEventos = 0;
                for (SimuladorClasificacion.ResultadoVuelta resultado : resultados) {
                    if (comentariosEventos >= 5) break;

                    if (resultado.isDnf()) {
                        EventoCarrera evento = resultado.getEvento();
                        if (evento.esAbandonoDNF()) {
                            if (evento.toString().contains("ACCIDENTE")) {
                                agregarComentario(Comentarista.comentarAccidente(
                                        resultado.getNombrePiloto(),
                                        resultado.getEquipo(),
                                        true
                                ));
                            } else {
                                agregarComentario(Comentarista.comentarFalloMecanico(
                                        resultado.getNombrePiloto(),
                                        resultado.getEquipo()
                                ));
                            }
                            comentariosEventos++;
                            Thread.sleep(800);
                        }
                    }
                }

                updateProgress(0.9);

                // Comentarios finales
                String ganador = resultadoCarrera.getGanador();
                agregarComentario("\n" + Comentarista.comentarVictoria(ganador, "", circuito.getNombre()));
                Thread.sleep(1000);

                // Resumen
                agregarComentario(Comentarista.generarResumenCarrera(resultadoCarrera));

                updateProgress(1.0);

                // Mostrar resultados
                Platform.runLater(() -> {
                    mostrarResultadosRapidos(resultados);
                    btnVerEstadisticas.setVisible(true);
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    areaComentarios.appendText("\n\n❌ Error durante la simulación: " + e.getMessage());
                });
                e.printStackTrace();
            } finally {
                Platform.runLater(() -> {
                    btnSimular.setDisable(false);
                    btnSimular.setText("🏁 SIMULAR SIGUIENTE CARRERA");
                    progressBar.setVisible(false);
                });
            }
        }).start();
    }

    private Clima cambiarClimaAleatorio(Clima climaActual) {
        double rand = Math.random();
        return switch (climaActual) {
            case SECO -> {
                if (rand < 0.70) yield Clima.SECO;
                if (rand < 0.95) yield Clima.LLUVIOSO;
                yield Clima.EXTREMO;
            }
            case LLUVIOSO -> {
                if (rand < 0.40) yield Clima.SECO;
                if (rand < 0.80) yield Clima.LLUVIOSO;
                yield Clima.EXTREMO;
            }
            case EXTREMO -> {
                if (rand < 0.20) yield Clima.SECO;
                if (rand < 0.70) yield Clima.LLUVIOSO;
                yield Clima.EXTREMO;
            }
        };
    }

    private void mostrarEstadisticasDetalladas() {
        if (ultimoResultadoCarrera != null) {
            // Crear resultado detallado simple desde el resultado de carrera
            SimuladorCarreraRealista.ResultadoCarreraDetallado detallado =
                new SimuladorCarreraRealista.ResultadoCarreraDetallado(
                    ultimoResultadoCarrera.getClasificacion(),
                    ultimoResultadoCarrera.getClima(),
                    ultimoResultadoCarrera.getClima(),
                    0,
                    new java.util.ArrayList<>(),
                    0,
                    null,
                    0
                );

            // Crear ventana modal con estadísticas
            Stage estadisticasStage = new Stage();
            estadisticasStage.setTitle("Estadísticas Detalladas de la Carrera");

            PanelEstadisticasCarrera panel = new PanelEstadisticasCarrera(
                detallado,
                ultimoResultadoCarrera
            );

            BorderPane root = new BorderPane();
            root.getStyleClass().add("root-screen");
            root.setCenter(panel);

            Scene scene = new Scene(root, 1000, 700);
            Estilos.aplicar(scene);

            estadisticasStage.setScene(scene);
            estadisticasStage.show();
        }
    }

    private void agregarComentario(String comentario) {
        Platform.runLater(() -> {
            areaComentarios.appendText(comentario + "\n\n");
            areaComentarios.setScrollTop(Double.MAX_VALUE);
        });
    }

    private void updateProgress(double valor) {
        Platform.runLater(() -> progressBar.setProgress(valor));
    }

    private void mostrarResultadosRapidos(List<SimuladorClasificacion.ResultadoVuelta> resultados) {
        areaResultados.getChildren().clear();

        Label titulo = new Label("🏁 RESULTADOS FINALES - TOP 10");
        titulo.getStyleClass().add("hero-subtitulo");

        VBox listaResultados = new VBox(10);

        // Mostrar solo top 10 en vista rápida
        int limite = Math.min(10, resultados.size());
        for (int i = 0; i < limite; i++) {
            SimuladorClasificacion.ResultadoVuelta resultado = resultados.get(i);
            HBox fila = crearFilaResultado(i + 1, resultado);

            if (i == 0 && !resultado.isDnf()) fila.getStyleClass().add("list-row-gold");
            else if (i == 1 && !resultado.isDnf()) fila.getStyleClass().add("list-row-silver");
            else if (i == 2 && !resultado.isDnf()) fila.getStyleClass().add("list-row-bronze");
            else if (i < 10 && !resultado.isDnf()) fila.getStyleClass().add("list-row-points");
            else if (resultado.isDnf()) fila.getStyleClass().add("list-row-dnf");
            else fila.getStyleClass().add("list-row");

            listaResultados.getChildren().add(fila);
        }

        areaResultados.getChildren().addAll(titulo, listaResultados);
        areaResultados.setVisible(true);
    }

    private HBox crearFilaResultado(int posicion, SimuladorClasificacion.ResultadoVuelta resultado) {
        HBox fila = new HBox(15);
        fila.setPadding(new Insets(12));
        fila.setAlignment(Pos.CENTER_LEFT);

        Label lblPosicion = new Label(String.format("%2d", posicion));
        lblPosicion.getStyleClass().add("tarjeta-label-valor");
        lblPosicion.setPrefWidth(40);

        Label lblPiloto = new Label(resultado.getNombrePiloto());
        lblPiloto.getStyleClass().add("tarjeta-label-valor");
        lblPiloto.setStyle("-fx-font-size: 14px;");
        lblPiloto.setPrefWidth(180);

        Label lblEquipo = new Label(resultado.getEquipo());
        lblEquipo.getStyleClass().add("tarjeta-label-detalle");
        lblEquipo.setPrefWidth(150);

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        Label lblTiempo = new Label(
                resultado.isDnf() ? resultado.getEstadoFinal() : String.format("%.3fs", resultado.getTiempoVueltaSegundos())
        );
        lblTiempo.getStyleClass().add("tarjeta-label-valor");
        lblTiempo.setStyle("-fx-font-size: 14px; -fx-text-fill: " +
                (resultado.isDnf() ? "#e74c3c" : "#27ae60") + ";");

        fila.getChildren().addAll(lblPosicion, lblPiloto, lblEquipo, espaciador, lblTiempo);
        return fila;
    }

    public Scene getScene() {
        return scene;
    }
}
