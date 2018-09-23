/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Codigo.Validador;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

/**
 * FXML Controller class
 *
 * @author Jess
 */
public class VistaFormulasController implements Initializable{

    private double voltaje = 0;
    private double tiempo = 0;
    private double resistencia = 0;
    private double capacidad = 0;
    private final Validador validador = new Validador();
    private Series cargaData = null;
    private Series condensadorData = null;
    private Series resistenciaData = null;
    private Series corrienteData = null;
    ObservableList<String> contenido = FXCollections.observableArrayList("Carga","Voltaje Resistencia","Corriente","Voltaje Condensador" );
    private Series series;   
    
    @FXML
    private TextField baseCapa;
    @FXML
    private TextField baseVoltF;
    @FXML
    private TextField baseResis;
    @FXML
    private TextField baseTiempo;
    @FXML
    private TextField expCapa;
    @FXML
    private TextField expVolt;
    @FXML
    private TextField expResis;
    @FXML
    private LineChart<?, ?> lineChart;
    @FXML
    private ToggleButton tgButtonIngresar;
    @FXML
    private Button btnBorrar;
    @FXML
    private Button btnSalir;
    @FXML
    private ChoiceBox choiceBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
            choiceBox.setValue("Carga");
            choiceBox.setItems(contenido);
    }
    
    @FXML
    private void onEnterPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            actionCalcular();
        }
    }
    @FXML
    private void mouseWheel(ScrollEvent event) {
        if(tiempo > 3 && event.getDeltaY() > 0){
            int t = Integer.parseInt(baseTiempo.getText()) - 1;
            baseTiempo.setText("" + (t));
            actionCalcular();
        } else if (event.getDeltaY() < 0) {
            int t = Integer.parseInt(baseTiempo.getText()) + 1;
            baseTiempo.setText("" + (t));
            actionCalcular();
        
        }
    }
    
    private void validarExp() {
        if (expCapa.getText().length() == 0) {
            expCapa.setText("0");
        }
        if (expResis.getText().length() == 0) {
            expResis.setText("0");
        }
        if (expVolt.getText().length() == 0) {
            expVolt.setText("0");
        }
    }
    
    
    @FXML
    private void metodoBotonBorrar(ActionEvent event) {
        baseCapa.setText("");
        baseVoltF.setText("");
        baseResis.setText("");
        baseTiempo.setText("");
        expCapa.setText("");
        expVolt.setText("");
        expResis.setText("");
        lineChart.getData().clear();
    }

    @FXML
    private void actionCalcular() {
        validarExp();
        if(validador.validarPotencia(baseVoltF, expVolt) && validador.validarPotencia(baseCapa, expCapa) && validador.validarPotencia(baseResis, expResis) && validador.validarNumero(baseTiempo)){
            cargaData = new Series();
            resistenciaData = new Series();
            corrienteData = new Series();
            condensadorData = new Series();
            
            capacidad = validador.createDouble(Double.parseDouble(baseCapa.getText()), Integer.parseInt(expCapa.getText()));
            resistencia = validador.createDouble(Double.parseDouble(baseResis.getText()), Integer.parseInt(expResis.getText()));
            voltaje = validador.createDouble(Double.parseDouble(baseVoltF.getText()), Integer.parseInt(expVolt.getText()));
            tiempo = Integer.parseInt(baseTiempo.getText());
            
            lineChart.getData().clear();
            lineChart.setLegendVisible(true);
            lineChart.setCreateSymbols(false);
            
            if(choiceBox.getValue()=="Carga"){
                carga(true);
            }
            if(choiceBox.getValue()=="Voltaje Resistencia"){
                resistenciaVolt(true);
            }
            if(choiceBox.getValue()=="Corriente"){
                corriente(true);
            }
            if(choiceBox.getValue()=="Voltaje Condensador"){
                condensadorVolt(true);
            }
                 
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error garrafal");
            alert.setHeaderText("Error al ingresar los datos");
            String s ="Intentelo de nuevo, por favor";
            alert.setContentText(s);
            alert.show();
        }
    }

    private void carga(boolean show) {
    if (cargaData != null) {
            if (show) {
                cargaData = carga(voltaje, tiempo, resistencia, capacidad);
                lineChart.getData().add(cargaData);
            } else {
                lineChart.getData().remove(cargaData);
            }
        }
    }
    private void condensadorVolt(boolean show)  {
        if (condensadorData != null) {
            if (show) {
                condensadorData = condensadorVoltaje(voltaje, tiempo, resistencia, capacidad);
                lineChart.getData().add(condensadorData);
            } else {
                lineChart.getData().remove(condensadorData);
            }
        }
    }
    private void resistenciaVolt(boolean show)  {
        if (resistenciaData != null) {
            if (show) {
                resistenciaData = resistanciaVoltaje(voltaje, tiempo, resistencia, capacidad);
                lineChart.getData().add(resistenciaData);
            } else {
                lineChart.getData().remove(resistenciaData);
            }
        }
    }
    private void corriente(boolean show)  {
        if (corrienteData != null) {
            if (show) {
                corrienteData= corriente(voltaje, tiempo, resistencia, capacidad);
                lineChart.getData().add(corrienteData);
            } else {
                lineChart.getData().remove(corrienteData);
            }
        }
    }

    @FXML
    private void btnSalir(ActionEvent event) {
        System.exit(0);
    }
    

    
    public Series carga(double voltaje, double tiempo, double resistencia, double capacidad) {
    series = new Series();
    series.setName("Carga");

        for (int i = 0; i < tiempo; i++) {
            double result = (capacidad * voltaje) * (1 - Math.exp((-i) / (resistencia * capacidad)));
            series.getData().add(new XYChart.Data("" + i, result));
        }

        return series;
    }

    public Series condensadorVoltaje(double voltaje, double tiempo, double resistencia, double capacidad) {
        series = new Series();
        series.setName("Voltaje del capacitor");

        for (int i = 0; i < tiempo; i++) {
            double result = voltaje * (1 - Math.exp((-i) / (resistencia * capacidad)));
            series.getData().add(new XYChart.Data("" + i, result));
        }

        return series;
    }

    public Series resistanciaVoltaje(double voltaje, double tiempo, double resistencia, double capacidad) {
        series = new Series();
        series.setName("Voltaje en Resistencia");

        for (int i = 0; i < tiempo; i++) {
            double result = voltaje * (Math.exp((-i) / (resistencia * capacidad)));
            series.getData().add(new XYChart.Data("" + i, result));
        }

        return series;
    }

    public Series corriente(double voltaje, double tiempo, double resistencia, double capacidad) {
        series = new Series();
        series.setName("Corriente");

        for (int i = 0; i < tiempo; i++) {
            double result = (voltaje / resistencia) * (Math.exp((-i) / (resistencia * capacidad)));
            series.getData().add(new XYChart.Data("" + i, result));
        }

        return series;
    }



}
