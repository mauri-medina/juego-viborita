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
        Font font = Font.font("", FontWeight.EXTRA_BOLD, 30);
        texto.setFont(font);

        Button boton = new Button("Otra vez");
        boton.setLayoutX(0);
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
        int numDeFilas = ALTURA_JUEGO / Vibora.TAMANO_CUERPO;
        int numDeColumnas = ANCHO_JUEGO / vibora.TAMANO_CUERPO;
        Color color = Color.LIGHTGRAY;

        for(int fila = 0; fila < numDeFilas; ++fila)
        {
            Line lineaVertical = new Line(0, (fila * Vibora.TAMANO_CUERPO), ANCHO_JUEGO, (fila * Vibora.TAMANO_CUERPO));
            lineaVertical.setStroke(color);
            panelCuadriculado.getChildren().add(lineaVertical);
        }

        for(int columna = 0; columna < numDeColumnas; ++columna)
        {
            Line lineaHorizontal = new Line((columna * Vibora.TAMANO_CUERPO), 0, (columna * Vibora.TAMANO_CUERPO), ALTURA_JUEGO);
            lineaHorizontal.setStroke(color);
            panelCuadriculado.getChildren().add(lineaHorizontal);
        }

        //agrego en el primer lugar del observable list
        //para que sea el primero en dibujarse y
        // no tape a otros dibujos
        getChildren().add(0,panelCuadriculado);

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

                case ADD:                       //tecla con signo +
                    vibora.aumentarVelocidad();
                    break;
                case SUBTRACT:                  //tecla con signo -
                    vibora.disminuirVelocidad();
                    break;

                //para debug
                case SPACE:
                    cambiarVisibilidadCuadriculado();
            }
        }
    }
}
