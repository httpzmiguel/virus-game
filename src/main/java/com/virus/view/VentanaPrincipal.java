package com.virus.view;

import com.virus.logic.MotorJuego;
import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("VIRUS! - Proyecto Ingeniería de Sistemas");
        setSize(980, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        volverAlInicio();
    }

    public void volverAlInicio() {
        setContentPane(new PantallaInicioPanel(this));
        revalidate();
        repaint();
    }

    public void iniciarPartida(String jugador1, String jugador2) {
        MotorJuego motor = new MotorJuego(jugador1, jugador2);
        setContentPane(new TableroJuegoPanel(motor, this));
        revalidate();
        repaint();
    }
}
