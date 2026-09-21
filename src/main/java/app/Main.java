package app;

import interfaces.Despachable;
import modelo.*;
import modelo.ServicioComida;
import modelo.ServicioComprasExpress;
import modelo.ServicioEncomiendas;
import gestores.PrepararPedido;
import gestores.ZonaDeCarga;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Clase principal donde aplicamos la lógica del programa para evidenciar el funcionamiento polimórfico del código.
 */

public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println(" ::::::::::::: SISTEMA DE GESTION :::::::::::::\n");
        List<Pedido> pedidos = new ArrayList<>();

        Pedido pedido1 = new ServicioComida(26, "José López", " AV siempre viva 1234.", 4.5, " comida china ", 0.30, true, "XUI CHIN", true,
                new Repartidor(" ramon ramirez ", true, "moto", true));

        Pedido pedido2 = new ServicioEncomiendas(305, " Daniel Müller ", " santa isabel 302 ", 1.5, "oficina", 0.45, true, 1.2, "sobre",
                new Repartidor("ana diaz", false, "bicicleta", false));

        Pedido pedido3 = new ServicioComprasExpress(45, "Raúl Giménez", "pasaje 2, 1233", 7.0, "farmacia", 1.5, true,
                new Repartidor("Rubén luz", false, "furgon", true));


        pedidos.add(pedido1);
        pedidos.add(pedido2);
        pedidos.add(pedido3);


        for (Pedido pedido : pedidos) {
            pedido.mostrarInformacion();
            if (pedido instanceof Despachable despachable) {
                despachable.asignarRepartidor();
            }
            pedido.mostrarHistorial();
            System.out.println("--------------------------------------------------");
        }

        // Probar la cancelación y ver cómo se actualiza el historial
        System.out.println(":::::::PEDIDOS CANCELADOS::::::::");
        pedido1.cancelarPedido();
        System.out.println();
        pedido1.mostrarHistorial();

        System.out.println("--------------------------------------------------");

        PedidoSync pedidoSync1 = new PedidoSync(1, "Ramonete Rojas", "Oficina central, piso 3", 20, "Servicio oficina", 1, PrioridadPedido.NORMAL, new Repartidor("ana diaz", true, "moto", true));

        PedidoSync pedidoSync2 = new PedidoSync(2, "Willian Rivas", "Bodega norte, bloque C", 15, "Encomienda importante", 3, PrioridadPedido.ALTA, new Repartidor("ruben luz", true, "farmacia", true));

        PedidoSync pedidoSync3 = new PedidoSync(3, "Maria Love", "Colegio San Andrés, portería", 25, "Combo Kids", 2, PrioridadPedido.NORMAL, new Repartidor("Ramonete Ramirez", true, "moto", true));

        /**
         * En esta sección del main relalizamos la utilizacion de los Threads a traves de ExecutorService, para administrar
         * los hilos y que terminen de manera limpia.
         */
        System.out.println(":::::::INICIANDO HILOS::::::::\n");

        System.out.println(" ::::::::::::: PREPARANDO PEDIDOS ::::::::::::: ");

        ExecutorService executor = Executors.newFixedThreadPool(3); // uno por cada PedidoSync


        pedidoSync1.mostrarInformacion();
        System.out.println();
        pedidoSync2.mostrarInformacion();
        System.out.println();
        pedidoSync3.mostrarInformacion();


        executor.submit(new PrepararPedido(pedidoSync1));
        System.out.println();
        executor.submit(new PrepararPedido(pedidoSync2));
        System.out.println();
        executor.submit(new PrepararPedido(pedidoSync3));


        executor.shutdown();

        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("::: simulación finalizada ::: ");

        // =========================================================
        // PASO 5: Zona de carga compartida + repartidores concurrentes
        // =========================================================
        System.out.println();
        System.out.println(":::::::::::::: ZONA DE CARGA Y REPARTIDORES ::::::::::::::\n");

        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();

        zonaDeCarga.agregarPedido(new PedidoSync(101, "Carla Soto", "Av. Pajaritos 1500", 2, "Pizza familiar", 1, PrioridadPedido.NORMAL, new Repartidor("Por asignar")));
        zonaDeCarga.agregarPedido(new PedidoSync(102, "Matías Fuentes", "Los Alerces 245", 3, "Sushi combo", 2, PrioridadPedido.ALTA, new Repartidor("Por asignar")));
        zonaDeCarga.agregarPedido(new PedidoSync(103, "Fernanda Vidal", "Camino La Farfana 88", 1, "Documentos urgentes", 1, PrioridadPedido.ALTA, new Repartidor("Por asignar")));
        zonaDeCarga.agregarPedido(new PedidoSync(104, "Ignacio Bravo", "Pasaje Los Robles 12", 2, "Compra supermercado", 3, PrioridadPedido.NORMAL, new Repartidor("Por asignar")));
        zonaDeCarga.agregarPedido(new PedidoSync(105, "Valentina Rojas", "Av. 5 de Abril 900", 2, "Medicamentos", 1, PrioridadPedido.ALTA, new Repartidor("Por asignar")));

        // A partir de aquí ya no llegarán más pedidos: los repartidores
        // podrán terminar su hilo apenas la zona de carga quede vacía.
        zonaDeCarga.cerrarZona();

        Repartidor repartidor1 = new Repartidor("Camila Reyes", zonaDeCarga);
        Repartidor repartidor2 = new Repartidor("Jorge Pino", zonaDeCarga);
        Repartidor repartidor3 = new Repartidor("Felipe Soto", zonaDeCarga);

        ExecutorService executorRepartidores = Executors.newFixedThreadPool(3);

        executorRepartidores.submit(repartidor1);
        executorRepartidores.submit(repartidor2);
        executorRepartidores.submit(repartidor3);

        executorRepartidores.shutdown();

        try {
            if (!executorRepartidores.awaitTermination(30, TimeUnit.SECONDS)) {
                executorRepartidores.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorRepartidores.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("\nTodos los pedidos han sido entregados correctamente.");
    }
}