package com.campusland.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.respository.RepositoryFactura;
import com.campusland.respository.models.Factura;
import com.campusland.services.ServiceFactura;

public class ServiceFacturaImpl implements ServiceFactura {

  private final RepositoryFactura repositoryFacturaMysql;
  private final RepositoryFactura repositoryFacturaJson;

  public ServiceFacturaImpl(RepositoryFactura repositoryFacturaMysql, RepositoryFactura repositoryFacturaJson) {
    this.repositoryFacturaMysql = repositoryFacturaMysql;
    this.repositoryFacturaJson = repositoryFacturaJson;
  }

  @Override
  public List<Factura> listar() {
    return this.repositoryFacturaMysql.listar();
  }

    @Override
    public List<Factura> listarPorAnho(int anho) {
        List<Factura> facturas = new ArrayList<>();
        for (Factura factura : listar()) {
            if (factura.getFecha().getYear() == anho) {
                facturas.add(factura);
            }
        }
        return facturas;
    }

  public double totalVentas() {
      double totalVentas = 0;
      for (Factura factura : listar()) {
          totalVentas += factura.getTotalFactura();
      }
      return totalVentas;
  }

    public double totalDescuentos() {
        double totalDescuentos = 0;
        for (Factura factura : listar()) {
            totalDescuentos += factura.getDescuento();
        }
        return totalDescuentos;
    }

    public double totalImpuestos() {
        double totalImpuestos = 0;
        for (Factura factura : listar()) {
            totalImpuestos += factura.getImpuesto();
        }
        return totalImpuestos;
    }

    public void listarClientesPorCompras() {
        this.repositoryFacturaMysql.listarClientesPorCompras();
    }

    public void listarProductosMasVendidos() {
        this.repositoryFacturaMysql.listarProductosMasVendidos();
    }

    public double getImpuestoAnual(int anho) {
        return this.repositoryFacturaMysql.getImpuestoAnual(anho);
    }

  @Override
  public void crear(Factura factura) {
      try {
          guardarEnMysqlYJson(factura);
      } catch (FacturaExceptionInsertDataBase e) {
         e.printStackTrace();
      }
  }
  
  private void guardarEnMysqlYJson(Factura factura) throws FacturaExceptionInsertDataBase {
      guardarEnMysql(factura);
      guardarEnJson(factura);
  }
  
  private void guardarEnMysql(Factura factura) throws FacturaExceptionInsertDataBase {
      this.repositoryFacturaMysql.crear(factura);
  }
  
  private void guardarEnJson(Factura factura) throws FacturaExceptionInsertDataBase {
      try {
          this.repositoryFacturaJson.crear(factura);
      } catch (FacturaExceptionInsertDataBase eJson) {
          eJson.printStackTrace();
      }
  }
  

}
