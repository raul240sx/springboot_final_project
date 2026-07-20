# Sales API

API REST desarrollada con Spring Boot y Java para gestionar productos, clientes y ventas de un bazar, diseñada para servir tanto a una aplicación web como a una futura app móvil.

## Descripción general

Este proyecto implementa un backend robusto y escalable para administrar el catálogo de productos, los clientes del negocio y las ventas realizadas. La API está orientada a un uso real en producción, con validaciones, manejo de excepciones, respuestas estándar, persistencia con MySQL, migraciones automáticas con Flyway y un modelo de borrado lógico mediante soft delete.

La solución sigue una arquitectura en capas y utiliza DTOs para separar la lógica de negocio de la representación de datos expuesta a los clientes HTTP.

## Objetivo del proyecto

El objetivo principal es facilitar la gestión operativa del bazar permitiendo:

- Registrar, consultar, actualizar y eliminar productos.
- Registrar, consultar, actualizar y eliminar clientes.
- Registrar, consultar, actualizar y eliminar ventas.
- Consultar productos con bajo stock.
- Obtener reportes de ventas por fecha y la venta de mayor monto.
- Exponer una API REST clara, segura y preparada para integración con frontend y mobile.

## Funcionalidades implementadas

### 1. Gestión de productos

Se implementa un CRUD completo para productos con los siguientes endpoints:

- GET /products: lista todos los productos.
- GET /products/{productCode}: obtiene un producto por su código.
- POST /products: crea un nuevo producto.
- PUT /products/{productCode}: actualiza un producto existente.
- DELETE /products/{productCode}: elimina lógicamente un producto.

Además, se incluyen funciones adicionales:

- GET /products/low-stock/{lessThanStock}: obtiene productos con stock menor al valor indicado.
- GET /products/get-categories: expone las categorías disponibles para que el frontend pueda construir formularios y filtros de forma consistente.

### 2. Gestión de clientes

Se implementa un CRUD completo para clientes con los siguientes endpoints:

- GET /clients: lista todos los clientes.
- GET /clients/{clientCode}: obtiene un cliente por su código.
- POST /clients: crea un nuevo cliente.
- PUT /clients/{clientCode}: actualiza un cliente existente.
- DELETE /clients/{clientCode}: elimina lógicamente un cliente.

### 3. Gestión de ventas

Se implementa un CRUD completo para ventas con los siguientes endpoints:

- GET /sales: lista todas las ventas registradas.
- GET /sales/{saleCode}: obtiene una venta por su código.
- POST /sales: crea una nueva venta con sus detalles asociados.
- PUT /sales/{saleCode}: actualiza una venta existente.
- DELETE /sales/{saleCode}: elimina lógicamente una venta.

Además, se incluyen consultas agregadas:

- GET /sales/sum-count/{date}: obtiene la cantidad total de ventas y la sumatoria del monto de un día específico.
- GET /sales/best-sale: obtiene la venta con mayor monto, junto con la cantidad de productos, el cliente y su código.

## Diseño y buenas prácticas aplicadas

### Códigos de negocio en lugar de IDs

La API expone códigos generados para los recursos principales en lugar de utilizar IDs como identificadores visibles o seguros. Esto mejora la privacidad y la claridad de la integración:

- Productos: códigos tipo PRO-00001.
- Clientes: códigos tipo CLI-00001.
- Ventas: códigos tipo YYYYMMDD0001.

### Generación automática de códigos

Se utilizan clases especializadas para generar códigos de producto, cliente y venta, manteniendo una lógica consistente y reutilizable.

### Persistencia y migraciones

El proyecto usa Flyway con el archivo de creación de tablas en:

- src/main/resources/db/migration/V1__init.sql

Esto permite crear la estructura base de la base de datos de forma controlada y reproducible.

### Repositorios con Spring Data JPA

Se utilizan repositorios con query methods y consultas personalizadas con JPQL para obtener información de forma eficiente, por ejemplo:

- Búsquedas por código.
- Consultas de stock bajo.
- Sumatoria y conteo de ventas por fecha.
- Consulta de la venta con mayor monto.

### DTOs y validaciones

Los controladores reciben y devuelven DTOs, evitando exponer directamente entidades JPA. Además, se aplican validaciones sobre los datos de entrada con mensajes personalizados.

### Manejo de excepciones

El proyecto implementa excepciones personalizadas y un manejador global de excepciones utilizando ProblemDetail, lo que permite responder con errores estándar y consistentes en toda la API.

### Soft delete

Se implementa borrado lógico mediante las anotaciones SQLDelete y SQLRestriction en las entidades. Esto evita la eliminación física de registros y modifica la forma en que se filtran los resultados en las consultas.

### Respuestas HTTP consistentes

Todos los controladores utilizan ResponseEntity para devolver respuestas HTTP claras y semánticas, incluyendo estados como OK, CREATED y NO CONTENT.

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- Flyway
- MySQL
- Lombok
- Maven
- Docker Compose

## Estructura del proyecto

```text
src/
  main/
    java/
      com/store/sales_api/
        controller/
        dto/
        exception/
        mapper/
        model/
        repository/
        service/
        util/
    resources/
      application.properties
      ValidationMessages.properties
      db/migration/V1__init.sql
```

## Cómo replicar el proyecto

### Requisitos previos

- Java 21
- Maven
- Docker y Docker Compose
- MySQL (o usar Docker Compose para levantarla)

### Opción 1: Ejecutar con Docker Compose

1. Clona el repositorio.
2. Crea un archivo .env en la raíz con las variables de entorno necesarias, por ejemplo:

```env
DB_NAME=sales_db
DB_USER=sales_user
DB_PASS=sales_password
DB_ROOT_PASS=root_password
DB_URL=jdbc:mysql://sales-db:3306/sales_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
```

3. Ejecuta:

```bash
docker compose up --build
```

### Opción 2: Ejecutar localmente

1. Asegúrate de tener una base de datos MySQL en ejecución.
2. Ajusta las credenciales en application.properties o mediante variables de entorno.
3. Ejecuta:

```bash
./mvnw spring-boot:run
```

La API quedará disponible en:

- http://localhost:8080

## Flujo de negocio propuesto

1. Se registran productos y clientes.
2. Se crean ventas asociando un cliente y uno o más detalles de productos.
3. La aplicación calcula los importes y almacena la información en base de datos.
4. Se pueden consultar reportes y estados del stock para apoyar la operación del bazar.

## Autoría

Proyecto desarrollado por:

- Raúl Ignacio Ramírez Sanhueza
- GitHub: @raul240sx
- Email: raul.ramirez1401@gmail.com
