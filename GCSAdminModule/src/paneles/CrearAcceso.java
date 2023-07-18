package paneles;

import control.Password;
import control.Validacion;
import java.awt.Color;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CrearAcceso extends javax.swing.JPanel {

    Connection conexion;
    Color colorDefecto, colorError;
    int xMouse, yMouse;
    boolean claveOculta;

    public CrearAcceso(Connection conexion) {
        initComponents();
        this.conexion = conexion;
        colorDefecto = new Color(204, 204, 204);
        colorError = new Color(255, 0, 51);
        //this.setLocationRelativeTo(null);
        inicializarClave();
        nombrarCampos();
        bloquearNavegacionTab();
    }

    private void inicializarClave() {
        claveOculta = false;
        jpswClave.setEchoChar((char) 0);
        jpswClaveC.setEchoChar((char) 0);
        jlblVerOcultarClave.setIcon(new ImageIcon(getClass().getResource("/imagenes/verClave_40.png")));
    }

    private void bloquearNavegacionTab() {
        jtxtNombre.setFocusTraversalKeysEnabled(false);
        jtxtApellido.setFocusTraversalKeysEnabled(false);
        jtxtUsuario.setFocusTraversalKeysEnabled(false);
        jpswClave.setFocusTraversalKeysEnabled(false);
        jpswClaveC.setFocusTraversalKeysEnabled(false);
        jtxtCorreo.setFocusTraversalKeysEnabled(false);
    }

    private void definirIconoOcultarClave() {
        if (claveOculta) {
            inicializarClave();
        } else {
            jpswClave.setEchoChar('*');
            jpswClaveC.setEchoChar('*');
            claveOculta = true;
            jlblVerOcultarClave.setIcon(new ImageIcon(getClass().getResource("/imagenes/ocultarClave_40.png")));
        }
    }

    private void nombrarCampos() {
        jtxtNombre.setName("NOMBRE");
        jtxtApellido.setName("APELLIDO");
        jtxtCedula.setName("Cï¿½DULA");
        jtxtFechaNacimiento.setName("FECHA DE NACIMIENTO");
        jtxtUsuario.setName("NOMBRE DE USUARIO");
        jpswClave.setName("CONTRASEï¿½A");
        jpswClaveC.setName("CONFIRMACIï¿½N");
        jtxtCorreo.setName("CORREO ELECTRï¿½NICO");
    }

    private void textosPorDefecto(JTextField campo) {
        ArrayList<JTextField> formulario = new ArrayList<>(Arrays.asList(jtxtNombre, jtxtApellido, jtxtCedula, jtxtFechaNacimiento, jtxtUsuario, jpswClave, jpswClaveC, jtxtCorreo));
        if (campo.getText().equals(campo.getName())) {
            campo.setText("");
            campo.setForeground(Color.black);
        }
        formulario.remove(campo);
        for (JTextField campoF : formulario) {
            if (campoF.getText().isEmpty()) {
                campoF.setText(campoF.getName());
                campoF.setForeground(colorDefecto);
            }
        }
    }

    public boolean validarLongitudMinima(JTextField campo, int longitudMinima) {
        if (campo.getText().length() < longitudMinima) {
            campo.setForeground(colorError);
            return false;
        } else {
            campo.setForeground(Color.black);
            return true;
        }
    }

    private void validarEmail() {
        if (!Validacion.validarEmail(jtxtCorreo.getText())) {
            jtxtCorreo.setForeground(colorError);
        } else {
            jtxtCorreo.setForeground(Color.black);
        }
    }

    private void validarFecha() {
        if (!Validacion.validarFechaDMA(jtxtFechaNacimiento.getText())) {
            jtxtFechaNacimiento.setForeground(colorError);
        } else {
            jtxtFechaNacimiento.setForeground(Color.black);
        }
    }

    private void validarClave() {
        if (!Validacion.validarClave(new String(jpswClave.getPassword()))) {
            jpswClave.setForeground(colorError);
        } else {
            jpswClave.setForeground(Color.black);
        }
    }

    private void validarCoincidencia() {
        if (String.valueOf(jpswClaveC.getPassword()).equals(jpswClaveC.getName())) {
            return;
        }
        if (String.valueOf(jpswClave.getPassword()).equals(String.valueOf(jpswClaveC.getPassword()))) {
            jpswClaveC.setForeground(Color.black);
            jlblNotaConfirmacion.setText("");
        } else {
            jpswClaveC.setForeground(colorError);
            jlblNotaConfirmacion.setText("CONTRASEï¿½AS NO COINCIDEN");
        }
    }

    private void validarCedula() {
        if (!Validacion.validarCedula(jtxtCedula.getText())) {
            jtxtCedula.setForeground(colorError);
        } else {
            jtxtCedula.setForeground(Color.black);
        }
    }

    private void validarUsuario() {
        if (!Validacion.validarUsuario(jtxtUsuario.getText())) {
            jtxtUsuario.setForeground(colorError);
        } else {
            jtxtUsuario.setForeground(Color.black);
        }
    }

    private void definirEstadoBoton() {
        ArrayList<JComponent> formulario = new ArrayList<>(Arrays.asList(jtxtNombre, jtxtApellido, jtxtCorreo, jtxtCedula, jtxtUsuario, jtxtFechaNacimiento, jpswClave, jpswClaveC));
        for (JComponent campoF : formulario) {
            if (campoF.getForeground().equals(colorError) || campoF.getForeground().equals(colorDefecto)) {
                jtxtCrear.setEnabled(false);
                return;
            }
        }
        jtxtCrear.setEnabled(true);
    }

    private boolean validarDatos() throws SQLException {
        if (cedulaRegistrada()) {
            JOptionPane.showMessageDialog(this, "Cï¿½dula ya registrada");
            jtxtCedula.requestFocus();
            jtxtCedula.setForeground(colorError);
            return false;
        } else if (usuarioRegistrado()) {
            JOptionPane.showMessageDialog(this, "Usuario ya registrado");
            jtxtUsuario.requestFocus();
            jtxtUsuario.setForeground(colorError);
            return false;
        } else if (correoRegistrado()) {
            JOptionPane.showMessageDialog(this, "Correo ya registrado");
            jtxtCorreo.requestFocus();
            jtxtCorreo.setForeground(colorError);
            return false;
        }
        return true;
    }

    private boolean cedulaRegistrada() throws SQLException {
        PreparedStatement statement;
        String sql = "SELECT * FROM EMPLEADO WHERE CED_EMP = ? ";
        statement = conexion.prepareStatement(sql);
        statement.setString(1, jtxtCedula.getText());
        ResultSet rs = statement.executeQuery();
        return rs.next();
    }

    private boolean usuarioRegistrado() throws SQLException {
        PreparedStatement statement;
        String sql = "SELECT * FROM PERFIL WHERE NOM_PER = ?";
        statement = conexion.prepareStatement(sql);
        statement.setString(1, jtxtUsuario.getText());
        ResultSet rs = statement.executeQuery();
        return rs.next();
    }

    private boolean correoRegistrado() throws SQLException {
        PreparedStatement statement;
        String sql = "SELECT * FROM EMPLEADO WHERE COR_EMP = ?";
        statement = conexion.prepareStatement(sql);
        statement.setString(1, jtxtCorreo.getText());
        ResultSet rs = statement.executeQuery();
        return rs.next();
    }

    private void crearCuentaUsuario() throws SQLException {
        //Control control = new Control();
        conexion.setAutoCommit(false);
        String sqlEmpleado = "INSERT INTO EMPLEADO(CED_EMP, NOM_EMP, APE_EMP, FEC_NAC_EMP, COR_EMP) VALUES(?, ?, ?, ?, ?)";
        String sqlUsuario = "INSERT INTO PERFIL(NOM_PER, CLA_PER, CED_EMP_PER) VALUES(?, ?)";
        PreparedStatement psUsuario = null, psEmpleado = null;
        try {
            psEmpleado = conexion.prepareStatement(sqlEmpleado);
            psEmpleado.setString(1, jtxtCedula.getText());
            psEmpleado.setString(2, jtxtNombre.getText());
            psEmpleado.setString(3, jtxtApellido.getText());
            psEmpleado.setString(4, cambiarFormatoFecha());
            psEmpleado.setString(5, jtxtCorreo.getText());
            psEmpleado.executeUpdate();

            psUsuario = conexion.prepareStatement(sqlUsuario);
            psUsuario.setString(1, jtxtUsuario.getText());
            psUsuario.setString(2, Password.encryptPassword(new String(jpswClave.getPassword())));
            psUsuario.setString(3, jtxtCedula.getText());

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
            JOptionPane.showMessageDialog(this, "Cuenta creada");
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

        jpnlLateral = new javax.swing.JPanel();
        jlblLateral = new javax.swing.JLabel();
        jpnlCabecera = new javax.swing.JPanel();
        jpnlSalir = new javax.swing.JPanel();
        jtxtSalir = new javax.swing.JLabel();
        jlblTitulo = new javax.swing.JLabel();
        jtxtNombre = new javax.swing.JTextField();
        jtxtApellido = new javax.swing.JTextField();
        jtxtCedula = new javax.swing.JTextField();
        jtxtCorreo = new javax.swing.JTextField();
        jtxtUsuario = new javax.swing.JTextField();
        jtxtFechaNacimiento = new javax.swing.JTextField();
        jpswClave = new javax.swing.JPasswordField();
        jpnlVerOcultarClave = new javax.swing.JPanel();
        jlblVerOcultarClave = new javax.swing.JLabel();
        jpswClaveC = new javax.swing.JPasswordField();
        jpnlCrear = new javax.swing.JPanel();
        jtxtCrear = new javax.swing.JLabel();
        jlblBoton = new javax.swing.JLabel();
        jlblNotaUsuario = new javax.swing.JLabel();
        jlblNotaFecha = new javax.swing.JLabel();
        jlblNotaClave = new javax.swing.JLabel();
        jlblNotaConfirmacion = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(415, 390));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpnlLateral.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jpnlLateralLayout = new javax.swing.GroupLayout(jpnlLateral);
        jpnlLateral.setLayout(jpnlLateralLayout);
        jpnlLateralLayout.setHorizontalGroup(
            jpnlLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlLateralLayout.createSequentialGroup()
                .addComponent(jlblLateral, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jpnlLateralLayout.setVerticalGroup(
            jpnlLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlLateralLayout.createSequentialGroup()
                .addComponent(jlblLateral, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        add(jpnlLateral, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 450));

        jpnlCabecera.setBackground(new java.awt.Color(255, 255, 255));
        jpnlCabecera.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jpnlCabeceraMouseDragged(evt);
            }
        });
        jpnlCabecera.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jpnlCabeceraMousePressed(evt);
            }
        });

        jpnlSalir.setBackground(new java.awt.Color(255, 255, 255));

        jtxtSalir.setFont(new java.awt.Font("Rubik Light", 0, 14)); // NOI18N
        jtxtSalir.setForeground(new java.awt.Color(54, 58, 64));
        jtxtSalir.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtxtSalir.setText("X");
        jtxtSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtxtSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtxtSalirMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jtxtSalirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jtxtSalirMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jpnlSalirLayout = new javax.swing.GroupLayout(jpnlSalir);
        jpnlSalir.setLayout(jpnlSalirLayout);
        jpnlSalirLayout.setHorizontalGroup(
            jpnlSalirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlSalirLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jtxtSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jpnlSalirLayout.setVerticalGroup(
            jpnlSalirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlSalirLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jtxtSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jpnlCabeceraLayout = new javax.swing.GroupLayout(jpnlCabecera);
        jpnlCabecera.setLayout(jpnlCabeceraLayout);
        jpnlCabeceraLayout.setHorizontalGroup(
            jpnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlCabeceraLayout.createSequentialGroup()
                .addGap(0, 570, Short.MAX_VALUE)
                .addComponent(jpnlSalir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jpnlCabeceraLayout.setVerticalGroup(
            jpnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlCabeceraLayout.createSequentialGroup()
                .addComponent(jpnlSalir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jpnlCabecera, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 30));

        jlblTitulo.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jlblTitulo.setForeground(new java.awt.Color(28, 59, 112));
        jlblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblTitulo.setText("CREAR CUENTA DE ACCESO");
        add(jlblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 550, 30));

        jtxtNombre.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jtxtNombre.setForeground(new java.awt.Color(204, 204, 204));
        jtxtNombre.setText("NOMBRE");
        jtxtNombre.setBorder(null);
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
        add(jtxtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 220, 40));

        jtxtApellido.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jtxtApellido.setForeground(new java.awt.Color(204, 204, 204));
        jtxtApellido.setText("APELLIDO");
        jtxtApellido.setBorder(null);
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
        add(jtxtApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, 220, 40));

        jtxtCedula.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jtxtCedula.setForeground(new java.awt.Color(204, 204, 204));
        jtxtCedula.setText("CÉDULA");
        jtxtCedula.setBorder(null);
        jtxtCedula.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtxtCedulaMousePressed(evt);
            }
        });
        jtxtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtCedulaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtCedulaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtCedulaKeyTyped(evt);
            }
        });
        add(jtxtCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 145, 220, 40));

        jtxtCorreo.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jtxtCorreo.setForeground(new java.awt.Color(204, 204, 204));
        jtxtCorreo.setText("CORREO ELECTRÓNICO");
        jtxtCorreo.setBorder(null);
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
        add(jtxtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 145, 220, 40));

        jtxtUsuario.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jtxtUsuario.setForeground(new java.awt.Color(204, 204, 204));
        jtxtUsuario.setText("NOMBRE DE USUARIO");
        jtxtUsuario.setBorder(null);
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
        add(jtxtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 200, 220, 40));

        jtxtFechaNacimiento.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jtxtFechaNacimiento.setForeground(new java.awt.Color(204, 204, 204));
        jtxtFechaNacimiento.setText("FECHA DE NACIMIENTO");
        jtxtFechaNacimiento.setBorder(null);
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
        add(jtxtFechaNacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 200, 220, 40));

        jpswClave.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jpswClave.setForeground(new java.awt.Color(204, 204, 204));
        jpswClave.setText("CONTRASEÑA");
        jpswClave.setBorder(null);
        jpswClave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jpswClaveMousePressed(evt);
            }
        });
        jpswClave.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jpswClaveKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jpswClaveKeyReleased(evt);
            }
        });
        add(jpswClave, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 275, 180, 40));

        jpnlVerOcultarClave.setBackground(new java.awt.Color(255, 255, 255));

        jlblVerOcultarClave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlblVerOcultarClaveMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jpnlVerOcultarClaveLayout = new javax.swing.GroupLayout(jpnlVerOcultarClave);
        jpnlVerOcultarClave.setLayout(jpnlVerOcultarClaveLayout);
        jpnlVerOcultarClaveLayout.setHorizontalGroup(
            jpnlVerOcultarClaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlVerOcultarClaveLayout.createSequentialGroup()
                .addComponent(jlblVerOcultarClave, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jpnlVerOcultarClaveLayout.setVerticalGroup(
            jpnlVerOcultarClaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlVerOcultarClaveLayout.createSequentialGroup()
                .addComponent(jlblVerOcultarClave, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        add(jpnlVerOcultarClave, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 275, 40, 40));

        jpswClaveC.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jpswClaveC.setForeground(new java.awt.Color(204, 204, 204));
        jpswClaveC.setText("CONFIRMACIÓN");
        jpswClaveC.setBorder(null);
        jpswClaveC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jpswClaveCMousePressed(evt);
            }
        });
        jpswClaveC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jpswClaveCKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jpswClaveCKeyReleased(evt);
            }
        });
        add(jpswClaveC, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 275, 180, 40));

        jpnlCrear.setBackground(new java.awt.Color(255, 255, 255));
        jpnlCrear.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtxtCrear.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jtxtCrear.setForeground(new java.awt.Color(255, 255, 255));
        jtxtCrear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtxtCrear.setText("CREAR");
        jtxtCrear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtxtCrear.setEnabled(false);
        jtxtCrear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtxtCrearMouseClicked(evt);
            }
        });
        jpnlCrear.add(jtxtCrear, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1, 160, 40));

        jlblBoton.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jlblBoton.setForeground(new java.awt.Color(255, 255, 255));
        jlblBoton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblBoton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/botonAzul_160_40.png"))); // NOI18N
        jlblBoton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jpnlCrear.add(jlblBoton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        add(jpnlCrear, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 370, 160, 40));

        jlblNotaUsuario.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        jlblNotaUsuario.setForeground(new java.awt.Color(1, 154, 214));
        jlblNotaUsuario.setText("REQUISITOS USUARIO");
        jlblNotaUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlblNotaUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlblNotaUsuarioMouseClicked(evt);
            }
        });
        add(jlblNotaUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 245, 180, 15));

        jlblNotaFecha.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        jlblNotaFecha.setForeground(new java.awt.Color(54, 58, 64));
        jlblNotaFecha.setText("DD-MM-AAAA");
        add(jlblNotaFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 245, 180, 15));

        jlblNotaClave.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        jlblNotaClave.setForeground(new java.awt.Color(1, 154, 214));
        jlblNotaClave.setText("REQUISITOS CONTRASEÑA");
        jlblNotaClave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlblNotaClave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlblNotaClaveMouseClicked(evt);
            }
        });
        add(jlblNotaClave, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 320, 180, 15));

        jlblNotaConfirmacion.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        jlblNotaConfirmacion.setForeground(new java.awt.Color(255, 0, 51));
        add(jlblNotaConfirmacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 320, 180, 15));
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtSalirMouseClicked
        System.exit(0);
    }//GEN-LAST:event_jtxtSalirMouseClicked

    private void jtxtSalirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtSalirMouseEntered
        jpnlSalir.setBackground(new Color(0, 75, 133));
        jtxtSalir.setForeground(Color.white);
    }//GEN-LAST:event_jtxtSalirMouseEntered

    private void jtxtSalirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtSalirMouseExited
        jpnlSalir.setBackground(Color.white);
        jtxtSalir.setForeground(new Color(54, 58, 64));
    }//GEN-LAST:event_jtxtSalirMouseExited

    private void jpnlCabeceraMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlCabeceraMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_jpnlCabeceraMouseDragged

    private void jpnlCabeceraMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlCabeceraMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_jpnlCabeceraMousePressed

    private void jtxtNombreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtNombreMousePressed
        textosPorDefecto(jtxtNombre);
    }//GEN-LAST:event_jtxtNombreMousePressed

    private void jtxtNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNombreKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jtxtNombreKeyPressed

    private void jtxtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNombreKeyReleased
        validarLongitudMinima(jtxtNombre, 2);
        definirEstadoBoton();
    }//GEN-LAST:event_jtxtNombreKeyReleased

    private void jtxtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNombreKeyTyped
        //Validacion.convertirMayuscula(evt);
        Validacion.validarNLetras(evt, jtxtNombre, 25);
    }//GEN-LAST:event_jtxtNombreKeyTyped

    private void jtxtApellidoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtApellidoMousePressed
        textosPorDefecto(jtxtApellido);
    }//GEN-LAST:event_jtxtApellidoMousePressed

    private void jtxtApellidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtApellidoKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jtxtApellidoKeyPressed

    private void jtxtApellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtApellidoKeyReleased
        validarLongitudMinima(jtxtApellido, 2);
        definirEstadoBoton();
    }//GEN-LAST:event_jtxtApellidoKeyReleased

    private void jtxtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtApellidoKeyTyped
        Validacion.convertirMayuscula(evt);
        Validacion.validarNLetras(evt, jtxtApellido, 25);
    }//GEN-LAST:event_jtxtApellidoKeyTyped

    private void jtxtCedulaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtCedulaMousePressed
        textosPorDefecto(jtxtCedula);
    }//GEN-LAST:event_jtxtCedulaMousePressed

    private void jtxtCedulaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCedulaKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jtxtCedulaKeyPressed

    private void jtxtCedulaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCedulaKeyReleased
        validarCedula();
        definirEstadoBoton();
    }//GEN-LAST:event_jtxtCedulaKeyReleased

    private void jtxtCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCedulaKeyTyped
        Validacion.validarNNumeros(evt, jtxtCedula, 10);
    }//GEN-LAST:event_jtxtCedulaKeyTyped

    private void jtxtCorreoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtCorreoMousePressed
        textosPorDefecto(jtxtCorreo);
    }//GEN-LAST:event_jtxtCorreoMousePressed

    private void jtxtCorreoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCorreoKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jtxtCorreoKeyPressed

    private void jtxtCorreoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCorreoKeyReleased
        validarEmail();
        definirEstadoBoton();
    }//GEN-LAST:event_jtxtCorreoKeyReleased

    private void jtxtCorreoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCorreoKeyTyped
        Validacion.validarSinEspacios(evt);
        Validacion.validarNCaracteres(evt, jtxtCorreo, 254);
    }//GEN-LAST:event_jtxtCorreoKeyTyped

    private void jtxtUsuarioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtUsuarioMousePressed
        textosPorDefecto(jtxtUsuario);
    }//GEN-LAST:event_jtxtUsuarioMousePressed

    private void jtxtUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtUsuarioKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jtxtUsuarioKeyPressed

    private void jtxtUsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtUsuarioKeyReleased
        validarUsuario();
        definirEstadoBoton();
    }//GEN-LAST:event_jtxtUsuarioKeyReleased

    private void jtxtUsuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtUsuarioKeyTyped
        Validacion.validarSinEspacios(evt);
        Validacion.validarNCaracteres(evt, jtxtUsuario, 30);
    }//GEN-LAST:event_jtxtUsuarioKeyTyped

    private void jtxtFechaNacimientoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtFechaNacimientoMousePressed
        textosPorDefecto(jtxtFechaNacimiento);
    }//GEN-LAST:event_jtxtFechaNacimientoMousePressed

    private void jtxtFechaNacimientoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtFechaNacimientoKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jtxtFechaNacimientoKeyPressed

    private void jtxtFechaNacimientoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtFechaNacimientoKeyReleased
        validarFecha();
        definirEstadoBoton();
    }//GEN-LAST:event_jtxtFechaNacimientoKeyReleased

    private void jtxtFechaNacimientoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtFechaNacimientoKeyTyped
        Validacion.validarSinEspacios(evt);
        Validacion.validarNCaracteres(evt, jtxtFechaNacimiento, 10);
    }//GEN-LAST:event_jtxtFechaNacimientoKeyTyped

    private void jpswClaveMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpswClaveMousePressed
        textosPorDefecto(jpswClave);
    }//GEN-LAST:event_jpswClaveMousePressed

    private void jpswClaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jpswClaveKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jpswClaveKeyPressed

    private void jpswClaveKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jpswClaveKeyReleased
        validarClave();
        validarCoincidencia();
        definirEstadoBoton();
    }//GEN-LAST:event_jpswClaveKeyReleased

    private void jlblVerOcultarClaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlblVerOcultarClaveMouseClicked
        definirIconoOcultarClave();
    }//GEN-LAST:event_jlblVerOcultarClaveMouseClicked

    private void jpswClaveCMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpswClaveCMousePressed
        textosPorDefecto(jpswClaveC);
    }//GEN-LAST:event_jpswClaveCMousePressed

    private void jpswClaveCKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jpswClaveCKeyPressed
        Validacion.bloquearCopyPaste(evt);
    }//GEN-LAST:event_jpswClaveCKeyPressed

    private void jpswClaveCKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jpswClaveCKeyReleased
        validarCoincidencia();
        definirEstadoBoton();
    }//GEN-LAST:event_jpswClaveCKeyReleased

    private void jtxtCrearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtCrearMouseClicked
        if (jtxtCrear.isEnabled()) {
            try {
                crearCuentaUsuario();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex);
            }
        }
    }//GEN-LAST:event_jtxtCrearMouseClicked

    private void jlblNotaUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlblNotaUsuarioMouseClicked
        JOptionPane.showMessageDialog(this, " Mínimo 6 caracteres \n Caracteres especiales: \n - Punto (.) intermedio \n - Guión bajo (_)", "Usuario", 1);
    }//GEN-LAST:event_jlblNotaUsuarioMouseClicked

    private void jlblNotaClaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlblNotaClaveMouseClicked
        JOptionPane.showMessageDialog(this, " Mínimo 6 caracteres \n Caracteres especiales opcionales \n Al menos una mayúscula \n Al menos una minúscula \n Al menos un número", "Usuario", 1);
    }//GEN-LAST:event_jlblNotaClaveMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jlblBoton;
    private javax.swing.JLabel jlblLateral;
    private javax.swing.JLabel jlblNotaClave;
    private javax.swing.JLabel jlblNotaConfirmacion;
    private javax.swing.JLabel jlblNotaFecha;
    private javax.swing.JLabel jlblNotaUsuario;
    private javax.swing.JLabel jlblTitulo;
    private javax.swing.JLabel jlblVerOcultarClave;
    private javax.swing.JPanel jpnlCabecera;
    private javax.swing.JPanel jpnlCrear;
    private javax.swing.JPanel jpnlLateral;
    private javax.swing.JPanel jpnlSalir;
    private javax.swing.JPanel jpnlVerOcultarClave;
    private javax.swing.JPasswordField jpswClave;
    private javax.swing.JPasswordField jpswClaveC;
    private javax.swing.JTextField jtxtApellido;
    private javax.swing.JTextField jtxtCedula;
    private javax.swing.JTextField jtxtCorreo;
    private javax.swing.JLabel jtxtCrear;
    private javax.swing.JTextField jtxtFechaNacimiento;
    private javax.swing.JTextField jtxtNombre;
    private javax.swing.JLabel jtxtSalir;
    private javax.swing.JTextField jtxtUsuario;
    // End of variables declaration//GEN-END:variables
}
