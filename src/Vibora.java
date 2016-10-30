import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Created by mauri on 28-Oct-16.
 */

public class Vibora
{

    public static enum Movimiento {IZQUIERDA, DERECHA, ARRIBA, ABAJO}

    private Movimiento movActual;

    public static final int TAMANO_CUERPO = 10;

    private Rectangle cabeza;
    private ArrayList<Rectangle> cuerpo = new ArrayList();
    private Rectangle comida;

    private Timeline animacion;
    private final double MAX_RATE = 5.0D;
    private final double CAMBIO_EN_RATE = 0.5D;

    private PanelControlador panelDelJuego;

    public Vibora(PanelControlador panelDelJuego)
    {
        this.panelDelJuego = panelDelJuego;
        crearCabeza();
        crearComida();
        setMovActual(Vibora.Movimiento.DERECHA);
        iniciarAnimacion();
    }

    /**
     * Crea un rectangulo de posicion, tamaño y color
     * prederteminado para usar como cabeza
     */
    private void crearCabeza()
    {
        cabeza = new Rectangle(0, 0, TAMANO_CUERPO, TAMANO_CUERPO);
        cabeza.setFill(Color.BLUEVIOLET);
        panelDelJuego.getChildren().add(cabeza);
    }

    /**
     * Crea un rectangulo de posicion, tamaño y color
     * prederteminado para usar como comida
     */
    private void crearComida()
    {
        comida = new Rectangle(TAMANO_CUERPO, TAMANO_CUERPO);
        comida.setFill(Color.STEELBLUE);

        moverComidaAPosicionRandom();
        panelDelJuego.getChildren().add(comida);
    }

    private void iniciarAnimacion()
    {
        animacion = new Timeline(new KeyFrame(Duration.millis(200),
                (e) -> moverVibora()));
        animacion.setCycleCount(Animation.INDEFINITE);
        animacion.play();
    }

    /**
     * Crea un rectangulo de posicion, tamaño y color
     * prederteminado para usar como una parte del cuerpo
     */
    private Rectangle getUnPedazodDeCuerpo()
    {
        Rectangle cuerpo = new Rectangle(0., 0, TAMANO_CUERPO, TAMANO_CUERPO);
        cuerpo.setFill(Color.LIGHTGRAY);
        cuerpo.setStroke(Color.BLACK);
        return cuerpo;
    }

    /**
     * Añade el pedazo del cuerpo al cuerpo entero, lo añade
     * a la matriz cuerpo y al panel del juego
     *
     * @param pedazo
     * @param index posicion dentro del cuerpo que debe ir este pedazo
     *              la posicion 0 esta justo detras de la cabeza
     */
    private void anadirNuevoPedazoDeCuerpo(Rectangle pedazo, int index)
    {
        cuerpo.add(index, pedazo);
        panelDelJuego.getChildren().add(pedazo);
    }

    public void setMovActual(Vibora.Movimiento mov)
    {
        movActual = mov;
    }

    public void aumentarVelocidad()
    {
        if(animacion.getRate() < MAX_RATE)
            animacion.setRate(animacion.getRate() + CAMBIO_EN_RATE);

    }

    public void disminuirVelocidad()
    {
        if(animacion.getRate() - CAMBIO_EN_RATE > 0)
            animacion.setRate(animacion.getRate() - CAMBIO_EN_RATE);

    }

    public void moverVibora()
    {
        switch(movActual)
        {
            case IZQUIERDA:
                moverALaIzquierda();
                break;

            case DERECHA:
                moverALaDerecha();
                break;

            case ARRIBA:
                moverArriba();
                break;

            case ABAJO:
                moverAbajo();
        }

    }

    private void moverALaIzquierda()
    {
        mover(-TAMANO_CUERPO, 0);
    }

    private void moverALaDerecha()
    {
        mover(TAMANO_CUERPO, 0);
    }

    private void moverArriba()
    {
        mover(0, -TAMANO_CUERPO);
    }

    private void moverAbajo()
    {
        mover(0, TAMANO_CUERPO);
    }

