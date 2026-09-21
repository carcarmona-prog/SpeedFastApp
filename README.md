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

1- Encapsulamiento de clases.

2- Herencia de clases.

3- Polimorfismo.

4- Uso de interfaces.

5- Separación de estructura.

6- Uso de clases Abstractas.

7- Uso de hilos (Threads) y ExecutorService.

8- Sincronización de recursos compartidos (synchronized, wait/notifyAll).

9- Interfaz gráfica de escritorio con Java Swing (JFrame, JTable, JComboBox, CardLayout).

10- Separación de responsabilidades por capas (vista, controlador, data, modelo).

Estructura del programa:

📁 src/main/java/
├── app/                        # Demo de consola de semanas anteriores
│   └── Main.java

├── main/                       # Punto de entrada de la GUI
│   └── Main.java                # new VentanaPrincipal()

├── modelo/                     # Clases de dominio
│   ├── Pedido.java               (abstract)
│   ├── Repartidor.java           (Mostrable, Runnable)
│   ├── ServicioComida.java
│   ├── ServicioComprasExpress.java
│   ├── ServicioEncomiendas.java
│   ├── PedidoSync.java           # Pedido preparado para trabajar con hilos
│   ├── EstadoPedido.java         # Enum: PENDIENTE, EN_PREPARACION, EN_REPARTO, ENTREGADO, CANCELADO
│   └── PrioridadPedido.java      # Enum: NORMAL, ALTA

├── interfaces/                 # Interfaces y contratos
│   ├── Mostrable.java
│   ├── Despachable.java          # asignarRepartidor() + setRepartidor()
│   ├── Cancelable.java
│   └── Rastreable.java

├── gestores/                   # Concurrencia
│   ├── PrepararPedido.java       # Runnable: simula la preparación de un pedido
│   └── ZonaDeCarga.java          # Recurso compartido (synchronized) entre repartidores

├── data/                        # Datos compartidos en memoria
│   └── PedidoData.java           # Lista estática de pedidos registrados

├── controlador/                 # Lógica entre la vista y los datos
│   └── PedidoControlador.java

└── vista/                       # Interfaz gráfica (Swing)
├── VentanaPrincipal.java     # Ventana principal: 3 botones de navegación
├── VentanaRegistroPedido.java# Formulario de registro (ID, Cliente, Dirección, Tipo, ...)
├── VentanaListaPedidos.java  # Tabla de pedidos (JTable + DefaultTableModel)
└── VentanaAsignarRepartidor.java # Asigna repartidor y simula la entrega en un hilo aparte

🖥️ Sobre la interfaz gráfica (Swing):

VentanaPrincipal es el punto de partida: desde ahí se abren las otras tres ventanas. VentanaRegistroPedido arma dinámicamente sus campos según el Tipo de pedido elegido (Comida / Encomienda / Compra Express) usando CardLayout, y construye la subclase de Pedido correspondiente. VentanaListaPedidos muestra todos los pedidos registrados en una tabla que se puede refrescar en cualquier momento. VentanaAsignarRepartidor crea un Repartidor real, se lo asigna a un pedido (Despachable.setRepartidor()), cambia su estado a EN_REPARTO, y simula el viaje de entrega en un hilo aparte para no congelar la ventana; cuando termina, actualiza la interfaz de forma segura con SwingUtilities.invokeLater(). Todas las ventanas comparten los mismos datos a través de PedidoControlador, que a su vez lee y escribe la lista estática PedidoData.pedidos.

⚙️ Instrucciones para clonar y ejecutar el proyecto

Clona el repositorio desde GitHub.

Abre el proyecto en IntelliJ IDEA.

Para la demo de consola: ejecuta Main.java desde el paquete app.

Para la interfaz gráfica: ejecuta Main.java desde el paquete main.

Sigue las instrucciones en pantalla (o en consola, según el punto de entrada elegido).

Fecha de entrega: 21/09/2026