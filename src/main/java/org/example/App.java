package org.example;

import entity.Class;
import entity.ClassContainer;
import entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    public static EntityManager entityManager = entityManagerFactory.createEntityManager();
    public static EntityTransaction transaction = entityManager.getTransaction();

    public static List<Student> studentsList = new ArrayList<>();
    public static List<Class> classList = new ArrayList<>();
    public static List<ClassContainer> classContainerList = new ArrayList<>();

    public static void main( String[] args ) {
        StartUp();
        PrintStudents();


        try {
            transaction.begin();

//            StudentsEntity student = new StudentsEntity();
//            student.setName("Elon");
//            student.setSurname("Mosque");
//            student.setClassId(1);
//
//            entityManager.persist(student);

            final String QUERY = "SELECT id, name, surname FROM students";




            transaction.commit();
        }   finally {
            if(transaction.isActive()) {
                transaction.rollback();
            }
            entityManager.close();
            entityManagerFactory.close();
        }


    }

    public static void StartUp() {
        studentsList = entityManager.createQuery("SELECT a FROM Student a").getResultList();
        classList = entityManager.createQuery("SELECT a FROM Class a").getResultList();
        classContainerList = entityManager.createQuery("select a FROM ClassContainer a").getResultList();
    }

    static void PrintStudents(){
        System.out.println("List of Students: ");
        for (Student student:studentsList) {
            System.out.println(student);
        }
    }
    static void PrintClasses(){
        System.out.println("List of Classes: ");
        for (Class class1: classList) {
            System.out.println(class1);
        }
    }

    static void PrintClassContainers(){
        System.out.println("List of Class Containers: ");
        for (ClassContainer class1: classContainerList) {
            System.out.println(class1);
        }
    }

    static void addStudent() {
        Scanner in = new Scanner(System.in);
        System.out.print("Type in a figure name: ");

        System.out.println("Type in a name: ");
        String name = in.nextLine();
        System.out.println("Type in a surname: ");
        String surname = in.nextLine();
        System.out.println("Type in a class ID: ");
        int classID = Integer.parseInt(in.nextLine());

        try {
            transaction.begin();

            Student student = new Student();
            student.setName(name);
            student.setSurname(surname);
            student.setClassId(classID);

            entityManager.persist(student);

            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }

        StartUp();
    }
}