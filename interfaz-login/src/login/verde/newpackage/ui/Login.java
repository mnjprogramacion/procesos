
package login.verde.newpackage.ui;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Login.class.getName());

    public Login() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldUrl = new javax.swing.JTextField();
        jPasswordContr = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldCorreo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldUsuario = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setMaximumSize(new java.awt.Dimension(500, 500));
        setMinimumSize(new java.awt.Dimension(500, 500));
        setPreferredSize(new java.awt.Dimension(500, 500));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(500, 500));
        jPanel1.setMinimumSize(new java.awt.Dimension(500, 500));
        jPanel1.setName(""); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 500));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 102));
        jLabel1.setText("LOGIN");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(180, 10, 110, 48);

        jLabel2.setBackground(new java.awt.Color(102, 102, 102));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Usuario");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(80, 250, 50, 20);

        jTextFieldUrl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldUrl.setForeground(new java.awt.Color(102, 102, 102));
        jTextFieldUrl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldUrlActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldUrl);
        jTextFieldUrl.setBounds(80, 190, 324, 38);

        jPasswordContr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordContrActionPerformed(evt);
            }
        });
        jPanel1.add(jPasswordContr);
        jPasswordContr.setBounds(80, 370, 324, 38);

        jLabel3.setBackground(new java.awt.Color(102, 102, 102));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Contraseña");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(80, 340, 80, 20);

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Login");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(80, 440, 100, 36);

        jLabel8.setBackground(new java.awt.Color(102, 102, 102));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Correo electrónico");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(80, 70, 120, 20);

        jTextFieldCorreo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldCorreo.setForeground(new java.awt.Color(102, 102, 102));
        jTextFieldCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCorreoActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldCorreo);
        jTextFieldCorreo.setBounds(80, 100, 324, 38);

        jLabel9.setBackground(new java.awt.Color(102, 102, 102));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("URL");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(80, 160, 25, 20);

        jTextFieldUsuario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldUsuario.setForeground(new java.awt.Color(102, 102, 102));
        jTextFieldUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldUsuarioActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldUsuario);
        jTextFieldUsuario.setBounds(80, 280, 324, 38);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.getAccessibleContext().setAccessibleName("LOGIN");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    String correo = jTextFieldCorreo.getText();
    String url = jTextFieldUrl.getText();
    String usuario = jTextFieldUsuario.getText();
    String contraseña = new String(jPasswordContr.getPassword());

    String regexCorreo = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    String regexURL = "(^(https?:\\/\\/[\\w.-]+\\.[a-zA-Z]{2,}\\/[\\w.-]+:\\d+)$)|(^((\\d{1,3}\\.){3}\\d{1,3}:\\d+)$)";
    String regexUsuario = "^[A-Z@#$%^&*+=!¡¿?][A-Z0-9@#$%^&*+=!¡¿?]*$";
    String regexPassword = "^[a-z][a-zA-Z0-9]*$";

    if (!correo.matches(regexCorreo)) {
        JOptionPane.showMessageDialog(this, "❌ Correo electrónico no válido.");
        return;
    }

    if (!url.matches(regexURL)) {
        JOptionPane.showMessageDialog(this, "❌ URL no válida. Debe ser http(s)://...:puerto o IP:puerto");
        return;
    }

    //  Aviso si no usa HTTPS
    if (url.startsWith("http://")) {
        JOptionPane.showMessageDialog(this, "⚠ Atención: estás usando HTTP en lugar de HTTPS. La conexión puede no ser segura.");
    }

    if (!usuario.matches(regexUsuario)) {
        JOptionPane.showMessageDialog(this, "❌ Usuario no válido. Sólo puede incluir mayúsculas, símbolos y números, y no puede comenzar con número.");
        return;
    }

    if (!contraseña.matches(regexPassword)) {
        JOptionPane.showMessageDialog(this, "❌ Contraseña no válida. Debe empezar con minúscula y solo puede contener letras y números.");
        return;
    }

    JOptionPane.showMessageDialog(this, "✅ Datos válidos, iniciando sesión...");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextFieldCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCorreoActionPerformed

    private void jTextFieldUrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldUrlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldUrlActionPerformed

    private void jTextFieldUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldUsuarioActionPerformed

    private void jPasswordContrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordContrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordContrActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordContr;
    private javax.swing.JTextField jTextFieldCorreo;
    private javax.swing.JTextField jTextFieldUrl;
    private javax.swing.JTextField jTextFieldUsuario;
    // End of variables declaration//GEN-END:variables
}
