package data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OutputRecord {
    private int genId;
    private double fitness;
    private double objectiveValue;
    private int violations;
    private double averageFitness;
}
