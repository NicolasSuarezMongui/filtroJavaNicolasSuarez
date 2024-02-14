package com.campusland.respository.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Impuesto {

    private Cliente cliente;
    private int numeroFactura;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fecha_factura;
    private double totalFactura;

    private double impuesto;

    public Impuesto(Factura factura) {
        this.cliente = factura.getCliente();
        this.numeroFactura = factura.getNumeroFactura();
        this.fecha_factura = factura.getFecha();
        this.totalFactura = factura.getTotalFactura();
        this.impuesto = factura.getImpuesto();
    }


}
