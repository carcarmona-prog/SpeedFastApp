package modelo;

import interfaces.Mostrable;
import interfaces.Repartible;

public class ServicioComida extends Pedido implements Mostrable, Repartible {

    private String nombreRestaurante;
    private boolean mochilaTermica;
    private Repartidor repartidor;

    public ServicioComida(int idPedido, String nombreCliente, String direccionDeEntrega, double distanciaKm, String tipoDelPedido, double tiempoEstimadoDeEntrega, boolean tipoDeEntrega, String nombreRestaurante, boolean mochilaTermica, Repartidor repartidor) {
        super(idPedido, nombreCliente, direccionDeEntrega, distanciaKm, tipoDelPedido, tiempoEstimadoDeEntrega, tipoDeEntrega);
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
        System.out.println("Dirección destinada: " +  direccionDeEntrega);

    }

    @Override
    public double calcularTiempoEntrega() {

       double tiempoEstimadoDePreparacion = 15.0;
       double tiempoDeViaje = distanciaKm * 2.0;

        return tiempoEstimadoDePreparacion + tiempoDeViaje;
    }

    @Override
    public void asignarRepartidor() {
        String mochila = mochilaTermica ? "Sí tiene" : "No tiene";
        System.out.println(" \n Repartidor asignado: " + repartidor.nombreRepartidor + " \n Tipo de vehiculo: " + repartidor.tipoVehiculoRepartidor + " \n Tiene mochila Térmica? " + mochila);
        System.out.println();
        System.out.println(" Tiempo estimado del viaje: " + calcularTiempoEntrega() + " Minutos. ");
        System.out.println(":::::::::::::::::::::::::::::::::::::\n");
    }




}
