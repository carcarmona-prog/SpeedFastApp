package modelo;

import interfaces.Mostrable;
import interfaces.Repartible;

public class ServicioEncomiendas extends Pedido implements Repartible, Mostrable {

    private double pesoEncomienda;
    private String tipoDeEmbalaje;
    private Repartidor repartidor;



    public ServicioEncomiendas(int idPedido, String nombreCliente, String direccionDeEntrega, String tipoDelPedido, double pesoEncomienda, String tipoDeEmbalaje, Repartidor repartidor) {
        super(idPedido, nombreCliente, direccionDeEntrega, tipoDelPedido);
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

        System.out.println(" \n Id pedido: " + getIdPedido() +  " \n Peso: " + pesoEncomienda + " kg " +  " \n Embalaje: " + tipoDeEmbalaje);
        repartidor.mostrarInformacion();

    }
    @Override
    public void asignarRepartidor() {
        System.out.println(" \n Encomienda asignada para: " + repartidor.getNombreRepartidor() + " \n Tipo de vehiculo: " + repartidor.tipoVehiculoRepartidor  );
        System.out.println(":::::::::::::::::::::::::::::::::::::\n");
    }

}
