package com.campusland.utils.conexionpersistencia.conexionbdjson;

import com.campusland.respository.models.Impuesto;

public class ConexionBDJsonImpuestos extends ConexionBDJsonBase<Impuesto>{

    private static ConexionBDJsonImpuestos conexionImpuestos;

    private ConexionBDJsonImpuestos() {
        super("impuestos.json");
    }

    public static ConexionBDJsonImpuestos getConexion() {
        if (conexionImpuestos != null) {
            return conexionImpuestos;
        } else {
            conexionImpuestos = new ConexionBDJsonImpuestos();
            return conexionImpuestos;
        }
    }

}
