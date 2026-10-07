# SpeedFastApp

👤 Autor del proyecto:

Carlos Estivens Carmona Barreto

carcarmona-prog

Carrera:

Analista Programador Computacional.

Instituto: Duoc UC.

📘 Descripción general del sistema SpeedFastApp:

Diseñamos un programa para la gestión de pedidos de la empresa SpeedFastApp, implementando técnicas que mejoren el manejo interno de los distintos tipos de servicios de entrega (comida, compras express y encomiendas), siempre pensando en el crecimiento del sistema. Creamos una base sólida implementando distintas técnicas de POO, para lograr un programa con estructura modular, demostrando el buen uso de la implementación que presta dicha forma de programar.

*Actualización semana 2: Esta semana implementamos métodos abstractos con funciones específicas dentro del programa que funciona como estructura util para el cálculo de los tiempos estimados de entrega usando clases abstractas.

*Actualización semana 3: En la semana 3 implementamos interfaces con la finalidad de crear nuevos contratos de comportamiento usando una estructura aislada de la lógica y asi demostrar la modularidad del diseño orientado a objetos.

*Actualización semana 4: Incorporamos el uso de hilos (Threads) para simular la preparación de pedidos de forma concurrente. Se creó la clase PedidoSync (pensada específicamente para trabajar con hilos) y la clase PrepararPedido, que implementa Runnable y se ejecuta a través de un ExecutorService, permitiendo administrar y finalizar los hilos de manera limpia.

*Actualización semana 5: Resolvimos el problema de coordinación de entregas: varios repartidores retirando pedidos al mismo tiempo desde una misma zona de carga, lo que podía provocar errores o entregas duplicadas. Se implementó la clase ZonaDeCarga como recurso compartido, usando synchronized para controlar el acceso concurrente y garantizar que cada pedido sea retirado y entregado por un único repartidor. La clase Repartidor ahora también puede ejecutarse como hilo (Runnable), retirando pedidos de la zona de carga, actualizando su estado (EN_REPARTO → ENTREGADO) y simulando la entrega con Thread.sleep().

*Actualización semana 6: Le agregamos una interfaz gráfica de escritorio al sistema, usando Java Swing. Ahora se pueden registrar pedidos, listarlos en una tabla y asignarles un repartidor para simular el inicio de la entrega, todo desde ventanas en vez de la consola. Separamos la lógica en capas (vista / controlador / data), reutilizando las clases del modelo (Pedido, Repartidor) que ya teníamos de semanas anteriores. Esta separación de responsabilidades demostró su valor esta misma semana: se eliminaron por completo las clases de login y gestión de usuarios (que no correspondían al enunciado) sin que el resto del programa se viera afectado.

*Actualización semana 7: Conectamos la aplicación a una base de datos MySQL (speedfast_db) usando JDBC, de modo que los pedidos, repartidores y entregas se guardan y se consultan de forma persistente en vez de vivir en una lista en memoria. Se creó la clase ConexionBD (paquete conexion), que gestiona la conexión con DriverManager y crea la base de datos y sus tablas (pedido, repartidor, entrega, además de usuarios y funcionarios) si todavía no existen. Se implementó el patrón DAO con las clases PedidoDAO, RepartidorDAO y EntregaDAO, todas con PreparedStatement para evitar inyección SQL y con try-with-resources para cerrar siempre los recursos; los errores se informan con la excepción propia DaoException. La capa controlador pasó a hablar con los DAO (PedidoControlador y el nuevo RepartidorControlador), por lo que las ventanas siguieron sin conocer nada de SQL. En la interfaz, los formularios registran pedidos y repartidores directamente en la base de datos (el ID de cada pedido lo genera MySQL con AUTO_INCREMENT), se agregó una ventana para registrar repartidores, el listado de pedidos se llena con una JTable desde la base y la asignación de repartidor registra la entrega y actualiza el estado del pedido (PENDIENTE → EN_REPARTO → ENTREGADO) en la base de datos. Además se incorporó una ventana de inicio de sesión (LoginView) que valida el correo y la contraseña contra la tabla usuarios antes de abrir la ventana principal.

