import java.util.ArrayList;
import java.util.Date;

    public static class AttendanceManager {
        private ArrayList<Attendance> students;

        public AttendanceManager() {
            this.students = new ArrayList<>();
        }

        public void markAttendance(int idNumber, Date date) {
            for (Attendance student : students) {
                if (student.getIdNumber() == idNumber) {
                    student.addAttendanceDate(date);
                    return;
                }
            }
            System.out.println("Student with ID " + idNumber + " not found.");
        }

        public void displayAllAttendance() {
            for (Attendance student : students) {
                student.displayAttendanceDetails();
            }
        }

        public void displayStudentAttendance(int idNumber) {
            for (Attendance student : students) {
                if (student.getIdNumber() == idNumber) {
                    student.displayAttendanceDetails();
                    return;
                }
            }
            System.out.println("Student with ID " + idNumber + " not found.");
        }
    }
