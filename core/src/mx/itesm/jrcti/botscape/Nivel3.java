package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

/**
 * Created by Cinthya on 21/04/2017.
 */

public class Nivel3 extends PantallaNivel {

    //Debuggers, handle with care
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;

    private static final int ANCHO_MAPA=0;
    private static final int ALTO_MAPA=0;
    private int xInicialRobot;
    private int yInicialRobot;


    Sprite texturaFondo;

    public Nivel3(Juego j, EstadoMusica estadoMusicaGeneral, String nombreMapa, String nombreMusica, EstadoSonido estadoSonidoGeneral) {
        super(j, estadoMusicaGeneral, nombreMapa, nombreMusica, estadoSonidoGeneral);
        this.juego = j;
        setManager(j.getAssetManager());
    }

    @Override
    public void show() {
        cargarMapa();
        cargarTexturasEscenciales();
        cargarTexturasExtras();
        crearObjetos();
        crearCuerpos();
        crearEnemigos();
        crearHUD();
    }

    @Override
    public void cargarTexturasExtras() {

    }

    @Override
    public boolean moverPalanca(TiledMap mapa) {
        return false;
    }

    private void crearCuerpos() {
    }

    private void crearObjetos() {
    }

    private void crearEnemigos() {
    }

    private void crearPiso() {
        leerMapa();
    }

    @Override
    protected void revisarMuertePorCaida() {
        if (getRobot().getBody().getTransform().getPosition().y < 0.0f) {
            //Gdx.app.log("Cambio de posición"," a su posición inicial");
            getEscenaHUD().getActors().get(getEscenaHUD().getActors().size - 1).remove();
            getRobot().morir();
            getRobot().reposicionar(xInicialRobot, yInicialRobot);
        }
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        actualizarCamara(ANCHO_MAPA,ALTO_MAPA);
        revisarMuertePorCaida();
        getEstadoJuego();
        getBatch().setProjectionMatrix(camara.combined);

        if (getEstadoJuego() == EstadoJuego.PAUSADO) {
            getEscenaPausa().draw();
        } else if (getEstadoJuego()== EstadoJuego.JUGANDO){
            getRobot().actualizar(getMapa());
            getBatch().begin();
            texturaFondo.draw(getBatch());
            getBatch().end();
            getMapRenderer().setView(camara);
            getMapRenderer().render();

            debugMatrix=getBatch().getProjectionMatrix().cpy().scale(PantallaNivel.getPtM(),PantallaNivel.getPtM(),0);
            debugRenderer.render(getWorld(),debugMatrix);

            //Debugging
            debugMatrix = getBatch().getProjectionMatrix().cpy().scale(PantallaNivel.getPtM(),
                    PantallaNivel.getPtM(), 0);

            getBatch().begin();

            //Colocar objetos aquí

            buscarMiniVis();
            moverPalanca(getMapa());
            //para mostrar el puntaje de mini vis
            getTexto().mostrarMensaje(getBatch(), Integer.toString(getContadorMiniVis()), camara.position.x+ANCHO/2-50, camara.position.y+ALTO/2-40);
            getRobot().dibujar(getBatch());
            getBatch().end();

            getWorld().step(delta,6,2);
            getBatch().setProjectionMatrix(getCamaraHUD().combined);
            getEscenaHUD().draw();
            //Gdx.app.log("MI estado es:", ""+getEstadoJuego().toString());
        }

        if ((getRobot().sprite.getX()+getRobot().sprite.getWidth()/2)>ANCHO_MAPA){
            setEstadoJuego(EstadoJuego.GANADO);
            if(getEscenaGanaste()==null){
                setEscenaGanaste(new EscenaGanaste(getVistaHUD(), getBatch(),this,2));
            }
            Gdx.input.setInputProcessor(getEscenaGanaste());
        }
        if(getEstadoJuego()== EstadoJuego.GANADO){
            getEscenaGanaste().draw();
        }

        if(getRobot().getVidas()==0){
            setEstadoJuego(EstadoJuego.PIERDE);
            if(getEscenaPerdiste()==null) {
                setEscenaPerdiste(new EscenaPerdiste(getVistaHUD(), getBatch(),this));
            }
            Gdx.input.setInputProcessor(getEscenaPerdiste());

        }
        if(getEstadoJuego()==EstadoJuego.PIERDE){
            getEscenaPerdiste().draw();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            //estado = estado == EstadoJuego.JUGANDO ? EstadoJuego.PAUSADO: EstadoJuego.JUGANDO;
            if(getEstadoJuego()== EstadoJuego.JUGANDO){
                setEstadoJuego(EstadoJuego.PAUSADO);
            }
            if (getEstadoJuego() == EstadoJuego.PAUSADO) {
                // Activar escenaPausa y pasarle el control
                if (getEscenaPausa() == null) {
                    setEscenaPausa(new EscenaPausa(getVistaHUD(), getBatch()));
                }
                Gdx.input.setInputProcessor(getEscenaPausa());
            }else {
                // Continuar el juego
                setEstadoJuego(EstadoJuego.JUGANDO);
                // Regresa el control a la pantalla
                Gdx.input.setInputProcessor(getEscenaHUD());
            }

        }
    }

    @Override
       public void pause () {
        }

    @Override
    public void resume () {
    }

    @Override
    public void dispose () {

    }

}