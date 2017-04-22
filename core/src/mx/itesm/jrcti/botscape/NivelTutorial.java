package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import java.util.ArrayList;

/**
 * Created by Cinthya on 15/02/2017.
 */

public class NivelTutorial extends PantallaNivel {

    //Debuggers, handle with care
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;


    private static final int ANCHO_MAPA=3840;
    private int xInicialRobot = 100;
    private int yInicialRobot = 300;

    //Texturas
    Sprite texturaFondo;

    private Texture texturaPlataforma;
    private Texture texturaFondoTutorial;
    private Texture LUGWalk_Cycle;
    private Plataforma plat1;




    private ArrayList<Image> listaVidasVIU = new ArrayList<Image>();

    private BodyDef bodyDefPiso;
    private Body bodyPiso;
    private FixtureDef fixPiso;
    private EdgeShape edgeShape;
    private Body bodyPlat1;

    private FixtureDef fix;


    private Enemigo enemigo;
    private boolean palancaActivada = false;


    public NivelTutorial(Juego j,EstadoMusica estadoMusicaGeneral,EstadoSonido estadoSonidoGeneral){

        super(j, estadoMusicaGeneral,"Mapas/Map_TutorialV2.tmx", "Sonidos/BringTheFoxhoundToMe.mp3",estadoSonidoGeneral);
        this.juego=j;
        setManager(j.getAssetManager());
    }

    @Override
    public void show() {
        cargarMapa();
        cargarTexturasEscenciales();
        cargarTexturasExtras();
        crearObjetos();
        crearCuerpos();
        crearHUD();
    }



    private void crearCuerpos() {
        crearRobot(xInicialRobot,yInicialRobot);
        crearEnemigo();
        crearPiso();
    }

    private void crearEnemigo() {
        fix = new FixtureDef();
        fix.density=.1f;
        enemigo = new Enemigo(LUGWalk_Cycle,3f,1600,128,Enemigo.EstadoMovimiento.MOV_DERECHA,
                getWorld(), BodyDef.BodyType.KinematicBody,fix);
    }

    private void crearPiso() {

        leerMapa();

    }



    private void crearObjetos(){
        crearMundo();
        texturaFondo=new Sprite(texturaFondoTutorial);
        texturaFondo.setPosition(0,0);
        createCollisionListener();

        //Debugger
        debugRenderer = new Box2DDebugRenderer();

        plat1 = new Plataforma(texturaPlataforma, 3, 3, 200, 120,
                Plataforma.EstadoMovimiento.MOV_DERECHA, getWorld());

        Gdx.input.setCatchBackKey(true);
    }

    @Override
    protected void revisarMuertePorCaida() {
        if(getRobot().sprite.getY()+getRobot().sprite.getHeight()<0) {
            getRobot().morir();
            getRobot().reposicionar(xInicialRobot, yInicialRobot);
        }
    }

    @Override
    public void cargarTexturasExtras(){
        texturaPlataforma = getManager().get("NivelPlataforma.png");
        texturaFondoTutorial = getManager().get("Fondos/NivelTutorialFondo.jpg");
        LUGWalk_Cycle = getManager().get("Personaje/LUG7 Walk_Cycle.png");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        actualizarCamara(ANCHO_MAPA, 0);
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

            //algo
            getBatch().begin();
            enemigo.dibujar(getBatch());
            enemigo.mover(1600,2300);
            plat1.dibujar(getBatch());
            plat1.mover(100, 900, 100, 600);

            buscarMiniVis();
            if(!palancaActivada) {
                if(moverPalanca(getMapa())){
                    palancaActivada=true;
                }
            }

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
                setEscenaGanaste(new EscenaGanaste(getVistaHUD(), getBatch(),this,3));
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
    public boolean moverPalanca(TiledMap mapa) {

        TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(2);
        int x;
        int y = (int)(getRobot().sprite.getY()/64);
        TiledMapTileLayer.Cell celda;
        int cantPuntos=5;
        for(int i=0; i<cantPuntos; i++){
            x= (int)(((getRobot().sprite.getX()+getRobot().sprite.getWidth())/64));
            x= x-i*(int)getRobot().sprite.getWidth()/((cantPuntos-1)*64);
            celda = capa.getCell(x,y);
            if (celda!=null) {
                Object tipo = celda.getTile().getProperties().get("tipo");
                if ("palanca".equals(tipo) ) {
                    capa.setCell(x,y,celda.setFlipHorizontally(true));
                    mapa.getLayers().get(1).setVisible(!mapa.getLayers().get(1).isVisible());
                    return true;
                }
            }
        }
        return false;
    }




    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }


}
