package com.formula1.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.formula1.simulacion.GestorTemporada;
import com.formula1.simulacion.TablaCampeonato;

import java.util.List;

public class PantallaCampeonato {
    private AppFX app;
    private GestorTemporada gestorTemporada;
    private Scene scene;

    public PantallaCampeonato(AppFX app, GestorTemporada gestorTemporada) {
        this.app = app;
        this.gestorTemporada = gestorTemporada;
        this.scene = crearEscena();
    }

    private Scene crearEscena() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a1a;");

        // Header
        Label titulo = new Label("🏅 TABLA DE CAMPEONATO 2026");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titulo.setStyle("-fx-text-fill: #e10600;");

        Button btnVolver = new Button("⬅ Volver");
        btnVolver.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 5;");
        btnVolver.setOnAction(e -> app.volverAlInicio());

        HBox header = new HBox(20, btnVolver, titulo);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));

        // Scroll para la tabla
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #1a1a1a; -fx-background-color: #1a1a1a;");

        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(20));

        // Info de carreras completadas
        int carrerasCompletadas = gestorTemporada.getCarreraActual();
        Label info = new Label(String.format("Carreras completadas: %d / %d",
                carrerasCompletadas, gestorTemporada.getCalendario().size()));
        info.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 16px;");

        // Encabezado de tabla
        HBox encabezado = crearEncabezado();

        // Lista de pilotos
        VBox listaPilotos = new VBox(5);
        List<TablaCampeonato.PilotoCampeonato> clasificacion =
                gestorTemporada.getCampeonato().obtenerClasificacion();

        for (int i = 0; i < clasificacion.size(); i++) {
            TablaCampeonato.PilotoCampeonato piloto = clasificacion.get(i);
            HBox fila = crearFilaPiloto(i + 1, piloto);
            listaPilotos.getChildren().add(fila);
        }

        contenido.getChildren().addAll(info, encabezado, listaPilotos);
        scroll.setContent(contenido);

        root.setTop(header);
        root.setCenter(scroll);

        return new Scene(root, 1000, 700);
    }

    private HBox crearEncabezado() {
        Label pos = new Label("POS");
        pos.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        pos.setMinWidth(50);

        Label piloto = new Label("PILOTO");
        piloto.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        piloto.setMinWidth(250);

        Label equipo = new Label("EQUIPO");
        equipo.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        equipo.setMinWidth(200);

        Label puntos = new Label("PUNTOS");
        puntos.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        puntos.setMinWidth(100);

        Label carreras = new Label("CARRERAS");
        carreras.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        carreras.setMinWidth(100);

        Label mejor = new Label("MEJOR");
        mejor.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        mejor.setMinWidth(80);

        HBox encabezado = new HBox(15, pos, piloto, equipo, puntos, carreras, mejor);
        encabezado.setAlignment(Pos.CENTER_LEFT);
        encabezado.setPadding(new Insets(10, 15, 10, 15));
        encabezado.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 5;");

        return encabezado;
    }

    private HBox crearFilaPiloto(int posicion, TablaCampeonato.PilotoCampeonato piloto) {
        String colorFondo;
        if (posicion == 1) {
            colorFondo = "#f39c12"; // Oro
        } else if (posicion == 2) {
            colorFondo = "#95a5a6"; // Plata
        } else if (posicion == 3) {
            colorFondo = "#cd7f32"; // Bronce
        } else if (posicion <= 10) {
            colorFondo = "#27ae60"; // Verde - zona de puntos
        } else {
            colorFondo = "#34495e"; // Gris
        }

        Label lblPos = new Label(String.valueOf(posicion));
        lblPos.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        lblPos.setMinWidth(50);

        Label lblPiloto = new Label(piloto.getNombre());
        lblPiloto.setStyle("-fx-text-fill: white; -fx-font-size: 15px;");
        lblPiloto.setMinWidth(250);

        Label lblEquipo = new Label(piloto.getEquipo());
        lblEquipo.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 13px;");
        lblEquipo.setMinWidth(200);

        Label lblPuntos = new Label(String.valueOf(piloto.getPuntosTotal()));
        lblPuntos.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        lblPuntos.setMinWidth(100);

        Label lblCarreras = new Label(String.valueOf(piloto.getCarrerasCompletadas()));
        lblCarreras.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");
        lblCarreras.setMinWidth(100);

        String mejorPos = piloto.getMejorPosicion() == Integer.MAX_VALUE ? "-" : "P" + piloto.getMejorPosicion();
        Label lblMejor = new Label(mejorPos);
        lblMejor.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");
        lblMejor.setMinWidth(80);

        HBox fila = new HBox(15, lblPos, lblPiloto, lblEquipo, lblPuntos, lblCarreras, lblMejor);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(12, 15, 12, 15));
        fila.setStyle("-fx-background-color: " + colorFondo + "; -fx-background-radius: 5;");

        return fila;
    }

    public Scene getScene() {
        return scene;
    }
}
