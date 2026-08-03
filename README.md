# Sales API Cloud - Migración a Microservicios con Spring Cloud

API REST para la gestión de productos, clientes, ventas, autenticación y permisos de una tienda o bazar, evolucionada desde una solución monolítica hacia una arquitectura de microservicios con Spring Boot, Spring Cloud, Eureka, Config Server, Gateway, OpenFeign, Resilience4j y seguridad basada en JWT y mTLS.

## Descripción general

Esta migración transforma la aplicación original en una plataforma distribuida pensada para el uso diario de vendedores y administradores de una tienda. La idea central es escalar la solución monolítica hacia un modelo más modular, tolerante a fallos y preparado para crecer con nuevas funcionalidades, sin perder el enfoque de negocio original: administrar el catálogo, los clientes, las ventas y los permisos de acceso.

El proyecto separa los dominios principales en microservicios independientes, cada uno con su propia base de datos y responsabilidades bien definidas. Además, incorpora un gateway de entrada, un servidor de configuración, un registro de servicios y mecanismos de seguridad que permiten operar la arquitectura de forma más robusta y profesional.

## Objetivo del proyecto

El objetivo principal es facilitar la operación del negocio permitiendo:

- Gestionar productos, clientes y ventas de forma distribuida.
- Controlar el acceso a la plataforma según el rol del usuario.
- Separar responsabilidades por dominio para mejorar mantenimiento y escalabilidad.
- Preparar la aplicación para crecer en un entorno cloud con múltiples servicios.
- Mantener la base del negocio del bazar como contexto principal de la API.

## Contexto de negocio

La API está pensada para una tienda o bazar que necesita:

- Registrar y administrar productos con stock y categorías.
- Mantener un catálogo de clientes para asociarlos a las ventas.
- Registrar operaciones de venta con detalle de productos.
- Diferenciar accesos entre vendedores y administradores.
- Contar con un modelo de seguridad que permita controlar quién puede crear, editar o consultar información sensible.

La solución también deja preparada la integración con una base histórica de usuarios para futuras versiones, lo que facilita la migración o la interoperabilidad con sistemas heredados.

## Arquitectura propuesta

La aplicación está compuesta por los siguientes servicios:

- ms-api-gateway: punto de entrada único para enrutar solicitudes y centralizar el acceso a la plataforma.
- ms-auth: autenticación, autorización, gestión de usuarios/vendedores, roles y emisión de tokens JWT.
- ms-products: gestión del catálogo de productos, categorías y control de stock.
- ms-clients: administración de clientes del negocio.
- ms-sales: registro de ventas, detalle de ventas y reportes.
- ms-config-server: servidor centralizado de configuraciones.
- ms-registry-server: descubrimiento y registro de servicios.
- cloud-db: base de datos MySQL compartida para los servicios del ecosistema.

## Seguridad y permisos

La plataforma usa autenticación basada en JWT y autorización por roles.

Roles principales:

- ROLE_ADMIN: acceso completo a operaciones administrativas y de gestión.
- ROLE_VENDOR: acceso orientado al uso operativo del negocio, con permisos limitados según el contexto.

Esto permite separar claramente la operativa del negocio de las tareas de administración y supervisión.

## Datos iniciales para pruebas

Para facilitar las primeras pruebas, el servicio de autenticación crea automáticamente los roles base y un primer usuario administrador cuando la base de datos está vacía.

Lo que se genera al iniciar por primera vez:

- Los roles ROLE_ADMIN y ROLE_VENDOR.
- Un primer usuario administrador con los datos definidos en las variables de entorno FIRST_USER_CODE, FIRST_USER_NAME, FIRST_USER_LAST_NAME, FIRST_USER_DNI y FIRST_USER_PASSWORD.

Este comportamiento está implementado en el inicializador de datos del microservicio de autenticación y resulta útil para probar el flujo completo de login, permisos y gestión de usuarios en un entorno de desarrollo.

## Documentación de endpoints

