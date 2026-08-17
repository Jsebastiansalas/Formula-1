package com.formula1.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.formula1.simulacion.GestorTemporada;

import java.util.List;

public class PantallaHistorial {
    private AppFX app;
    private GestorTemporada gestorTemporada;
    private Scene scene;

    public PantallaHistorial(AppFX app, GestorTemporada gestorTemporada) {
        this.app = app;
        this.gestorTemporada = gestorTemporada;
        this.scene = crearEscena();
    }

    private Scene crearEscena() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a1a;");

        // Header
        Label titulo = new Label("📊 HISTORIAL DE CARRERAS");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titulo.setStyle("-fx-text-fill: #e10600;");

        Button btnVolver = new Button("⬅ Volver");
        btnVolver.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 5;");
        btnVolver.setOnAction(e -> app.volverAlInicio());

        HBox header = new HBox(20, btnVolver, titulo);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));

        // Contenido
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #1a1a1a; -fx-background-color: #1a1a1a;");

        VBox contenido = new VBox(15);
        contenido.setPadding(new Insets(20));

        List<GestorTemporada.ResultadoCarrera> historial = gestorTemporada.getHistorialCarreras();

        if (historial.isEmpty()) {
            Label mensaje = new Label("No hay carreras simuladas todavía");
            mensaje.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 18px;");
            VBox centro = new VBox(mensaje);
            centro.setAlignment(Pos.CENTER);
            centro.setPadding(new Insets(100));
            scroll.setContent(centro);
        } else {
            // Estadísticas acumuladas
            VBox stats = crearEstadisticasAcumuladas(historial);
            contenido.getChildren().add(stats);
            contenido.getChildren().add(new Separator());

            // Lista de carreras
            for (GestorTemporada.ResultadoCarrera resultado : historial) {
                VBox carreraBox = crearBoxCarrera(resultado);
                contenido.getChildren().add(carreraBox);
            }

            scroll.setContent(contenido);
        }

        root.setTop(header);
        root.setCenter(scroll);

        return new Scene(root, 1000, 700);
    }

    private VBox crearEstadisticasAcumuladas(List<GestorTemporada.ResultadoCarrera> historial) {
        int totalDNFs = 0;
        int totalAccidentes = 0;
        int totalFallosMecanicos = 0;
        int totalPenalizaciones = 0;

        for (GestorTemporada.ResultadoCarrera r : historial) {
            totalDNFs += r.getTotalDNF();
            totalAccidentes += r.getTotalAccidentes();
            totalFallosMecanicos += r.getTotalFallosMecanicos();
            totalPenalizaciones += r.getTotalPenalizaciones();
        }

        Label titulo = new Label("📈 ESTADÍSTICAS ACUMULADAS");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titulo.setStyle("-fx-text-fill: white;");

        Label carreras = new Label("Carreras completadas: " + historial.size());
        Label dnfs = new Label("Total DNF: " + totalDNFs);
        Label accidentes = new Label("Accidentes: " + totalAccidentes);
        Label fallos = new Label("Fallos mecánicos: " + totalFallosMecanicos);
        Label penalizaciones = new Label("Penalizaciones: " + totalPenalizaciones);

        carreras.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");
        dnfs.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");
        accidentes.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");
        fallos.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");
        penalizaciones.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px;");

        HBox statsBox = new HBox(30, carreras, dnfs, accidentes, fallos, penalizaciones);
        statsBox.setAlignment(Pos.CENTER);

        VBox box = new VBox(10, titulo, statsBox);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 10;");

        return box;
    }

    private VBox crearBoxCarrera(GestorTemporada.ResultadoCarrera resultado) {
        // Título de la carrera
        Label numero = new Label("CARRERA " + resultado.getNumeroCarrera());
        numero.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        numero.setStyle("-fx-text-fill: #e10600;");

        Label circuito = new Label(resultado.getCircuito().getNombre());
        circuito.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        circuito.setStyle("-fx-text-fill: white;");

        HBox titulo = new HBox(15, numero, circuito);
        titulo.setAlignment(Pos.CENTER_LEFT);

        // Información
        Label clima = new Label("☁ " + resultado.getClima());
        clima.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 13px;");

        Label ganador = new Label("🏆 " + resultado.getGanador());
        ganador.setStyle("-fx-text-fill: #f39c12; -fx-font-size: 13px; -fx-font-weight: bold;");

        Label finalizados = new Label("✓ Finalizados: " + resultado.getTotalFinalizados());
        finalizados.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 12px;");

        Label dnf = new Label("✗ DNF: " + resultado.getTotalDNF());
        dnf.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");

        Label accidentes = new Label("💥 Accidentes: " + resultado.getTotalAccidentes());
        accidentes.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");

        Label fallos = new Label("🔧 Fallos mec.: " + resultado.getTotalFallosMecanicos());
        fallos.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");

        Label penalizaciones = new Label("⚠ Penalizaciones: " + resultado.getTotalPenalizaciones());
        penalizaciones.setStyle("-fx-text-fill: #f39c12; -fx-font-size: 12px;");

        HBox info1 = new HBox(20, clima, ganador);
        info1.setAlignment(Pos.CENTER_LEFT);

        HBox info2 = new HBox(20, finalizados, dnf, accidentes, fallos, penalizaciones);
        info2.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(8, titulo, info1, info2);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: #34495e; -fx-background-radius: 8;");

        return box;
    }

    public Scene getScene() {
        return scene;
    }
}
