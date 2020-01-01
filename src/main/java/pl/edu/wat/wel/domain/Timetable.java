package pl.edu.wat.wel.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Timetable.
 */
@Entity
@Table(name = "timetable")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Timetable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;

    @NotNull
    @Column(name = "class_date", nullable = false)
    private LocalDate classDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("timetables")
    private SchoolGroup schoolGroup;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("timetables")
    private Building building;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("timetables")
    private ClassRoom classRoom;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("timetables")
    private ClassHours startTime;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("timetables")
    private ClassDuration classDuration;

    @ManyToOne
    @JsonIgnoreProperties("timetables")
    private ClassHours endTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public Timetable subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDate getClassDate() {
        return classDate;
    }

    public Timetable classDate(LocalDate classDate) {
        this.classDate = classDate;
        return this;
    }

    public void setClassDate(LocalDate classDate) {
        this.classDate = classDate;
    }

    public SchoolGroup getSchoolGroup() {
        return schoolGroup;
    }

    public Timetable schoolGroup(SchoolGroup schoolGroup) {
        this.schoolGroup = schoolGroup;
        return this;
    }

    public void setSchoolGroup(SchoolGroup schoolGroup) {
        this.schoolGroup = schoolGroup;
    }

    public Building getBuilding() {
        return building;
    }

    public Timetable building(Building building) {
        this.building = building;
        return this;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public Timetable classRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
        return this;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public ClassHours getStartTime() {
        return startTime;
    }

    public Timetable startTime(ClassHours classHours) {
        this.startTime = classHours;
        return this;
    }

    public void setStartTime(ClassHours classHours) {
        this.startTime = classHours;
    }

    public ClassDuration getClassDuration() {
        return classDuration;
    }

    public Timetable classDuration(ClassDuration classDuration) {
        this.classDuration = classDuration;
        return this;
    }

    public void setClassDuration(ClassDuration classDuration) {
        this.classDuration = classDuration;
    }

    public ClassHours getEndTime() {
        return endTime;
    }

    public Timetable endTime(ClassHours classHours) {
        this.endTime = classHours;
        return this;
    }

    public void setEndTime(ClassHours classHours) {
        this.endTime = classHours;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Timetable)) {
            return false;
        }
        return id != null && id.equals(((Timetable) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Timetable{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", classDate='" + getClassDate() + "'" +
            "}";
    }
}
