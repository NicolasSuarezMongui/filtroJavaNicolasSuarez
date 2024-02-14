package com.campusland.respository.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Descuento {

    private int id_descuento;
    private String tipo;
    private String condicion;

    private float porcentaje;
    private int producto_id;
    private boolean estado;

    public Descuento(String tipo, String condicion, float porcentaje, int producto_id, boolean estado) {
        this.tipo = tipo;
        this.condicion = condicion;
        this.porcentaje = porcentaje;
        this.producto_id = producto_id;
        this.estado = estado;
    }


}
