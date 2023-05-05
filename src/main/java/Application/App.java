package Application;

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
        ClassContainer AGH = new ClassContainer("AGH");

        StartUp(AGH);
        //PrintStudents();

        System.out.print("\nProgram operating college database with realtime connection to a MySql database through hibernate\n\n");

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
                if (clas.ID == studentEntity.getClassId()) {
                    clas.setStudent(new Student(studentEntity.getId(), studentEntity.getName(), studentEntity.getSurname(), studentEntity.getPoints()));
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
}