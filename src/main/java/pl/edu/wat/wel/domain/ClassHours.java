package pl.edu.wat.wel.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ClassHours.
 */
@Entity
@Table(name = "class_hours")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassHours implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private String startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private String endTime;

    @OneToMany(mappedBy = "originalStartTime")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reservation> originalStartTimes = new HashSet<>();

    @OneToMany(mappedBy = "newStartTime")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reservation> newStartTimes = new HashSet<>();

    @OneToMany(mappedBy = "startTime")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Timetable> timetables = new HashSet<>();

    @OneToMany(mappedBy = "endTime")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Timetable> tt_endTimes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public ClassHours startTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public ClassHours endTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Set<Reservation> getOriginalStartTimes() {
        return originalStartTimes;
    }

    public ClassHours originalStartTimes(Set<Reservation> reservations) {
        this.originalStartTimes = reservations;
        return this;
    }

    public ClassHours addOriginalStartTime(Reservation reservation) {
        this.originalStartTimes.add(reservation);
        reservation.setOriginalStartTime(this);
        return this;
    }

    public ClassHours removeOriginalStartTime(Reservation reservation) {
        this.originalStartTimes.remove(reservation);
        reservation.setOriginalStartTime(null);
        return this;
    }

    public void setOriginalStartTimes(Set<Reservation> reservations) {
        this.originalStartTimes = reservations;
    }

    public Set<Reservation> getNewStartTimes() {
        return newStartTimes;
    }

    public ClassHours newStartTimes(Set<Reservation> reservations) {
        this.newStartTimes = reservations;
        return this;
    }

    public ClassHours addNewStartTime(Reservation reservation) {
        this.newStartTimes.add(reservation);
        reservation.setNewStartTime(this);
        return this;
    }

    public ClassHours removeNewStartTime(Reservation reservation) {
        this.newStartTimes.remove(reservation);
        reservation.setNewStartTime(null);
        return this;
    }

    public void setNewStartTimes(Set<Reservation> reservations) {
        this.newStartTimes = reservations;
    }

    public Set<Timetable> getTimetables() {
        return timetables;
    }

    public ClassHours timetables(Set<Timetable> timetables) {
        this.timetables = timetables;
        return this;
    }

    public ClassHours addTimetable(Timetable timetable) {
        this.timetables.add(timetable);
        timetable.setStartTime(this);
        return this;
    }

    public ClassHours removeTimetable(Timetable timetable) {
        this.timetables.remove(timetable);
        timetable.setStartTime(null);
        return this;
    }

    public void setTimetables(Set<Timetable> timetables) {
        this.timetables = timetables;
    }

    public Set<Timetable> getTt_endTimes() {
        return tt_endTimes;
    }

    public ClassHours tt_endTimes(Set<Timetable> timetables) {
        this.tt_endTimes = timetables;
        return this;
    }

    public ClassHours addTt_endTime(Timetable timetable) {
        this.tt_endTimes.add(timetable);
        timetable.setEndTime(this);
        return this;
    }

    public ClassHours removeTt_endTime(Timetable timetable) {
        this.tt_endTimes.remove(timetable);
        timetable.setEndTime(null);
        return this;
    }

    public void setTt_endTimes(Set<Timetable> timetables) {
        this.tt_endTimes = timetables;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassHours)) {
            return false;
        }
        return id != null && id.equals(((ClassHours) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ClassHours{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            "}";
    }
}
