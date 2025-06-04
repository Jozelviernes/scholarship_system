/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package scholarship_system;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;  // Correct import for ResultSet
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.sql.PreparedStatement;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.*;
import javax.swing.border.EmptyBorder;
import static scholarship_system.db_connection.getConnection;
import scholarship_system.scholarship.ButtonEditor;
import scholarship_system.scholarship.ButtonRenderer;
 import java.io.File;
 import java.io.IOException;
 import java.nio.file.Files;
 import java.nio.file.StandardCopyOption;
import javax.swing.text.Document;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
public class students extends javax.swing.JFrame {

private DefaultTableModel studentModel;


    /**
     * Creates new form admin_page
     */
public students() {
    
    if (session.getUsername() == null || session.getUsername().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please log in first.", "Access Denied", JOptionPane.WARNING_MESSAGE);
        
        // Redirect to login page
        login_page login = new login_page();
        login.setVisible(true);
        
        dispose(); // Close the dashboard
        return;
           }
    initComponents();


JPanel paginationPanel = new JPanel();

paginationPanel = new JPanel(new FlowLayout());
yourMainPanel.add(paginationPanel, BorderLayout.SOUTH);
yourMainPanel.revalidate();
yourMainPanel.repaint();




studentModel = new DefaultTableModel(
    new String[]{"Student Number", "Full Name", "Course", "Year", "Scholarship", "Actions"}, 0
);
students_table.setModel(studentModel);  // Make sure this is set once
// Set model on existing table
   jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

students_table.getColumnModel().getColumn(2).setPreferredWidth(300);
students_table.getColumnModel().getColumn(1).setPreferredWidth(150);
students_table.getColumnModel().getColumn(5).setPreferredWidth(200);
students_table.getColumnModel().getColumn(4).setPreferredWidth(180);
    students_table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
    students_table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

    loadStudents();
    comboBox_scholarships.addItem("All"); // Default option

try (Connection conn = db_connection.getConnection();
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT name FROM scholarships")) {

    while (rs.next()) {
        comboBox_scholarships.addItem(rs.getString("name"));
    }

} catch (SQLException e) {
    e.printStackTrace();
}

}


private void loadStudents() {
    allStudents = new ArrayList<>();

    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(
             "SELECT s.student_number, s.first_name, s.last_name, s.course, s.year_level, sc.name AS scholarship_name " +
             "FROM students s " +
             "LEFT JOIN scholarships sc ON s.scholarship_id = sc.id"
         )) {

        while (rs.next()) {
            String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
            allStudents.add(new Object[]{
                rs.getString("student_number"),
                fullName,
                rs.getString("course"),
                rs.getInt("year_level"),
                rs.getString("scholarship_name"),
                "Actions"
            });
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to load students data.");
    }

    // ✅ Make sure to set model AFTER initComponents() and before updateTablePage
    studentModel.setRowCount(0); // Clear all rows
    currentPage = 1;
    updateTablePage();
}


private void updateTablePage() {
    studentModel.setRowCount(0); // Clear previous rows
System.out.println("Before adding, model has rows: " + studentModel.getRowCount());

    int start = (currentPage - 1) * rowsPerPage;
    int end = Math.min(start + rowsPerPage, allStudents.size());

    for (int i = start; i < end; i++) {
        studentModel.addRow(allStudents.get(i));
    }


}




// Renderer for buttons in the table
class ButtonRenderer extends JPanel implements TableCellRenderer {
    private final JButton viewButton = new JButton("View");
    private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");

  public ButtonRenderer() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Vertical layout
    setOpaque(true);
    setAlignmentX(Component.CENTER_ALIGNMENT);
    setAlignmentY(Component.CENTER_ALIGNMENT);

    // Optional: Wrap buttons inside a horizontal FlowLayout panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
    buttonPanel.setOpaque(false); // So it blends with the cell background

    viewButton.setFocusable(false);
    viewButton.setBackground(new Color(70, 130, 180));
    viewButton.setForeground(Color.WHITE);
    viewButton.setFocusPainted(false);

    editButton.setFocusable(false);
    editButton.setBackground(new Color(46, 204, 113));
    editButton.setForeground(Color.WHITE);
    editButton.setFocusPainted(false);

    deleteButton.setFocusable(false);
    deleteButton.setBackground(new Color(231, 76, 60));
    deleteButton.setForeground(Color.WHITE);
    deleteButton.setFocusPainted(false);

    buttonPanel.add(viewButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);

    add(Box.createVerticalGlue()); // Push content to center vertically
    add(buttonPanel);
    add(Box.createVerticalGlue()); // Bottom padding
}

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
        return this;
    }
}

// Editor for buttons in the table
class ButtonEditor extends DefaultCellEditor {
    protected JPanel panel;
    protected JButton viewButton;
    protected JButton editButton;
    protected JButton deleteButton;
    private int currentRow;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

        viewButton = new JButton("View");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
viewButton.setBackground(new Color(70, 130, 180)); // Your chosen color
viewButton.setForeground(Color.WHITE);             // Text color
viewButton.setOpaque(true);
viewButton.setContentAreaFilled(true);
viewButton.setBorderPainted(false);
viewButton.setFocusPainted(false);


// Edit button (Green)
editButton.setBackground(new Color(46, 204, 113));
editButton.setForeground(Color.WHITE);
editButton.setOpaque(true);
editButton.setContentAreaFilled(true);
editButton.setBorderPainted(false);
editButton.setFocusPainted(false);

// Delete button (Red)
    deleteButton .setBackground(new Color(231, 76, 60));
    deleteButton .setForeground(Color.WHITE);
    deleteButton .setOpaque(true);
    deleteButton .setContentAreaFilled(true);
    deleteButton .setBorderPainted(false);
    deleteButton.setFocusPainted(false);


        panel.add(viewButton);
        panel.add(editButton);
        panel.add(deleteButton);

      viewButton.addActionListener(e -> {
    fireEditingStopped();
    String studentNumber = (String) studentModel.getValueAt(currentRow, 0);
    showStudentDetails(studentNumber);
});

