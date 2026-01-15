/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import ecotrack.ListaZonas;
import ecotrack.Zona;
import ecotrack.ZonaGuayaquil;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    private Label pesoRecolectadoLabel;
    @FXML
    private Label pesoPendienteLabel;
    @FXML
    private Label utilidadLabel;
    
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
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.FORTIN_MONTE_SINAI);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Zona Fortin-MonteSinai.png")));
            zonaSeleccionada.setText("Fortin / Monte Sinai");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Zona Fortin-MonteSinai.png")));
            zonaSeleccionada.setText("Fortin / Monte Sinai");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Zona Fortin-MonteSinai.png")));
            zonaSeleccionada.setText("Fortin / Monte Sinai");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }
    
    @FXML
    public void highlightVergeles(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.VERGELES);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Zona Vergeles.png")));
            zonaSeleccionada.setText("Vergeles");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Zona Vergeles.png")));
            zonaSeleccionada.setText("Vergeles");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Zona Vergeles.png")));
            zonaSeleccionada.setText("Vergeles");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightSaucesSamanes(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.SAUCES_SAMANES);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Zona Sauces-Samanes.png")));
            zonaSeleccionada.setText("Sauces / Samanes");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Zona Sauces-Samanes.png")));
            zonaSeleccionada.setText("Sauces / Samanes");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Zona Sauces-Samanes.png")));
            zonaSeleccionada.setText("Sauces / Samanes");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightBastion(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.BASTION);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Zona Bastion.png")));
            zonaSeleccionada.setText("Bastion");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Zona Bastion.png")));
            zonaSeleccionada.setText("Bastion");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Zona Bastion.png")));
            zonaSeleccionada.setText("Bastion");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightViaCosta1(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.VIA_COSTA_1);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Via a la costa 1.png")));
            zonaSeleccionada.setText("Via a la Costa 1");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Via a la costa 1.png")));
            zonaSeleccionada.setText("Via a la Costa 1");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Via a la costa 1.png")));
            zonaSeleccionada.setText("Via a la Costa 1");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightViaCosta2(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.VIA_COSTA_2);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Via a la costa 2.png")));
            zonaSeleccionada.setText("Via a la Costa 2");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Via a la costa 2.png")));
            zonaSeleccionada.setText("Via a la Costa 2");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Via a la costa 2.png")));
            zonaSeleccionada.setText("Via a la Costa 2");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightUrdesaKennedy(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.URDESA_KENNEDY);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Urdesa-Kennedy.png")));
            zonaSeleccionada.setText("Urdesa / Kennedy");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Urdesa-Kennedy.png")));
            zonaSeleccionada.setText("Urdesa / Kennedy");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Urdesa-Kennedy.png")));
            zonaSeleccionada.setText("Urdesa / Kennedy");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightSanFelipeMapasingue(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.SAN_FELIPE_MAPASINGUE);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco San Felipe-Mapasingue.png")));
            zonaSeleccionada.setText("San Felipe / Mapasingue");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco San Felipe-Mapasingue.png")));
            zonaSeleccionada.setText("San Felipe / Mapasingue");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco San Felipe-Mapasingue.png")));
            zonaSeleccionada.setText("San Felipe / Mapasingue");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightSamborondon(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.SAMBORONDON);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Samborondon.png")));
            zonaSeleccionada.setText("Samborondon");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Samborondon.png")));
            zonaSeleccionada.setText("Samborondon");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Samborondon.png")));
            zonaSeleccionada.setText("Samborondon");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightProsperinaCeibos(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.PROSPERINA_CEIBOS);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Prosperina-Ceibos.png")));
            zonaSeleccionada.setText("Prosperina / Ceibos");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Prosperina-Ceibos.png")));
            zonaSeleccionada.setText("Prosperina / Ceibos");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Prosperina-Ceibos.png")));
            zonaSeleccionada.setText("Prosperina / Ceibos");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightPortete(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.PORTETE);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Portete.png")));
            zonaSeleccionada.setText("Portete");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Portete.png")));
            zonaSeleccionada.setText("Portete");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Portete.png")));
            zonaSeleccionada.setText("Portete");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightParqueCentenario(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.PARQUE_CENTENARIO);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Parque Centenario.png")));
            zonaSeleccionada.setText("Parque Centenario");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Parque Centenario.png")));
            zonaSeleccionada.setText("Parque Centenario");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Parque Centenario.png")));
            zonaSeleccionada.setText("Parque Centenario");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightIslaTrinitaria(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.ISLA_TRINITARIA);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Isla Trinitaria.png")));
            zonaSeleccionada.setText("Isla Trinitaria");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Isla Trinitaria.png")));
            zonaSeleccionada.setText("Isla Trinitaria");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Isla Trinitaria.png")));
            zonaSeleccionada.setText("Isla Trinitaria");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightIslaMocoli(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.ISLA_MOCOLI);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Isla Mocoli.png")));
            zonaSeleccionada.setText("Isla Mocoli");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Isla Mocoli.png")));
            zonaSeleccionada.setText("Isla Mocoli");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Isla Mocoli.png")));
            zonaSeleccionada.setText("Isla Mocoli");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightGuasmo(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.GUASMO);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Guasmo.png")));
            zonaSeleccionada.setText("Guasmo");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Guasmo.png")));
            zonaSeleccionada.setText("Guasmo");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Guasmo.png")));
            zonaSeleccionada.setText("Guasmo");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightFAE(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.FAE);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco FAE.png")));
            zonaSeleccionada.setText("FAE");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco FAE.png")));
            zonaSeleccionada.setText("FAE");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco FAE.png")));
            zonaSeleccionada.setText("FAE");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightEntreRiosPuntilla(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.ENTRE_RIOS_PUNTILLA);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Entre Rios-La Puntilla.png")));
            zonaSeleccionada.setText("Entre Rios / La Puntilla");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Entre Rios-La Puntilla.png")));
            zonaSeleccionada.setText("Entre Rios / La Puntilla");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Entre Rios-La Puntilla.png")));
            zonaSeleccionada.setText("Entre Rios / La Puntilla");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
    }

    @FXML
    public void highlightCentro(){
        Zona z = ListaZonas.getInstance().buscarPorZonaGuayaquil(ZonaGuayaquil.CENTRO);
        if (z.calcularUtilidadAmbiental() >= 0){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Verde/Mapa Guayaquil Trazado Blanco Centro.png")));
            zonaSeleccionada.setText("Centro de Guayaquil");
        } else if (z.calcularUtilidadAmbiental() >= -500){
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Amarillo/Mapa Guayaquil Trazado Blanco Centro.png")));
            zonaSeleccionada.setText("Centro de Guayaquil");
        } else {
            mapaView.setImage(new Image(getClass().getResourceAsStream("/images/MapasGYE/Rojo/Mapa Guayaquil Trazado Blanco Centro.png")));
            zonaSeleccionada.setText("Centro de Guayaquil");
        }
        pesoRecolectadoLabel.setText(z.getPesoRecolectado() + "");
        pesoPendienteLabel.setText(z.getPesoPendiente() + "");
        utilidadLabel.setText(z.calcularUtilidadAmbiental() + "");
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

