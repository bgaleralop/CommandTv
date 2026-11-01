# 📱 AuraTV (AuraTV Bridge)

**AuraTV** es una aplicación moderna para Android diseñada como una navaja suiza de control, mantenimiento y optimización para Smart TVs y dispositivos multimedia (compatible con **Android TV / Chromecast** y **LG webOS**).

La aplicación permite ir mucho más allá de un mando a distancia tradicional, ofreciendo una consola de comandos directa, gestión avanzada de aplicaciones (*debloat* e instalación de APKs) y control remoto mediante flujos de red de bajo nivel.

---

## 🚀 Características Principales

* **🔍 Descubrimiento Inteligente de Dispositivos:**
    * Detección automática en la red local mediante **mDNS (NSD)** para mostrar nombres amigables de los televisores.
    * Sistema de *fallback* automático y escaneo profundo por fuerza bruta de puertos TCP (`5555`) en caso de restricciones en la red.
* **🔐 Autenticación Segura (ADB):**
    * Gestión nativa de claves criptográficas **RSA de 2048 bits** (`AdbKeyManager`) para superar los saludos de seguridad (*handshake* `CNXN`/`AUTH`) y evitar avisos recurrentes en pantalla.
* **🎮 Mando a Distancia Avanzado:**
    * Panel D-Pad direccional, control multimedia, accesos directos a aplicaciones favoritas y gestión de volumen mediante eventos de teclado (`input keyevent`).
* **📦 Gestor de Aplicaciones y Debloat:**
    * Listado completo de aplicaciones instaladas en la televisión diferenciando entre apps de usuario y de sistema.
    * Capacidad para congelar/deshabilitar servicios de telemetría y anuncios (`pm disable-user --user 0`) y la opción de instalar APKs externos de forma remota.
* **📺 Soporte Multi-plataforma:**
    * Integración en desarrollo tanto para el ecosistema Android TV (vía sockets TCP y ADB) como para televisores LG con **webOS** (vía WebSockets y protocolo SSAP).

---

## 🛠️ Arquitectura y Estado Técnico

El proyecto se está construyendo bajo estrictos estándares de **Clean Architecture** y principios de diseño modular:

* **Gestión de Conexiones (Fuente Única de Verdad):** El socket TCP activo y sus flujos de entrada/salida (`InputStream` / `OutputStream`) se centralizan de forma segura en un repositorio persistente (`AdbRepositoryImpl`), garantizando que la conexión no se pierda al navegar entre las diferentes pantallas de la aplicación.
* **Inyección de Dependencias Manual (Pure DI):** Se evita el uso de frameworks pesados mediante un contenedor global (`AppContainer`) que centraliza las dependencias, facilitando la escalabilidad y las pruebas unitarias.
* **Interfaz de Usuario Reactiva:** Desarrollada íntegramente con **Jetpack Compose** y Material 3, separando la lógica de estado (*StateFlow*) de los componentes visuales puros.

---

## 📂 Estructura del Proyecto

```text
app/
├── di/
│   └── AppContainer.kt          # Contenedor central de dependencias manuales
├── data/
│   ├── crypto/                  # Generación y firma RSA (AdbKeyManager)
│   ├── network/                 # Sockets TCP, tramas binarias y comandos ADB
│   └── repository/              # Implementación del repositorio de red
├── domain/
│   ├── model/                   # Modelos de datos (AdbDevice, AppPackage)
│   ├── repository/              # Interfaces y estados de conexión (ConnectionState)
│   └── usecase/                 # Casos de uso de negocio (Debloat, Remote, Discovery)
└── ui/
    ├── discovery/               # Pantalla de búsqueda automática y manual
    ├── remote/                  # Panel de control remoto y multimedia
    ├── apps/                    # Gestor de aplicaciones y debloat
    └── terminal/                # Consola de comandos shell y logs en vivo