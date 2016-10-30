import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by mauri on 28-Oct-16.
 */
public class Main extends Application
{
    //al dibujar los paneles no se porque suma 10 unidades al tamaño deseado
    private int alturaCorregida = PanelControlador.ALTURA_JUEGO-10;
    private int anchoCorregido = PanelControlador.ANCHO_JUEGO-10;

    public void start(Stage primaryStage) throws Exception
    {
        iniciarAplicacion(primaryStage);
    }

    private void iniciarAplicacion(Stage primaryStage)
    {
        BorderPane panelInicial = new BorderPane();

        Button btn = new Button("Empezar Juego");
        panelInicial.setCenter(btn);
        btn.setOnAction((e) -> iniciarJuego(primaryStage));

        primaryStage.setScene(new Scene(panelInicial, anchoCorregido, alturaCorregida));
        primaryStage.show();
    }

    private void iniciarJuego(Stage primaryStage)
    {
        PanelControlador panel = new PanelControlador();

        primaryStage.setScene(new Scene(panel, anchoCorregido, alturaCorregida));
        primaryStage.setTitle("Vibora en JavaFx");
        primaryStage.setResizable(false);
        primaryStage.show();

        panel.setOnKeyReleased(panel.getControladorInput());
        panel.empezarJuego();
        panel.requestFocus();
    }

    public static void main(String[] args) {
        Application.launch(new String[0]);
    }
}