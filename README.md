# Sales API

API REST desarrollada con Spring Boot y Java para gestionar productos, clientes, ventas, usuarios y roles de un bazar. El proyecto combina una arquitectura en capas con DTOs, validaciones, manejo de excepciones, Flyway, MySQL, soft delete y autenticación JWT con Spring Security.

## Descripción general

Esta API está orientada a un entorno realista de negocio, con endpoints RESTful bajo el prefijo /api, respuestas HTTP explícitas mediante ResponseEntity, códigos de negocio para los recursos principales y una capa de seguridad basada en tokens JWT almacenados en cookies HTTP-only.

## Objetivo del proyecto

Facilitar la operación del bazar permitiendo:

- Registrar, consultar, actualizar y eliminar productos.
- Registrar, consultar, actualizar y eliminar clientes.
- Registrar, consultar, actualizar y eliminar ventas.
- Consultar productos con stock bajo.
- Obtener reportes de ventas por fecha y la venta de mayor monto.
- Autenticar usuarios y proteger los recursos con roles.

## Funcionalidades implementadas

### 1. Gestión de productos

Endpoints disponibles:

- GET /api/products
- GET /api/products/{productCode}
- POST /api/products
- PUT /api/products/{productCode}
- DELETE /api/products/{productCode}
- GET /api/products/low-stock/{lessThanStock}
- GET /api/products/get-categories

### 2. Gestión de clientes

Endpoints disponibles:

- GET /api/clients
- GET /api/clients/{clientCode}
- POST /api/clients
- PUT /api/clients/{clientCode}
- DELETE /api/clients/{clientCode}

### 3. Gestión de ventas

Endpoints disponibles:

- GET /api/sales
- GET /api/sales/{saleCode}
- POST /api/sales
- PUT /api/sales/{saleCode}
- DELETE /api/sales/{saleCode}
- GET /api/sales/sum-count/{date}
- GET /api/sales/best-sale

### 4. Seguridad con Spring Security

La aplicación incorpora autenticación y autorización basada en JWT:

- POST /api/auth/login: autentica un usuario y devuelve cookies AUTH-TOKEN y REFRESH-TOKEN.
- POST /api/auth/refresh: renueva los tokens a partir del refresh token almacenado en cookie.
- POST /api/auth/logout: invalida la sesión y limpia las cookies.
- GET /api/vendor y /api/role: protegidos para usuarios con rol ADMIN.
- PUT /api/vendor/change-password/{vendorCode}: permite al propio usuario cambiar su contraseña.

Roles implementados:

- ROLE_ADMIN
- ROLE_VENDOR

## Seguridad implementada

El módulo de seguridad usa Spring Security con OAuth2 Resource Server y JWT firmado con RSA.

Aspectos principales:

- Autenticación con username/password mediante AuthenticationManager.
- Generación de access token y refresh token.
- Tokens JWT firmados con RS256.
- Cookies HTTP-only para AUTH-TOKEN y REFRESH-TOKEN.
- Configuración de CORS y URLs públicas desde application.properties.
- Protección por roles con @PreAuthorize.
- Inicialización automática de un usuario administrador inicial desde variables de entorno.

### URLs públicas

Las rutas públicas incluyen:

- /api/auth/login
- /api/auth/refresh
- /
- /index.html
- /css/**

## Contratos de entrada y salida

La API trabaja con DTOs para separar la capa de transporte del dominio:

- ProductRequestDTO / ProductResponseDTO
- ClientRequestDTO / ClientResponseDTO
- SaleRequestDTO / SaleResponseDTO
- SaleSumCountDTO
- SaleMajorAmountDTO
- VendorRequestDTO / VendorResponseDTO
- LoginRequestDTO

## Diseño y buenas prácticas aplicadas

- Códigos de negocio en lugar de IDs visibles para productos, clientes y ventas.
- Validaciones de entrada con mensajes personalizados.
- Manejo global de excepciones con ProblemDetail.
- Soft delete con SQLDelete y SQLRestriction.
- Migraciones automáticas con Flyway.
- Repositorios con Spring Data JPA y consultas personalizadas.
- Respuestas HTTP consistentes con ResponseEntity.

## Tecnologías utilizadas

- Java 21
- Spring Boot 3.x
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Spring Validation
- OAuth2 Resource Server
- Flyway
- MySQL
- Lombok
- Maven
- Docker Compose

## Cómo ejecutar el proyecto

### Requisitos previos

- Java 21
- Maven
- Docker y Docker Compose
- MySQL o Docker Compose para levantar la base de datos

### Opción 1: Docker Compose

1. Crear un archivo .env en la raíz del proyecto con las variables de entorno necesarias.
2. Ejecutar:

```bash
docker compose up --build
```

### Opción 2: Ejecución local

```bash
./mvnw spring-boot:run
```

La API quedará disponible en:

- http://localhost:8080

## Autoría

Proyecto desarrollado por:

- Raúl Ignacio Ramírez Sanhueza
- GitHub: @raul240sx
- Email: raul.ramirez1401@gmail.com
