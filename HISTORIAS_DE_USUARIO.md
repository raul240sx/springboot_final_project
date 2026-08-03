# Historias de usuario - Migración a microservicios del proyecto Sales API

Este documento describe la versión actual de la API con Spring Cloud, basada en los controladores y DTOs reales del proyecto. Su propósito es servir como guía de uso para probar la arquitectura distribuida de la tienda o bazar, sin tomar como referencia la carpeta legacy-monolith.

> Nota: los ejemplos de petición y respuesta que aparecen a continuación reflejan la estructura de los DTOs implementados en los microservicios. En un entorno con Docker Compose, las peticiones pueden enviarse a través del gateway en http://localhost:9022.

## 1. Autenticación y acceso

### HU-01: Inicio de sesión
Como vendedor o administrador, quiero iniciar sesión para obtener acceso a los recursos protegidos del sistema.

- Endpoint: POST /api/auth/login
- Cuerpo de petición:

```json
{
  "username": "admin-0001",
  "password": "asd123123"
}
```

- Respuesta esperada:
  - Código HTTP: 200 OK
  - Headers Set-Cookie con los tokens de acceso y refresh
  - Cuerpo vacío

### HU-02: Renovar sesión
Como usuario autenticado, quiero refrescar la sesión sin volver a ingresar las credenciales.

- Endpoint: POST /api/auth/refresh
- Requiere: cookie REFRESH-TOKEN
- Respuesta esperada:
  - Código HTTP: 200 OK
  - Headers Set-Cookie actualizados
  - Cuerpo vacío

### HU-03: Cerrar sesión
Como usuario autenticado, quiero cerrar la sesión y eliminar las credenciales vigentes.

- Endpoint: POST /api/auth/logout
- Requiere: cookie REFRESH-TOKEN (opcional)
- Respuesta esperada:
  - Código HTTP: 200 OK
  - Headers Set-Cookie para invalidar las cookies
  - Cuerpo vacío

## 2. Gestión de roles

### HU-04: Listar roles
Como administrador, quiero consultar los roles disponibles del sistema.

- Endpoint: GET /api/roles
- Respuesta esperada:

```json
[
  {
    "id": 1,
    "name": "ROLE_ADMIN"
  },
  {
    "id": 2,
    "name": "ROLE_VENDOR"
  }
]
```

### HU-05: Obtener rol por id
- Endpoint: GET /api/roles/{roleId}
- Respuesta esperada:

```json
{
  "id": 1,
  "name": "ROLE_ADMIN"
}
```

### HU-06: Crear rol
Como administrador, quiero crear nuevos roles para extender la autorización del sistema.

- Endpoint: POST /api/roles
- Requiere: rol ADMIN
- Cuerpo de petición:

```json
{
  "name": "ROLE_AUDITOR"
}
```

- Respuesta esperada:

```json
{
  "id": 3,
  "name": "ROLE_AUDITOR"
}
```

### HU-07: Eliminar rol
Como administrador, quiero eliminar un rol que ya no se use.

- Endpoint: DELETE /api/roles/{roleId}
- Requiere: rol ADMIN
- Respuesta esperada:
  - Código HTTP: 204 No Content
  - Cuerpo vacío

## 3. Gestión de vendedores

### HU-08: Listar vendedores
Como administrador, quiero consultar los vendedores registrados.

- Endpoint: GET /api/vendors
- Requiere: rol ADMIN
- Respuesta esperada:

```json
[
  {
    "code": "VEN-00001",
    "name": "admin",
    "lastName": "admin",
    "dni": "112223334",
    "roles": ["ROLE_ADMIN", "ROLE_VENDOR"]
  }
]
```

### HU-09: Obtener vendedor por código
- Endpoint: GET /api/vendors/{vendorCode}
- Requiere: rol ADMIN
- Respuesta esperada:

```json
{
  "code": "admin-0001",
  "name": "admin",
  "lastName": "admin",
  "dni": "112223334",
  "roles": ["ROLE_ADMIN", "ROLE_VENDOR"]
}
```