     editButton.addActionListener(e -> {
    fireEditingStopped();
    String studentNumber = (String) studentModel.getValueAt(currentRow, 0);
    openEditStudentDialog(studentNumber);
});

        
        
deleteButton.addActionListener(e -> {
    fireEditingStopped();
    String studentNumber = (String) studentModel.getValueAt(currentRow, 0);

    int confirm = JOptionPane.showConfirmDialog(null,
            "Are you sure you want to delete student: " + studentNumber + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try (Connection conn = getConnection()) {

            // Step 1: Get scholarship_id assigned to student
            int scholarshipId = -1;
            try (PreparedStatement getScholarshipStmt = conn.prepareStatement(
                    "SELECT scholarship_id FROM students WHERE student_number = ?")) {
                getScholarshipStmt.setString(1, studentNumber);
                ResultSet rs = getScholarshipStmt.executeQuery();
                if (rs.next()) {
                    scholarshipId = rs.getInt("scholarship_id");
                }
            }

            // Step 2: Delete student
            try (PreparedStatement deleteStmt = conn.prepareStatement(
                    "DELETE FROM students WHERE student_number = ?")) {
                deleteStmt.setString(1, studentNumber);
                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Step 3: If student had a scholarship, increase student_needed
                    if (scholarshipId > 0) {
                        try (PreparedStatement updateScholarshipStmt = conn.prepareStatement(
                                "UPDATE scholarships SET student_needed = student_needed + 1 WHERE id = ?")) {
                            updateScholarshipStmt.setInt(1, scholarshipId);
                            updateScholarshipStmt.executeUpdate();
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Student " + studentNumber + " deleted.");
                    loadStudents(); // Refresh table
                } else {
                    JOptionPane.showMessageDialog(null, "Student not found or already deleted.");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting student.");
        }
    }
});


    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        currentRow = row;
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}private void openEditStudentDialog(String studentNumber) {
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(
             "SELECT s.*, sc.name AS name, sc.description AS description " +
             "FROM students s LEFT JOIN scholarships sc ON s.scholarship_id = sc.id WHERE s.student_number = ?")
    ) {
        stmt.setString(1, studentNumber);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            // Create main dialog
            JDialog editDialog = new JDialog(this, "Edit Student - " + studentNumber, true);
            editDialog.setSize(700, 600);
            editDialog.setLocationRelativeTo(this);
            editDialog.setLayout(new BorderLayout());
            editDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            // Create main panel with padding
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            // Create tabbed pane for better organization
            JTabbedPane tabbedPane = new JTabbedPane();
            
            // === PERSONAL INFO TAB ===
            JPanel personalPanel = createFormPanel();
            JTextField firstNameField = new JTextField(rs.getString("first_name"));
            JTextField lastNameField = new JTextField(rs.getString("last_name"));
            JTextField courseField = new JTextField(rs.getString("course"));
            JTextField yearField = new JTextField(String.valueOf(rs.getInt("year_level")));
            JTextField gpaField = new JTextField(String.valueOf(rs.getDouble("gpa")));
            JTextField emailField = new JTextField(rs.getString("email"));
            JTextField phoneField = new JTextField(rs.getString("phone_number"));
            JTextArea addressArea = new JTextArea(rs.getString("address"), 3, 20);
            addressArea.setLineWrap(true);
            addressArea.setWrapStyleWord(true);
            JScrollPane addressScroll = new JScrollPane(addressArea);
            
            addFormRow(personalPanel, "First Name:", firstNameField);
            addFormRow(personalPanel, "Last Name:", lastNameField);
            addFormRow(personalPanel, "Course:", courseField);
            addFormRow(personalPanel, "Year Level:", yearField);
            addFormRow(personalPanel, "GPA:", gpaField);
            addFormRow(personalPanel, "Email:", emailField);
            addFormRow(personalPanel, "Phone:", phoneField);
            addFormRow(personalPanel, "Address:", addressScroll);
            
            tabbedPane.addTab("Personal Info", personalPanel);

            // === SCHOLARSHIP TAB ===
            JPanel scholarshipPanel = createFormPanel();
           // Create JComboBox for scholarship name
JComboBox<String> scholarshipComboBox = new JComboBox<>();
JTextField scholarshipDescField = new JTextField();
scholarshipDescField.setEditable(false); // still read-only for description

// Populate dropdown from database
Map<String, String> scholarshipMap = new HashMap<>(); // name -> description

try (PreparedStatement scholarshipStmt = conn.prepareStatement("SELECT name, description FROM scholarships");
     ResultSet scholarshipRs = scholarshipStmt.executeQuery()) {

    while (scholarshipRs.next()) {
        String name = scholarshipRs.getString("name");
        String desc = scholarshipRs.getString("description");
        scholarshipComboBox.addItem(name);
        scholarshipMap.put(name, desc);
    }

} catch (SQLException ex) {
    ex.printStackTrace();
    JOptionPane.showMessageDialog(null, "Failed to load scholarships.", "Error", JOptionPane.ERROR_MESSAGE);
}

// Set selected value based on current student scholarship
String currentScholarshipName = rs.getString("name");
scholarshipComboBox.setSelectedItem(currentScholarshipName);
scholarshipDescField.setText(scholarshipMap.get(currentScholarshipName));

// Listener: change description field when dropdown changes
scholarshipComboBox.addActionListener(e -> {
    String selected = (String) scholarshipComboBox.getSelectedItem();
    scholarshipDescField.setText(scholarshipMap.getOrDefault(selected, ""));
});

// 🧩 Add to form layout (your style)
addFormRow(scholarshipPanel, "Scholarship Name:", scholarshipComboBox);


            
            // Add note about scholarship editing
            JLabel scholarshipNote = new JLabel("<html><i>Note: Scholarship information is read-only. Edit through scholarship management.</i></html>");
            scholarshipNote.setForeground(Color.GRAY);
            scholarshipPanel.add(Box.createVerticalStrut(10));
            scholarshipPanel.add(scholarshipNote);
            
            tabbedPane.addTab("Scholarship", scholarshipPanel);

            // === FAMILY INFO TAB ===
            JPanel familyPanel = createFormPanel();
            
            // Father section
            JLabel fatherLabel = new JLabel("Father Information");
            fatherLabel.setFont(fatherLabel.getFont().deriveFont(Font.BOLD, 14f));
            familyPanel.add(fatherLabel);
            familyPanel.add(Box.createVerticalStrut(5));
            
            JTextField fatherNameField = new JTextField(rs.getString("father_full_name"));
            JTextField fatherOccField = new JTextField(rs.getString("father_occupation"));
            JTextField fatherIncomeField = new JTextField(String.valueOf(rs.getDouble("father_income")));
            
            addFormRow(familyPanel, "Father's Name:", fatherNameField);
            addFormRow(familyPanel, "Father's Occupation:", fatherOccField);
            addFormRow(familyPanel, "Father's Income:", fatherIncomeField);
            
            familyPanel.add(Box.createVerticalStrut(15));
            
            // Mother section
            JLabel motherLabel = new JLabel("Mother Information");
            motherLabel.setFont(motherLabel.getFont().deriveFont(Font.BOLD, 14f));
            familyPanel.add(motherLabel);
            familyPanel.add(Box.createVerticalStrut(5));
            
            JTextField motherNameField = new JTextField(rs.getString("mother_full_name"));
            JTextField motherOccField = new JTextField(rs.getString("mother_occupation"));
            JTextField motherIncomeField = new JTextField(String.valueOf(rs.getDouble("mother_income")));
            
            addFormRow(familyPanel, "Mother's Name:", motherNameField);
            addFormRow(familyPanel, "Mother's Occupation:", motherOccField);
            addFormRow(familyPanel, "Mother's Income:", motherIncomeField);
            
            familyPanel.add(Box.createVerticalStrut(15));
            
            // Contact person
            JTextField contactField = new JTextField(rs.getString("contact_person"));
            addFormRow(familyPanel, "Emergency Contact:", contactField);
            
            tabbedPane.addTab("Family Info", familyPanel);

            // === DOCUMENTS TAB ===
            JPanel documentsPanel = createFormPanel();
            JTextField birthCertField = new JTextField(rs.getString("birth_certificate"));
            JTextField coeField = new JTextField(rs.getString("certificate_of_enrollment"));
            JTextField idPicField = new JTextField(rs.getString("two_x_two_id"));
            
            // File storage arrays
            File[] birthCertFile = {null};
            File[] coeFile = {null};
            File[] idPicFile = {null};
            
            // File size labels
            JLabel birthCertSizeLabel = new JLabel("");
            JLabel coeSizeLabel = new JLabel("");
            JLabel idPicSizeLabel = new JLabel("");
            
            addFormRowWithFileInfo(documentsPanel, "Birth Certificate:", birthCertField, birthCertSizeLabel);
            addFormRowWithFileInfo(documentsPanel, "Certificate of Enrollment:", coeField, coeSizeLabel);
            addFormRowWithFileInfo(documentsPanel, "2x2 ID Picture:", idPicField, idPicSizeLabel);
            
            // Add file browser buttons in a single row with proper file handling
            addFileBrowserButtonsRowWithValidation(documentsPanel, editDialog,
                new JTextField[]{birthCertField, coeField, idPicField},
                new String[]{"Browse Birth Cert", "Browse Certificate", "Browse ID Picture"},
                new File[][]{birthCertFile, coeFile, idPicFile},
                new JLabel[]{birthCertSizeLabel, coeSizeLabel, idPicSizeLabel});
            
            tabbedPane.addTab("Documents", documentsPanel);

            // Add tabbed pane to main panel
            mainPanel.add(tabbedPane, BorderLayout.CENTER);

            // === BUTTON PANEL ===
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            
            JButton cancelBtn = new JButton("Cancel");
            cancelBtn.setPreferredSize(new Dimension(100, 35));
            cancelBtn.addActionListener(e -> editDialog.dispose());
            
            JButton saveBtn = new JButton("Save Changes");
            saveBtn.setPreferredSize(new Dimension(120, 35));
            saveBtn.setBackground(new Color(70, 130, 180));
            saveBtn.setForeground(Color.WHITE);
           saveBtn.addActionListener(e -> {
                try {
                    saveStudentChanges(editDialog, studentNumber, conn,
                            firstNameField, lastNameField, courseField, yearField, gpaField, emailField, phoneField, addressArea,
                            fatherNameField, fatherOccField, fatherIncomeField, motherNameField, motherOccField, motherIncomeField,
                            contactField, birthCertField, coeField, idPicField, birthCertFile, coeFile, idPicFile, scholarshipComboBox);
                } catch (SQLException ex) {
                    Logger.getLogger(students.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            buttonPanel.add(cancelBtn);
            buttonPanel.add(saveBtn);
            
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            editDialog.add(mainPanel, BorderLayout.CENTER);

            // Set focus to first field
            SwingUtilities.invokeLater(() -> firstNameField.requestFocusInWindow());
            
            editDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Student not found.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to load student for editing.");
    }
}

// Helper method to create consistent form panels
private JPanel createFormPanel() {
    JPanel outerPanel = new JPanel(new BorderLayout());
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    
    // Add the form panel to the top of BorderLayout to prevent bottom spacing
    outerPanel.add(panel, BorderLayout.NORTH);
    return panel;
}

// Helper method to add form rows consistently
private void addFormRow(JPanel parent, String labelText, JComponent field) {
    JPanel rowPanel = new JPanel(new BorderLayout(10, 5));
    rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height + 10));
    
    JLabel label = new JLabel(labelText);
    label.setPreferredSize(new Dimension(150, label.getPreferredSize().height));
    
    rowPanel.add(label, BorderLayout.WEST);
    rowPanel.add(field, BorderLayout.CENTER);
    
    parent.add(rowPanel);
    parent.add(Box.createVerticalStrut(8));
}

// Add these imports at the top of your class file:
// import java.io.File;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.StandardCopyOption;

// Add constant for file size limit
private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

// Helper method to add form rows with file size info
private void addFormRowWithFileInfo(JPanel parent, String labelText, JComponent field, JLabel sizeLabel) {
    JPanel rowPanel = new JPanel(new BorderLayout(10, 5));
    rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height + 10));
    
    JLabel label = new JLabel(labelText);
    label.setPreferredSize(new Dimension(150, label.getPreferredSize().height));
    
    // Create a panel for field and size info
    JPanel fieldPanel = new JPanel(new BorderLayout());
    fieldPanel.add(field, BorderLayout.CENTER);
    
    if (sizeLabel != null) {
        sizeLabel.setForeground(Color.GRAY);
        sizeLabel.setFont(sizeLabel.getFont().deriveFont(Font.ITALIC, 10f));
        fieldPanel.add(sizeLabel, BorderLayout.SOUTH);
    }
    
    rowPanel.add(label, BorderLayout.WEST);
    rowPanel.add(fieldPanel, BorderLayout.CENTER);
    
    parent.add(rowPanel);
    parent.add(Box.createVerticalStrut(8));
}

// Helper method to add file browser buttons in a row with validation
private void addFileBrowserButtonsRowWithValidation(JPanel parent, JDialog dialog, 
                                                   JTextField[] fields, String[] buttonTexts, 
                                                   File[][] fileArrays, JLabel[] sizeLabels) {
    JPanel buttonRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    JFileChooser fileChooser = new JFileChooser();
    
    for (int i = 0; i < fields.length; i++) {
        JButton browseBtn = new JButton(buttonTexts[i]);
        browseBtn.setPreferredSize(new Dimension(140, 30));
        
        final int index = i;
        browseBtn.addActionListener(e -> {
            if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                
                // File size validation
                if (selectedFile.length() > MAX_FILE_SIZE) {
                    JOptionPane.showMessageDialog(dialog, 
                        "File size exceeds the 10MB limit.", 
                        "File Too Large", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Store the file and update UI
                fileArrays[index][0] = selectedFile;
                fields[index].setText(selectedFile.getName());
                sizeLabels[index].setText(String.format("%.2f MB", selectedFile.length() / (1024.0 * 1024.0)));
            }
        });
        
        buttonRowPanel.add(browseBtn);
    }
    
    parent.add(buttonRowPanel);
    parent.add(Box.createVerticalStrut(10));
}

// Helper method to add file browser functionality
private void addFileBrowserButton(JPanel parent, JTextField field, String buttonText) {
    JButton browseBtn = new JButton(buttonText);
    browseBtn.setPreferredSize(new Dimension(200, 30));
    browseBtn.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            field.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    });
    
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
    buttonPanel.add(browseBtn);
    parent.add(buttonPanel);
    parent.add(Box.createVerticalStrut(10));
}

// Extracted save method for better organization
private void saveStudentChanges(JDialog dialog, String studentNumber, Connection conn,
                               JTextField firstNameField, JTextField lastNameField, JTextField courseField,
                               JTextField yearField, JTextField gpaField, JTextField emailField,
                               JTextField phoneField, JTextArea addressArea, JTextField fatherNameField,
                               JTextField fatherOccField, JTextField fatherIncomeField, JTextField motherNameField,
                               JTextField motherOccField, JTextField motherIncomeField, JTextField contactField,
                               JTextField birthCertField, JTextField coeField, JTextField idPicField,
                               File[] birthCertFile, File[] coeFile, File[] idPicFile,       JComboBox<String> scholarshipComboBox) throws SQLException {
    
    // Validate required fields
    if (firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "First name and last name are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    try {
        // Validate numeric fields
        Integer.parseInt(yearField.getText().trim());
        Double.parseDouble(gpaField.getText().trim());
        Double.parseDouble(fatherIncomeField.getText().trim());
        Double.parseDouble(motherIncomeField.getText().trim());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for year level, GPA, and income fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    String selectedScholarshipName = (String) scholarshipComboBox.getSelectedItem();
int scholarshipId = 0;
try (PreparedStatement stmt = conn.prepareStatement("SELECT id FROM scholarships WHERE name = ?")) {
    stmt.setString(1, selectedScholarshipName);
    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
        scholarshipId = rs.getInt("id");
    } else {
        JOptionPane.showMessageDialog(dialog, "Scholarship not found.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
}

    // Handle file uploads if new files were selected
    String birthCertPath = birthCertField.getText().trim();
    String coePath = coeField.getText().trim();
    String idPath = idPicField.getText().trim();
    
    try {
        // Process new file uploads
        if (birthCertFile[0] != null) {
            new File("uploads/birth_certificates").mkdirs();
            birthCertPath = "uploads/birth_certificates/" + System.currentTimeMillis() + "_" + birthCertFile[0].getName();
            Files.copy(birthCertFile[0].toPath(), new File(birthCertPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        
        if (coeFile[0] != null) {
            new File("uploads/enrollment_certificates").mkdirs();
            coePath = "uploads/enrollment_certificates/" + System.currentTimeMillis() + "_" + coeFile[0].getName();
            Files.copy(coeFile[0].toPath(), new File(coePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        
        if (idPicFile[0] != null) {
            new File("uploads/id_pictures").mkdirs();
            idPath = "uploads/id_pictures/" + System.currentTimeMillis() + "_" + idPicFile[0].getName();
            Files.copy(idPicFile[0].toPath(), new File(idPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(dialog, "Error saving files: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    try (PreparedStatement updateStmt = conn.prepareStatement(
        "UPDATE students SET first_name=?, last_name=?, course=?, year_level=?, gpa=?, " +
        "email=?, phone_number=?, address=?, father_full_name=?, father_occupation=?, father_income=?, " +
        "mother_full_name=?, mother_occupation=?, mother_income=?, contact_person=?, birth_certificate=?, " +
        "certificate_of_enrollment=?, two_x_two_id=?, scholarship_id=? WHERE student_number=?"
    )) {
        updateStmt.setString(1, firstNameField.getText().trim());
        updateStmt.setString(2, lastNameField.getText().trim());
        updateStmt.setString(3, courseField.getText().trim());
        updateStmt.setInt(4, Integer.parseInt(yearField.getText().trim()));
        updateStmt.setDouble(5, Double.parseDouble(gpaField.getText().trim()));
        updateStmt.setString(6, emailField.getText().trim());
        updateStmt.setString(7, phoneField.getText().trim());
        updateStmt.setString(8, addressArea.getText().trim());

        updateStmt.setString(9, fatherNameField.getText().trim());
        updateStmt.setString(10, fatherOccField.getText().trim());
        updateStmt.setDouble(11, Double.parseDouble(fatherIncomeField.getText().trim()));

        updateStmt.setString(12, motherNameField.getText().trim());
        updateStmt.setString(13, motherOccField.getText().trim());
        updateStmt.setDouble(14, Double.parseDouble(motherIncomeField.getText().trim()));

        updateStmt.setString(15, contactField.getText().trim());
        updateStmt.setString(16, birthCertPath);
        updateStmt.setString(17, coePath);
        updateStmt.setString(18, idPath);
      updateStmt.setInt(19, scholarshipId);  // scholarship_id = ?
updateStmt.setString(20, studentNumber); 
        int updated = updateStmt.executeUpdate();
        if (updated > 0) {
            JOptionPane.showMessageDialog(dialog, "Student information updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            loadStudents(); // Refresh table
        } else {
            JOptionPane.showMessageDialog(dialog, "No changes were made.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(dialog, "Error while updating student information: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void showStudentDetails(String studentNumber) {
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(
             "SELECT * FROM students LEFT JOIN scholarships ON students.scholarship_id = scholarships.id WHERE student_number = ?"
         )) {

        stmt.setString(1, studentNumber);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            // Extract data
            String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
            String course = rs.getString("course");
            int year = rs.getInt("year_level");
            double gpa = rs.getDouble("gpa");
            String scholarshipName = rs.getString("name");
            String scholarshipDesc = rs.getString("description");
            String father = rs.getString("father_full_name") + " (" + rs.getString("father_occupation") + ")";
            String mother = rs.getString("mother_full_name") + " (" + rs.getString("mother_occupation") + ")";
            String income = "Father: ₱" + rs.getDouble("father_income") + " / Mother: ₱" + rs.getDouble("mother_income");
            String contact = rs.getString("contact_person");
            String email = rs.getString("email");
            String phone = rs.getString("phone_number");
            String address = rs.getString("address");
            String birthCert = rs.getString("birth_certificate");
            String coe = rs.getString("certificate_of_enrollment");
            String idPic = rs.getString("two_x_two_id");
 // Create StudentData object for PDF generation
           
            // Create dialog
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Student Details", true);
            dialog.setSize(800, 600);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setLocationRelativeTo(this);

            // === INFO PANEL (left) ===
JPanel infoPanel = new JPanel();
infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
// === Student Details Section ===
infoPanel.add(new JLabel("<html><h3>Student Details</h3></html>"));
infoPanel.add(new JLabel("<html><b>Student Number:</b> " + studentNumber + "</html>"));
infoPanel.add(new JLabel("<html><b>Full Name:</b> " + fullName + "</html>"));
infoPanel.add(new JLabel("<html><b>GPA:</b> " + gpa + "</html>"));
infoPanel.add(new JLabel("<html><b>Course:</b> " + course + "</html>"));
infoPanel.add(new JLabel("<html><b>Year Level:</b> " + year + "</html>"));

// === Scholarship Section ===
infoPanel.add(new JLabel("<html><h3>Scholarship</h3></html>"));
infoPanel.add(new JLabel("<html><b>Scholarship:</b> " + (scholarshipName != null ? scholarshipName : "None") + "</html>"));
String desc = (scholarshipDesc != null ? scholarshipDesc : "None");

JLabel scholarshipDescLabel = new JLabel("<html><body style='width:500px'><b>Description:</b><br>" + desc + "</body></html>");
infoPanel.add(scholarshipDescLabel);
// === Parent Info Section ===
infoPanel.add(new JLabel("<html><h3>Parental Information</h3></html>"));
infoPanel.add(new JLabel("<html><b>Father:</b> " + father + "</html>"));
infoPanel.add(new JLabel("<html><b>Mother:</b> " + mother + "</html>"));
infoPanel.add(new JLabel("<html><b>Combined Income:</b> " + income + "</html>"));

// === Contact Info Section ===
infoPanel.add(new JLabel("<html><h3>Contact Details</h3></html>"));
infoPanel.add(new JLabel("<html><b>Contact Person:</b> " + contact + "</html>"));
infoPanel.add(new JLabel("<html><b>Email:</b> " + email + "</html>"));
infoPanel.add(new JLabel("<html><b>Phone:</b> " + phone + "</html>"));
infoPanel.add(new JLabel("<html><b>Address:</b> " + address + "</html>"));


            // === IMAGE PANEL (right) ===
            JPanel imagePanel = new JPanel();
            imagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            if (idPic != null && !idPic.isEmpty()) {
                String imgPath = "./" + idPic;
                File file = new File(imgPath);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(imgPath);
                    Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    JLabel picLabel = new JLabel(new ImageIcon(scaled));
                    picLabel.setBorder(BorderFactory.createTitledBorder("2x2 ID Picture"));
                    imagePanel.add(picLabel);
                } else {
                    imagePanel.add(new JLabel("Picture not found."));
                }
            }

            // === BUTTON PANEL ===
           // Panel background
JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
buttonPanel.setBackground(new Color(245, 245, 245)); // light gray background

// Common button style
Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
Color btnBgColor = new Color(70, 130, 180); // Steel Blue
Color btnFgColor = Color.WHITE;

// Birth Certificate Button
JButton birthBtn = new JButton("Open Birth Certificate");
birthBtn.setFont(buttonFont);
birthBtn.setBackground(btnBgColor);
birthBtn.setForeground(btnFgColor);
birthBtn.setFocusPainted(false);

// COE Button
JButton coeBtn = new JButton("Open COE");
coeBtn.setFont(buttonFont);
coeBtn.setBackground(btnBgColor);
coeBtn.setForeground(btnFgColor);
coeBtn.setFocusPainted(false);

// 2x2 Picture Button
JButton picBtn = new JButton("Open 2x2 Picture");
picBtn.setFont(buttonFont);
picBtn.setBackground(btnBgColor);
picBtn.setForeground(btnFgColor);
picBtn.setFocusPainted(false);

// Generate PDF Button
JButton printBtn = new JButton("Generate PDF");
printBtn.setFont(buttonFont);
printBtn.setBackground(new Color(34, 139, 34)); // Forest Green
printBtn.setForeground(Color.WHITE);
printBtn.setFocusPainted(false);

// Add buttons to panel
buttonPanel.add(birthBtn);
buttonPanel.add(coeBtn);
buttonPanel.add(picBtn);
buttonPanel.add(printBtn);


            birthBtn.addActionListener(e -> openFile("./" + birthCert));
            coeBtn.addActionListener(e -> openFile("./" + coe));
            picBtn.addActionListener(e -> openFile("./" + idPic));
printBtn.addActionListener(e -> {
    try {
        String pdfFile = "Student_" + studentNumber + ".pdf";

        PdfGenerator.generateStudentPdf(pdfFile, studentNumber, fullName, course, year, gpa,
                scholarshipName, scholarshipDesc, father, mother, income, contact, email, phone, address);

        JOptionPane.showMessageDialog(this, "PDF Created: " + pdfFile);

        // Open PDF only (let the user print manually)
        openPdfInViewer(pdfFile);

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
});






            buttonPanel.add(birthBtn);
            buttonPanel.add(coeBtn);
             buttonPanel.add(picBtn);
buttonPanel.add(printBtn);
            // === ADD TO MAIN PANEL ===
            dialog.add(infoPanel, BorderLayout.CENTER);
            dialog.add(imagePanel, BorderLayout.EAST);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "Student not found.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Something went wrong.");
    }
}

public static void openPdfInViewer(String pdfPath) {
    try {
        File pdfFile = new File(pdfPath);
        if (pdfFile.exists()) {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
                System.out.println("PDF opened in viewer.");
            } else {
                System.out.println("AWT Desktop is not supported.");
            }
        } else {
            System.out.println("PDF file not found: " + pdfPath);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Failed to open PDF viewer: " + e.getMessage());
    }
}

// Open file method
private void openFile(String path) {
    try {
        File file = new File(path);
        if (file.exists()) {
            Desktop.getDesktop().open(file);
        } else {
            JOptionPane.showMessageDialog(null, "File not found: " + path);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Cannot open file.");
    }
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        dashboard = new javax.swing.JButton();
        logout_button = new javax.swing.JButton();
        scholarship = new javax.swing.JButton();
        students = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        yourMainPanel = new javax.swing.JPanel();
        prev = new javax.swing.JButton();
        next = new javax.swing.JButton();
        assign_scholarship = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        students_table = new javax.swing.JTable();
        search_students = new javax.swing.JTextField();
        search_button = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        comboBox_scholarships = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1532, 850));

        jPanel1.setBackground(new java.awt.Color(213, 213, 181));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(213, 213, 181));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        dashboard.setBackground(new java.awt.Color(255, 255, 237));
        dashboard.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scholarship_system/dashboard.png"))); // NOI18N
        dashboard.setText("Dashboard");
        dashboard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        dashboard.setOpaque(true);
        dashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardActionPerformed(evt);
            }
        });

        logout_button.setBackground(new java.awt.Color(255, 255, 237));
        logout_button.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        logout_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scholarship_system/logout.png"))); // NOI18N
        logout_button.setText("Logout");
        logout_button.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        logout_button.setOpaque(true);
        logout_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout_buttonActionPerformed(evt);
            }
        });

        scholarship.setBackground(new java.awt.Color(255, 255, 237));
        scholarship.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        scholarship.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scholarship_system/scholarship (1).png"))); // NOI18N
        scholarship.setText("Scholarship");
        scholarship.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scholarship.setOpaque(true);
        scholarship.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scholarshipActionPerformed(evt);
            }
        });

        students.setBackground(new java.awt.Color(255, 255, 237));
        students.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        students.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scholarship_system/graduating-student (2).png"))); // NOI18N
        students.setText("Students");
        students.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        students.setOpaque(true);

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel5.setText("Welcome,");

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 238), 3));

        jLabel9.setBackground(new java.awt.Color(255, 255, 238));
        jLabel9.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 238));
        jLabel9.setText("Admin");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(logout_button, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scholarship, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(students, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 55, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(scholarship, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(students, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(logout_button, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(381, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 850));

        jPanel2.setBackground(new java.awt.Color(255, 255, 237));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(149, 149, 141)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        jLabel1.setText("Students");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, 480, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(81, 81, 76));
        jLabel2.setText("Assign student scholarship , search, and manage student records efficiently.");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 90, 670, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scholarship_system/graduating-student (3).png"))); // NOI18N
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 30, 100, 80));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1532, 128));

        prev.setText("Prev");
        prev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevActionPerformed(evt);
            }
        });

        next.setText("Next");
        next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout yourMainPanelLayout = new javax.swing.GroupLayout(yourMainPanel);
        yourMainPanel.setLayout(yourMainPanelLayout);
        yourMainPanelLayout.setHorizontalGroup(
            yourMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(yourMainPanelLayout.createSequentialGroup()
                .addGap(268, 268, 268)
                .addComponent(prev, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(next, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(203, Short.MAX_VALUE))
        );
        yourMainPanelLayout.setVerticalGroup(
            yourMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(yourMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(yourMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(prev, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(next, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel1.add(yourMainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 700, 660, 60));

        assign_scholarship.setBackground(new java.awt.Color(255, 254, 0));
        assign_scholarship.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        assign_scholarship.setText("Assign Scholarship");
        assign_scholarship.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assign_scholarshipActionPerformed(evt);
            }
        });
        jPanel1.add(assign_scholarship, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 150, 190, 40));

        students_table.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        students_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},  // empty data
            new String [] {
                "Student number", "Full name", "Course", "Year", "Scholarship", "Actions"
            }
        ));
        students_table.setOpaque(false);
        students_table.setRowHeight(40);
        jScrollPane1.setViewportView(students_table);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 220, 1070, 460));

        search_students.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_studentsActionPerformed(evt);
            }
        });
        jPanel1.add(search_students, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 150, 280, 40));

        search_button.setBackground(new java.awt.Color(213, 213, 181));
        search_button.setText("Search");
        search_button.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        search_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_buttonActionPerformed(evt);
            }
        });
        jPanel1.add(search_button, new org.netbeans.lib.awtextra.AbsoluteConstraints(1290, 150, 70, 40));

        clear.setBackground(new java.awt.Color(213, 213, 181));
        clear.setText("clear");
        clear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });
        jPanel1.add(clear, new org.netbeans.lib.awtextra.AbsoluteConstraints(1370, 150, 70, 40));

        comboBox_scholarships.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        comboBox_scholarships.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBox_scholarshipsActionPerformed(evt);
            }
        });
        jPanel1.add(comboBox_scholarships, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 150, 180, 40));

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel4.setText("Welcome,");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, 480, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardActionPerformed
 admin_page dashboard = new admin_page();  // Create an instance of Login_page
       dashboard.setVisible(true);  // Make it visible
        this.setVisible(false);  // Hide the Landing page        // TODO add your handling code here:
    }//GEN-LAST:event_dashboardActionPerformed

    private void scholarshipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scholarshipActionPerformed
  scholarship scholarship = new scholarship();  // Create an instance of Login_page
        scholarship.setVisible(true);  // Make it visible
        this.setVisible(false);  // Hide the Landing page        // TODO add your handling code here:
    }//GEN-LAST:event_scholarshipActionPerformed

    private void assign_scholarshipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assign_scholarshipActionPerformed
       insertStudentWithScholarship();
    }//GEN-LAST:event_assign_scholarshipActionPerformed

    private void search_studentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_studentsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_search_studentsActionPerformed

    private void search_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_buttonActionPerformed
   String searchInput = search_students.getText().trim();

