package modelo;

import interfaces.Mostrable;
import gestores.ZonaDeCarga;

/**
 * Clase que define los atributos del objeto repartidor.
 * Implementa Mostrable para la utilización de su comportamiento.
 *
 */
public class Repartidor implements Mostrable, Runnable {


    protected String nombreRepartidor;
    protected boolean disponibilidadInmediata;
    protected boolean mochilaTermica;
    protected String tipoVehiculoRepartidor;

    private ZonaDeCarga zonaDeCarga;


    public Repartidor(String nombreRepartidor) {
        this.nombreRepartidor = nombreRepartidor;
    }

    public Repartidor(String nombreRepartidor, boolean disponibilidadInmediata,String tipoVehiculoRepartidor, boolean mochilaTermica) {
        this.nombreRepartidor = nombreRepartidor;
        this.disponibilidadInmediata = disponibilidadInmediata;
        this.mochilaTermica = mochilaTermica;
        this.tipoVehiculoRepartidor = tipoVehiculoRepartidor;

    }

    /**
     *  Repartidor que trabaja de forma
     * independiente retirando pedidos desde la zona de carga.
     */
    public Repartidor(String nombreRepartidor, ZonaDeCarga zonaDeCarga) {
        this.nombreRepartidor = nombreRepartidor;
        this.zonaDeCarga = zonaDeCarga;
    }

    public String getNombreRepartidor() {
        return nombreRepartidor;
    }

    public boolean isMochilaTermica() {
        return mochilaTermica;
    }

    public String getTipoVehiculoRepartidor() {
        return tipoVehiculoRepartidor;
    }

    public boolean isDisponibilidadInmediata() {
        return disponibilidadInmediata;
    }

    public void setNombreRepartidor(String nombreRepartidor) {
        this.nombreRepartidor = nombreRepartidor;
    }

    public void setDisponibilidadInmediata(boolean disponibilidadInmediata) {
        this.disponibilidadInmediata = disponibilidadInmediata;
    }

    public void setMochilaTermica(boolean mochilaTermica) {
        this.mochilaTermica = mochilaTermica;
    }

    public void setTipoVehiculoRepartidor(String tipoVehiculoRepartidor) {
        this.tipoVehiculoRepartidor = tipoVehiculoRepartidor;
    }

    public void setZonaDeCarga(ZonaDeCarga zonaDeCarga) {
        this.zonaDeCarga = zonaDeCarga;
    }

    @Override
    public String toString() {
        String mochila = mochilaTermica ? "Sí tiene" : "No tiene";
        String disponibilidad = disponibilidadInmediata ? "Disponible ahora" : "No disponible";

        return "Nombre Repartidor: " + nombreRepartidor
                + " | Tipo de vehículo: " + tipoVehiculoRepartidor
                + " | Mochila térmica: " + mochila
                + " | Disponibilidad: " + disponibilidad;
    }

    @Override
    public void mostrarInformacion() {
        System.out.println(toString());
    }



    @Override
    public void run() {
        if (zonaDeCarga == null) {

            return;
        }

        String nombreHilo = Thread.currentThread().getName();
        PedidoSync pedido;

        while ((pedido = zonaDeCarga.retirarPedido()) != null) {
            pedido.setEstadoPedido(EstadoPedido.EN_REPARTO);
            System.out.printf("[%s] %s retiró el pedido %d (%s) -> EN_REPARTO%n",
                    nombreHilo, nombreRepartidor, pedido.getNumero(), pedido.getDireccionEntrega());

            try {
                Thread.sleep(pedido.getTiempoPreparacion() * 1_000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            pedido.setEstadoPedido(EstadoPedido.ENTREGADO);
            System.out.printf("[%s] %s entregó el pedido %d a %s -> ENTREGADO%n",
                    nombreHilo, nombreRepartidor, pedido.getNumero(), pedido.getCliente());
        }

        System.out.printf("[%s] %s no tiene más pedidos que retirar, finaliza.%n",
                nombreHilo, nombreRepartidor);
    }
}
