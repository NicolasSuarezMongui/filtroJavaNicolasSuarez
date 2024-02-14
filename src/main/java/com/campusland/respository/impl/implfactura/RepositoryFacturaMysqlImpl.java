package com.campusland.respository.impl.implfactura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.respository.RepositoryFactura;
import com.campusland.respository.models.Cliente;
import com.campusland.respository.models.Factura;
import com.campusland.respository.models.ItemFactura;
import com.campusland.respository.models.Producto;
import com.campusland.utils.Formato;
import com.campusland.utils.conexionpersistencia.conexionbdmysql.ConexionBDMysql;


import java.sql.Statement;

public class RepositoryFacturaMysqlImpl implements RepositoryFactura {

    private static final String SQL_GET_LIST_FACTURAS = "SELECT f.numeroFactura, f.fecha, c.id, c.nombre, c.apellido, c.email, c.direccion, c.celular, c.documento, f.descuento, f.impuesto FROM factura f JOIN cliente c ON c.id=f.cliente_id ORDER BY f.numeroFactura asc";
    private static final String SQL_GET_LIST_ITEMS_FACTURAS = "SELECT i.id,i.cantidad,i.importe,p.codigo,p.nombre,p.descripcion,p.precioVenta,p.precioCompra  FROM item_factura i join producto p on i.producto_codigo=p.codigo WHERE i.factura_numeroFactura =?";
    private static final String INSERT_FACTURA = "INSERT INTO factura (fecha, cliente_id, descuento, impuesto) VALUES (?, ?, ?, ?)";
    private static final String INSERT_ITEM_FACTURA = "INSERT INTO item_factura (factura_numeroFactura, producto_codigo, cantidad, importe) VALUES (?, ?, ?, ?)";

    private static final String SQL_GET_LIST_CLIENTES_COMPRAS = "SELECT c.id, c.nombre, c.apellido, SUM(itf.importe) AS total FROM cliente c INNER JOIN factura ON c.id = factura.cliente_id INNER JOIN item_factura itf ON factura.numeroFactura = itf.factura_numeroFactura GROUP BY c.id ORDER BY total DESC";
    private static final String SQL_GET_LIST_PRODUCTOS_VENDIDOS = "SELECT P.nombre, SUM(itf.cantidad) AS total FROM producto p INNER JOIN item_factura itf ON p.codigo = itf.producto_codigo GROUP BY p.codigo ORDER BY total DESC";



    private Connection getConnection() throws SQLException {
        return ConexionBDMysql.getInstance();
    }

    @Override
    public List<Factura> listar() {
        List<Factura> listFacturas = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQL_GET_LIST_FACTURAS);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Factura factura = creaFactura(rs);
                obtenerItemsFactura(conn,factura);
                listFacturas.add(factura);
            }

        } catch (SQLException e) {
            
                e.printStackTrace();
            
        }

       
        return listFacturas;
    }

    @Override
    public void listarClientesPorCompras() {
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(SQL_GET_LIST_CLIENTES_COMPRAS)) {
            while (rs.next()) {
                System.out.println("Cliente: " + rs.getString("nombre") + " " + rs.getString("apellido") + " Total: " + rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getImpuestoAnual(int anho){
        double value = 0;
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT porcentaje FROM impuestos WHERE anho = ?")) {
            preparedStatement.setInt(1, anho);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    value = rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public void listarProductosMasVendidos() {
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(SQL_GET_LIST_PRODUCTOS_VENDIDOS)) {
            while (rs.next()) {
                System.out.println("Producto: " + rs.getString("nombre") + " Total: " + rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void obtenerItemsFactura(Connection connection, Factura factura) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_LIST_ITEMS_FACTURAS)) {
            preparedStatement.setInt(1, factura.getNumeroFactura());
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    factura.agregarItem(crearItems(rs));
                }
            }
        }
    }

    @Override
    public void crear(Factura factura) throws FacturaExceptionInsertDataBase {
        Connection conn=null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psFactura = conn.prepareStatement(INSERT_FACTURA,
                    Statement.RETURN_GENERATED_KEYS)) {
                psFactura.setDate(1, Formato.convertirLocalDateTimeSqlDate(factura.getFecha()));
                psFactura.setInt(2, factura.getCliente().getId());
                psFactura.setDouble(3, factura.getDescuento());
                psFactura.setDouble(4, factura.getImpuesto());
                psFactura.executeUpdate();

                try (ResultSet generatedKeys = psFactura.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        factura.setNumeroFactura(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("No se pudo obtener el valor autoincremental para numeroFactura");
                    }
                }
            }

            try (PreparedStatement psItem = conn.prepareStatement(INSERT_ITEM_FACTURA)) {
                for (ItemFactura item : factura.getItems()) {
                    psItem.setInt(1, factura.getNumeroFactura());
                    psItem.setInt(2, item.getProducto().getCodigo());
                    psItem.setInt(3, item.getCantidad());
                    psItem.setDouble(4, item.getImporte());
                    psItem.addBatch();
                }
                psItem.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();                    
                }
            }
            e.printStackTrace();
            throw new FacturaExceptionInsertDataBase("No se pudo hacer el insert en la base de datos de factura");
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeException) {
                    closeException.printStackTrace();                    
                }
            }
        }
    }

   

    private Factura creaFactura(ResultSet rs) throws SQLException {
        final int clienteId = rs.getInt("id");
        final String documento = rs.getString("documento");
        final String nombre = rs.getString("nombre");
        final String apellido = rs.getString("apellido");
        final String email = rs.getString("email");
        final String direccion = rs.getString("direccion");
        final String celular = rs.getString("celular");
        final Cliente cliente = new Cliente(clienteId, documento, nombre, apellido, email, direccion, celular);
        final int numeroFactura = rs.getInt("numeroFactura");
        final LocalDateTime fecha = Formato.convertirTimestampFecha(rs.getTimestamp("fecha"));
        final double descuento = rs.getDouble("descuento");
        final double impuesto = rs.getDouble("impuesto");
        return new Factura(numeroFactura, fecha, cliente, descuento, impuesto);

    }

    private ItemFactura crearItems(ResultSet rs) throws SQLException {
        final int codigo = rs.getInt("codigo");
        final String nombre = rs.getString("nombre");
        final String descripcion = rs.getString("descripcion");
        final double precioVenta = rs.getDouble("precioVenta");
        final double precioCompra = rs.getDouble("precioCompra");
        final Producto producto = new Producto(codigo, nombre, descripcion, precioVenta, precioCompra);
        return new ItemFactura(rs.getInt("cantidad"), producto);

    }

}
