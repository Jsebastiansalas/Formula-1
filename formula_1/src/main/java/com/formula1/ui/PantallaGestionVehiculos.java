package com.formula1.ui;

import com.formula1.gestor.GestorVehiculos;
import com.formula1.modelo.Vehiculo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;

public class PantallaGestionVehiculos {
    private AppFX app;
    private GestorVehiculos gestorVehiculos;
    private Scene scene;
    private TableView<Vehiculo> tabla;
    private TextField txtBuscar;
    private ObservableList<Vehiculo> datosTabla;

    public PantallaGestionVehiculos(AppFX app, GestorVehiculos gestorVehiculos) {
        this.app = app;
        this.gestorVehiculos = gestorVehiculos;
        this.datosTabla = FXCollections.observableArrayList();
        this.scene = crearEscena();
        cargarDatos();
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
        Label logoSub = new Label("GESTIÓN VEHÍCULOS");
        logoSub.getStyleClass().add("sidebar-logo-sub");

        VBox logoBox = new VBox(4, logo, logoSub);
        logoBox.setPadding(new Insets(30, 24, 30, 24));

        Region espaciador = new Region();
        VBox.setVgrow(espaciador, Priority.ALWAYS);

        Button btnVolver = new Button("← Volver");
        btnVolver.getStyleClass().add("nav-boton-salir");
        btnVolver.setMaxWidth(Double.MAX_VALUE);
        btnVolver.setOnAction(e -> app.mostrarAdministracion());

        VBox espaciadorInferior = new VBox();
        espaciadorInferior.setPadding(new Insets(16));
        espaciadorInferior.getChildren().add(btnVolver);

        sidebar.getChildren().addAll(logoBox, espaciador, espaciadorInferior);
        return sidebar;
    }

    private VBox crearContenido() {
        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(40));

        Label titulo = new Label("🏎️ GESTIÓN DE VEHÍCULOS");
        titulo.getStyleClass().add("hero-titulo");

        HBox barraHerramientas = crearBarraHerramientas();

        tabla = crearTabla();
        VBox.setVgrow(tabla, Priority.ALWAYS);

