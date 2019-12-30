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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link pl.edu.wat.wel.domain.Timetable} entity. This class is used
 * in {@link pl.edu.wat.wel.web.rest.TimetableResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /timetables?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TimetableCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter subject;

    private LocalDateFilter classDate;

    public TimetableCriteria(){
    }

    public TimetableCriteria(TimetableCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.subject = other.subject == null ? null : other.subject.copy();
        this.classDate = other.classDate == null ? null : other.classDate.copy();
    }

    @Override
    public TimetableCriteria copy() {
        return new TimetableCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
    }

    public LocalDateFilter getClassDate() {
        return classDate;
    }

    public void setClassDate(LocalDateFilter classDate) {
        this.classDate = classDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TimetableCriteria that = (TimetableCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(classDate, that.classDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        subject,
        classDate
        );
    }

    @Override
    public String toString() {
        return "TimetableCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (subject != null ? "subject=" + subject + ", " : "") +
                (classDate != null ? "classDate=" + classDate + ", " : "") +
            "}";
    }

}
