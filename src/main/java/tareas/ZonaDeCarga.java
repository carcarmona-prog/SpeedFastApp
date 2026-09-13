package tareas;

import modelo.Pedido;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Recurso compartido entre hilos: representa la zona física donde los
 * pedidos esperan a ser retirados para su preparación/despacho.
*/
public class ZonaDeCarga {

    private final BlockingDeque<Pedido> pedidosPendientes;

    public ZonaDeCarga() {
        this.pedidosPendientes = new LinkedBlockingDeque<>();
    }

    /**
     * Agrega un pedido a la zona de carga y notifica a los hilos
     * que puedan estar esperando (bloqueados en retirarPedido()).
     */
    public synchronized void agregarPedido(Pedido pedido) {
        pedidosPendientes.addLast(pedido);
        System.out.printf("[ZonaDeCarga] Pedido agregado. Pendientes: %d%n",
                pedidosPendientes.size());
        notifyAll();
    }

    /**
     * Retira y remueve el siguiente pedido disponible (FIFO).
     * Si no hay pedidos, el hilo se bloquea (wait) hasta que otro
     * hilo agregue uno con agregarPedido(). Al estar synchronized,
     * solo un hilo a la vez puede estar retirando, por lo que nunca
     * dos hilos obtienen el mismo pedido.
     */
    public synchronized Pedido retirarPedido() {
        while (pedidosPendientes.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        Pedido pedido = pedidosPendientes.pollFirst();
        System.out.printf("[ZonaDeCarga] Pedido retirado. Pendientes: %d%n",
                pedidosPendientes.size());
        return pedido;
    }

    /**
     * Útil para consultar el estado de la cola sin alterarla
     * (por ejemplo, para saber si ya se puede cerrar la simulación).
     */
    public synchronized int pedidosDisponibles() {
        return pedidosPendientes.size();
    }

    public synchronized boolean estaVacia() {
        return pedidosPendientes.isEmpty();
    }
}