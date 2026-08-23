package modelo;

import interfaces.Mostrable;
import interfaces.Repartible;

public class ServicioEncomiendas extends Pedido implements Repartible, Mostrable {

    private double pesoEncomienda;
    private String tipoDeEmbalaje;
    private Repartidor repartidor;


    public ServicioEncomiendas(int idPedido, String nombreCliente, String direccionDeEntrega, double distanciaKm, String tipoDelPedido, double tiempoEstimadoDeEntrega, boolean tipoDeEntrega, double pesoEncomienda, String tipoDeEmbalaje, Repartidor repartidor) {
        super(idPedido, nombreCliente, direccionDeEntrega, distanciaKm, tipoDelPedido, tiempoEstimadoDeEntrega, tipoDeEntrega);
        this.pesoEncomienda = pesoEncomienda;
        this.tipoDeEmbalaje = tipoDeEmbalaje;
        this.repartidor = repartidor;
    }

    public double getPesoEncomienda() {
        return pesoEncomienda;
    }

    public String getTipoDeEmbalaje() {
        return tipoDeEmbalaje;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    @Override
    public void mostrarInformacion() {

        System.out.println(" \n Id pedido: " + getIdPedido() + " \n Nombre del cliente: " + getNombreCliente() +  " \n Peso: " + pesoEncomienda + " kg " +  " \n Embalaje: " + tipoDeEmbalaje + " \n Dirección destinada: " +  direccionDeEntrega);


    }

    @Override
    public double calcularTiempoEntrega() {
        double tiempoBase = 10.0;
        if(this.distanciaKm > 5.0) {
           tiempoBase += 5.0;
        }
        return tiempoBase;
    }

    @Override
    public void asignarRepartidor() {
        String disponibilidad = repartidor.isDisponibilidadInmediata() ? "Disponible ahora" : "No disponible";
        System.out.println(" \n Repartidor asignado: " + repartidor.getNombreRepartidor() + " \n Tipo de vehiculo: " + repartidor.tipoVehiculoRepartidor + " \n Disponibilidad: " + disponibilidad);
        System.out.println();
        System.out.println(" Tiempo estimado del viaje: " + calcularTiempoEntrega() + " minutos.");
        System.out.println(":::::::::::::::::::::::::::::::::::::\n");
    }

}
