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
 * Costo de un incidente para un servicio afectado,
 * calculado a partir de la tarifa de dicho servicio
 * y el tiempo que estuvo degradado.
 */
@Entity
@Table(name = "costo_de_incidente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CostoDeIncidente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "minutos_caidos", nullable = false)
    private Integer minutosCaidos;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "monto_total", precision = 21, scale = 2, nullable = false)
    private BigDecimal montoTotal;

    @Column(name = "calculado_en")
    private Instant calculadoEn;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "comandante", "servicios", "postmortem", "alertas", "eventos", "notificacions", "costos" },
        allowSetters = true
    )
    private Incidente incidente;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "equipo", "objetivos", "alertas", "politicas", "tarifas", "costos", "incidentes" }, allowSetters = true)
    private Servicio servicio;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "costos" }, allowSetters = true)
    private PeriodoDeFacturacion periodo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CostoDeIncidente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinutosCaidos() {
        return this.minutosCaidos;
    }

    public CostoDeIncidente minutosCaidos(Integer minutosCaidos) {
        this.setMinutosCaidos(minutosCaidos);
        return this;
    }

    public void setMinutosCaidos(Integer minutosCaidos) {
        this.minutosCaidos = minutosCaidos;
    }

    public BigDecimal getMontoTotal() {
        return this.montoTotal;
    }

    public CostoDeIncidente montoTotal(BigDecimal montoTotal) {
        this.setMontoTotal(montoTotal);
        return this;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Instant getCalculadoEn() {
        return this.calculadoEn;
    }

    public CostoDeIncidente calculadoEn(Instant calculadoEn) {
        this.setCalculadoEn(calculadoEn);
        return this;
    }

    public void setCalculadoEn(Instant calculadoEn) {
        this.calculadoEn = calculadoEn;
    }

    public Incidente getIncidente() {
        return this.incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

    public CostoDeIncidente incidente(Incidente incidente) {
        this.setIncidente(incidente);
        return this;
    }

    public Servicio getServicio() {
        return this.servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public CostoDeIncidente servicio(Servicio servicio) {
        this.setServicio(servicio);
        return this;
    }

    public PeriodoDeFacturacion getPeriodo() {
        return this.periodo;
    }

    public void setPeriodo(PeriodoDeFacturacion periodoDeFacturacion) {
        this.periodo = periodoDeFacturacion;
    }

    public CostoDeIncidente periodo(PeriodoDeFacturacion periodoDeFacturacion) {
        this.setPeriodo(periodoDeFacturacion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CostoDeIncidente)) {
            return false;
        }
        return getId() != null && getId().equals(((CostoDeIncidente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CostoDeIncidente{" +
            "id=" + getId() +
            ", minutosCaidos=" + getMinutosCaidos() +
            ", montoTotal=" + getMontoTotal() +
            ", calculadoEn='" + getCalculadoEn() + "'" +
            "}";
    }
}
