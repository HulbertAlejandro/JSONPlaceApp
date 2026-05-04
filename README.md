# 🚀 JSONPlaceApp - Android Clean Architecture

Aplicación Android desarrollada en Kotlin siguiendo la arquitectura **Clean Architecture** y **MVVM**. El proyecto consume la API de [JSONPlaceholder](https://jsonplaceholder.typicode.com/) con un enfoque **Offline-First**.

## 🛠 Tecnologías Utilizadas
* **Jetpack Compose** - Interfaz de usuario declarativa.
* **Hilt** - Inyección de dependencias.
* **Retrofit & OkHttp** - Consumo de APIs REST.
* **Room** - Base de datos local (Persistencia).
* **Paging 3 & RemoteMediator** - Paginación inteligente (API + DB).
* **NetworkMonitor** - Detección de conectividad en tiempo real mediante Flows.

## 🏗 Arquitectura
El proyecto sigue los principios de Clean Architecture:
1. **Data:** Implementación de repositorios, RemoteMediator, DAOs y API Service.
2. **Domain:** Modelos de dominio, interfaces de repositorios y estados de Resource.
3. **UI/Presentation:** ViewModels con manejo de estados complejos y buscador dual.
4. **DI:** Módulos de Hilt para proveer dependencias de red, base de datos y repositorios.

## 📝 Estado del Proyecto
✅ Configuración base y DI.
✅ Capa de Dominio y Datos (Offline-First completa).
✅ ViewModels con Paging 3 y filtros dinámicos.
⏳ Interfaz de Usuario final en Compose (En proceso).