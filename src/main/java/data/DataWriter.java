package data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Vector;

public class DataWriter {
    public static void writeToCsv(Vector<OutputRecord> data, String filename) {
        try {
            String path = "src\\main\\java\\data\\" + filename;
            PrintWriter pr = new PrintWriter(new FileOutputStream(path));
            for(OutputRecord outputRecord:data) {
                pr.print(outputRecord.getGenId() + ",");
                pr.print(outputRecord.getFitness() + ",");
                pr.print(outputRecord.getAverageFitness() + ",");
                pr.print(outputRecord.getViolations() + ",");
                pr.println(outputRecord.getObjectiveValue());
            }
            pr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
