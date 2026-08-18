package com.formula1.ui;

import com.formula1.simulacion.GestorTemporada;
import com.formula1.simulacion.TablaCampeonato;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

/**
 * Pantalla de estadisticas detalladas de la temporada.
 * Presenta cuatro secciones: estadisticas generales (carreras completadas/pendientes),
 * top 10 pilotos con puntos, clasificacion por equipos (placeholder),
 * y resultados por circuito (ganador, clima, DNFs de cada carrera).
 */
public class PantallaEstadisticas {
    private AppFX app;
    private GestorTemporada gestorTemporada;
    private Scene scene;

    public PantallaEstadisticas(AppFX app, GestorTemporada gestorTemporada) {
        this.app = app;
        this.gestorTemporada = gestorTemporada;
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
        Label logoSub = new Label("ESTADÍSTICAS");
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

        Label titulo = new Label("ESTADÍSTICAS DETALLADAS");
        titulo.getStyleClass().add("hero-titulo");
        titulo.setAlignment(Pos.CENTER);

        // Crear secciones de estadísticas
        VBox seccionGeneral = crearSeccionEstadisticasGenerales();
        VBox seccionPilotos = crearSeccionEstadisticasPilotos();
        VBox seccionEquipos = crearSeccionEstadisticasEquipos();
        VBox seccionCircuitos = crearSeccionEstadisticasCircuitos();

        ScrollPane scroll = new ScrollPane();
        scroll.setContent(new VBox(30, titulo, seccionGeneral, seccionPilotos, seccionEquipos, seccionCircuitos));
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");

        VBox.setVgrow(scroll, Priority.ALWAYS);
        contenido.getChildren().add(scroll);

        return contenido;
    }

    private VBox crearSeccionEstadisticasGenerales() {
        VBox seccion = new VBox(15);
        seccion.getStyleClass().add("stats-box");

        Label tituloSeccion = new Label("📊 ESTADÍSTICAS GENERALES");
        tituloSeccion.getStyleClass().add("hero-subtitulo");

        int carrerasCompletadas = gestorTemporada.getCarreraActual();
        int carrerasPendientes = 24 - carrerasCompletadas;

        HBox tarjetas = new HBox(15);
        tarjetas.setAlignment(Pos.CENTER);

        tarjetas.getChildren().addAll(
                crearTarjetaEstadistica("CARRERAS COMPLETADAS", String.valueOf(carrerasCompletadas), "de 24 totales"),
                crearTarjetaEstadistica("CARRERAS PENDIENTES", String.valueOf(carrerasPendientes), "por disputar"),
                crearTarjetaEstadistica("PROGRESO", String.format("%.1f%%", (carrerasCompletadas * 100.0 / 24)), "de temporada")
        );

        seccion.getChildren().addAll(tituloSeccion, tarjetas);
        return seccion;
    }

    private VBox crearSeccionEstadisticasPilotos() {
        VBox seccion = new VBox(15);
        seccion.getStyleClass().add("stats-box");

        Label tituloSeccion = new Label("🏎️ TOP 10 PILOTOS");
        tituloSeccion.getStyleClass().add("hero-subtitulo");

        TablaCampeonato campeonato = gestorTemporada.getCampeonato();
        List<TablaCampeonato.PilotoCampeonato> clasificacion = campeonato.obtenerClasificacion();

        VBox listaPilotos = new VBox(10);

        int limit = Math.min(10, clasificacion.size());
        for (int i = 0; i < limit; i++) {
            TablaCampeonato.PilotoCampeonato piloto = clasificacion.get(i);
            HBox filaPiloto = crearFilaEstadistica(
                    (i + 1) + ". " + piloto.getNombre(),
                    piloto.getPuntosTotal() + " pts",
                    i < 3
            );

            if (i == 0) filaPiloto.getStyleClass().add("list-row-gold");
            else if (i == 1) filaPiloto.getStyleClass().add("list-row-silver");
            else if (i == 2) filaPiloto.getStyleClass().add("list-row-bronze");
            else if (i < 10) filaPiloto.getStyleClass().add("list-row-points");

            listaPilotos.getChildren().add(filaPiloto);
        }

        seccion.getChildren().addAll(tituloSeccion, listaPilotos);
        return seccion;
    }

