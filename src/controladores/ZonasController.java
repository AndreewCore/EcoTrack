/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Paúl Rodríguez
 */
public class ZonasController{

    private Stage stage;
    private Scene scene;
    private Parent root;    
    
    @FXML
    private ImageView zonasSceneView;
    @FXML
    private ImageView mapaView;
    
    @FXML
    private Button centroButton;
    @FXML
    private Button entreRiosPuntillaButton;
    @FXML
    private Button FAEButton;
    @FXML
    private Button guasmoButton;
    @FXML
    private Button islaMocoliButton;
    @FXML
    private Button islaTrinitariaButton;
    @FXML
    private Button parqueCenterarioButton;
    @FXML
    private Button porteteButton;
    @FXML
    private Button prosperinaCeibosButton;
    @FXML
    private Button samborondonButton;
    @FXML
    private Button sanFelipeMapasingueButton;
    @FXML
    private Button urdesaKeneddyButton;
    @FXML
    private Button viaCosta1Button;
    @FXML
    private Button viaCosta2Button;
    @FXML
    private Button bastionButton;
    @FXML
    private Button saucesSamanesButton;
    @FXML
    private Button vergelesButton;
    @FXML
    private Button fortinMonteSinaiButton;
    
    @FXML
    private Button returnButton;
    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
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
        zonasSceneView.setPreserveRatio(true);
        
        // Hacer que la imagen sea suave al escalar
        zonasSceneView.setSmooth(true);
    }
    
    @FXML
    public void highlightFortinMonteSinai(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Zona Fortin-MonteSinai.png")));
    }
    @FXML
    public void highlightVergeles(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Zona Vergeles.png")));
    }
    @FXML
    public void highlightSaucesSamanes(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Zona Sauces-Samanes.png")));
    }
    @FXML
    public void highlightBastion(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Zona Bastion.png")));
    }
    @FXML
    public void highlightViaCosta2(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Via a la costa 2.png")));
    }
    @FXML
    public void highlightViaCosta1(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Via a la costa 1.png")));
    }
    @FXML
    public void highlightUrdesaKennedy(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Urdesa-Kennedy.png")));
    }
    @FXML
    public void highlightSanFelipeMapasingue(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco San Felipe-Mapasingue.png")));
    }
    @FXML
    public void highlightSamborondon(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Samborondon.png")));
    }
    @FXML
    public void highlightProsperinaCeibos(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Prosperina-Ceibos.png")));
    }
    @FXML
    public void highlightPortete(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Portete.png")));
    }
    @FXML
    public void highlightParqueCentenario(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Parque Centenario.png")));
    }
    @FXML
    public void highlightIslaTrinitaria(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Isla Trinitaria.png")));
    }
    @FXML
    public void highlightIslaMocoli(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Isla Mocoli.png")));
    }
    @FXML
    public void highlightGuasmo(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Guasmo.png")));
    }
    @FXML
    public void highlightFAE(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco FAE.png")));
    }
    @FXML
    public void highlightEntreRiosPuntilla(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Entre Rios-La Puntilla.png")));
    }
    @FXML
    public void highlightCentro(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Mapa Guayaquil Trazado Blanco Centro.png")));
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
