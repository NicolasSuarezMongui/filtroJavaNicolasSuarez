package com.campusland.respository.impl.implfactura;

import java.util.List;

import com.campusland.respository.RepositoryFactura;
import com.campusland.respository.models.Factura;
import com.campusland.utils.conexionpersistencia.conexiondblist.ConexionBDList;

public class RepositoryFacturaImp implements RepositoryFactura {

    ConexionBDList conexion = ConexionBDList.getConexion();

    @Override
    public List<Factura> listar() {
        return conexion.getListFacturas();
        
    }

    @Override
    public void crear(Factura factura) {
       conexion.getListFacturas().add(factura);
        
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
