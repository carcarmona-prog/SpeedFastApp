package vista;

import controlador.UsuarioControlador;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GestorUsuarioView  extends JFrame {

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JComboBox<String> cmbTipo;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnIngresar;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnCancelar;
    private UsuarioControlador controlador = new UsuarioControlador();

    public GestorUsuarioView() {
        setTitle("Gestor de Usuarios");
        setSize(800,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        crearComponentes();
        cargarDatos();

    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);

        for(Usuario usuario : controlador.listaUsuarios()){
            modeloTabla.addRow(new Object[]{usuario.getId(),usuario.getNombre(),usuario.getCorreo(),usuario.getPerfil()});
        }
    }

    private void crearComponentes() {
        setLayout(null);
        JLabel lblNombre = new JLabel("Nombre");
        lblNombre.setBounds(10, 10, 80, 14);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(80, 10, 100, 20);
        add(txtNombre);

        JLabel lblCorreo = new JLabel("Correo");
        lblCorreo.setBounds(10, 40, 80, 14);
        add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setBounds(80, 40, 100, 20);
        add(txtCorreo);

        JLabel lblContrasena = new JLabel("Contrasena");
    }
}
