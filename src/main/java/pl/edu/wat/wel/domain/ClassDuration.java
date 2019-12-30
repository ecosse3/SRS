package pl.edu.wat.wel.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ClassDuration.
 */
@Entity
@Table(name = "class_duration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassDuration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "classDuration")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reservation> reservations = new HashSet<>();

    @OneToMany(mappedBy = "classDuration")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Timetable> timetables = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ClassDuration name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public ClassDuration reservations(Set<Reservation> reservations) {
        this.reservations = reservations;
        return this;
    }

    public ClassDuration addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setClassDuration(this);
        return this;
    }

    public ClassDuration removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.setClassDuration(null);
        return this;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Set<Timetable> getTimetables() {
        return timetables;
    }

    public ClassDuration timetables(Set<Timetable> timetables) {
        this.timetables = timetables;
        return this;
    }

    public ClassDuration addTimetable(Timetable timetable) {
        this.timetables.add(timetable);
        timetable.setClassDuration(this);
        return this;
    }

    public ClassDuration removeTimetable(Timetable timetable) {
        this.timetables.remove(timetable);
        timetable.setClassDuration(null);
        return this;
    }

    public void setTimetables(Set<Timetable> timetables) {
        this.timetables = timetables;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassDuration)) {
            return false;
        }
        return id != null && id.equals(((ClassDuration) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ClassDuration{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
