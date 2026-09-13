package tareas;

import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.PedidoSync;

/**
 * Hilo que representa a un repartidor/preparador: retira pedidos
 * desde la ZonaDeCarga (recurso compartido) y los procesa uno por
 * uno hasta que se le indica que se detenga.
 *
 * OJO: este archivo asume que puedes obtener un PedidoSync a partir
 * de un Pedido (por ejemplo porque Pedido lo contiene, o porque
 * PedidoSync es en realidad tu clase Pedido con soporte de
 * concurrencia). Ajusta esa parte según cómo estén definidas tus
 * clases modelo.Pedido y modelo.PedidoSync — la idea central es el
 * patrón productor/consumidor con la zona de carga, que es lo que
 * pide la actividad.
 */
public class HiloRepartidor implements Runnable {

    private final ZonaDeCarga zonaDeCarga;
    private volatile boolean activo = true;

    public HiloRepartidor(ZonaDeCarga zonaDeCarga) {
        this.zonaDeCarga = zonaDeCarga;
    }

    public void detener() {
        activo = false;
    }

    @Override
    public void run() {
        String nombreHilo = Thread.currentThread().getName();

        while (activo) {
            Pedido pedido = zonaDeCarga.retirarPedido();
            if (pedido == null) {
                // wait() fue interrumpido o el hilo fue detenido
                break;
            }
            procesarPedido(pedido, nombreHilo);
        }
    }

    private void procesarPedido(Pedido pedido, String nombreHilo) {
        // Si tu clase PedidoSync es distinta de Pedido, aquí debes
        // obtener/crear el PedidoSync correspondiente a este pedido.
        PedidoSync pedidoSync = (PedidoSync) pedido; // ajustar según tu modelo real

        pedidoSync.setEstadoPedido(EstadoPedido.EN_PREPARACION);
        System.out.printf("Preparando pedido %d - %s (%s) [%s]%n",
                pedidoSync.getNumero(), pedidoSync.getCliente(),
                pedidoSync.getProducto(), nombreHilo);
        try {
            Thread.sleep(pedidoSync.getTiempoPreparacion() * 1_000L);
            pedidoSync.setEstadoPedido(EstadoPedido.ENTREGADO);
            System.out.printf("Pedido %d - %s [%s] Listo para repartir%n",
                    pedidoSync.getNumero(), pedidoSync.getCliente(), nombreHilo);
        } catch (InterruptedException e) {
            pedidoSync.setEstadoPedido(EstadoPedido.CANCELADO);
            Thread.currentThread().interrupt();
            System.out.printf("Pedido %d - %s [%s] Cancelado%n",
                    pedidoSync.getNumero(), pedidoSync.getCliente(), nombreHilo);
        }
    }
}