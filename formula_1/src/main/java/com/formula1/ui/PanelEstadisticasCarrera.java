package com.formula1.ui;

import com.formula1.simulacion.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Panel de estadísticas detalladas de una carrera individual
 */
public class PanelEstadisticasCarrera extends VBox {

    public PanelEstadisticasCarrera(SimuladorCarreraRealista.ResultadoCarreraDetallado resultado,
                                    GestorTemporada.ResultadoCarrera resultadoCarrera) {
        super(20);
        this.setPadding(new Insets(20));
        this.getStyleClass().add("stats-box");

        // Título
        Label titulo = new Label("📊 ESTADÍSTICAS COMPLETAS DE LA CARRERA");
        titulo.getStyleClass().add("hero-subtitulo");

        // Sección clima
        VBox seccionClima = crearSeccionClima(resultado);

        // Sección eventos importantes
        VBox seccionEventos = crearSeccionEventos(resultado);

        // Sección estadísticas generales
        VBox seccionStats = crearSeccionStats(resultado, resultadoCarrera);

        // Sección vuelta rápida
        VBox seccionVueltaRapida = crearSeccionVueltaRapida(resultado);

        // Sección clasificación completa
        VBox seccionClasificacion = crearSeccionClasificacion(resultado);

        ScrollPane scroll = new ScrollPane();
        VBox contenido = new VBox(20, titulo, seccionClima, seccionStats, seccionVueltaRapida,
                                 seccionEventos, seccionClasificacion);
        scroll.setContent(contenido);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");

        VBox.setVgrow(scroll, Priority.ALWAYS);
        this.getChildren().add(scroll);
    }

    private VBox crearSeccionClima(SimuladorCarreraRealista.ResultadoCarreraDetallado resultado) {
        VBox seccion = new VBox(10);
        seccion.setPadding(new Insets(15));
        seccion.getStyleClass().add("tarjeta");

        Label titulo = new Label("🌤️ CONDICIONES CLIMÁTICAS");
        titulo.getStyleClass().add("tarjeta-label-valor");
        titulo.setStyle("-fx-font-size: 18px; -fx-text-fill: #ffffff;");

        HBox climaBox = new HBox(30);
        climaBox.setAlignment(Pos.CENTER_LEFT);

        VBox climaInicial = crearInfoClima("Inicio", resultado.getClimaInicial().toString());
        VBox climaFinal = crearInfoClima("Final", resultado.getClimaFinal().toString());
        VBox cambios = crearInfoClima("Cambios", resultado.getCambiosClima() + " veces");

        climaBox.getChildren().addAll(climaInicial, climaFinal, cambios);

        if (resultado.getCambiosClima() > 0) {
            Label advertencia = new Label("⚠️ Hubo cambios de clima durante la carrera, aumentando la dificultad");
            advertencia.getStyleClass().add("tarjeta-label-detalle");
            advertencia.setStyle("-fx-text-fill: #f59e0b; -fx-font-size: 13px;");
            seccion.getChildren().addAll(titulo, climaBox, advertencia);
        } else {
            seccion.getChildren().addAll(titulo, climaBox);
        }

        return seccion;
    }

