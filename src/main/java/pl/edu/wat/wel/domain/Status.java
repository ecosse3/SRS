package pl.edu.wat.wel.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Status.
 */
@Entity
@Table(name = "status")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Status implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "polish_name", nullable = false)
    private String polishName;

    @NotNull
    @Column(name = "english_name", nullable = false)
    private String englishName;

    @OneToMany(mappedBy = "status")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reservation> reservations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolishName() {
        return polishName;
    }

    public Status polishName(String polishName) {
        this.polishName = polishName;
        return this;
    }

    public void setPolishName(String polishName) {
        this.polishName = polishName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public Status englishName(String englishName) {
        this.englishName = englishName;
        return this;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public Status reservations(Set<Reservation> reservations) {
        this.reservations = reservations;
        return this;
    }

    public Status addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setStatus(this);
        return this;
    }

    public Status removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.setStatus(null);
        return this;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Status)) {
            return false;
        }
        return id != null && id.equals(((Status) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Status{" +
            "id=" + getId() +
            ", polishName='" + getPolishName() + "'" +
            ", englishName='" + getEnglishName() + "'" +
            "}";
    }
}
