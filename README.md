# Meteorología Mock API

Una aplicación Mock desarrollada con Quarkus que expone servicios REST para datos meteorológicos simulados, completamente documentada con OpenAPI 3.0.2+.

## 🌦️ Características

- **API REST completa** para datos meteorológicos
- **Datos mockeados** realistas para desarrollo y testing
- **Documentación OpenAPI 3.0.2+** integrada
- **Swagger UI** incluido para explorar la API
- **Validación de parámetros** con Bean Validation
- **Manejo de errores** estructurado
- **CORS** habilitado para desarrollo frontend
- **Compilación nativa** con GraalVM

## 🚀 Inicio Rápido

### Prerrequisitos

- Java 17+
- Maven 3.8+
- (Opcional) GraalVM para compilación nativa

### Ejecutar en modo desarrollo

```bash
./mvnw compile quarkus:dev
```

La aplicación estará disponible en:
- **API Base**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/swagger-ui
- **OpenAPI Spec**: http://localhost:8080/q/openapi

### Compilar para producción

```bash
./mvnw package
java -jar target/meteorologia-mock-api-1.0.0-SNAPSHOT-runner.jar
```

### Compilación nativa (opcional)

```bash
./mvnw package -Pnative
./target/meteorologia-mock-api-1.0.0-SNAPSHOT-runner
```

## 📡 Endpoints Principales

### Weather API (`/api/weather`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/current/{stationId}` | Clima actual por ID de estación |
| GET | `/current?lat={lat}&lon={lon}` | Clima actual por coordenadas |
| GET | `/forecast/{stationId}?days={days}` | Pronóstico por estación |
| GET | `/forecast?lat={lat}&lon={lon}&days={days}` | Pronóstico por coordenadas |
| GET | `/health` | Estado del servicio |

### Stations API (`/api/stations`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Todas las estaciones |
| GET | `/{id}` | Estación por ID |
| GET | `/search/city/{city}` | Estaciones por ciudad |
| GET | `/search/country/{country}` | Estaciones por país |
| GET | `/search?filters` | Búsqueda con filtros múltiples |
| GET | `/nearby?lat={lat}&lon={lon}` | Estaciones cercanas |
| GET | `/statistics` | Estadísticas de estaciones |

## 🔧 Ejemplos de Uso

### Obtener clima actual

```bash
# Por ID de estación
curl "http://localhost:8080/api/weather/current/STATION_001"

# Por coordenadas
curl "http://localhost:8080/api/weather/current?lat=40.4168&lon=-3.7038"
```

### Obtener pronóstico

```bash
# 7 días por estación
curl "http://localhost:8080/api/weather/forecast/STATION_001?days=7"

# 5 días por coordenadas
curl "http://localhost:8080/api/weather/forecast?lat=40.4168&lon=-3.7038&days=5"
```

### Buscar estaciones

```bash
# Todas las estaciones
curl "http://localhost:8080/api/stations"

# Por ciudad
curl "http://localhost:8080/api/stations/search/city/Madrid"

# Estaciones cercanas
curl "http://localhost:8080/api/stations/nearby?lat=40.4168&lon=-3.7038&radius=50"
```

## 📊 Modelos de Datos

### WeatherData
Datos meteorológicos actuales incluyendo:
- Temperatura, sensación térmica, humedad
- Presión atmosférica, viento, visibilidad
- Índice UV, descripción del clima
- Precipitación en 24h, coordenadas

### WeatherForecast
Pronóstico diario con:
- Temperaturas máxima y mínima
- Probabilidad y cantidad de precipitación
- Datos de viento, índice UV
- Horarios de amanecer y atardecer

### WeatherStation
Información de estaciones:
- Identificación, ubicación, coordenadas
- Tipo, estado operativo
- Organización responsable, contacto

## ⚙️ Configuración

El archivo `application.properties` incluye:

```properties
# Puerto del servidor
quarkus.http.port=8080

# Configuración OpenAPI
quarkus.smallrye-openapi.info-title=Meteorología Mock API
quarkus.smallrye-openapi.info-version=1.0.0
quarkus.smallrye-openapi.info-description=API Mock para servicios meteorológicos

# Swagger UI
quarkus.swagger-ui.always-include=true
quarkus.swagger-ui.path=/swagger-ui

# CORS habilitado
quarkus.http.cors=true
```

## 🧪 Datos Mock

La aplicación incluye 5 estaciones meteorológicas predefinidas:
- **STATION_001**: Madrid Centro
- **STATION_002**: Barcelona Puerto  
- **STATION_003**: Sevilla Aeropuerto
- **STATION_004**: Bilbao Ciudad
- **STATION_005**: Valencia Playa

Los datos meteorológicos se generan aleatoriamente pero de forma realista, considerando:
- Variaciones de temperatura por latitud
- Condiciones climáticas coherentes
- Direcciones de viento con descripción
- Rangos válidos para todos los parámetros

## 🔍 Documentación OpenAPI

La documentación completa está disponible en:
- **Formato JSON**: `/q/openapi`
- **Interfaz visual**: `/swagger-ui`

Incluye:
- Descripción detallada de cada endpoint
- Esquemas de todos los modelos de datos
- Ejemplos de peticiones y respuestas
- Códigos de error y sus significados
- Validaciones de parámetros

## 🛠️ Tecnologías Utilizadas

- **Quarkus 3.6.4** - Framework principal
- **RESTEasy Reactive** - API REST
- **Jackson** - Serialización JSON
- **SmallRye OpenAPI** - Documentación API
- **Hibernate Validator** - Validación de datos
- **Maven** - Gestión de dependencias

## 📈 Características Técnicas

### Validación
- Validación de coordenadas geográficas
- Rangos válidos para parámetros meteorológicos
- Validación de tipos de datos

### Manejo de Errores
- Respuestas de error estructuradas
- Códigos HTTP apropiados
- Mensajes descriptivos en español

### Rendimiento
- Inicio rápido con Quarkus
- Compilación nativa opcional
- Consumo mínimo de memoria

### Seguridad
- Headers de seguridad incluidos
- CORS configurado apropiadamente
- Validación de entrada exhaustiva

## 🤝 Contribuir

1. Fork del proyecto
2. Crear rama para nueva funcionalidad
3. Commit de cambios
4. Push a la rama
5. Abrir Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia Apache 2.0. Ver archivo `LICENSE` para más detalles.

## 📞 Soporte

Para soporte y consultas:
- Email: support@meteorologia.example.com
- Documentación: Swagger UI integrado
- Issues: GitHub del proyecto