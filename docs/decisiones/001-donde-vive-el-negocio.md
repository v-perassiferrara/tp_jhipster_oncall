# ADR 001: Dónde vive el negocio

## Contexto
OnCall es un monolito JHipster 9.2.0 generado desde `oncall.jh`.
El generador administra entidades, ABM, DTO, mappers, pantallas y migraciones.
La extensión de costo de la interrupción ya tiene sus tres entidades generadas.
Necesitamos agregar reglas sin perderlas al regenerar el modelo.

## Decisión
El negocio nuevo y sus pruebas viven en subpaquetes propios `oncall/`:
`service/oncall/`, `repository/oncall/` y `web/rest/oncall/`, bajo
`src/main/java/ar/edu/um/isa/oncall/`, y rutas equivalentes en `src/test/java/`.
Los endpoints nuevos usan `/api/oncall/`. Se reutilizan los componentes generados
por composición; no se inserta negocio dentro del ABM.
Los cambios al modelo parten de `oncall.jh`; una regeneración se revisa por diff.

Como antecedente, incorporamos sin reubicar la deduplicación de la rama docente
`02---Clase-III---Reglas-de-Negocio`, commit `d81249a`.
Sus clases propias están fuera de esos subpaquetes, pero separadas de los archivos
ABM generados. Esta excepción conserva la referencia de clase y no es el patrón
para la regla C. Su endpoint sigue siendo `POST /api/alertas/ingesta`.

## Consecuencias
El negocio nuevo queda separado del territorio del generador y requiere clases
adicionales. Las regeneraciones deben revisarse antes de aceptarse.
Se mantienen las pruebas docentes de deduplicación y se agregan pruebas propias
para cada nueva regla. No se asume que una copia de archivos demuestra que compila:
es necesario ejecutar las verificaciones y registrar sus resultados.
La deduplicación heredada conserva su limitación de concurrencia documentada
por el profesor; esta integración no la modifica.

## Alternativas
- Editar servicios y controladores generados: descartado por riesgo de sobrescritura.
- Congelar el generador: descartado porque impide evolucionar el modelo.
- Reubicar ahora toda la deduplicación: descartado para minimizar la integración.
- Reemplazar el proyecto personal por la copia docente: descartado porque ya existe
  una extensión generada que queremos conservar.
