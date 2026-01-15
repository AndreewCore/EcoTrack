/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import ecotrack.Zona;
import ecotrack.Residuo;
import ecotrack.TipoResiduo;
import ecotrack.ListaResiduos;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dominic Izurrieta & Paúl Rodríguez
 */

public class ResiduosController implements Initializable{

    private Stage stage;
    private Scene scene;
    private Parent root;    
    
    @FXML
    private ImageView residuosSceneView;
    
    @FXML
    private Button returnButton;
    
    @FXML
    private Label avisoLabel;

    @FXML
    private TextField nombreField;
    @FXML
    private ChoiceBox<TipoResiduo> tipoChoiceBox;
    @FXML
    private TextField pesoField;
    @FXML
    private ChoiceBox<String> ordenamientoChoiceBox;
    @FXML
    private ChoiceBox<String> filtradoChoiceBox;
    @FXML
    private ChoiceBox<String> zonaChoiceBox;
    
    private String[] opcionesFiltrado = {};
    private String[] opcionesOrdenado = {};
    private String[] zonas = {"Fortin / Monte Sinai", "Vergeles", "Sauces / Samanes", "Bastion", "Via a la Costa 1", "Via a la Costa 2", "Urdesa / Kennedy", "San Felipe / Mapasingue", "Samborondon", "Prosperina / Ceibos", "Portete", "Parque Centenario", "Isla Trinitaria", "Isla Mocoli", "Guasmo", "FAE", "Entre Rios / La Puntilla", "Centro"};
    private TipoResiduo[] tiposResiduo = TipoResiduo.values();
    
    private String nombreResiduo;
    private TipoResiduo tipoResiduo;
    private int valorPeso;
    private Zona zonaResiduo;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        tipoChoiceBox.getItems().addAll(tiposResiduo);
        tipoChoiceBox.setOnAction(this::establecerTipo);
        
        zonaChoiceBox.getItems().addAll(zonas);
        zonaChoiceBox.setOnAction(this::establecerZona);
        
        ordenamientoChoiceBox.getItems().addAll(opcionesOrdenado);
        ordenamientoChoiceBox.setOnAction(this::elegirOrdenamiento);
        
        filtradoChoiceBox.getItems().addAll(opcionesFiltrado);
        filtradoChoiceBox.setOnAction(this::elegirFiltrado);
    }
    
    public void establecerZona(ActionEvent event){
        String zona = zonaChoiceBox.getValue();
    }
    
    public void establecerTipo(ActionEvent event){
        tipoResiduo = tipoChoiceBox.getValue();
    }
    
    public void elegirOrdenamiento(ActionEvent event){
        String ordenamiento = ordenamientoChoiceBox.getValue();
    }
    
    public void elegirFiltrado(ActionEvent event){
        String filtrado = filtradoChoiceBox.getValue();
    }
    
    public void agregarResiduo(ActionEvent event){
        // Validar nombre
        if (nombreField.getText() == null || nombreField.getText().isEmpty()){
            avisoLabel.setText("Por favor, escribir un Nombre para el Residuo");
            avisoLabel.setTextFill(Color.RED);
            return;
        }

        // Validar tipo
        if (tipoChoiceBox.getValue() == null){
            avisoLabel.setText("Por favor, elegir un Tipo de Residuo");
            avisoLabel.setTextFill(Color.RED);
            return;
        }

        // Validar zona
        if (zonaChoiceBox.getValue() == null){
            avisoLabel.setText("Por favor, elegir una Zona");
            avisoLabel.setTextFill(Color.RED);
            return;
        }

        try{
            valorPeso = Integer.parseInt(pesoField.getText());

            if (valorPeso <= 0) {
                avisoLabel.setText("El peso debe ser mayor a 0");
                avisoLabel.setTextFill(Color.RED);
                return;
            }

            nombreResiduo = nombreField.getText();
            tipoResiduo = tipoChoiceBox.getValue();
            

            Residuo r = new Residuo(nombreResiduo, tipoResiduo, valorPeso);
            ListaResiduos.getListaResiduosGlobal().addLast(r);

            avisoLabel.setText("Se agregó el Residuo Exitosamente");
            avisoLabel.setTextFill(Color.GREEN);

            // Limpiar campos después de agregar exitosamente
            limpiarCampos();

        }
        catch (NumberFormatException e){
            avisoLabel.setText("Por favor, solo ingrese números en Peso");
            avisoLabel.setTextFill(Color.RED);
        }
        catch (Exception e){
            avisoLabel.setText("Error: " + e.getMessage());
            avisoLabel.setTextFill(Color.RED);
        }
    }

    // Método auxiliar para limpiar campos
    private void limpiarCampos() {
        nombreField.clear();
        pesoField.clear();
        tipoChoiceBox.setValue(null);
        zonaChoiceBox.setValue(null);
    }
    
    @FXML
    private void switchToSceneMain(ActionEvent event) throws IOException{
            Parent root = FXMLLoader.load(getClass().getResource("/vistas/ecotrack.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
    }    
    
}
