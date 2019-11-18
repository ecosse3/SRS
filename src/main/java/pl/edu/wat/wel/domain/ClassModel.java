package pl.edu.wat.wel.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ClassModel.
 */
@Entity
@Table(name = "class_model")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "classModels")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<ClassRoom> classRooms = new HashSet<>();

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

    public ClassModel name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ClassRoom> getClassRooms() {
        return classRooms;
    }

    public ClassModel classRooms(Set<ClassRoom> classRooms) {
        this.classRooms = classRooms;
        return this;
    }

    public ClassModel addClassRoom(ClassRoom classRoom) {
        this.classRooms.add(classRoom);
        classRoom.getClassModels().add(this);
        return this;
    }

    public ClassModel removeClassRoom(ClassRoom classRoom) {
        this.classRooms.remove(classRoom);
        classRoom.getClassModels().remove(this);
        return this;
    }

    public void setClassRooms(Set<ClassRoom> classRooms) {
        this.classRooms = classRooms;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassModel)) {
            return false;
        }
        return id != null && id.equals(((ClassModel) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ClassModel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
