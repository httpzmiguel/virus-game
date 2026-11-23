package com.virus.logic;

import com.virus.model.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    private final List<Carta> cartasRobo;
    private final List<Carta> descarte;

    public Mazo() {
        this.cartasRobo = new ArrayList<>();
        this.descarte = new ArrayList<>();
        inicializarMazoOficial();
        barajar();
    }

    private void inicializarMazoOficial() {
        ColorCarta[] basicos = {ColorCarta.ROJO, ColorCarta.VERDE, ColorCarta.AZUL, ColorCarta.AMARILLO};

        for (ColorCarta c : basicos) {
            for (int i = 0; i < 5; i++) cartasRobo.add(new Carta(TipoCarta.ORGANO, c));
        }
        cartasRobo.add(new Carta(TipoCarta.ORGANO, ColorCarta.MULTICOLOR));

        for (ColorCarta c : basicos) {
            for (int i = 0; i < 4; i++) cartasRobo.add(new Carta(TipoCarta.VIRUS, c));
        }
        cartasRobo.add(new Carta(TipoCarta.VIRUS, ColorCarta.MULTICOLOR));

        for (ColorCarta c : basicos) {
            for (int i = 0; i < 4; i++) cartasRobo.add(new Carta(TipoCarta.MEDICINA, c));
        }
        for (int i = 0; i < 4; i++) {
            cartasRobo.add(new Carta(TipoCarta.MEDICINA, ColorCarta.MULTICOLOR));
        }

        for (int i = 0; i < 3; i++) cartasRobo.add(new Carta(TipoCarta.TRATAMIENTO, ColorCarta.MULTICOLOR, TipoTratamiento.TRASPLANTE));
        for (int i = 0; i < 3; i++) cartasRobo.add(new Carta(TipoCarta.TRATAMIENTO, ColorCarta.MULTICOLOR, TipoTratamiento.LADRON));
        for (int i = 0; i < 2; i++) cartasRobo.add(new Carta(TipoCarta.TRATAMIENTO, ColorCarta.MULTICOLOR, TipoTratamiento.CONTAGIO));
        cartasRobo.add(new Carta(TipoCarta.TRATAMIENTO, ColorCarta.MULTICOLOR, TipoTratamiento.GUANTE_LATEX));
        cartasRobo.add(new Carta(TipoCarta.TRATAMIENTO, ColorCarta.MULTICOLOR, TipoTratamiento.ERROR_MEDICO));
    }

    public void barajar() {
        Collections.shuffle(cartasRobo);
    }

    public Carta robar() {
        if (cartasRobo.isEmpty()) {
            if (descarte.isEmpty()) return null;
            cartasRobo.addAll(descarte);
            descarte.clear();
            barajar();
        }
        return cartasRobo.remove(0);
    }

    public void descartar(Carta carta) {
        if (carta != null) descarte.add(carta);
    }

    public void descartarTodas(List<Carta> cartas) {
        descarte.addAll(cartas);
    }

    public List<Carta> getDescarte() {
        return descarte;
    }

    public int cartasRestantes() {
        return cartasRobo.size();
    }
}
