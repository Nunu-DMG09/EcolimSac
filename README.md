# EcolimSac — Sistema de Gestión de Residuos Industriales

[![Estado](https://img.shields.io/badge/Estado-En%20Desarrollo-yellow)]()
[![Plataforma](https://img.shields.io/badge/Plataforma-Android-brightgreen)]()
[![Lenguaje](https://img.shields.io/badge/Lenguaje-Java-blue)]()
[![Framework](https://img.shields.io/badge/Framework-Android%20SDK-orange)]()

## Descripción

**EcolimSac** es una aplicación móvil diseñada para optimizar la recolección y manejo de residuos generados en procesos de limpieza industrial. Su objetivo principal es facilitar el trabajo del personal operativo y administrativo, promoviendo prácticas seguras, cumplimiento de normativas ambientales y eficiencia en los procesos.

## Características Principales

- **Gestión de Usuarios**: Autenticación simple con persistencia de datos en `SharedPreferences`.
- **Navegación Intuitiva**: Barra de navegación inferior (`BottomNavigationView`) con acceso rápido a las secciones principales.
- **Dashboard Interactivo**: Resumen de indicadores clave y métricas operativas.
- **Registro de Actividades**: Formularios para registrar actividades de limpieza y manejo de residuos.
- **Reportes Automatizados**: Generación y visualización de informes detallados.
- **Herramientas Prácticas**: Calculadora técnica para cálculos de volúmenes y cargas.
- **Cierre de Sesión Seguro**: Limpieza de datos de usuario y redirección a la pantalla de inicio de sesión.

## Arquitectura del Proyecto

El proyecto sigue una estructura modular para facilitar el mantenimiento y escalabilidad:

```
EcolimSac/
├── java/com/example/ecolimsac/
│   ├── activities/
│   │   ├── MainActivity.java
│   │   ├── LoginActivity.java
│   ├── fragments/
│   │   ├── DashboardFragment.java
│   │   ├── RegistroFragment.java
│   │   ├── ReportesFragment.java
│   │   ├── UsuarioFragment.java
│   │   ├── CalculadoraFragment.java
├── res/
│   ├── layout/            # Diseños XML (activity_main.xml, fragment layouts)
│   ├── menu/              # Menús (action_logout)
│   ├── values/            # Strings, colores y estilos
```

## Instalación y Configuración

### Requisitos
- **Android Studio** (última versión recomendada)
- **JDK 11+**
- **Android SDK** (API mínima según configuración del proyecto)

### Pasos de Instalación
1. **Clonar el repositorio**:
   ```bash
   git clone <url-del-repositorio>
   ```
2. **Abrir el proyecto**:
   - En Android Studio, selecciona `File > Open` y elige la carpeta del proyecto.
3. **Sincronizar dependencias**:
   - Espera a que Gradle descargue las dependencias necesarias.
4. **Ejecutar la aplicación**:
   - Configura un emulador o conecta un dispositivo físico y ejecuta el proyecto.

## Comportamiento Clave

- **Inicio de Sesión**:
  - El nombre de usuario se guarda en `SharedPreferences` bajo la clave `usuario`.
  - Ejemplo:
    ```java
    SharedPreferences.edit().putString("usuario", "Nombre").apply();
    ```
- **Navegación**:
  - La barra inferior permite cambiar entre fragments, reemplazando dinámicamente el contenedor `R.id.fragment_container`.
  - El fragment predeterminado es el Dashboard.
- **Cierre de Sesión**:
  - Limpia las preferencias y redirige a la pantalla de inicio de sesión:
    ```java
    SharedPreferences.edit().clear().apply();
    startActivity(new Intent(this, LoginActivity.class));
    finish();
    ```

## Buenas Prácticas y Mejoras Futuras

- **Validación de Datos**:
  - Asegurar que los datos ingresados en los formularios sean válidos y estén sanitizados.
- **Conexión a Backend**:
  - Implementar una API REST para centralizar datos y reportes.
- **Seguridad**:
  - Cifrar datos sensibles utilizando el Android Keystore.
- **Exportación de Reportes**:
  - Añadir funcionalidad para exportar reportes en formatos como PDF o CSV.
- **Roles y Permisos**:
  - Diferenciar entre operadores y supervisores para un mejor control de acceso.

## Diseño y Accesibilidad

- **Material Design**:
  - La interfaz sigue las pautas de diseño de Google para garantizar una experiencia moderna y consistente.
- **Optimización para Dispositivos**:
  - Adaptado para móviles y tablets.
- **Accesibilidad**:
  - Colores y tipografías legibles, con soporte para usuarios con discapacidades visuales.

## 📝 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 👨‍💻 Autor

**L. David Mesta**
- Email: davidmesta09@gmail.com


---

⭐ **¡Dale una estrella si te gusta este proyecto!** ⭐