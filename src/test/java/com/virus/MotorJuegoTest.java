package com.virus;

import com.virus.logic.Mazo;
import com.virus.logic.MotorJuego;
import com.virus.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MotorJuegoTest {

    private MotorJuego motor;

    @BeforeEach
    void setUp() {
        motor = new MotorJuego("Alice", "Bob");
    }

    @Test
    void testMazoOficialTiene68Cartas() {
        Mazo mazo = new Mazo();
        assertEquals(68, mazo.cartasRestantes());
    }

    @Test
    void testNoPermiteOrganosDelMismoColor() {
        Jugador actual = motor.getJugadorActual();
        actual.getMano().clear();
        actual.getMano().add(new Carta(TipoCarta.ORGANO, ColorCarta.ROJO));
        actual.getMano().add(new Carta(TipoCarta.ORGANO, ColorCarta.ROJO));

        assertTrue(motor.jugarOrgano(0));
        assertFalse(motor.jugarOrgano(0));
        assertEquals(1, actual.getCuerpo().size());
    }

    @Test
    void testDobleVirusExtirpaOrgano() {
        Jugador j1 = motor.getJugadorActual();
        Jugador j2 = motor.getJugadorRival();

        j2.getCuerpo().add(new OrganoEnMesa(new Carta(TipoCarta.ORGANO, ColorCarta.AZUL)));
        j1.getMano().clear();
        j1.getMano().add(new Carta(TipoCarta.VIRUS, ColorCarta.AZUL));
        j1.getMano().add(new Carta(TipoCarta.VIRUS, ColorCarta.AZUL));

        assertTrue(motor.jugarVirus(0, 0));
        assertTrue(j2.getCuerpo().get(0).estaInfectado());

        assertTrue(motor.jugarVirus(0, 0));
        assertTrue(j2.getCuerpo().isEmpty());
    }

    @Test
    void testDobleMedicinaInmunizaContraVirusYRobo() {
        Jugador j1 = motor.getJugadorActual();
        Jugador j2 = motor.getJugadorRival();

        j1.getCuerpo().add(new OrganoEnMesa(new Carta(TipoCarta.ORGANO, ColorCarta.VERDE)));
        j1.getMano().clear();
        j1.getMano().add(new Carta(TipoCarta.MEDICINA, ColorCarta.VERDE));
        j1.getMano().add(new Carta(TipoCarta.MEDICINA, ColorCarta.VERDE));

        assertTrue(motor.jugarMedicina(0, 0));
        assertTrue(motor.jugarMedicina(0, 0));
        assertTrue(j1.getCuerpo().get(0).estaInmunizado());

        motor.finalizarTurno();
        j2.getMano().clear();
        j2.getMano().add(new Carta(TipoCarta.VIRUS, ColorCarta.VERDE));
        j2.getMano().add(new Carta(TipoCarta.TRATAMIENTO, ColorCarta.MULTICOLOR, TipoTratamiento.LADRON));

        assertFalse(motor.jugarVirus(0, 0));
        assertFalse(motor.jugarTratamiento(1, -1, 0));
    }

    @Test
    void testCondicionVictoria4OrganosSanos() {
        Jugador j1 = motor.getJugadorActual();
        j1.getCuerpo().add(new OrganoEnMesa(new Carta(TipoCarta.ORGANO, ColorCarta.ROJO)));
        j1.getCuerpo().add(new OrganoEnMesa(new Carta(TipoCarta.ORGANO, ColorCarta.VERDE)));
        j1.getCuerpo().add(new OrganoEnMesa(new Carta(TipoCarta.ORGANO, ColorCarta.AZUL)));
        assertNull(motor.verificarGanador());

        j1.getCuerpo().add(new OrganoEnMesa(new Carta(TipoCarta.ORGANO, ColorCarta.AMARILLO)));
        assertEquals(j1, motor.verificarGanador());
    }
}
