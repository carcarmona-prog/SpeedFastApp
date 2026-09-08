package tareas;

import modelo.EstadoPedido;
import modelo.PedidoSync;

/**
 *  Clase encargada de la lógica del estado de preparación de los pedidos solicitados,
 *  implementa Runnable para cumplir con la función Run() y asi ejecutar los hilos de manera correcta.
 */

public class PrepararPedido implements Runnable {
    private PedidoSync pedidoSync;


    public PrepararPedido(PedidoSync pedido) {
       if(pedido == null){
           throw  new IllegalArgumentException("EL pedido no puede ser null");
       }
       this.pedidoSync = pedido;
    }
    @Override
    public void run() {
        String nombreHilo = Thread.currentThread().getName();

        pedidoSync.setEstadoPedido(EstadoPedido.EN_PREPARACION);

        System.out.printf("Preparando pedido %d - %s (%s) [%s]%n", pedidoSync.getNumero(), pedidoSync.getCliente(), pedidoSync.getProducto(), nombreHilo + " en preparación...");

        try {
            Thread.sleep(pedidoSync.getTiempoPreparacion() * 1_000L);
            pedidoSync.setEstadoPedido(EstadoPedido.LISTO);
            System.out.printf("Pedido %d - %s  [%s] Listo para repartir%n",
                    pedidoSync.getNumero(), pedidoSync.getCliente(), nombreHilo);

        } catch (InterruptedException e) {
            pedidoSync.setEstadoPedido(EstadoPedido.CANCELADO);
            Thread.currentThread().interrupt();
            System.out.printf("Pedido %d - %s  [%s] Cancelado%s", pedidoSync.getNumero(), pedidoSync.getCliente(), nombreHilo);

        }
    }
}
