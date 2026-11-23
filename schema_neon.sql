CREATE TABLE IF NOT EXISTS historial_partidas (
    id SERIAL PRIMARY KEY,
    jugador_1 VARCHAR(60) NOT NULL,
    jugador_2 VARCHAR(60) NOT NULL,
    ganador VARCHAR(60) NOT NULL,
    turnos_totales INT NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
