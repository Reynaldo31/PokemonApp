# Pokémon App

## Desarrollador

Reynaldo Jafet Giron Tercero
30/07/2025

Aplicación Android desarrollada en Android Studio(Kotlin) que muestra información sobre Pokémon de primera generación consumiendo la API de PokeAPI. La app incluye un listado con búsqueda y pantallas de detalle para cada Pokémon.

## Características

- Listado de los 151 Pokémon de primera generación
- Búsqueda por nombre o ID
- Vista detallada de cada Pokémon
- Diseño responsive
- Manejo de errores y estados de carga
- Recarga al deslizar hacia abajo (pull-to-refresh)
- Grid adaptable a diferentes tamaños de pantalla
- Estadísticas con gráficos
- Tipos con colores temáticos
- Características físicas (peso, altura)

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Arquitectura modular
- Clean Architecture
- Inyección de dependencias con Hilt
- Retrofit para consumo de API
- Coil para carga de imágenes
- Navigation Compose
- swiperefresh para actualizar datos

## Requisitos

- Android Studio Giraffe (2022.3.1) o superior
- Android SDK 37
- Java 11

## Configuración Inicial

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Reynaldo31/PokemonApp.git

2. Abre el proyecto en Android Studio: 
   Selecciona "Open an existing project"
   Navega hasta la carpeta del proyecto y selecciona el archivo settings.gradle

3. Sincroniza las dependencias de Gradle de cada modulo(app, core, data, domain, feature-pokemon):
   Haz clic en "Sync Project with Gradle Files" en la barra de herramientas

4. Ejecuta la aplicación:
   Selecciona un dispositivo o emulador
   Haz clic en el botón "Run" (Shift+F10)

## Estructura del proyecto
- app/
- ├── app/              # Módulo principal
- ├── core/             # Módulo común (constants, extensions, ui.componets, utils, theme)
- ├── data/             # Módulo de datos (api, di, dto, mapper, repository, responses)
- ├── domain/           # Módulo de dominio (model, repository, usecase)
- └── feature-pokemon/  # Módulo de características (UI, ViewModels)

## Git Flow

Ramas principales:
- main: Versiones estables (producción)
- develop: Integración continua
  
Ramas de trabajo

- Para nuevas características:
   - bash
   - git checkout -b feature/nombre-caracteristica develop

- Para releases:
   - bash
   - git checkout -b release/v1.0.0 develop

- Para hotfixes:
   - bash
   - git checkout -b hotfix/nombre-fix main

## Flujo de trabajo

1. Crear ramas para cada característica:
   -  ```bash
   - git checkout -b feature/pokemon-list
   - git checkout -b feature/pokemon-detail
    
2. Desarrolla y haz commits descriptivos en ingles:
  git commit -m "feat: Implement Pokemon list screen with search functionality"
  git commit -m "fix: Resolve image loading issue in Pokemon detail"
  
3. Crea un Pull Request a develop
   - git checkout develop
   - git merge --no-ff feature/pokemon-list
   
4. Crear ramas de release cuando esté listo para producción:
   - git checkout -b release/1.0.0
   
6. Para producción, crea rama de release y mergea a main
  - git checkout main
  - git merge --no-ff release/1.0.0
  - git tag -a v1.0.0 -m "Initial release"

## Arquitectura:

La aplicación sigue Clean Architecture con MVVM:
UI (Compose) → ViewModel → Use Cases ← Repositories ← Data Sources (API)

## Componentes clave:
- ViewModel: Maneja el estado de la UI
- UI: Manejo y diseñeo de los componentes de las pantallas(PokemonListScreen, PokemonDetailScreen)
- Repository: Única fuente de verdad para los datos
- Core: Componentes reutilizable
- Data: Conexión con PokeAPI, implementadores e injección de dependecias
- Navigation: Maneja la navegación de la app usando Jetpack Compose, permitiendo ir de la lista de Pokémon al detalle de uno específico usando rutas con argumentos.

## Dependencias Principales:

- Jetpack Compose
   - implementation('androidx.activity:activity-compose:1.8.0')
   - implementation("androidx.compose.ui:ui:1.5.0")
   - implementation("androidx.compose.material3:material3:1.10.0")

- Navigation
   - implementation("androidx.navigation:navigation-compose:2.7.7")

- Hilt (DI)
   - implementation("com.google.dagger:hilt-android:2.51.1")
   - kapt("com.google.dagger:hilt-compiler:2.0.21")

- Retrofit (API)
   - implementation("com.squareup.retrofit2:retrofit:2.9.0")
   - implementation("com.squareup.retrofit2:converter-gson:2.9.0")

- Coil (Imágenes)
   - implementation("io.coil-kt:coil-compose:2.7.0")

- Pull-to-refresh
   - implementation("com.google.accompanist:accompanist-swiperefresh:0.27.0")

## Licencia

Este proyecto está bajo la licencia MIT. Ver LICENSE para más detalles.

## Contribución

- Haz fork del proyecto
- Crea tu rama de feature (git checkout -b feature/amazing-feature)
- Haz commit de tus cambios (git commit -m 'Add some amazing feature')
- Haz push a la rama (git push origin feature/amazing-feature)
- Abre un Pull Request

## FAQ
- ¿Cómo cambio los colores de la app?
   Edita los colores en core/src/main/res/values/colors.xml

- ¿Cómo modifico la cantidad de Pokémon amostrar?
   Modifica el parámetro limit en PokemonRepositoryImpl.kt

- ¿Cómo cambio la API base?
   Edita PokeApiService.kt en el módulo data

