package Application;

public class Student implements Comparable<Student> {
    public int ID;
    String name;
    String surname;
    StudentCondition studentCondition;
    int birthYear;

    double points;

    public Student(String name, String surname, int id) {
        this.name = name;
        this.surname = surname;
        this.ID = id;
    }

    public double getPoints() {
        return points;
    }

    public Student(String name, String surname, StudentCondition studentCondition, int birthYear, double points) {
        this.name = name;
        this.surname = surname;
        this.studentCondition = studentCondition;
        this.birthYear = birthYear;
        this.points = points;
    }

    public Student(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.studentCondition = StudentCondition.absent;
        this.birthYear = 2000;
    }

    public Student(String name, String surname, int id, double points) {
        this.name = name;
        this.surname = surname;
        this.studentCondition = StudentCondition.absent;
        this.birthYear = 2000;
        this.ID = id;
        this.points = points;
    }

    public void print() {
        System.out.println("Application.Student " + name + " " + surname + "");
        System.out.println("- Application.Student's Condition: " + studentCondition);
        System.out.println("- Birth Year: " + birthYear);
        System.out.println("- Amount of points: " + points);
    }

    @Override
    public int compareTo(Student otherStudent) {
        return this.surname.compareTo(otherStudent.surname);
    }
}
