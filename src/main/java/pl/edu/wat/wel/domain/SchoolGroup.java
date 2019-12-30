package pl.edu.wat.wel.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A SchoolGroup.
 */
@Entity
@Table(name = "school_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SchoolGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(unique = true)
    private User starost;

    @OneToMany(mappedBy = "schoolGroup")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reservation> reservationS = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("schoolGroups")
    private Major major;

    @OneToMany(mappedBy = "schoolGroup")
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

    public SchoolGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getStarost() {
        return starost;
    }

    public SchoolGroup starost(User user) {
        this.starost = user;
        return this;
    }

    public void setStarost(User user) {
        this.starost = user;
    }

    public Set<Reservation> getReservationS() {
        return reservationS;
    }

    public SchoolGroup reservationS(Set<Reservation> reservations) {
        this.reservationS = reservations;
        return this;
    }

    public SchoolGroup addReservationS(Reservation reservation) {
        this.reservationS.add(reservation);
        reservation.setSchoolGroup(this);
        return this;
    }

    public SchoolGroup removeReservationS(Reservation reservation) {
        this.reservationS.remove(reservation);
        reservation.setSchoolGroup(null);
        return this;
    }

    public void setReservationS(Set<Reservation> reservations) {
        this.reservationS = reservations;
    }

    public Major getMajor() {
        return major;
    }

    public SchoolGroup major(Major major) {
        this.major = major;
        return this;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public Set<Timetable> getTimetables() {
        return timetables;
    }

    public SchoolGroup timetables(Set<Timetable> timetables) {
        this.timetables = timetables;
        return this;
    }

    public SchoolGroup addTimetable(Timetable timetable) {
        this.timetables.add(timetable);
        timetable.setSchoolGroup(this);
        return this;
    }

    public SchoolGroup removeTimetable(Timetable timetable) {
        this.timetables.remove(timetable);
        timetable.setSchoolGroup(null);
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
        if (!(o instanceof SchoolGroup)) {
            return false;
        }
        return id != null && id.equals(((SchoolGroup) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SchoolGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
