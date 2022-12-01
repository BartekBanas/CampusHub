public class Student implements Comparable<Student> {
    String name;
    String surname;
    StudentCondition studentCondition;
    int birthYear;

    double points;

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

    public void print() {
        System.out.println("Student " + name + " " + surname + "");
        System.out.println("- Student's Condition: " + studentCondition);
        System.out.println("- Birth Year: " + birthYear);
        System.out.println("- Amount of points: " + points);
    }

    @Override
    public int compareTo(Student otherStudent) {
        return this.surname.compareTo(otherStudent.surname);
    }
}
