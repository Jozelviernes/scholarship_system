/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import java.io.IOException;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class CreateStudentDetailsPDF {
    
    public static void createPDF(String fileName, String studentNumber, String fullName, 
                               String course, int yearLevel, double gpa) {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);
            
            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                // Title with bold font
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
                content.newLineAtOffset(50, 750);
                content.showText("Student Details");
                content.endText();
                
                // Student information with regular font
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                content.newLineAtOffset(50, 720);
                content.showText("Student Number: " + studentNumber);
                content.newLineAtOffset(0, -20);
                content.showText("Full Name: " + fullName);
                content.newLineAtOffset(0, -20);
                content.showText("Course: " + course);
                content.newLineAtOffset(0, -20);
                content.showText("Year Level: " + yearLevel);
                content.newLineAtOffset(0, -20);
                content.showText("GPA: " + String.format("%.2f", gpa));
                content.endText();
            }
            
            doc.save(fileName);
            System.out.println("PDF created successfully: " + fileName);
            
        } catch (IOException e) {
            System.err.println("Error creating PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Enhanced version with better formatting and layout
    public static void createEnhancedPDF(String fileName, String studentNumber, String fullName, 
                                       String course, int yearLevel, double gpa) {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);
            
            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                float yPosition = 750;
                float margin = 50;
                float lineHeight = 20;
                
                // Title
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
                content.newLineAtOffset(margin, yPosition);
                content.showText("STUDENT INFORMATION REPORT");
                content.endText();
                
                yPosition -= 40;
                
                // Underline
                content.moveTo(margin, yPosition);
                content.lineTo(margin + 300, yPosition);
                content.stroke();
                
                yPosition -= 30;
                
                // Student details with labels and values
                String[][] details = {
                    {"Student Number:", studentNumber},
                    {"Full Name:", fullName},
                    {"Course:", course},
                    {"Year Level:", String.valueOf(yearLevel)},
                    {"GPA:", String.format("%.2f", gpa)}
                };
                
                for (String[] detail : details) {
                    // Label (bold)
                    content.beginText();
                    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                    content.newLineAtOffset(margin, yPosition);
                    content.showText(detail[0]);
                    content.endText();
                    
                    // Value (regular)
                    content.beginText();
                    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                    content.newLineAtOffset(margin + 120, yPosition);
                    content.showText(detail[1]);
                    content.endText();
                    
                    yPosition -= lineHeight;
                }
                
                // Footer
                yPosition = 100;
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE), 10);
                content.newLineAtOffset(margin, yPosition);
                content.showText("Generated on: " + java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                content.endText();
            }
            
            doc.save(fileName);
            System.out.println("Enhanced PDF created successfully: " + fileName);
            
        } catch (IOException e) {
            System.err.println("Error creating enhanced PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        // Create basic PDF
        createPDF("student_details_basic.pdf", "2025-1234", "Juan Dela Cruz", 
                 "Computer Science", 3, 3.75);
        
        // Create enhanced PDF
        createEnhancedPDF("student_details_enhanced.pdf", "2025-1234", "Juan Dela Cruz", 
                         "Computer Science", 3, 3.75);
    }
}