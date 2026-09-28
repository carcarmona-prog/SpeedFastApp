package dao;

import conexion.ConexionBD;
import modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a la tabla "repartidor" (id, nombre).
 */
public class RepartidorDAO {

    /** Inserta el repartidor y devuelve el id generado por la base de datos. */
    public int guardar(Repartidor repartidor) {
        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, repartidor.getNombreRepartidor());
            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                int id = claves.next() ? claves.getInt(1) : 0;
                repartidor.setId(id);
                return id;
            }

        } catch (Exception e) {
            throw new DaoException("No se pudo guardar el repartidor: " + e.getMessage(), e);
        }
    }

    /** Devuelve todos los repartidores registrados. */
    public List<Repartidor> listarTodos() {
        String sql = "SELECT id, nombre FROM repartidor ORDER BY id";
        List<Repartidor> repartidores = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Repartidor repartidor = new Repartidor(rs.getString("nombre"));
                repartidor.setId(rs.getInt("id"));
                repartidores.add(repartidor);
            }
            return repartidores;

        } catch (Exception e) {
            throw new DaoException("No se pudieron consultar los repartidores: " + e.getMessage(), e);
        }
    }
}
