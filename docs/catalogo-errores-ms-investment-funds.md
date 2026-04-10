# Tabla de Endpoints - MS-INVESTMENT-FUNDS

## Resumen
Base Path: `/investment-funds`

---

| # | Método HTTP | Endpoint | Versión API                         | HTTP Code | Errores de Negocio |
|---|-------------|----------|-------------------------------------|-----------|-------------------|
| 1 | GET         | `/investment-funds` | Sin versión / 1.0.0 / 3.0.0         | 200, 400, 500 | No aplica |
| 2 | GET         | `/investment-funds/metadata` | Sin versión / 1.0.0 / 2.0.0 / 3.0.0 | 200, 400, 500 | No aplica |
| 3 | POST        | `/investment-funds/{number}/alias` | Sin versión / 1.0.0 / 3.0.0         | 200, 400, 403, 500 | `IFNT0001` (403) - Investment fund not found<br>`IFNT0002` (500) - Error inserting alias |

---

## Errores Genéricos (ResponseCodeEnum.java)
- `IFNT0001` - Investment fund not found
- `IFNT0002` - Error inserting alias

---

## Notas Importantes

1. **Versionado de APIs**: El microservicio soporta versionado mediante el header `accept-version`. Las versiones disponibles son:
   - Sin versión (default)
   - `1.0.0`
   - `2.0.0` (Esta versión se utiliza para que el método `/investment-funds/metadata` realice la consulta solo a Open FGA)
   - `3.0.0` (Esta versión se utiliza para habilitar el encriptado de datos sensibles)

2. **Base Path**: Configurado en `spring.webflux.base-path` como `/investment-funds`.

3. **Parámetros requeridos**:
   - GET `/investment-funds`: Requiere header `channel` (Procedencia: iOS, Android)
   - GET `/investment-funds/metadata` - versiones **`sin versión`** y **`1.0.0`** : Requiere header `channel` (Procedencia: iOS, Android)
   - GET `/investment-funds/metadata` - versiones **`2.0.0`** y **`3.0.0`** : No requieren header `channel`
   - POST `/investment-funds/{number}/alias`: Requiere parámetros:
     - Path: `number` (identificador del fondo de inversión)
     - Query: `country` (código de país)
     - Header: `channel` (Procedencia: iOS, Android)
     - Body: JSON con el alias a crear o actualizar

4. **Códigos HTTP de respuesta**:
   - `200 OK`: Solicitud exitosa
   - `400 Bad Request`: Validación fallida en los parámetros de entrada
   - `403 Forbidden`: El usuario no es propietario del fondo de inversión (IFNT0001)
   - `500 Internal Server Error`: Error interno del servidor o error inserting alias (IFNT0002)

5. **Seguridad y Autorización**: 
   - El microservicio utiliza OpenFGA para autorización basada en acceso abierto
   - Valida que el usuario sea propietario del fondo de inversión antes de permitir operaciones

6. **Integraciones**:
   - DataPower: Para obtener los datos de fondos de inversión del servicio remoto
   - OAuth: Para obtener tokens de autenticación en ambiente de producción
   - Alias Manager: Para gestionar los alias de los fondos de inversión

---

**Generado**: 2026-03-06
**Microservicio**: bancadigital-ms-investment-funds
**Framework**: Spring WebFlux
**Java Version**: 17+