*Actualización semana 8: Completamos el ciclo CRUD (crear, leer, actualizar y eliminar) sobre las tres entidades persistentes del sistema. Se definió la interfaz genérica CrudDAO&lt;T, ID&gt;, con los métodos create(), readAll(), update() y delete(), que ahora implementan PedidoDAO, RepartidorDAO y EntregaDAO; cada uno suma además sus propios métodos de consulta (PedidoDAO.filtrar(estado, tipo), EntregaDAO.listarPorPedido/listarPorRepartidor). Se creó el controlador EntregaControlador para el CRUD manual de entregas. En la interfaz: VentanaListaPedidos ahora permite filtrar los pedidos por tipo y/o estado, y editar o eliminar el seleccionado; VentanaRegistroRepartidor permite seleccionar un repartidor de la tabla para editarlo o eliminarlo; se agregó la ventana VentanaGestionEntregas, que registra, lista, edita y elimina entregas asociando un Pedido y un Repartidor mediante JComboBox (que muestran un texto legible pero conservan el id real), con fecha y hora editables. Se reforzaron las validaciones de entrada y el manejo de errores: por ejemplo, intentar eliminar un pedido o un repartidor con una entrega asociada ahora muestra un mensaje claro en vez de un error de SQL. VentanaAsignarRepartidor, de la semana 5, se mantuvo sin cambios: sigue siendo el flujo de negocio que simula el viaje de entrega con un hilo aparte, distinto del CRUD manual de entregas.

*Correcciones Consistencia JDBC: PedidoControlador.iniciarEntrega() registraba la entrega y actualizaba el estado del pedido con dos conexiones separadas, por lo que un fallo en el segundo paso podía dejar la base de datos inconsistente (una entrega registrada para un pedido que seguía PENDIENTE). Ahora ambas operaciones comparten una sola Connection dentro de una transacción (setAutoCommit(false), commit() si todo sale bien, rollback() si algo falla); EntregaDAO y PedidoDAO ganaron sobrecargas de create()/actualizarEstado() que reciben esa conexión en vez de abrir la suya propia. (2) Credenciales externalizadas: ConexionBD ya no tiene la URL, el usuario y la contraseña escritos directamente en el código; ahora los busca primero en variables de entorno (DB_URL, DB_USER, DB_PASSWORD), después en un archivo src/main/resources/db.properties (listado en .gitignore, nunca se sube al repositorio; se incluye db.properties.example con el formato) y, si no encuentra ninguna de las dos, usa un valor por defecto solo para que el proyecto funcione sin configurar nada en un entorno de evaluación. (3) Hash de contraseñas: la tabla usuarios ya no guarda la contraseña en texto plano. ConexionBD y LoginView calculan el hash SHA-256 (cada una con su propio método privado, para no depender de un paquete nuevo) antes de guardar o comparar la contraseña; la columna password pasó de VARCHAR(50) a VARCHAR(255) para que quepa el hash de 64 caracteres. Además, se agregó un botón "Actualizar pedidos" en VentanaAsignarRepartidor, que antes solo cargaba los pedidos pendientes una vez al abrirse y no se enteraba de pedidos registrados después desde otra ventana.

1- Encapsulamiento de clases.

2- Herencia de clases.

3- Polimorfismo.

4- Uso de interfaces.

5- Separación de estructura.

6- Uso de clases Abstractas.

7- Uso de hilos (Threads) y ExecutorService.

8- Sincronización de recursos compartidos (synchronized, wait/notifyAll).

9- Interfaz gráfica de escritorio con Java Swing (JFrame, JTable, JComboBox, CardLayout).

10- Separación de responsabilidades por capas (vista, controlador, dao, modelo).

11- Conexión a base de datos relacional con JDBC (DriverManager, Connection).

12- Patrón DAO (PedidoDAO, RepartidorDAO, EntregaDAO).

13- Consultas seguras con PreparedStatement y lectura de resultados con ResultSet.

14- Manejo de excepciones y cierre de recursos (try-with-resources, DaoException).

15- Autenticación de usuarios contra la base de datos (LoginView).

16- Interfaz genérica con tipos parametrizados (CrudDAO&lt;T, ID&gt;).

17- CRUD completo (create, readAll, update, delete) sobre pedidos, repartidores y entregas.

18- Validación de entradas y retroalimentación de errores en la GUI (campos obligatorios, formato de fecha/hora, restricciones de llave foránea).

19- Transacciones JDBC (setAutoCommit, commit, rollback) para mantener la consistencia entre operaciones relacionadas.

20- Externalización de configuración sensible (variables de entorno / archivo de propiedades no versionado) en vez de credenciales escritas en el código.

