package pl.edu.wat.wel.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link pl.edu.wat.wel.domain.Reservation} entity. This class is used
 * in {@link pl.edu.wat.wel.web.rest.ReservationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reservations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReservationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter noteToTeacher;

    private LocalDateFilter originalClassDate;

    private LocalDateFilter newClassDate;

    private StringFilter requestedBy;

    private InstantFilter createdDate;

    private LongFilter participantsId;

    private LongFilter schoolGroupId;

    private LongFilter buildingId;

    private LongFilter classRoomId;

    private LongFilter originalStartTimeId;

    private LongFilter newStartTimeId;

    private LongFilter classDurationId;

    private LongFilter statusId;

    public ReservationCriteria(){
    }

    public ReservationCriteria(ReservationCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.noteToTeacher = other.noteToTeacher == null ? null : other.noteToTeacher.copy();
        this.originalClassDate = other.originalClassDate == null ? null : other.originalClassDate.copy();
        this.newClassDate = other.newClassDate == null ? null : other.newClassDate.copy();
        this.requestedBy = other.requestedBy == null ? null : other.requestedBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.participantsId = other.participantsId == null ? null : other.participantsId.copy();
        this.schoolGroupId = other.schoolGroupId == null ? null : other.schoolGroupId.copy();
        this.buildingId = other.buildingId == null ? null : other.buildingId.copy();
        this.classRoomId = other.classRoomId == null ? null : other.classRoomId.copy();
        this.originalStartTimeId = other.originalStartTimeId == null ? null : other.originalStartTimeId.copy();
        this.newStartTimeId = other.newStartTimeId == null ? null : other.newStartTimeId.copy();
        this.classDurationId = other.classDurationId == null ? null : other.classDurationId.copy();
        this.statusId = other.statusId == null ? null : other.statusId.copy();
    }

    @Override
    public ReservationCriteria copy() {
        return new ReservationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getNoteToTeacher() {
        return noteToTeacher;
    }

    public void setNoteToTeacher(StringFilter noteToTeacher) {
        this.noteToTeacher = noteToTeacher;
    }

    public LocalDateFilter getOriginalClassDate() {
        return originalClassDate;
    }

    public void setOriginalClassDate(LocalDateFilter originalClassDate) {
        this.originalClassDate = originalClassDate;
    }

    public LocalDateFilter getNewClassDate() {
        return newClassDate;
    }

    public void setNewClassDate(LocalDateFilter newClassDate) {
        this.newClassDate = newClassDate;
    }

    public StringFilter getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(StringFilter requestedBy) {
        this.requestedBy = requestedBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getParticipantsId() {
        return participantsId;
    }

    public void setParticipantsId(LongFilter participantsId) {
        this.participantsId = participantsId;
    }

    public LongFilter getSchoolGroupId() {
        return schoolGroupId;
    }

    public void setSchoolGroupId(LongFilter schoolGroupId) {
        this.schoolGroupId = schoolGroupId;
    }

    public LongFilter getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(LongFilter buildingId) {
        this.buildingId = buildingId;
    }

    public LongFilter getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(LongFilter classRoomId) {
        this.classRoomId = classRoomId;
    }

    public LongFilter getOriginalStartTimeId() {
        return originalStartTimeId;
    }

    public void setOriginalStartTimeId(LongFilter originalStartTimeId) {
        this.originalStartTimeId = originalStartTimeId;
    }

    public LongFilter getNewStartTimeId() {
        return newStartTimeId;
    }

    public void setNewStartTimeId(LongFilter newStartTimeId) {
        this.newStartTimeId = newStartTimeId;
    }

    public LongFilter getClassDurationId() {
        return classDurationId;
    }

    public void setClassDurationId(LongFilter classDurationId) {
        this.classDurationId = classDurationId;
    }

    public LongFilter getStatusId() {
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReservationCriteria that = (ReservationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(noteToTeacher, that.noteToTeacher) &&
            Objects.equals(originalClassDate, that.originalClassDate) &&
            Objects.equals(newClassDate, that.newClassDate) &&
            Objects.equals(requestedBy, that.requestedBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(participantsId, that.participantsId) &&
            Objects.equals(schoolGroupId, that.schoolGroupId) &&
            Objects.equals(buildingId, that.buildingId) &&
            Objects.equals(classRoomId, that.classRoomId) &&
            Objects.equals(originalStartTimeId, that.originalStartTimeId) &&
            Objects.equals(newStartTimeId, that.newStartTimeId) &&
            Objects.equals(classDurationId, that.classDurationId) &&
            Objects.equals(statusId, that.statusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        noteToTeacher,
        originalClassDate,
        newClassDate,
        requestedBy,
        createdDate,
        participantsId,
        schoolGroupId,
        buildingId,
        classRoomId,
        originalStartTimeId,
        newStartTimeId,
        classDurationId,
        statusId
        );
    }

    @Override
    public String toString() {
        return "ReservationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (noteToTeacher != null ? "noteToTeacher=" + noteToTeacher + ", " : "") +
                (originalClassDate != null ? "originalClassDate=" + originalClassDate + ", " : "") +
                (newClassDate != null ? "newClassDate=" + newClassDate + ", " : "") +
                (requestedBy != null ? "requestedBy=" + requestedBy + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (participantsId != null ? "participantsId=" + participantsId + ", " : "") +
                (schoolGroupId != null ? "schoolGroupId=" + schoolGroupId + ", " : "") +
                (buildingId != null ? "buildingId=" + buildingId + ", " : "") +
                (classRoomId != null ? "classRoomId=" + classRoomId + ", " : "") +
                (originalStartTimeId != null ? "originalStartTimeId=" + originalStartTimeId + ", " : "") +
                (newStartTimeId != null ? "newStartTimeId=" + newStartTimeId + ", " : "") +
                (classDurationId != null ? "classDurationId=" + classDurationId + ", " : "") +
                (statusId != null ? "statusId=" + statusId + ", " : "") +
            "}";
    }

}
