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
 * Criteria class for the {@link pl.edu.wat.wel.domain.SchoolGroup} entity. This class is used
 * in {@link pl.edu.wat.wel.web.rest.SchoolGroupResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /school-groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SchoolGroupCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter starostId;

    private LongFilter reservationSId;

    private LongFilter majorId;

    public SchoolGroupCriteria(){
    }

    public SchoolGroupCriteria(SchoolGroupCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.starostId = other.starostId == null ? null : other.starostId.copy();
        this.reservationSId = other.reservationSId == null ? null : other.reservationSId.copy();
        this.majorId = other.majorId == null ? null : other.majorId.copy();
    }

    @Override
    public SchoolGroupCriteria copy() {
        return new SchoolGroupCriteria(this);
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

    public LongFilter getStarostId() {
        return starostId;
    }

    public void setStarostId(LongFilter starostId) {
        this.starostId = starostId;
    }

    public LongFilter getReservationSId() {
        return reservationSId;
    }

    public void setReservationSId(LongFilter reservationSId) {
        this.reservationSId = reservationSId;
    }

    public LongFilter getMajorId() {
        return majorId;
    }

    public void setMajorId(LongFilter majorId) {
        this.majorId = majorId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SchoolGroupCriteria that = (SchoolGroupCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(starostId, that.starostId) &&
            Objects.equals(reservationSId, that.reservationSId) &&
            Objects.equals(majorId, that.majorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        starostId,
        reservationSId,
        majorId
        );
    }

    @Override
    public String toString() {
        return "SchoolGroupCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (starostId != null ? "starostId=" + starostId + ", " : "") +
                (reservationSId != null ? "reservationSId=" + reservationSId + ", " : "") +
                (majorId != null ? "majorId=" + majorId + ", " : "") +
            "}";
    }

}
