package com.virus.logic;

import com.virus.model.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MotorJuego {
    private final Jugador jugador1;
    private final Jugador jugador2;
    private final Mazo mazo;
    private int indiceTurnoActual;
    private int contadorTurnos;

    public MotorJuego(String nombreJ1, String nombreJ2) {
        this.jugador1 = new Jugador(nombreJ1);
        this.jugador2 = new Jugador(nombreJ2);
        this.mazo = new Mazo();
        this.indiceTurnoActual = 0;
        this.contadorTurnos = 1;
        repartirInicial();
    }

    private void repartirInicial() {
        for (int i = 0; i < 3; i++) {
            jugador1.getMano().add(mazo.robar());
            jugador2.getMano().add(mazo.robar());
        }
    }

    public Jugador getJugadorActual() {
        return indiceTurnoActual == 0 ? jugador1 : jugador2;
    }

    public Jugador getJugadorRival() {
        return indiceTurnoActual == 0 ? jugador2 : jugador1;
    }

    public Jugador getJugador1() { return jugador1; }
    public Jugador getJugador2() { return jugador2; }
    public Mazo getMazo() { return mazo; }
    public int getContadorTurnos() { return contadorTurnos; }

    public boolean jugarOrgano(int indiceCartaMano) {
        Jugador actual = getJugadorActual();
        if (indiceCartaMano < 0 || indiceCartaMano >= actual.getMano().size()) return false;

        Carta carta = actual.getMano().get(indiceCartaMano);
        if (carta.getTipo() != TipoCarta.ORGANO) return false;
        if (actual.tieneColorEnCuerpo(carta.getColor())) return false;

        actual.getMano().remove(indiceCartaMano);
        actual.getCuerpo().add(new OrganoEnMesa(carta));
        return true;
    }

    public boolean jugarMedicina(int indiceCartaMano, int indiceOrganoPropio) {
        Jugador actual = getJugadorActual();
        if (indiceCartaMano < 0 || indiceCartaMano >= actual.getMano().size()) return false;
        if (indiceOrganoPropio < 0 || indiceOrganoPropio >= actual.getCuerpo().size()) return false;

        Carta carta = actual.getMano().get(indiceCartaMano);
        OrganoEnMesa objetivo = actual.getCuerpo().get(indiceOrganoPropio);

        if (carta.getTipo() != TipoCarta.MEDICINA || !objetivo.esCompatible(carta)) return false;

        actual.getMano().remove(indiceCartaMano);
        List<Carta> descartes = objetivo.aplicarMedicina(carta);
        mazo.descartarTodas(descartes);
        return true;
    }

    public boolean jugarVirus(int indiceCartaMano, int indiceOrganoRival) {
        Jugador actual = getJugadorActual();
        Jugador rival = getJugadorRival();
        if (indiceCartaMano < 0 || indiceCartaMano >= actual.getMano().size()) return false;
        if (indiceOrganoRival < 0 || indiceOrganoRival >= rival.getCuerpo().size()) return false;

        Carta carta = actual.getMano().get(indiceCartaMano);
        OrganoEnMesa objetivo = rival.getCuerpo().get(indiceOrganoRival);

        if (carta.getTipo() != TipoCarta.VIRUS || !objetivo.esCompatible(carta)) return false;

        actual.getMano().remove(indiceCartaMano);
        boolean extirpado = objetivo.aplicarVirus(carta, mazo.getDescarte());
        if (extirpado) {
            rival.getCuerpo().remove(indiceOrganoRival);
        }
        return true;
    }

    public boolean jugarTratamiento(int indiceCartaMano, int idxOrganoPropio, int idxOrganoRival) {
        Jugador actual = getJugadorActual();
        Jugador rival = getJugadorRival();
        if (indiceCartaMano < 0 || indiceCartaMano >= actual.getMano().size()) return false;

        Carta carta = actual.getMano().get(indiceCartaMano);
        if (carta.getTipo() != TipoCarta.TRATAMIENTO) return false;

        boolean exito = false;
        switch (carta.getTratamiento()) {
            case GUANTE_LATEX:
                mazo.descartarTodas(rival.getMano());
                rival.getMano().clear();
                exito = true;
                break;

            case ERROR_MEDICO:
                List<OrganoEnMesa> temp = new ArrayList<>(actual.getCuerpo());
                actual.getCuerpo().clear();
                actual.getCuerpo().addAll(rival.getCuerpo());
                rival.getCuerpo().clear();
                rival.getCuerpo().addAll(temp);
                exito = true;
                break;

            case LADRON:
                if (idxOrganoRival >= 0 && idxOrganoRival < rival.getCuerpo().size()) {
                    OrganoEnMesa robado = rival.getCuerpo().get(idxOrganoRival);
                    if (!robado.estaInmunizado() && !actual.tieneColorEnCuerpo(robado.getColor())) {
                        rival.getCuerpo().remove(idxOrganoRival);
                        actual.getCuerpo().add(robado);
                        exito = true;
                    }
                }
                break;

            case TRASPLANTE:
                if (idxOrganoPropio >= 0 && idxOrganoPropio < actual.getCuerpo().size()
                        && idxOrganoRival >= 0 && idxOrganoRival < rival.getCuerpo().size()) {
                    OrganoEnMesa propio = actual.getCuerpo().get(idxOrganoPropio);
                    OrganoEnMesa ajeno = rival.getCuerpo().get(idxOrganoRival);

                    if (!propio.estaInmunizado() && !ajeno.estaInmunizado()) {
                        boolean colorRepetidoEnActual = ajeno.getColor() != propio.getColor() && actual.tieneColorEnCuerpo(ajeno.getColor());
                        boolean colorRepetidoEnRival = propio.getColor() != ajeno.getColor() && rival.tieneColorEnCuerpo(propio.getColor());

                        if (!colorRepetidoEnActual && !colorRepetidoEnRival) {
                            actual.getCuerpo().set(idxOrganoPropio, ajeno);
                            rival.getCuerpo().set(idxOrganoRival, propio);
                            exito = true;
                        }
                    }
                }
                break;

            case CONTAGIO:
                boolean contagioAlguno = false;
                for (OrganoEnMesa orgPropio : actual.getCuerpo()) {
                    if (orgPropio.estaInfectado()) {
                        Iterator<Carta> itVirus = orgPropio.getVirusAplicados().iterator();
                        while (itVirus.hasNext()) {
                            Carta virus = itVirus.next();
                            for (OrganoEnMesa orgRival : rival.getCuerpo()) {
                                if (orgRival.estaSano() && !orgRival.estaVacunado()
                                        && !orgRival.estaInmunizado() && orgRival.esCompatible(virus)) {
                                    orgRival.getVirusAplicados().add(virus);
                                    itVirus.remove();
                                    contagioAlguno = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                exito = contagioAlguno;
                break;
            default:
                break;
        }

        if (exito) {
            actual.getMano().remove(indiceCartaMano);
            mazo.descartar(carta);
        }
        return exito;
    }

    public void descartarSeleccionadas(List<Integer> indicesDescendentes) {
        Jugador actual = getJugadorActual();
        indicesDescendentes.sort((a, b) -> b - a);
        for (int idx : indicesDescendentes) {
            if (idx >= 0 && idx < actual.getMano().size()) {
                mazo.descartar(actual.getMano().remove(idx));
            }
        }
    }

    public void finalizarTurno() {
        Jugador actual = getJugadorActual();
        while (actual.getMano().size() < 3) {
            Carta robada = mazo.robar();
            if (robada == null) break;
            actual.getMano().add(robada);
        }
        indiceTurnoActual = (indiceTurnoActual == 0) ? 1 : 0;
        contadorTurnos++;
    }

    public Jugador verificarGanador() {
        if (jugador1.haGanado()) return jugador1;
        if (jugador2.haGanado()) return jugador2;
        return null;
    }
}
