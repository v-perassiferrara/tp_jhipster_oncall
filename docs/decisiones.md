# Decisiones y verificación: regla C

## Alcance
Extensión 3, costo de la interrupción. Esta entrega implementa la regla C:
resolver quién está de guardia para un servicio y un instante.
Las entidades de costos ya estaban generadas. El cálculo monetario por horario
y el reporte mensual todavía no están implementados.

## Suposiciones
- El equipo del servicio determina las rotaciones aplicables; solo cuentan las activas.
- Se devuelve un resultado por rotación, sin inventar jerarquías primario/secundario.
- Los turnos incluyen su inicio y excluyen su fin: `[desde, hasta)`.
- Un reemplazo prevalece sobre un turno regular. Después gana el inicio más reciente
  y, si empatan, el mayor ID. Un reemplazo nulo equivale a regular.
- Un hueco se informa como `SIN_COBERTURA`, sin asignar un respaldo ni producir error.
  Sin rotaciones activas, la colección queda vacía. `hayCobertura` indica que al menos
  una rotación tiene guardia; el detalle permite detectar cobertura parcial.
- Los instantes se comparan en UTC y requieren Z u offset explícito al consultar.
  `horaMendoza` muestra el mismo instante en `America/Argentina/Mendoza`.
  Una hora local repetida por DST se distingue por su offset. No se generan recurrencias.
- La consulta es de solo lectura y no ejecuta escalamiento.

Estas decisiones se justifican en el ADR 002. La separación del código generado
se explica en el ADR 001. No se cambiaron el JDL, los ABM ni las dependencias.

## Uso
Endpoint autenticado: `GET /api/oncall/servicios/{servicioId}/guardia`.
Parámetro obligatorio: `instante`, por ejemplo `2026-11-05T09:00:00-03:00`.
La respuesta identifica rotación, turno y responsable (ID y login), sin datos de cuenta.
Una consulta sin cobertura devuelve HTTP 200; un servicio inexistente, 404;
una fecha inválida, sin offset, o un ID no positivo, 400. Sin autenticación devuelve 401.
Si se usa un offset positivo en una URL, debe codificarse el signo `+` (o usar Z).

## Verificación realizada
El 5 de noviembre de 2026 compilamos desde cero con JDK 25 y ejecutamos
41 pruebas, todas aprobadas y ninguna omitida:
- 18 de deduplicación del profesor, para detectar regresiones en esa regla.
- 8 unitarias de regla C: prioridad, desempates independientes del orden de consulta,
  reemplazo nulo, huecos, múltiples rotaciones, servicio inexistente y hora de Mendoza.
- 15 de integración con HTTP, seguridad, consultas JPQL y PostgreSQL 18.4 en Docker:
  bordes, relevo, aislamiento entre equipos, exclusión de rotaciones inactivas,
  cobertura simultánea/parcial, ausencia de rotaciones, offsets equivalentes,
  hora repetida por DST, consultas inválidas y autenticación.

Para repetirlas, con JDK 21 o 25, dependencias disponibles y Docker funcionando:

```sh
./mvnw -P-webapp -Dspring.profiles.active=testdev \
  -Dtest=TurnoVigenteServiceTest,GuardiaVigenteResourceIT,DeduplicacionDeAlertasServiceTest test
```

Los informes quedan en `target/surefire-reports/`. Durante la primera compilación
se detectó un mapper inválido en `target`; una compilación limpia lo regeneró.
No se modificó ese mapper ni ningún archivo fuente generado.
No se ejecutó toda la suite del proyecto ni se verificaron las pantallas del frontend.
