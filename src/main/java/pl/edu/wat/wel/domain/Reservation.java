package pl.edu.wat.wel.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "note_to_teacher")
    private String noteToTeacher;

    @NotNull
    @Column(name = "original_class_date", nullable = false)
    private LocalDate originalClassDate;

    @NotNull
    @Column(name = "new_class_date", nullable = false)
    private LocalDate newClassDate;

    @Column(name = "requested_by")
    private String requestedBy;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "reservation_participants",
               joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "participants_id", referencedColumnName = "id"))
    private Set<User> participants = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("reservationS")
    private SchoolGroup schoolGroup;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("reservationBS")
    private Building building;

    @ManyToOne
    @JsonIgnoreProperties("reservationCS")
    private ClassRoom classRoom;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("originalStartTimes")
    private ClassHours originalStartTime;

    @ManyToOne
    @JsonIgnoreProperties("newStartTimes")
    private ClassHours newStartTime;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("reservations")
    private ClassDuration classDuration;

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

    public Reservation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoteToTeacher() {
        return noteToTeacher;
    }

    public Reservation noteToTeacher(String noteToTeacher) {
        this.noteToTeacher = noteToTeacher;
        return this;
    }

    public void setNoteToTeacher(String noteToTeacher) {
        this.noteToTeacher = noteToTeacher;
    }

    public LocalDate getOriginalClassDate() {
        return originalClassDate;
    }

    public Reservation originalClassDate(LocalDate originalClassDate) {
        this.originalClassDate = originalClassDate;
        return this;
    }

    public void setOriginalClassDate(LocalDate originalClassDate) {
        this.originalClassDate = originalClassDate;
    }

    public LocalDate getNewClassDate() {
        return newClassDate;
    }

    public Reservation newClassDate(LocalDate newClassDate) {
        this.newClassDate = newClassDate;
        return this;
    }

    public void setNewClassDate(LocalDate newClassDate) {
        this.newClassDate = newClassDate;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public Reservation requestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
        return this;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public Reservation participants(Set<User> users) {
        this.participants = users;
        return this;
    }

    public Reservation addParticipants(User user) {
        this.participants.add(user);
        return this;
    }

    public Reservation removeParticipants(User user) {
        this.participants.remove(user);
        return this;
    }

    public void setParticipants(Set<User> users) {
        this.participants = users;
    }

    public SchoolGroup getSchoolGroup() {
        return schoolGroup;
    }

    public Reservation schoolGroup(SchoolGroup schoolGroup) {
        this.schoolGroup = schoolGroup;
        return this;
    }

    public void setSchoolGroup(SchoolGroup schoolGroup) {
        this.schoolGroup = schoolGroup;
    }

    public Building getBuilding() {
        return building;
    }

    public Reservation building(Building building) {
        this.building = building;
        return this;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public Reservation classRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
        return this;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public ClassHours getOriginalStartTime() {
        return originalStartTime;
    }

    public Reservation originalStartTime(ClassHours classHours) {
        this.originalStartTime = classHours;
        return this;
    }

    public void setOriginalStartTime(ClassHours classHours) {
        this.originalStartTime = classHours;
    }

    public ClassHours getNewStartTime() {
        return newStartTime;
    }

    public Reservation newStartTime(ClassHours classHours) {
        this.newStartTime = classHours;
        return this;
    }

    public void setNewStartTime(ClassHours classHours) {
        this.newStartTime = classHours;
    }

    public ClassDuration getClassDuration() {
        return classDuration;
    }

    public Reservation classDuration(ClassDuration classDuration) {
        this.classDuration = classDuration;
        return this;
    }

    public void setClassDuration(ClassDuration classDuration) {
        this.classDuration = classDuration;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        return id != null && id.equals(((Reservation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", noteToTeacher='" + getNoteToTeacher() + "'" +
            ", originalClassDate='" + getOriginalClassDate() + "'" +
            ", newClassDate='" + getNewClassDate() + "'" +
            ", requestedBy='" + getRequestedBy() + "'" +
            "}";
    }
}
