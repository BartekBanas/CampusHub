package entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "classes", schema = "dziennik")
public class ClassEntity extends AbstractEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "capacity")
    private Integer capacity;
    @Basic
    @Column(name = "containerID")
    private Integer containerId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getContainerId() {
        return containerId;
    }

    public void setContainerId(Integer containerId) {
        this.containerId = containerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassEntity that = (ClassEntity) o;

        if (id != that.id) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(capacity, that.capacity)) return false;
        if (!Objects.equals(containerId, that.containerId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (capacity != null ? capacity.hashCode() : 0);
        result = 31 * result + (containerId != null ? containerId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClassEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", containerId=" + containerId +
                '}';
    }

    //    @OneToMany(mappedBy = "aClass")
//    private Collection<StudentsEntity> studentEntity;
//
//    public Collection<StudentsEntity> getStudent() {
//        return studentEntity;
//    }
//
//    public void setStudent(Collection<StudentsEntity> studentEntity) {
//        this.studentEntity = studentEntity;
//    }
}
