package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa una fila de la tabla "entrega": la relación entre un pedido y
 * el repartidor que lo lleva, con la fecha y la hora en que se registró.
 */
public class Entrega {

    private int id;
    private final int idPedido;
    private final int idRepartidor;
    private final LocalDate fecha;
    private final LocalTime hora;

    /** Entrega nueva: la fecha y la hora son las del momento de crearla. */
    public Entrega(int idPedido, int idRepartidor) {
        this(0, idPedido, idRepartidor, LocalDate.now(), LocalTime.now());
    }

    public Entrega(int id, int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this.id = id;
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }
}
