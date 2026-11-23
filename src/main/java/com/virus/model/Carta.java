package com.virus.model;

public class Carta {
    private final TipoCarta tipo;
    private final ColorCarta color;
    private final TipoTratamiento tratamiento;

    public Carta(TipoCarta tipo, ColorCarta color) {
        this(tipo, color, TipoTratamiento.NINGUNO);
    }

    public Carta(TipoCarta tipo, ColorCarta color, TipoTratamiento tratamiento) {
        this.tipo = tipo;
        this.color = color;
        this.tratamiento = tratamiento;
    }

    public TipoCarta getTipo() {
        return tipo;
    }

    public ColorCarta getColor() {
        return color;
    }

    public TipoTratamiento getTratamiento() {
        return tratamiento;
    }

    public String getNombreVisible() {
        if (tipo == TipoCarta.TRATAMIENTO) {
            return tratamiento.getTitulo();
        }
        return tipo.name() + " (" + color.getNombreOrgano() + ")";
    }
}
