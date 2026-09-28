package dao;

import conexion.ConexionBD;
import modelo.Entrega;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;

/**
 * Acceso a la tabla "entrega", que relaciona un pedido con un repartidor.
 */
public class EntregaDAO {

    /** Inserta la entrega y devuelve el id generado por la base de datos. */
    public int guardar(Entrega entrega) {
        String sql = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, Date.valueOf(entrega.getFecha()));
            ps.setTime(4, Time.valueOf(entrega.getHora()));
            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                int id = claves.next() ? claves.getInt(1) : 0;
                entrega.setId(id);
                return id;
            }

        } catch (Exception e) {
            throw new DaoException("No se pudo guardar la entrega: " + e.getMessage(), e);
        }
    }
}
