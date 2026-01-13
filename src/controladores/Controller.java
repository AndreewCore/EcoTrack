/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

/**
 *
 * @author Usuario
 */
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.ImageView;

public class Controller {

    @FXML
    private ImageView mainSceneView;
    
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