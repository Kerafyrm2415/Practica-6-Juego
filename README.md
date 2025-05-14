# Práctica #6 – Herencia y Persistencia

# OBJETIVO (COMPETENCIA)
Analizar, diseñar e desarrollar una aplicación que emplee la herencia entre clases, para la
representación y manipulación eficiente de los objetos, así como incorporar persistencia de datos
utilizando archivos, con disposición para el análisis y resolución creativa de problemas.
# Introducción
Los videojuegos 2D de plataformas permiten aplicar múltiples conceptos fundamentales de la
programación orientada a objetos, especialmente herencia y polimorfismo. También son un
contexto práctico para aprender sobre la detección de colisiones, la renderización con la
clase Graphics, y el uso de archivos para guardar y cargar el estado del juego, como
puntuación, nivel alcanzado o personajes desbloqueados.
En esta práctica, el estudiante creará un juego de plataforma 2D con temática libre, que
incluya al menos:
▪ Un personaje controlado por el usuario.
▪ Múltiples tipos de enemigos u obstáculos, usando herencia.
▪ Elementos interactivos como monedas o ítems recolectables.
▪ Detección de colisiones entre elementos.
▪ Interfaz gráfica básica renderizada con Graphics.
▪ Mecanismo de guardado/carga de progreso a un archivo.
El propósito es aplicar la herencia para modelar personajes u objetos del juego, usar la clase
Graphics para renderizar elementos en pantalla, implementar detección de colisiones e
incorporar persistencia de datos mediante archivos (FileWriter, FileReader, BufferedReader,
etc.).

# Desarrollo
# Temática del Juego
El estudiante deberá elegir una temática libre para su juego (fantasía, ciencia ficción, mundo
subterráneo, etc.) y diseñar:
▪ Un personaje principal.
▪ Al menos dos tipos diferentes de enemigos u obstáculos que hereden de una clase
base.
▪ Ítems recolectables (monedas, llaves, etc.).

# Requerimientos técnicos
🔸 Herencia
Usar una jerarquía de clases como:
▪ Entidad (abstracta): con posición, tamaño, método draw(Graphics g)
▪ Jugador, Enemigo heredan de Entidad
▪ EnemigoVolador, EnemigoTerrestre heredan de Enemigo y tienen comportamiento
específico
🔸 Colisiones
▪ Implementar detección de colisiones usando Rectangle o verificación de coordenadas,
para ello se pueden apoyar de funciones como intersects e intersection, u otras
funciones relacionadas como contains o union.
▪ Las colisiones deben provocar reacciones (por ejemplo: daño, recolección de objetos,
rebote).
🔸 Gráficos (Graphics)
▪ Dibujar todos los elementos con métodos como drawRect, fillRect, drawImage, etc.
▪ Dibujar la interfaz (barra de salud, puntaje).
🔸 Persistencia
▪ Guardar el estado del juego (nombre del jugador, puntaje, nivel) en un archivo plano
(progreso.txt).
▪ Al iniciar, permitir cargar el archivo para continuar el juego.

# Consideraciones basicas
▪ El juego debe tener una ventana de al menos 800x600 píxeles.
▪ El jugador debe poder moverse lateralmente y saltar.
▪ El juego debe tener suelo y al menos dos plataformas elevadas.
▪ Deben existir colisiones visibles (por ejemplo, que eviten caer al vacío).
▪ Al menos un botón en el teclado debe guardar el progreso actual.
▪ Mostrar mensaje de "¡Has perdido!" si el jugador colisiona con un enemigo.
# Extensiones opcionales
▪ Agregar música o efectos de sonido
▪ Menú inicial con opciones de "Nuevo Juego" / "Cargar"
▪ Enemigos con movimiento autónomo (uso de hilos)
▪ Animaciones simples con sprites
# Entregables
▪ Código fuente completo (carpeta src/)
▪ Archivo de progreso (datos/progreso.txt)
▪ Archivo README.txt que describa el juego, controles y cómo ejecutar
▪ Reporte de practica con capturas de pantalla y video del juego en ejecución
▪ Repositorio en GitHub
