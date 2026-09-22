package vista;

import controlador.PedidoControlador;
import interfaces.Despachable;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Ventana de asignación de repartidor / inicio de entrega (tercer botón del
 * Paso 1). Permite elegir un pedido ya registrado, crear un Repartidor con
 * los datos ingresados, asignárselo (Despachable.setRepartidor +
 * asignarRepartidor()) y simular el viaje de entrega.
 *
 * Nota de concurrencia: la simulación de la entrega (Thread.sleep) se hace
 * en un hilo aparte, NO en este método (que corre en el Event Dispatch
 * Thread de Swing). Si durmiéramos aquí mismo, toda la ventana quedaría
 * congelada durante ese tiempo. Y cuando ese hilo termina, no puede tocar
 * componentes Swing directamente: por eso usa SwingUtilities.invokeLater()
 * para volver a actualizar la etiqueta desde el hilo correcto.
 */
public class VentanaAsignarRepartidor extends JFrame {

    private JComboBox<String> cmbPedidos;
    private JTextField txtNombreRepartidor;
    private JComboBox<String> cmbVehiculo;
    private JCheckBox chkMochila;
    private JLabel lblEstado;

    private final PedidoControlador controlador = new PedidoControlador();
    private List<Pedido> pedidosDisponibles;

    public VentanaAsignarRepartidor() {
        setTitle("Asignar repartidor / Iniciar entrega");
        setSize(430, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        crearComponentes();
    }

    private void crearComponentes() {
        setLayout(new GridLayout(0, 2, 8, 8));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pedidosDisponibles = controlador.listarPedidos();

        add(new JLabel("Pedido:"));
        cmbPedidos = new JComboBox<>();
        // Se arma una etiqueta legible por pedido en vez de depender de
        // toString() (algunas clases de Pedido lo usan para imprimir por
        // consola, no para representarlo como texto corto).
        for (Pedido p : pedidosDisponibles) {
            cmbPedidos.addItem("#" + p.getIdPedido() + " - " + p.getNombreCliente() + " (" + p.getDireccionDeEntrega() + ")");
        }
        add(cmbPedidos);

        add(new JLabel("Nombre del repartidor:"));
        txtNombreRepartidor = new JTextField();
        add(txtNombreRepartidor);

        add(new JLabel("Tipo de vehículo:"));
        cmbVehiculo = new JComboBox<>(new String[]{"Moto", "Bicicleta", "Auto", "Furgón"});
        add(cmbVehiculo);

        add(new JLabel("¿Mochila térmica?"));
        chkMochila = new JCheckBox();
        add(chkMochila);

        JButton btnAsignar = new JButton("Asignar y iniciar entrega");
        btnAsignar.addActionListener(e -> asignarYSimularEntrega());
        add(btnAsignar);

        lblEstado = new JLabel(" ");
        add(lblEstado);
    }

    private void asignarYSimularEntrega() {
        int indice = cmbPedidos.getSelectedIndex();
        if (indice < 0 || pedidosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay pedidos registrados para asignar.");
            return;
        }

        String nombre = txtNombreRepartidor.getText().trim();
        if (nombre.isBlank()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del repartidor.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pedido pedido = pedidosDisponibles.get(indice);
        Repartidor repartidor = new Repartidor(nombre, true, (String) cmbVehiculo.getSelectedItem(), chkMochila.isSelected());

        // Solo los pedidos "Despachables" (Comida, Encomienda, Compra
        // Express) saben asignar un repartidor y mostrar su información.
        if (pedido instanceof Despachable despachable) {
            despachable.setRepartidor(repartidor);
            despachable.asignarRepartidor(); // imprime en consola los datos del repartidor asignado
        }

        pedido.setEstadoPedido(EstadoPedido.EN_REPARTO);
        lblEstado.setText("Pedido #" + pedido.getIdPedido() + ": EN_REPARTO");
        JOptionPane.showMessageDialog(this,
                "Repartidor " + nombre + " asignado. Entrega del pedido #" + pedido.getIdPedido() + " iniciada.");

        iniciarSimulacionDeEntrega(pedido);
    }

    private void iniciarSimulacionDeEntrega(Pedido pedido) {
        Thread hiloEntrega = new Thread(() -> {
            try {
                Thread.sleep(4000); // simula el tiempo de viaje del repartidor
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }
            pedido.setEstadoPedido(EstadoPedido.ENTREGADO);

            // Los componentes Swing solo deben modificarse desde el Event
            // Dispatch Thread: por eso el resultado se aplica con
            // invokeLater() en vez de tocar lblEstado directamente aquí.
            SwingUtilities.invokeLater(() ->
                    lblEstado.setText("Pedido #" + pedido.getIdPedido() + ": ENTREGADO"));
        }, "hilo-entrega-" + pedido.getIdPedido());

        hiloEntrega.start();
    }
}
