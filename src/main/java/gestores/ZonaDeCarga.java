package gestores;

import modelo.PedidoSync;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Recurso compartido entre hilos: representa la zona física donde los
 * pedidos esperan a ser retirados por los repartidores.
 */
public class ZonaDeCarga {

    private final BlockingDeque<PedidoSync> pedidosPendientes;

    private boolean sinMasPedidos = false;

    public ZonaDeCarga() {
        this.pedidosPendientes = new LinkedBlockingDeque<>();
    }

    /**
     * Agrega un pedido a la zona de carga y despierta a los
     * repartidores que puedan estar bloqueados esperando (wait())
     * dentro de retirarPedido().
     */
    public synchronized void agregarPedido(PedidoSync pedido) {
        pedidosPendientes.addLast(pedido);
        System.out.printf("[ZonaDeCarga] Pedido %d agregado. Pendientes: %d%n",
                pedido.getNumero(), pedidosPendientes.size());
        notifyAll();
    }

    /**
     * Retira y remueve el siguiente pedido disponible (FIFO).
     * Si no hay pedidos, el hilo se bloquea (wait) hasta que otro
     * agregue uno, o hasta que se llame a cerrarZona() indicando que
     * ya no llegarán más pedidos, en cuyo caso devuelve null para
     * que el repartidor pueda terminar su ejecución ordenadamente.
     */
    public synchronized PedidoSync retirarPedido() {
        while (pedidosPendientes.isEmpty() && !sinMasPedidos) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        if (pedidosPendientes.isEmpty()) {
            return null;
        }
        PedidoSync pedido = pedidosPendientes.pollFirst();
        System.out.printf("[ZonaDeCarga] Pedido %d retirado. Pendientes: %d%n",
                pedido.getNumero(), pedidosPendientes.size());
        return pedido;
    }

    /**
     * Indica que ya no se agregarán más pedidos a la zona de carga.
     * Despierta a los repartidores que estén esperando para que
     * puedan salir de su bucle de trabajo en vez de quedar
     * bloqueados para siempre.
     */
    public synchronized void cerrarZona() {
        sinMasPedidos = true;
        notifyAll();
    }

    public synchronized int pedidosDisponibles() {
        return pedidosPendientes.size();
    }
}