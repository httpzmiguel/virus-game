package com.virus.view;

import com.virus.dao.PartidaDAO;
import com.virus.model.EstadoPartida;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PantallaInicioPanel extends JPanel {
    private final JTextField txtJugador1;
    private final JTextField txtJugador2;
    private final PartidaDAO partidaDAO;

    public PantallaInicioPanel(VentanaPrincipal ventana) {
        this.partidaDAO = new PartidaDAO();
        setLayout(new GridBagLayout());
        setBackground(new Color(26, 37, 48));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel lblTitulo = new JLabel("VIRUS!");
        lblTitulo.setFont(new Font("Impact", Font.BOLD, 74));
        lblTitulo.setForeground(new Color(46, 204, 113));
        gbc.gridy = 0;
        add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("El juego de cartas más contagioso — Edición 2 Jugadores");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblSubtitulo.setForeground(new Color(220, 225, 230));
        gbc.gridy = 1;
        add(lblSubtitulo, gbc);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 12));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel l1 = new JLabel("Nombre Jugador 1:");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("SansSerif", Font.BOLD, 14));
        txtJugador1 = new JTextField("Jugador 1", 15);

        JLabel l2 = new JLabel("Nombre Jugador 2:");
        l2.setForeground(Color.WHITE);
        l2.setFont(new Font("SansSerif", Font.BOLD, 14));
        txtJugador2 = new JTextField("Jugador 2", 15);

        formPanel.add(l1);
        formPanel.add(txtJugador1);
        formPanel.add(l2);
        formPanel.add(txtJugador2);

        gbc.gridy = 2;
        add(formPanel, gbc);

        JButton btnIniciar = new JButton("INICIAR PARTIDA");
        btnIniciar.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnIniciar.setBackground(new Color(231, 76, 60));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setPreferredSize(new Dimension(240, 48));
        btnIniciar.addActionListener(e -> {
            String j1 = txtJugador1.getText().trim().isEmpty() ? "Jugador 1" : txtJugador1.getText().trim();
            String j2 = txtJugador2.getText().trim().isEmpty() ? "Jugador 2" : txtJugador2.getText().trim();
            ventana.iniciarPartida(j1, j2);
        });
        gbc.gridy = 3;
        add(btnIniciar, gbc);

        JButton btnHistorial = new JButton("VER RANKING EN NEON DB");
        btnHistorial.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnHistorial.setBackground(new Color(52, 152, 219));
        btnHistorial.setForeground(Color.WHITE);
        btnHistorial.setFocusPainted(false);
        btnHistorial.addActionListener(e -> mostrarHistorialNeon());
        gbc.gridy = 4;
        add(btnHistorial, gbc);
    }

    private void mostrarHistorialNeon() {
        List<EstadoPartida> historial = partidaDAO.listarUltimasPartidas(10);
        if (historial.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay partidas registradas aún o verifica tu conexión a Neon.",
                    "Historial Neon", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("=== ÚLTIMAS PARTIDAS (NEON POSTGRESQL) === ");
        for (EstadoPartida p : historial) {
            sb.append(String.format("• %s vs %s | Ganador: %s (%d turnos)\n",
                    p.getJugador1(), p.getJugador2(), p.getGanador(), p.getTurnosTotales()));
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Historial de Partidas", JOptionPane.INFORMATION_MESSAGE);
    }
}
