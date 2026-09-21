package vista;

import data.UsuarioData;

import javax.swing.*;

public class VistaMain {

    public static void main(String[] args) {
        UsuarioData.cargarUsuario();

        SwingUtilities.invokeLater(()->{
        LoginWiew login = new LoginWiew();
        login.setVisible(true);
        });
    }
}
