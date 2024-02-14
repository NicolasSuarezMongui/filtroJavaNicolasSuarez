package com.campusland.services;

import com.campusland.respository.models.Descuento;
import com.campusland.respository.models.Factura;

import java.util.List;

public interface ServiceDescuento {

    List<Descuento> listar();

    List<Descuento> listarActivos();

    List<Descuento> listarAAplicar(Factura factura);

}
