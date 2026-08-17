package com.formula1.ui;

import com.formula1.almacenamiento.HistorialResultados;
import com.formula1.gestor.GestorCircuitos;
import com.formula1.modelo.Circuito;
import com.formula1.simulacion.GestorTemporada;
import com.formula1.simulacion.SimuladorClasificacion;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AppFX extends Application {
    private GestorCircuitos gestorCircuitos;
    private GestorTemporada gestorTemporada;
    private SimuladorClasificacion simulador;
    private HistorialResultados historial;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        inicializarComponentes();

        Scene scene = crearPantallaInicio();

        stage.setTitle("Simulador F1 - Temporada 2026");
        stage.setScene(scene);
        stage.setWidth(1100);
        stage.setHeight(720);
        stage.show();
    }

    private void inicializarComponentes() {
        gestorCircuitos = new GestorCircuitos();
        gestorCircuitos.cargarDesdeJSON("data/circuitos.json");

        gestorTemporada = new GestorTemporada(2026);
        gestorTemporada.cargarCalendario(gestorCircuitos);

        simulador = new SimuladorClasificacion();
        historial = new HistorialResultados();
    }

    private Scene crearPantallaInicio() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-screen");

        // Franja superior tipo bandera a cuadros
        Region franja = new Region();
        franja.getStyleClass().add("franja-cuadros");
        franja.setPrefHeight(6);

        VBox sidebar = crearSidebar();
        VBox contenidoCentral = crearContenidoCentral();

        VBox contenedorDerecho = new VBox(franja, contenidoCentral);
        VBox.setVgrow(contenidoCentral, Priority.ALWAYS);

        root.setLeft(sidebar);
        root.setCenter(contenedorDerecho);

        Scene scene = new Scene(root);
        Estilos.aplicar(scene);

        // Animación de entrada suave
        FadeTransition fade = new FadeTransition(Duration.millis(500), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        return scene;
    }

    private VBox crearSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(240);
        sidebar.setMinWidth(240);

        Label logo = new Label("F1 SIMULATOR");
        logo.getStyleClass().add("sidebar-logo");
        Label logoSub = new Label("TEMPORADA 2026");
        logoSub.getStyleClass().add("sidebar-logo-sub");

        VBox logoBox = new VBox(4, logo, logoSub);
        logoBox.setPadding(new Insets(30, 24, 30, 24));

        Button btnSimularCarrera = crearBotonNav("🏁  Simular Siguiente Carrera");
        btnSimularCarrera.setOnAction(e -> mostrarPantallaSimulacion());

        Button btnSimularTemporada = crearBotonNav("🏆  Simular Temporada Completa");
        btnSimularTemporada.setOnAction(e -> simularTemporadaCompleta());

        Button btnVerCalendario = crearBotonNav("📅  Calendario");
        btnVerCalendario.setOnAction(e -> mostrarCalendario());

        Button btnVerCampeonato = crearBotonNav("🏅  Tabla de Campeonato");
        btnVerCampeonato.setOnAction(e -> mostrarTablaCampeonato());

        Button btnHistorial = crearBotonNav("📊  Historial de Carreras");
        btnHistorial.setOnAction(e -> mostrarHistorial());

        VBox navBox = new VBox(2, btnSimularCarrera, btnSimularTemporada,
                btnVerCalendario, btnVerCampeonato, btnHistorial);

        Region espaciador = new Region();
        VBox.setVgrow(espaciador, Priority.ALWAYS);

        Button btnSalir = new Button("✕  Salir");
        btnSalir.getStyleClass().add("nav-boton-salir");
        btnSalir.setMaxWidth(Double.MAX_VALUE);
        btnSalir.setOnAction(e -> primaryStage.close());

        sidebar.getChildren().addAll(logoBox, navBox, espaciador, btnSalir);
        return sidebar;
    }

    private Button crearBotonNav(String texto) {
        Button btn = new Button(texto);
        btn.getStyleClass().add("nav-boton");
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private VBox crearContenidoCentral() {
        VBox contenido = new VBox(30);
        contenido.setAlignment(Pos.CENTER);
        contenido.setPadding(new Insets(50));

        Label titulo = new Label("BIENVENIDO AL SIMULADOR");
        titulo.getStyleClass().add("hero-titulo");

        Label subtitulo = new Label("VIVE CADA CARRERA DE LA TEMPORADA 2026");
        subtitulo.getStyleClass().add("hero-subtitulo");

        VBox header = new VBox(12, titulo, subtitulo);
        header.setAlignment(Pos.CENTER);

        HBox tarjetas = crearTarjetasResumen();

        Button btnComenzar = new Button("COMENZAR SIMULACIÓN →");
        btnComenzar.getStyleClass().add("boton-cta");
        btnComenzar.setOnAction(e -> mostrarPantallaSimulacion());

        contenido.getChildren().addAll(header, tarjetas, btnComenzar);
        return contenido;
    }

    private HBox crearTarjetasResumen() {
    HBox tarjetas = new HBox(20);
    tarjetas.setAlignment(Pos.CENTER);

    Circuito proximo = gestorTemporada.getSiguienteCircuito();
    String nombreCircuito = proximo != null ? proximo.getNombre() : "Temporada finalizada";
    String paisCircuito = proximo != null ? proximo.getPais() : "-";

    tarjetas.getChildren().addAll(
            crearTarjeta("PRÓXIMA CARRERA", nombreCircuito, paisCircuito),
            crearTarjeta("CARRERAS TOTALES", "24", "Calendario 2026"),
            crearTarjeta("EQUIPOS", "11", "22 pilotos en pista")
    );
    return tarjetas;
}
    private VBox crearTarjeta(String etiqueta, String valor, String detalle) {
        VBox tarjeta = new VBox(8);
        tarjeta.getStyleClass().add("tarjeta");
        tarjeta.setPadding(new Insets(24));
        tarjeta.setPrefWidth(240);
        tarjeta.setAlignment(Pos.CENTER_LEFT);

        Label lblEtiqueta = new Label(etiqueta);
        lblEtiqueta.getStyleClass().add("tarjeta-label-titulo");

        Label lblValor = new Label(valor);
        lblValor.getStyleClass().add("tarjeta-label-valor");
        lblValor.setWrapText(true);

        Label lblDetalle = new Label(detalle);
        lblDetalle.getStyleClass().add("tarjeta-label-detalle");

        tarjeta.getChildren().addAll(lblEtiqueta, lblValor, lblDetalle);
        return tarjeta;
    }

    private void mostrarPantallaSimulacion() {
        PantallaSimulacion pantallaSimulacion = new PantallaSimulacion(
            this, gestorTemporada, simulador, historial
        );
        primaryStage.setScene(pantallaSimulacion.getScene());
    }

    private void simularTemporadaCompleta() {
        PantallaTemporadaCompleta pantallaTemporada = new PantallaTemporadaCompleta(
            this, gestorTemporada, simulador, historial
        );
        primaryStage.setScene(pantallaTemporada.getScene());
    }

    private void mostrarCalendario() {
        PantallaCalendario pantallaCalendario = new PantallaCalendario(this, gestorTemporada);
        primaryStage.setScene(pantallaCalendario.getScene());
    }

    private void mostrarTablaCampeonato() {
        PantallaCampeonato pantallaCampeonato = new PantallaCampeonato(this, gestorTemporada);
        primaryStage.setScene(pantallaCampeonato.getScene());
    }

    private void mostrarHistorial() {
        PantallaHistorial pantallaHistorial = new PantallaHistorial(this, gestorTemporada);
        primaryStage.setScene(pantallaHistorial.getScene());
    }

    public void volverAlInicio() {
        primaryStage.setScene(crearPantallaInicio());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
