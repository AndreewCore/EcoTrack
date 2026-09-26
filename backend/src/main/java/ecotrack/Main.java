package ecotrack;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    /** Relleno de las franjas que quedan cuando la ventana no tiene la proporción 16:9 de las vistas. */
    private static final String COLOR_FONDO = "#ECECEC";

    /** Marca que la vista ya está envuelta, para no escalarla dos veces si se reasigna la misma escena. */
    private static final String MARCA_ESCALADA = "ecotrack.escalada";

    /** Configura la ventana principal y muestra el menú de inicio. */
    @Override
    public void start(Stage stage) throws Exception{
        // Los controladores cambian de pantalla creando una Scene nueva; escuchar aquí
        // permite escalar todas las vistas sin tocar cada controlador.
        stage.sceneProperty().addListener((obs, anterior, nueva) -> {
            if (nueva != null) {
                ajustarAVentana(nueva);
            }
        });

        Parent root = FXMLLoader.load(getClass().getResource("/vistas/ecotrack.fxml"));
        Scene scene = new Scene(root);
        Image icon = new Image("/images/icon.png");
        stage.getIcons().add(icon);
        stage.setTitle("EcoTrack");
        stage.setScene(scene);
        //stage.setFullScreen(true);
        stage.show();
    }

    /**
     * Escala la vista de la escena para que quepa completa en la ventana, conservando su proporción.
     * Las vistas FXML tienen tamaño fijo (1280x720); sin esto, un gestor de ventanas en mosaico o una
     * ventana maximizada las recorta o deja un área vacía. Con la ventana a su tamaño original la escala
     * es 1 y la vista se ve igual que antes.
     */
    private static void ajustarAVentana(Scene scene) {
        Parent vista = scene.getRoot();
        if (vista.getProperties().containsKey(MARCA_ESCALADA)) {
            return;
        }

        double ancho = vista.prefWidth(-1);
        double alto = vista.prefHeight(-1);
        NumberBinding escala = Bindings.min(
                scene.widthProperty().divide(ancho),
                scene.heightProperty().divide(alto));
        vista.scaleXProperty().bind(escala);
        vista.scaleYProperty().bind(escala);

        // El Group toma el tamaño ya escalado de la vista, así el StackPane puede centrarla.
        StackPane contenedor = new StackPane(new Group(vista));
        contenedor.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        contenedor.getProperties().put(MARCA_ESCALADA, Boolean.TRUE);
        scene.setRoot(contenedor);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
