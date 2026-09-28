package controlador;

import dao.EntregaDAO;
import dao.PedidoDAO;
import modelo.Entrega;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.PedidoRegistro;

import java.util.List;

/**
 * Controlador que centraliza el acceso a los pedidos y a sus entregas. Las
 * ventanas (vista) nunca tocan los DAO directamente: esto separa la lógica
 * de la interfaz gráfica de dónde/cómo se guardan los datos (ahora en la
 * base de datos MySQL, antes en la lista en memoria PedidoData).
 */
public class PedidoControlador {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();

    /** Guarda el pedido en la base de datos y devuelve el id que se le asignó. */
    public int registrarPedido(Pedido pedido) {
        return pedidoDAO.guardar(pedido);
    }

    /** Devuelve todos los pedidos guardados en la base de datos. */
    public List<PedidoRegistro> listarPedidos() {
        return pedidoDAO.listarTodos();
    }

    /** Devuelve solo los pedidos que todavía no tienen repartidor asignado. */
    public List<PedidoRegistro> listarPendientes() {
        return pedidoDAO.listarPorEstado(EstadoPedido.PENDIENTE.name());
    }

    /**
     * Registra la entrega (pedido + repartidor) y deja el pedido EN_REPARTO.
     */
    public void iniciarEntrega(int idPedido, int idRepartidor) {
        entregaDAO.guardar(new Entrega(idPedido, idRepartidor));
        pedidoDAO.actualizarEstado(idPedido, EstadoPedido.EN_REPARTO.name());
    }

    /** Marca el pedido como ENTREGADO en la base de datos. */
    public void marcarEntregado(int idPedido) {
        pedidoDAO.actualizarEstado(idPedido, EstadoPedido.ENTREGADO.name());
    }
}
