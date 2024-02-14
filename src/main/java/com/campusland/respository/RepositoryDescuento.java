package com.campusland.respository;

import com.campusland.respository.models.Descuento;

import java.util.List;

public interface RepositoryDescuento {

    List<Descuento> listar();

    List<Descuento> listarActivos();

    void crear(Descuento descuento);

    Descuento porId(int id);

    Descuento porProducto(int id);

    int validarCantidadCliente(int idCliente);
    void actualizar(Descuento descuento);

    void eliminar(int id);
}
