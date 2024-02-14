package com.campusland.respository.impl;

import com.campusland.respository.RepositoryImpuesto;
import com.campusland.respository.models.Impuesto;
import com.campusland.utils.conexionpersistencia.conexionbdjson.ConexionBDJsonImpuestos;

import java.util.List;

public class RepositoryImpuestoJsonImpl implements RepositoryImpuesto {

    ConexionBDJsonImpuestos conexion = ConexionBDJsonImpuestos.getConexion();

    @Override
    public List<Impuesto> listarImpuestos() {
        return conexion.getData(Impuesto.class);
    }

    @Override
    public void crearImpuesto(Impuesto impuesto) {
        List<Impuesto> listImpuestos = conexion.getData(Impuesto.class);
        listImpuestos.add(impuesto);
        conexion.saveData(listImpuestos);
    }

}

