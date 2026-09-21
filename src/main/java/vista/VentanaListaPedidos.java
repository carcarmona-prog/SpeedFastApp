package vista;

import controlador.PedidoControlador;
import modelo.Pedido;
import modelo.ServicioComida;
import modelo.ServicioComprasExpress;
import modelo.ServicioEncomiendas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Ventana de listado de pedidos (Paso 3): una JTable respaldada por un
 * DefaultTableModel, que se puede "refrescar" en cualquier momento leyendo
 * de nuevo la lista compartida a través de PedidoControlador.
 */
public class VentanaListaPedidos extends JFrame {

    private DefaultTableModel modeloTabla;
    private final PedidoControlador controlador = new PedidoControlador();

    public VentanaListaPedidos() {
        setTitle("Listado de Pedidos");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        crearComponentes();
        cargarDatos();
    }

    private void crearComponentes() {
        setLayout(new BorderLayout(10, 10));

        String[] columnas = {"ID", "Cliente", "Dirección", "Tipo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // La tabla es solo de lectura: los datos se editan desde
                // VentanaRegistroPedido / VentanaAsignarRepartidor, no aquí.
                return false;
            }
        };

        JTable tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarDatos());

        JPanel panelInferior = new JPanel();
        panelInferior.add(btnActualizar);
        add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Vuelve a leer PedidoControlador.listarPedidos() y reconstruye todas
     * las filas de la tabla. Se llama al abrir la ventana y cada vez que se
     * presiona "Actualizar" (por ejemplo, después de registrar un pedido
     * nuevo desde otra ventana).
     */
    public void cargarDatos() {
        modeloTabla.setRowCount(0);
        for (Pedido pedido : controlador.listarPedidos()) {
            modeloTabla.addRow(new Object[]{
                    pedido.getIdPedido(),
                    pedido.getNombreCliente(),
                    pedido.getDireccionDeEntrega(),
                    obtenerTipo(pedido),
                    pedido.getEstado()
            });
        }
    }

    /**
     * Pedido no guarda su "tipo" como texto (comida/encomienda/express);
     * se deduce a partir de cuál de las 3 subclases es la instancia real.
     */
    private String obtenerTipo(Pedido pedido) {
        if (pedido instanceof ServicioComida) return "Comida";
        if (pedido instanceof ServicioEncomiendas) return "Encomienda";
        if (pedido instanceof ServicioComprasExpress) return "Compra Express";
        return "Desconocido";
    }
}
