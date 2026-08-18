package modelo;

import interfaces.Mostrable;
import interfaces.Repartible;

public class ServicioComida extends Pedido implements Mostrable, Repartible {

    private String nombreRestaurante;
    private boolean mochilaTermica;
    private Repartidor repartidor;

    public ServicioComida(int idPedido, String nombreCliente, String direccionDeEntrega, String tipoDelPedido, String nombreRestaurante, boolean mochilaTermica, Repartidor repartidor) {
        super(idPedido, nombreCliente, direccionDeEntrega, tipoDelPedido);
        this.nombreRestaurante = nombreRestaurante;
        this.mochilaTermica = mochilaTermica;
        this.repartidor = repartidor;
    }

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }


    public boolean isMochilaTermica() {
        return mochilaTermica;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    @Override
    public void mostrarInformacion() {

        String mochila = mochilaTermica ? "Sí requiere" : "No es necesario";
        System.out.println("Id del pedido: " + getIdPedido());
        System.out.println("Nombre del cliente: " + getNombreCliente());
        System.out.println("Restaurante: " + nombreRestaurante);
        System.out.println("Requiere mochila térmica: " + mochila);
        repartidor.mostrarInformacion();
    }

    @Override
    public void asignarRepartidor() {
        String mochila = mochilaTermica ? "Sí tiene" : "No tiene";
        System.out.println(" \n Asignando repartidor: " + repartidor.nombreRepartidor + " \n Tipo de vehiculo: " + repartidor.tipoVehiculoRepartidor + " \n Tiene mochila Térmica? " + mochila+ " \n Dirección destinada: " +  direccionDeEntrega);
        System.out.println(":::::::::::::::::::::::::::::::::::::\n");
    }




}
