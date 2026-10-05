package vista;

import controlador.EntregaControlador;
import controlador.PedidoControlador;
import controlador.RepartidorControlador;
import dao.DaoException;
import modelo.Entrega;
import modelo.PedidoRegistro;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD completo de entregas: asocia un Pedido con un Repartidor (más fecha
 * y hora), y permite listar, editar y eliminar las entregas ya registradas.
 * Los combos de Pedido y Repartidor muestran un texto legible
 * ("#id - dato") pero por dentro guardan el id real de cada uno.
 *
 * Esta ventana es el CRUD manual sobre la tabla "entrega". Es distinta de
 * VentanaAsignarRepartidor, que automatiza el flujo de negocio completo
 * (crear la entrega, dejar el pedido EN_REPARTO y simular el viaje con un
 * hilo hasta marcarlo ENTREGADO).
 */
public class VentanaGestionEntregas extends JFrame {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private JComboBox<String> cmbPedidos;
    private JComboBox<String> cmbRepartidores;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private List<PedidoRegistro> pedidos = new ArrayList<>();
    private List<Repartidor> repartidores = new ArrayList<>();

    // id de la entrega que se está editando; -1 si el formulario está en
    // modo "nuevo registro".
    private int idEnEdicion = -1;

    private final EntregaControlador entregaControlador = new EntregaControlador();
    private final PedidoControlador pedidoControlador = new PedidoControlador();
    private final RepartidorControlador repartidorControlador = new RepartidorControlador();