if (searchInput.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Please enter Student Number, First Name, Last Name, or Scholarship Name to search.");
    return;
}

try (Connection conn = db_connection.getConnection();
     PreparedStatement stmt = conn.prepareStatement(
         "SELECT s.student_number, s.first_name, s.last_name, s.course, s.year_level, sch.name AS scholarship_name " +
         "FROM students s " +
         "LEFT JOIN scholarships sch ON s.scholarship_id = sch.id " +
         "WHERE s.student_number LIKE ? " +
         "OR s.first_name LIKE ? " +
         "OR s.last_name LIKE ? " +
         "OR sch.name LIKE ?")) {

    String query = "%" + searchInput + "%";
    stmt.setString(1, query);
    stmt.setString(2, query);
    stmt.setString(3, query);
    stmt.setString(4, query); // for scholarship name

    ResultSet rs = stmt.executeQuery();

    // Clear existing rows in table
    studentModel.setRowCount(0);

    while (rs.next()) {
        String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
        String scholarshipName = rs.getString("scholarship_name");

        Object[] row = new Object[]{
            rs.getString("student_number"),
            fullName,
            rs.getString("course"),
            rs.getInt("year_level"),
            (scholarshipName != null ? scholarshipName : "None"),
            "View | Edit"
        };
        studentModel.addRow(row);
    }

    if (studentModel.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "No matching student found.");
    }

} catch (Exception ex) {
    ex.printStackTrace();
    JOptionPane.showMessageDialog(this, "Error while searching: " + ex.getMessage());
}


    }//GEN-LAST:event_search_buttonActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        // TODO add your handling code here:
         loadStudents();
         search_students.setText("");
    }//GEN-LAST:event_clearActionPerformed

    private void comboBox_scholarshipsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox_scholarshipsActionPerformed
        // TODO add your handling code here:
         String selectedScholarship = (String) comboBox_scholarships.getSelectedItem();

    String query = "SELECT s.student_number, s.first_name, s.last_name, s.course, s.year_level, sch.name AS scholarship_name " +
                   "FROM students s " +
                   "LEFT JOIN scholarships sch ON s.scholarship_id = sch.id ";

    if (!"All".equalsIgnoreCase(selectedScholarship)) {
        query += "WHERE sch.name = ?";
    }

    try (Connection conn = db_connection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        if (!"All".equalsIgnoreCase(selectedScholarship)) {
            stmt.setString(1, selectedScholarship);
        }

        ResultSet rs = stmt.executeQuery();
        studentModel.setRowCount(0); // Clear table

        while (rs.next()) {
            String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
            String scholarshipName = rs.getString("scholarship_name");

            Object[] row = new Object[]{
                rs.getString("student_number"),
                fullName,
                rs.getString("course"),
                rs.getInt("year_level"),
                (scholarshipName != null ? scholarshipName : "None"),
                "View | Edit"
            };
            studentModel.addRow(row);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error filtering students.");
    }
    }//GEN-LAST:event_comboBox_scholarshipsActionPerformed

    private void prevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevActionPerformed
   if (currentPage > 1) {
        currentPage--;
        updateTablePage();
    }        // TODO add your handling code here:
    }//GEN-LAST:event_prevActionPerformed

    private void nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextActionPerformed
