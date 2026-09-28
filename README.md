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

*Actualización semana 7: Conectamos la aplicación a una base de datos MySQL (speedfast_db) usando JDBC, de modo que los pedidos, repartidores y entregas se guardan y se consultan de forma persistente en vez de vivir en una lista en memoria. Se creó la clase ConexionBD (paquete conexion), que gestiona la conexión con DriverManager y crea la base de datos y sus tablas (pedido, repartidor, entrega, además de usuarios y funcionarios) si todavía no existen. Se implementó el patrón DAO con las clases PedidoDAO (guardar, listar, filtrar por estado y actualizar estado), RepartidorDAO (guardar y listarTodos) y EntregaDAO (guardar la relación entre un pedido y un repartidor), todas con PreparedStatement para evitar inyección SQL y con try-with-resources para cerrar siempre los recursos; los errores se informan con la excepción propia DaoException. La capa controlador pasó a hablar con los DAO (PedidoControlador y el nuevo RepartidorControlador), por lo que las ventanas siguieron sin conocer nada de SQL. En la interfaz, los formularios ahora registran pedidos y repartidores directamente en la base de datos (el ID de cada pedido lo genera MySQL con AUTO_INCREMENT), se agregó una ventana para registrar repartidores, el listado de pedidos se llena con una JTable desde la base y la asignación de repartidor registra la entrega y actualiza el estado del pedido (PENDIENTE → EN_REPARTO → ENTREGADO) en la base de datos. Además se incorporó una ventana de inicio de sesión (LoginView) que valida el correo y la contraseña contra la tabla usuarios antes de abrir la ventana principal.

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

Estructura del programa:

📁 raíz del proyecto
```
├── pom.xml                       # Dependencia: mysql-connector-j
├── README.md
├── sql/
│   └── speedfast_db.sql          # Script DDL que crea la base de datos y sus tablas
└── src/main/java/
```

📁 src/main/java/
```
├── app/                          # Puntos de entrada
│   ├── Main.java                   # Demo de consola de semanas anteriores
│   └── MainSwing.java              # Punto de entrada de la GUI: LoginView -> VentanaPrincipal

├── conexion/                     # Conexión con MySQL
│   ├── ConexionBD.java             # DriverManager + creación de la base de datos y las tablas
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
│   ├── PedidoDAO.java              # guardar(Pedido), listarTodos(), listarPorEstado(), actualizarEstado()
│   ├── RepartidorDAO.java          # guardar(Repartidor), listarTodos()
│   ├── EntregaDAO.java             # guardar(Entrega)
│   └── DaoException.java           # Error de acceso a datos (RuntimeException)

├── data/                         # Datos en memoria de la semana 6
│   └── PedidoData.java             # Lista estática de pedidos (ya no la usa la GUI)

├── controlador/                  # Lógica entre la vista y los DAO
│   ├── PedidoControlador.java
│   └── RepartidorControlador.java

└── vista/                        # Interfaz gráfica (Swing)
    ├── LoginView.java              # Inicio de sesión (correo y contraseña de la tabla usuarios)
    ├── VentanaPrincipal.java       # Ventana principal: 4 botones de navegación
    ├── VentanaRegistroPedido.java  # Formulario de registro (Cliente, Dirección, Tipo, ...)
    ├── VentanaRegistroRepartidor.java # Formulario de repartidores + tabla de repartidores
    ├── VentanaListaPedidos.java    # Tabla de pedidos guardados (JTable + DefaultTableModel)
    └── VentanaAsignarRepartidor.java # Asigna repartidor, registra la entrega y simula el viaje en un hilo aparte
```

🗄️ Sobre la base de datos (MySQL):

La base de datos se llama speedfast_db y tiene tres tablas principales, además de usuarios (para el login) y funcionarios:

- repartidor (id, nombre): un repartidor puede realizar muchas entregas.
- pedido (id, direccion, tipo, estado): tipo puede ser COMIDA, ENCOMIENDA o EXPRESS; estado puede ser PENDIENTE, EN_REPARTO o ENTREGADO.
- entrega (id, id_pedido, id_repartidor, fecha, hora): cada entrega se asocia a un pedido y a un repartidor mediante claves foráneas.

Solo se guardan las columnas anteriores: datos como el nombre del cliente o la distancia se usan al armar el pedido en el formulario, pero no forman parte del modelo relacional de esta actividad.

🖥️ Sobre la interfaz gráfica (Swing):

Al iniciar, LoginView pide el correo y la contraseña y los valida contra la tabla usuarios de la base de datos; si son correctos, abre VentanaPrincipal, que es el punto de partida desde donde se abren las otras cuatro ventanas. VentanaRegistroPedido arma dinámicamente sus campos según el Tipo de pedido elegido (Comida / Encomienda / Compra Express) usando CardLayout, construye la subclase de Pedido correspondiente y la guarda en la base de datos, que le asigna el ID. VentanaRegistroRepartidor guarda nuevos repartidores y muestra en una tabla todos los que ya están registrados. VentanaListaPedidos muestra en una JTable los pedidos guardados en la base de datos, y se puede refrescar en cualquier momento. VentanaAsignarRepartidor permite elegir un pedido pendiente y un repartidor (ambos leídos de la base de datos), registra la entrega, cambia el estado del pedido a EN_REPARTO y simula el viaje de entrega en un hilo aparte para no congelar la ventana; cuando termina, deja el pedido como ENTREGADO en la base de datos y actualiza la interfaz de forma segura con SwingUtilities.invokeLater(). Todas las ventanas se comunican con la base de datos a través de los controladores (PedidoControlador y RepartidorControlador), que a su vez usan los DAO.

⚙️ Instrucciones para clonar y ejecutar el proyecto

1. Clona el repositorio desde GitHub.

2. Abre el proyecto en IntelliJ IDEA (el pom.xml descarga automáticamente el conector mysql-connector-j).

3. Ten un servidor MySQL en ejecución en localhost:3306. En conexion/ConexionBD.java revisa que las constantes USER y PASSWORD correspondan a tu usuario de MySQL.

4. Crea la base de datos: puedes ejecutar el script sql/speedfast_db.sql en MySQL Workbench o DBeaver, o dejar que ConexionBD cree la base de datos y las tablas automáticamente la primera vez que se conecta.

5. Verifica la conexión ejecutando ProbarConexion.java desde el paquete conexion: debe imprimir "Conectado a: speedfast_db".

6. Para la demo de consola: ejecuta Main.java desde el paquete app.

7. Para la interfaz gráfica: ejecuta MainSwing.java desde el paquete app e inicia sesión con el usuario administrador (correo: admin@spf.cl, contraseña: admin123).

8. Sigue las instrucciones en pantalla (o en consola, según el punto de entrada elegido).


Fecha de entrega: 28/09/2026