package data;

import modelo.Pedido;

import java.util.ArrayList;

/**
 * Lista compartida en memoria de todos los pedidos registrados desde la GUI.
 * Es "estática" a propósito -igual que UsuarioData.usuarios-: así todas las
 * ventanas (VentanaRegistroPedido, VentanaListaPedidos,
 * VentanaAsignarRepartidor) ven siempre los mismos datos sin necesidad de
 * pasarse referencias entre sí.
 */
public class PedidoData {

    public static ArrayList<Pedido> pedidos = new ArrayList<>();
}
