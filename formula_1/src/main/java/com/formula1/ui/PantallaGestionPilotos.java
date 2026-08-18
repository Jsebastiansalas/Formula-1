package com.formula1.ui;

import com.formula1.gestor.GestorPilotos;
import com.formula1.modelo.Piloto;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;

public class PantallaGestionPilotos {
    private AppFX app;
    private GestorPilotos gestorPilotos;
    private Scene scene;
    private TableView<Piloto> tabla;
    private TextField txtBuscar;
    private ObservableList<Piloto> datosTabla;

    public PantallaGestionPilotos(AppFX app, GestorPilotos gestorPilotos) {
        this.app = app;
        this.gestorPilotos = gestorPilotos;
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
        Label logoSub = new Label("GESTIÓN PILOTOS");
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

        Label titulo = new Label("👤 GESTIÓN DE PILOTOS");
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
        txtBuscar.setPromptText("🔍 Buscar piloto por nombre o equipo...");
        txtBuscar.setPrefWidth(300);
        txtBuscar.getStyleClass().add("text-field");
        txtBuscar.textProperty().addListener((obs, old, nuevo) -> filtrarTabla(nuevo));

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        Button btnAgregar = new Button("➕ Agregar Piloto");
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

    private TableView<Piloto> crearTabla() {
        TableView<Piloto> tabla = new TableView<>();
        tabla.setItems(datosTabla);
        tabla.getStyleClass().add("table-view");

        TableColumn<Piloto, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getId())));
        colId.setPrefWidth(50);

        TableColumn<Piloto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colNombre.setPrefWidth(200);

        TableColumn<Piloto, String> colEquipo = new TableColumn<>("Equipo");
        colEquipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEquipo()));
        colEquipo.setPrefWidth(150);

        TableColumn<Piloto, String> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRol()));
        colRol.setPrefWidth(100);

        TableColumn<Piloto, String> colExperiencia = new TableColumn<>("Experiencia");
        colExperiencia.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getExperienciaAnios() + " años"));
        colExperiencia.setPrefWidth(100);

        TableColumn<Piloto, String> colHabilidad = new TableColumn<>("Habilidad");
        colHabilidad.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getHabilidad())));
        colHabilidad.setPrefWidth(100);

        tabla.getColumns().addAll(colId, colNombre, colEquipo, colRol, colExperiencia, colHabilidad);
        return tabla;
    }

    private void cargarDatos() {
        List<Piloto> pilotos = gestorPilotos.listarPilotos();
        datosTabla.clear();
        datosTabla.addAll(pilotos);
    }

    private void filtrarTabla(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            cargarDatos();
            return;
        }

        String filtroLower = filtro.toLowerCase();
        List<Piloto> todosPilotos = gestorPilotos.listarPilotos();
        List<Piloto> filtrados = todosPilotos.stream()
            .filter(p -> p.getNombre().toLowerCase().contains(filtroLower) ||
                        p.getEquipo().toLowerCase().contains(filtroLower))
            .toList();

        datosTabla.clear();
        datosTabla.addAll(filtrados);
    }

    private void mostrarDialogoAgregar() {
        Dialog<Piloto> dialog = new Dialog<>();
        dialog.setTitle("Agregar Piloto");
        dialog.setHeaderText("Complete los datos del nuevo piloto");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = crearFormularioPiloto(null);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                return extraerPilotoDeFormulario(grid, null);
            }
            return null;
        });

        Optional<Piloto> resultado = dialog.showAndWait();
        resultado.ifPresent(piloto -> {
            gestorPilotos.agregarPiloto(piloto);
            cargarDatos();
            mostrarMensaje("Piloto agregado (solo en memoria)", Alert.AlertType.INFORMATION);
        });
    }

    private void editarSeleccionado() {
        Piloto seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Dialog<Piloto> dialog = new Dialog<>();
        dialog.setTitle("Editar Piloto");
        dialog.setHeaderText("Modifique los datos del piloto");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = crearFormularioPiloto(seleccionado);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                return extraerPilotoDeFormulario(grid, seleccionado);
            }
            return null;
        });

        Optional<Piloto> resultado = dialog.showAndWait();
        resultado.ifPresent(piloto -> {
            gestorPilotos.actualizarPiloto(piloto);
            cargarDatos();
            mostrarMensaje("Piloto actualizado (solo en memoria)", Alert.AlertType.INFORMATION);
        });
    }

    private void eliminarSeleccionado() {
        Piloto seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar piloto?");
        confirmacion.setContentText("¿Está seguro de eliminar: " + seleccionado.getNombre() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            gestorPilotos.eliminarPiloto(seleccionado.getId());
            cargarDatos();
            mostrarMensaje("Piloto eliminado (solo en memoria)", Alert.AlertType.INFORMATION);
        }
    }

    private GridPane crearFormularioPiloto(Piloto piloto) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtId = new TextField(piloto != null ? String.valueOf(piloto.getId()) : "");
        TextField txtNombre = new TextField(piloto != null ? piloto.getNombre() : "");
        TextField txtEquipo = new TextField(piloto != null ? piloto.getEquipo() : "");
        TextField txtRol = new TextField(piloto != null ? piloto.getRol() : "");
        TextField txtExperiencia = new TextField(piloto != null ? String.valueOf(piloto.getExperienciaAnios()) : "");
        TextField txtHabilidad = new TextField(piloto != null ? String.valueOf(piloto.getHabilidad()) : "");

        if (piloto != null) {
            txtId.setDisable(true);
        }

        grid.add(new Label("ID:"), 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(txtNombre, 1, 1);
        grid.add(new Label("Equipo:"), 0, 2);
        grid.add(txtEquipo, 1, 2);
        grid.add(new Label("Rol:"), 0, 3);
        grid.add(txtRol, 1, 3);
        grid.add(new Label("Experiencia (años):"), 0, 4);
        grid.add(txtExperiencia, 1, 4);
        grid.add(new Label("Habilidad:"), 0, 5);
        grid.add(txtHabilidad, 1, 5);

        return grid;
    }

    private Piloto extraerPilotoDeFormulario(GridPane grid, Piloto pilotoExistente) {
        TextField txtId = (TextField) grid.getChildren().get(1);
        TextField txtNombre = (TextField) grid.getChildren().get(3);
        TextField txtEquipo = (TextField) grid.getChildren().get(5);
        TextField txtRol = (TextField) grid.getChildren().get(7);
        TextField txtExperiencia = (TextField) grid.getChildren().get(9);
        TextField txtHabilidad = (TextField) grid.getChildren().get(11);

        Piloto piloto = pilotoExistente != null ? pilotoExistente : new Piloto();
        if (pilotoExistente == null) {
            piloto.setId(Integer.parseInt(txtId.getText()));
        }
        piloto.setNombre(txtNombre.getText());
        piloto.setEquipo(txtEquipo.getText());
        piloto.setRol(txtRol.getText());
        piloto.setExperienciaAnios(Integer.parseInt(txtExperiencia.getText()));
        piloto.setHabilidad(Integer.parseInt(txtHabilidad.getText()));

        return piloto;
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
