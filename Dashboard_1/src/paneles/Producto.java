/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paneles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author usuario
 */
public class Producto extends javax.swing.JFrame {

    private int idProductoSeleccionado;
    private String nombreSeleccionado;
    private String descripcionSeleccionada;
    private String categoriaSeleccionada;
    private float precioSeleccionado;

    /**
     * Creates new form Producto
     */
    public Producto(int idProductoSeleccionado, String nombreSeleccionado, String descripcionSeleccionada, String categoriaSeleccionada, float precioSeleccionado) throws IOException {
        initComponents();
        cargarCategorias();

        this.idProductoSeleccionado = idProductoSeleccionado;
        this.nombreSeleccionado = nombreSeleccionado;
        this.descripcionSeleccionada = descripcionSeleccionada;
        this.categoriaSeleccionada = categoriaSeleccionada;
        this.precioSeleccionado = precioSeleccionado;

        // Llenar los campos de texto con los datos del producto seleccionado
        jtxtNombreProducto.setText(nombreSeleccionado);
        jtxtDescripcion.setText(descripcionSeleccionada);
        jtxtPrecioProducto.setText(String.valueOf(precioSeleccionado));
        jcbCategorias.setSelectedItem(categoriaSeleccionada);
        if (idProductoSeleccionado == 0) {
            jtxtTitulo.setText("Nuevo Producto");
        } else {
            jtxtTitulo.setText("Editar Producto");
        }

        Document doc = jtxtPrecioProducto.getDocument();
        if (doc instanceof AbstractDocument) {
            AbstractDocument abstractDoc = (AbstractDocument) doc;
            abstractDoc.setDocumentFilter(new DecimalDocumentFilter());
        }
    }

    Producto() {

    }

