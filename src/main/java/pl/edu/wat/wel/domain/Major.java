package pl.edu.wat.wel.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Major.
 */
@Entity
@Table(name = "major")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Major implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "major")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SchoolGroup> schoolGroups = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("majors")
    private Department department;

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

    public Major name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SchoolGroup> getSchoolGroups() {
        return schoolGroups;
    }

    public Major schoolGroups(Set<SchoolGroup> schoolGroups) {
        this.schoolGroups = schoolGroups;
        return this;
    }

    public Major addSchoolGroup(SchoolGroup schoolGroup) {
        this.schoolGroups.add(schoolGroup);
        schoolGroup.setMajor(this);
        return this;
    }

    public Major removeSchoolGroup(SchoolGroup schoolGroup) {
        this.schoolGroups.remove(schoolGroup);
        schoolGroup.setMajor(null);
        return this;
    }

    public void setSchoolGroups(Set<SchoolGroup> schoolGroups) {
        this.schoolGroups = schoolGroups;
    }

    public Department getDepartment() {
        return department;
    }

    public Major department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Major)) {
            return false;
        }
        return id != null && id.equals(((Major) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Major{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
