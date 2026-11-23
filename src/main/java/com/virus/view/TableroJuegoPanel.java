package com.virus.view;

import com.virus.dao.PartidaDAO;
import com.virus.logic.MotorJuego;
import com.virus.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TableroJuegoPanel extends JPanel {
    private final MotorJuego motor;
    private final VentanaPrincipal ventana;
    private final PartidaDAO partidaDAO;

    private int cartaSeleccionada = -1;
    private int organoPropioSeleccionado = -1;
    private boolean pantallaPrivacidadActiva = false;
    private String mensajeEstado = "Selecciona una carta de tu mano para jugar o descartar.";

    private final List<Rectangle> hitboxesMano = new ArrayList<>();
    private final List<Rectangle> hitboxesCuerpoPropio = new ArrayList<>();
    private final List<Rectangle> hitboxesCuerpoRival = new ArrayList<>();

    public TableroJuegoPanel(MotorJuego motor, VentanaPrincipal ventana) {
        this.motor = motor;
        this.ventana = ventana;
        this.partidaDAO = new PartidaDAO();
        setLayout(new BorderLayout());
        setBackground(new Color(34, 49, 63));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        panelBotones.setBackground(new Color(24, 34, 45));

        JButton btnJugarDirecto = new JButton("Jugar Órgano / Tratamiento Global");
        btnJugarDirecto.setBackground(new Color(46, 204, 113));
        btnJugarDirecto.setForeground(Color.WHITE);
        btnJugarDirecto.setFocusPainted(false);
        btnJugarDirecto.addActionListener(e -> ejecutarAccionDirecta());

        JButton btnDescartar = new JButton("Descartar Carta y Pasar Turno");
        btnDescartar.setBackground(new Color(230, 126, 34));
        btnDescartar.setForeground(Color.WHITE);
        btnDescartar.setFocusPainted(false);
        btnDescartar.addActionListener(e -> descartarCartaSeleccionada());

        JButton btnSalir = new JButton("Menú Principal");
        btnSalir.addActionListener(e -> ventana.volverAlInicio());

        panelBotones.add(btnJugarDirecto);
        panelBotones.add(btnDescartar);
        panelBotones.add(btnSalir);
        add(panelBotones, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClicTablero(e.getPoint());
            }
        });
    }

    private void manejarClicTablero(Point p) {
        if (pantallaPrivacidadActiva) {
            pantallaPrivacidadActiva = false;
            repaint();
            return;
        }

        for (int i = 0; i < hitboxesMano.size(); i++) {
            if (hitboxesMano.get(i).contains(p)) {
                cartaSeleccionada = (cartaSeleccionada == i) ? -1 : i;
                organoPropioSeleccionado = -1;
                actualizarHintCarta();
                repaint();
                return;
            }
        }

        if (cartaSeleccionada == -1) return;
        Carta carta = motor.getJugadorActual().getMano().get(cartaSeleccionada);

        for (int i = 0; i < hitboxesCuerpoPropio.size(); i++) {
            if (hitboxesCuerpoPropio.get(i).contains(p)) {
                if (carta.getTipo() == TipoCarta.MEDICINA) {
                    if (motor.jugarMedicina(cartaSeleccionada, i)) {
                        completarJugadaExitosa("Medicina aplicada.");
                    } else {
                        mensajeEstado = "No puedes aplicar esa medicina ahí.";
                    }
                } else if (carta.getTipo() == TipoCarta.TRATAMIENTO
                        && carta.getTratamiento() == TipoTratamiento.TRASPLANTE) {
                    organoPropioSeleccionado = i;
                    mensajeEstado = "Órgano propio seleccionado. Ahora haz clic en el órgano rival a intercambiar.";
                }
                repaint();
                return;
            }
        }

        for (int i = 0; i < hitboxesCuerpoRival.size(); i++) {
            if (hitboxesCuerpoRival.get(i).contains(p)) {
                if (carta.getTipo() == TipoCarta.VIRUS) {
                    if (motor.jugarVirus(cartaSeleccionada, i)) {
                        completarJugadaExitosa("¡Virus lanzado al rival!");
                    } else {
                        mensajeEstado = "Virus incompatible o el órgano está inmunizado.";
                    }
                } else if (carta.getTipo() == TipoCarta.TRATAMIENTO) {
                    if (carta.getTratamiento() == TipoTratamiento.LADRON) {
                        if (motor.jugarTratamiento(cartaSeleccionada, -1, i)) {
                            completarJugadaExitosa("¡Órgano robado con éxito!");
                        } else {
                            mensajeEstado = "No puedes robar ese órgano (inmunizado o color repetido).";
                        }
                    } else if (carta.getTratamiento() == TipoTratamiento.TRASPLANTE) {
                        if (organoPropioSeleccionado != -1 && motor.jugarTratamiento(cartaSeleccionada, organoPropioSeleccionado, i)) {
                            completarJugadaExitosa("¡Trasplante realizado!");
                        } else {
                            mensajeEstado = "Selecciona primero tu órgano propio y asegúrate de que el intercambio sea válido.";
                        }
                    }
                }
                repaint();
                return;
            }
        }
    }

    private void ejecutarAccionDirecta() {
        if (pantallaPrivacidadActiva || cartaSeleccionada == -1) return;
        Carta carta = motor.getJugadorActual().getMano().get(cartaSeleccionada);

        if (carta.getTipo() == TipoCarta.ORGANO) {
            if (motor.jugarOrgano(cartaSeleccionada)) {
                completarJugadaExitosa("Órgano colocado en tu cuerpo.");
            } else {
                mensajeEstado = "Ya tienes un órgano de ese color en tu cuerpo.";
            }
        } else if (carta.getTipo() == TipoCarta.TRATAMIENTO) {
            TipoTratamiento t = carta.getTratamiento();
            if (t == TipoTratamiento.GUANTE_LATEX || t == TipoTratamiento.ERROR_MEDICO || t == TipoTratamiento.CONTAGIO) {
                if (motor.jugarTratamiento(cartaSeleccionada, -1, -1)) {
                    completarJugadaExitosa("Tratamiento " + t.getTitulo() + " ejecutado.");
                } else {
                    mensajeEstado = "No hay objetivos válidos para ejecutar " + t.getTitulo() + ".";
                }
            } else {
                mensajeEstado = "Para " + t.getTitulo() + ", haz clic directamente sobre los órganos en el tablero.";
            }
        }
        repaint();
    }

    private void descartarCartaSeleccionada() {
        if (pantallaPrivacidadActiva || cartaSeleccionada == -1) {
            mensajeEstado = "Selecciona primero la carta de tu mano que deseas descartar.";
            repaint();
            return;
        }
        List<Integer> lista = new ArrayList<>();
        lista.add(cartaSeleccionada);
        motor.descartarSeleccionadas(lista);
        completarJugadaExitosa("Carta descartada.");
    }

    private void completarJugadaExitosa(String info) {
        cartaSeleccionada = -1;
        organoPropioSeleccionado = -1;

        Jugador ganador = motor.verificarGanador();
        if (ganador != null) {
            boolean guardado = partidaDAO.guardarPartida(
                    motor.getJugador1().getNombre(),
                    motor.getJugador2().getNombre(),
                    ganador.getNombre(),
                    motor.getContadorTurnos()
            );
            String msgNeon = guardado ? "\n(Resultado guardado en Neon PostgreSQL)" : "\n(Aviso: No se pudo conectar a Neon)";
            JOptionPane.showMessageDialog(this,
                    "¡FELICIDADES " + ganador.getNombre().toUpperCase() + "!\nHas completado 4 órganos sanos en "
                            + motor.getContadorTurnos() + " turnos." + msgNeon,
                    "¡Victoria!", JOptionPane.INFORMATION_MESSAGE);
            ventana.volverAlInicio();
            return;
        }

        motor.finalizarTurno();
        pantallaPrivacidadActiva = true;
        mensajeEstado = info + " Turno de " + motor.getJugadorActual().getNombre();
        repaint();
    }

    private void actualizarHintCarta() {
        if (cartaSeleccionada == -1) {
            mensajeEstado = "Selecciona una carta de tu mano.";
            return;
        }
        Carta c = motor.getJugadorActual().getMano().get(cartaSeleccionada);
        mensajeEstado = switch (c.getTipo()) {
            case ORGANO -> "Pulsa 'Jugar Órgano / Tratamiento Global' para bajarlo a tu cuerpo.";
            case MEDICINA -> "Haz clic sobre uno de tus órganos para curarlo o protegerlo.";
            case VIRUS -> "Haz clic sobre un órgano compatible del rival para infectarlo.";
            case TRATAMIENTO -> c.getTratamiento().getTitulo() + ": " + c.getTratamiento().getDescripcion();
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (pantallaPrivacidadActiva) {
            dibujarPantallaCambioTurno(g2);
            return;
        }

        Jugador actual = motor.getJugadorActual();
        Jugador rival = motor.getJugadorRival();

        g2.setColor(new Color(20, 30, 40));
        g2.fillRect(0, 0, getWidth(), 45);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("Turno #" + motor.getContadorTurnos() + " | Jugando: " + actual.getNombre()
                + " | Mazo: " + motor.getMazo().cartasRestantes() + " cartas", 20, 28);

        g2.setColor(new Color(231, 76, 60));
        g2.drawString("CUERPO DE RIVAL (" + rival.getNombre() + ") - Sanos: " + rival.contarOrganosSanos() + "/4", 30, 75);
        dibujarCuerpoJugador(g2, rival, 30, 90, hitboxesCuerpoRival, false);

        g2.setColor(new Color(46, 204, 113));
        g2.drawString("TU CUERPO (" + actual.getNombre() + ") - Sanos: " + actual.contarOrganosSanos() + "/4", 30, 265);
        dibujarCuerpoJugador(g2, actual, 30, 280, hitboxesCuerpoPropio, true);

        g2.setColor(Color.WHITE);
        g2.drawString("TU MANO (Haz clic para seleccionar):", 30, 460);
        hitboxesMano.clear();
        List<Carta> mano = actual.getMano();
        for (int i = 0; i < mano.size(); i++) {
            int cx = 30 + i * 135;
            int cy = 480;
            hitboxesMano.add(new Rectangle(cx, cy - 12, 115, 165));
            RenderizadorCarta.dibujarCarta(g2, mano.get(i), cx, cy, 115, 155, i == cartaSeleccionada);
        }

        g2.setColor(new Color(241, 196, 15));
        g2.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2.drawString("INSTRUCCIÓN:", 460, 510);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g2.drawString(mensajeEstado, 460, 535);
    }

    private void dibujarCuerpoJugador(Graphics2D g2, Jugador jugador, int startX, int startY,
                                      List<Rectangle> hitboxes, boolean esPropio) {
        hitboxes.clear();
        List<OrganoEnMesa> cuerpo = jugador.getCuerpo();
        if (cuerpo.isEmpty()) {
            g2.setColor(new Color(149, 165, 166));
            g2.drawRect(startX, startY, 110, 140);
            g2.drawString("Sin órganos", startX + 20, startY + 75);
            return;
        }

        for (int i = 0; i < cuerpo.size(); i++) {
            int ox = startX + i * 145;
            OrganoEnMesa org = cuerpo.get(i);
            boolean sel = esPropio && (i == organoPropioSeleccionado);

            hitboxes.add(new Rectangle(ox, startY, 115, 145));
            RenderizadorCarta.dibujarCarta(g2, org.getOrganoBase(), ox, startY, 105, 140, sel);

            int badgeY = startY + 10;
            for (Carta v : org.getVirusAplicados()) {
                g2.setColor(new Color(192, 57, 43));
                g2.fillOval(ox + 85, badgeY, 26, 26);
                g2.setColor(Color.WHITE);
                g2.drawString("☣", ox + 92, badgeY + 18);
                badgeY += 28;
            }
            for (Carta m : org.getMedicinasAplicadas()) {
                g2.setColor(new Color(39, 174, 96));
                g2.fillOval(ox + 85, badgeY, 26, 26);
                g2.setColor(Color.WHITE);
                g2.drawString("+", ox + 94, badgeY + 18);
                badgeY += 28;
            }
            if (org.estaInmunizado()) {
                g2.setColor(new Color(241, 196, 15));
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                g2.drawString("INMUNIZADO", ox + 20, startY + 152);
            }
        }
    }

    private void dibujarPantallaCambioTurno(Graphics2D g2) {
        g2.setColor(new Color(20, 28, 36));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(46, 204, 113));
        g2.setFont(new Font("SansSerif", Font.BOLD, 32));
        String texto1 = "TURNO DE: " + motor.getJugadorActual().getNombre().toUpperCase();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(texto1, (getWidth() - fm.stringWidth(texto1)) / 2, getHeight() / 2 - 20);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        String texto2 = "Pasa el computador al siguiente jugador y haz CLIC en cualquier parte para ver tus cartas.";
        fm = g2.getFontMetrics();
        g2.drawString(texto2, (getWidth() - fm.stringWidth(texto2)) / 2, getHeight() / 2 + 25);
    }
}
