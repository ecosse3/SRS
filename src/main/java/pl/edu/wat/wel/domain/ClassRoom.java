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
 * A ClassRoom.
 */
@Entity
@Table(name = "class_room")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    @Min(value = 2)
    @Max(value = 150)
    @Column(name = "maximum_size")
    private Integer maximumSize;

    @OneToMany(mappedBy = "classRoom")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reservation> reservationCS = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "class_room_class_model",
               joinColumns = @JoinColumn(name = "class_room_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "class_model_id", referencedColumnName = "id"))
    private Set<ClassModel> classModels = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("classRoomBS")
    private Building building;

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

    public ClassRoom number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getMaximumSize() {
        return maximumSize;
    }

    public ClassRoom maximumSize(Integer maximumSize) {
        this.maximumSize = maximumSize;
        return this;
    }

    public void setMaximumSize(Integer maximumSize) {
        this.maximumSize = maximumSize;
    }

    public Set<Reservation> getReservationCS() {
        return reservationCS;
    }

    public ClassRoom reservationCS(Set<Reservation> reservations) {
        this.reservationCS = reservations;
        return this;
    }

    public ClassRoom addReservationC(Reservation reservation) {
        this.reservationCS.add(reservation);
        reservation.setClassRoom(this);
        return this;
    }

    public ClassRoom removeReservationC(Reservation reservation) {
        this.reservationCS.remove(reservation);
        reservation.setClassRoom(null);
        return this;
    }

    public void setReservationCS(Set<Reservation> reservations) {
        this.reservationCS = reservations;
    }

    public Set<ClassModel> getClassModels() {
        return classModels;
    }

    public ClassRoom classModels(Set<ClassModel> classModels) {
        this.classModels = classModels;
        return this;
    }

    public ClassRoom addClassModel(ClassModel classModel) {
        this.classModels.add(classModel);
        classModel.getClassRooms().add(this);
        return this;
    }

    public ClassRoom removeClassModel(ClassModel classModel) {
        this.classModels.remove(classModel);
        classModel.getClassRooms().remove(this);
        return this;
    }

    public void setClassModels(Set<ClassModel> classModels) {
        this.classModels = classModels;
    }

    public Building getBuilding() {
        return building;
    }

    public ClassRoom building(Building building) {
        this.building = building;
        return this;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassRoom)) {
            return false;
        }
        return id != null && id.equals(((ClassRoom) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ClassRoom{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", maximumSize=" + getMaximumSize() +
            "}";
    }
}
