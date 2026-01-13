/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

/**
 *
 * @author Usuario
 */
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class EcotrackController {

    private Stage stage;
    private Scene scene;
    private Parent root;    
    
    @FXML
    private ImageView mainSceneView;
    
    @FXML
    private Button residuosButton;
    @FXML
    private Button zonasButton;
    @FXML
    private Button centroReciclajeButton;
    @FXML
    private Button estadisticasButton;
    
    @FXML
    private void switchToSceneResiduos(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/vistas/ResiduosMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void switchToSceneZonas(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/vistas/RutasMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void switchToSceneCentroReciclaje(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/vistas/CentroReciclajeMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void switchToSceneEstadisticas(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/vistas/EstadisticasMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}