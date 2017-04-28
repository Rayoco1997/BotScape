package mx.itesm.jrcti.botscape;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by Julio on 11/04/2017.
 */

public class Nivel2 extends PantallaNivel {

    //Debuggers, handle with care
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;

    private static final int ANCHO_MAPA=3840;
    private static final int ALTO_MAPA=2944;
    private int xInicialRobot = 100;
    private int yInicialRobot = 300;

    //Texturas
    Sprite texturaFondo;
    private Texture texturaPlataforma;
    private Texture texturaFondoTutorial;
    private Texture LUGWalk_Cycle;

    //Objetos
    private Plataforma platf1;
    private Plataforma platf2;
    private Plataforma platf3;

    private FixtureDef fix;
    private Enemigo enemigo1;
    private Enemigo enemigo2;

    public Nivel2(Juego j, EstadoMusica estadoMusicaGeneral,EstadoSonido estadoSonidoGeneral) {
        super(j, estadoMusicaGeneral, "Mapas/Nivel2.tmx", "Sonidos/BringTheFoxhoundToMe.mp3",estadoSonidoGeneral);
        this.juego=j;
        setManager(j.getAssetManager());
    }

    @Override
    public void cargarTexturasExtras() {
        texturaPlataforma = getManager().get("NivelPlataforma.png");
        texturaFondoTutorial = getManager().get("Fondos/Fondo2.jpg");
        LUGWalk_Cycle = getManager().get("Personaje/LUG7 Walk_Cycle.png");
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

    private void crearCuerpos() {
        crearRobot(xInicialRobot,yInicialRobot);
        crearPiso();
    }

    private void crearPiso() {
        leerMapa();
    }

    private void crearEnemigos() {
        fix = new FixtureDef();
        fix.density=.1f;
        enemigo1 = new Enemigo(LUGWalk_Cycle,3f,64,832,Enemigo.EstadoMovimiento.MOV_DERECHA,
                getWorld(), BodyDef.BodyType.KinematicBody,fix);
        enemigo2 = new Enemigo(LUGWalk_Cycle,3f,1408,1536,Enemigo.EstadoMovimiento.MOV_DERECHA,
                getWorld(), BodyDef.BodyType.KinematicBody,fix);
    }

    private void crearObjetos() {
        crearMundo();
        texturaFondo=new Sprite(texturaFondoTutorial);
        texturaFondo.setPosition(0,0);
        createCollisionListener();

        platf1 = new Plataforma(texturaPlataforma, 3, 3, 1536, 192,
                Plataforma.EstadoMovimiento.MOV_ARRIBA, getWorld());
        platf2 = new Plataforma(texturaPlataforma, 2.7f, 2.7f, 2560, 740,
                Plataforma.EstadoMovimiento.MOV_ARRIBA, getWorld());
        platf3 = new Plataforma(texturaPlataforma, 2.9f, 2.9f, 192, 1472,
                Plataforma.EstadoMovimiento.MOV_ARRIBA, getWorld());

        //Debugger
        debugRenderer = new Box2DDebugRenderer();
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    protected void revisarMuertePorCaida() {
        if(getRobot().getBody().getTransform().getPosition().y< 0.0f) {
            //Gdx.app.log("Cambio de posición"," a su posición inicial");
            getEscenaHUD().getActors().get(getEscenaHUD().getActors().size-1).remove();
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
            //Dibujando plataforma
            platf1.dibujar(getBatch());
            platf1.mover(1536,1536,192,870);
            platf2.dibujar(getBatch());
            platf2.mover(2560,3280,740,740);
            platf3.dibujar(getBatch());
            platf3.mover(192,1344,1472,2176);

            enemigo1.dibujar(getBatch());
            enemigo1.mover(64,640);
            enemigo2.dibujar(getBatch());
            enemigo2.mover(1408,1984);


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
    public boolean moverPalanca(TiledMap mapa) {
        //Saqué elementos comunes en un ciclo para detectar la
        //colisión con las palancas dentro del juego
        //Con esta optimización se puede notar una mejora en el tiempo de procesamiento,
        //al no tener que asignar continuamente los mismos valores a las variables antes mencionadas.
        long inicio = System.nanoTime();
        int x= (int)(((getRobot().sprite.getX()+getRobot().sprite.getWidth())/64));         //  Variables
        int y = (int)(getRobot().sprite.getY()/64);                                         //  extraidas
        TiledMapTileLayer.Cell celda;                                                       //  de la ejecución
        int cantPuntos=5;                                                                   //  del bucle.
        for(int j=3; j<=4;j++){
            TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(j);
            for(int i=0; i<cantPuntos; i++){
                x= x-i*(int)getRobot().sprite.getWidth()/((cantPuntos-1)*64);
                celda = capa.getCell(x,y);
                if (celda!=null) {
                    Object tipo = celda.getTile().getProperties().get("tipo");
                    if ("palanca".equals(tipo) ) {
                        capa.setCell(x,y,celda.setFlipHorizontally(true));
                        if(j==3){
                            Gdx.app.log("ACA DEBO:"," APARECER UNA PLATAFORMA");
                        }
                        else if(j==4){
                            //Aqui abre la puerta
                            mapa.getLayers().get(1).setVisible(!mapa.getLayers().get(1).isVisible());
                        }
                        return true;
                    }
                }
            }
        }
        long fin = System.nanoTime();
        Gdx.app.log("MoverPalancaOptimizado","Tiempo: " + (fin-inicio)/1000);
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
