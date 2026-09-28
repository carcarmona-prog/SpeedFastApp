package modelo;

/**
 * Fila de la tabla "pedido" tal como está guardada en la base de datos.
 * Se usa para consultar y mostrar pedidos (JTable, combos) sin tener que
 * reconstruir un Pedido completo, porque la tabla solo guarda id,
 * dirección, tipo y estado.
 */
public record PedidoRegistro(int id, String direccion, String tipo, String estado) {
}
