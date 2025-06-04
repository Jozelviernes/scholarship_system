/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package scholarship_system;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/**
/**
 *
 * @author Admin
 */
public class scholarship extends javax.swing.JFrame {

    /**
     * Creates new form admin_page
     */
    public scholarship() {
        
          if (session.getUsername() == null || session.getUsername().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please log in first.", "Access Denied", JOptionPane.WARNING_MESSAGE);
        
        // Redirect to login page
        login_page login = new login_page();
        login.setVisible(true);
        
        dispose(); // Close the dashboard
        return;
           }
          
        initComponents();
    DefaultTableModel model = new DefaultTableModel(new String[]{ "Id","Name",  "Type", "Start Date", "Deadline", "Actions"}, 0);
scholarshipTable.setModel(model);
scholarshipTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
scholarshipTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));
loadScholarships(model);
scholarshipTable.getColumnModel().getColumn(0).setPreferredWidth(5);
scholarshipTable.getColumnModel().getColumn(5).setPreferredWidth(200);
scholarshipTable.getColumnModel().getColumn(1).setPreferredWidth(100);
    }
    
private void loadScholarships(DefaultTableModel model) {
    try (Connection conn = db_connection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM scholarships")) {

        // Clear the table before reloading to avoid appending duplicate data
        model.setRowCount(0);

        while (rs.next()) {
            // Add only the columns you want, excluding description
            model.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("type"),
                rs.getDate("start_date"),
                rs.getDate("deadline"),
                "Actions" // Keep this for the action buttons
            });
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


class ButtonRenderer extends JPanel implements TableCellRenderer {
     protected JButton viewBtn = new JButton("View");
    private final JButton editBtn = new JButton("Edit");
    private final JButton deleteBtn = new JButton("Delete");

    public ButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5)); // 5px horizontal and vertical gap

        // Customize viewBtn
        viewBtn.setBackground(new Color(70, 130, 180)); // Steel Blue color
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFocusPainted(false);
     // top, left, bottom, right padding

        // Customize editBtn
        editBtn.setBackground(new Color(46, 204, 113)); // Green color
        editBtn.setForeground(Color.WHITE);
        editBtn.setFocusPainted(false);
       

        // Customize deleteBtn
        deleteBtn.setBackground(new Color(231, 76, 60)); // Red color
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
   
        add(viewBtn);
        add(editBtn);
        add(deleteBtn);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}
class ButtonEditor extends DefaultCellEditor {
    protected JPanel panel = new JPanel();
    protected JButton viewBtn = new JButton("View");
    protected JButton editBtn = new JButton("Edit");
    protected JButton deleteBtn = new JButton("Delete");

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(viewBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);

        
 // Example for viewBtn
viewBtn.setBackground(new Color(70, 130, 180)); // Your chosen color
viewBtn.setForeground(Color.WHITE);             // Text color
viewBtn.setOpaque(true);
viewBtn.setContentAreaFilled(true);
viewBtn.setBorderPainted(false);
viewBtn.setFocusPainted(false);


// Edit button (Green)
editBtn.setBackground(new Color(46, 204, 113));
editBtn.setForeground(Color.WHITE);
editBtn.setOpaque(true);
editBtn.setContentAreaFilled(true);
editBtn.setBorderPainted(false);
editBtn.setFocusPainted(false);

// Delete button (Red)
deleteBtn.setBackground(new Color(231, 76, 60));
deleteBtn.setForeground(Color.WHITE);
deleteBtn.setOpaque(true);
deleteBtn.setContentAreaFilled(true);
deleteBtn.setBorderPainted(false);
deleteBtn.setFocusPainted(false);

    
 viewBtn.addActionListener(e -> {
    int row = scholarshipTable.getSelectedRow();
    if (row >= 0) {
        int id = (int) scholarshipTable.getValueAt(row, 0); // scholarship ID

        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM scholarships WHERE id = ?")) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
    String scholarshipName = rs.getString("name");

    StringBuilder info = new StringBuilder();
    info.append("<html><div style='font-size:14px;'>");
    info.append("<b>ID:</b> ").append(rs.getInt("id")).append("<br>");
    info.append("<b>Name:</b> ").append(scholarshipName).append("<br>");
    info.append("<b>Description:</b><br><div style='width:400px;'>")
        .append(rs.getString("description").replaceAll("\n", "<br>"))
        .append("</div><br>");
    info.append("<b>Type:</b> ").append(rs.getString("type")).append("<br>");
    info.append("<b>Start Date:</b> ").append(rs.getDate("start_date")).append("<br>");
    info.append("<b>Deadline:</b> ").append(rs.getDate("deadline")).append("<br>");
    info.append("<b>Slots Needed:</b> ").append(rs.getInt("student_needed")).append("<br>");
    info.append("<b>Contact Person:</b> ").append(rs.getString("contact_person")).append("<br>");
    info.append("</div></html>");

    JLabel label = new JLabel(info.toString());

    Object[] options = {"Generate PDF", "Close"};

    int choice = JOptionPane.showOptionDialog(
        null,
        label,
        "Scholarship Details - " + scholarshipName,  // Display name here
        JOptionPane.YES_NO_OPTION,
        JOptionPane.INFORMATION_MESSAGE,
        null,
        options,
        options[1]
    );

    if (choice == JOptionPane.YES_OPTION) {
        // Pass scholarshipName to the PDF generator if you want to display in PDF
        ScholarshipPDFGenerator.generate(id, scholarshipName, conn);
    }
}


        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch scholarship info from database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});