    private VBox crearSeccionEstadisticasEquipos() {
        VBox seccion = new VBox(15);
        seccion.getStyleClass().add("stats-box");

        Label tituloSeccion = new Label("🏆 CLASIFICACIÓN DE EQUIPOS");
        tituloSeccion.getStyleClass().add("hero-subtitulo");

        // Por ahora mostramos un mensaje indicando que se agregará en el futuro
        Label mensaje = new Label("Estadísticas de equipos próximamente");
        mensaje.getStyleClass().add("tarjeta-label-detalle");

        seccion.getChildren().addAll(tituloSeccion, mensaje);
        return seccion;
    }

    private VBox crearSeccionEstadisticasCircuitos() {
        VBox seccion = new VBox(15);
        seccion.getStyleClass().add("stats-box");

        Label tituloSeccion = new Label("🗺️ RESULTADOS POR CIRCUITO");
        tituloSeccion.getStyleClass().add("hero-subtitulo");

        List<GestorTemporada.ResultadoCarrera> historial = gestorTemporada.getHistorialCarreras();

        VBox listaCircuitos = new VBox(10);

        for (GestorTemporada.ResultadoCarrera resultado : historial) {
            VBox filaCircuito = crearFilaCircuito(resultado);
            listaCircuitos.getChildren().add(filaCircuito);
        }

        if (historial.isEmpty()) {
            Label sinDatos = new Label("No hay carreras completadas aún");
            sinDatos.getStyleClass().add("tarjeta-label-detalle");
            listaCircuitos.getChildren().add(sinDatos);
        }

        seccion.getChildren().addAll(tituloSeccion, listaCircuitos);
        return seccion;
    }

    private VBox crearTarjetaEstadistica(String etiqueta, String valor, String detalle) {
        VBox tarjeta = new VBox(8);
        tarjeta.getStyleClass().add("tarjeta");
        tarjeta.setPadding(new Insets(24));
        tarjeta.setPrefWidth(220);
        tarjeta.setAlignment(Pos.CENTER_LEFT);

        Label lblEtiqueta = new Label(etiqueta);
        lblEtiqueta.getStyleClass().add("tarjeta-label-titulo");

        Label lblValor = new Label(valor);
        lblValor.getStyleClass().add("tarjeta-label-valor");

        Label lblDetalle = new Label(detalle);
        lblDetalle.getStyleClass().add("tarjeta-label-detalle");

        tarjeta.getChildren().addAll(lblEtiqueta, lblValor, lblDetalle);
        return tarjeta;
    }

    private HBox crearFilaEstadistica(String nombre, String puntos, boolean destacar) {
        HBox fila = new HBox(15);
        fila.getStyleClass().add("list-row");
        fila.setPadding(new Insets(16));
        fila.setAlignment(Pos.CENTER_LEFT);

        Label lblNombre = new Label(nombre);
        lblNombre.getStyleClass().add("tarjeta-label-valor");
        lblNombre.setStyle("-fx-font-size: 16px;");

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        Label lblPuntos = new Label(puntos);
        lblPuntos.getStyleClass().add("tarjeta-label-valor");
        lblPuntos.setStyle("-fx-font-size: 18px; -fx-text-fill: #e10600;");

        fila.getChildren().addAll(lblNombre, espaciador, lblPuntos);
        return fila;
    }

    private VBox crearFilaCircuito(GestorTemporada.ResultadoCarrera resultado) {
        VBox fila = new VBox(8);
        fila.getStyleClass().add("list-row");
        fila.setPadding(new Insets(16));

        Label lblCircuito = new Label("🏁 " + resultado.getCircuito().getNombre());
        lblCircuito.getStyleClass().add("tarjeta-label-valor");
        lblCircuito.setStyle("-fx-font-size: 16px;");

        Label lblGanador = new Label("🏆 Ganador: " + resultado.getGanador());
        lblGanador.getStyleClass().add("tarjeta-label-detalle");

        HBox estadisticas = new HBox(20);
        estadisticas.getChildren().addAll(
                new Label("🌤️ " + resultado.getClima()),
                new Label("✅ " + resultado.getTotalFinalizados() + " finalizados"),
                new Label("❌ " + resultado.getTotalDNF() + " DNF"),
                new Label("💥 " + resultado.getTotalAccidentes() + " accidentes")
        );

        for (javafx.scene.Node node : estadisticas.getChildren()) {
            ((Label) node).getStyleClass().add("tarjeta-label-detalle");
        }

        fila.getChildren().addAll(lblCircuito, lblGanador, estadisticas);
        return fila;
    }

    public Scene getScene() {
        return scene;
    }
}
