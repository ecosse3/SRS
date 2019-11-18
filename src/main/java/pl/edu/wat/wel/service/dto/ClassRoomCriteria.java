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
 * Criteria class for the {@link pl.edu.wat.wel.domain.ClassRoom} entity. This class is used
 * in {@link pl.edu.wat.wel.web.rest.ClassRoomResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /class-rooms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassRoomCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter number;

    private IntegerFilter maximumSize;

    private LongFilter reservationCId;

    private LongFilter classModelId;

    private LongFilter buildingId;

    public ClassRoomCriteria(){
    }

    public ClassRoomCriteria(ClassRoomCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.maximumSize = other.maximumSize == null ? null : other.maximumSize.copy();
        this.reservationCId = other.reservationCId == null ? null : other.reservationCId.copy();
        this.classModelId = other.classModelId == null ? null : other.classModelId.copy();
        this.buildingId = other.buildingId == null ? null : other.buildingId.copy();
    }

    @Override
    public ClassRoomCriteria copy() {
        return new ClassRoomCriteria(this);
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

    public IntegerFilter getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(IntegerFilter maximumSize) {
        this.maximumSize = maximumSize;
    }

    public LongFilter getReservationCId() {
        return reservationCId;
    }

    public void setReservationCId(LongFilter reservationCId) {
        this.reservationCId = reservationCId;
    }

    public LongFilter getClassModelId() {
        return classModelId;
    }

    public void setClassModelId(LongFilter classModelId) {
        this.classModelId = classModelId;
    }

    public LongFilter getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(LongFilter buildingId) {
        this.buildingId = buildingId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClassRoomCriteria that = (ClassRoomCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(number, that.number) &&
            Objects.equals(maximumSize, that.maximumSize) &&
            Objects.equals(reservationCId, that.reservationCId) &&
            Objects.equals(classModelId, that.classModelId) &&
            Objects.equals(buildingId, that.buildingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        number,
        maximumSize,
        reservationCId,
        classModelId,
        buildingId
        );
    }

    @Override
    public String toString() {
        return "ClassRoomCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (number != null ? "number=" + number + ", " : "") +
                (maximumSize != null ? "maximumSize=" + maximumSize + ", " : "") +
                (reservationCId != null ? "reservationCId=" + reservationCId + ", " : "") +
                (classModelId != null ? "classModelId=" + classModelId + ", " : "") +
                (buildingId != null ? "buildingId=" + buildingId + ", " : "") +
            "}";
    }

}
