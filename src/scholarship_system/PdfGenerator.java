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
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.io.FileInputStream;
import java.io.IOException;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import java.io.*;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.draw.LineSeparator;
import java.awt.Color;public class PdfGenerator {
    // This method generates a PDF file with student details
    public static void generateStudentPdf(String filePath, String studentNumber, String fullName, String course,
                                          int year, double gpa, String scholarshipName, String scholarshipDesc,
                                          String father, String mother, String income, String contact,
                                          String email, String phone, String address) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // Modern header styling
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Color.BLUE);
            Paragraph header = new Paragraph("STUDENT PROFILE", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(5f);
            document.add(header);
            
            // Subtitle
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.GRAY);
            Paragraph subtitle = new Paragraph("Academic & Personal Information", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(5f);
            document.add(subtitle);
            
            // Add spacing
            document.add(Chunk.NEWLINE);
            
            // Modern fonts
            Font sectionHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.DARK_GRAY);
            Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK);
            Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.DARK_GRAY);
            
            // Academic Information Section
            document.add(new Paragraph("ACADEMIC INFORMATION", sectionHeaderFont));
            document.add(Chunk.NEWLINE);
            
            PdfPTable studentTable = new PdfPTable(2);
            studentTable.setWidthPercentage(100);
            studentTable.setWidths(new float[]{30f, 70f});
            
            addTableRow(studentTable, "Student ID:", studentNumber, labelFont, valueFont);
            addTableRow(studentTable, "Full Name:", fullName, labelFont, valueFont);
            addTableRow(studentTable, "Course:", course, labelFont, valueFont);
            addTableRow(studentTable, "Year Level:", String.valueOf(year), labelFont, valueFont);
            addTableRow(studentTable, "GPA:", String.format("%.2f", gpa), labelFont, valueFont);
            
            document.add(studentTable);
            document.add(Chunk.NEWLINE);
            
            // Scholarship Information Section
            document.add(new Paragraph("SCHOLARSHIP DETAILS", sectionHeaderFont));
            document.add(Chunk.NEWLINE);
            
            PdfPTable scholarshipTable = new PdfPTable(2);
            scholarshipTable.setWidthPercentage(100);
            scholarshipTable.setWidths(new float[]{30f, 70f});
            
            addTableRow(scholarshipTable, "Scholarship:", 
                scholarshipName != null ? scholarshipName : "Not Applicable", labelFont, valueFont);
            addTableRow(scholarshipTable, "Description:", 
                scholarshipDesc != null ? scholarshipDesc : "Not Applicable", labelFont, valueFont);
            
            document.add(scholarshipTable);
            document.add(Chunk.NEWLINE);
            
            // Family Information Section
            document.add(new Paragraph("FAMILY INFORMATION", sectionHeaderFont));
            document.add(Chunk.NEWLINE);
            
            PdfPTable familyTable = new PdfPTable(2);
            familyTable.setWidthPercentage(100);
            familyTable.setWidths(new float[]{30f, 70f});
            
            addTableRow(familyTable, "Father's Name:", father, labelFont, valueFont);
            addTableRow(familyTable, "Mother's Name:", mother, labelFont, valueFont);
            addTableRow(familyTable, "Family Income:", income, labelFont, valueFont);
            
            document.add(familyTable);
            document.add(Chunk.NEWLINE);
            
            // Contact Information Section
            document.add(new Paragraph("CONTACT INFORMATION", sectionHeaderFont));
            document.add(Chunk.NEWLINE);
            
            PdfPTable contactTable = new PdfPTable(2);
            contactTable.setWidthPercentage(100);
            contactTable.setWidths(new float[]{30f, 70f});
            
            addTableRow(contactTable, "Contact Person:", contact, labelFont, valueFont);
            addTableRow(contactTable, "Email Address:", email, labelFont, valueFont);
            addTableRow(contactTable, "Phone Number:", phone, labelFont, valueFont);
            addTableRow(contactTable, "Address:", address, labelFont, valueFont);
            
            document.add(contactTable);
            
            // Footer
            document.add(Chunk.NEWLINE);
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.GRAY);
            Paragraph footer = new Paragraph("Generated on " + new java.util.Date().toString(), footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(10f);
            document.add(footer);
            
            document.close();
            System.out.println("PDF created successfully: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Helper method to add table rows
    private static void addTableRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5f);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5f);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
    
    public static void printPdf(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.PDF, null);
            PrintRequestAttributeSet printAttributes = new HashPrintRequestAttributeSet();
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            if (printService != null) {
                DocPrintJob printJob = printService.createPrintJob();
                printJob.print(pdfDoc, printAttributes);
                System.out.println("Printing initiated for " + filePath);
            } else {
                System.err.println("No default print service found.");
            }
        } catch (PrintException | IOException e) {
            e.printStackTrace();
        }
    }
}