21- Hash de contraseñas (SHA-256) en vez de texto plano.

Estructura del programa:

📁 raíz del proyecto
```
├── pom.xml                       # Dependencia: mysql-connector-j
├── README.md
└── src/main/
    ├── java/                       # Código fuente (detalle más abajo)
    └── resources/
        ├── speedfast_db.sql        # Script DDL que crea la base de datos y sus tablas
        ├── db.properties.example   # Formato de configuración de conexión (copiar a db.properties y completar)
        └── db.properties           # Credenciales locales — NO se versiona (está en .gitignore)
```

📁 src/main/java/
```
├── app/                          # Puntos de entrada
│   ├── Main.java                   # Demo de consola de semanas anteriores
│   └── MainSwing.java              # Punto de entrada de la GUI: LoginView -> VentanaPrincipal

├── conexion/                     # Conexión con MySQL
│   ├── ConexionBD.java             # DriverManager + creación de la base de datos y las tablas; lee la configuración (env vars / db.properties / valores por defecto) y hashea la contraseña del admin inicial
│   └── ProbarConexion.java         # Clase de prueba para verificar la conexión

├── modelo/                       # Clases de dominio
│   ├── Pedido.java                 (abstract)
│   ├── Repartidor.java             (Mostrable, Runnable; ahora con id de la base de datos)
│   ├── ServicioComida.java
│   ├── ServicioComprasExpress.java
│   ├── ServicioEncomiendas.java
│   ├── PedidoSync.java             # Pedido preparado para trabajar con hilos
│   ├── Entrega.java                # Relación pedido - repartidor (tabla entrega)
│   ├── PedidoRegistro.java         # Fila de la tabla pedido (id, direccion, tipo, estado)
│   ├── EstadoPedido.java           # Enum: PENDIENTE, EN_PREPARACION, EN_REPARTO, ENTREGADO, CANCELADO
│   └── PrioridadPedido.java        # Enum: NORMAL, ALTA

├── interfaces/                   # Interfaces y contratos
│   ├── Mostrable.java
│   ├── Despachable.java            # asignarRepartidor() + setRepartidor()
│   ├── Cancelable.java
│   └── Rastreable.java

├── gestores/                     # Concurrencia
│   ├── PrepararPedido.java         # Runnable: simula la preparación de un pedido
│   └── ZonaDeCarga.java            # Recurso compartido (synchronized) entre repartidores

├── dao/                          # Acceso a la base de datos (JDBC)
│   ├── CrudDAO.java                # Interfaz genérica: create(), readAll(), update(), delete()
│   ├── PedidoDAO.java               implements CrudDAO<PedidoRegistro, Integer>; además filtrar(estado, tipo), actualizarEstado() (con sobrecarga transaccional que recibe una Connection)
│   ├── RepartidorDAO.java           implements CrudDAO<Repartidor, Integer>
│   ├── EntregaDAO.java              implements CrudDAO<Entrega, Integer>; además listarPorPedido(), listarPorRepartidor(), create() con sobrecarga transaccional
│   └── DaoException.java           # Error de acceso a datos (RuntimeException)

├── data/                         # Datos en memoria de la semana 6
│   └── PedidoData.java             # Lista estática de pedidos (ya no la usa la GUI)

├── controlador/                  # Lógica entre la vista y los DAO
│   ├── PedidoControlador.java
│   ├── RepartidorControlador.java
│   └── EntregaControlador.java     # CRUD manual de entregas (lo usa VentanaGestionEntregas)

└── vista/                        # Interfaz gráfica (Swing)
    ├── LoginView.java              # Inicio de sesión (correo y contraseña hasheada contra la tabla usuarios)
    ├── VentanaPrincipal.java       # Ventana principal: 5 botones de navegación
    ├── VentanaRegistroPedido.java  # Formulario de registro (Cliente, Dirección, Tipo, ...)
    ├── VentanaRegistroRepartidor.java # Registrar, editar y eliminar repartidores (tabla seleccionable)
    ├── VentanaListaPedidos.java    # Listado de pedidos con filtros por tipo/estado, editar y eliminar
    ├── VentanaAsignarRepartidor.java # Flujo de negocio: asigna repartidor (transacción JDBC), simula el viaje en un hilo aparte; botón "Actualizar pedidos" para refrescar los combos
    └── VentanaGestionEntregas.java # CRUD manual de entregas: crear, listar, editar y eliminar (combos Pedido/Repartidor)
```

