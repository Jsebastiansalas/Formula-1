package com.formula1.ui;

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

import java.util.List;

public class PantallaSimulacion {
    private AppFX app;
    private GestorTemporada gestorTemporada;
    private SimuladorClasificacion simulador;
    private HistorialResultados historial;
    private Scene scene;

    public PantallaSimulacion(AppFX app, GestorTemporada gestorTemporada,
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
        Label titulo = new Label("SIMULACIÓN DE CARRERA");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titulo.setStyle("-fx-text-fill: #e10600;");

        Button btnVolver = new Button("⬅ Volver");
        btnVolver.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 5;");
        btnVolver.setOnAction(e -> app.volverAlInicio());

        HBox header = new HBox(20, btnVolver, titulo);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));

        // Verificar si hay carreras disponibles
        if (gestorTemporada.temporadaFinalizada()) {
            Label mensaje = new Label("¡La temporada ha finalizado! No hay más carreras.");
            mensaje.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
            VBox centro = new VBox(mensaje);
            centro.setAlignment(Pos.CENTER);
            root.setTop(header);
            root.setCenter(centro);
            return new Scene(root, 1000, 700);
        }

        // Información de la siguiente carrera
        VBox infoCarrera = crearInfoCarrera();

        // Botón simular
        Button btnSimular = new Button("🏁 SIMULAR CARRERA");
        btnSimular.setStyle("-fx-background-color: #e10600; -fx-text-fill: white; " +
                "-fx-font-size: 20px; -fx-padding: 15px 50px; -fx-background-radius: 10;");
        btnSimular.setOnAction(e -> simularCarrera(root));

        VBox centro = new VBox(30, infoCarrera, btnSimular);
        centro.setAlignment(Pos.CENTER);
        centro.setPadding(new Insets(40));

        root.setTop(header);
        root.setCenter(centro);

        return new Scene(root, 1000, 700);
    }

    private VBox crearInfoCarrera() {
        int numeroCarrera = gestorTemporada.getCarreraActual() + 1;
        var circuito = gestorTemporada.getSiguienteCircuito();

        Label titulo = new Label("PRÓXIMA CARRERA: #" + numeroCarrera);
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titulo.setStyle("-fx-text-fill: white;");

        Label nombreCircuito = new Label(circuito.getNombre());
        nombreCircuito.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        nombreCircuito.setStyle("-fx-text-fill: #e10600;");

        Label pais = new Label("📍 " + circuito.getPais());
        pais.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        Label distancia = new Label("📏 " + circuito.getLongitudKm() + " km x " +
                circuito.getVueltas() + " vueltas = " +
                String.format("%.2f", circuito.getLongitudKm() * circuito.getVueltas()) + " km totales");
        distancia.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        VBox info = new VBox(10, titulo, nombreCircuito, pais, distancia);
        info.setAlignment(Pos.CENTER);
        info.setStyle("-fx-background-color: #2c3e50; -fx-padding: 30px; -fx-background-radius: 10;");

        return info;
    }

    private void simularCarrera(BorderPane root) {
        // Simular
        GestorTemporada.ResultadoCarrera resultado = gestorTemporada.simularSiguienteCarrera(simulador);

        if (resultado != null) {
            // Guardar en historial
            historial.guardarSesion(resultado.getCircuito(), resultado.getClima(),
                    resultado.getClasificacion());

            // Mostrar resultados
            mostrarResultados(root, resultado);
        }
    }

    private void mostrarResultados(BorderPane root, GestorTemporada.ResultadoCarrera resultado) {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #1a1a1a; -fx-background-color: #1a1a1a;");

        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(20));
        contenido.setStyle("-fx-background-color: #1a1a1a;");

        // Título resultados
        Label tituloResultados = new Label("RESULTADOS - " + resultado.getCircuito().getNombre());
        tituloResultados.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        tituloResultados.setStyle("-fx-text-fill: #e10600;");

        Label clima = new Label("Clima: " + resultado.getClima());
        clima.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        // Estadísticas
        Label stats = new Label(String.format("Finalizados: %d | DNF: %d (Accidentes: %d, Fallos: %d)",
                resultado.getTotalFinalizados(), resultado.getTotalDNF(),
                resultado.getTotalAccidentes(), resultado.getTotalFallosMecanicos()));
        stats.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 14px;");

        VBox header = new VBox(10, tituloResultados, clima, stats);
        header.setAlignment(Pos.CENTER);

        // Tabla de resultados
        VBox tablaResultados = new VBox(5);
        List<SimuladorClasificacion.ResultadoVuelta> clasificacion = resultado.getClasificacion();

        for (SimuladorClasificacion.ResultadoVuelta r : clasificacion) {
            HBox fila = crearFilaResultado(r);
            tablaResultados.getChildren().add(fila);
        }

        // Botones
        Button btnVerCampeonato = new Button("Ver Tabla de Campeonato");
        btnVerCampeonato.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-padding: 10px 30px; -fx-background-radius: 5;");
        btnVerCampeonato.setOnAction(e -> {
            PantallaCampeonato pantalla = new PantallaCampeonato(app, gestorTemporada);
            app.getPrimaryStage().setScene(pantalla.getScene());
        });

        Button btnOtraCarrera = new Button("Simular Otra Carrera");
        btnOtraCarrera.setStyle("-fx-background-color: #e10600; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-padding: 10px 30px; -fx-background-radius: 5;");
        btnOtraCarrera.setOnAction(e -> {
            PantallaSimulacion nueva = new PantallaSimulacion(app, gestorTemporada, simulador, historial);
            app.getPrimaryStage().setScene(nueva.getScene());
        });

        Button btnVolver = new Button("Volver al Menú");
        btnVolver.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-padding: 10px 30px; -fx-background-radius: 5;");
        btnVolver.setOnAction(e -> app.volverAlInicio());

        HBox botones = new HBox(15, btnVerCampeonato, btnOtraCarrera, btnVolver);
        botones.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(header, new Separator(), tablaResultados, new Separator(), botones);
        scroll.setContent(contenido);

        root.setCenter(scroll);
    }

    private HBox crearFilaResultado(SimuladorClasificacion.ResultadoVuelta r) {
        String color = r.isDnf() ? "#c0392b" : (r.getPosicion() <= 3 ? "#f39c12" :
                (r.getPosicion() <= 10 ? "#27ae60" : "#34495e"));

        Label posicion = new Label("P" + r.getPosicion());
        posicion.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        posicion.setMinWidth(40);

        Label piloto = new Label(r.getNombrePiloto());
        piloto.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        piloto.setMinWidth(200);

        Label equipo = new Label(r.getEquipo());
        equipo.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 12px;");
        equipo.setMinWidth(180);

        Label tiempo = new Label(r.isDnf() ? r.getEstadoFinal() : formatearTiempo(r.getTiempoVueltaSegundos()));
        tiempo.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        tiempo.setMinWidth(120);

        Label puntos = new Label(r.getPuntos() + " pts");
        puntos.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        puntos.setMinWidth(60);

        HBox fila = new HBox(15, posicion, piloto, equipo, tiempo, puntos);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(8, 15, 8, 15));
        fila.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 5;");

        return fila;
    }

    private String formatearTiempo(double segundos) {
        int minutos = (int) (segundos / 60);
        double segs = segundos % 60;
        return String.format("%d:%06.3f", minutos, segs);
    }

    public Scene getScene() {
        return scene;
    }
}