   editBtn.addActionListener(e -> {
    int row = scholarshipTable.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(null, "Please select a scholarship to edit.", 
                                     "No Selection", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    
    int id = (int) scholarshipTable.getValueAt(row, 0); // Get ID
    try (Connection conn = db_connection.getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM scholarships WHERE id = ?")) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            // Pre-fill fields with controlled height
            JTextField nameField = new JTextField(rs.getString("name"));
            nameField.setPreferredSize(new Dimension(200, 25)); // Control height
            
            JTextArea descField = new JTextArea(rs.getString("description"), 3, 20);
            descField.setLineWrap(true);
            descField.setWrapStyleWord(true);
            JScrollPane descScroll = new JScrollPane(descField);
            descScroll.setPreferredSize(new Dimension(200, 60)); // Control height
            
            JTextField typeField = new JTextField(rs.getString("type"));
            typeField.setPreferredSize(new Dimension(200, 25)); // Control height
            
            JTextField startField = new JTextField(rs.getString("start_date"));
            startField.setPreferredSize(new Dimension(200, 25)); // Control height
            
            JTextField deadlineField = new JTextField(rs.getString("deadline"));
            deadlineField.setPreferredSize(new Dimension(200, 25)); // Control height
            
            JTextField neededField = new JTextField(String.valueOf(rs.getInt("student_needed")));
            neededField.setPreferredSize(new Dimension(200, 25)); // Control height
            
            JTextField contactField = new JTextField(rs.getString("contact_person"));
            contactField.setPreferredSize(new Dimension(200, 25)); // Control height
            
            // Use a more compact panel
            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5)); // 2 columns with 5px spacing
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add some padding
            
            // Add components with labels
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            
            panel.add(new JLabel("Description:"));
            panel.add(descScroll);
            
            panel.add(new JLabel("Type:"));
            panel.add(typeField);
            
            panel.add(new JLabel("Start Date (YYYY-MM-DD):"));
            panel.add(startField);
            
            panel.add(new JLabel("Deadline (YYYY-MM-DD):"));
            panel.add(deadlineField);
            
            panel.add(new JLabel("Students Needed:"));
            panel.add(neededField);
            
            panel.add(new JLabel("Contact Person:"));
            panel.add(contactField);
            
            // Create a compact scrollable panel with controlled size
            JScrollPane scrollPanel = new JScrollPane(panel);
            scrollPanel.setPreferredSize(new Dimension(450, 300)); // Control overall size
            
            int result = JOptionPane.showConfirmDialog(null, scrollPanel, 
                    "Edit Scholarship", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                // Validate input (simple validation)
                if (nameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Name is required", 
                                                "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Save to DB
                String updateQuery = "UPDATE scholarships SET name=?, description=?, type=?, " +
                                    "start_date=?, deadline=?, student_needed=?, contact_person=? " +
                                    "WHERE id=?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, nameField.getText().trim());
                    updateStmt.setString(2, descField.getText().trim());
                    updateStmt.setString(3, typeField.getText().trim());
                    updateStmt.setString(4, startField.getText().trim());
                    updateStmt.setString(5, deadlineField.getText().trim());
                    updateStmt.setInt(6, Integer.parseInt(neededField.getText().trim()));
                    updateStmt.setString(7, contactField.getText().trim());
                    updateStmt.setInt(8, id);
                    
                    updateStmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Scholarship updated successfully!");
                    
                    // Update table model row data
                  // Assuming you want to update columns: Name (1), Type (2), Start Date (3), and Deadline (4)

scholarshipTable.setValueAt(nameField.getText().trim(), row, 1); // Update the Name column
// Do not update the description column (index 2), as it was removed from the table
scholarshipTable.setValueAt(typeField.getText().trim(), row, 2); // Update the Type column
scholarshipTable.setValueAt(startField.getText().trim(), row, 3); // Update the Start Date column
scholarshipTable.setValueAt(deadlineField.getText().trim(), row, 4); // Update the Deadline column

                    // Add similar updates for other visible columns
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Failed to fetch or update scholarship.", 
                                    "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "Please enter a valid number for Students Needed.", 
                                    "Input Error", JOptionPane.ERROR_MESSAGE);
    }
});
deleteBtn.addActionListener(e -> {
    int row = scholarshipTable.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(null, "Please select a scholarship to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int id = (int) scholarshipTable.getValueAt(row, 0); // Assuming column 0 = ID
    String scholarshipName = (String) scholarshipTable.getValueAt(row, 1); // Assuming column 1 = Name

    // Check for assigned students
    List<String> assignedStudents = getAssignedStudentNames(id);
    if (!assignedStudents.isEmpty()) {
        StringBuilder message = new StringBuilder("Cannot delete scholarship: \"" + scholarshipName + "\".\nStudents assigned:\n");
        for (String name : assignedStudents) {
            message.append("• ").append(name).append("\n");
        }
        message.append("Please reassign or remove these students first.");

        JOptionPane.showMessageDialog(null, message.toString(), "Deletion Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(null, "Delete scholarship: \"" + scholarshipName + "\"?", "Confirm", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        if (scholarshipTable.isEditing()) {
            scholarshipTable.getCellEditor().stopCellEditing();
        }

        if (deleteScholarship(id)) {
            DefaultTableModel model = (DefaultTableModel) scholarshipTable.getModel();
            model.removeRow(row);
            JOptionPane.showMessageDialog(null, "Scholarship \"" + scholarshipName + "\" deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "Failed to delete scholarship.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});




    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        return panel;
    }
}

private boolean deleteScholarship(int scholarshipId) {
    String sql = "DELETE FROM scholarships WHERE id = ?";
    try (Connection conn = db_connection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, scholarshipId);
        int affectedRows = stmt.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}

private List<String> getAssignedStudentNames(int scholarshipId) {
    List<String> studentNames = new ArrayList<>();
    String sql = "SELECT CONCAT(first_name, ' ', last_name) AS full_name FROM students WHERE scholarship_id = ?";

    try (Connection conn = db_connection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, scholarshipId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            studentNames.add(rs.getString("full_name"));
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    return studentNames;
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
        jButton3 = new javax.swing.JButton();
        students = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        scholarshipTable = new javax.swing.JTable();
        add_scholarships = new javax.swing.JButton();

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

        jButton3.setBackground(new java.awt.Color(255, 255, 237));
        jButton3.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scholarship_system/scholarship (1).png"))); // NOI18N
        jButton3.setText("Scholarship");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton3.setOpaque(true);

        students.setBackground(new java.awt.Color(255, 255, 237));
        students.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        students.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scholarship_system/graduating-student (2).png"))); // NOI18N
        students.setText("Students");
        students.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        students.setOpaque(true);
        students.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentsActionPerformed(evt);
            }
        });

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
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(students, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 55, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(students, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(logout_button, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(371, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 850));

        jPanel2.setBackground(new java.awt.Color(255, 255, 237));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(149, 149, 141)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        jLabel1.setText("Scholarship");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, 480, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(81, 81, 76));
        jLabel2.setText("Add, edit, and track scholarship opportunities available for students.");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, 670, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scholarship_system/scholarship (3).png"))); // NOI18N
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 30, 100, 80));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1532, 128));

        scholarshipTable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scholarshipTable.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        scholarshipTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        scholarshipTable.setGridColor(new java.awt.Color(255, 255, 237));
        scholarshipTable.setRowHeight(40);
        scholarshipTable.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jScrollPane1.setViewportView(scholarshipTable);
        if (scholarshipTable.getColumnModel().getColumnCount() > 0) {
            scholarshipTable.getColumnModel().getColumn(0).setResizable(false);
            scholarshipTable.getColumnModel().getColumn(1).setMinWidth(50);
            scholarshipTable.getColumnModel().getColumn(2).setResizable(false);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 190, 1150, 560));

        add_scholarships.setBackground(new java.awt.Color(255, 254, 0));
        add_scholarships.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        add_scholarships.setText(" Add scholarship");
        add_scholarships.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_scholarshipsActionPerformed(evt);
            }
        });
        jPanel1.add(add_scholarships, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 160, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardActionPerformed
        // TODO add your handling code here:
          admin_page dashboard = new admin_page();  // Create an instance of Login_page
       dashboard.setVisible(true);  // Make it visible
        this.setVisible(false);  // Hide the Landing page
    }//GEN-LAST:event_dashboardActionPerformed

    private void studentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentsActionPerformed
        // TODO add your handling code here:
          students students = new students();  // Create an instance of Login_page
        students.setVisible(true);  // Make it visible
        this.setVisible(false);  // Hide the Landing page
    }//GEN-LAST:event_studentsActionPerformed

    private void add_scholarshipsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_scholarshipsActionPerformed
     JTextField nameField = new JTextField(20);
    JTextArea descField = new JTextArea(3, 20);
    descField.setLineWrap(true);
    descField.setWrapStyleWord(true);

    JTextField typeField = new JTextField(20);
    JTextField startDateField = new JTextField("YYYY-MM-DD", 10);
    JTextField deadlineField = new JTextField("YYYY-MM-DD", 10);
    JTextField slotsField = new JTextField(5);
    JTextField contactField = new JTextField(20);

    JScrollPane scrollDesc = new JScrollPane(descField);
    scrollDesc.setPreferredSize(new Dimension(300, 60));

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 10, 5, 10);
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;

    panel.add(new JLabel("Name:"), gbc);
    gbc.gridx = 1;
    panel.add(nameField, gbc);

    gbc.gridx = 0; gbc.gridy++;
    panel.add(new JLabel("Description:"), gbc);
    gbc.gridx = 1;
    panel.add(scrollDesc, gbc);

    gbc.gridx = 0; gbc.gridy++;
    panel.add(new JLabel("Type:"), gbc);
    gbc.gridx = 1;
    panel.add(typeField, gbc);

    gbc.gridx = 0; gbc.gridy++;
    panel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
    gbc.gridx = 1;
    panel.add(startDateField, gbc);

    gbc.gridx = 0; gbc.gridy++;
    panel.add(new JLabel("Deadline (YYYY-MM-DD):"), gbc);
    gbc.gridx = 1;
    panel.add(deadlineField, gbc);

    gbc.gridx = 0; gbc.gridy++;
    panel.add(new JLabel("Students Needed:"), gbc);
    gbc.gridx = 1;
    panel.add(slotsField, gbc);

    gbc.gridx = 0; gbc.gridy++;
    panel.add(new JLabel("Contact Person:"), gbc);
    gbc.gridx = 1;
    panel.add(contactField, gbc);

    int result = JOptionPane.showConfirmDialog(null, panel, "Add Scholarship",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        String name = nameField.getText().trim();
        String desc = descField.getText().trim();
        String type = typeField.getText().trim();
        String start = startDateField.getText().trim();
        String deadline = deadlineField.getText().trim();
        String slots = slotsField.getText().trim();
        String contact = contactField.getText().trim();

        addScholarshipToDB(name, desc, type, start, deadline, slots, contact);
    }
    }//GEN-LAST:event_add_scholarshipsActionPerformed

    private void logout_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout_buttonActionPerformed
        // TODO add your handling code here:
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
private void addScholarshipToDB(String name, String desc, String type,
                                 String startDate, String deadline,
                                 String slots, String contact) {
    String query = "INSERT INTO scholarships (name, description, type, start_date, deadline, student_needed, contact_person) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = db_connection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, name);
        stmt.setString(2, desc);
        stmt.setString(3, type);
       stmt.setDate(4, java.sql.Date.valueOf(startDate));
stmt.setDate(5, java.sql.Date.valueOf(deadline));

        stmt.setInt(6, Integer.parseInt(slots));
        stmt.setString(7, contact);

        int rowsInserted = stmt.executeUpdate();
        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(null, "Scholarship added successfully!");
            DefaultTableModel model = (DefaultTableModel) scholarshipTable.getModel();
            model.setRowCount(0); // Clear table
            loadScholarships(model); // Reload
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Failed to add scholarship.\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    /**
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
            java.util.logging.Logger.getLogger(scholarship.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(scholarship.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(scholarship.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(scholarship.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new scholarship().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_scholarships;
    private javax.swing.JButton dashboard;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton logout_button;
    private javax.swing.JTable scholarshipTable;
    private javax.swing.JButton students;
    // End of variables declaration//GEN-END:variables
}