🗄️ Sobre la base de datos (MySQL):

La base de datos se llama speedfast_db y tiene tres tablas principales, además de usuarios (para el login) y funcionarios:

- repartidor (id, nombre): un repartidor puede realizar muchas entregas.
- pedido (id, direccion, tipo, estado): tipo puede ser COMIDA, ENCOMIENDA o EXPRESS; estado puede ser PENDIENTE, EN_REPARTO o ENTREGADO.
- entrega (id, id_pedido, id_repartidor, fecha, hora): cada entrega se asocia a un pedido y a un repartidor mediante claves foráneas.

Solo se guardan las columnas anteriores: datos como el nombre del cliente o la distancia se usan al armar el pedido en el formulario, pero no forman parte del modelo relacional de esta actividad.

🖥️ Sobre la interfaz gráfica (Swing):

Al iniciar, LoginView pide el correo y la contraseña y los valida contra la tabla usuarios de la base de datos; si son correctos, abre VentanaPrincipal, que es el punto de partida desde donde se abren las otras cinco ventanas. VentanaRegistroPedido arma dinámicamente sus campos según el Tipo de pedido elegido (Comida / Encomienda / Compra Express) usando CardLayout, construye la subclase de Pedido correspondiente y la guarda en la base de datos, que le asigna el ID. VentanaRegistroRepartidor guarda nuevos repartidores y muestra en una tabla todos los que ya están registrados; al seleccionar una fila, sus datos se cargan en el formulario para editarlos o eliminarlos. VentanaListaPedidos muestra en una JTable los pedidos guardados en la base de datos, se puede filtrar por tipo y/o estado, y el pedido seleccionado se puede editar (dirección, tipo, estado) o eliminar. VentanaAsignarRepartidor permite elegir un pedido pendiente y un repartidor (ambos leídos de la base de datos), registra la entrega, cambia el estado del pedido a EN_REPARTO y simula el viaje de entrega en un hilo aparte para no congelar la ventana; cuando termina, deja el pedido como ENTREGADO en la base de datos y actualiza la interfaz de forma segura con SwingUtilities.invokeLater(). VentanaGestionEntregas es el CRUD manual de entregas: con dos JComboBox (que muestran un texto legible pero guardan el id real de cada Pedido y Repartidor) y campos de fecha/hora, permite crear, listar, editar y eliminar entregas directamente, sin pasar por la simulación de VentanaAsignarRepartidor. Todas las ventanas se comunican con la base de datos a través de los controladores (PedidoControlador, RepartidorControlador y EntregaControlador), que a su vez usan los DAO.

⚙️ Instrucciones para clonar y ejecutar el proyecto

1. Clona el repositorio desde GitHub.

2. Abre el proyecto en IntelliJ IDEA (el pom.xml descarga automáticamente el conector mysql-connector-j).

3. Ten un servidor MySQL en ejecución en localhost:3306. Configura tus credenciales por una de estas dos vías (si no configuras ninguna, ConexionBD usa un valor por defecto para que el proyecto funcione igual): (a) copia src/main/resources/db.properties.example a db.properties (en la misma carpeta) y completa db.url, db.user y db.password con los tuyos — ese archivo no se sube al repositorio; o (b) define las variables de entorno DB_URL, DB_USER y DB_PASSWORD (por ejemplo, en IntelliJ: Run → Edit Configurations → Environment variables), que tienen prioridad sobre el archivo.

4. Crea la base de datos: puedes ejecutar el script src/main/resources/speedfast_db.sql en MySQL Workbench o DBeaver, o dejar que ConexionBD cree la base de datos y las tablas automáticamente la primera vez que se conecta.

5. Verifica la conexión ejecutando ProbarConexion.java desde el paquete conexion: debe imprimir "Conectado a: speedfast_db".

6. Para la demo de consola: ejecuta Main.java desde el paquete app.

7. Para la interfaz gráfica: ejecuta MainSwing.java desde el paquete app e inicia sesión con el usuario administrador (correo: admin@spf.cl, contraseña: admin123). Desde VentanaPrincipal puedes registrar pedidos y repartidores, listarlos con filtros, asignar repartidores (flujo de negocio) y gestionar entregas (CRUD manual).

8. Sigue las instrucciones en pantalla (o en consola, según el punto de entrada elegido).


Fecha de entrega: 05-10-2026