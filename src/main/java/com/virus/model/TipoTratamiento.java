package com.virus.model;

public enum TipoTratamiento {
    NINGUNO("", ""),
    TRASPLANTE("Trasplante", "Intercambia un órgano con un rival"),
    LADRON("Ladrón de Órganos", "Roba un órgano no inmunizado de un rival"),
    CONTAGIO("Contagio", "Pasa tus virus a órganos libres rivales"),
    GUANTE_LATEX("Guante de Látex", "El rival descarta toda su mano"),
    ERROR_MEDICO("Error Médico", "Intercambia todo tu cuerpo con el rival");

    private final String titulo;
    private final String descripcion;

    TipoTratamiento(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
