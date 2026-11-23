package com.virus.model;

import java.util.ArrayList;
import java.util.List;

public class OrganoEnMesa {
    private final Carta organoBase;
    private final List<Carta> virusAplicados;
    private final List<Carta> medicinasAplicadas;

    public OrganoEnMesa(Carta organoBase) {
        this.organoBase = organoBase;
        this.virusAplicados = new ArrayList<>();
        this.medicinasAplicadas = new ArrayList<>();
    }

    public Carta getOrganoBase() {
        return organoBase;
    }

    public ColorCarta getColor() {
        return organoBase.getColor();
    }

    public List<Carta> getVirusAplicados() {
        return virusAplicados;
    }

    public List<Carta> getMedicinasAplicadas() {
        return medicinasAplicadas;
    }

    public boolean estaInmunizado() {
        return medicinasAplicadas.size() >= 2;
    }

    public boolean estaInfectado() {
        return !virusAplicados.isEmpty();
    }

    public boolean estaVacunado() {
        return medicinasAplicadas.size() == 1;
    }

    public boolean estaSano() {
        return virusAplicados.isEmpty();
    }

    public boolean esCompatible(Carta carta) {
        if (estaInmunizado()) return false;
        return this.getColor() == ColorCarta.MULTICOLOR
                || carta.getColor() == ColorCarta.MULTICOLOR
                || this.getColor() == carta.getColor();
    }

    public List<Carta> aplicarMedicina(Carta medicina) {
        List<Carta> descartes = new ArrayList<>();
        if (!virusAplicados.isEmpty()) {
            descartes.add(virusAplicados.remove(0));
            descartes.add(medicina);
        } else if (medicinasAplicadas.size() < 2) {
            medicinasAplicadas.add(medicina);
        }
        return descartes;
    }

    public boolean aplicarVirus(Carta virus, List<Carta> pilaDescarte) {
        if (!medicinasAplicadas.isEmpty()) {
            pilaDescarte.add(medicinasAplicadas.remove(0));
            pilaDescarte.add(virus);
            return false;
        }
        if (!virusAplicados.isEmpty()) {
            pilaDescarte.addAll(virusAplicados);
            pilaDescarte.add(virus);
            pilaDescarte.add(organoBase);
            return true;
        }
        virusAplicados.add(virus);
        return false;
    }
}
