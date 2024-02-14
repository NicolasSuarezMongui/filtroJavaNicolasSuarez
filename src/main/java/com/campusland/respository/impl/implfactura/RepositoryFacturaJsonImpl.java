package com.campusland.respository.impl.implfactura;

import java.util.List;

import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.respository.RepositoryFactura;
import com.campusland.respository.models.Factura;
import com.campusland.utils.conexionpersistencia.conexionbdjson.ConexionBDJsonFacturas;

public class RepositoryFacturaJsonImpl implements RepositoryFactura {

    ConexionBDJsonFacturas conexion = ConexionBDJsonFacturas.getConexion();

    @Override
    public List<Factura> listar() {
        return conexion.getData(Factura.class);
    }

    @Override
    public void crear(Factura factura) throws FacturaExceptionInsertDataBase {
        List<Factura> listFacturas = conexion.getData(Factura.class);
        listFacturas.add(factura);
        conexion.saveData(listFacturas);
       
    }

    @Override
    public void listarClientesPorCompras() {
        // TODO Auto-generated method stub

    }

    @Override
    public void listarProductosMasVendidos() {
        // TODO Auto-generated method stub

    }

    @Override
    public double getImpuestoAnual(int anho) {
        // TODO Auto-generated method stub
        return 0;
    }

}
