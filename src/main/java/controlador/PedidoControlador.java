package controlador;

import data.PedidoData;
import modelo.Pedido;

import java.util.ArrayList;

/**
 * Controlador que centraliza el acceso a PedidoData.pedidos. Las ventanas
 * (vista) nunca tocan PedidoData directamente.
 * Esto separa la lógica de la
 * interfaz gráfica de dónde/cómo se guardan los datos.
 */
public class PedidoControlador {

    /**
     * Devuelve la lista completa de pedidos registrados.
     */
    public ArrayList<Pedido> listarPedidos() {
        return PedidoData.pedidos;
    }

    /**
     * Agrega un pedido ya construido (ServicioComida, ServicioComprasExpress
     * o ServicioEncomiendas) a la lista compartida.
     */
    public void registrarPedido(Pedido pedido) {
        PedidoData.pedidos.add(pedido);
    }

    /**
     * Busca un pedido por su ID. Devuelve null si no existe.
     */
    public Pedido buscarPorId(int id) {
        for (Pedido pedido : PedidoData.pedidos) {
            if (pedido.getIdPedido() == id) {
                return pedido;
            }
        }
        return null;
    }

    /**
     * Indica si ya existe un pedido con ese ID, para validar el formulario
     * de registro antes de guardar (no permitir IDs repetidos).
     */
    public boolean existeId(int id) {
        return buscarPorId(id) != null;
    }
}
