package com.formula1.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.formula1.almacenamiento.HistorialResultados;
import com.formula1.simulacion.GestorTemporada;
import com.formula1.simulacion.SimuladorClasificacion;
import com.formula1.simulacion.TablaCampeonato;

public class PantallaTemporadaCompleta {
    private AppFX app;
    private GestorTemporada gestorTemporada;
    private SimuladorClasificacion simulador;
    private HistorialResultados historial;
    private Scene scene;

    public PantallaTemporadaCompleta(AppFX app, GestorTemporada gestorTemporada,
                                     SimuladorClasificacion simulador, HistorialResultados historial) {
        this.app = app;
        this.gestorTemporada = gestorTemporada;
        this.simulador = simulador;
        this.historial = historial;
        this.scene = crearEscena();
    }

    private Scene crearEscena() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a1a;");

        // Header
        Label titulo = new Label("SIMULACIÓN DE TEMPORADA COMPLETA");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titulo.setStyle("-fx-text-fill: #e10600;");

        VBox header = new VBox(titulo);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));

        // Centro - Área de simulación
        VBox centro = new VBox(20);
        centro.setAlignment(Pos.CENTER);
        centro.setPadding(new Insets(40));

        Label info = new Label("Se simularán todas las carreras restantes de la temporada 2026");
        info.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(600);
        progressBar.setStyle("-fx-accent: #e10600;");

        Label progreso = new Label("Listo para simular");
        progreso.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 14px;");

        Button btnIniciar = new Button("🏆 INICIAR SIMULACIÓN");
        btnIniciar.setStyle("-fx-background-color: #e10600; -fx-text-fill: white; " +
                "-fx-font-size: 20px; -fx-padding: 15px 50px; -fx-background-radius: 10;");
        btnIniciar.setOnAction(e -> {
            btnIniciar.setDisable(true);
            simularTemporada(progressBar, progreso, root);
        });

        centro.getChildren().addAll(info, progressBar, progreso, btnIniciar);

        root.setTop(header);
        root.setCenter(centro);

        return new Scene(root, 1000, 700);
    }

    private void simularTemporada(ProgressBar progressBar, Label progreso, BorderPane root) {
        new Thread(() -> {
            int totalCarreras = gestorTemporada.getCalendario().size();
            int carreraInicial = gestorTemporada.getCarreraActual();
            int carrerasASimular = totalCarreras - carreraInicial;

            int totalDNFs = 0;
            int totalAccidentes = 0;
            int totalFallosMecanicos = 0;

            for (int i = 0; i < carrerasASimular; i++) {
                int carreraNumero = carreraInicial + i + 1;

                Platform.runLater(() -> progreso.setText("Simulando carrera " + carreraNumero + " de " + totalCarreras + "..."));

                GestorTemporada.ResultadoCarrera resultado = gestorTemporada.simularSiguienteCarrera(simulador);

                if (resultado != null) {
                    totalDNFs += resultado.getTotalDNF();
                    totalAccidentes += resultado.getTotalAccidentes();
                    totalFallosMecanicos += resultado.getTotalFallosMecanicos();

                    historial.guardarSesion(resultado.getCircuito(), resultado.getClima(),
                            resultado.getClasificacion());
                }

                double progresoPorcentaje = (double) (i + 1) / carrerasASimular;
                Platform.runLater(() -> progressBar.setProgress(progresoPorcentaje));

                try {
                    Thread.sleep(200); // Pausa para que se vea el progreso
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int finalTotalDNFs = totalDNFs;
            int finalTotalAccidentes = totalAccidentes;
            int finalTotalFallosMecanicos = totalFallosMecanicos;

            Platform.runLater(() -> {
                progreso.setText("¡Temporada completada!");
                mostrarResultadosFinales(root, finalTotalDNFs, finalTotalAccidentes, finalTotalFallosMecanicos);
            });

        }).start();
    }

    private void mostrarResultadosFinales(BorderPane root, int totalDNFs, int totalAccidentes, int totalFallosMecanicos) {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #1a1a1a; -fx-background-color: #1a1a1a;");

        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: #1a1a1a;");

        // Título
        Label titulo = new Label("🏁 ¡TEMPORADA 2026 FINALIZADA! 🏁");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titulo.setStyle("-fx-text-fill: #e10600;");

        // Estadísticas de la temporada
        VBox stats = crearEstadisticas(totalDNFs, totalAccidentes, totalFallosMecanicos);

        // Campeón
        TablaCampeonato.PilotoCampeonato campeon = gestorTemporada.getCampeonato().obtenerCampeon();
        VBox campeonBox = crearBoxCampeon(campeon);

        // Top 3
        VBox top3Box = crearTop3();

        // Botones
        Button btnVerCampeonato = new Button("Ver Tabla Completa");
        btnVerCampeonato.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-padding: 10px 30px; -fx-background-radius: 5;");
        btnVerCampeonato.setOnAction(e -> {
            PantallaCampeonato pantalla = new PantallaCampeonato(app, gestorTemporada);
            app.getPrimaryStage().setScene(pantalla.getScene());
        });

        Button btnVerHistorial = new Button("Ver Historial");
        btnVerHistorial.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-padding: 10px 30px; -fx-background-radius: 5;");
        btnVerHistorial.setOnAction(e -> {
            PantallaHistorial pantalla = new PantallaHistorial(app, gestorTemporada);
            app.getPrimaryStage().setScene(pantalla.getScene());
        });

        Button btnVolver = new Button("Volver al Menú");
        btnVolver.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-padding: 10px 30px; -fx-background-radius: 5;");
        btnVolver.setOnAction(e -> app.volverAlInicio());

        HBox botones = new HBox(15, btnVerCampeonato, btnVerHistorial, btnVolver);
        botones.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(titulo, stats, campeonBox, top3Box, new Separator(), botones);
        scroll.setContent(contenido);

        root.setCenter(scroll);
    }

    private VBox crearEstadisticas(int totalDNFs, int totalAccidentes, int totalFallosMecanicos) {
        int totalCarreras = gestorTemporada.getHistorialCarreras().size();
        double promedioDNF = totalCarreras > 0 ? (double) totalDNFs / totalCarreras : 0;
        double tasaFinalizacion = totalCarreras > 0 ?
                ((22.0 * totalCarreras - totalDNFs) / (22.0 * totalCarreras)) * 100 : 0;

        Label titulo = new Label("📊 ESTADÍSTICAS DE LA TEMPORADA");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titulo.setStyle("-fx-text-fill: white;");

        Label carreras = new Label("Carreras disputadas: " + totalCarreras);
        Label dnfs = new Label("Total abandonos (DNF): " + totalDNFs);
        Label accidentes = new Label("- Por accidentes: " + totalAccidentes);
        Label fallos = new Label("- Por fallos mecánicos: " + totalFallosMecanicos);
        Label promedio = new Label(String.format("Promedio DNF por carrera: %.2f", promedioDNF));
        Label tasa = new Label(String.format("Tasa de finalización: %.1f%%", tasaFinalizacion));

        carreras.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");
        dnfs.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");
        accidentes.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");
        fallos.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");
        promedio.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");
        tasa.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");

        VBox box = new VBox(8, titulo, carreras, dnfs, accidentes, fallos, promedio, tasa);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 10;");

        return box;
    }

    private VBox crearBoxCampeon(TablaCampeonato.PilotoCampeonato campeon) {
        Label titulo = new Label("🏆 CAMPEÓN DEL MUNDO 2026 🏆");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titulo.setStyle("-fx-text-fill: #f39c12;");

        Label nombre = new Label(campeon.getNombre());
        nombre.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        nombre.setStyle("-fx-text-fill: white;");

        Label equipo = new Label(campeon.getEquipo());
        equipo.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        equipo.setStyle("-fx-text-fill: #95a5a6;");

        Label puntos = new Label(campeon.getPuntosTotal() + " puntos");
        puntos.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        puntos.setStyle("-fx-text-fill: #e10600;");

        VBox box = new VBox(10, titulo, nombre, equipo, puntos);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(30));
        box.setStyle("-fx-background-color: linear-gradient(to bottom, #8e44ad, #2c3e50); " +
                "-fx-background-radius: 15; -fx-border-color: #f39c12; -fx-border-width: 3; " +
                "-fx-border-radius: 15;");

        return box;
    }

    private VBox crearTop3() {
        var clasificacion = gestorTemporada.getCampeonato().obtenerClasificacion();

        Label titulo = new Label("🥇🥈🥉 PODIO DEL CAMPEONATO");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titulo.setStyle("-fx-text-fill: white;");

        VBox lista = new VBox(10);

        for (int i = 0; i < Math.min(3, clasificacion.size()); i++) {
            var piloto = clasificacion.get(i);
            String medalla = i == 0 ? "🥇" : (i == 1 ? "🥈" : "🥉");
            String colorFondo = i == 0 ? "#f39c12" : (i == 1 ? "#95a5a6" : "#cd7f32");

            Label fila = new Label(String.format("%s P%d: %s (%s) - %d pts",
                    medalla, (i + 1), piloto.getNombre(), piloto.getEquipo(), piloto.getPuntosTotal()));
            fila.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; " +
                    "-fx-background-color: " + colorFondo + "; -fx-background-radius: 5;");

            lista.getChildren().add(fila);
        }

        VBox box = new VBox(10, titulo, lista);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #34495e; -fx-background-radius: 10;");

        return box;
    }

    public Scene getScene() {
        return scene;
    }
}
