# 📝 Sistema de Evaluación y Simulador de Exámenes

**👥 Estudiantes:** Celimar Rojas

---

## 📋 Descripción del Proyecto
Software integral de evaluación educativa desarrollado en **Java** bajo el paradigma de **Programación Orientada a Objetos**. El sistema permite la gestión de bancos de preguntas, la simulación de pruebas en tiempo real con cronómetro y el análisis estadístico avanzado de resultados.

🎯 **Características Principales:**
- 📚 **Banco de Preguntas:** Organizado por temas y niveles de dificultad.
- 🎲 **Generación Aleatoria:** Exámenes únicos basados en filtros de configuración.
- ⏱️ **Simulación Real:** Validación de tiempo límite con cronómetro integrado.
- 📈 **Dashboard Visual:** Gráficos estadísticos dinámicos con **JFreeChart**.
- 📄 **Exportación PDF:** Reportes de resultados agrupados por usuario mediante **iText**.
- 💡 **Retroalimentación:** Explicaciones inmediatas por cada respuesta procesada.
- 💾 **Persistencia:** Almacenamiento histórico en archivos de texto (sin necesidad de DB externa).

---

## ⚙️ Requisitos del Sistema

### 💻 Software Necesario
- ☕ **Java JDK 11** o superior (Recomendado JDK 17+ para soporte de librerías)
- 🖥️ **Sistema operativo:** Windows, Linux o macOS
- 🔧 **IDE Utilizado:** NetBeans
- 📦 **Gestor de Dependencias:** Maven (para JFreeChart e iText)

---

## 🚀 Paso 1: Instalación y Ejecución

### 📥 Opción #1: Clonar el Repositorio en NetBeans
1. Abrir **NetBeans**
2. Ir al menú: **Team → Git → Clone...**
3. En la ventana emergente, ingresar:
   - 🌐 **Repository URL:** `https://github.com/CELIM709/GeneradorExamenes.git`
4. Click en **"Next"** ➡️
5. Seleccionar rama **"main"** 🌿
6. Elegir carpeta destino 📁
7. Click en **"Finish"** ✅

---

## 📁 Paso 2: Estructura del Proyecto

El proyecto sigue una arquitectura de capas para mayor escalabilidad:

```text
src/main/java/com/sistema/
├── modelos/    # Clases POJO (Usuario, Pregunta, Resultado, etc.)
├── logica/     # Procesamiento, Persistencia, Estadísticas y PDF
└── ui/         # Interfaces gráficas (Swing, JFreeChart)
```

---

## ▶️ Paso 3: Compilar y Ejecutar

### 🎯 Método 1: Desde NetBeans
- Dado que el proyecto usa **Maven**, primero haz clic derecho sobre el proyecto → **Clean and Build** 🛠️ (Esto descargará las librerías de gráficos y PDF).
- Luego, clic derecho sobre el proyecto → **Run** 🚀 o presionar **F6**.

### 🔍 Método 2: Clase Principal
1. Expandir **"Source Packages"** 📦
2. Localizar el paquete `com.sistema.ui`
3. Buscar el archivo **`MenuPrincipal.java`** 🖥️
4. Click derecho sobre ese archivo → **Run File** (Shift + F6)

---

## 📊 Visualización de Resultados
El sistema incluye un **Dashboard estadístico** que permite filtrar el rendimiento por:
1. **Usuario:** Comparativa de intentos.
2. **Tema:** Fortalezas y debilidades en áreas específicas.
3. **Nivel:** Desempeño según la complejidad (Básico, Intermedio, Avanzado).

---

## 🛠️ Tecnologías Utilizadas
- **Java Swing:** Para la creación de la interfaz de usuario.
- **Maven:** Para la gestión de dependencias externas.
- **JFreeChart:** Generación de gráficos de barras dinámicos.
- **iText PDF:** Creación de reportes profesionales exportables.
- **File I/O:** Persistencia de datos en archivos planos `.txt`.

---

<div align="center">
<sub>💙 Proyecto desarrollado con Java y NetBeans para Programación III</sub>
</div>
