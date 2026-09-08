package app;

import interfaces.Despachable;
import modelo.*;
import tareas.PrepararPedido;

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

        PedidoSync pedidoSync1 = new PedidoSync(1, "Ramonete Rojas", 20, "Servicio oficina", 1, PrioridadPedido.NORMAL,new Repartidor("ana diaz",true,"moto", true ));

        PedidoSync pedidoSync2 = new PedidoSync(2, "Willian Rivas", 15, "Encomienda importante", 3, PrioridadPedido.ALTA,new Repartidor("ruben luz", true, "farmacia", true));

        PedidoSync pedidoSync3 = new PedidoSync(3, "Maria Love", 25, "Combo Kids", 2, PrioridadPedido.NORMAL,new Repartidor("Ramonete Ramirez", true, "moto", true));

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

        long tiempoEjecucion = System.currentTimeMillis();

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

        }




    }






