package data;

import lib.*;
import lib.Class;
import model.Cofficient;
import model.GaParameter;
import model.InputData;
import model.Model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

public class DataReader {
    public static Vector<Teacher> getTeachersData(String filename) {
        Vector<Teacher> teachers = new Vector<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            String str = "";
            int id = 0;
            br.readLine();
            while ((str = br.readLine()) != null && str.length() != 0) {
//                System.out.println(str);
                String datas[] = str.split(",");
                teachers.add(new Teacher(datas[1], datas[1], id++));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teachers;

    }

    public static int getSlotIdByName(Vector<Slot> slots, String name) {
        for (Slot slot : slots) {
            if (slot.getName().equals(name)) return slot.getId();
        }
        return -1;
    }

    public static Slot getSlotByName(Vector<Slot> slots, String name) {
        for (Slot slot : slots) {
            if (slot.getName().equals(name)) return slot;
        }
        return null;
    }

    public static int getTeacherIdByEmail(Vector<Teacher> teachers, String email) {
        for (Teacher teacher : teachers) {
            if (teacher.getEmail().equalsIgnoreCase(email)) return teacher.getId();
        }
        return -1;
    }

    public static int getSubjectIdByName(Vector<Subject> subjects, String name) {
        for (Subject subject : subjects) {
            if (subject.getName().equalsIgnoreCase(name)) {
                return subject.getId();
            }
        }
        return -1;
    }

    public static Subject getSubjectByName(Vector<Subject> subjects, String name) {
        for (Subject subject : subjects) {
            if (subject.getName().equalsIgnoreCase(name)) {
                return subject;
            }
        }
        return null;
    }

    public static double[][] getRegisteredSlot(Vector<Teacher> teachers, Vector<Slot> slots, String filename) {
        double res[][] = new double[teachers.size()][slots.size()];
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            String str = "";
            int id = 0;
            br.readLine();
            while ((str = br.readLine()) != null && str.length() != 0) {
                String datas[] = str.split(",");
                String teacherEmail = datas[1];
                String tSlots = datas[2];
                String slotStrs[] = tSlots.split("\\+");
                for (String slot : slotStrs)
                    res[getTeacherIdByEmail(teachers, teacherEmail)][getSlotIdByName(slots, slot)] = 1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    static boolean isOnlineSubject(String str) {

        return Character.isAlphabetic(str.charAt(str.length() - 1));
    }

    public static Vector<Subject> getSubjects(String filename) {
        Vector<Subject> res = new Vector<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            String str = "";
            int id = 0;
            br.readLine();
            Set<String> set = new TreeSet<>();
            while ((str = br.readLine()) != null && str.length() != 0) {
                String datas[] = str.split(",");
                String substrs[] = datas[3].split("\\+");
                for (String sub : substrs) {
                    if (sub.length() != 0 && !isOnlineSubject(sub) && !set.contains(sub)) {
                        res.add(new Subject(sub, id++));
                        set.add(sub);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static double[][] getRegisteredSubject(Vector<Teacher> teachers, Vector<Subject> subjects, String filename) {
        double res[][] = new double[teachers.size()][subjects.size()];
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            String str = "";
            int id = 0;
            br.readLine();
            while ((str = br.readLine()) != null && str.length() != 0) {
                String datas[] = str.split(",");
                String teacherEmail = datas[1];
//                String tSlots = datas[2];
                String substrs[] = datas[3].split("\\+");
                for (String sub : substrs) {
                    if (sub.length() != 0 && !isOnlineSubject(sub))
                        res[getTeacherIdByEmail(teachers, teacherEmail)][getSubjectIdByName(subjects, sub)] = 1;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < teachers.size(); i++) {
            for (int j = 0; j < subjects.size(); j++) {
                System.out.print(res[i][j] + " ");
            }
            System.out.println();
        }

        return res;
    }

    static String getBuilding(String room) {
        return room.split("-")[0];
    }

    public static InputData getData() {
        Vector<Teacher> teachers = new Vector<>();
        String test_case = "real_summer";
        String registerSlotPath = "src\\main\\java\\data\\teacher_slot_" + test_case + ".xml";
        String registerSubjectPath = "src\\main\\java\\data\\teacher_subject_" + test_case + ".xml";
        String classPath = "src\\main\\java\\data\\class_" + test_case + ".xml";
        GaParameter gaParameter = new GaParameter();
        gaParameter.setMutationRate(0.9);
        gaParameter.setPopulationSize(100);
        gaParameter.setTournamentSize(7);
        gaParameter.setConvergenceCheckRange(70);
        gaParameter.setModelType(Model.LINER_SCALARIZATION);
        Cofficient coff = new Cofficient();
        coff.setFulltimeCoff(0.5);
        coff.setParttimeCoff(0.5);
        coff.setHardConstraintCoff(0.7);
        coff.setSoftConstraintCoff(0.3);
        coff.setSlotCoff(0.25);
        coff.setSubjectCoff(0.25);
        coff.setNumberOfClassCoff(0.3);
        coff.setNumberOfSessionCoff(0.2);
        coff.setDistanceCoff(0.00);
        coff.setConsicutiveClassCoff(0.00);
        coff.setSatisfactionSumCoff(1.0);
        coff.setStdCoff(0.0);
        try {
            File f = new File(registerSlotPath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder buider = factory.newDocumentBuilder();
            Document doc = buider.parse(f);

            Element lst = doc.getDocumentElement();

            NodeList teacherList = lst.getElementsByTagName("Row");

            Vector<ExpectedSlot> registeredSlots = new Vector<>();
            Integer[] expectedNumberOfClass = new Integer[teacherList.getLength() - 1];
            Integer[] consecutiveSlotLimit = new Integer[teacherList.getLength() - 1];
            Integer[] quota = new Integer[teacherList.getLength() - 1];

            for (int i = 1; i < teacherList.getLength(); i++) {

                Node node = teacherList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;
                    String teacherName = e.getElementsByTagName("Cell").item(0).getTextContent();
                    int teacherType = Integer.parseInt(e.getElementsByTagName("Cell").item(13).getTextContent());
                    teachers.add(new Teacher(teacherName, teacherName, i - 1, teacherType));
                    for (int j = 1; j < 11; j++) {
                        int sat = Integer.parseInt(e.getElementsByTagName("Cell").item(j).getTextContent());
                        if (sat > 0) registeredSlots.add(new ExpectedSlot(i - 1, j - 1, sat));
                    }
                    int ec = Integer.parseInt(e.getElementsByTagName("Cell").item(11).getTextContent());
                    expectedNumberOfClass[i - 1] = ec;
                    int ccl = Integer.parseInt(e.getElementsByTagName("Cell").item(12).getTextContent());
                    consecutiveSlotLimit[i - 1] = ccl;
                    int qt = Integer.parseInt(e.getElementsByTagName("Cell").item(14).getTextContent());
                    quota[i - 1] = qt;
                }
            }

            doc = buider.parse(new File(registerSubjectPath));
            lst = doc.getDocumentElement();
            teacherList = lst.getElementsByTagName("Row");

            Vector<Subject> subjects = new Vector<>();

            Element firstRowElement = (Element) teacherList.item(0);
            for (int i = 1; i < firstRowElement.getElementsByTagName("Cell").getLength(); i++) {
                String subjectName = firstRowElement.getElementsByTagName("Cell").item(i).getTextContent();
                subjects.add(new Subject(subjectName, i - 1));
            }
            Vector<ExpectedSubject> registeredSubjects = new Vector<>();
            for (int i = 1; i < teacherList.getLength(); i++) {
                if (teacherList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) teacherList.item(i);
                    for (int j = 0; j < subjects.size(); j++) {
                        int sat = Integer.parseInt(e.getElementsByTagName("Cell").item(j + 1).getTextContent());
                        if (sat > 0) registeredSubjects.add(new ExpectedSubject(i - 1, j, sat));
                    }
                }
            }
            System.out.println(1);

            Vector<SlotGroup> slots = new Vector<>();
            SlotGroup m246 = new SlotGroup(3, 0);
            m246.addSlot(new Slot("M1", 0));
            m246.addSlot(new Slot("M2", 1));
            m246.addSlot(new Slot("M3", 2));
            SlotGroup e246 = new SlotGroup(3, 1);

            e246.addSlot(new Slot("E1", 3));
            e246.addSlot(new Slot("E2", 4));
            e246.addSlot(new Slot("E3", 5));

            SlotGroup m35 = new SlotGroup(1, 2);

            m35.addSlot(new Slot("M4", 6));
            m35.addSlot(new Slot("M5", 7));

            SlotGroup e35 = new SlotGroup(1, 3);

            e35.addSlot(new Slot("E4", 8));
            e35.addSlot(new Slot("E5", 9));
            slots.add(m246);
            slots.add(e246);
            slots.add(m35);
            slots.add(e35);


            Vector<Class> classes = new Vector<>();
            Vector<Slot> slotList = SlotGroup.getSlotList(slots);
            doc = buider.parse(new File(classPath));
            Element classList = doc.getDocumentElement();
            NodeList nodelist = classList.getElementsByTagName("Row");
            for (int i = 1; i < nodelist.getLength(); i++) {
                Node node = nodelist.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;
                    String studentGroup = e.getElementsByTagName("Cell").item(0).getTextContent();
                    String subjectName = e.getElementsByTagName("Cell").item(1).getTextContent();
                    String slotName = e.getElementsByTagName("Cell").item(2).getTextContent();
                    String roomName = e.getElementsByTagName("Cell").item(3).getTextContent();
                    classes.add(new Class(studentGroup, getSlotByName(slotList, slotName).getId(), getSubjectByName(subjects, subjectName).getId(),
                            new Room(roomName, 1, roomName.split("-")[0]), i - 1, 1));
                }
            }
            for (int i = 0; i < teachers.size(); i++) {
                teachers.get(i).setExpectedNumberOfClass(expectedNumberOfClass[i]);
                teachers.get(i).setConsecutiveSlotLimit(consecutiveSlotLimit[i]);
                teachers.get(i).setQuota(quota[i]);
            }
            gaParameter.setCofficient(coff);
            InputData inputData = new InputData(teachers, slots, subjects, classes, registeredSlots, registeredSubjects, gaParameter);
            return inputData;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        DataReader.getData();
    }
}
