# ADR 002: Zona horaria y turnos superpuestos

## Contexto
La regla C responde quién está de guardia para un servicio en un instante dado.
El servicio pertenece a un equipo, que puede tener varias rotaciones activas.
Los turnos concretos tienen `desde`, `hasta` (Instant), `esReemplazo` y responsable.
La rotación tiene zona horaria, pero el modelo no distingue primario y secundario.
Necesitamos resolver bordes, reemplazos y huecos sin depender del orden de una consulta.
Estas decisiones se acordaron antes de implementar la regla.

## Decisión
- La consulta recibe servicio e instante con zona u offset explícito. Se compara
  con `Instant` en UTC; no se interpreta una fecha/hora local sin offset.
  Para mostrar horarios usamos `America/Argentina/Mendoza`, no la zona del servidor.
  Los turnos ya almacenados como instantes no se reinterpretan usando `zonaHoraria`.
  No generamos turnos recurrentes ni convertimos aquí horarios locales ambiguos por DST.
- Se consideran las rotaciones activas del equipo al que pertenece el servicio.
  Se devuelve un resultado por rotación, sin inventar prioridades entre ellas.
- El intervalo es `[desde, hasta)`: incluye el comienzo y excluye el final.
  A las 09:00 un turno que termina a esa hora ya no cubre; uno que empieza sí.
- Dentro de cada rotación gana un reemplazo frente a un turno regular.
  `esReemplazo = null` equivale a regular. Entre candidatos del mismo tipo gana
  el de inicio más reciente; si empatan, el de mayor ID. El ID solo desempata:
  no representa una fecha de modificación.
- Una rotación sin turno vigente se informa como `SIN_COBERTURA`, sin responsable.
  Si no existen rotaciones activas se devuelve una colección vacía y sin cobertura.
  La falta de cobertura es una respuesta normal, no una excepción.
- La respuesta identifica rotación, turno y responsable cuando existe cobertura.
  No se exponen entidades User completas ni datos de cuenta sensibles.
- No se ejecuta escalamiento ni se elige un responsable del equipo como respaldo.
  La consulta es de solo lectura y no crea ni modifica turnos.

## Consecuencias
El resultado es determinista y admite varias guardias simultáneas.
En los cambios de turno no se duplica la cobertura por compartir el mismo extremo.
Las diferencias de zona y horario de verano no cambian la comparación de instantes;
la conversión de una hora local al cargar turnos queda fuera de esta regla.
El desempate permite responder ante un solapamiento, pero no corrige la planificación.
La regla C será una base para costos por horario; todavía no define tarifas,
redondeos, cálculo monetario ni reporte mensual.

Antes de dar la implementación por terminada verificaremos con pruebas:
- Inicio inclusivo, fin exclusivo y relevo entre dos turnos consecutivos.
- Reemplazo frente a regular, inicio más reciente y desempate por ID.
- Hueco de cobertura, ausencia de rotaciones y rotaciones inactivas.
- Varias rotaciones, aislamiento entre equipos y servicios.
- El mismo instante expresado con distintos offsets, incluido un cambio de DST.
- Respuesta del endpoint para cobertura y falta de cobertura.
Estas pruebas están pendientes: este ADR registra la especificación, no resultados.

## Alternativas
- Elegir el primer turno que devuelva la base: descartado por falta de determinismo.
- Rechazar todo solapamiento: descartado porque los reemplazos deben prevalecer.
- Devolver todos los turnos superpuestos de una rotación: descartado por ambigüedad.
- Exigir una sola rotación activa: descartado porque el modelo admite varias.
- Usar hora local del servidor o extremos ambos inclusivos: descartado por ambigüedades.
- Resolver escalamiento o asignar un responsable por defecto: descartado por alcance.