    public VentanaGestionEntregas() {
        setTitle("Gestión de Entregas: SPEED FAST");
        setSize(760, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        crearComponentes();
        cargarCombos();
        cargarEntregas();
    }

    private void crearComponentes() {
        setLayout(new BorderLayout(10, 10));
        add(crearPanelFormulario(), BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Pedido", "Repartidor", "Fecha", "Hora"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccionEnFormulario();
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(crearPanelBotonesInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        panel.add(new JLabel("Pedido:"));
        cmbPedidos = new JComboBox<>();
        panel.add(cmbPedidos);

        panel.add(new JLabel("Repartidor:"));
        cmbRepartidores = new JComboBox<>();
        panel.add(cmbRepartidores);

        /**
         * LocalDate.now() obtiene la fecha de hoy, y .format(FORMATO_FECHA) la convierte a texto "2026-10-03" usando el formateador que definiste arriba.
         * Así el campo arranca pre-llenado con la fecha y hora actuales,
         * para que el usuario no tenga que escribirlas si va a registrar la entrega ahora mismo
         * (pero sí puede editarlas, por ejemplo para corregir una entrega pasada).
         */
        panel.add(new JLabel("Fecha (aaaa-mm-dd):"));
        txtFecha = new JTextField(LocalDate.now().format(FORMATO_FECHA));
        panel.add(txtFecha);

        panel.add(new JLabel("Hora (hh:mm):"));
        txtHora = new JTextField(LocalTime.now().format(FORMATO_HORA));
        panel.add(txtHora);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarOActualizar());
        panel.add(btnGuardar);

        JButton btnRefrescarCombos = new JButton("Refrescar pedidos/repartidores");
        btnRefrescarCombos.addActionListener(e -> cargarCombos());
        panel.add(btnRefrescarCombos);

        return panel;
    }

    private JPanel crearPanelBotonesInferior() {
        JPanel panel = new JPanel();

        JButton btnActualizarTabla = new JButton("Actualizar listado");
        btnActualizarTabla.addActionListener(e -> cargarEntregas());

        JButton btnEliminar = new JButton("Eliminar seleccionada");
        btnEliminar.addActionListener(e -> eliminarSeleccionada());

        JButton btnCancelarEdicion = new JButton("Cancelar edición");
        btnCancelarEdicion.addActionListener(e -> limpiarFormulario());

        panel.add(btnActualizarTabla);
        panel.add(btnEliminar);
        panel.add(btnCancelarEdicion);
        return panel;
    }

    /** Llena los combos de Pedido y Repartidor con lo que haya en la base de datos. */
    private void cargarCombos() {
        try {
            pedidos = new ArrayList<>(pedidoControlador.listarPedidos());
            repartidores = new ArrayList<>(repartidorControlador.listarRepartidores());
        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cmbPedidos.removeAllItems();
        for (PedidoRegistro p : pedidos) {
            cmbPedidos.addItem("#" + p.id() + " - " + p.tipo() + " (" + p.direccion() + ")");
        }
        cmbRepartidores.removeAllItems();
        for (Repartidor r : repartidores) {
            cmbRepartidores.addItem("#" + r.getId() + " - " + r.getNombreRepartidor());
        }
    }

    private void cargarEntregas() {
        try {
            modeloTabla.setRowCount(0);
            for (Entrega entrega : entregaControlador.listarEntregas()) {
                modeloTabla.addRow(new Object[]{
                        entrega.getId(),
                        entrega.getIdPedido(),
                        entrega.getIdRepartidor(),
                        entrega.getFecha(),
                        entrega.getHora()
                });
            }
        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error al cargar la base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;

        idEnEdicion = (int) modeloTabla.getValueAt(fila, 0);
        int idPedido = (int) modeloTabla.getValueAt(fila, 1);
        int idRepartidor = (int) modeloTabla.getValueAt(fila, 2);

        seleccionarPorId(cmbPedidos, pedidos.stream().map(PedidoRegistro::id).toList(), idPedido);
        seleccionarPorId(cmbRepartidores, repartidores.stream().map(Repartidor::getId).toList(), idRepartidor);

        txtFecha.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtHora.setText(modeloTabla.getValueAt(fila, 4).toString().substring(0, 5));
    }

    private void seleccionarPorId(JComboBox<String> combo, List<Integer> ids, int id) {
        int indice = ids.indexOf(id);
        if (indice >= 0) combo.setSelectedIndex(indice);
    }

    /**
     * Si no hay ninguna entrega seleccionada en la tabla, crea una nueva;
     * si hay una seleccionada (idEnEdicion != -1), actualiza ese registro.
     */
    private void guardarOActualizar() {
        int indicePedido = cmbPedidos.getSelectedIndex();
        int indiceRepartidor = cmbRepartidores.getSelectedIndex();

        if (indicePedido < 0 || indiceRepartidor < 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe haber al menos un pedido y un repartidor registrados.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate fecha;
        LocalTime hora;
        try {
            fecha = LocalDate.parse(txtFecha.getText().trim(), FORMATO_FECHA);
            hora = LocalTime.parse(txtHora.getText().trim(), FORMATO_HORA);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "La fecha debe tener el formato aaaa-mm-dd y la hora hh:mm.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idPedido = pedidos.get(indicePedido).id();
        int idRepartidor = repartidores.get(indiceRepartidor).getId();

        try {
            if (idEnEdicion == -1) {
                Entrega nueva = new Entrega(0, idPedido, idRepartidor, fecha, hora);
                int id = entregaControlador.registrarEntrega(nueva);
                JOptionPane.showMessageDialog(this, "Entrega #" + id + " registrada correctamente.");
            } else {
                Entrega actualizada = new Entrega(idEnEdicion, idPedido, idRepartidor, fecha, hora);
                entregaControlador.actualizarEntrega(actualizada);
                JOptionPane.showMessageDialog(this, "Entrega #" + idEnEdicion + " actualizada correctamente.");
            }
            limpiarFormulario();
            cargarEntregas();
        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona primero una entrega de la tabla.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la entrega #" + id + "?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmacion != JOptionPane.YES_OPTION) return;

        try {
            entregaControlador.eliminarEntrega(id);
            JOptionPane.showMessageDialog(this, "Entrega #" + id + " eliminada.");
            limpiarFormulario();
            cargarEntregas();
        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        idEnEdicion = -1;
        txtFecha.setText(LocalDate.now().format(FORMATO_FECHA));
        txtHora.setText(LocalTime.now().format(FORMATO_HORA));
        tabla.clearSelection();
    }
}