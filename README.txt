# SpringEduManager

Aplicación web educativa desarrollada con **Spring Boot** que permite gestionar estudiantes, cursos, inscripciones y evaluaciones de un bootcamp de programación. Incluye autenticación con **JWT**, control de acceso por roles y una interfaz frontend construida con **HTML + Bootstrap + JavaScript**.

---

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.x
- Spring MVC — arquitectura REST
- Spring Data JPA + Hibernate
- Spring Security + JWT (jjwt)
- MySQL 9.x
- Maven
- HTML5 + CSS3 + Bootstrap 5.3
- JavaScript (Fetch API)

---

## Estructura del proyecto

```
SpringEduManager/          ← Backend (Spring Boot)
└── src/main/java/com/vale/springedumanager/
    ├── config/            ← SecurityConfig
    ├── controller/        ← AuthController, EstudianteController, CursoController...
    ├── dto/               ← DTOs de request y response
    ├── entity/            ← Entidades JPA
    ├── exception/         ← Manejo global de errores
    ├── jwt/               ← JwtAuthFilter
    ├── mapper/            ← Mappers entidad ↔ DTO
    ├── repository/        ← Interfaces JpaRepository
    └── service/           ← Lógica de negocio

SpringEduManagerFront/     ← Frontend (estático)
└── WebContent/
    ├── html/
    │   ├── login.html
    │   ├── admin.html
    │   └── estudiante.html
    ├── css/
    │   ├── global.css
    │   ├── login.css
    │   ├── admin.css
    │   └── estudiante.css
    └── js/
        ├── auth.js
        ├── admin.js
        └── estudiante.js
```

---

## Requisitos previos

- Java 21+
- Maven 3.8+
- MySQL 8+ corriendo en `localhost:3306`
- IDE (Eclipse o IntelliJ)
- VS Code con extensión **Live Server** (para el frontend)

---

## Configuración de la base de datos

1. Crear la base de datos en MySQL:

```sql
CREATE DATABASE springedu;
```

2. Configurar las credenciales en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/springedu
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
spring.jpa.hibernate.ddl-auto=update
```

---

## Cómo ejecutar el backend

1. Clonar o descargar el proyecto
2. Abrir `SpringEduManager` en Eclipse como proyecto Maven existente
3. Ejecutar `SpringEduManagerApplication.java` como **Spring Boot App**
4. El servidor arranca en `http://localhost:9095`

Al arrancar, la app crea automáticamente las tablas en la BD y un usuario administrador por defecto:

| Campo    | Valor |
|----------|-------|
| Username | `admin` |
| Password | `1234` |
| Rol      | `ADMIN` |

---

## Cargar datos de prueba

Una vez que la app esté corriendo y las tablas estén creadas, ejecutar el archivo `datos_prueba.sql` en MySQL Workbench:

```
Archivo → Open SQL Script → datos_prueba.sql → Execute
```

Esto crea:
- 3 estudiantes con sus usuarios vinculados
- 4 cursos
- 6 inscripciones
- 14 evaluaciones con notas variadas

Todos los usuarios de prueba tienen contraseña `1234`.

| Username | Rol | Nombre |
|---|---|---|
| `admin` | ADMIN | Administrador |
| `ana.garcia` | USER | Ana García |
| `carlos.lopez` | USER | Carlos López |
| `maria.perez` | USER | María Pérez |

---

## Cómo ejecutar el frontend

1. Abrir la carpeta `SpringEduManagerFront/WebContent` en **VS Code**
2. Hacer clic derecho sobre `html/login.html` → **Open with Live Server**
3. Acceder desde `http://127.0.0.1:5500/html/login.html`

> El frontend consume la API en `http://localhost:9095`. El backend debe estar corriendo antes de usar el frontend.

---

## Endpoints principales

### Autenticación
| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| POST | `/api/auth/login` | Público | Retorna token JWT |

### Usuarios
| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/api/usuarios` | Autenticado | Lista todos los usuarios |
| POST | `/api/usuarios` | ADMIN | Crea un usuario (y estudiante si rol USER) |
| PUT | `/api/usuarios/{id}` | ADMIN | Actualiza un usuario |
| DELETE | `/api/usuarios/{id}` | ADMIN | Elimina un usuario |

### Estudiantes
| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/api/estudiantes` | Autenticado | Lista todos los estudiantes |
| GET | `/api/estudiantes/{id}` | Autenticado | Obtiene un estudiante por ID |
| DELETE | `/api/estudiantes/{id}` | Autenticado | Elimina un estudiante |

### Cursos
| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/api/cursos` | Autenticado | Lista todos los cursos |
| POST | `/api/cursos` | Autenticado | Crea un curso |
| DELETE | `/api/cursos/{id}` | ADMIN | Elimina un curso |

### Inscripciones
| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/api/inscripciones` | Autenticado | Lista todas las inscripciones |
| POST | `/api/inscripciones` | Autenticado | Inscribe un estudiante a un curso |

### Evaluaciones
| Método | Endpoint | Acceso | Descripción |
|--------|----------|--------|-------------|
| GET | `/api/evaluaciones` | Autenticado | Lista todas las evaluaciones |
| POST | `/api/evaluaciones/inscripcion/{id}` | Autenticado | Registra una evaluación |
| GET | `/api/evaluaciones/tablero/{estudianteId}` | Autenticado | Tablero de notas por estudiante |

---

## Uso del token JWT

Todos los endpoints protegidos requieren el header:

```
Authorization: Bearer <token>
```

El token se obtiene haciendo POST a `/api/auth/login` con:

```json
{
  "username": "admin",
  "password": "1234"
}
```

---

## Roles y permisos

| Rol | Permisos |
|-----|----------|
| `ADMIN` | Acceso total — CRUD de usuarios, cursos, inscripciones y evaluaciones |
| `USER` | Puede ver sus propios cursos y evaluaciones en el tablero |

Al crear un usuario con rol `USER`, el sistema genera automáticamente un perfil de estudiante vinculado.

---
## Clase TestPassword

Fue creada únicamente para comprobar el valor de la contraseña encriptada.

---

## Autor

Valentina Villarroel
Desarrollado como proyecto final del **Módulo 6 — Desarrollo de aplicaciones JEE con Spring Framework** en Alkemy - English Always.