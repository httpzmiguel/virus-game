package com.virus.model;

import java.time.LocalDateTime;

public class EstadoPartida {
    private final String jugador1;
    private final String jugador2;
    private final String ganador;
    private final int turnosTotales;
    private final LocalDateTime fecha;

    public EstadoPartida(String jugador1, String jugador2, String ganador, int turnosTotales, LocalDateTime fecha) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.ganador = ganador;
        this.turnosTotales = turnosTotales;
        this.fecha = fecha;
    }

    public String getJugador1() { return jugador1; }
    public String getJugador2() { return jugador2; }
    public String getGanador() { return ganador; }
    public int getTurnosTotales() { return turnosTotales; }
    public LocalDateTime getFecha() { return fecha; }
}
