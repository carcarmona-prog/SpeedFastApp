package dao;

import conexion.ConexionBD;
import modelo.PedidoRegistro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a la tabla "pedido" (id, direccion, tipo, estado). Implementa el
 * CRUD completo con PedidoRegistro, que son exactamente las columnas que
 * la tabla guarda. Todas las consultas usan PreparedStatement y los
 * recursos (Connection, PreparedStatement, ResultSet) se declaran en un
 * try-with-resources, que los cierra siempre, incluso si ocurre un error
 * (equivale al cierre que se haría en un bloque finally).
 */
public class PedidoDAO implements CrudDAO<PedidoRegistro, Integer> {

    /** Inserta el pedido y devuelve el id que generó la base de datos. */
    @Override
    public Integer create(PedidoRegistro pedido) {
        String sql = "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pedido.direccion());
            ps.setString(2, pedido.tipo());
            ps.setString(3, pedido.estado());
            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                return claves.next() ? claves.getInt(1) : 0;
            }

        } catch (Exception e) {
            throw new DaoException("No se pudo guardar el pedido: " + e.getMessage(), e);
        }
    }

    /** Devuelve todos los pedidos guardados, del más antiguo al más nuevo. */
    @Override
    public List<PedidoRegistro> readAll() {
        return filtrar(null, null);
    }

    /**
     * Actualiza dirección, tipo y estado de un pedido ya existente
     * (identificado por pedido.id()).
     */
    @Override
    public void update(PedidoRegistro pedido) {
        String sql = "UPDATE pedido SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, pedido.direccion());
            ps.setString(2, pedido.tipo());
            ps.setString(3, pedido.estado());
            ps.setInt(4, pedido.id());

            if (ps.executeUpdate() == 0) {
                throw new DaoException("No existe un pedido con id " + pedido.id() + ".", null);
            }

        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("No se pudo actualizar el pedido #" + pedido.id() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Elimina el pedido indicado. Si tiene entregas asociadas, la base de
     * datos rechaza el borrado por la clave foránea de "entrega"; en ese
     * caso se informa un mensaje claro en vez del error crudo de SQL.
     */
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM pedido WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoException("No se puede eliminar el pedido #" + id
                    + " porque tiene una entrega registrada. Elimine primero esa entrega.", e);
        } catch (Exception e) {
            throw new DaoException("No se pudo eliminar el pedido #" + id + ": " + e.getMessage(), e);
        }
    }

    /** Devuelve los pedidos que están en el estado indicado (ej.: PENDIENTE). */
    public List<PedidoRegistro> listarPorEstado(String estado) {
        return filtrar(estado, null);
    }

    /** Devuelve los pedidos del tipo indicado (COMIDA | ENCOMIENDA | EXPRESS). */
    public List<PedidoRegistro> listarPorTipo(String tipo) {
        return filtrar(null, tipo);
    }

    /**
     * Filtra por estado y/o tipo a la vez; cualquiera de los dos puede ser
     * null, en cuyo caso no se filtra por ese campo. Si ambos son null,
     * devuelve todos los pedidos (igual que readAll()).
     */
    public List<PedidoRegistro> filtrar(String estado, String tipo) {
        StringBuilder sql = new StringBuilder("SELECT id, direccion, tipo, estado FROM pedido");
        List<String> condiciones = new ArrayList<>();
        if (estado != null) condiciones.add("estado = ?");
        if (tipo != null) condiciones.add("tipo = ?");
        if (!condiciones.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", condiciones));
        }
        sql.append(" ORDER BY id");

        List<PedidoRegistro> pedidos = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            int indice = 1;
            if (estado != null) ps.setString(indice++, estado);
            if (tipo != null) ps.setString(indice, tipo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(new PedidoRegistro(
                            rs.getInt("id"),
                            rs.getString("direccion"),
                            rs.getString("tipo"),
                            rs.getString("estado")));
                }
            }
            return pedidos;

        } catch (Exception e) {
            throw new DaoException("No se pudieron consultar los pedidos: " + e.getMessage(), e);
        }
    }

    /** Cambia solo el estado de un pedido (PENDIENTE, EN_REPARTO, ENTREGADO...). */
    public void actualizarEstado(int idPedido, String nuevoEstado) {
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new DaoException("No se pudo actualizar el estado del pedido #" + idPedido + ": " + e.getMessage(), e);
        }
    }

    /**
     * Cambia el estado usando una conexión ya abierta por quien llama, para
     * que este UPDATE pueda compartir una transacción con otra operación
     * (ver PedidoControlador.iniciarEntrega). Igual que en EntregaDAO, no
     * maneja la conexión ni envuelve el error: deja que quien maneja la
     * transacción decida si hace commit o rollback.
     */
    public void actualizarEstado(Connection conexion, int idPedido, String nuevoEstado) {
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";

        try(PreparedStatement ps = conexion.prepareStatement(sql)){
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
