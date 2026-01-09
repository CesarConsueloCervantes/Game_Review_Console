# Game_Review_Console
Aplicación android sobre reviews de juegos de consola

# Este repositorio contiene
- Aplicacion android
- API REST (Backend)

# Especificaciones
Aplicacion Android
    - Desarrollada en Android Studio
    - Lenguaje de programación: Kotlin
    - Consume datos desde la API REST
    - Almacenamiento local con SQLite
    - Manejo de sesión de usuario de forma local

API REST
    - Node.js / JavaScript (CommonJS)
    - Express para ruteo y middleware
    - MongoDB como base de datos
    - Mongoose para modelado de datos
    - Arquitectura modular
    - Utilidades internas para lógica de negocio

# Arquitectura general
- Separación clara entre cliente y servidor
- Comunicación mediante HTTP / JSON
- Backend orientado a API REST
- Persistencia de datos distribuida:
- SQLite (cliente)
- MongoDB (servidor)

# Tecnologías utilizadas
Frontend (Android)
    - Kotlin
    - Android Studio
    - SQLite

Backend
    - Node.js
    - Express
    - MongoDB
    - Mongoose

# Detalles y consideraciones importantes
- La aplicación y la API no se encuentran desplegadas en ningún entorno productivo.
- Actualmente, el backend solo funciona en entorno local (localhost).
- La aplicación Android está configurada para consumir la API únicamente desde localhost.
- No existe un dominio público ni servidor remoto configurado.
- El proyecto tiene fines académicos / de aprendizaje.

# Almacenamiento local (SQLite)
- La base de datos SQLite se utiliza únicamente para:
    - Guardar el nombre del usuario
- No se almacenan:
    - Contraseñas
    - Tokens
    - Información sensible
- El uso de SQLite es básico y demostrativo, enfocado en el manejo de sesión simple.