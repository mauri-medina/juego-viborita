import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Created by mauri on 28-Oct-16.
 */

public class PanelControlador extends Pane
{
    public static final int ANCHO_JUEGO = 250;
    public static final int ALTURA_JUEGO = 250;
    public EventHandler<KeyEvent> input;
    private Vibora vibora;
    private boolean estaVisibleElCuadriculado = false;
    private Pane panelCuadriculado;

    public void empezarJuego()
    {
        //se vacia por si ya se uso en un juego previo
        if(!getChildren().isEmpty())
            getChildren().clear();

        vibora = new Vibora(this);

        requestFocus();
    }

    public void terminarJuego()
    {
        Text texto = new Text(0, ALTURA_JUEGO/4, "GAME OVER");
        texto.setFill(Color.RED);
        Font font = Font.font("", FontWeight.EXTRA_BOLD, 30.0D);
        texto.setFont(font);

        Button boton = new Button("Otra vez");
        boton.setLayoutX(0.0D);
        boton.setLayoutY(ALTURA_JUEGO/2);
        boton.setOnAction((e) -> empezarJuego());

        getChildren().addAll(texto, boton);
    }

    public EventHandler<KeyEvent> getControladorInput()
    {
        if(input == null)
            input = new PanelControlador.Input();

        return input;
    }

    public void mostrarCuadriculado()
    {
        if(panelCuadriculado == null)
            crearPanelCuadriculado();

        panelCuadriculado.setVisible(true);
    }

    /**
     * crea una cuadricula que divide la pantalla de juego en
     * cuadrados que pueden ser las posibles poisiciones de la
     * vibora
     */
    private void crearPanelCuadriculado()
    {
        panelCuadriculado = new Pane();
        int filas = ALTURA_JUEGO / Vibora.TAMANO_CUERPO;
        int columnas = ANCHO_JUEGO / vibora.TAMANO_CUERPO;
        Color color = Color.LIGHTGRAY;

        Line columna;
        for(int i = 0; i < filas; ++i)
        {
            columna = new Line(0, (i * Vibora.TAMANO_CUERPO), ANCHO_JUEGO, (i * Vibora.TAMANO_CUERPO));
            columna.setStroke(color);
            panelCuadriculado.getChildren().add(columna);
        }

        for(int i = 0; i < columnas; ++i)
        {
            columna = new Line((i * Vibora.TAMANO_CUERPO), 0, (i * Vibora.TAMANO_CUERPO), ALTURA_JUEGO);
            columna.setStroke(color);
            panelCuadriculado.getChildren().add(columna);
        }

        getChildren().add(panelCuadriculado);

    }

    private void esconderCuadriculado()
    {
        panelCuadriculado.setVisible(false);
    }

    public void cambiarVisibilidadCuadriculado()
    {
        estaVisibleElCuadriculado = !estaVisibleElCuadriculado;

        if(estaVisibleElCuadriculado)
            mostrarCuadriculado();
        else
            esconderCuadriculado();

    }

    class Input implements EventHandler<KeyEvent>
    {
        public void handle(KeyEvent event)
        {
            switch(event.getCode())
            {
                case UP:
                    vibora.setMovActual(Vibora.Movimiento.ARRIBA);
                    break;
                case W:
                    vibora.setMovActual(Vibora.Movimiento.ARRIBA);
                    break;
                case DOWN:
                    vibora.setMovActual(Vibora.Movimiento.ABAJO);
                    break;
                case S:
                    vibora.setMovActual(Vibora.Movimiento.ABAJO);
                    break;
                case LEFT:
                    vibora.setMovActual(Vibora.Movimiento.IZQUIERDA);
                    break;
                case A:
                    vibora.setMovActual(Vibora.Movimiento.IZQUIERDA);
                    break;
                case RIGHT:
                    vibora.setMovActual(Vibora.Movimiento.DERECHA);
                    break;
                case D:
                    vibora.setMovActual(Vibora.Movimiento.DERECHA);
                    break;
                case ADD:
                    vibora.aumentarVelocidad();
                    break;
                case SUBTRACT:
                    vibora.disminuirVelocidad();
                    break;

                //para debug
                case SPACE:
                    cambiarVisibilidadCuadriculado();
            }
        }
    }
}