import java.util.ArrayList;
import java.util.Date;

public class Attendance {
    private String fullName;
    private int idNumber;
    private ArrayList<Date> attendanceDates;
    private int totalClasses;

    public Attendance(String fullName, int idNumber, int totalClasses) {
        this.fullName = fullName;
        this.idNumber = idNumber;
        this.totalClasses = totalClasses;
        this.attendanceDates = new ArrayList<>();
    }

    public String getfullName() {
        return fullName;
    }

    public int idNumber() {
        return idNumber;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public ArrayList<Date> getAttendanceDates() {
        return attendanceDates;
    }

    public void addAttendanceDate(Date date) {
        attendanceDates.add(date);
    }

    public double calculateAttendancePercentage() {
        if (totalClasses == 0) {
            return 0.0;
        }
        return (attendanceDates.size() / (double) totalClasses) * 100;
    }

    public void displayAttendanceDetails() {
        System.out.println("Student Name: " + fullName);
        System.out.println("Student ID: " + idNumber);
        System.out.println("Total Classes: " + totalClasses);
        System.out.println("Attendance Percentage: " + calculateAttendancePercentage() + "%");
    }
}
