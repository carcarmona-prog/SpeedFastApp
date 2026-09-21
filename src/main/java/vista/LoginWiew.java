package vista;

import controlador.UsuarioControlador;
import modelo.Usuario;

import javax.swing.*;

import java.awt.*;

import static sun.tools.jconsole.inspector.XDataViewer.dispose;

public class LoginWiew extends JFrame {
   private JTextField txtCorreo;
   private JPasswordField txtContrasena;
   private JButton btnLoginSesion;
    private UsuarioControlador controlador = new UsuarioControlador();


    public LoginWiew() {
        setTitle("Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        crearComponentes();
    }

    public void crearComponentes() {
        setLayout(null);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setBounds(30, 30, 100, 30);
        add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setBounds(130, 30, 200, 30);
        add(txtCorreo);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(30, 80, 100, 30);
        add(lblContrasena);

        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(130, 80, 200, 30);
        add(txtContrasena);

        btnLoginSesion = new JButton("Login");
        btnLoginSesion.setBounds(130, 130, 200, 30);
        add(btnLoginSesion);

        btnLoginSesion.addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();

        Usuario usuarioEncontrado = controlador.validarCredenciales(correo, contrasena);
        if (usuarioEncontrado != null) {
            JOptionPane.showMessageDialog(this,"inicio de sesion completa, BIENVENIDO!" + usuarioEncontrado.getNombre());
            GestorUsuarioView gestorUsuarioView = new GestorUsuarioView();
            gestorUsuarioView.setVisible(true);
            dispose();
        }else {
            JOptionPane.showMessageDialog(this,"Ingrese un correo o contraseña correcta.");
        }

    }






}
