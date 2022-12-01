import entity.ClassEntity;
import entity.ClassContainerEntity;
import entity.StudentEntity;
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

    public static List<StudentEntity> studentsList = new ArrayList<>();
    public static List<ClassEntity> classEntityList = new ArrayList<>();
    public static List<ClassContainerEntity> classContainerEntityList = new ArrayList<>();

    public static void main(String[] args) {
        ClassContainer AGH = new ClassContainer();

        StartUp(AGH);
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


        Student s1 = new Student("John", "Cena", StudentCondition.present, 2000, 7);
        Student s2 = new Student("Alex", "Woodrow", StudentCondition.present, 2001, 0);
        Student s3 = new Student("Petter", "Griffin", StudentCondition.present, 1998, 2);
        Student s4 = new Student("Randy", "Marsh", StudentCondition.present, 1999, 5);
        Student s5 = new Student("Matt", "Wentworth", StudentCondition.present, 2000, 9);

//        AGH.addClass(new Class("Programming", 8));
//        AGH.addClass("metallurgy", 100);

//        AGH.garbageClassMap.get("Programming").addStudent(s1);
//        AGH.garbageClassMap.get("Programming").addStudent(s2);
//        AGH.garbageClassMap.get("Programming").addStudent(s3);
//        AGH.garbageClassMap.get("Programming").addStudent(s4);
//        AGH.garbageClassMap.get("Programming").addStudent(s5);


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI(AGH);
            }
        });

        //entityManager.close();
        //entityManagerFactory.close();
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

    public static void StartUp(ClassContainer college) {
        List<StudentEntity> TempStudentsList = entityManager.createQuery("SELECT a FROM StudentEntity a").getResultList();
        List<ClassEntity> tempCassEntityList = entityManager.createQuery("SELECT a FROM ClassEntity a").getResultList();
        List<ClassContainerEntity> tempClassContainerEntityList = entityManager.createQuery("select a FROM ClassContainerEntity a").getResultList();

        college.name = tempClassContainerEntityList.get(0).getName();
        college.ID = tempClassContainerEntityList.get(0).getId();

        for (ClassEntity classEntity : tempCassEntityList) {
            college.addClass(new Class(classEntity.getName(), classEntity.getCapacity(), classEntity.getId()));
            //System.out.println(classEntity);

        }

        for (StudentEntity studentEntity : TempStudentsList) {
//            college.garbageClassMap.get(tempCassEntityList.get(studentEntity.getClassId() - 1).getName()).
            college.listOfClasses.get(studentEntity.getClassId() - 1).
                    setStudent(new Student(studentEntity.getName(), studentEntity.getSurname()));
            System.out.println(studentEntity);
        }
    }

    static void PrintStudents() {
        System.out.println("List of Students: ");
        for (StudentEntity studentEntity : studentsList) {
            System.out.println(studentEntity);
        }
    }
//    static void PrintClasses(){
//        System.out.println("List of Classes: ");
//        for (ClassEntity classEntity1 : classEntityList) {
//            System.out.println(classEntity1);
//        }
//    }
//
//    static void PrintClassContainers(){
//        System.out.println("List of Class Containers: ");
//        for (ClassContainerEntity class1: classContainerEntityList) {
//            System.out.println(class1);
//        }
//    }

//    static void addStudent() {
//        Scanner in = new Scanner(System.in);
//        System.out.print("Type in a figure name: ");
//
//        System.out.println("Type in a name: ");
//        String name = in.nextLine();
//        System.out.println("Type in a surname: ");
//        String surname = in.nextLine();
//        System.out.println("Type in a class ID: ");
//        int classID = Integer.parseInt(in.nextLine());
//
//        try {
//            transaction.begin();
//
//            StudentEntity studentEntity = new StudentEntity();
//            studentEntity.setName(name);
//            studentEntity.setSurname(surname);
//            studentEntity.setClassId(classID);
//
//            entityManager.persist(studentEntity);
//
//            transaction.commit();
//        } finally {
//            if (transaction.isActive()) {
//                transaction.rollback();
//            }
//        }
//
//        StartUp();
//    }
}