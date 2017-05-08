package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by Cinthya on 21/04/2017.
 */

public class Nivel3 extends PantallaNivel {

    //Debuggers, handle with care
    //Box2DDebugRenderer debugRenderer;
    //Matrix4 debugMatrix;

    private static final int ANCHO_MAPA=5120;
    private static final int ALTO_MAPA=2880;
    private int xInicialRobot=100;
    private int yInicialRobot=300;

    private int TOTAL_MINIVIS= 9;

    //Texturas
    Sprite texturaFondo;
    private Texture texturaFondoTutorial;
    private Texture LUGWalk_Cycle;
    private Texture texturaPlataforma;
    private Texture texturaBandas;
    private Texture texturaIman;
    private Texture texturaBoss;
    private Texture texturaBossMuerte;

    //Objetos
    private Plataforma platf1;
    private Iman iman1;
    private Banda banda1;

    private FixtureDef fix;
    private Enemigo enemigo1;
    private Enemigo enemigo2;
    private Geniallo jelloDeLimon;

    private Sound explosionGeniallo;

    public Nivel3(Juego j, EstadoMusica estadoMusicaGeneral,EstadoSonido estadoSonidoGeneral) {
        super(j, estadoMusicaGeneral, "Mapas/Nivel3.tmx", "Sonidos/BelowThePath(FinalBoss).mp3",estadoSonidoGeneral);
        this.juego=j;
        setManager(j.getAssetManager());
        explosionGeniallo= Gdx.audio.newSound(Gdx.files.internal("Sonidos/Sound Effects/In Game/ExplosionGeniallo.mp3"));
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
        texturaFondoTutorial = getManager().get("Fondos/Fondo3.jpg");
        LUGWalk_Cycle = getManager().get("Personaje/LUG7 Walk_Cycle.png");
        texturaPlataforma = getManager().get("NivelPlataforma.png");
        texturaIman = getManager().get("NivelIman.png");
        texturaBandas= getManager().get("NivelBandas.png");
        texturaBoss = getManager().get("Personaje/GenialloWalk_Cycle.png");
        texturaBossMuerte = getManager().get("Personaje/GenialloDeath_Cycle.png");
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
                if ("palancaPuerta".equals(tipo) ) {
                    if(!celda.getFlipHorizontally()){
                        if(estadoSonidoGeneral==EstadoSonido.ENCENDIDO){
                            sonidoPuerta.play(1.5f);
                        }
                    }
                    capa.setCell(x,y,celda.setFlipHorizontally(true));
                    Gdx.app.log("Nivel 3","Movi la palanca de la puerta, ahora debo abrirla");
                    if(mapa.getLayers().get(1).isVisible())
                        mapa.getLayers().get(1).setVisible(false);
                    return true;
                }else if("palancaBoss".equals(tipo) ){
                    if(!celda.getFlipHorizontally()){
                        if(estadoSonidoGeneral==EstadoSonido.ENCENDIDO){
                            sonidoPuerta.play(1.5f);
                            explosionGeniallo.play();
                        }


                        capa.setCell(x,y,celda.setFlipHorizontally(true));
                        //Gdx.app.log("Nivel 3","Movi la palanca del boss, ahora puedo ganar");
                        if(mapa.getLayers().get(3).isVisible())
                            mapa.getLayers().get(3).setVisible(false);
                        jelloDeLimon.morir(getWorld());


                    }

                    return true;
                }
            }

        }

        return false;
    }

    private void crearCuerpos() {
        crearRobot(xInicialRobot,yInicialRobot);
        crearPiso();
    }

    private void crearObjetos() {
        fix = new FixtureDef();
        fix.density=.1f;
        crearMundo();
        texturaFondo=new Sprite(texturaFondoTutorial);
        texturaFondo.setPosition(0,0);
        createCollisionListener();

        //Objetos
        //platf1 = new Plataforma(texturaPlataforma, 3, 3, 1984, 384,
                //Plataforma.EstadoMovimiento.MOV_ARRIBA, getWorld());
        iman1= new Iman(texturaIman,2.5f ,2.5f ,37*PantallaNivel.getTtoP(),20*PantallaNivel.getTtoP(), Plataforma.EstadoMovimiento.MOV_ARRIBA, getWorld());
        banda1= new Banda(texturaBandas,48*PantallaNivel.getTtoP(),29*PantallaNivel.getTtoP()+19,fix,getWorld(),false);




        //Debugger
        //debugRenderer = new Box2DDebugRenderer();
        Gdx.input.setCatchBackKey(true);
    }

    private void crearEnemigos() {
        fix = new FixtureDef();
        fix.density=.1f;
        enemigo1 = new Enemigo(LUGWalk_Cycle,3f,2560,256,Enemigo.EstadoMovimiento.MOV_DERECHA,
                getWorld(), BodyDef.BodyType.KinematicBody,fix);
        enemigo2 = new Enemigo(LUGWalk_Cycle,3f,640,896,Enemigo.EstadoMovimiento.MOV_DERECHA,
                getWorld(), BodyDef.BodyType.KinematicBody,fix);
        jelloDeLimon = new Geniallo(texturaBoss, texturaBossMuerte,2f,7*PantallaNivel.getTtoP(),28*PantallaNivel.getTtoP(),getWorld(),Geniallo.EstadoMovimiento.MOV_DERECHA);
    }

    private void crearPiso() {
        leerMapa();
    }

    @Override
    protected void revisarMuertePorCaida() {
        if (getRobot().getBody().getTransform().getPosition().y < 0.0f) {
            Gdx.input.vibrate(500);
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

            /*debugMatrix=getBatch().getProjectionMatrix().cpy().scale(PantallaNivel.getPtM(),PantallaNivel.getPtM(),0);
            debugRenderer.render(getWorld(),debugMatrix);

            //Debugging
            debugMatrix = getBatch().getProjectionMatrix().cpy().scale(PantallaNivel.getPtM(),
                    PantallaNivel.getPtM(), 0);*/

            getBatch().begin();

            //Colocar objetos aquí
            enemigo1.dibujar(getBatch(),delta);
            enemigo1.mover(2560,3390);
            enemigo2.dibujar(getBatch(),delta);
            enemigo2.mover(1600,2240);
            //platf1.dibujar(getBatch());
            //platf1.mover(1984,4608,320,320);
            iman1.dibujar(getBatch());
            iman1.mover(2368,2368,20*PantallaNivel.getTtoP(),40*PantallaNivel.getTtoP());
            elevarRobotConIman(iman1);

            banda1.dibujar(getBatch(),delta);
            moverRobotConBanda(banda1);

            jelloDeLimon.dibujar(getBatch(),delta);
            if(jelloDeLimon.isVivo()) {
                jelloDeLimon.mover(8 * PantallaNivel.getTtoP(), 15 * PantallaNivel.getTtoP());
            }

            recolectar();
            moverPalanca(getMapa());
            //para mostrar el puntaje de mini vis
            getTexto().mostrarMensaje(getBatch(), Integer.toString(getContadorMiniVis()) , camara.position.x+ANCHO/2-50, camara.position.y+ALTO/2-40);
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
                setEscenaGanaste(new EscenaGanaste(getVistaHUD(), getBatch(),this,super.CalcularPuntajeMiniVis(getContadorMiniVis(),TOTAL_MINIVIS)));
            }
            Gdx.input.setInputProcessor(getEscenaGanaste());
        }
        if(getEstadoJuego()== EstadoJuego.GANADO){
            getEscenaGanaste().draw();
        }

        if(getRobot().getVidas()<=0){
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