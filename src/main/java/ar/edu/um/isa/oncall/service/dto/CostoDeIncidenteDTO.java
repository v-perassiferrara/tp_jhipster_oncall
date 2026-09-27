package ar.edu.um.isa.oncall.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.CostoDeIncidente} entity.
 */
@Schema(
    description = "Costo de un incidente para un servicio afectado,\ncalculado a partir de la tarifa de dicho servicio\ny el tiempo que estuvo degradado."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CostoDeIncidenteDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer minutosCaidos;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal montoTotal;

    private Instant calculadoEn;

    @NotNull
    private IncidenteDTO incidente;

    @NotNull
    private ServicioDTO servicio;

    @NotNull
    private PeriodoDeFacturacionDTO periodo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinutosCaidos() {
        return minutosCaidos;
    }

    public void setMinutosCaidos(Integer minutosCaidos) {
        this.minutosCaidos = minutosCaidos;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Instant getCalculadoEn() {
        return calculadoEn;
    }

    public void setCalculadoEn(Instant calculadoEn) {
        this.calculadoEn = calculadoEn;
    }

    public IncidenteDTO getIncidente() {
        return incidente;
    }

    public void setIncidente(IncidenteDTO incidente) {
        this.incidente = incidente;
    }

    public ServicioDTO getServicio() {
        return servicio;
    }

    public void setServicio(ServicioDTO servicio) {
        this.servicio = servicio;
    }

    public PeriodoDeFacturacionDTO getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodoDeFacturacionDTO periodo) {
        this.periodo = periodo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CostoDeIncidenteDTO)) {
            return false;
        }

        CostoDeIncidenteDTO costoDeIncidenteDTO = (CostoDeIncidenteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, costoDeIncidenteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CostoDeIncidenteDTO{" +
            "id=" + getId() +
            ", minutosCaidos=" + getMinutosCaidos() +
            ", montoTotal=" + getMontoTotal() +
            ", calculadoEn='" + getCalculadoEn() + "'" +
            ", incidente=" + getIncidente() +
            ", servicio=" + getServicio() +
            ", periodo=" + getPeriodo() +
            "}";
    }
}
