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
 * Criteria class for the {@link pl.edu.wat.wel.domain.ClassModel} entity. This class is used
 * in {@link pl.edu.wat.wel.web.rest.ClassModelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /class-models?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassModelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter classRoomId;

    public ClassModelCriteria(){
    }

    public ClassModelCriteria(ClassModelCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.classRoomId = other.classRoomId == null ? null : other.classRoomId.copy();
    }

    @Override
    public ClassModelCriteria copy() {
        return new ClassModelCriteria(this);
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

    public LongFilter getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(LongFilter classRoomId) {
        this.classRoomId = classRoomId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClassModelCriteria that = (ClassModelCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(classRoomId, that.classRoomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        classRoomId
        );
    }

    @Override
    public String toString() {
        return "ClassModelCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (classRoomId != null ? "classRoomId=" + classRoomId + ", " : "") +
            "}";
    }

}
