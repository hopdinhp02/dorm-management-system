package us.thedorm.models;

import java.util.List;

public class ResponseRoomStatus {
    private Room room;
    private List<Slot> availableSlot;
    private List<Slot> bookedSlot;
    private List<Slot> keepSlot;

    public ResponseRoomStatus() {
    }

    public ResponseRoomStatus(Room room, List<Slot> availableSlot, List<Slot> bookedSlot, List<Slot> keepSlot) {
        this.room = room;
        this.availableSlot = availableSlot;
        this.bookedSlot = bookedSlot;
        this.keepSlot = keepSlot;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Slot> getAvailableSlot() {
        return availableSlot;
    }

    public void setAvailableSlot(List<Slot> availableSlot) {
        this.availableSlot = availableSlot;
    }

    public List<Slot> getBookedSlot() {
        return bookedSlot;
    }

    public void setBookedSlot(List<Slot> bookedSlot) {
        this.bookedSlot = bookedSlot;
    }

    public List<Slot> getKeepSlot() {
        return keepSlot;
    }

    public void setKeepSlot(List<Slot> keepSlot) {
        this.keepSlot = keepSlot;
    }
}
