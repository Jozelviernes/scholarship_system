/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scholarship_system;

/**
 *
 * @author Admin
 */import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfTest {
    public static void main(String[] args) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("hello_openpdf.pdf"));
            document.open();
            document.add(new Paragraph("Hello, OpenPDF!"));
            document.close();
            System.out.println("PDF created!");
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
