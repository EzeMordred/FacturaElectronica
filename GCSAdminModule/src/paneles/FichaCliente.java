package paneles;

import entidades.Cliente;
import javax.swing.JPanel;

public class FichaCliente extends javax.swing.JPanel {
    
    Cliente cliente;

    public FichaCliente(Cliente estudiante) {
        initComponents();
        this.cliente = estudiante;
        fillFields();
    }
    
    private void fillFields() {
        txtCedula.setText(cliente.getCedula());
        txtNombre.setText(String.format("%s %s", cliente.getNombre(), cliente.getApellido()));
        txtTelefono.setText(cliente.getTelefono());
        txtDireccion.setText(cliente.getDireccion());
    }

    public Cliente getEstudiante() {
        return cliente;
    }

    public JPanel getBkFicha() {
        return bkFicha;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtCedula = new javax.swing.JLabel();
        txtNombre = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JLabel();
        bkFicha = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 30, 38)));
        setPreferredSize(new java.awt.Dimension(623, 90));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel1.setText("Cédula:");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel3.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel3.setText("Nombre:");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        jLabel5.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel5.setText("Teléfono:");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, -1, -1));

        jLabel6.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel6.setText("Dirección:");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, -1, -1));

        txtCedula.setFont(new java.awt.Font("Roboto Light", 0, 18)); // NOI18N
        txtCedula.setText("1801020304");
        add(txtCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, -1, -1));

        txtNombre.setFont(new java.awt.Font("Roboto Light", 0, 18)); // NOI18N
        txtNombre.setText("Christian Jaramillo");
        add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, -1, -1));

        txtTelefono.setFont(new java.awt.Font("Roboto Light", 0, 18)); // NOI18N
        txtTelefono.setText("0910203040");
        add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, -1, -1));

        txtDireccion.setFont(new java.awt.Font("Roboto Light", 0, 18)); // NOI18N
        txtDireccion.setText("Huachi Chico");
        add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 60, -1, -1));

        bkFicha.setBackground(new java.awt.Color(255, 255, 255));
        bkFicha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bkFichaMouseClicked(evt);
            }
        });
        add(bkFicha, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 605, 112));
    }// </editor-fold>//GEN-END:initComponents

    private void bkFichaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bkFichaMouseClicked
        ListaClientes.seleccionarRegistro(cliente.getCedula());
    }//GEN-LAST:event_bkFichaMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bkFicha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel txtCedula;
    private javax.swing.JLabel txtDireccion;
    private javax.swing.JLabel txtNombre;
    private javax.swing.JLabel txtTelefono;
    // End of variables declaration//GEN-END:variables
}
