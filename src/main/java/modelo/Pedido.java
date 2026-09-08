package modelo;

import interfaces.Cancelable;
import interfaces.Mostrable;
import interfaces.Rastreable;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que define los atributos de los pedidos y sirve como clase Padre del programa.
 */

public abstract class Pedido implements  Mostrable, Cancelable, Rastreable {

    protected int IdPedido;
    protected String nombreCliente;
    protected String direccionDeEntrega;
    protected double distanciaKm;
    protected String tipoDelPedido;
    protected double tiempoEstimadoDePreparacion;
    protected boolean tipoDeEntrega;
    protected String estado;
    protected List<String> historial;



    public Pedido(int idPedido, String nombreCliente, String direccionDeEntrega, double distanciaKm, String tipoDelPedido, double tiempoEstimadoDeEntrega, boolean tipoDeEntrega) {
        IdPedido = idPedido;
        this.nombreCliente = nombreCliente;
        this.direccionDeEntrega = direccionDeEntrega;
        this.distanciaKm = distanciaKm;
        this.tipoDelPedido = tipoDelPedido;
        this.tiempoEstimadoDePreparacion = tiempoEstimadoDeEntrega;
        this.tipoDeEntrega = tipoDeEntrega;
        this.estado = "pendiente";
        this.historial = new ArrayList<>();
        historial.add("Pedido creado con estado: " + estado);


    }

    public String getEstado(EstadoPedido enPreparacion) {
        return estado;
    }

    public int getIdPedido() {
        return IdPedido;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getDireccionDeEntrega() {
        return direccionDeEntrega;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public String getTipoDelPedido() {
        return tipoDelPedido;
    }

    public double getTiempoEstimadoDePreparacion() {
        return tiempoEstimadoDePreparacion;
    }

    public boolean isTipoDeEntrega() {
        return tipoDeEntrega;
    }


    @Override
    public void mostrarInformacion() {
        System.out.println("IdPedido: " + IdPedido + " dirección de entrega: " + direccionDeEntrega + " tipo del pedido: " + tipoDelPedido + " estado: " + estado);

    }

    public abstract double calcularTiempoEntrega();


    @Override
    public void cancelarPedido() {
        if (estado.equals("Cancelado")) {
            System.out.println("El pedido ya se encuentra cancelado.");
            return;
        }
        estado = "Cancelado";
        historial.add("Pedido cancelado.");
        System.out.println("Pedido #" + IdPedido + " cancelado correctamente.");
    }

    @Override
    public void mostrarHistorial() {
        System.out.println("Historial de pedidos: " + historial);
        for (String historial : historial) {
            System.out.println(" - " + historial);
        }
    }


    public abstract void setEstadoPedido(EstadoPedido estadoPedido);
}