La documentación operativa y de consumo de la API se encuentra en [HISTORIAS_DE_USUARIO.md](HISTORIAS_DE_USUARIO.md). Allí se incluyen los endpoints reales expuestos por los controladores de la versión con Spring Cloud, junto con ejemplos de petición y respuesta basados en los DTOs del proyecto.

## Generación de certificados y almacenes de confianza

La arquitectura incorpora seguridad mTLS para proteger la comunicación entre servicios y con el gateway. Para ello se utilizan un keystore y un truststore por servicio.

### ¿Qué son?

- Keystore: almacena el certificado y la clave privada del servicio.
- Truststore: almacena los certificados de confianza que permiten validar a otros servicios.

### Proceso general para generarlos

1. Crear una Autoridad Certificadora (CA) propia.
2. Generar un certificado para cada servicio con el nombre correspondiente.
3. Firmar esos certificados con la CA.
4. Exportarlos en formato PKCS12 para que puedan usarse como keystore.
5. Importar la CA al truststore de cada servicio para validar las conexiones TLS.

Un flujo básico puede ser:

```bash
openssl req -x509 -newkey rsa:4096 -days 365 -keyout ca-key.pem -out ca-cert.pem -subj "/CN=Store-CA"
```

Luego se genera el certificado del servicio, se firma con la CA y se convierte a PKCS12 para usarlo como keystore. El truststore se arma importando el certificado de la CA o los certificados firmados por ella.

En este proyecto, los archivos quedan organizados bajo la carpeta secret, separando los certificados de servicios, los certificados de la CA y las claves JWT.

## Ejecución con Docker Compose

### Requisitos previos

- Docker
- Docker Compose
- Java 21 (si se desea ejecutar algún servicio localmente)
- Maven

### Levantar la infraestructura

1. Crear un archivo .env con las variables necesarias para la base de datos, los puertos y los certificados.
2. Ejecutar:

```bash
docker compose up --build
```

### Puertos principales

- Gateway: http://localhost:9022
- Registry Server: http://localhost:9020
- Config Server: https://localhost:9021
- Auth: http://localhost:9023
- Products: http://localhost:9024
- Sales: http://localhost:9025
- Clients: http://localhost:9026

## Flujo de negocio principal

1. Un usuario inicia sesión en el servicio de autenticación.
2. El gateway enruta la solicitud al microservicio adecuado.
3. Los servicios validan permisos y, cuando corresponde, consultan información de otros dominios mediante OpenFeign.
4. Las operaciones de negocio se registran y se exponen mediante endpoints REST claros y seguros.

## Diferencias con la versión monolítica

Esta versión no solo reemplaza la arquitectura monolítica por una distribución de servicios, sino que además introduce:

- separación de dominios de negocio,
- descubrimiento de servicios,
- configuración centralizada,
- seguridad reforzada con TLS,
- mejor escalabilidad horizontal,
- un diseño más preparado para entornos productivos y futuras integraciones.

## Futuras mejoras

Como siguiente etapa de evolución, se propone trabajar en varias mejoras de arquitectura y operación:

- Asignación dinámica de permisos según el rol, de modo que los nuevos roles puedan recibir permisos específicos sin requerir cambios manuales en el código.
- Implementación de tareas asíncronas entre ms-sales y ms-products para asegurar la devolución de stock ante fallos de servicio o de comunicación, incluyendo una base de datos de pendientes con el estado de cada devolución.
- Sustituir el modelo actual de mTLS nativo por una estrategia basada en sidecars y Kubernetes, lo que facilitaría la gestión centralizada de identidad, certificados y política de red.
- Crear más endpoints administrativos para consultar estadísticas de ventas, indicadores operativos y reportes de negocio.
- Ampliar el monitoreo con más endpoints de Actuator y construir visualizaciones con Grafana para observar métricas de salud, rendimiento y comportamiento del sistema.

## Autoría

Proyecto desarrollado por:

- Raúl Ignacio Ramírez Sanhueza
- GitHub: @raul240sx
- Email: raul.ramirez1401@gmail.com
