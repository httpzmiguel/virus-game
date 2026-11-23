package com.virus.model;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private final String nombre;
    private final List<Carta> mano;
    private final List<OrganoEnMesa> cuerpo;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mano = new ArrayList<>();
        this.cuerpo = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Carta> getMano() {
        return mano;
    }

    public List<OrganoEnMesa> getCuerpo() {
        return cuerpo;
    }

    public boolean tieneColorEnCuerpo(ColorCarta color) {
        for (OrganoEnMesa org : cuerpo) {
            if (org.getColor() == color) {
                return true;
            }
        }
        return false;
    }

    public int contarOrganosSanos() {
        int sanos = 0;
        for (OrganoEnMesa org : cuerpo) {
            if (org.estaSano()) {
                sanos++;
            }
        }
        return sanos;
    }

    public boolean haGanado() {
        return contarOrganosSanos() >= 4;
    }
}
