# 🚀 JSONPlaceApp - Android Clean Architecture

Aplicación Android desarrollada en Kotlin siguiendo la arquitectura **Clean Architecture** y **MVVM**. El proyecto consume la API de [JSONPlaceholder](https://jsonplaceholder.typicode.com/) con un enfoque **Offline-First**, garantizando una experiencia de usuario fluida incluso sin conexión a internet.

## 🛠 Tecnologías Utilizadas
* **Jetpack Compose** - Interfaz de usuario moderna y declarativa.
* **Hilt** - Inyección de dependencias para desacoplamiento y testabilidad.
* **Retrofit & OkHttp** - Cliente HTTP para el consumo de APIs REST.
* **Room** - Persistencia local robusta con SQLite.
* **Paging 3 & RemoteMediator** - Gestión de paginación infinita sincronizando API y Base de Datos.
* **NetworkMonitor** - Monitoreo de conectividad en tiempo real mediante Kotlin Flows.
* **Navigation Compose** - Sistema de navegación basado en rutas.

## 🏗 Arquitectura
El proyecto se divide en capas siguiendo los principios de Clean Architecture:
1. **Data:** Implementación de repositorios, `RemoteMediator` para la lógica offline, DAOs, Entidades de Room y servicios de Retrofit.
2. **Domain:** Modelos de negocio puros, interfaces de repositorios y clases de utilidad como `Resource`.
3. **UI / Presentation:** Pantallas en Compose, ViewModels con `StateFlow` y gestión de estados de paginación.
4. **DI:** Módulos de Hilt para la provisión centralizada de dependencias.

## 📡 Endpoints Utilizados
Se consumieron los siguientes recursos de JSONPlaceholder:
* `GET /posts`: Lista paginada de publicaciones.
* `GET /posts/{id}`: Detalle de una publicación específica.
* `GET /posts/{id}/comments`: Lista de comentarios asociados a un post.
* `GET /users`: Información de usuarios para el filtro de autor.

## ✨ Funcionalidades Implementadas
* **Buscador Dual:** Filtrado combinado por título (texto libre) y por usuario (selector dinámico).
* **Scroll Infinito:** Carga bajo demanda mediante Paging 3 para optimizar recursos.
* **Modo Offline:** Acceso total a los datos previamente cargados gracias a la caché de Room.
* **Indicador de Red:** Notificación visual en tiempo real sobre el estado de la conexión (Online/Offline).
* **Navegación Detallada:** Paso seguro de parámetros para visualizar comentarios y contenido completo del post.

## 📝 Estado del Proyecto
✅ Configuración base y DI (Hilt). 
✅ Capa de Dominio y Datos (Offline-First mediante RemoteMediator). 
✅ ViewModels con Paging 3 y gestión de filtros dinámicos. 
✅ Interfaz de Usuario final con Jetpack Compose y Material Design 3. 
✅ Navegación y flujo de pantallas completo.