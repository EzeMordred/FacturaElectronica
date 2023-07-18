/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Usuario
 */
public class Validacion {

    public static boolean validarUsuario(String usuario) {
        Pattern pattern = Pattern.compile("^(?=.{6,30}$)(?![.])(?!.*[.]{2})[a-zA-Z0-9._]+(?<![.])$");
        Matcher matcher = pattern.matcher(usuario);
        return matcher.matches();
    }
    
    public static boolean validarEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validarFechaDMA(String fecha) {
        if (fecha.length() < 10) {
            return false;
        }
        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            format.setLenient(false);
            format.parse(fecha);
        } catch (ParseException e) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9-]+$");
        Matcher matcher = pattern.matcher(fecha);
        return matcher.matches();
    }
    
    public static boolean validarClave(String clave) {
        Pattern pattern = Pattern.compile("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,30}$");
        Matcher matcher = pattern.matcher(clave);
        return matcher.matches();
    }
    
    public static boolean validarTelefono(String telefono) {
        if (telefono.isEmpty()) {
            return true;
        }
        if (telefono.length() < 10) {
            return false;
        }
        Pattern pattern = Pattern.compile("^09.*");
        Matcher matcher = pattern.matcher(telefono);
        return matcher.matches();
    }

    public static boolean validarCedula(String cedula) {
        if (cedula.length() < 10) {
            return false;
        }
        int checkDigit;
        int oddSum = 0;
        int pairsSum = 0;
        int provinceCode = Integer.parseInt(cedula.substring(0, 2));//Generamos el c�digo de provincia como entero.
        //Evaluamos que la c�dula tenga 10 d�gitos, el tercer d�gito entre 0 y 6, y un c�digo de provincia v�lido:
        if ((Integer.valueOf(cedula.charAt(2)) - 48) <= 6 && ((provinceCode >= 1 && provinceCode <= 24) || provinceCode == 30)) {
            int odd;
            for (int j = 0; j < 10; j += 2) {
                odd = (Integer.parseInt(cedula.charAt(j) + "")) * 2;//Transformamos las posiciones impares a enteros.
                if (odd > 9) {//Si la posici�n impar multiplicada por 2 es mayor a 9, le restamos 9.
                    odd -= 9;
                }
                oddSum += odd;//Suma iterativa de posiciones impares.
                if (j < 8) {//Se controla que el �timo d�gito no interfiera en la operaci�n.
                    pairsSum += (Integer.parseInt(cedula.charAt(j + 1) + ""));//Suma iterativa de posiciones pares.
                }
            }
            checkDigit = (oddSum + pairsSum) % 10;//Se calcula el d�gito verificador con el m�dulo 10.
            if (checkDigit != 0) {
                checkDigit = 10 - checkDigit;//Si el m�dulo es distinto de cero, se lo resta de 10.
            }
            if (checkDigit == (Integer.parseInt(cedula.charAt(9) + ""))) {//Se compara el �ltimo resultado con el �ltimo d�gito de la c�dula.
                return true;
            }
        }
        return false;
    }
    
    public static void convertirMayuscula(java.awt.event.KeyEvent evt) {
        char validar = evt.getKeyChar();
        if (Character.isLowerCase(validar)) {
            String strCadena = ("" + validar).toUpperCase();
            validar = strCadena.charAt(0);
            evt.setKeyChar(validar);
        }
    }
    
    public static void validarNNumeros(java.awt.event.KeyEvent evt, JTextField campo, int longitud) {
        char validar = evt.getKeyChar();
        if (campo.getSelectedText() != null && Character.isDigit(validar)) {
            return;
        }
        if (!Character.isDigit(validar) || campo.getText().length() >= longitud) {
            evt.consume();
        }
    }
    
    public static void validarNLetras(java.awt.event.KeyEvent evt, JTextField campo, int longitud) {
        char validar = evt.getKeyChar();
        if (campo.getSelectedText() != null && Character.isLetter(validar)) {
            return;
        }
        if (!Character.isLetter(validar) || campo.getText().length() >= longitud) {
            evt.consume();
        }
    }

    public static void validarNCaracteres(java.awt.event.KeyEvent evt, JComponent campo, int longitud) {
        if (campo instanceof JTextField) {
            if (((JTextField) campo).getSelectedText() != null) {
                return;
            }
        } else {
            if (((JTextArea) campo).getSelectedText() != null) {
                return;
            }
        }
        if (campo instanceof JTextField) {
            if (((JTextField) campo).getText().length() >= longitud) {
                evt.consume();
            }
        } else {
            if (((JTextArea) campo).getText().length() >= longitud) {
                evt.consume();
            }
        }
    }

    public static void validarSinEspacios(java.awt.event.KeyEvent evt) {
        char validar = evt.getKeyChar();
        if (Character.isSpaceChar(validar)) {
            evt.consume();
        }
    }

    public static void bloquearCopyPaste(java.awt.event.KeyEvent evt) {
        if (evt.isControlDown()) {
            evt.consume();
        }
    }
}
