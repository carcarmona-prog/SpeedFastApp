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

1- Encapsulamiento de clases.

2- Herencia de clases.

3- Polimorfismo.

4- Uso de interfaces.

5- Separación de estructura.

6- Uso de clases Abstractas.

7- Uso de hilos (Threads) y ExecutorService.

8- Sincronización de recursos compartidos (synchronized, wait/notifyAll).

Estructura del programa:

📁 src/
├── app/                       # Clase con método main
│   └── Main.java

├── modelo/                    # Clases de dominio
│   ├── Pedido.java              (abstract)
│   ├── Repartidor.java          (Mostrable, Runnable)
│   ├── ServicioComida.java
│   ├── ServicioComprasExpress.java
│   ├── ServicioEncomiendas.java
│   ├── PedidoSync.java          # Pedido preparado para trabajar con hilos
│   ├── EstadoPedido.java        # Enum: PENDIENTE, EN_PREPARACION, EN_REPARTO, ENTREGADO, CANCELADO
│   └── PrioridadPedido.java     # Enum: NORMAL, ALTA

├── interfaces/                # Interfaces y contratos
│   ├── Mostrable.java
│   ├── Despachable.java
│   ├── Cancelable.java
│   └── Rastreable.java

├── gestores/                     # Concurrencia
│   ├── PrepararPedido.java      # Runnable: simula la preparación de un pedido
│   └── ZonaDeCarga.java         # Recurso compartido (synchronized) entre repartidores

🧵 Sobre la concurrencia (ZonaDeCarga y Repartidor):

La ZonaDeCarga almacena los pedidos pendientes en una BlockingDeque<PedidoSync> y expone dos métodos synchronized: agregarPedido() y retirarPedido(). Al ser synchronized, solo un hilo a la vez puede ejecutar cualquiera de los dos métodos sobre la misma instancia, lo que garantiza que ningún pedido pueda ser retirado dos veces. Si la zona de carga está vacía, los repartidores quedan en espera (wait()) hasta que llegue un nuevo pedido o se cierre la zona (cerrarZona()).

Repartidor, además de sus datos habituales (nombre, vehículo, disponibilidad), puede ejecutarse como hilo: retira un pedido, cambia su estado a EN_REPARTO, simula la entrega con Thread.sleep() y finalmente lo marca como ENTREGADO. Esto se repite hasta que ya no quedan pedidos disponibles.

⚙️ Instrucciones para clonar y ejecutar el proyecto

Clona el repositorio desde GitHub.

Abre el proyecto en IntelliJ IDEA.

Ejecuta el archivo Main.java desde el paquete app.

Sigue las instrucciones en consola.

Fecha de entrega: 14/09/2026