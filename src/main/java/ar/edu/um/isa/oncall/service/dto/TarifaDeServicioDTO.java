package ar.edu.um.isa.oncall.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.TarifaDeServicio} entity.
 */
@Schema(description = "Tarifa asociada a un servicio.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TarifaDeServicioDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal costoPorMinuto;

    @NotNull
    private Instant vigenteDesde;

    private Instant vigenteHasta;

    @NotNull
    private ServicioDTO servicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCostoPorMinuto() {
        return costoPorMinuto;
    }

    public void setCostoPorMinuto(BigDecimal costoPorMinuto) {
        this.costoPorMinuto = costoPorMinuto;
    }

    public Instant getVigenteDesde() {
        return vigenteDesde;
    }

    public void setVigenteDesde(Instant vigenteDesde) {
        this.vigenteDesde = vigenteDesde;
    }

    public Instant getVigenteHasta() {
        return vigenteHasta;
    }

    public void setVigenteHasta(Instant vigenteHasta) {
        this.vigenteHasta = vigenteHasta;
    }

    public ServicioDTO getServicio() {
        return servicio;
    }

    public void setServicio(ServicioDTO servicio) {
        this.servicio = servicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TarifaDeServicioDTO)) {
            return false;
        }

        TarifaDeServicioDTO tarifaDeServicioDTO = (TarifaDeServicioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tarifaDeServicioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TarifaDeServicioDTO{" +
            "id=" + getId() +
            ", costoPorMinuto=" + getCostoPorMinuto() +
            ", vigenteDesde='" + getVigenteDesde() + "'" +
            ", vigenteHasta='" + getVigenteHasta() + "'" +
            ", servicio=" + getServicio() +
            "}";
    }
}
