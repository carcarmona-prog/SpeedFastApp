package app;

import interfaces.Repartible;
import modelo.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println(" ::::::::::::: SISTEMA DE GESTION :::::::::::::\n");
        List<Pedido> pedidos = new ArrayList<>();

        Pedido pedido1 = new ServicioComida(26, "José López", " AV siempre viva 1234.", " comida china ", " XUI CHIN ", true, new Repartidor(" ramon ramirez ", true, "moto", true));
        Pedido pedido2 = new ServicioEncomiendas(305, " Daniel Müller ", " santa isabel 302 " , "documentos", 1.5,"sobre", new Repartidor("ana diaz", false, "bicicleta", false) );
        Pedido pedido3 = new ServicioComprasExpress(45,"Raúl Giménez","pasaje 2, 1233","Farmacia", new Repartidor("Rubén luz", false, "Furgon", true ));



        pedidos.add(pedido1);
        pedidos.add(pedido2);
        pedidos.add(pedido3);

        for (Pedido pedido : pedidos) {
            pedido.mostrarInformacion();
            if (pedido instanceof Repartible repartible) {
                repartible.asignarRepartidor();
            }
        }

        System.out.println("\n ::::::::::::: FIN DEL PROCESO ::::::::::::: ");
        System.out.println("Hecho por Carlos Carmona | carcarmona-prog en GitHub.");


    }
}
