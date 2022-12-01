import entity.ClassEntity;
import entity.StudentEntity;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Window {
    private JPanel rootPanel;
    private JTextPane titleMaybe;
    private JTable listOfClasses;
    private JTable listOfStudents;
    private JButton deleteButton;
    private JButton addStudentButton;
    private JTextField addStudentField;
    private JButton sortDataButton;
    private JButton addClassButton;
    private JTextField classNameToAddTextField;
    private JButton addPointsButton;
    private JTextField pointsToAddTextfield;
    private JTextField filter_textbox_;
    private JTextArea searchStudentTextArea;
    private JButton changePropertiesButton;
    public ClassContainer collage;
    private static Class currentClass;

    public Window(ClassContainer college) {
        this.collage = college;
        createClassTable();
        listOfClasses.addMouseListener(new MouseListener() //Opening list of a selected class
        {
            @Override
            public void mouseClicked(MouseEvent e) {
//                int i = 0;
//                for (Map.Entry<String, Class> entry : collage.classes.entrySet())
//                {
//                    if(i == listOfClasses.getSelectedRow()) {
//                        currentClass = entry.getValue();
//                    }   i++;
//                }

                currentClass = college.listOfClasses.get(listOfClasses.getSelectedRow());

                createStudentsTable();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel tblModel = (DefaultTableModel) listOfStudents.getModel();

                if (listOfStudents.getSelectedRowCount() == 0) {
                    college.listOfClasses.remove(currentClass);

                    try {
                        App.transaction.begin();

                        for (ClassEntity clas : App.classEntityList) {
                            if ((currentClass.ID) == clas.getId())

                                for (StudentEntity student : App.studentsList) {
                                    if(student.getClassId() == clas.getId()) {
                                        App.entityManager.remove(student);
                                    }
                                }

                                App.entityManager.remove(clas);
                        }

                        App.transaction.commit();
                    } finally {
                        if (App.transaction.isActive()) {
                            App.transaction.rollback();
                        }
                    }

                    UpdateTables();
                }

                if (listOfStudents.getSelectedRowCount() == 1) {

                    try {
                        App.transaction.begin();

                        for (StudentEntity student : App.studentsList) {
                            if ((currentClass.studentsList.get(listOfStudents.getSelectedRow()).ID) == student.getId())
                                App.entityManager.remove(student);
                        }

                        App.transaction.commit();
                    } finally {
                        if (App.transaction.isActive()) {
                            App.transaction.rollback();
                        }
                    }

                    currentClass.studentsList.remove(listOfStudents.getSelectedRow());

                    UpdateTables();
                }
            }
        });

        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentClass == null) {
                    throw new RuntimeException("No class selected\n");
                }

                String[] words = addStudentField.getText().split(" ", 2);
                currentClass.addStudent(new Student(words[0], words[1]));

                UpdateTables();
            }
        });

        addClassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                college.addClass(classNameToAddTextField.getText());

                UpdateTables();
            }
        });

        sortDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                college.listOfClasses.forEach(Class::sortByName);
                college.sortClasses();

                UpdateTables();
            }
        });

        addPointsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double pointsToAdd = Double.parseDouble(pointsToAddTextfield.getText());

                currentClass.studentsList.get(listOfStudents.getSelectedRow()).points += pointsToAdd;

                UpdateTables();
            }
        });

        filter_textbox_.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    String word = filter_textbox_.getText();

                    if (currentClass != null) {
                        createFilteredStudentsTable(currentClass.searchPartial(word));
                    }
                }
            }
        });

        changePropertiesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listOfStudents.getSelectedRowCount() == 0) {
                    String newName = JOptionPane.showInputDialog("Enter a new name");

                    if (!newName.isEmpty()) {
                        currentClass.className = newName;
                    }

                    UpdateTables();
                }

                if (listOfStudents.getSelectedRowCount() == 1) {
                    try {
                        String newName = JOptionPane.showInputDialog("Enter a new name");
                        if (!newName.isEmpty()) {
                            currentClass.studentsList.get(listOfStudents.getSelectedRow()).name = newName;
                        }
                    } catch (Exception f) {
                        System.out.println("Not so much of a name, it it?");
                    }

                    try {
                        String newSurname = JOptionPane.showInputDialog("Enter a new surname");
                        ;
                        if (!newSurname.isEmpty()) {
                            currentClass.studentsList.get(listOfStudents.getSelectedRow()).surname = newSurname;
                        }
                    } catch (Exception f) {
                        System.out.println("Not so much of a surname, it it?");
                    }

                    try {
                        String newPoints = JOptionPane.showInputDialog("Enter a new amount of points");
                        if (!newPoints.isEmpty()) {
                            currentClass.studentsList.get(listOfStudents.getSelectedRow()).points = Double.parseDouble(newPoints);
                        }
                    } catch (Exception f) {
                        System.out.println("Not so much of a number, it it?");
                    }

                    UpdateTables();
                }
            }
        });
    }

    private void createStudentsTable() {
        if (currentClass == null) {
            return;
        }

        String[][] data = new String[currentClass.studentsList.size()][3];

        for (int i = 0; i < currentClass.studentsList.size(); i++) {
            data[i][0] = currentClass.studentsList.get(i).name;
            data[i][1] = currentClass.studentsList.get(i).surname;
            data[i][2] = String.valueOf(currentClass.studentsList.get(i).points);
        }

        String[] column = {"Name", "Surname", "Points"};

        listOfStudents.setModel(new DefaultTableModel(
                data,
                column
        ));
    }

    private void createFilteredStudentsTable(List<Student> studentsList) {
        if (currentClass == null) {
            return;
        }

        String[][] data = new String[studentsList.size()][3];

        for (int i = 0; i < studentsList.size(); i++) {
            data[i][0] = studentsList.get(i).name;
            data[i][1] = studentsList.get(i).surname;
            data[i][2] = String.valueOf(studentsList.get(i).points);
        }

        String[] column = {"Name", "Surname", "Points"};

        listOfStudents.setModel(new DefaultTableModel(
                data,
                column
        ));
    }

    private void createClassTable() {
        String[][] data = new String[collage.listOfClasses.size()][3];
        double amountOfStudents;
        double maximumCapacity;

        int i = 0;

        for (Class aClass : collage.listOfClasses) {
            amountOfStudents = aClass.studentsList.size();
            maximumCapacity = aClass.capacity;

            data[i][0] = aClass.className;
            data[i][1] = (aClass.studentsList.size()) + " / " + (aClass.capacity);
            data[i][2] = amountOfStudents / maximumCapacity * 100 + "%";
            i++;
        }

        String[] column = {"Name", "Students", "Filled %"};

        listOfClasses.setModel(new DefaultTableModel(
                data,
                column
        ));
    }

    public static JTable makeListOfStudents(ClassContainer collage) {
        int amountOfStudents = 0;

        for (Map.Entry<String, Class> entry : collage.garbageClassMap.entrySet()) {
            amountOfStudents += entry.getValue().studentsList.size();
        }

        String[][] data = new String[amountOfStudents][3];

        int i = 0;

        for (Map.Entry<String, Class> entry : collage.garbageClassMap.entrySet()) {
            for (Student student : entry.getValue().studentsList) {
                data[i][0] = student.name;
                data[i][1] = student.surname;
                data[i][2] = String.valueOf(student.points);

                i++;
            }
        }

        String[] column = {"Name", "Surname", "Points"};
        return new JTable(data, column);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    void UpdateTables() {
        createStudentsTable();
        createClassTable();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 600), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listOfStudents = new JTable();
        scrollPane1.setViewportView(listOfStudents);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel1.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listOfClasses = new JTable();
        scrollPane2.setViewportView(listOfClasses);
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(5, 5), null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Goudy Stout", Font.PLAIN, 18, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setOpaque(true);
        label1.setText("Class Table");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Goudy Stout", -1, 18, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Students Table");
        panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(10, 10), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(16, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        deleteButton = new JButton();
        deleteButton.setText("Delete");
        panel2.add(deleteButton, new com.intellij.uiDesigner.core.GridConstraints(14, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(12, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addStudentButton = new JButton();
        addStudentButton.setText("Add a student");
        panel3.add(addStudentButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addStudentField = new JTextField();
        addStudentField.setText("name and surname");
        panel3.add(addStudentField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(13, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 30), null, null, 0, false));
        sortDataButton = new JButton();
        sortDataButton.setText("Sort data");
        panel2.add(sortDataButton, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(10, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(10, 10), null, null, 0, false));
        classNameToAddTextField = new JTextField();
        classNameToAddTextField.setText("Class name to add");
        panel4.add(classNameToAddTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        addClassButton = new JButton();
        addClassButton.setText("Add Class");
        panel4.add(addClassButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 30), null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 30), null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addPointsButton = new JButton();
        addPointsButton.setText("Add Points");
        panel5.add(addPointsButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pointsToAddTextfield = new JTextField();
        panel5.add(pointsToAddTextfield, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer6, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 30), null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer7 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer7, new com.intellij.uiDesigner.core.GridConstraints(11, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 30), null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer8 = new com.intellij.uiDesigner.core.Spacer();
        panel6.add(spacer8, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        filter_textbox_ = new JTextField();
        filter_textbox_.setText("");
        panel6.add(filter_textbox_, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchStudentTextArea = new JTextArea();
        searchStudentTextArea.setText("Search student");
        panel6.add(searchStudentTextArea, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 20), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer9 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer9, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 30), null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel7, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        changePropertiesButton = new JButton();
        changePropertiesButton.setText("Change Properties");
        panel7.add(changePropertiesButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer10 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer10, new com.intellij.uiDesigner.core.GridConstraints(15, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(-1, 20), null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer11 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer11, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 30), new Dimension(20, 30), null, 0, false));
        titleMaybe = new JTextPane();
        titleMaybe.setText("I don't like it here");
        panel2.add(titleMaybe, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 30), new Dimension(150, 50), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer12 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer12, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(10, -1), null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer13 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer13, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(5, -1), null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}