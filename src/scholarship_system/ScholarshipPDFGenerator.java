/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scholarship_system;

/**
 *
 * @author Admin
 */
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScholarshipPDFGenerator {

    public static void generate(int scholarshipId, String scholarshipName, Connection connection) {
        List<Object[]> students = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(
            "SELECT s.student_number, s.first_name, s.last_name, s.course, s.year_level " +
            "FROM students s WHERE s.scholarship_id = ?")) {

            stmt.setInt(1, scholarshipId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                students.add(new Object[]{
                    rs.getString("student_number"),
                    fullName,
                    rs.getString("course"),
                    rs.getInt("year_level")
                });
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Failed to fetch student list.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (students.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "No students assigned to this scholarship.", "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            Document document = new Document();
            String fileName = "scholarship_students_" + scholarshipId + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            document.open();
            document.add(new Paragraph("List of Scholars: " + scholarshipName, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{3, 5, 4, 2});
            table.addCell("Student Number");
            table.addCell("Full Name");
            table.addCell("Course");
            table.addCell("Year");

            for (Object[] student : students) {
                table.addCell(student[0].toString());
                table.addCell(student[1].toString());
                table.addCell(student[2].toString());
                table.addCell(student[3].toString());
            }

            document.add(table);
            document.close();

            javax.swing.JOptionPane.showMessageDialog(null, "PDF generated: " + fileName);
            Desktop.getDesktop().open(new File(fileName));

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Error generating PDF", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
