/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package scholarship_system;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
/**
 *
 * @author Admin
 */
public class user_page extends javax.swing.JFrame {

    /**
     * Creates new form user_page
     */
    public user_page() {
         initComponents();
        jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.Y_AXIS));
    displayScholarships(); // call to display scholarships
    }
   



   
// Method to handle scholarship application
private void openApplicationForm(String scholarshipName) {
    // Display a message (placeholder for actual form)
    JOptionPane.showMessageDialog(this, 
        "Opening application form for: " + scholarshipName,
        "Scholarship Application", 
        JOptionPane.INFORMATION_MESSAGE);
    
    // TODO: Implement actual application form
    // This could open a new window/frame with the application form
    // or navigate to a different panel in your application
}
   
private void displayScholarships() {
    String[][] scholarships = {
        {"Full Scholarship", "Covers full tuition.", "Why do you deserve this?"},
        {"Merit Scholarship", "Based on academic performance.", "What is your best achievement?"},
        {"Sports Scholarship", "For outstanding athletes.", "What sport do you excel in?"},
        {"Community Scholarship", "For volunteers & leaders.", "How have you helped your community?"},
        {"Arts Scholarship", "For talented artists.", "What art form are you passionate about?"}
    };
    
    // Calculate how many rows we'll need
    int totalScholarships = scholarships.length;
    int cardsPerRow = 3;
    int numRows = (int) Math.ceil((double) totalScholarships / cardsPerRow);
    
    // Create row panels and add cards to them
    for (int row = 0; row < numRows; row++) {
        // Create a panel for this row with FlowLayout
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 120, 10));
        
        // Determine start and end indices for this row
        int startIdx = row * cardsPerRow;
        int endIdx = Math.min((row + 1) * cardsPerRow, totalScholarships);
        
        // Add cards to this row
        for (int i = startIdx; i < endIdx; i++) {
            String[] s = scholarships[i];
            
            JPanel card = new JPanel();
            card.setPreferredSize(new Dimension(350, 200)); // This will be respected now
            card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            
            JLabel name = new JLabel("Name: " + s[0]);
            JLabel desc = new JLabel("<html>Description: " + s[1] + "</html>");
            JLabel question = new JLabel("<html>Question: " + s[2] + "</html>");
            
            // Make labels center-aligned horizontally
            name.setAlignmentX(Component.CENTER_ALIGNMENT);
            desc.setAlignmentX(Component.CENTER_ALIGNMENT);
            question.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Create an Apply button
            JButton applyButton = new JButton("Apply");
            applyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Add action listener to the button
            final String scholarshipName = s[0]; // Store the scholarship name for use in lambda
            applyButton.addActionListener((ActionEvent e) -> {
                openApplicationForm(scholarshipName);
            });
            
            // Style the button (optional)
            applyButton.setBackground(new Color(0, 123, 255));
            applyButton.setForeground(Color.WHITE);
            applyButton.setFocusPainted(false);
            
            // Add components to card
            card.add(Box.createVerticalStrut(10)); // Top margin
            card.add(name);
            card.add(Box.createVerticalStrut(10));
            card.add(desc);
            card.add(Box.createVerticalStrut(10));
            card.add(question);
            card.add(Box.createVerticalGlue()); // Fill remaining space
            card.add(applyButton);
            card.add(Box.createVerticalStrut(15)); // Bottom margin
            
            // Add card to row panel
            rowPanel.add(card);
        }
        
        // Add row panel to main panel
        jPanel3.add(rowPanel);
    }
    
    // Revalidate and repaint to apply changes
    jPanel3.revalidate();
    jPanel3.repaint();
}
    /**
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1920, 850));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 237));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setPreferredSize(new java.awt.Dimension(1100, 5));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        jLabel1.setText("Welcome, Jozel M. Viernes");

        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1721, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1914, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(538, 538, 538)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 772, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, -1));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 2000, 520));

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(user_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(user_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(user_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(user_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new user_page().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}
