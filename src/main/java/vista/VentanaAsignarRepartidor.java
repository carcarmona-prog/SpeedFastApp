package vista;

import controlador.PedidoControlador;
import controlador.RepartidorControlador;
import dao.DaoException;
import modelo.PedidoRegistro;
import modelo.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana de asignación de repartidor / inicio de entrega (cuarto botón del
 * menú principal). Permite elegir un pedido PENDIENTE y un repartidor, ambos
 * leídos de la base de datos; al asignar se registra la entrega
 * (tabla entrega), el pedido pasa a EN_REPARTO y se simula el viaje hasta
 * dejarlo ENTREGADO, también en la base de datos.
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
    private JComboBox<String> cmbRepartidores;
    private JLabel lblEstado;

    private final PedidoControlador pedidoControlador = new PedidoControlador();
    private final RepartidorControlador repartidorControlador = new RepartidorControlador();

    private List<PedidoRegistro> pedidosPendientes = new ArrayList<>();
    private List<Repartidor> repartidores = new ArrayList<>();

    public VentanaAsignarRepartidor() {
        setTitle("Asignar repartidor / Iniciar entrega");
        setSize(460, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        crearComponentes();
        cargarDatos();
    }

    private void crearComponentes() {
        setLayout(new GridLayout(0, 2, 8, 8));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(new JLabel("Pedido pendiente:"));
        cmbPedidos = new JComboBox<>();
        add(cmbPedidos);

        add(new JLabel("Repartidor:"));
        cmbRepartidores = new JComboBox<>();
        add(cmbRepartidores);

        JButton btnAsignar = new JButton("Asignar e iniciar entrega");
        btnAsignar.addActionListener(e -> asignarYSimularEntrega());
        add(btnAsignar);

        lblEstado = new JLabel(" ");
        add(lblEstado);
    }

    /**
     * Lee de la base de datos los pedidos pendientes y los repartidores, y
     * llena los combos. Se arma una etiqueta legible por elemento en vez de
     * depender de toString().
     */
    private void cargarDatos() {
        try {
            pedidosPendientes = new ArrayList<>(pedidoControlador.listarPendientes());
            repartidores = new ArrayList<>(repartidorControlador.listarRepartidores());
        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (PedidoRegistro p : pedidosPendientes) {
            cmbPedidos.addItem("#" + p.id() + " - " + p.tipo() + " (" + p.direccion() + ")");
        }
        for (Repartidor r : repartidores) {
            cmbRepartidores.addItem("#" + r.getId() + " - " + r.getNombreRepartidor());
        }
    }

    private void asignarYSimularEntrega() {
        int indicePedido = cmbPedidos.getSelectedIndex();
        int indiceRepartidor = cmbRepartidores.getSelectedIndex();

        if (indicePedido < 0) {
            JOptionPane.showMessageDialog(this, "No hay pedidos pendientes para asignar.");
            return;
        }
        if (indiceRepartidor < 0) {
            JOptionPane.showMessageDialog(this,
                    "No hay repartidores registrados. Regístrelos desde \"Registrar repartidor\".");
            return;
        }

        PedidoRegistro pedido = pedidosPendientes.get(indicePedido);
        Repartidor repartidor = repartidores.get(indiceRepartidor);

        try {
            // Guarda la entrega y deja el pedido EN_REPARTO en la base de datos
            pedidoControlador.iniciarEntrega(pedido.id(), repartidor.getId());
        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // El pedido ya no está pendiente: se quita de la lista para no
        // poder asignarlo dos veces.
        pedidosPendientes.remove(indicePedido);
        cmbPedidos.removeItemAt(indicePedido);

        lblEstado.setText("Pedido #" + pedido.id() + ": EN REPARTO");
        JOptionPane.showMessageDialog(this,
                "Repartidor " + repartidor.getNombreRepartidor() + " asignado. Entrega del pedido #"
                        + pedido.id() + " iniciada.");

        iniciarSimulacionDeEntrega(pedido.id());
    }

    /**La espera de 4 segundos y el UPDATE corren en un hilo aparte. Si corrieran en el hilo de Swing, la ventana se congelaría.
     Swing solo permite tocar componentes desde su hilo propio. Por eso el cambio de la etiqueta va dentro de SwingUtilities.invokeLater(...).
     La lambda necesita variables "efectivamente finales", por eso el resultado se copia a textoFinal antes de usarlo.
     * @param idPedido muestra el id del pedido simulado.
     */
    private void iniciarSimulacionDeEntrega(int idPedido) {
        Thread hiloEntrega = new Thread(() -> {
            try {
                Thread.sleep(4000); // simula el tiempo de viaje del repartidor
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }

            String resultado;
            try {
                pedidoControlador.marcarEntregado(idPedido);
                resultado = "Pedido #" + idPedido + ": ENTREGADO";
            } catch (DaoException ex) {
                resultado = "Pedido #" + idPedido + ": error al marcar como entregado";
            }

            // Los componentes Swing solo deben modificarse desde el Event
            // Dispatch Thread: por eso el resultado se aplica con
            // invokeLater() en vez de tocar lblEstado directamente aquí.
            String textoFinal = resultado;
            SwingUtilities.invokeLater(() -> lblEstado.setText(textoFinal));
        }, "hilo-entrega-" + idPedido);

        hiloEntrega.start();
    }
}

