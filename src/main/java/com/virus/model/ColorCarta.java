package com.virus.model;

import java.awt.Color;

public enum ColorCarta {
    ROJO("Corazón", new Color(220, 53, 69)),
    VERDE("Estómago", new Color(40, 167, 69)),
    AZUL("Cerebro", new Color(0, 123, 255)),
    AMARILLO("Hueso", new Color(255, 193, 7)),
    MULTICOLOR("Comodín", new Color(155, 89, 182));

    private final String nombreOrgano;
    private final Color colorAwt;

    ColorCarta(String nombreOrgano, Color colorAwt) {
        this.nombreOrgano = nombreOrgano;
        this.colorAwt = colorAwt;
    }

    public String getNombreOrgano() {
        return nombreOrgano;
    }

    public Color getColorAwt() {
        return colorAwt;
    }
}
