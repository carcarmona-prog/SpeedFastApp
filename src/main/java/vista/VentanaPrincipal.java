package vista;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la GUI de gestión de entregas (Paso 1).
 *
 * Usa BorderLayout para el título arriba (NORTH) y GridLayout para apilar
 * los 5 botones al centro (CENTER), cada uno abriendo una ventana distinta.
 * No guarda datos por sí misma: cada ventana hija lee/escribe en la base de
 * datos a través de PedidoControlador / RepartidorControlador.
 */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("SpeedFastApp - Gestor de Entregas");
        setSize(440, 380);
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

        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 12, 12));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(25, 60, 25, 60));

        JButton btnRegistrar = new JButton("Registrar pedido");
        JButton btnRepartidor = new JButton("Registrar repartidor");
        JButton btnListar = new JButton("Listar pedidos");
        JButton btnAsignar = new JButton("Asignar repartidor / Iniciar entrega");
        JButton btnEntregas = new JButton("Gestionar entregas");

        // Cada botón abre su propia ventana (JFrame independiente). Todas
        // comparten los mismos datos porque todas leen/escriben en la misma
        // base de datos (speedfast_db) a través de los controladores.
        btnRegistrar.addActionListener(e -> new VentanaRegistroPedido().setVisible(true));
        btnRepartidor.addActionListener(e -> new VentanaRegistroRepartidor().setVisible(true));
        btnListar.addActionListener(e -> new VentanaListaPedidos().setVisible(true));
        btnAsignar.addActionListener(e -> new VentanaAsignarRepartidor().setVisible(true));
        btnEntregas.addActionListener(e -> new VentanaGestionEntregas().setVisible(true));

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnRepartidor);
        panelBotones.add(btnListar);
        panelBotones.add(btnAsignar);
        panelBotones.add(btnEntregas);

        add(panelBotones, BorderLayout.CENTER);
    }
}