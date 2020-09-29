package lib;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Record {
    private int teacherId, classId, subjectId, slotId;
}
