package paneles;

import control.Validacion;
import java.awt.Color;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class Cuenta extends javax.swing.JPanel {

    Connection conexion;
    ResultSet usuario, empleado;
    Color colorDefecto, colorError;

    public Cuenta(Connection conexion, ResultSet usuario) {
        initComponents();
        this.usuario = usuario;
        colorDefecto = new Color(108, 116, 128);
        colorError = new Color(255, 0, 51);
        this.conexion = conexion;
        cargarDatosUsuario();
        nombrarCampos();
        bloquearNavegacionTab();
    }

    private void cargarDatosUsuario() {
        try {
            jtxtUsuario.setText(usuario.getString("NOM_PER"));
            jpswClave.setText(usuario.getString("CLA_PER"));
            String sql = "SELECT CED_EMP, NOM_EMP, APE_EMP, DATE_FORMAT(FEC_NAC_EMP,\"%d-%m-%Y\") AS FN, DIR_EMP, TEL_EMP, COR_EMP FROM EMPLEADO WHERE CED_EMP = ? ";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setString(1, usuario.getString("CED_EMP_PER"));
            empleado = statement.executeQuery();
            if (empleado.next()) {
                jtxtNombre.setText(empleado.getString("NOM_EMP"));
                jtxtApellido.setText(empleado.getString("APE_EMP"));
                jtxtCedula.setText(empleado.getString("CED_EMP"));
                jtxtFechaNacimiento.setText(empleado.getString("FN"));
                jtxtTelefono.setText(empleado.getString("TEL_EMP"));
                jtxtCorreo.setText(empleado.getString("COR_EMP"));
                jtxaDireccion.setText(empleado.getString("DIR_EMP"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void nombrarCampos() {
        try {
            jtxtNombre.setName(empleado.getString("NOM_EMP"));
            jtxtApellido.setName(empleado.getString("APE_EMP"));
            jtxtFechaNacimiento.setName(empleado.getString("FN"));
            jtxtUsuario.setName(usuario.getString("NOM_PER"));
            jtxtTelefono.setName(empleado.getString("TEL_EMP"));
            jtxtCorreo.setName(empleado.getString("COR_EMP"));
            jtxaDireccion.setName(empleado.getString("DIR_EMP"));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void bloquearNavegacionTab() {
        jtxtNombre.setFocusTraversalKeysEnabled(false);
        jtxtApellido.setFocusTraversalKeysEnabled(false);
        jtxtFechaNacimiento.setFocusTraversalKeysEnabled(false);
        jtxtUsuario.setFocusTraversalKeysEnabled(false);
        jtxtTelefono.setFocusTraversalKeysEnabled(false);
        jtxtCorreo.setFocusTraversalKeysEnabled(false);
        jtxaDireccion.setFocusTraversalKeysEnabled(false);
    }

    private void validarVacios(JComponent campo) {
        ArrayList<JComponent> formulario = new ArrayList<>(Arrays.asList(jtxtNombre, jtxtApellido, jtxtFechaNacimiento, jtxtUsuario, jtxtTelefono, jtxtCorreo, jtxaDireccion));
        formulario.remove(campo);
        for (JComponent campoF : formulario) {
            if (campoF instanceof JTextField) {
                if (((JTextField) campoF).getText().isEmpty()) {
                    if (campoF != jtxtTelefono) {
                        ((JTextField) campoF).setText(campoF.getName());
                        campoF.setForeground(colorDefecto);
                    } else if (verificarCambios(campoF)) {
                        campoF.setForeground(Color.green);
                    }
                }
            } else {
                if (((JTextArea) campoF).getText().isEmpty() && verificarCambios(campoF)) {
                    campoF.setForeground(Color.green);
                }
            }
        }
        definirEstadoBotones();
    }

    public boolean validarLongitudMinima(JTextField campo, int longitudMinima) {
        if (campo.getText().length() < longitudMinima) {
            campo.setForeground(colorError);
            return false;
        } else {
            campo.setForeground(Color.green);
            return true;
        }
    }

    private boolean validarUsuario() {
        if (!Validacion.validarUsuario(jtxtUsuario.getText())) {
            jtxtUsuario.setForeground(colorError);
            return false;
        }
        return true;
    }

    private boolean validarEmail() {
        if (!Validacion.validarEmail(jtxtCorreo.getText())) {
            jtxtCorreo.setForeground(colorError);
            return false;
        }
        return true;
    }

    private boolean validarFecha() {
        if (!Validacion.validarFechaDMA(jtxtFechaNacimiento.getText())) {
            jtxtFechaNacimiento.setForeground(colorError);
            return false;
        }
        return true;
    }

    private boolean validarTelefono() {
        if (!Validacion.validarTelefono(jtxtTelefono.getText())) {
            jtxtTelefono.setForeground(colorError);
            return false;
        }
        if (jtxtTelefono.getText().isEmpty()) {
            jtxtTelefono.setForeground(colorDefecto);
        }
        return true;
    }

    private void validarDireccion(java.awt.event.KeyEvent evt) {
        char validar = evt.getKeyChar();
        if (validar == '\n' || validar == '\t') {
            String aux = jtxaDireccion.getText().trim();
            jtxaDireccion.setText(aux);
        }
    }

    private boolean verificarCambios(JComponent campo) {
        if (campo instanceof JTextField) {
            if (((JTextField) campo).getText().equalsIgnoreCase(campo.getName())) {
                campo.setForeground(colorDefecto);
                return false;
            } else {
                campo.setForeground(Color.green);
                return true;
            }
        } else {
            if (((JTextArea) campo).getText().equalsIgnoreCase(campo.getName())) {
                campo.setForeground(colorDefecto);
                return false;
            } else {
                campo.setForeground(Color.green);
                return true;
            }
        }
    }

    private void definirEstadoBotones() {
        ArrayList<JComponent> formulario = new ArrayList<>(Arrays.asList(jtxtNombre, jtxtApellido, jtxtFechaNacimiento, jtxtUsuario, jtxtTelefono, jtxtCorreo, jtxaDireccion));
        boolean cambio = false;
        boolean error = false;
        for (JComponent campoF : formulario) {
            if (campoF.getForeground().equals(Color.green)) {
                cambio = true;
            } else if (campoF.getForeground().equals(colorError)) {
                error = true;
            }
        }
        if (cambio) {
            jtxtCancelar.setEnabled(true);
            jtxtGuardar.setEnabled(true);
        }
        if (error) {
            jtxtCancelar.setEnabled(true);
            jtxtGuardar.setEnabled(false);
        }
        if (!cambio && !error) {
            jtxtCancelar.setEnabled(false);
            jtxtGuardar.setEnabled(false);
        }
    }

    private void restablecerColorTextos() {
        ArrayList<JComponent> formulario = new ArrayList<>(Arrays.asList(jtxtNombre, jtxtApellido, jtxtFechaNacimiento, jtxtUsuario, jtxtTelefono, jtxtCorreo, jtxaDireccion));
        for (JComponent campoF : formulario) {
            campoF.setForeground(colorDefecto);
        }
    }

    private void restablecerDatosUsuario() throws SQLException {
        cargarDatosUsuario();
        restablecerColorTextos();
        jtxtCancelar.setEnabled(false);
        jtxtGuardar.setEnabled(false);
    }

    private void guardarDatosUsuario() throws SQLException {
        conexion.setAutoCommit(false);
        String sqlUsuario = "UPDATE PERFIL SET NOM_PER = ? WHERE CED_EMP_PER = ? ";
        String sqlEmpleado = "UPDATE EMPLEADO SET NOM_EMP = ?, APE_EMP = ?, FEC_NAC_EMP = ?, DIR_EMP = ?, TEL_EMP = ?, COR_EMP = ? WHERE CED_EMP = ? ";
        PreparedStatement psUsuario = null, psEmpleado = null;
        try {
            psUsuario = conexion.prepareStatement(sqlUsuario);
            psUsuario.setString(1, jtxtUsuario.getText());
            psUsuario.setString(2, usuario.getString("CED_EMP_PER"));

            psEmpleado = conexion.prepareStatement(sqlEmpleado);
            psEmpleado.setString(1, jtxtNombre.getText());
            psEmpleado.setString(2, jtxtApellido.getText());
            psEmpleado.setString(3, cambiarFormatoFecha());
            psEmpleado.setString(4, jtxaDireccion.getText());
            psEmpleado.setString(5, jtxtTelefono.getText());
            psEmpleado.setString(6, jtxtCorreo.getText());
            psEmpleado.setString(7, usuario.getString("CED_EMP_PER"));
            psEmpleado.executeUpdate();

            conexion.commit();
        } catch (SQLException ex) {
            conexion.rollback();
        } finally {
            if (psUsuario != null) {
                psUsuario.close();
            }
            if (psEmpleado != null) {
                psEmpleado.close();
            }
            conexion.setAutoCommit(true);
            cargarDatosUsuario();
            restablecerColorTextos();
            nombrarCampos();
            jtxtCancelar.setEnabled(false);
            jtxtGuardar.setEnabled(false);
        }
    }

    private String cambiarFormatoFecha() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = sdf.parse(jtxtFechaNacimiento.getText());
            sdf.applyPattern("yyyy/MM/dd");
            return sdf.format(date);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jtxtNombre = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jtxtApellido = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jtxtCedula = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jtxtFechaNacimiento = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtxtUsuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtxtCorreo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtxtTelefono = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtxaDireccion = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jpnlEditarClave = new javax.swing.JPanel();
        jlblEditarClave = new javax.swing.JLabel();
        jpswClave = new javax.swing.JPasswordField();
        jpnlCancelar = new javax.swing.JPanel();
        jtxtCancelar = new javax.swing.JLabel();
        jlblBotonCancelar = new javax.swing.JLabel();
        jpnlGuardar = new javax.swing.JPanel();
        jtxtGuardar = new javax.swing.JLabel();
        jlblBotonGuardar = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(415, 390));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(28, 59, 112));
        jLabel1.setText("NOMBRE");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 165, 20));

        jtxtNombre.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtxtNombre.setForeground(new java.awt.Color(108, 116, 128));
        jtxtNombre.setText("NOMBRE");
        Border line = BorderFactory.createLineBorder(Color.DARK_GRAY);
        Border empty = new EmptyBorder(6, 5, 2, 2);
        CompoundBorder border = new CompoundBorder(line, empty);
        jtxtNombre.setBorder(border);
        jtxtNombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtxtNombreMousePressed(evt);
            }
        });
        jtxtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtNombreKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtNombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtNombreKeyTyped(evt);
            }
        });
        add(jtxtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 165, 30));

        jLabel6.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(28, 59, 112));
        jLabel6.setText("APELLIDO");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 165, 20));

        jtxtApellido.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtxtApellido.setForeground(new java.awt.Color(108, 116, 128));
        jtxtApellido.setText("APELLIDO");
        jtxtApellido.setBorder(border);
        jtxtApellido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtxtApellidoMousePressed(evt);
            }
        });
        jtxtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtApellidoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtApellidoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtApellidoKeyTyped(evt);
            }
        });
        add(jtxtApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, 165, 30));

        jLabel9.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(28, 59, 112));
        jLabel9.setText("CÉDULA");
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 165, 20));

        jtxtCedula.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtxtCedula.setForeground(new java.awt.Color(108, 116, 128));
        jtxtCedula.setText("XXXXXXXXXX");
        jtxtCedula.setBorder(border);
        jtxtCedula.setEnabled(false);
        add(jtxtCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 165, 30));

        jLabel10.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(28, 59, 112));
        jLabel10.setText("FECHA DE NACIMIENTO");
        add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 165, 20));

        jtxtFechaNacimiento.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtxtFechaNacimiento.setForeground(new java.awt.Color(108, 116, 128));
        jtxtFechaNacimiento.setText("DD-MM-AAAA");
        jtxtFechaNacimiento.setBorder(border);
        jtxtFechaNacimiento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtxtFechaNacimientoMousePressed(evt);
            }
        });
        jtxtFechaNacimiento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtFechaNacimientoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtFechaNacimientoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtFechaNacimientoKeyTyped(evt);
            }
        });
        add(jtxtFechaNacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 165, 30));

        jLabel2.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(28, 59, 112));
        jLabel2.setText("USUARIO");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 165, 20));

        jtxtUsuario.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtxtUsuario.setForeground(new java.awt.Color(108, 116, 128));
        jtxtUsuario.setText("USUARIO");
        jtxtUsuario.setBorder(border);
        jtxtUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtxtUsuarioMousePressed(evt);
            }
        });
        jtxtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtUsuarioKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtUsuarioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtUsuarioKeyTyped(evt);
            }
        });
        add(jtxtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 165, 30));

        jLabel3.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(28, 59, 112));
        jLabel3.setText("CORREO ELECTRÓNICO");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 200, 195, 20));

        jtxtCorreo.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtxtCorreo.setForeground(new java.awt.Color(108, 116, 128));
        jtxtCorreo.setText("CORREO@GMAIL.COM");
        jtxtCorreo.setBorder(border);
        jtxtCorreo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtxtCorreoMousePressed(evt);
            }
        });
        jtxtCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtCorreoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtCorreoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtCorreoKeyTyped(evt);
            }
        });
        add(jtxtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 220, 195, 30));

        jLabel4.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(28, 59, 112));
        jLabel4.setText("DIRECCIÓN");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 200, 20));

        jtxtTelefono.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtxtTelefono.setForeground(new java.awt.Color(108, 116, 128));
        jtxtTelefono.setText("09XXXXXXXX");
        jtxtTelefono.setBorder(border);
        jtxtTelefono.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtxtTelefonoMousePressed(evt);
            }
        });
        jtxtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtTelefonoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtTelefonoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtTelefonoKeyTyped(evt);
            }
        });
        add(jtxtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 165, 30));

        jLabel5.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(28, 59, 112));
        jLabel5.setText("NÚMERO DE TELÉFONO");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 165, 20));

        jtxaDireccion.setColumns(20);
        jtxaDireccion.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jtxaDireccion.setForeground(new java.awt.Color(108, 116, 128));
        jtxaDireccion.setRows(5);
        jtxaDireccion.setText("Avenida Ejemplo\nCalle primaria 1-23 y calle\nsecundaria\nAmbato\nEcuador");
        jtxaDireccion.setLineWrap(true);
        jtxaDireccion.setWrapStyleWord(true);
        jtxaDireccion.setBorder(border);
        jtxaDireccion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtxaDireccionMousePressed(evt);
            }
        });
        jtxaDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxaDireccionKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxaDireccionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxaDireccionKeyTyped(evt);
            }
        });
        add(jtxaDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 200, 90));

        jLabel7.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(28, 59, 112));
        jLabel7.setText("CONTRASEÑA");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, 165, 20));

        jpnlEditarClave.setBackground(new java.awt.Color(255, 255, 255));
        jpnlEditarClave.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jlblEditarClave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cambiar_25_25.png"))); // NOI18N
        jlblEditarClave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlblEditarClave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlblEditarClaveMouseClicked(evt);
            }
        });
        jpnlEditarClave.add(jlblEditarClave, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 25, 25));

        add(jpnlEditarClave, new org.netbeans.lib.awtextra.AbsoluteConstraints(365, 160, 30, 30));

        jpswClave.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jpswClave.setText("CONTRASEÑA");
        jpswClave.setEnabled(false);
        jpswClave.setBorder(border);
        add(jpswClave, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 160, 165, 30));

        jpnlCancelar.setBackground(new java.awt.Color(255, 255, 255));
        jpnlCancelar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtxtCancelar.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtxtCancelar.setForeground(new java.awt.Color(28, 59, 112));
        jtxtCancelar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtxtCancelar.setText("CANCELAR");
        jtxtCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtxtCancelar.setEnabled(false);
        jtxtCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtxtCancelarMouseClicked(evt);
            }
        });
        jpnlCancelar.add(jtxtCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1, 120, 30));

        jlblBotonCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/botonB_120_30.png"))); // NOI18N
        jpnlCancelar.add(jlblBotonCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 30));

        add(jpnlCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 300, 120, 30));

        jpnlGuardar.setBackground(new java.awt.Color(255, 255, 255));
        jpnlGuardar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtxtGuardar.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtxtGuardar.setForeground(new java.awt.Color(255, 255, 255));
        jtxtGuardar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtxtGuardar.setText("GUARDAR");
        jtxtGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtxtGuardar.setEnabled(false);
        jtxtGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtxtGuardarMouseClicked(evt);
            }
        });
        jpnlGuardar.add(jtxtGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1, 120, 30));

        jlblBotonGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/boton_120_30.png"))); // NOI18N
        jpnlGuardar.add(jlblBotonGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 30));

        add(jpnlGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 340, 120, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNombreKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jtxtNombreKeyPressed

    private void jtxtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNombreKeyReleased
        if (validarLongitudMinima(jtxtNombre, 2)) {
            verificarCambios(jtxtNombre);
        }
        definirEstadoBotones();
    }//GEN-LAST:event_jtxtNombreKeyReleased

    private void jtxtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNombreKeyTyped
        Validacion.convertirMayuscula(evt);
        Validacion.validarNLetras(evt, jtxtNombre, 25);
    }//GEN-LAST:event_jtxtNombreKeyTyped

    private void jtxtNombreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtNombreMousePressed
        validarVacios(jtxtNombre);
    }//GEN-LAST:event_jtxtNombreMousePressed

    private void jtxtApellidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtApellidoKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jtxtApellidoKeyPressed

    private void jtxtApellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtApellidoKeyReleased
        if (validarLongitudMinima(jtxtApellido, 2)) {
            verificarCambios(jtxtApellido);
        }
        definirEstadoBotones();
    }//GEN-LAST:event_jtxtApellidoKeyReleased

    private void jtxtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtApellidoKeyTyped
        Validacion.convertirMayuscula(evt);
        Validacion.validarNLetras(evt, jtxtApellido, 25);
    }//GEN-LAST:event_jtxtApellidoKeyTyped

    private void jtxtApellidoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtApellidoMousePressed
        validarVacios(jtxtApellido);
    }//GEN-LAST:event_jtxtApellidoMousePressed

    private void jtxtFechaNacimientoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtFechaNacimientoKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jtxtFechaNacimientoKeyPressed

    private void jtxtFechaNacimientoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtFechaNacimientoKeyReleased
        if (validarFecha()) {
            verificarCambios(jtxtFechaNacimiento);
        }
        definirEstadoBotones();
    }//GEN-LAST:event_jtxtFechaNacimientoKeyReleased

    private void jtxtFechaNacimientoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtFechaNacimientoKeyTyped
        Validacion.validarSinEspacios(evt);
        Validacion.validarNCaracteres(evt, jtxtFechaNacimiento, 10);
    }//GEN-LAST:event_jtxtFechaNacimientoKeyTyped

    private void jtxtFechaNacimientoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtFechaNacimientoMousePressed
        validarVacios(jtxtFechaNacimiento);
    }//GEN-LAST:event_jtxtFechaNacimientoMousePressed

    private void jtxtUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtUsuarioKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jtxtUsuarioKeyPressed

    private void jtxtUsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtUsuarioKeyReleased
        if (validarUsuario()) {
            verificarCambios(jtxtUsuario);
        }
        definirEstadoBotones();
    }//GEN-LAST:event_jtxtUsuarioKeyReleased

    private void jtxtUsuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtUsuarioKeyTyped
        Validacion.validarSinEspacios(evt);
        Validacion.validarNCaracteres(evt, jtxtUsuario, 30);
    }//GEN-LAST:event_jtxtUsuarioKeyTyped

    private void jtxtUsuarioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtUsuarioMousePressed
        validarVacios(jtxtUsuario);
    }//GEN-LAST:event_jtxtUsuarioMousePressed

    private void jtxtTelefonoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTelefonoKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jtxtTelefonoKeyPressed

    private void jtxtTelefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTelefonoKeyReleased
        if (validarTelefono()) {
            verificarCambios(jtxtTelefono);
        }
        definirEstadoBotones();
    }//GEN-LAST:event_jtxtTelefonoKeyReleased

    private void jtxtTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTelefonoKeyTyped
        Validacion.validarNNumeros(evt, jtxtTelefono, 10);
    }//GEN-LAST:event_jtxtTelefonoKeyTyped

    private void jtxtTelefonoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtTelefonoMousePressed
        validarVacios(jtxtTelefono);
    }//GEN-LAST:event_jtxtTelefonoMousePressed

    private void jtxtCorreoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCorreoKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jtxtCorreoKeyPressed

    private void jtxtCorreoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCorreoKeyReleased
        if (validarEmail()) {
            verificarCambios(jtxtCorreo);
        }
        definirEstadoBotones();
    }//GEN-LAST:event_jtxtCorreoKeyReleased

    private void jtxtCorreoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCorreoKeyTyped
        Validacion.validarSinEspacios(evt);
        Validacion.validarNCaracteres(evt, jtxtCorreo, 254);
    }//GEN-LAST:event_jtxtCorreoKeyTyped

    private void jtxtCorreoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtCorreoMousePressed
        validarVacios(jtxtCorreo);
    }//GEN-LAST:event_jtxtCorreoMousePressed

    private void jtxaDireccionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxaDireccionKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jtxaDireccionKeyPressed

    private void jtxaDireccionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxaDireccionKeyReleased
        verificarCambios(jtxaDireccion);
        definirEstadoBotones();
    }//GEN-LAST:event_jtxaDireccionKeyReleased

    private void jtxaDireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxaDireccionKeyTyped
        validarDireccion(evt);
        Validacion.validarNCaracteres(evt, jtxaDireccion, 100);
    }//GEN-LAST:event_jtxaDireccionKeyTyped

    private void jtxaDireccionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxaDireccionMousePressed
        validarVacios(jtxaDireccion);
    }//GEN-LAST:event_jtxaDireccionMousePressed

    private void jlblEditarClaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlblEditarClaveMouseClicked
        PanelPrincipal.redefinirPanelContenido(new CambiarClave(conexion, usuario));
    }//GEN-LAST:event_jlblEditarClaveMouseClicked

    private void jtxtCancelarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtCancelarMouseClicked
        if (jtxtCancelar.isEnabled()) {
            try {
                restablecerDatosUsuario();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex);
            }
        }
    }//GEN-LAST:event_jtxtCancelarMouseClicked

    private void jtxtGuardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtGuardarMouseClicked
        if (jtxtGuardar.isEnabled()) {
            try {
                guardarDatosUsuario();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex);
            }
        }
    }//GEN-LAST:event_jtxtGuardarMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jlblBotonCancelar;
    private javax.swing.JLabel jlblBotonGuardar;
    private javax.swing.JLabel jlblEditarClave;
    private javax.swing.JPanel jpnlCancelar;
    private javax.swing.JPanel jpnlEditarClave;
    private javax.swing.JPanel jpnlGuardar;
    private javax.swing.JPasswordField jpswClave;
    private javax.swing.JTextArea jtxaDireccion;
    private javax.swing.JTextField jtxtApellido;
    private javax.swing.JLabel jtxtCancelar;
    private javax.swing.JTextField jtxtCedula;
    private javax.swing.JTextField jtxtCorreo;
    private javax.swing.JTextField jtxtFechaNacimiento;
    private javax.swing.JLabel jtxtGuardar;
    private javax.swing.JTextField jtxtNombre;
    private javax.swing.JTextField jtxtTelefono;
    private javax.swing.JTextField jtxtUsuario;
    // End of variables declaration//GEN-END:variables
}
