👤 Autor del proyecto:

Carlos Estivens Carmona Barreto

carcarmona-prog

Carrera:

Analista Programador Computacional.

Instituto: Duoc uc.

📘 Descripción general del sistema SpeedFastApp:

Diseñamos un programa para la gestión de pedidos de la empresa SpeedFastApp, implementando técnicas que mejoren el manejo interno de los distintos tipos de servicios de entrega (comida, compras express y encomiendas), siempre pensando en el crecimiento del sistema. Creamos una base sólida implementando distintas técnicas de POO, para lograr un programa con estructura modular, demostrando el buen uso de la implementación que presta dicha forma de programar.

*Actualización semana 2: Esta semana implementamos métodos abstractos con funciones específicas dentro del programa que funciona como estructura util para el cálculo de los tiempos estimados de entrega usando clases abstractas. 

*Actualización semana 3: En la semana 3 implementamos interfaces con la finalidad de crear nuevos contratos de comportamiento usando una estructura aislada de la lógica y asi demostrar la modularidad del diseño orientado a objetos.

1- Encapsulamiento de clases.

2- Herencia de clases.

3- Polimorfismo.

4- Uso de interfaces.

5- Separación de estructura.

6- Uso de clases Abstractas.

Estructura del programa:

📁 src/
├── app/                      # Clase con método main
│   └── Main.java

├── modelo/                   # Clases de dominio
│   ├── Pedido.java  (abstract)
│   ├── Repartidor.java
│   ├── ServicioComida.java
│   ├── ServicioComprasExpress.java
│   └── ServicioEncomiendas.java

├── interfaces/                # Interfaces y contratos
│   ├── Mostrable.java
│   ├── Despachable.java
│   ├── Cancelable.java
│   ├── Rastreable.java
│


⚙️ Instrucciones para clonar y ejecutar el proyecto

Clona el repositorio desde GitHub.

Abre el proyecto en IntelliJ IDEA.

Ejecuta el archivo Main.java desde el paquete app.

Sigue las instrucciones en consola.

Fecha de entrega: 07/08/2026