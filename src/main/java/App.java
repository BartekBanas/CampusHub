import entity.ClassEntity;
import entity.ClassContainerEntity;
import entity.StudentsEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    public static EntityManager entityManager = entityManagerFactory.createEntityManager();
    public static EntityTransaction transaction = entityManager.getTransaction();

    public static List<StudentsEntity> studentsList = new ArrayList<>();
    public static List<ClassEntity> classEntityList = new ArrayList<>();
    public static List<ClassContainerEntity> classContainerEntityList = new ArrayList<>();

    public static void main( String[] args ) {
        StartUp();
        PrintStudents();

        System.out.print("Program operating college database\n");

//        boolean again = true;
//        while (again)
//        {
//            System.out.println("What action you wish to perform");
//            Scanner in = new Scanner(System.in);
//            System.out.print("Type in a figure name: ");
//            String figureName = in.nextLine();
//
//
//            switch (figureName.toLowerCase())
//        }


        entityManager.close();
        entityManagerFactory.close();


    }

    private static void createGUI(ClassContainer collage) {
        Window ui = new Window(collage);
        JPanel root = ui.getRootPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void StartUp() {
        studentsList = entityManager.createQuery("SELECT a FROM StudentsEntity a").getResultList();
        classEntityList = entityManager.createQuery("SELECT a FROM ClassEntity a").getResultList();
        classContainerEntityList = entityManager.createQuery("select a FROM ClassContainerEntity a").getResultList();
    }

    static void PrintStudents(){
        System.out.println("List of Students: ");
        for (StudentsEntity studentEntity :studentsList) {
            System.out.println(studentEntity);
        }
    }
    static void PrintClasses(){
        System.out.println("List of Classes: ");
        for (ClassEntity classEntity1 : classEntityList) {
            System.out.println(classEntity1);
        }
    }

    static void PrintClassContainers(){
        System.out.println("List of Class Containers: ");
        for (ClassContainerEntity class1: classContainerEntityList) {
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

            StudentsEntity studentEntity = new StudentsEntity();
            studentEntity.setName(name);
            studentEntity.setSurname(surname);
            studentEntity.setClassId(classID);

            entityManager.persist(studentEntity);

            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }

        StartUp();
    }
}