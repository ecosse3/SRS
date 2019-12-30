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

/**
 * Criteria class for the {@link pl.edu.wat.wel.domain.Building} entity. This class is used
 * in {@link pl.edu.wat.wel.web.rest.BuildingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /buildings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BuildingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter number;

    private LongFilter classRoomBId;

    private LongFilter reservationBId;

    private LongFilter timetableId;

    public BuildingCriteria(){
    }

    public BuildingCriteria(BuildingCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.classRoomBId = other.classRoomBId == null ? null : other.classRoomBId.copy();
        this.reservationBId = other.reservationBId == null ? null : other.reservationBId.copy();
        this.timetableId = other.timetableId == null ? null : other.timetableId.copy();
    }

    @Override
    public BuildingCriteria copy() {
        return new BuildingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNumber() {
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
    }

    public LongFilter getClassRoomBId() {
        return classRoomBId;
    }

    public void setClassRoomBId(LongFilter classRoomBId) {
        this.classRoomBId = classRoomBId;
    }

    public LongFilter getReservationBId() {
        return reservationBId;
    }

    public void setReservationBId(LongFilter reservationBId) {
        this.reservationBId = reservationBId;
    }

    public LongFilter getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(LongFilter timetableId) {
        this.timetableId = timetableId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BuildingCriteria that = (BuildingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(number, that.number) &&
            Objects.equals(classRoomBId, that.classRoomBId) &&
            Objects.equals(reservationBId, that.reservationBId) &&
            Objects.equals(timetableId, that.timetableId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        number,
        classRoomBId,
        reservationBId,
        timetableId
        );
    }

    @Override
    public String toString() {
        return "BuildingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (number != null ? "number=" + number + ", " : "") +
                (classRoomBId != null ? "classRoomBId=" + classRoomBId + ", " : "") +
                (reservationBId != null ? "reservationBId=" + reservationBId + ", " : "") +
                (timetableId != null ? "timetableId=" + timetableId + ", " : "") +
            "}";
    }

}
