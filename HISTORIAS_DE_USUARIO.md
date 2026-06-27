# Historias de usuario del proyecto Sales API

Este documento recopila las historias de usuario aplicadas al proyecto, alineadas con la guía entregada y con las mejoras implementadas para adaptarlo a un escenario real de desarrollo backend.

## Historias de usuario

### HU-01: Gestión de productos
Como dueño del bazar, quiero poder crear, listar, editar y eliminar productos para mantener actualizado el catálogo de la tienda.

Criterios de aceptación:
- Se pueden registrar productos con nombre, marca, categoría, precio y stock.
- Se pueden consultar todos los productos o uno en particular por código.
- Se puede actualizar la información de un producto.
- Se puede eliminar un producto sin borrar físicamente el registro.

Formato esperado de los endpoints:
- POST /products: recibe un JSON con el formato del DTO de creación de producto.

```json
{
  "name": "Leche Entera",
  "brand": "Sanalac",
  "category": "GROCERIES",
  "price": 1200.50,
  "stock": 25
}
```

- PUT /products/{productCode}: usa el mismo cuerpo que el POST.
- GET /products y GET /products/{productCode}: devuelven un JSON con la estructura del DTO de respuesta:

```json
{
  "code": "GRO-00001",
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

Formato esperado de los endpoints:
- POST /clients y PUT /clients/{clientCode}: reciben un JSON con el DTO de cliente.

```json
{
  "name": "María",
  "lastName": "Pérez",
  "dni": "30111222"
}
```

- GET /clients y GET /clients/{clientCode}: responden con un JSON como este:

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
- Se pueden consultar ventas anteriores y detalles asociados.
- Se pueden eliminar ventas de forma lógica.

Formato esperado de los endpoints:
- POST /sales y PUT /sales/{saleCode}: reciben un JSON con la estructura del DTO de solicitud de venta.

```json
{
  "clientCode": "CLI-00001",
  "details": [
    {
      "productCode": "GRO-00001",
      "quantity": 2
    },
    {
      "productCode": "TEC-00002",
      "quantity": 1
    }
  ]
}
```

- GET /sales y GET /sales/{saleCode}: responden con un JSON como este:

```json
{
  "code": "202606270001",
  "date": "2026-06-27",
  "totalAmount": 2500.00,
  "clientCode": "CLI-00001",
  "details": [
    {
      "productCode": "GRO-00001",
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
- GET /products/low-stock/{lessThanStock}: devuelve una lista de productos en formato de DTO de respuesta.

```json
[
  {
    "code": "GRO-00003",
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
- GET /products/get-categories: devuelve un arreglo de objetos con el formato del DTO de categoría.

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
- Se puede consultar el total de ventas y la suma de montos para una fecha determinada.
- La respuesta está estructurada de forma clara y reutilizable.

Formato esperado del endpoint:
- GET /sales/sum-count/{date}: devuelve un JSON con la estructura del DTO de resumen diario.

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
- El sistema expone la venta con mayor importe junto con información del cliente y la cantidad de productos involucrados.

Formato esperado del endpoint:
- GET /sales/best-sale: devuelve un JSON con la estructura del DTO de venta destacada.

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

## Mejoras implementadas respecto a la guía

Además de cubrir los requerimientos funcionales de la guía, el proyecto incorpora varias mejoras para acercarlo a una implementación más profesional y RESTful:

- Endpoints RESTful y semánticos usando recursos en plural, como /products, /clients y /sales.
- Exposición de códigos de negocio en vez de IDs para mejorar la seguridad y la usabilidad de la API.
- Uso de DTOs para separar la capa de transporte de las entidades del modelo.
- Validaciones de entrada con mensajes personalizados y respuestas claras de error.
- Manejo global de excepciones con ProblemDetail, lo que permite un estándar de error uniforme.
- Uso de ResponseEntity en todos los controladores para devolver respuestas HTTP explícitas.
- Implementación de soft delete con SQLDelete y SQLRestriction para conservar la trazabilidad de los datos.
- Soporte para Flyway con un script de creación de tablas reproducible.
- Exposición de categorías y reportes para facilitar la integración con un frontend.
- Uso de query methods y consultas JPQL para operaciones de negocio más complejas.

## Valor agregado del proyecto

El proyecto no solo cumple con la guía del TP integrador, sino que además incorpora una base sólida para una API realista, preparada para continuar creciendo con nuevas funcionalidades como:

- control de stock automático al registrar ventas,
- descuentos y promociones,
- historial de cambios,
- autenticación y autorización,
- paginación y filtros avanzados.

