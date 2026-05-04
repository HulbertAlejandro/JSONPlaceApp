# JSONPlaceApp 🚀

Aplicación Android desarrollada en Kotlin siguiendo la arquitectura **Clean Architecture** y **MVVM**. El proyecto consume la API de [JSONPlaceholder](https://jsonplaceholder.typicode.com/) y cuenta con soporte Offline-First.

## 🛠 Tecnologías Utilizadas
* **Jetpack Compose** - UI Moderna.
* **Hilt** - Inyección de dependencias.
* **Retrofit & OkHttp** - Consumo de APIs REST.
* **Room** - Base de datos local (Caché).
* **Paging 3** - Paginación de datos.
* **KSP** - Procesamiento de anotaciones eficiente.

## 🏗 Arquitectura
El proyecto está estructurado en capas:
1. **Data:** DTOs, DAOs, Database, Mappers y Repositorios.
2. **Domain:** Modelos de dominio y Casos de uso.
3. **DI:** Módulos de Hilt.

## 📝 Estado del Proyecto
Actualmente se ha completado la configuración base, la capa de dominio y la capa de persistencia local.