### HU-10: Crear vendedor
Como administrador, quiero registrar un nuevo vendedor para permitir su acceso a la plataforma.

- Endpoint: POST /api/vendors
- Requiere: rol ADMIN
- Cuerpo de petición:

```json
{
  "name": "Carlos",
  "lastName": "Pérez",
  "dni": "12345678",
  "password": "miPassword123",
  "confirmPassword": "miPassword123"
}
```

- Respuesta esperada:

```json
{
  "code": "VEN-00002",
  "name": "Carlos",
  "lastName": "Pérez",
  "dni": "12345678",
  "roles": ["ROLE_VENDOR"]
}
```

### HU-11: Actualizar vendedor
- Endpoint: PUT /api/vendors/{vendorCode}
- Requiere: rol ADMIN
- Cuerpo de petición:

```json
{
  "name": "Carlos",
  "lastName": "Pérez",
  "dni": "12345678",
  "password": "nuevoPassword123",
  "confirmPassword": "nuevoPassword123"
}
```

- Respuesta esperada:

```json
{
  "code": "VEN-00002",
  "name": "Carlos",
  "lastName": "Pérez",
  "dni": "12345678",
  "roles": ["ROLE_VENDOR"]
}
```

### HU-12: Eliminar vendedor
- Endpoint: DELETE /api/vendors/{vendorCode}
- Requiere: rol ADMIN
- Respuesta esperada:
  - Código HTTP: 204 No Content
  - Cuerpo vacío

### HU-13: Asignar rol a un vendedor
- Endpoint: PUT /api/vendors/add-role
- Requiere: rol ADMIN
- Cuerpo de petición:

```json
{
  "vendorCode": "VEN-00002",
  "roleId": 1
}
```

- Respuesta esperada:

```json
{
  "code": "VEN-00002",
  "name": "Carlos",
  "lastName": "Pérez",
  "dni": "12345678",
  "roles": ["ROLE_VENDOR", "ROLE_ADMIN"]
}
```

### HU-14: Quitar rol a un vendedor
- Endpoint: PUT /api/vendors/remove-role
- Requiere: rol ADMIN
- Cuerpo de petición:

```json
{
  "vendorCode": "VEN-00002",
  "roleId": 1
}
```

- Respuesta esperada:

```json
{
  "code": "VEN-00002",
  "name": "Carlos",
  "lastName": "Pérez",
  "dni": "12345678",
  "roles": ["ROLE_VENDOR"]
}
```

### HU-15: Cambiar contraseña
Como vendedor, quiero actualizar mi propia contraseña sin afectar a otros usuarios.

- Endpoint: PUT /api/vendors/change-password/{vendorCode}
- Requiere: autenticación del mismo vendedor
- Cuerpo de petición:

```json
{
  "password": "nuevaPassword123",
  "confirmPassword": "nuevaPassword123"
}
```

- Respuesta esperada:

```json
{
  "code": "VEN-00002",
  "name": "Carlos",
  "lastName": "Pérez",
  "dni": "12345678",
  "roles": ["ROLE_VENDOR"]
}
```

## 4. Gestión de clientes

### HU-16: Listar clientes
Como operador de la tienda, quiero visualizar los clientes del negocio.

- Endpoint: GET /api/clients
- Respuesta esperada:

```json
[
  {
    "code": "CLI-00001",
    "name": "María",
    "lastName": "Pérez",
    "dni": "30111222"
  }
]
```

### HU-17: Obtener cliente por código
- Endpoint: GET /api/clients/{clientCode}
- Respuesta esperada:

```json
{
  "code": "CLI-00001",
  "name": "María",
  "lastName": "Pérez",
  "dni": "30111222"
}
```

### HU-18: Crear cliente
- Endpoint: POST /api/clients
- Cuerpo de petición:

```json
{
  "name": "María",
  "lastName": "Pérez",
  "dni": "30111222"
}
```

- Respuesta esperada:

```json
{
  "code": "CLI-00001",
  "name": "María",
  "lastName": "Pérez",
  "dni": "30111222"
}
```

