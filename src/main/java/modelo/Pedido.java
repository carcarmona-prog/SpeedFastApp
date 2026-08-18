package modelo;

import interfaces.Mostrable;
import interfaces.Repartible;

public class Pedido implements  Mostrable {

    protected int IdPedido;
    protected String nombreCliente;
    protected String direccionDeEntrega;
    protected String tipoDelPedido;

    public Pedido(int idPedido, String nombreCliente, String direccionDeEntrega, String tipoDelPedido) {
        IdPedido = idPedido;
        this.nombreCliente = nombreCliente;
        this.direccionDeEntrega = direccionDeEntrega;
        this.tipoDelPedido = tipoDelPedido;
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

    public String getTipoDelPedido() {
        return tipoDelPedido;
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("IdPedido: " + IdPedido + " dirección de entrega: " + direccionDeEntrega + " tipo del pedido: " + tipoDelPedido);
    }


}
