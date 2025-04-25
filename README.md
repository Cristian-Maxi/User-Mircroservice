# 🧩 Proyecto de Microservicios con Spring Boot

Este proyecto es una implementación de una arquitectura de microservicios basada en Spring Boot. Utiliza tecnologías modernas como Redis, RabbitMQ y PostgreSQL para resolver problemas empresariales comunes de manera escalable, eficiente y modular.

## 🔧 Tecnologías Utilizadas

- Java 21
- Spring Boot 3.4.4
- Spring Security + JWT
- Maven
- PostgreSQL
- Redis
- RabbitMQ
- Eureka Server (Service Discovery)
- Spring Cloud Gateway
- MapStruct
- Spring Data JPA
- Spring Validation
- Java Mail Sender
- ITextPDF
- Mailtrap
- Docker / Podman
- Postman / DBBeaver
- Open Api (Swagger)
- Junit / Mockito
- Jacoco + informe en HTML
- SonarQube (Revisión de cobertura)

---

## 📌 Módulo: Usuarios (Users)

Este módulo gestiona la creación y autenticación de usuarios, proporcionando una API segura para el manejo de sesiones, permisos y validaciones. Implementa roles, control de acceso, y soft delete para mantener la integridad de los datos.

### ⚙️ Características Técnicas

- Registro de usuarios con validación
- Autenticación con JWT
- Inicio de sesión con Google OAuth2
- Gestión de roles y permisos
- Soft delete (`activo` true/false)
- Uso de `MapStruct` para mapeo entre entidades y DTOs
- Respuestas estructuradas con `ApiResponseDTO`
- Envío de correos con `Mailtrap` (Java Mail Sender)
- Manejo centralizado de errores y excepciones

---

## 🔁 Endpoints – `UserEntityController - AuthController`

| Método | Endpoint                 | Descripción                                  |
|--------|--------------------------|----------------------------------------------|
| POST   | `/api/auth/create`       | Registro de nuevo usuario                    |
| POST   | `/api/auth/login`        | Inicio de sesión (JWT)                       |
| GET    | `/api/user/getAll`       | Listar todos los usuarios activos            |
| PUT    | `/api/user/update`       | Actualizar datos del usuario                 |
| DELETE | `/api/user/delete/{id}`  | Baja lógica del usuario                      |

---

## 🔄 Flujo General
```
Client (Frontend / Postman)
   ⬇
UserController (Validaciones de entrada)
   ⬇
UserService (Lógica de negocio)
   ⬇
MapStruct Mapper (DTO ↔ Entity)
   ⬇
UserRepository (Spring Data JPA)
   ⬇
Base de Datos PostgreSQL
```
---

## 🚀 Levantar el Proyecto con Podman Compose

### 🔸 Pre-requisitos

- Tener instalado `Podman` y `Podman Compose`.
- Crear un archivo `.env` con las siguientes variables:
```.env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=myappdb

SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

SECRET_KEY=B374A26A71421437AA024E4FADD5B478FDFF1A8EA6FF12F6FB65AF2720B59CCF
```

- docker-compose.yml:

```
version: '3.8'

services:
  postgres:
    image: postgres:16
    container_name: springboot_postgres
    restart: always
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_DB: ${POSTGRES_DB}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - spring-network

  redis:
    image: redis:7.2
    container_name: redis_cache
    restart: always
    ports:
      - "6379:6379"
    networks:
      - spring-network

  rabbitmq:
    image: rabbitmq:3-management
    container_name: rabbitmq
    restart: always
    ports:
      - "5672:5672"   # Puerto de conexión para microservicios
      - "15672:15672" # Puerto de panel web de administración
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest
    networks:
      - spring-network

  eureka:
    build: ./eureka_server
    container_name: eureka_server
    ports:
      - "8761:8761"
    networks:
      - spring-network

  gateway:
    build: ./cloud-gateway
    container_name: gateway_service
    ports:
      - "8080:8080"
    depends_on:
      - eureka
    environment:
      - SECRET_KEY=${SECRET_KEY}
    networks:
      - spring-network

  pointsalecost:
    build: ./Point_of_Sale_Cost-Microservice
    container_name: pointsalecost_service
    depends_on:
      - postgres
      - redis
      - eureka
    environment:
      - SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
      - SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}
    networks:
      - spring-network

  accreditations:
    build: ./Accreditation-Microservice
    container_name: accreditations_service
    depends_on:
      - postgres
      - redis
      - eureka
      - rabbitmq
    environment:
      - SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
      - SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}
    networks:
      - spring-network

  usermicroservice:
    build: ./Users-Microservice
    container_name: usermicroservice_service
    depends_on:
      - postgres
      - redis
      - eureka
      - rabbitmq
    environment:
      - SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
      - SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}
      - SECRET_KEY=${SECRET_KEY}
    networks:
      - spring-network

  emailrabbitmq:
    build: ./emailRabbitMQ-Microservice
    container_name: email_rabbitmq_service
    depends_on:
      - rabbitmq
    environment:
      - SECRET_KEY=${SECRET_KEY}
    ports:
      - "8084:8084"
    networks:
      - spring-network

volumes:
  postgres_data:

networks:
  spring-network:
    driver: bridge

```

- Comando para construir y levantar con Podman Compose:
  `podman compose up --build`

## ✅ Estructura de la Carpeta

![Estructura de carpetas](https://github.com/user-attachments/assets/b6ff7ad2-9a19-40d1-93d3-4d98b37054b8)


# ⚙️ Test unitarios

## Jacoco + informe en HTML

![user JaCoCo test](https://github.com/user-attachments/assets/83670922-0ef4-4a20-915c-542aa102c5ca)


##  Sonar Qube

![Sonar Qube user New Code](https://github.com/user-attachments/assets/19d2389d-a982-4e32-83f4-117098990d76)

![Sonar Qube user Overall Code](https://github.com/user-attachments/assets/8178d77f-deda-4267-a386-358337bd4de8)


## 🧩 Swagger | Open API


Endpoint swagger: http://localhost:8080/swagger-ui/index.html

![User Swagger](https://github.com/user-attachments/assets/7958d8d7-313f-406e-b3c8-e479ee54d22e)
