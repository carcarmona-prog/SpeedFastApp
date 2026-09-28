package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/speedfast_db?createDatabaseIfNotExist=true"; //comando para crear bases de datos en el workbrench si no existen

    private static final String USER = "speedfast";

    private static final String PASSWORD = "speedfast123";

    static{
        inicializarTabla();

    }

    public static Connection obtenerConexion() throws Exception{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }

    private static void inicializarTabla() {

        String sqlUsuarios =
                """
                CREATE TABLE IF NOT EXISTS usuarios (
                id INT AUTO_INCREMENT PRIMARY KEY,
                correo VARCHAR(80) UNIQUE,
                password VARCHAR(50)
                )
                """
                ;

         String sqlUsuarioAdmin =
                 """
                  INSERT IGNORE INTO usuarios (correo, password)
                  VALUES ('admin@spf.cl', 'admin123')
                  """
                 ;



        String sqlRepartidor = """
        CREATE TABLE IF NOT EXISTS repartidor (
            id INT AUTO_INCREMENT PRIMARY KEY,
            nombre VARCHAR(100) NOT NULL
        )
        """;

        String sqlPedido = """
        CREATE TABLE IF NOT EXISTS pedido (
            id INT AUTO_INCREMENT PRIMARY KEY,
            direccion VARCHAR(150) NOT NULL,
            tipo VARCHAR(30) NOT NULL,
            estado VARCHAR(20) NOT NULL
        )
        """;

        String sqlEntrega = """
        CREATE TABLE IF NOT EXISTS entrega (
            id INT AUTO_INCREMENT PRIMARY KEY,
            id_pedido INT NOT NULL,
            id_repartidor INT NOT NULL,
            fecha DATE NOT NULL,
            hora TIME NOT NULL,
            FOREIGN KEY (id_pedido) REFERENCES pedido(id),
            FOREIGN KEY (id_repartidor) REFERENCES repartidor(id)
        )
        """;

        try (Connection conexion = obtenerConexion();
             Statement statement = conexion.createStatement();
        ){

            statement.execute(sqlUsuarios);
            statement.execute(sqlUsuarioAdmin);

            statement.executeUpdate(sqlRepartidor);
            statement.executeUpdate(sqlPedido);
            statement.executeUpdate(sqlEntrega);

        } catch (SQLException e) {
            System.out.println("Error al inicializar Conexion BD: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }


    }

}