    private VBox crearInfoClima(String label, String valor) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER_LEFT);

        Label lbl = new Label(label);
        lbl.getStyleClass().add("tarjeta-label-titulo");
        lbl.setStyle("-fx-text-fill: #94a3b8;");

        Label val = new Label(valor);
        val.getStyleClass().add("tarjeta-label-valor");
        val.setStyle("-fx-font-size: 16px; -fx-text-fill: #ffffff;");

        box.getChildren().addAll(lbl, val);
        return box;
    }

    private VBox crearSeccionStats(SimuladorCarreraRealista.ResultadoCarreraDetallado resultado,
                                   GestorTemporada.ResultadoCarrera resultadoCarrera) {
        VBox seccion = new VBox(15);

        Label titulo = new Label("📈 ESTADÍSTICAS GENERALES");
        titulo.getStyleClass().add("tarjeta-label-valor");
        titulo.setStyle("-fx-font-size: 18px; -fx-text-fill: #ffffff;");

        HBox tarjetas = new HBox(15);
        tarjetas.setAlignment(Pos.CENTER);

        tarjetas.getChildren().addAll(
            crearTarjetaStat("FINALIZADOS", String.valueOf(resultadoCarrera.getTotalFinalizados()), "pilotos"),
            crearTarjetaStat("DNF", String.valueOf(resultadoCarrera.getTotalDNF()), "abandonos"),
            crearTarjetaStat("ACCIDENTES", String.valueOf(resultadoCarrera.getTotalAccidentes()), "incidentes"),
            crearTarjetaStat("ADELANTAMIENTOS", String.valueOf(resultado.getTotalAdelantamientos()), "maniobras"),
            crearTarjetaStat("PENALIZACIONES", String.valueOf(resultadoCarrera.getTotalPenalizaciones()), "sanciones")
        );

        seccion.getChildren().addAll(titulo, tarjetas);
        return seccion;
    }

    private VBox crearTarjetaStat(String label, String valor, String detalle) {
        VBox tarjeta = new VBox(8);
        tarjeta.getStyleClass().add("tarjeta");
        tarjeta.setPadding(new Insets(20));
        tarjeta.setPrefWidth(150);
        tarjeta.setAlignment(Pos.CENTER);

        Label lbl = new Label(label);
        lbl.getStyleClass().add("tarjeta-label-titulo");
        lbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px;");

        Label val = new Label(valor);
        val.getStyleClass().add("tarjeta-label-valor");
        val.setStyle("-fx-font-size: 32px; -fx-text-fill: #e10600; -fx-font-weight: bold;");

        Label det = new Label(detalle);
        det.getStyleClass().add("tarjeta-label-detalle");
        det.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");

        tarjeta.getChildren().addAll(lbl, val, det);
        return tarjeta;
    }

    private VBox crearSeccionVueltaRapida(SimuladorCarreraRealista.ResultadoCarreraDetallado resultado) {
        VBox seccion = new VBox(10);
        seccion.setPadding(new Insets(15));
        seccion.getStyleClass().add("tarjeta");
        seccion.setStyle("-fx-border-color: #9b59b6; -fx-border-width: 2;");

        Label titulo = new Label("⚡ VUELTA MÁS RÁPIDA");
        titulo.getStyleClass().add("tarjeta-label-valor");
        titulo.setStyle("-fx-font-size: 18px; -fx-text-fill: #ffffff; -fx-font-weight: bold;");

        if (resultado.getPilotoVueltaRapida() != null) {
            Label piloto = new Label("Piloto: " + resultado.getPilotoVueltaRapida());
            piloto.getStyleClass().add("tarjeta-label-valor");
            piloto.setStyle("-fx-font-size: 20px; -fx-text-fill: #e10600; -fx-font-weight: bold;");

            Label tiempo = new Label(String.format("Tiempo: %.3f segundos", resultado.getTiempoVueltaRapida()));
            tiempo.getStyleClass().add("tarjeta-label-detalle");
            tiempo.setStyle("-fx-font-size: 16px; -fx-text-fill: #ffffff;");

            seccion.getChildren().addAll(titulo, piloto, tiempo);
        } else {
            Label noData = new Label("No disponible");
            noData.getStyleClass().add("tarjeta-label-detalle");
            noData.setStyle("-fx-text-fill: #94a3b8;");
            seccion.getChildren().addAll(titulo, noData);
        }

        return seccion;
    }

    private VBox crearSeccionEventos(SimuladorCarreraRealista.ResultadoCarreraDetallado resultado) {
        VBox seccion = new VBox(10);

        Label titulo = new Label("📋 EVENTOS IMPORTANTES DE LA CARRERA");
        titulo.getStyleClass().add("tarjeta-label-valor");
        titulo.setStyle("-fx-font-size: 18px; -fx-text-fill: #ffffff; -fx-font-weight: bold;");

        VBox listaEventos = new VBox(8);

        List<String> eventos = resultado.getEventosImportantes();
        if (eventos.isEmpty()) {
            Label sinEventos = new Label("No hubo eventos destacables durante la carrera");
            sinEventos.getStyleClass().add("tarjeta-label-detalle");
            sinEventos.setStyle("-fx-text-fill: #94a3b8;");
            listaEventos.getChildren().add(sinEventos);
        } else {
            // Mostrar máximo 15 eventos más importantes
            int limit = Math.min(15, eventos.size());
            for (int i = 0; i < limit; i++) {
                HBox eventoBox = new HBox(10);
                eventoBox.getStyleClass().add("list-row");
                eventoBox.setPadding(new Insets(10));

                Label evento = new Label(eventos.get(i));
                evento.getStyleClass().add("tarjeta-label-detalle");
                evento.setStyle("-fx-font-size: 13px; -fx-text-fill: #ffffff;");
                evento.setWrapText(true);

                eventoBox.getChildren().add(evento);
                listaEventos.getChildren().add(eventoBox);
            }

            if (eventos.size() > 15) {
                Label mas = new Label("... y " + (eventos.size() - 15) + " eventos más");
                mas.getStyleClass().add("tarjeta-label-detalle");
                mas.setStyle("-fx-font-style: italic; -fx-text-fill: #94a3b8;");
                listaEventos.getChildren().add(mas);
            }
        }

        seccion.getChildren().addAll(titulo, listaEventos);
        return seccion;
    }

    private VBox crearSeccionClasificacion(SimuladorCarreraRealista.ResultadoCarreraDetallado resultado) {
        VBox seccion = new VBox(10);

        Label titulo = new Label("🏁 CLASIFICACIÓN FINAL");
        titulo.getStyleClass().add("tarjeta-label-valor");
        titulo.setStyle("-fx-font-size: 18px; -fx-text-fill: #ffffff; -fx-font-weight: bold;");

        VBox listaClasificacion = new VBox(8);

        List<SimuladorClasificacion.ResultadoVuelta> clasificacion = resultado.getClasificacion();
        for (int i = 0; i < clasificacion.size(); i++) {
            SimuladorClasificacion.ResultadoVuelta res = clasificacion.get(i);
            HBox fila = crearFilaClasificacion(i + 1, res);

            if (i == 0 && !res.isDnf()) fila.getStyleClass().add("list-row-gold");
            else if (i == 1 && !res.isDnf()) fila.getStyleClass().add("list-row-silver");
            else if (i == 2 && !res.isDnf()) fila.getStyleClass().add("list-row-bronze");
            else if (i < 10 && !res.isDnf()) fila.getStyleClass().add("list-row-points");
            else if (res.isDnf()) fila.getStyleClass().add("list-row-dnf");
            else fila.getStyleClass().add("list-row");

            listaClasificacion.getChildren().add(fila);
        }

        seccion.getChildren().addAll(titulo, listaClasificacion);
        return seccion;
    }

    private HBox crearFilaClasificacion(int pos, SimuladorClasificacion.ResultadoVuelta resultado) {
        HBox fila = new HBox(15);
        fila.setPadding(new Insets(12));
        fila.setAlignment(Pos.CENTER_LEFT);

        Label lblPos = new Label(String.format("%2d", pos));
        lblPos.getStyleClass().add("tarjeta-label-valor");
        lblPos.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold;");
        lblPos.setPrefWidth(40);

        Label lblPiloto = new Label(resultado.getNombrePiloto());
        lblPiloto.getStyleClass().add("tarjeta-label-valor");
        lblPiloto.setStyle("-fx-font-size: 14px; -fx-text-fill: #ffffff; -fx-font-weight: bold;");
        lblPiloto.setPrefWidth(180);

        Label lblEquipo = new Label(resultado.getEquipo());
        lblEquipo.getStyleClass().add("tarjeta-label-detalle");
        lblEquipo.setStyle("-fx-text-fill: #94a3b8;");
        lblEquipo.setPrefWidth(180);

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        Label lblPuntos = new Label(resultado.getPuntos() + " pts");
        lblPuntos.getStyleClass().add("tarjeta-label-valor");
        lblPuntos.setStyle("-fx-font-size: 14px; -fx-text-fill: #27ae60; -fx-font-weight: bold;");
        lblPuntos.setPrefWidth(60);

        Label lblTiempo = new Label(
            resultado.isDnf() ? resultado.getEstadoFinal() : String.format("%.3fs", resultado.getTiempoVueltaSegundos())
        );
        lblTiempo.getStyleClass().add("tarjeta-label-detalle");
        lblTiempo.setStyle("-fx-text-fill: " + (resultado.isDnf() ? "#e74c3c" : "#ffffff") + "; -fx-font-weight: bold;");

        fila.getChildren().addAll(lblPos, lblPiloto, lblEquipo, espaciador, lblPuntos, lblTiempo);
        return fila;
    }
}
