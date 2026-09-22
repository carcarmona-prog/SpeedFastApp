package app;

import vista.VentanaPrincipal;

import javax.swing.*;

/**
 * Punto de entrada de la GUI de gestión de entregas, tal como lo pide el
 * enunciado ("una clase Main en el paquete main, llamando a
 * new VentanaPrincipal()"). Es un punto de entrada adicional: no reemplaza
 * a app.Main (la demo de consola de semanas anteriores) ni a
 * vista.VistaMain (el flujo de login), que siguen funcionando igual.
 */
public class MainSwing {

    public static void main(String[] args) {
        // Toda la creación/manipulación de componentes Swing debe ocurrir
        // en el Event Dispatch Thread; invokeLater() la encola ahí.
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
