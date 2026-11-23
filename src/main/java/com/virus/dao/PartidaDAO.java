package com.virus.dao;

import com.virus.model.EstadoPartida;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartidaDAO {

    public boolean guardarPartida(String jugador1, String jugador2, String ganador, int turnos) {
        String sql = "INSERT INTO historial_partidas (jugador_1, jugador_2, ganador, turnos_totales) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionNeon.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, jugador1);
            stmt.setString(2, jugador2);
            stmt.setString(3, ganador);
            stmt.setInt(4, turnos);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al guardar partida en Neon: " + e.getMessage());
            return false;
        }
    }

    public List<EstadoPartida> listarUltimasPartidas(int limite) {
        List<EstadoPartida> lista = new ArrayList<>();
        String sql = "SELECT jugador_1, jugador_2, ganador, turnos_totales, fecha_registro " +
                     "FROM historial_partidas ORDER BY fecha_registro DESC LIMIT ?";

        try (Connection conn = ConexionNeon.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limite);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new EstadoPartida(
                            rs.getString("jugador_1"),
                            rs.getString("jugador_2"),
                            rs.getString("ganador"),
                            rs.getInt("turnos_totales"),
                            rs.getTimestamp("fecha_registro").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar historial en Neon: " + e.getMessage());
        }
        return lista;
    }
}