### HU-19: Actualizar cliente
- Endpoint: PUT /api/clients/{clientCode}
- Cuerpo de petición:

```json
{
  "name": "María",
  "lastName": "Pérez",
  "dni": "30111223"
}
```

- Respuesta esperada:

```json
{
  "code": "CLI-00001",
  "name": "María",
  "lastName": "Pérez",
  "dni": "30111223"
}
```

### HU-20: Eliminar cliente
- Endpoint: DELETE /api/clients/{clientCode}
- Respuesta esperada:
  - Código HTTP: 204 No Content
  - Cuerpo vacío

## 5. Gestión de productos

### HU-21: Listar productos
Como administrador, quiero consultar el catálogo de productos disponible.

- Endpoint: GET /api/products
- Respuesta esperada:

```json
[
  {
    "code": "PRO-00001",
    "name": "Leche Entera",
    "brand": "Sanalac",
    "category": "GROCERIES",
    "price": 1200.5,
    "stock": 25
  }
]
```

### HU-22: Obtener producto por código
- Endpoint: GET /api/products/{productCode}
- Respuesta esperada:

```json
{
  "code": "PRO-00001",
  "name": "Leche Entera",
  "brand": "Sanalac",
  "category": "GROCERIES",
  "price": 1200.5,
  "stock": 25
}
```

### HU-23: Crear producto
- Endpoint: POST /api/products
- Requiere: rol ADMIN
- Cuerpo de petición:

```json
{
  "name": "Leche Entera",
  "brand": "Sanalac",
  "category": "GROCERIES",
  "price": 1200.5,
  "stock": 25
}
```

- Respuesta esperada:

```json
{
  "code": "PRO-00001",
  "name": "Leche Entera",
  "brand": "Sanalac",
  "category": "GROCERIES",
  "price": 1200.5,
  "stock": 25
}
```

### HU-24: Actualizar producto
- Endpoint: PUT /api/products/{productCode}
- Requiere: rol ADMIN
- Cuerpo de petición:

```json
{
  "name": "Leche Entera",
  "brand": "Sanalac",
  "category": "GROCERIES",
  "price": 1300.0,
  "stock": 20
}
```

- Respuesta esperada:

```json
{
  "code": "PRO-00001",
  "name": "Leche Entera",
  "brand": "Sanalac",
  "category": "GROCERIES",
  "price": 1300.0,
  "stock": 20
}
```

### HU-25: Eliminar producto
- Endpoint: DELETE /api/products/{productCode}
- Requiere: rol ADMIN
- Respuesta esperada:
  - Código HTTP: 204 No Content
  - Cuerpo vacío

### HU-26: Productos con stock bajo
- Endpoint: GET /api/products/low-stock/{lessThanStock}
- Respuesta esperada:

```json
[
  {
    "code": "PRO-00002",
    "name": "Arroz",
    "brand": "Dos Hermanos",
    "category": "GROCERIES",
    "price": 950.0,
    "stock": 3
  }
]
```

### HU-27: Categorías del catálogo
- Endpoint: GET /api/products/get-categories
- Respuesta esperada:

```json
[
  {
    "code": "GROCERIES",
    "label": "Abarrotes"
  },
  {
    "code": "TECHNOLOGY",
    "label": "Tecnología"
  }
]
```

### HU-28: Validar productos y reducir stock
Como servicio interno de ventas, quiero validar que los productos existan y que haya stock suficiente antes de registrar una venta.

- Endpoint: POST /api/products/validate-products-reduce-stock
- Cuerpo de petición:

```json
[
  {
    "productCode": "PRO-00001",
    "quantity": 2
  }
]
```

- Respuesta esperada:

```json
[
  {
    "productCode": "PRO-00001",
    "quantity": 2,
    "partialAmount": 2400.0
  }
]
```

### HU-29: Devolver stock
Como servicio interno de ventas, quiero liberar stock cuando una venta se revierte o se elimina.

- Endpoint: POST /api/products/return-stock
- Cuerpo de petición:

