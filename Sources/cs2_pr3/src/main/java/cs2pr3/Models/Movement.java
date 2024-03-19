package cs2pr3.Models;

import java.sql.Timestamp;

public class Movement {
    private String visitNumber;
    private String service;
    private String bed;
    private String room;

    public Movement(String visitNumber, String service, String bed, String room, Timestamp admitTime) {
        this.visitNumber = visitNumber;
        this.service = service;
        this.bed = bed;
        this.room = room;
        this.admitTime = admitTime;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Timestamp getAdmitTime() {
        return admitTime;
    }

    public void setAdmitTime(Timestamp admitTime) {
        this.admitTime = admitTime;
    }

    private Timestamp admitTime;
}
