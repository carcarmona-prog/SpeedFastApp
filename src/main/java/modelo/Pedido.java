package modelo;

import interfaces.Mostrable;

public abstract class Pedido implements  Mostrable {

    protected int IdPedido;
    protected String nombreCliente;
    protected String direccionDeEntrega;
    protected double distanciaKm;
    protected String tipoDelPedido;
    protected double tiempoEstimadoDePreparacion;
    protected boolean tipoDeEntrega;
    protected boolean factorAfectanteEnTiemposDeEntrega;

    public Pedido(int idPedido, String nombreCliente, String direccionDeEntrega, double distanciaKm, String tipoDelPedido, double tiempoEstimadoDeEntrega, boolean tipoDeEntrega) {
        IdPedido = idPedido;
        this.nombreCliente = nombreCliente;
        this.direccionDeEntrega = direccionDeEntrega;
        this.distanciaKm = distanciaKm;
        this.tipoDelPedido = tipoDelPedido;
        this.tiempoEstimadoDePreparacion = tiempoEstimadoDeEntrega;
        this.tipoDeEntrega = tipoDeEntrega;

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
        System.out.println("IdPedido: " + IdPedido + " dirección de entrega: " + direccionDeEntrega + " tipo del pedido: " + tipoDelPedido);
    }

    public abstract double calcularTiempoEntrega();



}
