package modelo;

import interfaces.Mostrable;
import interfaces.Repartible;

public class ServicioComprasExpress extends Pedido implements Repartible, Mostrable {



    private Repartidor repartidor;

    public ServicioComprasExpress(int idPedido, String nombreCliente, String direccionDeEntrega, String tipoDelPedido, Repartidor repartidor) {
        super(idPedido, nombreCliente, direccionDeEntrega, tipoDelPedido);
        this.repartidor = repartidor;
    }


    public Repartidor getRepartidor() {
        return repartidor;
    }


    @Override

    public void mostrarInformacion() {

        System.out.println(" \n Id de la compra: " + getIdPedido() + " \n A nombre de:" + getNombreCliente() + " \n Dirección: " + direccionDeEntrega + " \n Tipo de pedido: " + getTipoDelPedido()  );
        repartidor.mostrarInformacion();

    }

    @Override
    public void asignarRepartidor() {
        String disponibilidad = repartidor.isDisponibilidadInmediata() ? "Disponible ahora" : "No disponible";

        System.out.println("Compra asignada para: " + repartidor.getNombreRepartidor()
                + " Tipo de vehículo: " + repartidor.getTipoVehiculoRepartidor()
                + " Disponibilidad: " + disponibilidad);
    }
}
