package lib;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class SlotGroup {
    private Vector<Slot> slots;
    private int coff;
    private int id;

    public SlotGroup(int coff, int id) {
        slots = new Vector<>();
        this.coff = coff;
        this.id = id;
    }

    public SlotGroup(Vector<Slot> slots, int coff, int id) {
        this.slots = slots;
        this.coff = coff;
        this.id = id;
    }

    public void addSlot(Slot slot) {
        this.slots.add(slot);
    }

    public Vector<Slot> getSlots() {
        return slots;
    }

    public void setSlots(Vector<Slot> slots) {
        this.slots = slots;
    }

    public int getCoff() {
        return coff;
    }

    public void setCoff(int coff) {
        this.coff = coff;
    }

    public static Vector<Slot> getSlotList(Vector<SlotGroup> slots) {
        Vector<Slot> res = new Vector<Slot>();
        for (SlotGroup slotGroup : slots) {
            res.addAll(slotGroup.getSlots());
        }

        Collections.sort(res, new Comparator<Slot>() {
            @Override
            public int compare(Slot o1, Slot o2) {
                if (o1.getId() < o2.getId()) {
                    return -1;
                }
                return 1;
            }
        });
        return res;
    }

    public int getId() {
        return this.id;
    }
}
