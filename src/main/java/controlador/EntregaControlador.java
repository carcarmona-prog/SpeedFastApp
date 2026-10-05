package controlador;

import dao.EntregaDAO;
import modelo.Entrega;

import java.util.List;

/**
 * Controlador de entregas: intermediario entre las ventanas y EntregaDAO.
 * Se usa desde VentanaGestionEntregas para el CRUD manual (crear, listar,
 * editar y eliminar entregas); VentanaAsignarRepartidor sigue usando
 * PedidoControlador.iniciarEntrega(...) para el flujo de negocio que
 * además cambia el estado del pedido y simula el viaje con un hilo.
 */
public class EntregaControlador {

    private final EntregaDAO entregaDAO = new EntregaDAO();

    /** Guarda la entrega en la base de datos y devuelve su id. */
    public int registrarEntrega(Entrega entrega) {
        return entregaDAO.create(entrega);
    }

    /** Devuelve todas las entregas registradas. */
    public List<Entrega> listarEntregas() {
        return entregaDAO.readAll();
    }

    /** Devuelve las entregas de un pedido en particular. */
    public List<Entrega> listarPorPedido(int idPedido) {
        return entregaDAO.listarPorPedido(idPedido);
    }

    /** Devuelve las entregas de un repartidor en particular. */
    public List<Entrega> listarPorRepartidor(int idRepartidor) {
        return entregaDAO.listarPorRepartidor(idRepartidor);
    }

    /** Actualiza el pedido, repartidor, fecha y hora de una entrega. */
    public void actualizarEntrega(Entrega entrega) {
        entregaDAO.update(entrega);
    }

    /** Elimina una entrega de la base de datos. */
    public void eliminarEntrega(int idEntrega) {
        entregaDAO.delete(idEntrega);
    }
}