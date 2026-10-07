package conexion;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;

public class ConexionBD {

   private static final String URL;

   private static final String USER;

   private static final String PASSWORD;

   static{
       Properties configuracion = cargarConfiguraion();
       URL = configuracion.getProperty("db.url");
       USER = configuracion.getProperty("db.user");
       PASSWORD = configuracion.getProperty("db.password");

       inicializarTabla();


       /**
        * Arma la configuración de conexión en este orden de prioridad (lo que
        * se encuentra primero gana):
        *
        * 1) Variables de entorno (DB_URL, DB_USER, DB_PASSWORD). Es la forma
        *    recomendada para un despliegue real: las credenciales nunca
        *    quedan escritas en ningún archivo del proyecto.
        * 2) El archivo src/main/resources/db.properties, si existe. Está
        *    listado en .gitignore a propósito, así que cada persona que
        *    clona el repositorio configura el suyo local y nunca se sube a
        *    GitHub (revisa db.properties.example para ver el formato).
        * 3) Valores por defecto, codificados aquí abajo, solo para que el
        *    proyecto compile y corra "out of the box" en un entorno de
        *    evaluación que no configuró nada. No deben usarse en producción.
        */

    }
    private static Properties cargarConfiguraion(){
       Properties valoresPorDefecto = new Properties();
       valoresPorDefecto.setProperty("db.url", "jdbc:mysql://localhost:3306/speedfast_db?createDatabaseIfNotExist=true");
       valoresPorDefecto.setProperty("db.user", "speedfast");
       valoresPorDefecto.setProperty("db.password", "speedfast123");

       Properties configuracion = new Properties(valoresPorDefecto);

       try(InputStream entrada = ConexionBD.class.getResourceAsStream("/db,properties")){
           if(entrada != null){
               configuracion.load(entrada);
           }

       }catch (IOException e){
           System.out.println("no se pudo leer db.properties, se usara la configuracion por defecto;" + e.getMessage());
       }

        sobrescribirConVariableDeEntorno(configuracion, "DB_URL", "db.url");
        sobrescribirConVariableDeEntorno(configuracion, "DB_USER", "db.user");
        sobrescribirConVariableDeEntorno(configuracion, "DB_PASSWORD", "db.password");

        return configuracion;
    }

    private static void sobrescribirConVariableDeEntorno(Properties configuracion, String variableDeEntorno, String clave) {
        String valor = System.getenv(variableDeEntorno);
        if (valor != null && !valor.isBlank()) {
            configuracion.setProperty(clave, valor);
        }
    }

    public static Connection obtenerConexion() throws Exception{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }

    private static void inicializarTabla() {

        // password ahora guarda un hash SHA-256 (64 caracteres en hexadecimal),
        // nunca la contraseña en texto plano; por eso el ancho de columna
        // creció de VARCHAR(50) a VARCHAR(255).
        String sqlUsuarios =
                """
                CREATE TABLE IF NOT EXISTS usuarios (
                id INT AUTO_INCREMENT PRIMARY KEY,
                correo VARCHAR(80) UNIQUE,
                password VARCHAR(256)
                )
                """
                ;

        // El INSERT del usuario administrador se hace más abajo con un
        // PreparedStatement, para poder pasarle el hash calculado por
        // PasswordUtil en vez de escribir la contraseña en el SQL.
        String sqlUsuarioAdmin = "INSERT IGNORE INTO usuarios (correo, password) VALUES (?, ?)";

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

            statement.executeUpdate(sqlRepartidor);
            statement.executeUpdate(sqlPedido);
            statement.executeUpdate(sqlEntrega);

            try (PreparedStatement ps = conexion.prepareStatement(sqlUsuarioAdmin)) {
                ps.setString(1, "admin@spf.cl");
                ps.setString(2, hashear("admin123"));
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Error al inicializar Conexion BD: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }


    }

    /**
     * Calcula el hash SHA-256 de un texto y lo devuelve en hexadecimal (64
     * caracteres). Se usa para no guardar contraseñas en texto plano: tanto
     * aquí (al crear el usuario administrador) como en LoginView (al
     * validar el login) se guarda/compara el hash, nunca la contraseña
     * original. LoginView tiene su propia copia de este mismo método,
     * porque ambas clases son pequeñas y así se evita crear un paquete
     * nuevo solo para una función de pocas líneas.
     */
    private static String hashear(String textoPlano) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytesHash = digest.digest(textoPlano.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexadecimal = new StringBuilder(bytesHash.length * 2);
            for (byte b : bytesHash) {
                hexadecimal.append(String.format("%02x", b));
            }
            return hexadecimal.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No se pudo calcular el hash de la contraseña.", e);
        }
    }

}
