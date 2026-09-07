👤 Autor del proyecto:
Carlos Estivens Carmona Barreto
carcarmona-prog
Carrera:
Analista Programador Computacional.
Instituto: Duoc UC.

📘 Descripción general del sistema SpeedFastApp:
Diseñamos un programa para la gestión de pedidos de la empresa SpeedFastApp, implementando técnicas que mejoren el manejo interno de los distintos tipos de servicios de entrega (comida, compras express y encomiendas), siempre pensando en el crecimiento del sistema. Creamos una base sólida implementando distintas técnicas de POO, para lograr un programa con estructura modular, demostrando el buen uso de la implementación que presta dicha forma de programar.

*Actualización semana 2: Esta semana implementamos métodos abstractos con funciones específicas dentro del programa que funciona como estructura útil para el cálculo de los tiempos estimados de entrega usando clases abstractas.

*Actualización semana 3: En la semana 3 implementamos interfaces con la finalidad de crear nuevos contratos de comportamiento usando una estructura aislada de la lógica y así demostrar la modularidad del diseño orientado a objetos.

*Actualización semana 4: Esta semana incorporamos el uso de Threads (hilos) para simular el procesamiento concurrente de pedidos. Se agregó la clase `PedidoSync`, una versión del pedido preparada para acceso concurrente (con el estado marcado como `volatile` y el método `setEstadoPedido` sincronizado con `synchronized` para evitar condiciones de carrera). El paquete nuevo `tareas` contiene `PrepararPedido`, que implementa `Runnable` y representa la preparación de un pedido dentro de su propio hilo: cambia el estado a `EN_PREPARACION`, simula el tiempo de preparación con `Thread.sleep()`, y al finalizar actualiza el estado a `LISTO` (o `CANCELADO` si el hilo es interrumpido). También se incorporaron los enums `EstadoPedido` (PENDIENTE, EN_PREPARACION, LISTO, CANCELADO) y `PrioridadPedido` (NORMAL, ALTA) para reemplazar el uso de strings sueltos por valores controlados y seguros.

1- Encapsulamiento de clases.
2- Herencia de clases.
3- Polimorfismo.
4- Uso de interfaces.
5- Separación de estructura.
6- Uso de clases Abstractas.
7- Uso de Threads y sincronización (concurrencia).
8- Uso de enumeraciones (enum).

Estructura del programa:
📁 src/main/java/
├── app/                       # Clase con método main
│   └── Main.java
├── modelo/                    # Clases de dominio
│   ├── Pedido.java             (abstract)
│   ├── PedidoSync.java         # Versión thread-safe del pedido
│   ├── EstadoPedido.java       # enum: estados del pedido
│   ├── PrioridadPedido.java    # enum: prioridades del pedido
│   ├── Repartidor.java
│   ├── ServicioComida.java
│   ├── ServicioComprasExpress.java
│   └── ServicioEncomiendas.java
├── interfaces/                 # Interfaces y contratos
│   ├── Mostrable.java
│   ├── Despachable.java
│   ├── Cancelable.java
│   └── Rastreable.java
├── tareas/                     # Tareas ejecutadas en hilos
│   └── PrepararPedido.java     # Runnable: simula la preparación concurrente de un pedido

⚙️ Instrucciones para clonar y ejecutar el proyecto
Clona el repositorio desde GitHub.
Abre el proyecto en IntelliJ IDEA.
Ejecuta el archivo Main.java desde el paquete app.
Sigue las instrucciones en consola.

Fecha de entrega: 31/08/2026
