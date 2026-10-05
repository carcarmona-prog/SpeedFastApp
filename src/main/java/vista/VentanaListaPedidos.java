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
 * PedidoControlador. Permite filtrar por tipo y/o estado, y editar o
 * eliminar el pedido seleccionado (CRUD completo sobre la tabla "pedido").
 */
public class VentanaListaPedidos extends JFrame {

    private static final String TODOS = "Todos";
    private static final String[] TIPOS = {"COMIDA", "ENCOMIENDA", "EXPRESS"};
    private static final String[] ESTADOS = {"PENDIENTE", "EN_REPARTO", "ENTREGADO"};

    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JComboBox<String> cmbFiltroTipo;
    private JComboBox<String> cmbFiltroEstado;

    private final PedidoControlador controlador = new PedidoControlador();

    public VentanaListaPedidos() {
        setTitle("Listado de Pedidos");
        setSize(760, 440);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        crearComponentes();
        cargarDatos();
    }

    private void crearComponentes() {
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFiltros(), BorderLayout.NORTH);

        String[] columnas = {"ID", "Dirección", "Tipo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // La tabla es solo de lectura: los datos se editan con el
                // botón "Editar" (que abre un formulario aparte), no aquí.
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        panel.add(new JLabel("Tipo:"));
        cmbFiltroTipo = new JComboBox<>(conTodos(TIPOS));
        panel.add(cmbFiltroTipo);

        panel.add(new JLabel("Estado:"));
        cmbFiltroEstado = new JComboBox<>(conTodos(ESTADOS));
        panel.add(cmbFiltroEstado);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> cargarDatos());
        panel.add(btnFiltrar);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarDatos());

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarSeleccionado());

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        panel.add(btnActualizar);
        panel.add(btnEditar);
        panel.add(btnEliminar);
        return panel;
    }

    private String[] conTodos(String[] valores) {
        String[] resultado = new String[valores.length + 1];
        resultado[0] = TODOS;
        System.arraycopy(valores, 0, resultado, 1, valores.length);
        return resultado;
    }

    /**
     * Vuelve a consultar los pedidos en la base de datos (aplicando los
     * filtros de tipo/estado elegidos) y reconstruye todas las filas de la
     * tabla. Se llama al abrir la ventana, al presionar "Filtrar" o
     * "Actualizar", y después de editar o eliminar un pedido.
     */
    public void cargarDatos() {
        String tipo = seleccionONull(cmbFiltroTipo);
        String estado = seleccionONull(cmbFiltroEstado);

        try {
            modeloTabla.setRowCount(0);
            for (PedidoRegistro pedido : controlador.listarFiltrado(estado, tipo)) {
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

    private String seleccionONull(JComboBox<String> combo) {
        String seleccion = (String) combo.getSelectedItem();
        return (seleccion == null || seleccion.equals(TODOS)) ? null : seleccion;
    }

    private PedidoRegistro obtenerSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona primero un pedido de la tabla.");
            return null;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String direccion = (String) modeloTabla.getValueAt(fila, 1);
        String tipo = (String) modeloTabla.getValueAt(fila, 2);
        String estado = (String) modeloTabla.getValueAt(fila, 3);
        return new PedidoRegistro(id, direccion, tipo, estado);
    }

    /**
     * Abre un formulario pequeño (JPanel dentro de un JOptionPane) con los
     * valores actuales del pedido seleccionado, permite modificarlos y
     * guarda los cambios con PedidoControlador.actualizarPedido(...).
     */
    private void editarSeleccionado() {
        PedidoRegistro actual = obtenerSeleccionado();
        if (actual == null) return;

        JTextField txtDireccion = new JTextField(actual.direccion());
        JComboBox<String> cmbTipo = new JComboBox<>(TIPOS);
        cmbTipo.setSelectedItem(actual.tipo());
        JComboBox<String> cmbEstado = new JComboBox<>(ESTADOS);
        cmbEstado.setSelectedItem(actual.estado());

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Dirección:"));
        panel.add(txtDireccion);
        panel.add(new JLabel("Tipo:"));
        panel.add(cmbTipo);
        panel.add(new JLabel("Estado:"));
        panel.add(cmbEstado);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Editar pedido #" + actual.id(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;

        String direccion = txtDireccion.getText().trim();
        if (direccion.isBlank()) {
            JOptionPane.showMessageDialog(this, "La dirección no puede estar vacía.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PedidoRegistro actualizado = new PedidoRegistro(actual.id(), direccion,
                (String) cmbTipo.getSelectedItem(), (String) cmbEstado.getSelectedItem());

        try {
            controlador.actualizarPedido(actualizado);
            JOptionPane.showMessageDialog(this, "Pedido #" + actual.id() + " actualizado correctamente.");
            cargarDatos();
        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSeleccionado() {
        PedidoRegistro actual = obtenerSeleccionado();
        if (actual == null) return;

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el pedido #" + actual.id() + "?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmacion != JOptionPane.YES_OPTION) return;

        try {
            controlador.eliminarPedido(actual.id());
            JOptionPane.showMessageDialog(this, "Pedido #" + actual.id() + " eliminado.");
            cargarDatos();
        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}
