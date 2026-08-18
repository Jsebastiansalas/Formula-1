package com.formula1.ui;

import com.formula1.gestor.GestorCircuitos;
import com.formula1.modelo.Circuito;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;

public class PantallaGestionCircuitos {
    private AppFX app;
    private GestorCircuitos gestorCircuitos;
    private Scene scene;
    private TableView<Circuito> tabla;
    private TextField txtBuscar;
    private ObservableList<Circuito> datosTabla;

    public PantallaGestionCircuitos(AppFX app, GestorCircuitos gestorCircuitos) {
        this.app = app;
        this.gestorCircuitos = gestorCircuitos;
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
        Label logoSub = new Label("GESTIÓN CIRCUITOS");
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

        Label titulo = new Label("🏁 GESTIÓN DE CIRCUITOS");
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
        txtBuscar.setPromptText("🔍 Buscar circuito por nombre o país...");
        txtBuscar.setPrefWidth(300);
        txtBuscar.getStyleClass().add("text-field");
        txtBuscar.textProperty().addListener((obs, old, nuevo) -> filtrarTabla(nuevo));

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        Button btnAgregar = new Button("➕ Agregar Circuito");
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

    private TableView<Circuito> crearTabla() {
        TableView<Circuito> tabla = new TableView<>();
        tabla.setItems(datosTabla);
        tabla.getStyleClass().add("table-view");

        TableColumn<Circuito, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colNombre.setPrefWidth(300);

        TableColumn<Circuito, String> colPais = new TableColumn<>("País");
        colPais.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPais()));
        colPais.setPrefWidth(150);

        TableColumn<Circuito, String> colLongitud = new TableColumn<>("Longitud (km)");
        colLongitud.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.format("%.2f km", data.getValue().getLongitudKm())
        ));
        colLongitud.setPrefWidth(120);

        TableColumn<Circuito, String> colVueltas = new TableColumn<>("Vueltas");
        colVueltas.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.valueOf(data.getValue().getVueltas())
        ));
        colVueltas.setPrefWidth(100);

        tabla.getColumns().addAll(colNombre, colPais, colLongitud, colVueltas);
        return tabla;
    }

    private void cargarDatos() {
        List<Circuito> circuitos = gestorCircuitos.obtenerTodosCircuitos();
        datosTabla.clear();
        datosTabla.addAll(circuitos);
    }

    private void filtrarTabla(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            cargarDatos();
            return;
        }

        String filtroLower = filtro.toLowerCase();
        List<Circuito> todosCircuitos = gestorCircuitos.obtenerTodosCircuitos();
        List<Circuito> filtrados = todosCircuitos.stream()
            .filter(c -> c.getNombre().toLowerCase().contains(filtroLower) ||
                        c.getPais().toLowerCase().contains(filtroLower))
            .toList();

        datosTabla.clear();
        datosTabla.addAll(filtrados);
    }

    private void mostrarDialogoAgregar() {
        Dialog<Circuito> dialog = new Dialog<>();
        dialog.setTitle("Agregar Circuito");
        dialog.setHeaderText("Complete los datos del nuevo circuito");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = crearFormularioCircuito(null);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                return extraerCircuitoDeFormulario(grid, null);
            }
            return null;
        });

        Optional<Circuito> resultado = dialog.showAndWait();
        resultado.ifPresent(circuito -> {
            gestorCircuitos.agregarCircuito(circuito);
            cargarDatos();
            mostrarMensaje("Circuito agregado (solo en memoria)", Alert.AlertType.INFORMATION);
        });
    }

    private void editarSeleccionado() {
        Circuito seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Dialog<Circuito> dialog = new Dialog<>();
        dialog.setTitle("Editar Circuito");
        dialog.setHeaderText("Modifique los datos del circuito");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = crearFormularioCircuito(seleccionado);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                return extraerCircuitoDeFormulario(grid, seleccionado);
            }
            return null;
        });

        Optional<Circuito> resultado = dialog.showAndWait();
        resultado.ifPresent(circuito -> {
            gestorCircuitos.actualizarCircuito(circuito);
            cargarDatos();
            mostrarMensaje("Circuito actualizado (solo en memoria)", Alert.AlertType.INFORMATION);
        });
    }

    private void eliminarSeleccionado() {
        Circuito seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar circuito?");
        confirmacion.setContentText("¿Está seguro de eliminar: " + seleccionado.getNombre() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            gestorCircuitos.eliminarCircuito(seleccionado.getNombre());
            cargarDatos();
            mostrarMensaje("Circuito eliminado (solo en memoria)", Alert.AlertType.INFORMATION);
        }
    }

    private GridPane crearFormularioCircuito(Circuito circuito) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtNombre = new TextField(circuito != null ? circuito.getNombre() : "");
        TextField txtPais = new TextField(circuito != null ? circuito.getPais() : "");
        TextField txtLongitud = new TextField(circuito != null ? String.valueOf(circuito.getLongitudKm()) : "");
        TextField txtVueltas = new TextField(circuito != null ? String.valueOf(circuito.getVueltas()) : "");

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("País:"), 0, 1);
        grid.add(txtPais, 1, 1);
        grid.add(new Label("Longitud (km):"), 0, 2);
        grid.add(txtLongitud, 1, 2);
        grid.add(new Label("Vueltas:"), 0, 3);
        grid.add(txtVueltas, 1, 3);

        return grid;
    }

    private Circuito extraerCircuitoDeFormulario(GridPane grid, Circuito circuitoExistente) {
        TextField txtNombre = (TextField) grid.getChildren().get(1);
        TextField txtPais = (TextField) grid.getChildren().get(3);
        TextField txtLongitud = (TextField) grid.getChildren().get(5);
        TextField txtVueltas = (TextField) grid.getChildren().get(7);

        if (circuitoExistente != null) {
            circuitoExistente.setNombre(txtNombre.getText());
            circuitoExistente.setPais(txtPais.getText());
            circuitoExistente.setLongitudKm(Double.parseDouble(txtLongitud.getText()));
            circuitoExistente.setVueltas(Integer.parseInt(txtVueltas.getText()));
            return circuitoExistente;
        } else {
            return new Circuito(
                txtNombre.getText(),
                txtPais.getText(),
                Double.parseDouble(txtLongitud.getText()),
                Integer.parseInt(txtVueltas.getText()),
                "",
                null
            );
        }
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
