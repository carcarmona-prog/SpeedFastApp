package dao;

import conexion.ConexionBD;
import modelo.Entrega;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a la tabla "entrega" (id, id_pedido, id_repartidor, fecha, hora),
 * que relaciona un pedido con un repartidor. CRUD completo.
 */
public class EntregaDAO implements CrudDAO<Entrega, Integer> {

    /** Inserta la entrega y devuelve el id generado por la base de datos. */
    @Override
    public Integer create(Entrega entrega) {
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

    /** Devuelve todas las entregas registradas. */
    @Override
    public List<Entrega> readAll() {
        return consultar("SELECT id, id_pedido, id_repartidor, fecha, hora FROM entrega ORDER BY id", null, null);
    }

    /** Devuelve las entregas asociadas al pedido indicado. */
    public List<Entrega> listarPorPedido(int idPedido) {
        return consultar("SELECT id, id_pedido, id_repartidor, fecha, hora FROM entrega WHERE id_pedido = ? ORDER BY id",
                idPedido, null);
    }

    /** Devuelve las entregas asociadas al repartidor indicado. */
    public List<Entrega> listarPorRepartidor(int idRepartidor) {
        return consultar("SELECT id, id_pedido, id_repartidor, fecha, hora FROM entrega WHERE id_repartidor = ? ORDER BY id",
                null, idRepartidor);
    }

    /** Actualiza el pedido, repartidor, fecha y hora de una entrega existente. */
    @Override
    public void update(Entrega entrega) {
        String sql = "UPDATE entrega SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, Date.valueOf(entrega.getFecha()));
            ps.setTime(4, Time.valueOf(entrega.getHora()));
            ps.setInt(5, entrega.getId());

            if (ps.executeUpdate() == 0) {
                throw new DaoException("No existe una entrega con id " + entrega.getId() + ".", null);
            }

        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("No se pudo actualizar la entrega #" + entrega.getId() + ": " + e.getMessage(), e);
        }
    }

    /** Elimina la entrega indicada. */
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM entrega WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new DaoException("No se pudo eliminar la entrega #" + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * el método privado consultar recibe cada SQL ya armado,no lo construye dinámicamente.
     * Es más simple porque nunca necesitas filtrar por pedido y repartidor al mismo tiempo (cada botón de la ventana filtra por uno u otro).
     */
    // Consulta común de los listados. Si "idPedido" o "idRepartidor" son null, no se filtra por ese campo.
    private List<Entrega> consultar(String sql, Integer idPedido, Integer idRepartidor) {
        List<Entrega> entregas = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            if (idPedido != null) {
                ps.setInt(1, idPedido);
            }
            if (idRepartidor != null) {
                ps.setInt(1, idRepartidor);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    entregas.add(new Entrega(
                            rs.getInt("id"),
                            rs.getInt("id_pedido"),
                            rs.getInt("id_repartidor"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getTime("hora").toLocalTime()));
                }
            }
            return entregas;

        } catch (Exception e) {
            throw new DaoException("No se pudieron consultar las entregas: " + e.getMessage(), e);
        }
    }
}