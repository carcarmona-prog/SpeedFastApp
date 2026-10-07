package vista;

import conexion.ConexionBD;


import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        String claveHasheada =  hashear(clave);

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, claveHasheada);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Calcula el hash SHA-256 de un texto y lo devuelve en hexadecimal (64
     * caracteres). Es el mismo cálculo que hace ConexionBD al crear el
     * usuario administrador; cada clase tiene su propia copia de este
     * método (en vez de compartir una clase utilitaria aparte) para no
     * depender de un paquete nuevo.
     *
     * 📌 ¿Para qué sirve?:
     * Seguridad de contraseñas: en lugar de guardar la contraseña original en la base de datos, se guarda el hash. Así, aunque alguien acceda a la base de datos, no verá las contraseñas en texto plano.
     * Verificación de integridad: se puede usar para comprobar que un archivo o mensaje no ha sido alterado. Si el hash coincide, el contenido es idéntico.
     * Identificadores únicos: a veces se usa para generar un identificador único de un texto o documento.
     * 🧭 Contexto típico de uso:
     * En aplicaciones como la que describes (gestión de usuarios en una base de datos):
     * Cuando un usuario se registra, su contraseña se hashea y se guarda el hash.
     * Cuando inicia sesión, la contraseña ingresada se vuelve a hashear y se compara con el hash guardado.
     * Si coinciden, significa que la contraseña es correcta, sin necesidad de almacenar la original.
     * Esto se hace porque los hashes son irreversibles: no puedes obtener la contraseña original a partir del hash, lo que protege la seguridad de los usuarios.
     */
    private String hashear(String textoPlano) {
        try{
            MessageDigest digest = MessageDigest.getInstance("sha-256");
            byte[] byteHash = digest.digest(textoPlano.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexadecimal = new StringBuilder(byteHash.length * 2);
            for (byte b : byteHash) {
                hexadecimal.append(String.format("%02x", b));
            }
            return hexadecimal.toString();

        }catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("no se puede hashear la contraseña");
        }
    }

}

