# Historias de usuario del proyecto Sales API

Este documento describe las historias de usuario del proyecto, alineadas con la implementación actual de la API y con los contratos reales definidos por los DTOs.

## Historias de usuario

### HU-01: Gestión de productos
Como dueño del bazar, quiero poder crear, listar, editar y eliminar productos para mantener actualizado el catálogo de la tienda.

Criterios de aceptación:
- Se pueden registrar productos con nombre, marca, categoría, precio y stock.
- Se pueden consultar todos los productos o uno en particular por código.
- Se puede actualizar la información de un producto.
- Se puede eliminar un producto sin borrar físicamente el registro.

Formato esperado del endpoint:
- POST /api/products y PUT /api/products/{productCode}: reciben un JSON con el DTO ProductRequestDTO.

```json
{
  "name": "Leche Entera",
  "brand": "Sanalac",
  "category": "GROCERIES",
  "price": 1200.50,
  "stock": 25
}
```

- GET /api/products y GET /api/products/{productCode}: responden con un JSON basado en ProductResponseDTO.

```json
{
  "code": "PRO-00001",
  "name": "Leche Entera",
  "brand": "Sanalac",
  "category": "GROCERIES",
  "price": 1200.50,
  "stock": 25
}
```

### HU-02: Gestión de clientes
Como empleado del bazar, quiero poder registrar y consultar clientes para asociarlos correctamente a las ventas.

Criterios de aceptación:
- Se pueden crear clientes con nombre, apellido y DNI.
- Se pueden listar y buscar clientes por código.
- Se pueden editar y eliminar clientes con control de integridad.

Formato esperado del endpoint:
- POST /api/clients y PUT /api/clients/{clientCode}: reciben un JSON con el DTO ClientRequestDTO.

```json
{
  "name": "María",
  "lastName": "Pérez",
  "dni": "30111222"
}
```

- GET /api/clients y GET /api/clients/{clientCode}: responden con un JSON basado en ClientResponseDTO.

```json
{
  "code": "CLI-00001",
  "name": "María",
  "lastName": "Pérez",
  "dni": "30111222"
}
```

### HU-03: Gestión de ventas
Como operador de caja, quiero registrar ventas con sus productos y el cliente asociado para llevar un control de las operaciones del negocio.

Criterios de aceptación:
- Se pueden registrar ventas con uno o más productos.
- Cada venta está asociada a un cliente.
- Se pueden consultar ventas anteriores y sus detalles asociados.
- Se pueden eliminar ventas de forma lógica.

Formato esperado del endpoint:
- POST /api/sales y PUT /api/sales/{saleCode}: reciben un JSON con el DTO SaleRequestDTO.

```json
{
  "clientCode": "CLI-00001",
  "details": [
    {
      "productCode": "PRO-00001",
      "quantity": 2
    },
    {
      "productCode": "PRO-00002",
      "quantity": 1
    }
  ]
}
```

- GET /api/sales y GET /api/sales/{saleCode}: responden con un JSON basado en SaleResponseDTO.

```json
{
  "code": "202606270001",
  "date": "2026-06-27",
  "totalAmount": 2500.00,
  "clientCode": "CLI-00001",
  "details": [
    {
      "productCode": "PRO-00001",
      "quantity": 2,
      "partialAmount": 2401.00
    }
  ]
}
```

### HU-04: Consulta de stock bajo
Como administrador, quiero visualizar los productos con stock bajo para reponer mercadería a tiempo.

Criterios de aceptación:
- Existe un endpoint que devuelve los productos cuyo stock es menor a un valor definido.
- La consulta ayuda a la toma de decisiones de reposición.

Formato esperado del endpoint:
- GET /api/products/low-stock/{lessThanStock}: devuelve una lista de ProductResponseDTO.

```json
[
  {
    "code": "PRO-00003",
    "name": "Arroz",
    "brand": "Dos Hermanos",
    "category": "GROCERIES",
    "price": 950.00,
    "stock": 3
  }
]
```

