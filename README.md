


# LibGDX Minijuegos

Un proyecto de múltiples minijuegos desarrollado con LibGDX que incluye funcionalidades multijugador online y un editor de mapas integrado.

## 🎮 Características Principales

### Minijuegos Incluidos
- **Nivel principal**: Juego de medusas con movimiento fluido
- **Nivel easter egg (Descubre tu mismo como acceder 😎)**: Juego de coches con mecánicas de combate

### Multijugador Online
El proyecto incluye un sistema completo de multijugador peer-to-peer con servidor dedicado que permite:
- Conexión automática entre jugadores
- Sincronización de posiciones y estados
- Gestión de sesiones de juego

### Editor de Mapas
Herramienta integrada para crear y editar mapas de juego que permite:
- Edición visual de tiles
- Controles de cámara (WASD)
- Guardado automático en formato de texto

## 🚀 Tecnologías Utilizadas

- **LibGDX** v1.9.10 - Framework principal de desarrollo
- **Box2D** - Motor de física
- **Ashley ECS** - Sistema de componentes y entidades
- **Box2DLights** - Sistema de iluminación
- **Node.js + Socket.io** - Servidor multijugador

## 📱 Plataformas Soportadas

- **Desktop** (Windows, Mac, Linux)
- **Android**

## 🛠️ Instalación y Configuración

### Requisitos Previos
- Java 8 o superior
- Node.js (para el servidor multijugador)
- Android SDK (para compilación Android)

### Configuración del Servidor
1. Navegar a la carpeta `GameServer`
2. Instalar dependencias: `npm install express socket.io`
3. Ejecutar el servidor: `node app.js`

### Compilación del Juego
```bash
# Para Desktop
./gradlew desktop:run

# Para Android
./gradlew android:assembleDebug
```

## 🎯 Características del Gameplay

### Sistema de Power-ups
Los juegos incluyen diferentes potenciadores que modifican las capacidades del jugador

### Cambio Dinámico de Niveles
Los jugadores pueden alternar entre diferentes minijuegos durante la partida

### Controles Adaptativos
El sistema de entrada se adapta automáticamente según el nivel activo

## 🔧 Arquitectura del Proyecto

```
core/
├── src/com/mygdx/game/
│   ├── Logic/              # Lógica principal del juego
│   │   ├── GameWorld/      # Gestión de niveles y mundo
│   │   ├── GameObjects/    # Entidades del juego
│   │   ├── Input/          # Sistema de entrada
│   │   ├── Physics/        # Motor de física
│   │   └── Online/         # Funcionalidades multijugador
│   ├── Utils/              # Utilidades y herramientas
│   └── GameConfig/         # Configuración del juego
desktop/                    # Launcher para escritorio
android/                    # Proyecto Android
GameServer/                 # Servidor Node.js
```

## 📝 Notas de Desarrollo

El proyecto utiliza un sistema de gestión de frames personalizado para mantener 60 FPS consistentes [18](#0-17)  y incluye manejo de errores para conexiones de red inestables.

---

**Nota**: Este es un proyecto de desarrollo activo. Para reportar bugs o contribuir, por favor consulta los archivos de código fuente para entender la estructura antes de realizar cambios.
