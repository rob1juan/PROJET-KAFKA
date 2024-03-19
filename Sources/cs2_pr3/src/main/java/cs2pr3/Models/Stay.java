package cs2pr3.Models;

import java.sql.Timestamp;

public class Stay {
    private String visitNumber;
    private Timestamp startTime;
    private Timestamp endTime;

    public Stay(String visitNumber, Timestamp startTime, Timestamp endTime) {
        this.visitNumber = visitNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
