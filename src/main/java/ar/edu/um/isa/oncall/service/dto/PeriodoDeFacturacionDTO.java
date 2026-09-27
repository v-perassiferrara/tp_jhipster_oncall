package ar.edu.um.isa.oncall.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.PeriodoDeFacturacion} entity.
 */
@Schema(description = "Periodo utilizado para agrupar los costos de los incidentes.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PeriodoDeFacturacionDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

    @NotNull
    private Boolean cerrado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getCerrado() {
        return cerrado;
    }

    public void setCerrado(Boolean cerrado) {
        this.cerrado = cerrado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeriodoDeFacturacionDTO)) {
            return false;
        }

        PeriodoDeFacturacionDTO periodoDeFacturacionDTO = (PeriodoDeFacturacionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, periodoDeFacturacionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeriodoDeFacturacionDTO{" +
            "id=" + getId() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", cerrado='" + getCerrado() + "'" +
            "}";
    }
}