    private String getApiResponse(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder responseBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

            return responseBuilder.toString();
        } else {
            throw new IOException("Error: " + responseCode);
        }
    }

    private void cargarCategorias() throws IOException {
        try {
            String apiUrl = "http://localhost:3000/categorias"; // Reemplaza con la URL del API que devuelve las categorías
            String apiResponse = getApiResponse(apiUrl);

            JSONArray jsonArray = new JSONArray(apiResponse);

            jcbCategorias.removeAllItems();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject categoria = jsonArray.getJSONObject(i);
                String categoriaPrincipal = categoria.getString("categoriaPrincipal");
                jcbCategorias.addItem(categoriaPrincipal);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void enviarDatos() throws IOException {
        String apiUrl = "http://localhost:3000/productos"; // Reemplaza con la URL de la API para enviar los datos por POST

        String nombre = jtxtNombreProducto.getText();
        String descripcion = jtxtDescripcion.getText();
        float precio = 0;
        String categoriaNombre = null;
        Integer categoriaId = null;

        if (!nombre.isEmpty() && !descripcion.isEmpty() && !jtxtPrecioProducto.getText().isEmpty() && jcbCategorias.getSelectedItem() != null) {

            precio = Float.parseFloat(jtxtPrecioProducto.getText());
            categoriaNombre = (String) jcbCategorias.getSelectedItem();
            categoriaId = obtenerIdCategoria(categoriaNombre);

            // Crear el objeto JSON con los datos
            JSONObject json = new JSONObject();
            json.put("nombre", nombre);
            json.put("descripcion", descripcion);
            json.put("precio", precio);
            json.put("id_categoria", categoriaId);

            // Convertir el objeto JSON a una cadena de texto
            String jsonData = json.toString();

            // Establecer la conexión HTTP
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Enviar los datos al servidor
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            // Obtener la respuesta del servidor
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // La solicitud fue exitosa
                // Realizar las acciones necesarias
                JOptionPane.showMessageDialog(this, "Datos enviados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                pnlCollection a = new pnlCollection();
                a.cargarDatosDesdeAPI();
                this.dispose();

            } else {
                // Ocurrió un error en la solicitud
                // Manejar el error adecuadamente
                JOptionPane.showMessageDialog(this, "Error al enviar los datos: " + responseCode, "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Cerrar la conexión
            connection.disconnect();

        } else {
            JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos requeridos.");
        }

    }

    private void actualizarDatos() throws IOException {
        String apiUrl = "http://localhost:3000/productos/" + idProductoSeleccionado; // Reemplaza con la URL de la API para actualizar el producto

        String nombre = jtxtNombreProducto.getText();
        String descripcion = jtxtDescripcion.getText();
        float precio = 0;
        String categoriaNombre = null;
        Integer categoriaId = null;

        if (!nombre.isEmpty() && !descripcion.isEmpty() && !jtxtPrecioProducto.getText().isEmpty() && jcbCategorias.getSelectedItem() != null) {

            precio = Float.parseFloat(jtxtPrecioProducto.getText());
            categoriaNombre = (String) jcbCategorias.getSelectedItem();
            categoriaId = obtenerIdCategoria(categoriaNombre);

            // Obtener los nuevos valores de los campos de texto
            String nuevoNombre = jtxtNombreProducto.getText();
            String nuevaDescripcion = jtxtDescripcion.getText();
            float nuevoPrecio = Float.parseFloat(jtxtPrecioProducto.getText());
            String nuevaCategoriaNombre = (String) jcbCategorias.getSelectedItem();
            Integer nuevaCategoriaId = obtenerIdCategoria(nuevaCategoriaNombre);

            // Crear el objeto JSON con los nuevos datos
            JSONObject json = new JSONObject();
            json.put("nombre", nuevoNombre);
            json.put("descripcion", nuevaDescripcion);
            json.put("precio", nuevoPrecio);
            json.put("id_categoria", nuevaCategoriaId);

            // Convertir el objeto JSON a una cadena de texto
            String jsonData = json.toString();

            // Establecer la conexión HTTP
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Enviar los datos al servidor
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            // Obtener la respuesta del servidor
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // La solicitud fue exitosa
                // Realizar las acciones necesarias
                JOptionPane.showMessageDialog(this, "Datos actualizados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                pnlCollection a = new pnlCollection();
                a.cargarDatosDesdeAPI();
                this.dispose();
            } else {
                // Ocurrió un error en la solicitud
                // Manejar el error adecuadamente
                JOptionPane.showMessageDialog(this, "Error al actualizar los datos: " + responseCode, "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Cerrar la conexión
            connection.disconnect();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos requeridos.");
        }
    }

    private Integer obtenerIdCategoria(String categoriaNombre) {
        try {
            String apiUrl = "http://localhost:3000/categorias"; // Reemplaza con la URL del API que devuelve las categorías
            String apiResponse = getApiResponse(apiUrl);

            JSONArray jsonArray = new JSONArray(apiResponse);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject categoria = jsonArray.getJSONObject(i);
                String categoriaPrincipal = categoria.getString("categoriaPrincipal");
                int idCategoria = categoria.getInt("id_categoria");

                if (categoriaPrincipal.equals(categoriaNombre)) {
                    return idCategoria;
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return null; // Manejar el caso en el que la categoría no exista o haya ocurrido un error
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtxtTitulo = new javax.swing.JLabel();
        jtxtNombreProducto = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jtxtDescripcion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtxtPrecioProducto = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jcbCategorias = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jbtnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jtxtTitulo.setFont(new java.awt.Font("Roboto", 1, 48)); // NOI18N
        jtxtTitulo.setForeground(new java.awt.Color(128, 128, 131));
        jtxtTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtxtTitulo.setText("Lista de Productos");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(128, 128, 131));
        jLabel1.setText("Nombre");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(128, 128, 131));
        jLabel2.setText("Descripcion");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(128, 128, 131));
        jLabel3.setText("Precio");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(128, 128, 131));
        jLabel4.setText("Categoria");

        jcbCategorias.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setBackground(new java.awt.Color(255, 0, 51));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jbtnGuardar.setBackground(new java.awt.Color(0, 204, 51));
        jbtnGuardar.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jbtnGuardar.setText("Guardar");
        jbtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jtxtTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(88, 88, 88))
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4)
                    .addComponent(jtxtPrecioProducto)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jtxtDescripcion)
                    .addComponent(jtxtNombreProducto)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcbCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtxtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jtxtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jcbCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(207, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jbtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGuardarActionPerformed
        if (idProductoSeleccionado == 0) {
            try {
                // TODO add your handling code here:
                enviarDatos();
            } catch (IOException ex) {
                Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                // TODO add your handling code here:
                actualizarDatos();
            } catch (IOException ex) {
                Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }//GEN-LAST:event_jbtnGuardarActionPerformed

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
            java.util.logging.Logger.getLogger(Producto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Producto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Producto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Producto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Producto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton jbtnGuardar;
    private javax.swing.JComboBox<String> jcbCategorias;
    private javax.swing.JTextField jtxtDescripcion;
    private javax.swing.JTextField jtxtNombreProducto;
    private javax.swing.JTextField jtxtPrecioProducto;
    private javax.swing.JLabel jtxtTitulo;
    // End of variables declaration//GEN-END:variables
}
