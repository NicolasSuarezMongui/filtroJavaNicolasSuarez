package com.campusland.respository.impl.impldescuento;

import com.campusland.respository.RepositoryDescuento;
import com.campusland.respository.models.Cliente;
import com.campusland.respository.models.Descuento;
import com.campusland.respository.models.Factura;
import com.campusland.utils.Formato;
import com.campusland.utils.conexionpersistencia.conexionbdmysql.ConexionBDMysql;
import com.campusland.utils.conexionpersistencia.conexiondblist.ConexionBDList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RepositoryDescuentoMysqlImpl implements RepositoryDescuento {

    private Connection getConnection() throws SQLException {
        return ConexionBDMysql.getInstance();
    }

    @Override
    public List<Descuento> listar() {
        List<Descuento> listaDescuentos = new ArrayList<>();
        try (Statement statement = getConnection().createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM descuentos")) {
            while (rs.next()) {
                listaDescuentos.add(creadescuento(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDescuentos;
    }

    @Override
    public List<Descuento> listarActivos() {
        List<Descuento> listaDescuentos = new ArrayList<>();
        try (Statement statement = getConnection().createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM descuentos WHERE activo = true")) {
            while (rs.next()) {
                listaDescuentos.add(creadescuento(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDescuentos;
    }

    @Override
    public void crear(Descuento descuento) {

    }

    @Override
    public Descuento porId(int id) {
        Descuento descuento = null;
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM descuentos WHERE id_descuento = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    descuento = creadescuento(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return descuento;
    }

    @Override
    public Descuento porProducto(int id) {
        Descuento descuento = null;
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM descuentos WHERE producto = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    descuento = creadescuento(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return descuento;
    }

    @Override
    public int validarCantidadCliente(int clienteId) {
        int cantidad = 0;
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT COUNT(*) FROM factura WHERE cliente_id = ?")) {
            preparedStatement.setInt(1, clienteId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    cantidad = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cantidad;
    }

    @Override
    public void actualizar(Descuento descuento) {

    }

    @Override
    public void eliminar(int id) {

    }

    private Descuento creadescuento(ResultSet rs) throws SQLException {
        final int descuento_id = rs.getInt("id_descuento");
        final String tipo = rs.getString("tipo");
        final String condicion = rs.getString("condicion");
        final float porcentaje = rs.getFloat("porcentaje");
        final int producto = rs.getInt("producto");
        final Boolean activo = rs.getBoolean("activo");
        return new Descuento(descuento_id, tipo, condicion, porcentaje, producto, activo);
    }

}
