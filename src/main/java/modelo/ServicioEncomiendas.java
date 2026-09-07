package modelo;

import interfaces.Mostrable;
import interfaces.Despachable;

public class ServicioEncomiendas extends Pedido implements Despachable, Mostrable {

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
    public String toString() {

        System.out.println(" \n Id pedido: " + getIdPedido() + " \n Nombre del cliente: " + getNombreCliente() +  " \n Peso: " + pesoEncomienda + " kg " +  " \n Embalaje: " + tipoDeEmbalaje + " \n Dirección destinada: " +  direccionDeEntrega);


        return null;
    }
    @Override
    public void mostrarInformacion() {
        System.out.println(toString());
    }


    @Override
    public double calcularTiempoEntrega() {
        double tiempoArmandoPedido = 20.0;
        double tiempoDeViaje = distanciaKm * 1.5;

        return tiempoArmandoPedido + tiempoDeViaje;
    }


    @Override
    public void asignarRepartidor() {
        String disponibilidad = repartidor.isDisponibilidadInmediata() ? "Disponible ahora" : "No disponible";
        System.out.println(" \n Repartidor asignado: " + repartidor.getNombreRepartidor() + " \n Tipo de vehiculo: " + repartidor.tipoVehiculoRepartidor + " \n Disponibilidad: " + disponibilidad);
        System.out.println();
        System.out.println(" Tiempo estimado del viaje: " + calcularTiempoEntrega() + " minutos.");
        System.out.println(":::::::::::::::::::::::::::::::::::::\n");
    }


    @Override
    public void cancelarPedido() {
        super.cancelarPedido();
        historial.add("Encomienda " + IdPedido + " cancelada, se notificó al remitente.");
        System.out.println("Encomienda cancelada y remitente notificado.");
    }

    @Override
    public void mostrarHistorial() {
        System.out.println("Historial de la encomienda " + IdPedido + ":");
        for (String evento : historial) {
            System.out.println(" - " + evento);
        }
    }

    @Override
    public void setEstadoPedido(EstadoPedido estadoPedido) {

    }
}
