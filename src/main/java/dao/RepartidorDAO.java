package dao;

import conexion.ConexionBD;
import modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a la tabla "repartidor" (id, nombre). CRUD completo.
 */
public class RepartidorDAO implements CrudDAO<Repartidor, Integer> {

    /** Inserta el repartidor y devuelve el id generado por la base de datos. */
    @Override
    public Integer create(Repartidor repartidor) {
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
    @Override
    public List<Repartidor> readAll() {
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

    /** Actualiza el nombre de un repartidor ya existente (por su id). */
    @Override
    public void update(Repartidor repartidor) {
        String sql = "UPDATE repartidor SET nombre = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, repartidor.getNombreRepartidor());
            ps.setInt(2, repartidor.getId());

            if (ps.executeUpdate() == 0) {
                throw new DaoException("No existe un repartidor con id " + repartidor.getId() + ".", null);
            }

        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("No se pudo actualizar el repartidor #" + repartidor.getId() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Elimina el repartidor indicado. Si tiene entregas asociadas, la base
     * de datos rechaza el borrado por la clave foránea de "entrega".
     */
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM repartidor WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoException("No se puede eliminar el repartidor #" + id
                    + " porque tiene una entrega registrada. Elimine primero esa entrega.", e);
        } catch (Exception e) {
            throw new DaoException("No se pudo eliminar el repartidor #" + id + ": " + e.getMessage(), e);
        }
    }
}