package com.virus.view;

import com.virus.model.*;
import java.awt.*;
import java.awt.geom.*;

public class RenderizadorCarta {

    public static void dibujarCarta(Graphics2D g2, Carta carta, int x, int y, int ancho, int alto, boolean seleccionada) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int offsetY = seleccionada ? -12 : 0;
        int drawY = y + offsetY;

        g2.setColor(new Color(0, 0, 0, 70));
        g2.fillRoundRect(x + 4, drawY + 6, ancho, alto, 18, 18);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, drawY, ancho, alto, 18, 18);

        Color colorPrincipal = carta.getColor().getColorAwt();
        g2.setColor(seleccionada ? new Color(255, 215, 0) : colorPrincipal);
        g2.setStroke(new BasicStroke(seleccionada ? 4f : 3f));
        g2.drawRoundRect(x, drawY, ancho, alto, 18, 18);

        Shape clipOriginal = g2.getClip();
        g2.setClip(new RoundRectangle2D.Float(x + 2, drawY + 2, ancho - 4, 28, 14, 14));
        if (carta.getColor() == ColorCarta.MULTICOLOR) {
            dibujarFondoMulticolor(g2, x, drawY, ancho, 30);
        } else {
            g2.setColor(colorPrincipal);
            g2.fillRect(x, drawY, ancho, 30);
        }
        g2.setClip(clipOriginal);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        String tituloTipo = carta.getTipo().name();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(tituloTipo, x + (ancho - fm.stringWidth(tituloTipo)) / 2, drawY + 19);

        int cx = x + ancho / 2;
        int cy = drawY + alto / 2 + 4;
        dibujarIlustracionCentral(g2, carta, cx, cy);

        g2.setColor(new Color(40, 40, 40));
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        String pie = carta.getTipo() == TipoCarta.TRATAMIENTO
                ? carta.getTratamiento().getTitulo()
                : carta.getColor().getNombreOrgano();
        fm = g2.getFontMetrics();
        g2.drawString(pie, x + (ancho - fm.stringWidth(pie)) / 2, drawY + alto - 10);
    }

    private static void dibujarFondoMulticolor(Graphics2D g2, int x, int y, int ancho, int alto) {
        Color[] cols = {ColorCarta.ROJO.getColorAwt(), ColorCarta.VERDE.getColorAwt(),
                        ColorCarta.AZUL.getColorAwt(), ColorCarta.AMARILLO.getColorAwt()};
        int franja = ancho / 4 + 1;
        for (int i = 0; i < 4; i++) {
            g2.setColor(cols[i]);
            g2.fillRect(x + i * (ancho / 4), y, franja, alto);
        }
    }

    private static void dibujarIlustracionCentral(Graphics2D g2, Carta carta, int cx, int cy) {
        Color c = carta.getColor().getColorAwt();
        g2.setColor(c);

        switch (carta.getTipo()) {
            case ORGANO:
                dibujarOrgano(g2, carta.getColor(), cx, cy);
                break;
            case VIRUS:
                dibujarVirus(g2, c, cx, cy);
                break;
            case MEDICINA:
                dibujarMedicina(g2, c, cx, cy);
                break;
            case TRATAMIENTO:
                dibujarTratamiento(g2, carta.getTratamiento(), cx, cy);
                break;
        }
    }

    private static void dibujarOrgano(Graphics2D g2, ColorCarta color, int cx, int cy) {
        if (color == ColorCarta.MULTICOLOR) {
            dibujarVirus(g2, new Color(155, 89, 182), cx, cy);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString("★", cx - 7, cy + 6);
            return;
        }
        switch (color) {
            case ROJO:
                g2.fillOval(cx - 22, cy - 16, 24, 24);
                g2.fillOval(cx - 2, cy - 16, 24, 24);
                int[] px = {cx - 21, cx + 21, cx};
                int[] py = {cy - 2, cy - 2, cy + 24};
                g2.fillPolygon(px, py, 3);
                break;
            case AZUL:
                g2.fillRoundRect(cx - 22, cy - 16, 44, 32, 22, 22);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(cx, cy - 15, cx, cy + 15);
                g2.drawArc(cx - 16, cy - 10, 12, 10, 0, 180);
                g2.drawArc(cx + 4, cy - 10, 12, 10, 0, 180);
                break;
            case VERDE:
                g2.fillArc(cx - 22, cy - 18, 42, 36, 180, 230);
                g2.fillOval(cx - 10, cy - 12, 26, 22);
                break;
            case AMARILLO:
                g2.fillRect(cx - 16, cy - 6, 32, 12);
                g2.fillOval(cx - 22, cy - 12, 14, 14);
                g2.fillOval(cx - 22, cy - 2, 14, 14);
                g2.fillOval(cx + 8, cy - 12, 14, 14);
                g2.fillOval(cx + 8, cy - 2, 14, 14);
                break;
            default:
                break;
        }
    }

    private static void dibujarVirus(Graphics2D g2, Color c, int cx, int cy) {
        g2.setColor(c);
        g2.setStroke(new BasicStroke(3f));
        for (int i = 0; i < 8; i++) {
            double ang = Math.toRadians(i * 45);
            int x2 = cx + (int) (Math.cos(ang) * 26);
            int y2 = cy + (int) (Math.sin(ang) * 26);
            g2.drawLine(cx, cy, x2, y2);
            g2.fillOval(x2 - 3, y2 - 3, 6, 6);
        }
        g2.fillOval(cx - 16, cy - 16, 32, 32);
        g2.setColor(Color.WHITE);
        g2.fillOval(cx - 9, cy - 5, 7, 7);
        g2.fillOval(cx + 2, cy - 5, 7, 7);
        g2.setColor(Color.BLACK);
        g2.fillOval(cx - 7, cy - 3, 3, 3);
        g2.fillOval(cx + 4, cy - 3, 3, 3);
    }

    private static void dibujarMedicina(Graphics2D g2, Color c, int cx, int cy) {
        Graphics2D gRot = (Graphics2D) g2.create();
        gRot.rotate(Math.toRadians(-35), cx, cy);
        gRot.setColor(c);
        gRot.fillRoundRect(cx - 22, cy - 11, 44, 22, 20, 20);
        gRot.setColor(new Color(240, 240, 240));
        gRot.fillRect(cx, cy - 10, 20, 20);
        gRot.setColor(c.darker());
        gRot.setStroke(new BasicStroke(2f));
        gRot.drawRoundRect(cx - 22, cy - 11, 44, 22, 20, 20);
        gRot.dispose();
    }

    private static void dibujarTratamiento(Graphics2D g2, TipoTratamiento t, int cx, int cy) {
        g2.setColor(new Color(142, 68, 173));
        g2.fillOval(cx - 20, cy - 20, 40, 40);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        String simbolo = switch (t) {
            case TRASPLANTE -> "⇄";
            case LADRON -> "✋";
            case CONTAGIO -> "☣";
            case GUANTE_LATEX -> "🧤";
            case ERROR_MEDICO -> "⚕";
            default -> "!";
        };
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(simbolo, cx - fm.stringWidth(simbolo) / 2, cy + 7);
    }
}
