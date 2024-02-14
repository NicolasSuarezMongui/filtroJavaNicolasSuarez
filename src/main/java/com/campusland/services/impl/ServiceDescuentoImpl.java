package com.campusland.services.impl;

import com.campusland.respository.RepositoryDescuento;
import com.campusland.respository.models.Descuento;
import com.campusland.respository.models.Factura;
import com.campusland.respository.models.ItemFactura;
import com.campusland.services.ServiceDescuento;

import java.util.ArrayList;
import java.util.List;

public class ServiceDescuentoImpl implements ServiceDescuento {

    private final RepositoryDescuento repositoryDescuento;

    public ServiceDescuentoImpl(RepositoryDescuento repositoryDescuento) {
        this.repositoryDescuento = repositoryDescuento;
    }

    @Override
    public List<Descuento> listar() {
        return this.repositoryDescuento.listar();
    }

    @Override
    public List<Descuento> listarActivos() {
        return this.repositoryDescuento.listarActivos();
    }

    @Override
    public List<Descuento> listarAAplicar(Factura factura) {
        List<Descuento> descuentos = this.repositoryDescuento.listarActivos();
        List<Descuento> descuentos_aplicables = new ArrayList<>();
        List<Integer> productosAplicables = new ArrayList<>();

        for (Descuento descuento : descuentos){
            if (descuento.getProducto_id() != 0){
                productosAplicables.add(descuento.getProducto_id());
            }
        }

        for (Descuento descuento: descuentos){
            switch (descuento.getId_descuento()){
                case 1:
                    if (factura.calcularSubtotal() > 1000){
                        descuentos_aplicables.add(descuento);
                    }
                    break;
                case 2:
                    for (ItemFactura itemFactura : factura.getItems()){
                        if (productosAplicables.contains(itemFactura.getProducto().getCodigo())){
                            if (itemFactura.getCantidad() >= 5) {
                                descuentos_aplicables.add(this.repositoryDescuento.porProducto(itemFactura.getProducto().getCodigo()));
                            }
                        }
                    }
                    break;
                case 3:
                    if (this.repositoryDescuento.validarCantidadCliente(factura.getCliente().getId()) > 10){
                        descuentos_aplicables.add(descuento);
                    }
                    break;
                case 4:
                    if (factura.getFecha().getDayOfWeek().getValue() == 5){
                        descuentos_aplicables.add(descuento);
                    }
                case 5:
                    if (factura.getFecha().getMonth().getValue() == 12){
                        descuentos_aplicables.add(descuento);
                    }

            }
        }




        return descuentos_aplicables;
    }



}
