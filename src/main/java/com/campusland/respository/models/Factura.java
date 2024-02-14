package com.campusland.respository.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.campusland.utils.Formato;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Factura {

    private int numeroFactura;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")    
    private LocalDateTime fecha;
    private Cliente cliente;
    private List<ItemFactura> items;

    private double descuento;

    private double impuesto;
    private static int nextNumeroFactura;

    public Factura(){

    }

  

    public Factura(int numeroFactura, LocalDateTime fecha, Cliente cliente, double descuento, double impuesto) {
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.descuento = descuento;
        this.impuesto = impuesto;
    }

    public Factura(LocalDateTime fecha, Cliente cliente, double descuento, double impuesto) {
        this.numeroFactura = ++nextNumeroFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.descuento = descuento;
        this.impuesto = impuesto;
    }

    public Factura(LocalDateTime fecha, Cliente cliente) {
        this.numeroFactura = ++nextNumeroFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.items = new ArrayList<>();
    }

    public void calcTotalDescuentos(List<Descuento> descuentos) {
        double totalDescuentos = 0;
        for (Descuento descuento : descuentos) {
            if (descuento.getTipo().equals("Porcentaje")){
                totalDescuentos += this.getTotalFactura() * (descuento.getPorcentaje() / 100);
            }else {
                totalDescuentos += descuento.getPorcentaje();

            }
        }
        this.descuento = totalDescuentos;
    }

    public void calcImpuesto(double impuesto) {
        this.impuesto = (calcularSubtotal()-this.descuento) * impuesto / 100;
    }

    public double calcularSubtotal(){
        double totalFactura = 0;
        for (ItemFactura item : items) {
            totalFactura += item.getImporte();
        }
        return totalFactura;
    }

    public double getTotalFactura() {
        return calcularSubtotal() - this.descuento + this.impuesto;
    }


    public void agregarItem(ItemFactura item){
        this.items.add(item);
    }

    public void display() {
        System.out.println();
        System.out.println("Factura: " + this.numeroFactura);
        System.out.println("Cliente: " + this.cliente.getFullName());
        System.out.println("Fecha: " + Formato.formatoFechaHora(this.getFecha()));
        System.out.println("-----------Detalle Factura----------------------");
        for (ItemFactura item : this.items) {
            System.out.println(item.getProducto().getCodigoNombre() + " " + item.getCantidad() + " "
                    + Formato.formatoMonedaPesos(item.getImporte()));

        }
        System.out.println();
        System.out.println("Total Descuentos             " + Formato.formatoMonedaPesos(this.descuento));
        System.out.println();
        System.out.println("Total Impuesto               " + Formato.formatoMonedaPesos(this.impuesto));
        System.out.println();
        System.out.println("Total                        " + Formato.formatoMonedaPesos(this.getTotalFactura()));
        System.out.println();
    }

    public void display(List<Descuento> descuentos) {
        System.out.println();
        System.out.println("Factura: " + this.numeroFactura);
        System.out.println("Cliente: " + this.cliente.getFullName());
        System.out.println("Fecha: " + Formato.formatoFechaHora(this.getFecha()));
        System.out.println("-----------Detalle Factura----------------------");
        for (ItemFactura item : this.items) {
            System.out.println(item.getProducto().getCodigoNombre() + " " + item.getCantidad() + " "
                    + Formato.formatoMonedaPesos(item.getImporte()));

        }
        for (Descuento descuento : descuentos) {
            System.out.println("Descuento: " + descuento.getCondicion() + " " + Formato.formatoMonedaPesos(descuento.getPorcentaje()));
        }
        System.out.println();
        System.out.println("Total Descuentos             " + Formato.formatoMonedaPesos(this.descuento));
        System.out.println();
        System.out.println("Total Impuesto               " + Formato.formatoMonedaPesos(this.impuesto));
        System.out.println();
        System.out.println("Total                        " + Formato.formatoMonedaPesos(this.getTotalFactura()));
        System.out.println();
    }

}
