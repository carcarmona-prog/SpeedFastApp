package vista;

import controlador.RepartidorControlador;
import dao.DaoException;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Formulario de registro de repartidores. Guarda el repartidor en la base de
 * datos y muestra en una JTable todos los repartidores ya registrados.
 */
public class VentanaRegistroRepartidor extends JFrame {

    private JTextField txtNombre;
    private DefaultTableModel modeloTabla;
    private final RepartidorControlador controlador = new RepartidorControlador();

    public VentanaRegistroRepartidor() {
        setTitle("Registrar Repartidor");
        setSize(430, 380);
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
        btnGuardar.addActionListener(e -> guardarRepartidor());
        txtNombre.addActionListener(e -> guardarRepartidor()); // Enter también guarda
        panelFormulario.add(btnGuardar, BorderLayout.EAST);

        add(panelFormulario, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scroll = new JScrollPane(new JTable(modeloTabla));
        scroll.setBorder(BorderFactory.createTitledBorder("Repartidores registrados"));
        add(scroll, BorderLayout.CENTER);
    }

    private void guardarRepartidor() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isBlank()) {
            JOptionPane.showMessageDialog(this, "El nombre del repartidor no puede estar vacío.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = controlador.registrarRepartidor(new Repartidor(nombre));
            JOptionPane.showMessageDialog(this, "Repartidor #" + id + " registrado correctamente.");
            txtNombre.setText("");
            cargarRepartidores();
        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
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
