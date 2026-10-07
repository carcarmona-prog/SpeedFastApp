package controlador;

import conexion.ConexionBD;
import dao.DaoException;
import dao.EntregaDAO;
import dao.PedidoDAO;
import modelo.Entrega;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.PedidoRegistro;
import modelo.ServicioComida;
import modelo.ServicioComprasExpress;
import modelo.ServicioEncomiendas;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador que centraliza el acceso a los pedidos y a sus entregas. Las
 * ventanas (vista) nunca tocan los DAO directamente: esto separa la lógica
 * de la interfaz gráfica de dónde/cómo se guardan los datos (en la base de
 * datos MySQL, a través de PedidoDAO).
 */
public class PedidoControlador {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();

    /**
     * Guarda el pedido en la base de datos y devuelve el id que se le
     * asignó. Recibe el Pedido completo (la subclase concreta armada en
     * VentanaRegistroPedido) para aprovechar el polimorfismo al deducir el
     * tipo, pero solo persiste las columnas que la tabla "pedido" tiene
     * (dirección, tipo, estado).
     */
    public int registrarPedido(Pedido pedido) {
        PedidoRegistro registro = new PedidoRegistro(0, pedido.getDireccionDeEntrega(),
                obtenerTipo(pedido), EstadoPedido.PENDIENTE.name());
        return pedidoDAO.create(registro);
    }

    /** Devuelve todos los pedidos guardados en la base de datos. */
    public List<PedidoRegistro> listarPedidos() {
        return pedidoDAO.readAll();
    }

    /** Devuelve solo los pedidos que todavía no tienen repartidor asignado. */
    public List<PedidoRegistro> listarPendientes() {
        return pedidoDAO.listarPorEstado(EstadoPedido.PENDIENTE.name());
    }

    /** Devuelve los pedidos filtrados por estado, o todos si estado es null. */
    public List<PedidoRegistro> listarPorEstado(String estado) {
        return estado == null ? pedidoDAO.readAll() : pedidoDAO.listarPorEstado(estado);
    }

    /** Devuelve los pedidos filtrados por tipo, o todos si tipo es null. */
    public List<PedidoRegistro> listarPorTipo(String tipo) {
        return tipo == null ? pedidoDAO.readAll() : pedidoDAO.listarPorTipo(tipo);
    }

    /** Filtra por estado y/o tipo a la vez; cualquiera puede ser null. */
    public List<PedidoRegistro> listarFiltrado(String estado, String tipo) {
        return pedidoDAO.filtrar(estado, tipo);
    }

    /** Actualiza dirección, tipo y estado de un pedido existente. */
    public void actualizarPedido(PedidoRegistro pedido) {
        pedidoDAO.update(pedido);
    }

    /** Elimina un pedido de la base de datos. */
    public void eliminarPedido(int idPedido) {
        pedidoDAO.delete(idPedido);
    }

    /**
     * Registra la entrega (pedido + repartidor) y deja el pedido EN_REPARTO,
     * las dos cosas dentro de una misma transacción: si el INSERT de la
     * entrega funciona pero el UPDATE del pedido falla (o viceversa), se
     * hace rollback de ambas en vez de dejar la base de datos a medio
     * camino (una entrega registrada para un pedido que sigue PENDIENTE).
     */
    public void iniciarEntrega(int idPedido, int idRepartidor) {

        try(Connection conexion = ConexionBD.obtenerConexion()){
            conexion.setAutoCommit(false);

            try{
                entregaDAO.create(new Entrega(idPedido, idRepartidor));
                pedidoDAO.actualizarEstado(idPedido, EstadoPedido.EN_REPARTO.name());
                conexion.commit();

            }catch (SQLException e){
                conexion.rollback();
                throw new DaoException("No se pudo iniciar la entrega del pedido #" + idPedido + ": " + e.getMessage(), e);
            }finally{
                conexion.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new DaoException("No se pudo conectar con la base de datos: " + e.getMessage(), e);
        }
    }

    /** Marca el pedido como ENTREGADO en la base de datos. */
    public void marcarEntregado(int idPedido) {
        pedidoDAO.actualizarEstado(idPedido, EstadoPedido.ENTREGADO.name());
    }

    /** El tipo se deduce de la subclase real (COMIDA | ENCOMIENDA | EXPRESS). */
    private String obtenerTipo(Pedido pedido) {
        if (pedido instanceof ServicioComida) return "COMIDA";
        if (pedido instanceof ServicioEncomiendas) return "ENCOMIENDA";
        if (pedido instanceof ServicioComprasExpress) return "EXPRESS";
        return "OTRO";
    }
}