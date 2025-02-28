# Spyro The Dragon Tour

##  Introducción
En esta actividad hemos actualizado una aplicación inspirada en el universo de **Spyro the Dragon** para hacerla más atractiva y fácil de usar. Para lograrlo, hemos desarrollado una **Guía de inicio interactiva** y hemos incorporado un **Easter Egg** oculto.

Esta aplicación guía a los usuarios a través de los diferentes apartados del juego, mostrando personajes, mundos y coleccionables de una forma interactiva.

## ✨ Características Principales
- 🔹 **Guía de inicio interactiva**: Tutorial animado para aprender a usar la aplicación.
- 🔹 **Exploración de Personajes**: Conoce a los personajes del universo de Spyro.
- 🔹 **Exploración de Mundos**: Descubre los escenarios donde se desarrolla la aventura.
- 🔹 **Coleccionables**: Consulta los objetos que puedes recolectar en el juego.
- 🔹 **Easter Egg**: Contenido especial desbloqueable dentro de la app.
- 🔹 **Persistencia de Datos**: El tour no se repite si ya ha sido completado.
- 🔹 **Transiciones y Animaciones**: Movimiento fluido entre secciones.
- 🔹 **Uso de Sonidos**: Reacciones auditivas a eventos de la aplicación.

## 🛠️ Tecnologías Utilizadas
Esta aplicación ha sido desarrollada con las siguientes tecnologías y herramientas:
- **Android Studio** - Entorno de desarrollo utilizado.
- **Java** - Lenguaje principal de desarrollo.
- **SharedPreferences** - Para almacenar el estado del tour y configuraciones del usuario.
- **FragmentManager** - Para la gestión de la navegación entre fragmentos.
- **Animaciones XML y `ObjectAnimator`** - Para transiciones fluidas entre pantallas.
- **MediaPlayer** - Para la reproducción de efectos de sonido.

## 🚀 Instrucciones de Uso
Para ejecutar esta aplicación en tu entorno de desarrollo, sigue estos pasos:

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/descolar/spyro_tour.git
   ```
2. **Abrir el proyecto en Android Studio**.
3. **Instalar dependencias**:
   - Asegúrate de que las dependencias en `build.gradle` están actualizadas.
4. **Ejecutar la aplicación** en un emulador o dispositivo físico.

---

## 🎭 **Guía de Inicio Interactiva**
La guía de inicio es un tutorial animado que explica al usuario cómo utilizar la aplicación.
- Presenta la aplicación con una animación de Spyro.
- Botón de **"Comenzar"** que inicia la guía.
![Home](https://github.com/user-attachments/assets/d815dd9c-4cc9-4756-b15d-45c1956e14fc)

### 🎭 **Guía de Personajes**
- Explora todos los personajes disponibles en el universo de Spyro.
- Al presionar **"Saltar"**, se muestra la imagen y el nombre de cada personaje.
- Al presionar **"Siguiente"**, se avanza al siguiente apartado de la guía.
![personaje](https://github.com/user-attachments/assets/97fe96a4-6f23-4f15-baf1-d1dae1dec2fc)

### 🌍 **Guía de los Mundos**
- Muestra los diferentes mundos del universo de Spyro.
- Al presionar **"Saltar"**, se visualiza el nombre y la imagen de cada mundo.
- Al presionar **"Siguiente"**, se avanza al siguiente apartado.
![mundos](https://github.com/user-attachments/assets/c651fab0-f868-4657-aa18-74d155e0ed3d)

### 🏆 **Guía de los Coleccionables**
- Presenta los coleccionables del juego y su función.
- Funciona de manera similar a los apartados anteriores.
![Coleccionables](https://github.com/user-attachments/assets/86474f8e-8dac-4262-8250-cd36d8b9f1bb)

### 📝 **Guía Resumen**
- Enumera los pasos completados por el usuario.
- Muestra un botón para comenzar a usar la aplicación sin más interrupciones.
![resumen](https://github.com/user-attachments/assets/ded0ebac-d936-4633-8c55-3acefb318964)

---

## 🎯 Conclusiones del Desarrollador

Durante el desarrollo de esta aplicación, hemos aprendido sobre la importancia de:

- **La gestión de animaciones y transiciones** para mejorar la experiencia de usuario.
- **El uso de `SharedPreferences`** para mantener el estado del tutorial y evitar que se repita innecesariamente.
- **La optimización del rendimiento** al manejar múltiples fragmentos y efectos visuales.

El mayor desafío ha sido evitar parpadeos en las transiciones entre fragmentos y garantizar que las animaciones sean fluidas en todos los dispositivos.

Esta unidad ha sido un reto para mí, ya que el manejo de **Fragments** me ha resultado un poco complicado. Sin embargo, he trabajado duro para asegurarme de que todo funcione correctamente y que la aplicación sea lo más fluida posible.

Aunque hay cosas que me gustaría mejorar aún más, he decidido dejarlas así para no arriesgarme a romper lo que ya está funcionando correctamente. Aun así, espero que se note el esfuerzo y el trabajo que he puesto en esta actividad.


---
