# Registro de Prompts Utilizados en el Laboratorio 2
**Institución:** Tecnológico de Antioquia  
**Materia:** Aplicaciones FullStack  
**Fecha:** 30 de Octubre de 2026  
**Herramienta de Asistencia con IA:** Cursor / Antigravity IDE  

---

## 1. Configuración de Reglas del Proyecto (Cursor MDC)

```markdown
Crea un archivo de reglas para cursor (mdc) llamado 'springboot' que 
siempre se aplique, con las buenas practicas de Java 25 en el desarrollo 
de un servicio en spring boot para que las aplique en el desarrollo del 
proyecto.
Ten en cuenta que los nombres de clases, métodos y atributos deben ser en 
ingles.
Cada método público debe tener su documentación Javadoc actualizada.
Para los DTO usamos java records.
Agrega la estructura actual del proyecto dentro de las reglas.
```

---

## 2. Configuración de Base de Datos y `application.properties`

```text
Crea la configuración para la conexión a la base de datos postgresql de 
nombre 'reservationtdea' con el usuario 'postgres' y contraseña 'root'.
Habilita la actualización del esquema automático en hibernate, el 
dialecto JPA e imprime las sentencias sql de JPA en consola.
Incluye además un perfil 'dev' con H2 en memoria compatible con PostgreSQL para 
ejecución inmediata de pruebas sin requerir servicios externos activos.
```

---

## 3. Generación de la Capa de Entidades (Entity Beans)

```text
Crea una entidad JPA que represente una reserva del sistema.
Debe tener los siguientes atributos (ajusta los nombres):
- id: Long, clave primaria autogenerada
- customerName: String
- date: LocalDate
- time: LocalTime
- service: String
- ReservationStatus: un enum con valores ACTIVA y CANCELADA
Generar getters, setters y constructores.
Usa las anotaciones JPA correctas y comentarios Javadoc completos.
```

---

## 4. Generación del Repositorio Spring Data JPA

```text
Crea un repositorio Spring Data JPA para la entidad Reserva.
Debe extender JpaRepository y tener un método que permita verificar si ya 
existe una reserva para una fecha y una hora específicas.
Incluye validación que excluya reservas canceladas.
```

---

## 5. Generación de la Capa de Servicio (Service)

```text
Crea una clase ReservaService que contenga la lógica de negocio del 
sistema.
Debe:
- Crear una reserva solo si no existe otra reserva en la misma fecha y 
hora
- Cancelar una reserva por su id
- Lanzar una excepción personalizada cuando se violen las reglas de 
negocio (BusinessException con código HTTP 409 Conflict)
- Listar todas las reservas existentes
```

---

## 6. Generación de la Capa de Controladores (Controller) y Swagger

```text
Crea un controlador REST para la entidad Reserva.
Debe exponer los siguientes endpoints:
- GET /reservas → listar todas las reservas
- POST /reservas → crear una nueva reserva
- DELETE /reservas/{id} → cancelar una reserva
Devuelve códigos de estado HTTP adecuados en cada caso (201 Created, 200 OK, 409 Conflict, 404 Not Found).
Configura SpringDoc OpenAPI / Swagger para documentar todos los endpoints.
```

---

## 7. Configuración de Seguridad y Filtro CORS

```text
Crea una clase Configuration para crear un filtro de CORS que se aplicará 
a todas las rutas (/**). Le indica a Spring Boot que debe aceptar 
peticiones del origen http://localhost:3000, http://localhost:4200 y localhost en general,
permitiendo todos los métodos (GET, POST, PUT, DELETE, OPTIONS) y 
cabeceras necesarios para la comunicación.
```

---

## 8. Generación del Frontend (Servicios y Vistas en Angular)

```text
Usando @.cursor/rules/springboot.mdc como regla, crea un servicio 
llamado ReservaService.
Debe:
- Usar AXIOS
- Tener métodos para:
  - Obtener todas las reservas
  - Crear una reserva
  - Cancelar una reserva
- Usar una variable de entorno para la URL del backend

Crea las vistas separando los 3 archivos del componente (.ts, .html y .css):
1. Formulario reactivo con: nombreCliente, fecha, hora, servicio (lista desplegable recomendada). Todos los campos obligatorios.
2. Toast para mostrar mensajes de confirmación y errores de conflicto (409).
3. Tabla dinámica con listado de reservas, badges de estado ACTIVE / CANCELADA y botón Cancelar.
```

---

## 9. Ejercicio Propuesto: Sistema Bancario con QR e Historial

```text
Siguiendo las buenas prácticas y arquitectura en capas del laboratorio anterior:
1. Crea un microservicio Spring Boot para un sistema bancario:
   - Entidad Cuenta (Account): número de cuenta, titular, saldo, tipo, estado.
   - Entidad Transacción (Transaction): referencia única, cuenta origen, cuenta destino, monto, fecha/hora, estado, código QR.
   - Servicio transaccional (@Transactional) que valide saldos suficientes, existencia de cuentas, debite origen y acredite destino de forma atómica.
   - Generación de código QR con los datos esenciales de la transferencia codificados en formato JSON y renderizados en SVG/imagen.
   - Endpoints REST para listar cuentas, ejecutar traslados y consultar el histórico de transacciones.
2. Crea el módulo Frontend en Angular (separado en .ts, .html y .css):
   - Resumen visual de cuentas y saldos en tiempo real.
   - Formulario de traslado de dinero entre cuentas.
   - Comprobante digital emergente (Modal) con el código QR escaneable desde dispositivos móviles.
   - Historial detallado de transferencias bancarias.
```
