package vista;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la GUI de gestión de entregas (Paso 1).
 *
 * Usa BorderLayout para el título arriba (NORTH) y GridLayout para apilar
 * los 3 botones al centro (CENTER), cada uno abriendo una ventana distinta.
 * No guarda datos por sí misma: cada ventana hija lee/escribe la lista
 * compartida de pedidos a través de PedidoControlador.
 */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("SpeedFastApp - Gestión de Entregas");
        setSize(420, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        crearComponentes();
    }

    private void crearComponentes() {
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Sistema de Gestión de Entregas - SpeedFast", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 15f));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 0, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 12, 12));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(25, 60, 25, 60));

        JButton btnRegistrar = new JButton("Registrar pedido");
        JButton btnListar = new JButton("Listar pedidos");
        JButton btnAsignar = new JButton("Asignar repartidor / Iniciar entrega");

        // Cada botón abre su propia ventana (JFrame independiente). Todas
        // comparten los mismos datos porque todas usan PedidoControlador,
        // que a su vez lee/escribe la misma lista estática PedidoData.pedidos.
        btnRegistrar.addActionListener(e -> new VentanaRegistroPedido().setVisible(true));
        btnListar.addActionListener(e -> new VentanaListaPedidos().setVisible(true));
        btnAsignar.addActionListener(e -> new VentanaAsignarRepartidor().setVisible(true));

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnListar);
        panelBotones.add(btnAsignar);

        add(panelBotones, BorderLayout.CENTER);
    }
}
