package com.formula1.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class PantallaAdministracion {
    private AppFX app;
    private Scene scene;

    public PantallaAdministracion(AppFX app) {
        this.app = app;
        this.scene = crearEscena();
    }

    private Scene crearEscena() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-screen");

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
        Label logoSub = new Label("ADMINISTRACIÓN");
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
        VBox contenido = new VBox(30);
        contenido.setPadding(new Insets(40));
        contenido.setAlignment(Pos.TOP_CENTER);

        Label titulo = new Label("⚙️ ADMINISTRACIÓN DEL SISTEMA");
        titulo.getStyleClass().add("hero-titulo");

        Label subtitulo = new Label("Gestiona circuitos, pilotos y vehículos");
        subtitulo.getStyleClass().add("hero-subtitulo");

        VBox header = new VBox(10, titulo, subtitulo);
        header.setAlignment(Pos.CENTER);

        HBox tarjetas = new HBox(20);
        tarjetas.setAlignment(Pos.CENTER);
        tarjetas.setPadding(new Insets(30, 0, 0, 0));

        VBox tarjetaCircuitos = crearTarjetaAdmin(
            "🏁",
            "CIRCUITOS",
            "Agregar, editar y eliminar circuitos del calendario",
            e -> app.mostrarGestionCircuitos()
        );

        VBox tarjetaPilotos = crearTarjetaAdmin(
            "👤",
            "PILOTOS",
            "Gestionar pilotos y sus atributos",
            e -> app.mostrarGestionPilotos()
        );

        VBox tarjetaVehiculos = crearTarjetaAdmin(
            "🏎️",
            "VEHÍCULOS",
            "Administrar vehículos y configuraciones",
            e -> app.mostrarGestionVehiculos()
        );

        tarjetas.getChildren().addAll(tarjetaCircuitos, tarjetaPilotos, tarjetaVehiculos);

        contenido.getChildren().addAll(header, tarjetas);
        return contenido;
    }

    private VBox crearTarjetaAdmin(String icono, String titulo, String descripcion, javafx.event.EventHandler<javafx.event.ActionEvent> accion) {
        VBox tarjeta = new VBox(15);
        tarjeta.getStyleClass().add("tarjeta");
        tarjeta.setPadding(new Insets(30));
        tarjeta.setPrefWidth(300);
        tarjeta.setAlignment(Pos.CENTER);

        Label lblIcono = new Label(icono);
        lblIcono.setStyle("-fx-font-size: 48px;");

        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("tarjeta-label-valor");
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

        Label lblDescripcion = new Label(descripcion);
        lblDescripcion.getStyleClass().add("tarjeta-label-detalle");
        lblDescripcion.setWrapText(true);
        lblDescripcion.setMaxWidth(250);
        lblDescripcion.setAlignment(Pos.CENTER);
        lblDescripcion.setStyle("-fx-text-fill: #94a3b8; -fx-text-alignment: center;");

        Button btnAcceder = new Button("Gestionar");
        btnAcceder.getStyleClass().add("boton-cta");
        btnAcceder.setOnAction(accion);

        tarjeta.getChildren().addAll(lblIcono, lblTitulo, lblDescripcion, btnAcceder);
        return tarjeta;
    }

    public Scene getScene() {
        return scene;
    }
}
