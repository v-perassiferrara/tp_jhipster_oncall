# OnCall: guía para agentes
Sistema de guardias e incidentes. JHipster 9.2.0, Spring Boot 4, Angular 21 y PostgreSQL.
Usá JDK 21 o 25; el JDK 17 de la terminal no sirve para este proyecto.

## Antes de cambiar
Leé `oncall.jh` y `docs/decisiones/`. El ADR 002 define la regla C.
Trabajá en la rama de la tarea y revisá el estado de Git; conservá los cambios ajenos.

## Territorios
Las rutas Java siguientes parten de `src/main/java/ar/edu/um/isa/oncall/`.
- GENERADO: no edites a mano `domain/`, repositorios y servicios ABM, `service/dto/`,
  `service/mapper/`, controladores ABM de `web/rest/` ni `src/main/webapp/app/entities/`.
- COMPARTIDO: usá los `jhipster-needle` para insertar código donde existan;
  limitá los cambios de configuración a lo necesario y revisá el diff.
- NUESTRO: agregá negocio en `service/oncall/`, `repository/oncall/` y `web/rest/oncall/`;
  poné sus DTO en `service/oncall/dto/` y sus pruebas en subpaquetes equivalentes.
- Conservá la deduplicación docente existente fuera de esos paquetes (ADR 001).

## Comandos desde la raíz
- Backend: `./mvnw`. Frontend: `npm start` (requieren entorno y dependencias del README).
- Compilar backend: `./mvnw -P-webapp compile`.
- Prueba docente: `./mvnw -P-webapp -Dtest=DeduplicacionDeAlertasServiceTest test`.
- Regla C: `./mvnw -P-webapp -Dspring.profiles.active=testdev -Dtest=TurnoVigenteServiceTest,GuardiaVigenteResourceIT test` (Docker activo).
- Revisar cambios: `git diff --check` y `git diff`.

## Reglas
Modificá el modelo en `oncall.jh`; no ejecutes `jhipster jdl` sin pedido explícito.
No agregues dependencias para resolver lo que ya ofrecen Java y Spring.
Toda regla nueva lleva pruebas de negocio, incluidos sus casos borde.
Usá endpoints propios bajo `/api/oncall/`; no cambies el comportamiento del ABM.
Para la regla C, no implementes escalamiento ni precios por horario sin definir su alcance.
Al terminar, informá qué cambió, qué pruebas corriste y qué quedó sin verificar.
