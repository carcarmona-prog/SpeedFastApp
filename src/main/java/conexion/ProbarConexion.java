package conexion;

public class ProbarConexion {
    public static void main(String[] args) throws Exception {
        try (var connection = ConexionBD.obtenerConexion()) {
            System.out.println("Conectado a: " + connection.getCatalog());
        }
    }
}
