import entity.StudentEntity;

import java.util.*;

public class Class {
    int ID;
    String className;
    List<Student> studentsList = new ArrayList<>();

    int capacity;

    public Class(String className) {
        this.className = className;
        this.capacity = 20;
    }

    public Class() {
    }

    public Class(String className, int capacity) {
        this.className = className;
        this.capacity = capacity;
    }

    public Class(String className, int capacity, int id) {
        this.className = className;
        this.capacity = capacity;
        this.ID = id;
    }

    public void addStudent(Student student) {
        if (studentsList.size() < capacity) {
            studentsList.add(student);

            try {
                App.transaction.begin();

                StudentEntity studentEntity = new StudentEntity();
                studentEntity.setName(student.name);
                studentEntity.setSurname(student.surname);
                studentEntity.setClassId(ID);

                App.entityManager.persist(studentEntity);
                App.studentEntityList.add(studentEntity);

                App.transaction.commit();
            } finally {
                if (App.transaction.isActive()) {
                    App.transaction.rollback();
                }
            }
        }
    }

    public void setStudent(Student student) {
        if (studentsList.size() < capacity) {
            studentsList.add(student);
        }
    }

    public void executeStudents() {
        studentsList.removeIf(student -> student.points <= 0);
    }

    public void assassinateStudent(Student convictedStudent) {
        for (Student student : studentsList) {
            if (Objects.equals(convictedStudent.name, student.name) &&
                    Objects.equals(convictedStudent.surname, student.surname)) {
                if (convictedStudent.points <= 0) {
                    studentsList.remove(convictedStudent);
                }
            }
        }
    }

    public void changeCondition(Student student, StudentCondition studentCondition) {
        student.studentCondition = studentCondition;
    }

    public void addPoints(Student student, double amount) {
        student.points += amount;
    }

    public void removePoints(Student student, double amount) {
        student.points -= amount;
    }

    public List<Student> searchPartial(String partOfSurname) {
        List<Student> listToReturn = new ArrayList<>();

        for (Student student : studentsList) {
            if (student.surname.toLowerCase().contains(partOfSurname.toLowerCase()) ||
                    student.name.toLowerCase().contains(partOfSurname.toLowerCase()))
                listToReturn.add(student);
        }
        return listToReturn;
    }

    public int countByCondition(StudentCondition studentCondition) {
        int i = 0;
        for (Student student : studentsList) {
            if (student.studentCondition == studentCondition) {
                i++;
            }
        }

        return i;
    }

    public void summary() {
        System.out.println("Class " + className + " summary:");
        for (Student student : studentsList) {
            student.print();
            System.out.println();
        }
    }

    public void sortByName() {
        int k = studentsList.size();

        Student temp;

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < i; j++) {
                if (studentsList.get(i).name.compareTo(studentsList.get(j).name) < 0) {
                    temp = studentsList.get(i);
                    studentsList.set(i, studentsList.get(j));
                    studentsList.set(j, temp);
                }
            }
        }
    }

    public void sortByPoints() {
        studentsList.sort(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return Double.compare(o1.points, o2.points);
            }
        });

        revlist(studentsList);
    }

    public double max() {
        List<Double> pointsOfStudents = new ArrayList<>();
        for (Student student : studentsList) {
            pointsOfStudents.add(student.points);
        }

        return Collections.max(pointsOfStudents);
    }

    public static <T> void revlist(List<T> list) {
        // base condition when the list size is 0
        if (list.size() <= 1)
            return;


        T value = list.remove(0);

        // call the recursive function to reverse
        // the list after removing the first element
        revlist(list);

        // now after the rest of the list has been
        // reversed by the upper recursive call,
        // add the first value at the end
        list.add(value);
    }
}