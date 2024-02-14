package com.campusland.respository;

import com.campusland.respository.models.Factura;
import com.campusland.respository.models.Impuesto;

import java.util.List;

public interface RepositoryImpuesto {

    List<Impuesto> listarImpuestos();
    void crearImpuesto(Impuesto impuesto);

}
