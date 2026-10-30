# Laboratorio Spring Boot 2026 — Tecnológico de Antioquia
## Aplicaciones FullStack: Sistema de Reservas & Sistema Bancario con QR

Este repositorio contiene la solución completa al **Laboratorio Spring Boot con Asistente de IA (Cursor / Antigravity)** y al **Ejercicio Propuesto de Transferencias Bancarias con Código QR**, desarrollado para el **Tecnológico de Antioquia (2026)**.

---

## 🚀 Estructura del Repositorio

El proyecto se encuentra organizado de forma modular y desacoplada:

```
LABORATORIO 2/
├── .cursor/
│   └── rules/
│       └── springboot.mdc          # Reglas y convenciones de código para IA
├── backend-reservas/               # Proyecto 1: API REST Sistema de Reservas (apptdea)
│   ├── src/main/java/com/udea/apptdea/
│   │   ├── config/                 # Filtro CORS y configuración OpenAPI / Swagger
│   │   ├── controller/             # Endpoints /reservas (GET, POST, DELETE)
│   │   ├── dto/                    # Records Java (Request y Response)
│   │   ├── entity/                 # Entidad JPA Reservation y Enum Status
│   │   ├── exception/              # Excepciones de negocio y GlobalExceptionHandler
│   │   ├── repository/             # Spring Data JPA Repository
│   │   └── service/                # Lógica transaccional de reservas
│   ├── pom.xml
│   └── mvnw.cmd
├── backend-banco/                  # Proyecto 2: Ejercicio Propuesto (banktdea)
│   ├── src/main/java/com/udea/banktdea/
│   │   ├── config/                 # CORS, Swagger y Semilla de cuentas demo
│   │   ├── controller/             # Endpoints /api/banco (cuentas, transferencias, historial)
│   │   ├── dto/                    # TransferRequestDto, TransactionResponseDto, etc.
│   │   ├── entity/                 # Account, Transaction, TransactionStatus
│   │   ├── exception/              # Manejador de excepciones bancarias
│   │   ├── repository/             # AccountRepository, TransactionRepository
│   │   └── service/                # BankingService (@Transactional) y QrCodeService
│   ├── pom.xml
│   └── mvnw.cmd
├── frontend/                       # Aplicación Web Frontend en Angular
│   ├── src/app/
│   │   ├── components/
│   │   │   ├── reservas/           # Componente (.ts, .html, .css) de Reservas
│   │   │   └── banco/              # Componente (.ts, .html, .css) de Banco y QR
│   │   ├── models/                 # Modelos TypeScript
│   │   └── services/               # Servicios con Axios (ReservaService, BancoService)
│   └── package.json
├── INFORME_LABORATORIO_2.md        # Informe académico formal para entrega
└── PROMPTS_UTILIZADOS.md           # Bitácora de prompts de IA utilizados
```

---

## 🛠️ Stack Tecnológico

### Backend
- **Lenguaje:** Java 21 LTS / 25
- **Framework:** Spring Boot 3.2.4 / 4.x
- **Persistencia:** Spring Data JPA + Hibernate
- **Bases de Datos:**
  - **PostgreSQL:** Conexión nativa configurada en `application.properties`
  - **H2 (In-Memory Dev Profile):** Configurado para pruebas inmediatas con consola web (`/h2-console`)
- **Documentación de API:** SpringDoc OpenAPI 2.5.0 (Swagger UI interactivo)
- **Construcción y Dependencias:** Apache Maven 3.9+

### Frontend
- **Framework:** Angular 18/22+ (Componentes Standalone)
- **Lenguaje:** TypeScript 5.x / JavaScript ECMAScript 2024
- **Cliente HTTP:** Axios 1.7+ (integrado con Angular)
- **Estilos:** CSS3 modular con diseño responsivo y tipografía moderna

---

## ⚙️ Instrucciones de Ejecución

### 1. Iniciar el Backend de Reservas (Puerto 8080)
```powershell
cd "backend-reservas"
.\mvnw.cmd spring-boot:run
```
- **Documentación Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Consola H2 (en modo dev):** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:reservationtdea`, User: `postgres`, Password: `root`)

### 2. Iniciar el Backend Bancario (Puerto 8081)
```powershell
cd "backend-banco"
.\mvnw.cmd spring-boot:run
```
- **Documentación Swagger UI:** [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **Cuentas precargadas:** Inicializa automáticamente 4 cuentas bancarias con fondos para pruebas inmediatas.

### 3. Iniciar el Frontend en Angular (Puerto 4200)
```powershell
cd "frontend"
npm start
```
- Abre tu navegador en [http://localhost:4200](http://localhost:4200).
- Podrás alternar entre el módulo de **Gestión de Reservas** y el módulo de **Traslados Bancarios con Código QR** mediante la barra superior.

---

## 📋 Endpoints Principales

### Sistema de Reservas (`/reservas`)
| Método | Endpoint | Descripción | Códigos HTTP |
|---|---|---|---|
| `GET` | `/reservas` | Obtiene el listado completo de reservas | 200 OK |
| `POST` | `/reservas` | Registra una reserva validando disponibilidad horaria | 201 Created, 400 Bad Request, 409 Conflict |
| `DELETE` | `/reservas/{id}` | Cancela una reserva existente | 200 OK, 404 Not Found |

### Sistema Bancario (`/api/banco`)
| Método | Endpoint | Descripción | Códigos HTTP |
|---|---|---|---|
| `GET` | `/api/banco/cuentas` | Consulta todas las cuentas y sus saldos | 200 OK |
| `POST` | `/api/banco/transferencias` | Ejecuta un traslado atómico de dinero y genera QR | 201 Created, 400 Bad Request |
| `GET` | `/api/banco/transacciones` | Consulta el histórico ordenado cronológicamente | 200 OK |
| `GET` | `/api/banco/transacciones/{ref}` | Obtiene comprobante y QR por referencia única | 200 OK, 404 Not Found |

---

## 📦 Instrucciones para Subir a GitHub

Para publicar este proyecto en tu cuenta de GitHub, ejecuta los siguientes comandos desde la carpeta raíz:

```bash
# 1. Inicializar repositorio git local
git init

# 2. Agregar todos los archivos
git add .

# 3. Realizar el commit inicial
git commit -m "feat: Laboratorio 2 Spring Boot con Cursor y Ejercicio Propuesto Bancario TdeA"

# 4. Vincular con tu repositorio remoto de GitHub
git remote add origin https://github.com/TU_USUARIO/laboratorio-springboot-tdea.git

# 5. Enviar el código a la rama principal
git branch -M main
git push -u origin main
```
