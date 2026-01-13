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
    public void initialize() {
        System.out.println("MainScene cargada correctamente");
        
        // Información del sistema (opcional)
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        System.out.println("Java Version: " + javaVersion);
        System.out.println("JavaFX Version: " + javafxVersion);
        
        // Configuración adicional del ImageView si es necesario
        configureImageView();
    }
    
    @FXML
    private void switchToSceneResiduos(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/vistas/ResiduosMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    private void configureImageView() {
        // Asegurar que la imagen mantenga su proporción
        mainSceneView.setPreserveRatio(true);
        
        // Hacer que la imagen sea suave al escalar
        mainSceneView.setSmooth(true);
    }
    
    public ImageView getMainSceneView() {
        return mainSceneView;
    }
}