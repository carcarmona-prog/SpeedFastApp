package conexion;

public class ProbarConexion {
    public static void main(String[] args) throws Exception {
        try (var c = ConexionBD.obtenerConexion()) {
            System.out.println("Conectado a: " + c.getCatalog());
        }
    }
}
