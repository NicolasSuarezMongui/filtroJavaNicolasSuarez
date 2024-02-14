package com.campusland.views;

import java.time.LocalDateTime;
import java.util.List;

import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.respository.models.*;

public class ViewFactura extends ViewMain {

    public static void startMenu() {

        int op = 0;

        do {

            op = mostrarMenu();
            switch (op) {
                case 1:
                    crearFactura();
                    break;
                case 2:
                    listarFactura();
                    break;
                case 3:
                    crearReporteDian();
                    break;
                case 4:
                    reporteTotales();
                    break;
                case 5:
                    reporteClientes();
                    break;
                case 6:
                    reporteProductos();
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }

        } while (op >= 1 && op < 7);

    }

    public static int mostrarMenu() {
        System.out.println("----Menu--Factura----");
        System.out.println("1. Crear factura.");
        System.out.println("2. Listar factura.");
        System.out.println("3. Generar archivo DIAN por año.");
        System.out.println("4. Generar total ventas, descuentos, e impuestos.");
        System.out.println("5. Listado descendente clientes por compras.");
        System.out.println("6. Listado descendente producto más vendido.");
        System.out.println("7. Salir ");
        return leer.nextInt();
    }

    public static void reporteTotales(){
        System.out.println("Total ventas: " + serviceFactura.totalVentas());
        System.out.println("Total descuentos: " + serviceFactura.totalDescuentos());
        System.out.println("Total impuestos: " + serviceFactura.totalImpuestos());
    }

    public static void reporteClientes(){
        System.out.println("Listado descendente clientes por compras");
        serviceFactura.listarClientesPorCompras();
    }

    public static void reporteProductos(){
        System.out.println("Listado descendente producto más vendido");
        serviceFactura.listarProductosMasVendidos();
    }

    public static void listarFactura() {
        System.out.println("Lista de Facturas");
        for (Factura factura : serviceFactura.listar()) {
            factura.display();
            System.out.println();
        }
    }

    public static void crearReporteDian(){
        System.out.println("Generar archivo DIAN por año");
        int anho = 2023;
        for (Factura factura : serviceFactura.listarPorAnho(anho)) {
            repositoryImpuesto.crearImpuesto(new Impuesto(factura));
        }
    }

    public static void crearFactura() {
        System.out.println("-- Creación de Factura ---");

        Cliente cliente;
        do {
            cliente = ViewCliente.buscarGetCliente();
        } while (cliente == null);

        Factura factura = new Factura(LocalDateTime.now(), cliente);
        System.out.println("-- Se creó la factura -----------------");
        System.out.println("-- Seleccione los productos a comprar por código");
     

        do {
            Producto producto = ViewProducto.buscarGetProducto();

            if (producto != null) {
                System.out.print("Cantidad: ");
                int cantidad = leer.nextInt();
                ItemFactura item = new ItemFactura(cantidad, producto);
                factura.agregarItem(item);

                System.out.println("Agregar otro producto: si o no");
                String otroItem = leer.next();
                if (!otroItem.equalsIgnoreCase("si")) {
                    break;
                }
            }

        } while (true);

        try {
            factura.calcTotalDescuentos(serviceDescuento.listarAAplicar(factura));
            factura.calcImpuesto(serviceFactura.getImpuestoAnual(factura.getFecha().getYear()));
            serviceFactura.crear(factura);
            System.out.println("Se creó la factura");
            List<Descuento> descuentos = serviceDescuento.listarAAplicar(factura);
            factura.display(descuentos);
        } catch (FacturaExceptionInsertDataBase e) {
            System.out.println(e.getMessage());
        }
    }

}