```json
[
  {
    "productCode": "PRO-00001",
    "quantity": 2
  }
]
```

- Respuesta esperada:
  - Código HTTP: 204 No Content
  - Cuerpo vacío

## 6. Gestión de ventas

### HU-30: Listar ventas
Como responsable de operaciones, quiero consultar las ventas registradas.

- Endpoint: GET /api/sales
- Respuesta esperada:

```json
[
  {
    "code": "202608030001",
    "date": "2026-08-03",
    "totalAmount": 2400.0,
    "clientCode": "CLI-00001",
    "details": [
      {
        "productCode": "PRO-00001",
        "quantity": 2,
        "partialAmount": 2400.0
      }
    ]
  }
]
```

### HU-31: Obtener venta por código
- Endpoint: GET /api/sales/{saleCode}
- Respuesta esperada:

```json
{
  "code": "202608030001",
  "date": "2026-08-03",
  "totalAmount": 2400.0,
  "clientCode": "CLI-00001",
  "details": [
    {
      "productCode": "PRO-00001",
      "quantity": 2,
      "partialAmount": 2400.0
    }
  ]
}
```

### HU-32: Crear venta
- Endpoint: POST /api/sales
- Cuerpo de petición:

```json
{
  "clientCode": "CLI-00001",
  "vendorCode": "VEN-00001",
  "details": [
    {
      "productCode": "PRO-00001",
      "quantity": 2
    }
  ]
}
```

- Respuesta esperada:

```json
{
  "code": "202608030001",
  "date": "2026-08-03",
  "totalAmount": 2400.0,
  "clientCode": "CLI-00001",
  "details": [
    {
      "productCode": "PRO-00001",
      "quantity": 2,
      "partialAmount": 2400.0
    }
  ]
}
```

### HU-33: Actualizar venta
- Endpoint: PUT /api/sales/{saleCode}
- Cuerpo de petición:

```json
{
  "clientCode": "CLI-00001",
  "vendorCode": "VEN-00001",
  "details": [
    {
      "productCode": "PRO-00001",
      "quantity": 3
    }
  ]
}
```

- Respuesta esperada:

```json
{
  "code": "202608030001",
  "date": "2026-08-03",
  "totalAmount": 3600.0,
  "clientCode": "CLI-00001",
  "details": [
    {
      "productCode": "PRO-00001",
      "quantity": 3,
      "partialAmount": 3600.0
    }
  ]
}
```

### HU-34: Eliminar venta
- Endpoint: DELETE /api/sales/{saleCode}
- Requiere: rol ADMIN
- Respuesta esperada:
  - Código HTTP: 204 No Content
  - Cuerpo vacío

### HU-35: Resumen diario de ventas
Como administrador, quiero consultar el total de ventas y el monto acumulado de un día determinado.

- Endpoint: GET /api/sales/sum-count/{date}
- Requiere: rol ADMIN
- Ejemplo de fecha: 2026-08-03
- Respuesta esperada:

```json
{
  "date": "2026-08-03",
  "saleCount": 1,
  "dayTotalAmount": 2400.0
}
```

### HU-36: Venta de mayor monto
Como administrador, quiero identificar la venta más relevante del negocio.

- Endpoint: GET /api/sales/best-sale
- Requiere: rol ADMIN
- Respuesta esperada:

```json
{
  "saleCode": "202608030001",
  "totalAmount": 2400.0,
  "clientName": "María",
  "clientLastName": "Pérez",
  "clientCode": "CLI-00001",
  "details": [
    {
      "productCode": "PRO-00001",
      "quantity": 2,
      "partialAmount": 2400.0
    }
  ]
}
```

## 7. Notas operativas

- Los roles base y el usuario administrador inicial se crean automáticamente cuando la base de datos está vacía y se leen desde las variables FIRST_USER_* del archivo .env.
- Para pruebas funcionales, conviene consumir la API a través del gateway en el puerto 9022.
- La documentación anterior se basa en los controladores y DTOs actually implementados en los microservicios de la versión Spring Cloud.
