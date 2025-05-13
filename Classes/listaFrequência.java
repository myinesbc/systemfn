import java.util.ArrayList;
import java.util.Date;

public class DailyAttendanceSystem {

    public static class DailyAttendance {
        private Date classDate;
        private ArrayList<StudentAttendance> studentAttendances;

        public DailyAttendance(Date classDate) {
            this.classDate = classDate;
            this.studentAttendances = new ArrayList<>();
        }

        public Date getClassDate() {
            return classDate;
        }

        public ArrayList<StudentAttendance> getStudentAttendances() {
            return studentAttendances;
        }

        public void addStudentAttendance(int idNumber, String status) {
            studentAttendances.add(new StudentAttendance(idNumber, status));
        }

        public void editStudentAttendance(int idNumber, String newStatus) {
            for (StudentAttendance attendance : studentAttendances) {
                if (attendance.getIdNumber() == idNumber) {
                    attendance.setStatus(newStatus);
                    return;
                }
            }
            System.out.println("Attendance record for student ID " + idNumber + " not found.");
        }

        public void displayDailyAttendance() {
            System.out.println("Class Date: " + classDate);
            for (StudentAttendance attendance : studentAttendances) {
                System.out.println("Student ID: " + attendance.getIdNumber() + " - Status: " + attendance.getStatus());
            }
        }
    }

    public static class StudentAttendance {
        private int idNumber;
        private String status;

        public StudentAttendance(int idNumber, String status) {
            this.idNumber = idNumber;
            this.status = status;
        }

        public int getIdNumber() {
            return idNumber;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class AttendanceManager {
        private ArrayList<DailyAttendance> dailyAttendances;

        public AttendanceManager() {
            this.dailyAttendances = new ArrayList<>();
        }

        public void addDailyAttendance(Date classDate) {
            dailyAttendances.add(new DailyAttendance(classDate));
        }

        public void markStudentAttendance(Date classDate, int idNumber, String status) {
            for (DailyAttendance dailyAttendance : dailyAttendances) {
                if (dailyAttendance.getClassDate().equals(classDate)) {
                    dailyAttendance.addStudentAttendance(idNumber, status);
                    return;
                }
            }
            System.out.println("Class on " + classDate + " not found.");
        }

        public void editStudentAttendance(Date classDate, int idNumber, String newStatus) {
            for (DailyAttendance dailyAttendance : dailyAttendances) {
                if (dailyAttendance.getClassDate().equals(classDate)) {
                    dailyAttendance.editStudentAttendance(idNumber, newStatus);
                    return;
                }
            }
            System.out.println("Class on " + classDate + " not found.");
        }

        public void displayAllDailyAttendance() {
            for (DailyAttendance dailyAttendance : dailyAttendances) {
                dailyAttendance.displayDailyAttendance();
            }
        }
    }
}
