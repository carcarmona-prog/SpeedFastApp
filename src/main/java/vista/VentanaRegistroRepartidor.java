package vista;

import controlador.RepartidorControlador;
import dao.DaoException;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Formulario de registro de repartidores. Guarda, lista, edita y elimina
 * repartidores en la base de datos (CRUD completo sobre la tabla
 * "repartidor"); la tabla muestra siempre los que ya están registrados.
 */
public class VentanaRegistroRepartidor extends JFrame {

    private JTextField txtNombre;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    // id del repartidor que se está editando; -1 si el formulario está en
    // modo "nuevo registro".
    private int idEnEdicion = -1;

    private final RepartidorControlador controlador = new RepartidorControlador();

    public VentanaRegistroRepartidor() {
        setTitle("Registrar Repartidor");
        setSize(460, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        crearComponentes();
        cargarRepartidores();
    }

    private void crearComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new BorderLayout(8, 8));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        panelFormulario.add(new JLabel("Nombre:"), BorderLayout.WEST);
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarOActualizar());
        txtNombre.addActionListener(e -> guardarOActualizar()); // Enter también guarda
        panelFormulario.add(btnGuardar, BorderLayout.EAST);

        add(panelFormulario, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        // Al seleccionar una fila, se carga en el formulario para editarla.
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccionEnFormulario();
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Repartidores registrados"));
        add(scroll, BorderLayout.CENTER);

        add(crearPanelBotonesInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelBotonesInferior() {
        JPanel panel = new JPanel();

        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        JButton btnCancelarEdicion = new JButton("Cancelar edición");
        btnCancelarEdicion.addActionListener(e -> limpiarFormulario());

        panel.add(btnEliminar);
        panel.add(btnCancelarEdicion);
        return panel;
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        idEnEdicion = (int) modeloTabla.getValueAt(fila, 0);
        txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
    }

    /**
     * Si no hay ningún repartidor seleccionado en la tabla, crea uno nuevo;
     * si hay uno seleccionado (idEnEdicion != -1), actualiza ese registro.
     */
    private void guardarOActualizar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isBlank()) {
            JOptionPane.showMessageDialog(this, "El nombre del repartidor no puede estar vacío.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (idEnEdicion == -1) {
                int id = controlador.registrarRepartidor(new Repartidor(nombre));
                JOptionPane.showMessageDialog(this, "Repartidor #" + id + " registrado correctamente.");
            } else {
                Repartidor repartidor = new Repartidor(nombre);
                repartidor.setId(idEnEdicion);
                controlador.actualizarRepartidor(repartidor);
                JOptionPane.showMessageDialog(this, "Repartidor #" + idEnEdicion + " actualizado correctamente.");
            }
            limpiarFormulario();
            cargarRepartidores();
        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona primero un repartidor de la tabla.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar al repartidor #" + id + "?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmacion != JOptionPane.YES_OPTION) return;

        try {
            controlador.eliminarRepartidor(id);
            JOptionPane.showMessageDialog(this, "Repartidor #" + id + " eliminado.");
            limpiarFormulario();
            cargarRepartidores();
        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        idEnEdicion = -1;
        txtNombre.setText("");
        tabla.clearSelection();
    }

    private void cargarRepartidores() {
        try {
            modeloTabla.setRowCount(0);
            for (Repartidor r : controlador.listarRepartidores()) {
                modeloTabla.addRow(new Object[]{r.getId(), r.getNombreRepartidor()});
            }
        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}