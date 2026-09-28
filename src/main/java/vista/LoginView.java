package vista;

import conexion.ConexionBD;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Ventana de inicio de sesión.
 *
 * Valida el correo y la contraseña contra la tabla "usuarios" de la base de
 * datos (speedfast_db) usando ConexionBD. Si son correctos, cierra el login
 * y ejecuta la acción que se le pase en el constructor (normalmente, abrir
 * VentanaPrincipal), por lo que sigue sin conocer al resto de las ventanas.
 */
public class LoginView extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtClave;

    public LoginView(Runnable alIniciarSesion) {
        setTitle("SpeedFastApp - Iniciar sesión");
        setSize(360, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        crearComponentes(alIniciarSesion);
    }

    private void crearComponentes(Runnable alIniciarSesion) {
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Acceso al sistema", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 15f));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 0, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(2, 2, 8, 8));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        panelCentro.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panelCentro.add(txtCorreo);

        panelCentro.add(new JLabel("Contraseña:"));
        txtClave = new JPasswordField();
        panelCentro.add(txtClave);

        add(panelCentro, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnIngresar = new JButton("Ingresar");
        panelBotones.add(btnIngresar);
        add(panelBotones, BorderLayout.SOUTH);

        // Enter dentro de la clave también intenta iniciar sesión
        txtClave.addActionListener(e -> validarLogin(alIniciarSesion));
        btnIngresar.addActionListener(e -> validarLogin(alIniciarSesion));
    }

    private void validarLogin(Runnable alIniciarSesion) {
        String correo = txtCorreo.getText().trim();
        String clave = new String(txtClave.getPassword());

        if (correo.isBlank() || clave.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa tu correo y tu contraseña.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (credencialesValidas(correo, clave)) {
                dispose();
                alIniciarSesion.run();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Correo o contraseña incorrectos.",
                        "Error de acceso", JOptionPane.ERROR_MESSAGE);
                txtClave.setText("");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo conectar con la base de datos:\n" + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Consulta la tabla usuarios con una sentencia parametrizada
     * (PreparedStatement), para que lo escrito en los campos nunca se
     * interprete como SQL.
     */
    private boolean credencialesValidas(String correo, String clave) throws Exception {
        String sql = "SELECT 1 FROM usuarios WHERE correo = ? AND password = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, clave);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}

