package dao;

import conexion.ConexionBD;
import modelo.Pedido;
import modelo.PedidoRegistro;
import modelo.ServicioComida;
import modelo.ServicioComprasExpress;
import modelo.ServicioEncomiendas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Acceso a la tabla "pedido". Todas las consultas usan PreparedStatement y
 * los recursos (Connection, PreparedStatement, ResultSet) se declaran en un
 * try-with-resources, que los cierra siempre, incluso si ocurre un error
 * (equivale al cierre que se haría en un bloque finally).
 */
public class PedidoDAO {

    /**
     * Inserta el pedido y devuelve el id que generó la base de datos
     * (la columna id es AUTO_INCREMENT).
     */
    public int guardar(Pedido pedido) {
        String sql = "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pedido.getDireccionDeEntrega());
            ps.setString(2, obtenerTipo(pedido));
            ps.setString(3, pedido.getEstado().toUpperCase(Locale.ROOT));
            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                return claves.next() ? claves.getInt(1) : 0;
            }

        } catch (Exception e) {
            throw new DaoException("No se pudo guardar el pedido: " + e.getMessage(), e);
        }
    }

    /** Devuelve todos los pedidos guardados, del más antiguo al más nuevo. */
    public List<PedidoRegistro> listarTodos() {
        return consultar("SELECT id, direccion, tipo, estado FROM pedido ORDER BY id", null);
    }

    /** Devuelve los pedidos que están en el estado indicado (ej.: PENDIENTE). */
    public List<PedidoRegistro> listarPorEstado(String estado) {
        return consultar("SELECT id, direccion, tipo, estado FROM pedido WHERE estado = ? ORDER BY id", estado);
    }

    /** Cambia el estado de un pedido (PENDIENTE, EN_REPARTO, ENTREGADO...). */
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

    // Consulta común de los dos listados. Si "estado" es null no se filtra.
    private List<PedidoRegistro> consultar(String sql, String estado) {
        List<PedidoRegistro> pedidos = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            if (estado != null) {
                ps.setString(1, estado);
            }

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

    /** El tipo se deduce de la subclase real (COMIDA | ENCOMIENDA | EXPRESS). */
    private String obtenerTipo(Pedido pedido) {
        if (pedido instanceof ServicioComida) return "COMIDA";
        if (pedido instanceof ServicioEncomiendas) return "ENCOMIENDA";
        if (pedido instanceof ServicioComprasExpress) return "EXPRESS";
        return "OTRO";
    }
}
