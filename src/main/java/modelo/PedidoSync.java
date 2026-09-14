package modelo;

import interfaces.Mostrable;

/**
 *  Clase creada para la lógica separa de los pedidos que requieran Hilos para su funcionamiento,
 *  con atributos propios, constructores y validaciones de datos,
 *   también se implementa la interfaz Mostrable para reutilizar esa función con base en lo que queremos mostrar en la consola
 */

public class PedidoSync extends Pedido implements Mostrable {

    private final int numero;
    private final String cliente;
    private final String direccionEntrega;
    private final int tiempoPreparacion;
    private final String producto;
    private final int cantidad;
    private final PrioridadPedido prioridad;
    private volatile EstadoPedido estadoPedido;
    private final Repartidor repartidor;


    public PedidoSync(int numero, String cliente, String direccionEntrega, int tiempoPreparacion, String producto, int cantidad, PrioridadPedido prioridad, Repartidor repartidor) {
        super();
        this.repartidor = repartidor;
        //validaciones
        if(numero <= 0) {
            throw new IllegalArgumentException("El numero debe ser mayor que 0");
        }
        if(cliente == null || cliente.isBlank()) {
            throw new IllegalArgumentException("El cliente no debe ser nulo ni vacío");
        }
        if(direccionEntrega == null || direccionEntrega.isBlank()) {
            throw new IllegalArgumentException("La dirección de entrega no debe ser nula ni vacía");
        }
        if(tiempoPreparacion <= 0) {
            throw new IllegalArgumentException("El tiempo debe ser mayor que 0");
        }
        if(producto == null || producto.isBlank()) {
            throw new IllegalArgumentException("El producto no debe ser nulo ni vacío");

        }
        if(cantidad <= 0) {
            throw new IllegalArgumentException("El cantidad debe ser mayor que 0");
        }
        if(prioridad == null) {
            throw new IllegalArgumentException("El prioridad no debe ser nulo ni vacío");
        }
        this.numero = numero;
        this.cliente = cliente;
        this.direccionEntrega = direccionEntrega;
        this.tiempoPreparacion = tiempoPreparacion;
        this.producto = producto;
        this.cantidad = cantidad;
        this.prioridad = prioridad;
        this.estadoPedido = EstadoPedido.PENDIENTE;
    }

    public int getNumero() {
        return numero;
    }

    public String getCliente() {
        return cliente;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public String getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public PrioridadPedido getPrioridad() {
        return prioridad;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }
    public synchronized void setEstadoPedido(EstadoPedido nuevoEstado) {
        if(nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no debe ser nulo");
        }
        this.estadoPedido = nuevoEstado;
    }

    @Override
    public void mostrarInformacion() {
        System.out.printf("Pedido: %d | Cliente: %s | Dirección: %s | Producto: %s | Tiempo de preparación: %d | Prioridad: %s | Estado: %s |\nRepartidor: %s%n",
                numero, cliente, direccionEntrega, producto, tiempoPreparacion, prioridad, estadoPedido, repartidor);

    }

    @Override
    public double calcularTiempoEntrega() {
        return 0;
    }

}