        contenido.getChildren().addAll(titulo, barraHerramientas, tabla);
        return contenido;
    }

    private HBox crearBarraHerramientas() {
        HBox barra = new HBox(15);
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setPadding(new Insets(10, 0, 10, 0));

        txtBuscar = new TextField();
        txtBuscar.setPromptText("🔍 Buscar vehículo por modelo o motor...");
        txtBuscar.setPrefWidth(300);
        txtBuscar.getStyleClass().add("text-field");
        txtBuscar.textProperty().addListener((obs, old, nuevo) -> filtrarTabla(nuevo));

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        Button btnAgregar = new Button("➕ Agregar Vehículo");
        btnAgregar.getStyleClass().add("boton-cta");
        btnAgregar.setOnAction(e -> mostrarDialogoAgregar());

        Button btnEditar = new Button("✏️ Editar");
        btnEditar.getStyleClass().add("button");
        btnEditar.setOnAction(e -> editarSeleccionado());
        btnEditar.disableProperty().bind(tabla.getSelectionModel().selectedItemProperty().isNull());

        Button btnEliminar = new Button("🗑️ Eliminar");
        btnEliminar.getStyleClass().add("button");
        btnEliminar.setOnAction(e -> eliminarSeleccionado());
        btnEliminar.disableProperty().bind(tabla.getSelectionModel().selectedItemProperty().isNull());

        barra.getChildren().addAll(txtBuscar, espaciador, btnAgregar, btnEditar, btnEliminar);
        return barra;
    }

    private TableView<Vehiculo> crearTabla() {
        TableView<Vehiculo> tabla = new TableView<>();
        tabla.setItems(datosTabla);
        tabla.getStyleClass().add("table-view");

        TableColumn<Vehiculo, String> colModelo = new TableColumn<>("Modelo");
        colModelo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getModelo()));
        colModelo.setPrefWidth(150);

        TableColumn<Vehiculo, String> colMotor = new TableColumn<>("Motor");
        colMotor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMotor()));
        colMotor.setPrefWidth(150);

        TableColumn<Vehiculo, String> colVelocidad = new TableColumn<>("Vel. Máx (km/h)");
        colVelocidad.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.valueOf(data.getValue().getVelocidadMaximaKmh())
        ));
        colVelocidad.setPrefWidth(150);

        TableColumn<Vehiculo, String> colAceleracion = new TableColumn<>("Acel. 0-100");
        colAceleracion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.format("%.2fs", data.getValue().getAceleracion0100())
        ));
        colAceleracion.setPrefWidth(150);

        tabla.getColumns().addAll(colModelo, colMotor, colVelocidad, colAceleracion);
        return tabla;
    }

    private void cargarDatos() {
        List<Vehiculo> vehiculos = gestorVehiculos.listarVehiculos();
        datosTabla.clear();
        datosTabla.addAll(vehiculos);
    }

    private void filtrarTabla(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            cargarDatos();
            return;
        }

        String filtroLower = filtro.toLowerCase();
        List<Vehiculo> todosVehiculos = gestorVehiculos.listarVehiculos();
        List<Vehiculo> filtrados = todosVehiculos.stream()
            .filter(v -> v.getModelo().toLowerCase().contains(filtroLower) ||
                        v.getMotor().toLowerCase().contains(filtroLower))
            .toList();

        datosTabla.clear();
        datosTabla.addAll(filtrados);
    }

    private void mostrarDialogoAgregar() {
        Dialog<Vehiculo> dialog = new Dialog<>();
        dialog.setTitle("Agregar Vehículo");
        dialog.setHeaderText("Complete los datos del nuevo vehículo");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = crearFormularioVehiculo(null);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                return extraerVehiculoDeFormulario(grid, null);
            }
            return null;
        });

        Optional<Vehiculo> resultado = dialog.showAndWait();
        resultado.ifPresent(vehiculo -> {
            gestorVehiculos.agregarVehiculo(vehiculo);
            cargarDatos();
            mostrarMensaje("Vehículo agregado (solo en memoria)", Alert.AlertType.INFORMATION);
        });
    }

    private void editarSeleccionado() {
        Vehiculo seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Dialog<Vehiculo> dialog = new Dialog<>();
        dialog.setTitle("Editar Vehículo");
        dialog.setHeaderText("Modifique los datos del vehículo");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = crearFormularioVehiculo(seleccionado);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                return extraerVehiculoDeFormulario(grid, seleccionado);
            }
            return null;
        });

        Optional<Vehiculo> resultado = dialog.showAndWait();
        resultado.ifPresent(vehiculo -> {
            gestorVehiculos.actualizarVehiculo(vehiculo);
            cargarDatos();
            mostrarMensaje("Vehículo actualizado (solo en memoria)", Alert.AlertType.INFORMATION);
        });
    }

    private void eliminarSeleccionado() {
        Vehiculo seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar vehículo?");
        confirmacion.setContentText("¿Está seguro de eliminar: " + seleccionado.getModelo() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            gestorVehiculos.eliminarVehiculo(seleccionado.getModelo());
            cargarDatos();
            mostrarMensaje("Vehículo eliminado (solo en memoria)", Alert.AlertType.INFORMATION);
        }
    }

    private GridPane crearFormularioVehiculo(Vehiculo vehiculo) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtModelo = new TextField(vehiculo != null ? vehiculo.getModelo() : "");
        TextField txtMotor = new TextField(vehiculo != null ? vehiculo.getMotor() : "");
        TextField txtVelocidad = new TextField(vehiculo != null ? String.valueOf(vehiculo.getVelocidadMaximaKmh()) : "");
        TextField txtAceleracion = new TextField(vehiculo != null ? String.valueOf(vehiculo.getAceleracion0100()) : "");

        grid.add(new Label("Modelo:"), 0, 0);
        grid.add(txtModelo, 1, 0);
        grid.add(new Label("Motor:"), 0, 1);
        grid.add(txtMotor, 1, 1);
        grid.add(new Label("Velocidad Máxima (km/h):"), 0, 2);
        grid.add(txtVelocidad, 1, 2);
        grid.add(new Label("Aceleración 0-100 (s):"), 0, 3);
        grid.add(txtAceleracion, 1, 3);

        return grid;
    }

    private Vehiculo extraerVehiculoDeFormulario(GridPane grid, Vehiculo vehiculoExistente) {
        TextField txtModelo = (TextField) grid.getChildren().get(1);
        TextField txtMotor = (TextField) grid.getChildren().get(3);
        TextField txtVelocidad = (TextField) grid.getChildren().get(5);
        TextField txtAceleracion = (TextField) grid.getChildren().get(7);

        Vehiculo vehiculo = vehiculoExistente != null ? vehiculoExistente : new Vehiculo();
        vehiculo.setModelo(txtModelo.getText());
        vehiculo.setMotor(txtMotor.getText());
        vehiculo.setVelocidadMaximaKmh(Integer.parseInt(txtVelocidad.getText()));
        vehiculo.setAceleracion0100(Double.parseDouble(txtAceleracion.getText()));

        return vehiculo;
    }

    private void mostrarMensaje(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}
