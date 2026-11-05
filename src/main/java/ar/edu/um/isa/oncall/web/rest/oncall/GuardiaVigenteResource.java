package ar.edu.um.isa.oncall.web.rest.oncall;

import ar.edu.um.isa.oncall.service.ServicioInexistenteException;
import ar.edu.um.isa.oncall.service.oncall.TurnoVigenteService;
import ar.edu.um.isa.oncall.service.oncall.dto.GuardiaVigenteDTO;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/oncall/servicios")
public class GuardiaVigenteResource {
    private final TurnoVigenteService turnos;

    public GuardiaVigenteResource(TurnoVigenteService turnos) {
        this.turnos = turnos;
    }

    @GetMapping("/{servicioId}/guardia")
    public GuardiaVigenteDTO consultar(@PathVariable Long servicioId, @RequestParam String instante) {
        if (servicioId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El servicioId debe ser positivo");
        }
        try {
            return turnos.consultar(servicioId, OffsetDateTime.parse(instante).toInstant());
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usá una fecha ISO 8601 con Z u offset explícito", e);
        } catch (ServicioInexistenteException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
