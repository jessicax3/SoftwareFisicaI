/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Codigo;


import javafx.scene.control.TextField;

/**
 *
 * @author Jess
 */

public class Validador {

    public double createDouble(double shortdecimal, int exp) {
        double crear = shortdecimal * Math.pow(10, exp);
        return crear;
    }


    public boolean validarNumero(TextField input) {
        try {
            String str = input.getText().trim();
            str = str.replaceAll(",", ".");
            Double.parseDouble(str);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public boolean validarPotencia(TextField txfase, TextField txfExp) {
        String decimal = "" + txfase.getText().trim();
        if (decimal == null || decimal.length() == 0) {
            return false;
        }
        int exponencial;
        try {
            exponencial = Integer.parseInt(txfExp.getText());
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        decimal = decimal.replaceAll(",", ".");
        if (decimal.contains(".")) {
            if (decimal.split("\\.").length == 1) {
                decimal += "0";
            } else if (decimal.split("\\.")[1].length() > 2) {
                exponencial -= (decimal.split("\\.")[1].length() - 2);
                decimal = decimal.split("\\.")[0] + "." + decimal.split("\\.")[1].substring(0, 2);
            }
        }
        txfase.setText(decimal);
        txfExp.setText("" + exponencial);
        return true;
    }
}
