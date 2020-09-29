package data;

import model.Chromosome;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Vector;

public class Schedule extends JFrame {

    public static class MyRenderer extends JLabel implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus, int row,
                                                       int col) {
            DefaultTableCellRenderer renderer =
                    new DefaultTableCellRenderer();
            Component c = renderer.getTableCellRendererComponent(table,
                    value, isSelected, hasFocus, row, col);
            String s = "";
            ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
            return c;
        }
    }
    private JList generationList;
    private JTable scheduleTable;
    private JPanel paneMain;
    private JLabel fitnessLb;

    Vector<Chromosome> schedules;

    public Schedule() {
        super();
        this.schedules = new Vector<>();


//        this.paneMain.add(new JScrollPane(generationList));

        this.setContentPane(paneMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.pack();
        this.setVisible(true);
        scheduleTable.setRowHeight(100);
        scheduleTable.setFont(new Font(null, 1, 9));
        scheduleTable.setDefaultRenderer(String.class, new MyRenderer());
        generationList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int idx = generationList.getSelectedIndex();
                if (idx != -1) {
                    showSchedule(idx);
                }
            }

        });

    }

    public void showSchedule(int idx) {
        DefaultTableModel model = new DefaultTableModel();
        Chromosome c = this.schedules.get(idx);
        Vector<String> headers = new Vector<>();
        headers.add("giang vien");
        headers.add("M1");
        headers.add("M2");
        headers.add("M3");
        headers.add("E1");
        headers.add("E2");
        headers.add("E4");
        headers.add("M4");
        headers.add("M5");
        headers.add("E4");
        headers.add("E5");
        headers.add("expected number of class");
        headers.add("number of sessions");
        headers.add("satisfaction");
        Vector<Vector<String>> data = new Vector<>();
        Vector<String> row;
        for (int i = 0; i < c.getInputData().getTeachers().size(); i++) {
            row = new Vector<>();
            row.add(c.getInputData().getTeachers().get(i).getName());

            for (int j = 0; j < c.getGenes().size(); j++) {
                int classId = c.getGenes().get(j).get(i);
                if (classId != -1) {
                    row.add(c.getInputData().getClasses().get(classId).toString());
                } else row.add("");
            }
            row.add(c.getInputData().getTeachers().get(i).getExpectedNumberOfClass()+ "");
            row.add(c.getNumberOfSessionPerWeek(i) + "");
            row.add(c.calculateSatisfaction(i) + "");
            data.add(row);
        }
        scheduleTable.setModel(new DefaultTableModel(data, headers));
        this.fitnessLb.setText("Fitness: " + c.getFitness());
    }

    public void updateListGeneration() {

        DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < this.schedules.size(); i++) {
            model.addElement("Generation " + (i + 1));
        }
        generationList.setModel(model);
    }


    public void addSchedule(Chromosome c) {
        this.schedules.add(c);

        updateListGeneration();
    }
}
