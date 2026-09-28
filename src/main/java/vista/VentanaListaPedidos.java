package vista;

import controlador.PedidoControlador;
import dao.DaoException;
import modelo.PedidoRegistro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Ventana de listado de pedidos: una JTable respaldada por un
 * DefaultTableModel, que se puede "refrescar" en cualquier momento leyendo
 * de nuevo los pedidos guardados en la base de datos a través de
 * PedidoControlador.
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

        String[] columnas = {"ID", "Dirección", "Tipo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) { //comienza en 0 para evitar repeticiones de ids
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
     * Vuelve a consultar los pedidos en la base de datos y reconstruye todas
     * las filas de la tabla. Se llama al abrir la ventana y cada vez que se
     * presiona "Actualizar" (por ejemplo, después de registrar un pedido
     * nuevo o de iniciar una entrega desde otra ventana).
     */
    public void cargarDatos() {
        try {
            modeloTabla.setRowCount(0);
            for (PedidoRegistro pedido : controlador.listarPedidos()) {
                modeloTabla.addRow(new Object[]{
                        pedido.id(),
                        pedido.direccion(),
                        pedido.tipo(),
                        pedido.estado()
                });
            }
        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}