int maxPage = (int) Math.ceil((double) allStudents.size() / rowsPerPage);
    if (currentPage < maxPage) {
        currentPage++;
        updateTablePage();
    }        // TODO add your handling code here:
    }//GEN-LAST:event_nextActionPerformed

    private void logout_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout_buttonActionPerformed
      int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to logout?",
        "Confirm Logout",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );

    if (confirm == JOptionPane.YES_OPTION) {
        // Close the current window
        this.dispose();

        // Open the login page
        java.awt.EventQueue.invokeLater(() -> {
            new login_page().setVisible(true);
        });
    }
    }//GEN-LAST:event_logout_buttonActionPerformed
private void insertStudentWithScholarship() {
    // Create a JDialog for the form instead of using JOptionPane for more control
    JDialog dialog = new JDialog();
    dialog.setTitle("Insert Student with Scholarship");
    dialog.setModal(true);
    dialog.setSize(800, 750);
    dialog.setLocationRelativeTo(null);
    
    // Main panel with border layout
    JPanel mainPanel = new JPanel(new BorderLayout());
    
    // Create panel for the form with two columns using GridBagLayout
    JPanel formPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    
    // Create text fields with preferred size
    Dimension textFieldDimension = new Dimension(200, 35);
    Dimension shortFieldDimension = new Dimension(200, 35);
    Dimension courseDimension = new Dimension(200, 40);
    // Student info fields
    JTextField studentNumberField = new JTextField(15);
    JTextField firstNameField = new JTextField(15);
    JTextField lastNameField = new JTextField(15);
    JTextField gpaField = new JTextField(5);
    JTextField yearLevelField = new JTextField(5);
JComboBox<String> courseField = new JComboBox<>(new String[] {
    "Bachelor of Science in Information Technology",
    "Bachelor of Science in Computer Science ",
    "Bachelor of Science in Information Systems ",
    "Bachelor of Science in Accountancy (BSA)",
   
    "Bachelor of Science in Tourism Management ",
    "Bachelor of Elementary Education ",
    "Bachelor of Secondary Education ",
    "Bachelor of Science in Industrial Technology "
});
courseField.setPreferredSize(courseDimension);

    // Parent info fields
    JTextField fatherNameField = new JTextField(15);
    JTextField fatherOccField = new JTextField(15);
    JTextField fatherIncomeField = new JTextField(10);
    JTextField motherNameField = new JTextField(15);
    JTextField motherOccField = new JTextField(15);
    JTextField motherIncomeField = new JTextField(10);

    // Contact info fields
    JTextField contactPersonField = new JTextField(15);
    JTextField emailField = new JTextField(15);
    JTextField phoneField = new JTextField(15);
    JTextArea addressArea = new JTextArea(3, 20);
    JScrollPane addressScrollPane = new JScrollPane(addressArea);
    
    // Set preferred sizes
    studentNumberField.setPreferredSize(textFieldDimension);
    firstNameField.setPreferredSize(textFieldDimension);
    lastNameField.setPreferredSize(textFieldDimension);
    gpaField.setPreferredSize(shortFieldDimension);
  
    yearLevelField.setPreferredSize(shortFieldDimension);
    
    fatherNameField.setPreferredSize(textFieldDimension);
    fatherOccField.setPreferredSize(textFieldDimension);
    fatherIncomeField.setPreferredSize(shortFieldDimension);
    motherNameField.setPreferredSize(textFieldDimension);
    motherOccField.setPreferredSize(textFieldDimension);
    motherIncomeField.setPreferredSize(shortFieldDimension);
    
    contactPersonField.setPreferredSize(textFieldDimension);
    emailField.setPreferredSize(textFieldDimension);
    phoneField.setPreferredSize(textFieldDimension);
    addressScrollPane.setPreferredSize(new Dimension(200, 60));
    
    // Create scholarship combo box
    DefaultComboBoxModel<ScholarshipItem> scholarshipModel = new DefaultComboBoxModel<>();
    JComboBox<ScholarshipItem> scholarshipBox = new JComboBox<>(scholarshipModel);
    scholarshipBox.setPreferredSize(textFieldDimension);

    // Load scholarships
    try (Connection conn = db_connection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(
             "SELECT id, name FROM scholarships WHERE student_needed > 0");
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            scholarshipModel.addElement(new ScholarshipItem(id, name));
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Failed to load scholarships.");
        dialog.dispose();
        return;
    }

    // If no scholarships available, show message
    if (scholarshipModel.getSize() == 0) {
        JOptionPane.showMessageDialog(null, "No scholarships available.");
        dialog.dispose();
        return;
    }

    // Document fields with file size validation
    final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB in bytes
    
    // File references
    final File[] birthCertFile = {null};
    final File[] coeFile = {null};
    final File[] idPicFile = {null};
    
    // Create file selection components
    JTextField birthCertField = new JTextField(15);
    birthCertField.setEditable(false);
    JTextField coeField = new JTextField(15);
    coeField.setEditable(false);
    JTextField idPicField = new JTextField(15);
    idPicField.setEditable(false);
    
    JButton birthCertButton = new JButton("Browse...");
    JButton coeButton = new JButton("Browse...");
    JButton idPicButton = new JButton("Browse...");
    
    // Labels to display file size information
    JLabel birthCertSizeLabel = new JLabel("");
    JLabel coeSizeLabel = new JLabel("");
    JLabel idPicSizeLabel = new JLabel("");
    
    // File chooser setup
    JFileChooser fileChooser = new JFileChooser();
    
    // File selection button actions with size validation
    birthCertButton.addActionListener(e -> {
        if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.length() > MAX_FILE_SIZE) {
                JOptionPane.showMessageDialog(dialog, 
                    "File size exceeds the 10MB limit.", 
                    "File Too Large", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            birthCertFile[0] = selectedFile;
            birthCertField.setText(selectedFile.getName());
            birthCertSizeLabel.setText(String.format("%.2f MB", selectedFile.length() / (1024.0 * 1024.0)));
        }
    });
    
    coeButton.addActionListener(e -> {
        if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.length() > MAX_FILE_SIZE) {
                JOptionPane.showMessageDialog(dialog, 
                    "File size exceeds the 10MB limit.", 
                    "File Too Large", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            coeFile[0] = selectedFile;
            coeField.setText(selectedFile.getName());
            coeSizeLabel.setText(String.format("%.2f MB", selectedFile.length() / (1024.0 * 1024.0)));
        }
    });
    
    idPicButton.addActionListener(e -> {
        if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.length() > MAX_FILE_SIZE) {
                JOptionPane.showMessageDialog(dialog, 
                    "File size exceeds the 10MB limit.", 
                    "File Too Large", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            idPicFile[0] = selectedFile;
            idPicField.setText(selectedFile.getName());
            idPicSizeLabel.setText(String.format("%.2f MB", selectedFile.length() / (1024.0 * 1024.0)));
        }
    });
    
    // Create file panels with size indicators
    JPanel birthCertPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    birthCertPanel.add(birthCertField);
    birthCertPanel.add(birthCertButton);
    birthCertPanel.add(birthCertSizeLabel);
    
    JPanel coePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    coePanel.add(coeField);
    coePanel.add(coeButton);
    coePanel.add(coeSizeLabel);
    
    JPanel idPicPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    idPicPanel.add(idPicField);
    idPicPanel.add(idPicButton);
    idPicPanel.add(idPicSizeLabel);
    
    // Form title
    JLabel titleLabel = new JLabel("Student Scholarship Application Form");
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    titleLabel.setHorizontalAlignment(JLabel.CENTER);
    
    // Section titles
    JLabel studentInfoLabel = new JLabel("Student Information");
    studentInfoLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    JLabel parentInfoLabel = new JLabel("Parent Information");
    parentInfoLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    JLabel contactInfoLabel = new JLabel("Contact Information");
    contactInfoLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    JLabel documentLabel = new JLabel("Required Documents");
    documentLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    
    // Add components to grid - LEFT COLUMN
    
    // Add title
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 4;
    formPanel.add(titleLabel, gbc);
    
    // Student info section - LEFT COLUMN
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    formPanel.add(studentInfoLabel, gbc);
    
    // Student fields - LEFT COLUMN
    gbc.gridwidth = 1;
    gbc.gridy = 2;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Student ID:"), gbc);
    gbc.gridx = 1;
    formPanel.add(studentNumberField, gbc);
    
    gbc.gridy = 3;
    gbc.gridx = 0;
    formPanel.add(new JLabel("First Name:"), gbc);
    gbc.gridx = 1;
    formPanel.add(firstNameField, gbc);
    
    gbc.gridy = 4;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Last Name:"), gbc);
    gbc.gridx = 1;
    formPanel.add(lastNameField, gbc);
    
    gbc.gridy = 5;
    gbc.gridx = 0;
    formPanel.add(new JLabel("GPA:"), gbc);
    gbc.gridx = 1;
    formPanel.add(gpaField, gbc);
    
    gbc.gridy = 6;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Course:"), gbc);
    gbc.gridx = 1;
    formPanel.add(courseField, gbc);
    
    gbc.gridy = 7;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Year Level:"), gbc);
    gbc.gridx = 1;
    formPanel.add(yearLevelField, gbc);
    
    // Scholarship selection - LEFT COLUMN
    gbc.gridy = 8;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Scholarship:"), gbc);
    gbc.gridx = 1;
    formPanel.add(scholarshipBox, gbc);
    
    // Contact info section - LEFT COLUMN
    gbc.gridy = 9;
    gbc.gridwidth = 2;
    gbc.gridx = 0;
    formPanel.add(contactInfoLabel, gbc);
    
    gbc.gridwidth = 1;
    gbc.gridy = 10;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Contact Person:"), gbc);
    gbc.gridx = 1;
    formPanel.add(contactPersonField, gbc);
    
    gbc.gridy = 11;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Email:"), gbc);
    gbc.gridx = 1;
    formPanel.add(emailField, gbc);
    
    gbc.gridy = 12;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Phone Number:"), gbc);
    gbc.gridx = 1;
    formPanel.add(phoneField, gbc);
    
    gbc.gridy = 13;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Address:"), gbc);
    gbc.gridx = 1;
    formPanel.add(addressScrollPane, gbc);
    
    // RIGHT COLUMN
    
    // Parent info section - RIGHT COLUMN
    gbc.gridy = 1;
    gbc.gridx = 2;
    gbc.gridwidth = 2;
    formPanel.add(parentInfoLabel, gbc);
    
    // Father info fields - RIGHT COLUMN
    gbc.gridwidth = 1;
    gbc.gridy = 2;
    gbc.gridx = 2;
    formPanel.add(new JLabel("Father's Full Name:"), gbc);
    gbc.gridx = 3;
    formPanel.add(fatherNameField, gbc);
    
    gbc.gridy = 3;
    gbc.gridx = 2;
    formPanel.add(new JLabel("Father's Occupation:"), gbc);
    gbc.gridx = 3;
    formPanel.add(fatherOccField, gbc);
    
    gbc.gridy = 4;
    gbc.gridx = 2;
    formPanel.add(new JLabel("Father's Income:"), gbc);
    gbc.gridx = 3;
    formPanel.add(fatherIncomeField, gbc);
    
    // Mother info fields - RIGHT COLUMN
    gbc.gridy = 5;
    gbc.gridx = 2;
    formPanel.add(new JLabel("Mother's Full Name:"), gbc);
    gbc.gridx = 3;
    formPanel.add(motherNameField, gbc);
    
    gbc.gridy = 6;
    gbc.gridx = 2;
    formPanel.add(new JLabel("Mother's Occupation:"), gbc);
    gbc.gridx = 3;
    formPanel.add(motherOccField, gbc);
    
    gbc.gridy = 7;
    gbc.gridx = 2;
    formPanel.add(new JLabel("Mother's Income:"), gbc);
    gbc.gridx = 3;
    formPanel.add(motherIncomeField, gbc);
    
    // Documents section - RIGHT COLUMN
    gbc.gridy = 8;
    gbc.gridx = 2;
    gbc.gridwidth = 2;
    formPanel.add(documentLabel, gbc);
    
    gbc.gridwidth = 1;
    gbc.gridy = 9;
    gbc.gridx = 2;
    formPanel.add(new JLabel("Birth Certificate:"), gbc);
    gbc.gridx = 3;
    formPanel.add(birthCertPanel, gbc);
    
    gbc.gridy = 10;
    gbc.gridx = 2;
    formPanel.add(new JLabel("Certificate of Enrollment:"), gbc);
    gbc.gridx = 3;
    formPanel.add(coePanel, gbc);
    
    gbc.gridy = 11;
    gbc.gridx = 2;
    formPanel.add(new JLabel("2x2 ID Picture:"), gbc);
    gbc.gridx = 3;
    formPanel.add(idPicPanel, gbc);
    
    // Add the form panel to the main panel with padding
    mainPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
    
    // Add buttons panel
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton submitButton = new JButton("Submit");
    JButton cancelButton = new JButton("Cancel");
    
    buttonsPanel.add(submitButton);
    buttonsPanel.add(cancelButton);
    mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
    
   submitButton.addActionListener(e -> {
    // Validate form fields
    if (studentNumberField.getText().trim().isEmpty() || 
        firstNameField.getText().trim().isEmpty() || 
        lastNameField.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "Student information is required.");
        return;
    }

    // Validate document files
    if (birthCertFile[0] == null || coeFile[0] == null || idPicFile[0] == null) {
        JOptionPane.showMessageDialog(dialog, "All document files are required.");
        return;
    }

    try {
        // Check for duplicates
        try (Connection conn = db_connection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM students WHERE student_number = ? AND first_name = ? AND last_name = ?")) {
            
            checkStmt.setString(1, studentNumberField.getText().trim());
            checkStmt.setString(2, firstNameField.getText().trim());
            checkStmt.setString(3, lastNameField.getText().trim());

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(dialog, "A student with the same Student Number, First Name, and Last Name already exists.");
                return;
            }
        }

        // Make sure subfolders exist
        new File("uploads/birth_certificates").mkdirs();
        new File("uploads/enrollment_certificates").mkdirs();
        new File("uploads/id_pictures").mkdirs();

        // Save files to the correct subfolder with timestamped names
        String selectedCourse = (String) courseField.getSelectedItem();
        String birthCertPath = "uploads/birth_certificates/" + System.currentTimeMillis() + "_" + birthCertFile[0].getName();
        String coePath = "uploads/enrollment_certificates/" + System.currentTimeMillis() + "_" + coeFile[0].getName();
        String idPath = "uploads/id_pictures/" + System.currentTimeMillis() + "_" + idPicFile[0].getName();

        Files.copy(birthCertFile[0].toPath(), new File(birthCertPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(coeFile[0].toPath(), new File(coePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(idPicFile[0].toPath(), new File(idPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

        // Prepare SQL query
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO students ");
        sql.append("(student_number, first_name, last_name, gpa, course, year_level, ");
        sql.append("birth_certificate, certificate_of_enrollment, two_x_two_id, ");
        sql.append("father_full_name, father_occupation, father_income, ");
        sql.append("mother_full_name, mother_occupation, mother_income, ");
        sql.append("scholarship_id, contact_person, email, phone_number, address) ");
        sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            stmt.setString(1, studentNumberField.getText().trim());
            stmt.setString(2, firstNameField.getText().trim());
            stmt.setString(3, lastNameField.getText().trim());
            stmt.setBigDecimal(4, new BigDecimal(gpaField.getText().trim()));
            stmt.setString(5, selectedCourse);   // <-- here
            stmt.setInt(6, Integer.parseInt(yearLevelField.getText().trim()));

            // Store file paths instead of binary data
            stmt.setString(7, birthCertPath);
            stmt.setString(8, coePath);
            stmt.setString(9, idPath);

            stmt.setString(10, fatherNameField.getText().trim());
            stmt.setString(11, fatherOccField.getText().trim());
            stmt.setBigDecimal(12, new BigDecimal(fatherIncomeField.getText().trim()));

            stmt.setString(13, motherNameField.getText().trim());
            stmt.setString(14, motherOccField.getText().trim());
            stmt.setBigDecimal(15, new BigDecimal(motherIncomeField.getText().trim()));

            stmt.setInt(16, ((ScholarshipItem)scholarshipBox.getSelectedItem()).getId());
            stmt.setString(17, contactPersonField.getText().trim());
            stmt.setString(18, emailField.getText().trim());
            stmt.setString(19, phoneField.getText().trim());
            stmt.setString(20, addressArea.getText().trim());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE scholarships SET student_needed = student_needed - 1 WHERE id = ?")) {
                    updateStmt.setInt(1, ((ScholarshipItem)scholarshipBox.getSelectedItem()).getId());
                    updateStmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(dialog, "Student added and scholarship assigned successfully.");
                loadStudents();
                dialog.dispose();
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(dialog, "Error inserting student: " + ex.getMessage());
    }
});

    
    cancelButton.addActionListener(e -> dialog.dispose());
    
    dialog.getContentPane().add(mainPanel);
    dialog.setVisible(true);
}

// Helper class to store scholarship data in the combo box
class ScholarshipItem {
    private int id;
    private String name;
    
    public ScholarshipItem(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}

    /**}
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(students.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(students.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(students.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(students.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new students().setVisible(true);
            }
        });
    }
private int currentPage = 1;
private final int rowsPerPage = 11;
private List<Object[]> allStudents; // store all student data here


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton assign_scholarship;
    private javax.swing.JButton clear;
    private javax.swing.JComboBox<String> comboBox_scholarships;
    private javax.swing.JButton dashboard;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton logout_button;
    private javax.swing.JButton next;
    private javax.swing.JButton prev;
    private javax.swing.JButton scholarship;
    private javax.swing.JButton search_button;
    private javax.swing.JTextField search_students;
    private javax.swing.JButton students;
    private javax.swing.JTable students_table;
    private javax.swing.JPanel yourMainPanel;
    // End of variables declaration//GEN-END:variables
}
