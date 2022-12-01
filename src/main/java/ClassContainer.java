import java.util.*;

public class ClassContainer {
    public String name = "College";
    public int ID = 0;

    public ClassContainer() {
    }

    public ClassContainer(String name) {
        this.name = name;
    }

    Map<String, Class> garbageClassMap = new LinkedHashMap<>();

    List<Class> listOfClasses = new ArrayList<>();

    public void addClass(Class clas) {
        garbageClassMap.put(name, new Class(clas.className, clas.capacity, clas.ID));
        listOfClasses.add(garbageClassMap.get(name));
    }

    public void addClass(String name, int capacity) {
        garbageClassMap.put(name, new Class(name, capacity));
        listOfClasses.add(garbageClassMap.get(name));
    }

    public void addClass(String name) {
        garbageClassMap.put(name, new Class(name));
        listOfClasses.add(garbageClassMap.get(name));
    }

    public void removeClass(String name) {
        garbageClassMap.remove(name);
    }

    public List<Class> findEmpty() {
        List<Class> listToReturn = new ArrayList<>();

        for (Map.Entry<String, Class> entry : garbageClassMap.entrySet())
        {
            if(entry.getValue().studentsList.size() == 0) {
                listToReturn.add(entry.getValue());
            }
        }

        return listToReturn;
    }

    public void summary() {
        System.out.println("Summary of a Class Container:");
        for (Map.Entry<String, Class> entry : garbageClassMap.entrySet())
        {
            double amountOfStudents =  entry.getValue().studentsList.size();
            double maximumCapacity = entry.getValue().capacity;

            System.out.println("Class " + entry.getKey() + " is " +
                    amountOfStudents / maximumCapacity * 100 + "% full");
        }   System.out.println();
    }

    public void sortClasses() {
        Class temp;

        for (int i = 0; i < listOfClasses.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (listOfClasses.get(i).studentsList.size() > listOfClasses.get(j).studentsList.size()) {
                    temp = listOfClasses.get(i);
                    listOfClasses.set(i, listOfClasses.get(j));
                    listOfClasses.set(j, temp);
                }
            }
        }
    }
}
