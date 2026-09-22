package vista;

import controlador.PedidoControlador;
import modelo.Pedido;
import modelo.Repartidor;
import modelo.ServicioComida;
import modelo.ServicioComprasExpress;
import modelo.ServicioEncomiendas;

import javax.swing.*;
import java.awt.*;

/**
 * Formulario de registro de pedidos (Paso 2).
 *
 * El enunciado pide 3 campos (ID, Dirección, Tipo), pero los constructores
 * reales de ServicioComida / ServicioComprasExpress / ServicioEncomiendas
 * (de semanas anteriores) exigen más datos que eso (cliente, distancia, y
 * algunos campos propios de cada tipo). Por eso el formulario se separa en:
 *   - Datos comunes: ID, Cliente, Dirección, Distancia y Tipo.
 *   - Un panel de datos específicos que cambia según el Tipo elegido
 *     (CardLayout), con valores por defecto razonables si se dejan en blanco.
 */
public class VentanaRegistroPedido extends JFrame {

    private JTextField txtId;
    private JTextField txtCliente;
    private JTextField txtDireccion;
    private JTextField txtDistancia;
    private JComboBox<String> cmbTipo;

    // Panel con los 3 "cards" de datos específicos por tipo de servicio
    private final CardLayout cardLayout = new CardLayout();
    private JPanel panelEspecifico;

    // Campos específicos de Comida
    private JTextField txtRestaurante;
    private JCheckBox chkMochila;

    // Campos específicos de Encomienda
    private JTextField txtPeso;
    private JTextField txtEmbalaje;

    private final PedidoControlador controlador = new PedidoControlador();

    public VentanaRegistroPedido() {
        setTitle("Registrar Pedido");
        setSize(430, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        crearComponentes();
    }

    private void crearComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelDatosComunes = new JPanel(new GridLayout(0, 2, 8, 8));
        panelDatosComunes.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        panelDatosComunes.add(new JLabel("ID:"));
        txtId = new JTextField();
        panelDatosComunes.add(txtId);

        panelDatosComunes.add(new JLabel("Cliente:"));
        txtCliente = new JTextField();
        panelDatosComunes.add(txtCliente);

        panelDatosComunes.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panelDatosComunes.add(txtDireccion);

        panelDatosComunes.add(new JLabel("Distancia (km):"));
        txtDistancia = new JTextField();
        panelDatosComunes.add(txtDistancia);

        panelDatosComunes.add(new JLabel("Tipo:"));
        cmbTipo = new JComboBox<>(new String[]{"Comida", "Encomienda", "Compra Express"});
        // Al cambiar el tipo, se muestra el "card" con los campos propios
        // de ese tipo de servicio (ver crearPanelEspecifico()).
        cmbTipo.addActionListener(e -> cardLayout.show(panelEspecifico, (String) cmbTipo.getSelectedItem()));
        panelDatosComunes.add(cmbTipo);

        add(panelDatosComunes, BorderLayout.NORTH);
        add(crearPanelEspecifico(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelEspecifico() {
        panelEspecifico = new JPanel(cardLayout);
        panelEspecifico.setBorder(BorderFactory.createTitledBorder("Datos específicos del tipo"));

        // --- Card: Comida ---
        JPanel panelComida = new JPanel(new GridLayout(0, 2, 8, 8));
        panelComida.add(new JLabel("Restaurante:"));
        txtRestaurante = new JTextField();
        panelComida.add(txtRestaurante);
        panelComida.add(new JLabel("¿Mochila térmica?"));
        chkMochila = new JCheckBox();
        panelComida.add(chkMochila);
        panelEspecifico.add(panelComida, "Comida");

        // --- Card: Encomienda ---
        JPanel panelEncomienda = new JPanel(new GridLayout(0, 2, 8, 8));
        panelEncomienda.add(new JLabel("Peso (kg):"));
        txtPeso = new JTextField();
        panelEncomienda.add(txtPeso);
        panelEncomienda.add(new JLabel("Tipo de embalaje:"));
        txtEmbalaje = new JTextField();
        panelEncomienda.add(txtEmbalaje);
        panelEspecifico.add(panelEncomienda, "Encomienda");

        // --- Card: Compra Express (no requiere datos adicionales) ---
        JPanel panelExpress = new JPanel(new BorderLayout());
        panelExpress.add(new JLabel("Sin datos adicionales para Compra Express.", SwingConstants.CENTER));
        panelEspecifico.add(panelExpress, "Compra Express");

        return panelEspecifico;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardarPedido());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        return panelBotones;
    }

    /**
     * Valida los campos, construye el Pedido concreto según el Tipo elegido
     * y lo agrega a la lista compartida a través del controlador.
     */
    private void guardarPedido() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            if (id <= 0) {
                throw new IllegalArgumentException("El ID debe ser un número mayor que 0.");
            }
            if (controlador.existeId(id)) {
                throw new IllegalArgumentException("Ya existe un pedido registrado con el ID " + id + ".");
            }

            String cliente = txtCliente.getText().trim();
            if (cliente.isBlank()) {
                throw new IllegalArgumentException("El cliente no puede estar vacío.");
            }

            String direccion = txtDireccion.getText().trim();
            if (direccion.isBlank()) {
                throw new IllegalArgumentException("La dirección no puede estar vacía.");
            }

            double distancia = Double.parseDouble(txtDistancia.getText().trim());
            if (distancia <= 0) {
                throw new IllegalArgumentException("La distancia debe ser un número mayor que 0.");
            }

            String tipo = (String) cmbTipo.getSelectedItem();

            // El pedido se crea con un repartidor "placeholder": todavía no
            // hay un repartidor real hasta que se use la ventana
            // "Asignar repartidor / Iniciar entrega" (Repartidor.setRepartidor()).
            Repartidor sinAsignar = new Repartidor("Por asignar");

            Pedido nuevoPedido = switch (tipo) {
                case "Comida" -> {
                    String restaurante = txtRestaurante.getText().trim();
                    if (restaurante.isBlank()) restaurante = "Restaurante sin especificar";
                    yield new ServicioComida(id, cliente, direccion, distancia, "Pedido de comida",
                            distancia * 2, true, restaurante, chkMochila.isSelected(), sinAsignar);
                }
                case "Encomienda" -> {
                    double peso = parseDoubleODefecto(txtPeso.getText(), 1.0);
                    String embalaje = txtEmbalaje.getText().trim();
                    if (embalaje.isBlank()) embalaje = "Caja";
                    yield new ServicioEncomiendas(id, cliente, direccion, distancia, "Pedido de encomienda",
                            distancia * 1.5, true, peso, embalaje, sinAsignar);
                }
                default -> new ServicioComprasExpress(id, cliente, direccion, distancia,
                        "Compra express", distancia, true, sinAsignar) {
                    @Override
                    public void setRepartidor(Repartidor repartidor) {

                    }
                };
            };

            controlador.registrarPedido(nuevoPedido);
            JOptionPane.showMessageDialog(this, "Pedido #" + id + " registrado correctamente.");
            limpiarFormulario();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID y la distancia deben ser numéricos.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double parseDoubleODefecto(String texto, double valorPorDefecto) {
        try {
            return Double.parseDouble(texto.trim());
        } catch (NumberFormatException ex) {
            return valorPorDefecto;
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtCliente.setText("");
        txtDireccion.setText("");
        txtDistancia.setText("");
        txtRestaurante.setText("");
        chkMochila.setSelected(false);
        txtPeso.setText("");
        txtEmbalaje.setText("");
    }
}
