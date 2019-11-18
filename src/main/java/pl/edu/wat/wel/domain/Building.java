package pl.edu.wat.wel.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Building.
 */
@Entity
@Table(name = "building")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Building implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    @OneToMany(mappedBy = "building")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ClassRoom> classRoomBS = new HashSet<>();

    @OneToMany(mappedBy = "building")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reservation> reservationBS = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public Building number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Set<ClassRoom> getClassRoomBS() {
        return classRoomBS;
    }

    public Building classRoomBS(Set<ClassRoom> classRooms) {
        this.classRoomBS = classRooms;
        return this;
    }

    public Building addClassRoomB(ClassRoom classRoom) {
        this.classRoomBS.add(classRoom);
        classRoom.setBuilding(this);
        return this;
    }

    public Building removeClassRoomB(ClassRoom classRoom) {
        this.classRoomBS.remove(classRoom);
        classRoom.setBuilding(null);
        return this;
    }

    public void setClassRoomBS(Set<ClassRoom> classRooms) {
        this.classRoomBS = classRooms;
    }

    public Set<Reservation> getReservationBS() {
        return reservationBS;
    }

    public Building reservationBS(Set<Reservation> reservations) {
        this.reservationBS = reservations;
        return this;
    }

    public Building addReservationB(Reservation reservation) {
        this.reservationBS.add(reservation);
        reservation.setBuilding(this);
        return this;
    }

    public Building removeReservationB(Reservation reservation) {
        this.reservationBS.remove(reservation);
        reservation.setBuilding(null);
        return this;
    }

    public void setReservationBS(Set<Reservation> reservations) {
        this.reservationBS = reservations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Building)) {
            return false;
        }
        return id != null && id.equals(((Building) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Building{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            "}";
    }
}
