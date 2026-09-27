package ar.edu.um.isa.oncall.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Periodo utilizado para agrupar los costos de los incidentes.
 */
@Entity
@Table(name = "periodo_de_facturacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PeriodoDeFacturacion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @NotNull
    @Column(name = "cerrado", nullable = false)
    private Boolean cerrado;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "periodo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "incidente", "servicio", "periodo" }, allowSetters = true)
    private Set<CostoDeIncidente> costos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PeriodoDeFacturacion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return this.fechaInicio;
    }

    public PeriodoDeFacturacion fechaInicio(LocalDate fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return this.fechaFin;
    }

    public PeriodoDeFacturacion fechaFin(LocalDate fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getCerrado() {
        return this.cerrado;
    }

    public PeriodoDeFacturacion cerrado(Boolean cerrado) {
        this.setCerrado(cerrado);
        return this;
    }

    public void setCerrado(Boolean cerrado) {
        this.cerrado = cerrado;
    }

    public Set<CostoDeIncidente> getCostos() {
        return this.costos;
    }

    public void setCostos(Set<CostoDeIncidente> costoDeIncidentes) {
        if (this.costos != null) {
            this.costos.forEach(i -> i.setPeriodo(null));
        }
        if (costoDeIncidentes != null) {
            costoDeIncidentes.forEach(i -> i.setPeriodo(this));
        }
        this.costos = costoDeIncidentes;
    }

    public PeriodoDeFacturacion costos(Set<CostoDeIncidente> costoDeIncidentes) {
        this.setCostos(costoDeIncidentes);
        return this;
    }

    public PeriodoDeFacturacion addCosto(CostoDeIncidente costoDeIncidente) {
        this.costos.add(costoDeIncidente);
        costoDeIncidente.setPeriodo(this);
        return this;
    }

    public PeriodoDeFacturacion removeCosto(CostoDeIncidente costoDeIncidente) {
        this.costos.remove(costoDeIncidente);
        costoDeIncidente.setPeriodo(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeriodoDeFacturacion)) {
            return false;
        }
        return getId() != null && getId().equals(((PeriodoDeFacturacion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeriodoDeFacturacion{" +
            "id=" + getId() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", cerrado='" + getCerrado() + "'" +
            "}";
    }
}
