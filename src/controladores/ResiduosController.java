/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import ecotrack.Zona;
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
    private TextField tipoField;
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
    
    private String nombreResiduo;
    private String tipoResiduo;
    private int valorPeso;
    private Zona zonaResiduo;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        zonaChoiceBox.getItems().addAll(zonas);
        zonaChoiceBox.setOnAction(this::establecerZona);
        
        ordenamientoChoiceBox.getItems().addAll(opcionesOrdenado);
        ordenamientoChoiceBox.setOnAction(this::elegirOrdenamiento);
        
        filtradoChoiceBox.getItems().addAll(opcionesFiltrado);
        filtradoChoiceBox.setOnAction(this::elegirFiltrado);
    }
    
    public void establecerZona(ActionEvent event){
        String zona = zonaChoiceBox.getValue();
        System.out.println(zona);
    }
    
    public void elegirOrdenamiento(ActionEvent event){
        String ordenamiento = ordenamientoChoiceBox.getValue();
    }
    
    public void elegirFiltrado(ActionEvent event){
        String filtrado = filtradoChoiceBox.getValue();
    }
    
    public void agregarResiduo(ActionEvent event){
        try{
            valorPeso = Integer.parseInt(pesoField.getText());
        }
        catch (NumberFormatException e){
            avisoLabel.setText("Por favor, solo ingrese números en Peso");
            avisoLabel.setTextFill(Color.RED);
        }
        catch (Exception e){
            avisoLabel.setText(e.getMessage());
        }
        nombreResiduo = nombreField.getText();
        tipoResiduo = tipoField.getText();
        System.out.println(valorPeso);
        System.out.println(nombreResiduo);
        System.out.println(tipoResiduo);
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
