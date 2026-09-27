package ar.edu.um.isa.oncall.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Tarifa asociada a un servicio.
 */
@Entity
@Table(name = "tarifa_de_servicio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TarifaDeServicio implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "costo_por_minuto", precision = 21, scale = 2, nullable = false)
    private BigDecimal costoPorMinuto;

    @NotNull
    @Column(name = "vigente_desde", nullable = false)
    private Instant vigenteDesde;

    @Column(name = "vigente_hasta")
    private Instant vigenteHasta;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "equipo", "objetivos", "alertas", "politicas", "tarifas", "costos", "incidentes" }, allowSetters = true)
    private Servicio servicio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TarifaDeServicio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCostoPorMinuto() {
        return this.costoPorMinuto;
    }

    public TarifaDeServicio costoPorMinuto(BigDecimal costoPorMinuto) {
        this.setCostoPorMinuto(costoPorMinuto);
        return this;
    }

    public void setCostoPorMinuto(BigDecimal costoPorMinuto) {
        this.costoPorMinuto = costoPorMinuto;
    }

    public Instant getVigenteDesde() {
        return this.vigenteDesde;
    }

    public TarifaDeServicio vigenteDesde(Instant vigenteDesde) {
        this.setVigenteDesde(vigenteDesde);
        return this;
    }

    public void setVigenteDesde(Instant vigenteDesde) {
        this.vigenteDesde = vigenteDesde;
    }

    public Instant getVigenteHasta() {
        return this.vigenteHasta;
    }

    public TarifaDeServicio vigenteHasta(Instant vigenteHasta) {
        this.setVigenteHasta(vigenteHasta);
        return this;
    }

    public void setVigenteHasta(Instant vigenteHasta) {
        this.vigenteHasta = vigenteHasta;
    }

    public Servicio getServicio() {
        return this.servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public TarifaDeServicio servicio(Servicio servicio) {
        this.setServicio(servicio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TarifaDeServicio)) {
            return false;
        }
        return getId() != null && getId().equals(((TarifaDeServicio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TarifaDeServicio{" +
            "id=" + getId() +
            ", costoPorMinuto=" + getCostoPorMinuto() +
            ", vigenteDesde='" + getVigenteDesde() + "'" +
            ", vigenteHasta='" + getVigenteHasta() + "'" +
            "}";
    }
}