    /**
     * Mueve la cabeza y  el cuerpo de la vibora una
     * distancia (dx, dy)
     * @param dx
     * @param dy
     */
    private void mover(int dx, int dy)
    {
        double nuevaPosX = cabeza.getX() + dx;
        double nuevaPosY = cabeza.getY() + dy;

        if(!viboraTrataDeMoverseAtras(nuevaPosX, nuevaPosY))
        {
            //hacer que siga moviendose adelante
            dx *= -1;
            dy *= -1;
        }

        cabeza.setX(cabeza.getX() + dx);
        cabeza.setY(cabeza.getY() + dy);

        //pedazo de cuerpo que va justo antes de la cabeza
        Rectangle nuevoCuello;

        if(estaCabezaSobreComida())
        {
            nuevoCuello = getUnPedazodDeCuerpo();
            nuevoCuello.setX(cabeza.getX() - dx);
            nuevoCuello.setY(cabeza.getY() - dy);
            anadirNuevoPedazoDeCuerpo(nuevoCuello, 0);
            moverComidaAPosicionRandom();
        }
        else if(cuerpo.size() > 0)
        {
            nuevoCuello = cuerpo.remove(cuerpo.size() - 1);
            nuevoCuello.setX(cabeza.getX() - dx);
            nuevoCuello.setY(cabeza.getY() - dy);
            cuerpo.add(0, nuevoCuello);
        }

        if(estaCabezaSobreCuerpo() || estaCabezaFueraDePantalla())
        {
            terminarJuego();
        }

    }

    /**
     * Termina el juego actual
     */
    private void terminarJuego()
    {
        animacion.stop();
        panelDelJuego.terminarJuego();
    }

    /**
     * Controla si la vibora trata de moverse hacia atras
     * de su movimiento anterior
     *
     * @param futuroX
     * @param futuroY
     * @return
     */
    private boolean viboraTrataDeMoverseAtras(double futuroX, double futuroY)
    {
        if(cuerpo.size() == 0)
            return true;

        else
        {
            Rectangle pedazo = cuerpo.get(0);
            return pedazo.getX() != futuroX || pedazo.getY() != futuroY;
        }
    }

    /**
     * Controla si la cabeza esta en una misma posicion
     * que una parte del cuerpo
     *
     * @return
     */
    private boolean estaCabezaSobreCuerpo()
    {

        for(Rectangle p:cuerpo)
        {
            if(p.getX() == cabeza.getX() && p.getY() == cabeza.getY())
                return true;
        }

        return false;
    }

    private boolean estaCabezaFueraDePantalla()
    {
        return  cabeza.getX() < 0 ||
                cabeza.getX() + cabeza.getWidth() > PanelControlador.ANCHO_JUEGO ||
                cabeza.getY() < 0 ||
                cabeza.getY() + cabeza.getHeight() > PanelControlador.ALTURA_JUEGO;
    }

    private boolean estaCabezaSobreComida()
    {
        return  comida.getX() == cabeza.getX() &&
                comida.getY() == cabeza.getY();
    }

    /**
     * Ubicar la comida en una posicion libre, y dentro
     * del juego elegido al azar
     */
    //TODO tratar de optimizar porque puede ser un loop infinito si no encuentra un lugar disponible
    private void moverComidaAPosicionRandom()
    {
        boolean estaComidaEnPosOcupada = true;
        int nuevaPosX = 0;
        int nuevaPosY = 0;

        while(estaComidaEnPosOcupada)
        {
            while(estaComidaEnPosOcupada)
            {
                int posAproximadaX =
                        (int)(Math.random() * (PanelControlador.ANCHO_JUEGO  - comida.getWidth()));
                int posAproximadaY =
                        (int)(Math.random() * (PanelControlador.ALTURA_JUEGO - comida.getHeight()));

                //hacer que este en una posicion exacta
                // dentro de las posibles posiciones para la vibora
                int columna = posAproximadaX / TAMANO_CUERPO;
                int fila = posAproximadaY / TAMANO_CUERPO;

                nuevaPosX = columna * TAMANO_CUERPO;
                nuevaPosY = fila * TAMANO_CUERPO;

                estaComidaEnPosOcupada = false;
                for(Rectangle p:cuerpo)
                {
                    if(p.getX() == nuevaPosX && p.getY() == nuevaPosY)
                    {
                        estaComidaEnPosOcupada = true;
                        break;
                    }
                }
            }

            comida.setX((double)nuevaPosX);
            comida.setY((double)nuevaPosY);
        }
    }
}
