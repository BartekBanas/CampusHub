import entity.ClassEntity;
import entity.ClassContainerEntity;
import entity.RatingEntity;
import entity.StudentEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    public static EntityManager entityManager = entityManagerFactory.createEntityManager();
    public static EntityTransaction transaction = entityManager.getTransaction();

    public static List<StudentEntity> studentEntityList = new ArrayList<>();
    public static List<ClassEntity> classEntityList = new ArrayList<>();
    public static List<ClassContainerEntity> classContainerEntityList = new ArrayList<>();

    public static List<RatingEntity> ratingEntityList = new ArrayList<>();

    public static void main(String[] args) {
        ClassContainer AGH = new ClassContainer();

        StartUp(AGH);
        //PrintStudents();

        System.out.print("Program operating college database\n");

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
        ratingEntityList = entityManager.createQuery("SELECT a FROM RatingEntity a").getResultList();

        college.name = tempClassContainerEntityList.get(0).getName();
        college.ID = tempClassContainerEntityList.get(0).getId();

        for (ClassEntity classEntity : tempCassEntityList) {
            classEntityList.add(classEntity);
            college.setClass(new Class(classEntity.getName(), classEntity.getCapacity(), classEntity.getId()));
        }

        for (StudentEntity studentEntity : TempStudentsList) {
            studentEntityList.add(studentEntity);

            for (Class clas : college.listOfClasses) {
                if(clas.ID == studentEntity.getClassId()) {
                    clas.setStudent(new Student(studentEntity.getName(), studentEntity.getSurname(), studentEntity.getId()));
                }
            }
        }
    }

    static void PrintStudents() {
        System.out.println("List of Students: ");
        for (StudentEntity studentEntity : studentEntityList) {
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