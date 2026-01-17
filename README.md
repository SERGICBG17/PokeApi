# PokeApi App - Android con Kotlin y Firebase

## Descripción del Proyecto
[cite_start]Este proyecto consiste en una aplicación móvil desarrollada en Android Studio utilizando Kotlin[cite: 3]. [cite_start]El objetivo principal es implementar un sistema de autenticación mediante Firebase y consumir datos en tiempo real desde una API pública gratuita (PokeAPI)[cite: 3].

[cite_start]La aplicación sigue las directrices de Material Design para garantizar una interfaz moderna y una navegación fluida mediante gestos[cite: 32, 33].

---

## Requerimientos Funcionales Implementados

### 1. Pantallas Obligatorias
* [cite_start]Inicio: Pantalla de presentación de la aplicación[cite: 11].
* [cite_start]Registro: Formulario para la creación de cuentas con Firebase Authentication[cite: 12].
* [cite_start]Login: Acceso de usuarios mediante validación de credenciales en Firebase[cite: 13].
* [cite_start]Datos: Sección para visualizar y actualizar la información del usuario registrado con persistencia en Firebase[cite: 14, 29].
* [cite_start]API: Pantalla que muestra información obtenida en tiempo real desde PokeAPI[cite: 15].

### 2. Autenticación y Gestión de Usuario
* [cite_start]Implementación de registro y login con Firebase Authentication[cite: 18].
* [cite_start]Validación de credenciales, incluyendo correo válido y longitud de contraseña[cite: 20].
* [cite_start]Retroalimentación visual en cada acción (confirmaciones y errores)[cite: 40].

### 3. Consumo de API y UX
* [cite_start]Consulta a PokeAPI con visualización de resultados en una interfaz organizada[cite: 23].
* [cite_start]Gestión de estados de carga mediante indicadores visuales (spinners) durante procesos de red o autenticación[cite: 39].
* [cite_start]Manejo de errores para situaciones sin conexión o datos vacíos[cite: 25].

---

## Tecnologías y Arquitectura

* [cite_start]Lenguaje: Kotlin[cite: 42].
* [cite_start]Arquitectura: Aplicación de buenas prácticas como la separación de capas y uso de ViewModel para la gestión de datos[cite: 43].
* [cite_start]Diseño: Interfaz basada en Material Design con validaciones claras en formularios[cite: 32, 35].

---

## Estructura del Repositorio

En cumplimiento con los criterios de evaluación de limpieza y orden:
* [cite_start]Control de versiones: Uso de Git con historial de commits descriptivos[cite: 77].
* [cite_start]Organización: Código estructurado, eliminando archivos innecesarios y duplicados[cite: 78].
* Seguridad: El archivo de configuración google-services.json se encuentra debidamente ignorado en el historial para proteger las credenciales del proyecto.

---

## Instrucciones de Instalación

1. Clonar el repositorio: git clone https://github.com/SERGICBG17/PokeApi.git
2. Abrir el proyecto en Android Studio.
3. Añadir el archivo google-services.json correspondiente en la carpeta app/.
4. Sincronizar el proyecto con Gradle y ejecutar en un dispositivo o emulador.

---
Proyecto desarrollado para el módulo de Desarrollo de Aplicaciones Multiplataforma (DAM).