### HU-05: Consulta de categorías
Como desarrollador frontend, quiero obtener las categorías de productos desde la API para construir formularios y filtros de manera consistente.

Criterios de aceptación:
- La API expone un endpoint que devuelve las categorías disponibles con su código y etiqueta.

Formato esperado del endpoint:
- GET /api/products/get-categories: devuelve una lista de objetos con el formato del DTO de categoría.

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

### HU-06: Reportes de ventas
Como responsable del negocio, quiero consultar reportes de ventas por día para conocer el volumen de operaciones y los montos acumulados.

Criterios de aceptación:
- Se puede consultar el total de ventas y la suma de montos de una fecha determinada.
- La respuesta está estructurada de forma clara y reutilizable.

Formato esperado del endpoint:
- GET /api/sales/sum-count/{date}: devuelve un JSON basado en SaleSumCountDTO.

```json
{
  "date": "2026-06-27",
  "saleCount": 5,
  "dayTotalAmount": 125000.00
}
```

### HU-07: Venta de mayor monto
Como dueño del bazar, quiero identificar la venta con mayor monto para analizar el comportamiento del negocio.

Criterios de aceptación:
- El sistema expone la venta de mayor importe junto con información del cliente y la cantidad de productos involucrados.

Formato esperado del endpoint:
- GET /api/sales/best-sale: devuelve un JSON basado en SaleMajorAmountDTO.

```json
{
  "saleCode": "202606270001",
  "totalAmount": 2500.00,
  "productQuantity": 3,
  "clientName": "María",
  "clientLastName": "Pérez",
  "clientCode": "CLI-00001"
}
```

### HU-08: Autenticación y gestión de sesión
Como usuario del sistema, quiero autenticarme de forma segura para acceder a los recursos protegidos y mantener una sesión administrada con tokens.

Criterios de aceptación:
- Se puede iniciar sesión mediante un usuario y contraseña válidos.
- El sistema devuelve cookies de acceso y refresh con los tokens JWT.
- Se puede renovar la sesión con el refresh token.
- Se puede cerrar la sesión y limpiar las cookies.

Formato esperado del endpoint:
- POST /api/auth/login: recibe un JSON con LoginRequestDTO.

```json
{
  "username": "admin",
  "password": "password123"
}
```

Respuesta esperada:
- Headers Set-Cookie con AUTH-TOKEN y REFRESH-TOKEN, sin un cuerpo JSON de respuesta.

### HU-09: Gestión de vendedores y roles
Como administrador, quiero poder administrar vendedores y roles para controlar el acceso a la API y mantener la seguridad del sistema.

Criterios de aceptación:
- El administrador puede listar, crear, actualizar y eliminar vendedores.
- El administrador puede asignar o remover roles a un vendedor.
- Un vendedor puede cambiar su propia contraseña.

Formato esperado del endpoint:
- GET /api/vendor, POST /api/vendor, PUT /api/vendor/{vendorCode}, DELETE /api/vendor/{vendorCode}: reciben y devuelven VendorRequestDTO y VendorResponseDTO.

```json
{
  "code": "VEN-00001",
  "name": "Ana",
  "lastName": "Gómez",
  "dni": "40111222",
  "roles": ["ROLE_ADMIN", "ROLE_VENDOR"]
}
```

## Mejoras implementadas respecto a la guía

Además de cubrir los requerimientos funcionales iniciales, el proyecto incorpora mejoras para acercarlo a una implementación más profesional y realista:

- Endpoints RESTful bajo el prefijo /api.
- Uso de códigos de negocio para productos, clientes y ventas.
- DTOs para separar transporte y dominio.
- Validaciones y manejo global de excepciones.
- Soft delete para conservar trazabilidad.
- Flyway para migraciones reproducibles.
- Seguridad con Spring Security, JWT y cookies HTTP-only.
- Roles y autorización por método con @PreAuthorize.

