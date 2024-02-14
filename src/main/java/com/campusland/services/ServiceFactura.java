package com.campusland.services;

import java.util.List;

import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.respository.models.Factura;

public interface ServiceFactura {

    List<Factura> listar();

    List<Factura> listarPorAnho(int anho);

    void crear(Factura factura)throws FacturaExceptionInsertDataBase;

    double totalVentas();

    double totalDescuentos();

    double totalImpuestos();

    void listarClientesPorCompras();

    void listarProductosMasVendidos();

    double getImpuestoAnual(int anho);

}
