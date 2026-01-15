/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Label zonaSeleccionada;
    
    @FXML
    private Button returnButton;
    
    @FXML
    public void highlightFortinMonteSinai(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Zona Fortin-MonteSinai.png")));
        zonaSeleccionada.setText("Fortin / Monte Sinai");
    }
    @FXML
    public void highlightVergeles(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Zona Vergeles.png")));
        zonaSeleccionada.setText("Vergeles");
    }
    @FXML
    public void highlightSaucesSamanes(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Zona Sauces-Samanes.png")));
        zonaSeleccionada.setText("Sauces / Samanes");
    }
    @FXML
    public void highlightBastion(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Zona Bastion.png")));
        zonaSeleccionada.setText("Bastion");
    }
    @FXML
    public void highlightViaCosta2(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Via a la costa 2.png")));
        zonaSeleccionada.setText("Via a la Costa 2");
    }
    @FXML
    public void highlightViaCosta1(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Via a la costa 1.png")));
        zonaSeleccionada.setText("Via a la Costa 1");
    }
    @FXML
    public void highlightUrdesaKennedy(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Urdesa-Kennedy.png")));
        zonaSeleccionada.setText("Urdesa / Kennedy");
    }
    @FXML
    public void highlightSanFelipeMapasingue(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco San Felipe-Mapasingue.png")));
        zonaSeleccionada.setText("San Felipe / Mapasingue");
    }
    @FXML
    public void highlightSamborondon(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Samborondon.png")));
        zonaSeleccionada.setText("Samborondon");
    }
    @FXML
    public void highlightProsperinaCeibos(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Prosperina-Ceibos.png")));
        zonaSeleccionada.setText("Prosperina / Ceibos");
    }
    @FXML
    public void highlightPortete(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Portete.png")));
        zonaSeleccionada.setText("Portete");
    }
    @FXML
    public void highlightParqueCentenario(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Parque Centenario.png")));
        zonaSeleccionada.setText("Parque Centenario");
    }
    @FXML
    public void highlightIslaTrinitaria(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Isla Trinitaria.png")));
        zonaSeleccionada.setText("Isla Trinitaria");
    }
    @FXML
    public void highlightIslaMocoli(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Isla Mocoli.png")));
        zonaSeleccionada.setText("Isla Mocoli");
    }
    @FXML
    public void highlightGuasmo(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Guasmo.png")));
        zonaSeleccionada.setText("Guasmo");
    }
    @FXML
    public void highlightFAE(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco FAE.png")));
        zonaSeleccionada.setText("FAE");
    }
    @FXML
    public void highlightEntreRiosPuntilla(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Entre Rios-La Puntilla.png")));
        zonaSeleccionada.setText("Entre Rios / La Puntilla");
    }
    @FXML
    public void highlightCentro(){
        mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Centro.png")));
        zonaSeleccionada.setText("Centro de Guayaquil");
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
