package modelo;

import interfaces.Mostrable;
import interfaces.Repartible;

public class ServicioComprasExpress extends Pedido implements Repartible, Mostrable {



    private Repartidor repartidor;

    public ServicioComprasExpress(int idPedido, String nombreCliente, String direccionDeEntrega, double distanciaKm, String tipoDelPedido, double tiempoEstimadoDeEntrega, boolean tipoDeEntrega, Repartidor repartidor) {
        super(idPedido, nombreCliente, direccionDeEntrega, distanciaKm, tipoDelPedido, tiempoEstimadoDeEntrega, tipoDeEntrega);
        this.repartidor = repartidor;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }


    @Override

    public void mostrarInformacion() {

        System.out.println(" \n Id de la compra: " + getIdPedido() + " \n A nombre de:" + getNombreCliente() + " \n Dirección destinada: " + direccionDeEntrega + " \n Tipo de pedido: " + getTipoDelPedido()  );


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

        System.out.println(" \n Repartidor asignado: " + repartidor.getNombreRepartidor()
                + " \n Tipo de vehículo: " + repartidor.getTipoVehiculoRepartidor()
                + " \n Disponibilidad: " + disponibilidad);
        System.out.println();
        System.out.println(" Tiempo estimado del viaje: " + calcularTiempoEntrega() + " minutos.");
    }
}
