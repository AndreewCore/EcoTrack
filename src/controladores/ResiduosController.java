package controladores;

import ecotrack.Zona;
import ecotrack.ZonaGuayaquil;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ResiduosController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ListView<String> nombreShownListView;
    @FXML
    private ListView<TipoResiduo> tipoShownListView;
    @FXML
    private ListView<Double> pesoShownListView;
    @FXML
    private ListView<ZonaGuayaquil> zonaShownListView;

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
    private ChoiceBox<ZonaGuayaquil> zonaChoiceBox;

    private String[] opcionesFiltrado = {
        "Todos",
        "ORGANICO",
        "PLASTICO",
        "VIDRIO",
        "PAPEL",
        "METAL",
        "ELECTRONICO",
        "PELIGROSO",
        "OTROS"
    };
    
    private String[] opcionesOrdenamiento = {
        "Peso (Ascendente)",
        "Peso (Descendente)",
        "Tipo (Ascendente)",
        "Tipo (Descendente)",
        "Prioridad (Ascendente)",
        "Prioridad (Descendente)"
    };
    
    private String filtroActual = "Todos";
    private String ordenamiento;
    private String filtrado;

    private String nombreResiduo;
    private TipoResiduo tipoResiduo;
    private int valorPeso;
    private ZonaGuayaquil zonaResiduo;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Configurar ChoiceBox de tipo
        tipoChoiceBox.getItems().addAll(TipoResiduo.values());
        tipoChoiceBox.setOnAction(this::establecerTipo);

        // Configurar ChoiceBox de zona
        zonaChoiceBox.getItems().addAll(ZonaGuayaquil.values());
        zonaChoiceBox.setOnAction(this::establecerZona);
        
        // Configurar ChoiceBox de filtrado - ¡SOLO UNA VEZ!
        filtradoChoiceBox.getItems().addAll(opcionesFiltrado);
        filtradoChoiceBox.setOnAction(this::aplicarFiltrado);
        filtradoChoiceBox.setValue("Todos");
        
        // Configurar ChoiceBox de ordenamiento
        ordenamientoChoiceBox.getItems().addAll(opcionesOrdenamiento);
        ordenamientoChoiceBox.setOnAction(this::aplicarOrdenamiento);
        
        // Cargar datos iniciales
        actualizarVista();
    }

    public void establecerZona(ActionEvent event) {
        zonaResiduo = zonaChoiceBox.getValue();
        System.out.println("DEBUG: Zona seleccionada: " + zonaResiduo);
    }

    public void establecerTipo(ActionEvent event) {
        tipoResiduo = tipoChoiceBox.getValue();
        System.out.println("DEBUG: Tipo seleccionado: " + tipoResiduo);
    }

    public void elegirOrdenamiento(ActionEvent event) {
        ordenamiento = ordenamientoChoiceBox.getValue();
        System.out.println("DEBUG: Ordenamiento seleccionado: " + ordenamiento);
    }

    public void elegirFiltrado(ActionEvent event) {
        filtrado = filtradoChoiceBox.getValue();
        System.out.println("DEBUG: Filtrado seleccionado: " + filtrado);
    }

    @FXML
    private void aplicarOrdenamiento(ActionEvent event) {
        String seleccion = ordenamientoChoiceBox.getValue();
        if (seleccion == null) {
            return;
        }

        try {
            switch (seleccion) {
                case "Peso (Ascendente)":
                    ListaResiduos.getListaResiduosGlobal().ordenarPorPeso();
                    avisoLabel.setText("Ordenado por Peso (Ascendente)");
                    break;
                case "Peso (Descendente)":
                    ListaResiduos.getListaResiduosGlobal().ordenarPorPesoDescendente();
                    avisoLabel.setText("Ordenado por Peso (Descendente)");
                    break;
                case "Tipo (Ascendente)":
                    ListaResiduos.getListaResiduosGlobal().ordenarPorTipo();
                    avisoLabel.setText("Ordenado por Tipo (Ascendente)");
                    break;
                case "Tipo (Descendente)":
                    ListaResiduos.getListaResiduosGlobal().ordenarPorTipoDescendente();
                    avisoLabel.setText("Ordenado por Tipo (Descendente)");
                    break;
                case "Prioridad (Ascendente)":
                    ListaResiduos.getListaResiduosGlobal().ordenarPorPrioridad();
                    avisoLabel.setText("Ordenado por Prioridad (Ascendente)");
                    break;
                case "Prioridad (Descendente)":
                    ListaResiduos.getListaResiduosGlobal().ordenarPorPrioridadDescendente();
                    avisoLabel.setText("Ordenado por Prioridad (Descendente)");
                    break;
            }
            avisoLabel.setTextFill(Color.BLUE);
            actualizarVista();
        } catch (Exception e) {
            avisoLabel.setText("Error al ordenar: " + e.getMessage());
            avisoLabel.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }

    @FXML  // ← ¡FALTA ESTA ANOTACIÓN!
    private void aplicarFiltrado(ActionEvent event) {
        String filtroSeleccionado = filtradoChoiceBox.getValue();
        
        if (filtroSeleccionado == null) {
            filtroActual = "Todos";
        } else {
            filtroActual = filtroSeleccionado;
        }
        
        System.out.println("DEBUG: Filtro seleccionado: " + filtroActual);
        actualizarVista();
    }
    
    private ListaResiduos obtenerListaFiltrada() {
        System.out.println("DEBUG: Obteniendo lista filtrada para: " + filtroActual);
        
        if (filtroActual == null || filtroActual.equals("Todos")) {
            System.out.println("DEBUG: Mostrando TODOS los residuos");
            return ListaResiduos.getListaResiduosGlobal();
        }
        
        try {
            TipoResiduo tipoFiltro = null;
            
            // Verifica qué tipo de filtro es
            switch (filtroActual) {
                case "ORGANICO":
                    tipoFiltro = TipoResiduo.ORGANICO;
                    break;
                case "PLASTICO":
                    tipoFiltro = TipoResiduo.PLASTICO;
                    break;
                case "VIDRIO":
                    tipoFiltro = TipoResiduo.VIDRIO;
                    break;
                case "METAL":
                    tipoFiltro = TipoResiduo.METAL;
                    break;
                case "ELECTRONICO":
                    tipoFiltro = TipoResiduo.ELECTRONICO;
                    break;
                case "PAPEL":
                    tipoFiltro = TipoResiduo.PAPEL;  // Si existe en tu enum
                    break;
                case "PELIGROSO":
                    tipoFiltro = TipoResiduo.PELIGROSO;  // Si existe en tu enum
                    break;
                case "OTROS":
                    tipoFiltro = TipoResiduo.OTROS;  // Si existe en tu enum
                    break;
                default:
                    System.out.println("DEBUG: Filtro no reconocido: " + filtroActual);
                    return ListaResiduos.getListaResiduosGlobal();
            }
            
            if (tipoFiltro != null) {
                System.out.println("DEBUG: Filtrando por tipo: " + tipoFiltro);
                ListaResiduos resultado = ListaResiduos.getListaResiduosGlobal().filtrarPorTipo(tipoFiltro);
                System.out.println("DEBUG: Encontrados " + resultado.size() + " residuos");
                return resultado;
            }
            
        } catch (Exception e) {
            System.err.println("ERROR en filtrado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ListaResiduos.getListaResiduosGlobal();
    }

    public void agregarResiduo(ActionEvent event) {
        // Validar nombre
        if (nombreField.getText() == null || nombreField.getText().isEmpty()) {
            avisoLabel.setText("Por favor, escribir un Nombre para el Residuo");
            avisoLabel.setTextFill(Color.RED);
            return;
        }

        // Validar tipo
        if (tipoChoiceBox.getValue() == null) {
            avisoLabel.setText("Por favor, elegir un Tipo de Residuo");
            avisoLabel.setTextFill(Color.RED);
            return;
        }

        // Validar zona
        if (zonaChoiceBox.getValue() == null) {
            avisoLabel.setText("Por favor, elegir una Zona");
            avisoLabel.setTextFill(Color.RED);
            return;
        }

        try {
            valorPeso = Integer.parseInt(pesoField.getText());

            if (valorPeso <= 0) {
                avisoLabel.setText("El peso debe ser mayor a 0");
                avisoLabel.setTextFill(Color.RED);
                return;
            }

            nombreResiduo = nombreField.getText();
            tipoResiduo = tipoChoiceBox.getValue();

            Residuo r = new Residuo(nombreResiduo, tipoResiduo, valorPeso, zonaResiduo);
            ListaResiduos.getListaResiduosGlobal().addLast(r);

            avisoLabel.setText("Se agregó el Residuo Exitosamente");
            avisoLabel.setTextFill(Color.GREEN);

            // Limpiar campos después de agregar exitosamente
            limpiarCampos();
            
            // ¡IMPORTANTE! Usar actualizarVista() en lugar de agregar manualmente
            actualizarVista();  // ← CORREGIDO

        } catch (NumberFormatException e) {
            avisoLabel.setText("Por favor, solo ingrese números en Peso");
            avisoLabel.setTextFill(Color.RED);
        } catch (Exception e) {
            avisoLabel.setText("Error: " + e.getMessage());
            avisoLabel.setTextFill(Color.RED);
            e.printStackTrace();
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
    private void switchToSceneMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vistas/ecotrack.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void actualizarVista() {
        // Limpiar todas las listas
        nombreShownListView.getItems().clear();
        tipoShownListView.getItems().clear();
        pesoShownListView.getItems().clear();
        zonaShownListView.getItems().clear();
        
        // Obtener lista filtrada
        ListaResiduos listaFiltrada = obtenerListaFiltrada();
        
        // DEPURACIÓN: Mostrar en consola
        System.out.println("=== ACTUALIZAR VISTA ===");
        System.out.println("Filtro actual: " + filtroActual);
        System.out.println("Total residuos en lista global: " + ListaResiduos.getListaResiduosGlobal().size());
        System.out.println("Residuos después de filtrar: " + listaFiltrada.size());
        
        // Mostrar cada residuo filtrado en consola
        int contador = 1;
        for (Residuo r : listaFiltrada) {
            System.out.println(contador + ". " + r.getNombre() + " - Tipo: " + r.getTipo());
            contador++;
            
            // Agregar a las listas de la interfaz
            nombreShownListView.getItems().add(r.getNombre());
            tipoShownListView.getItems().add(r.getTipo());
            pesoShownListView.getItems().add(r.getPeso());
            zonaShownListView.getItems().add(r.getZona().getZonaGuayaquil());
        }
        
        // Actualizar label informativo
        if (!filtroActual.equals("Todos")) {
            avisoLabel.setText("Mostrando " + listaFiltrada.size() + " residuos de tipo: " + filtroActual);
        } else {
            avisoLabel.setText("Mostrando todos los residuos (" + listaFiltrada.size() + " total)");
        }
        avisoLabel.setTextFill(Color.BLUE);
    }
}