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
    /*Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;*/

    private static final int ANCHO_MAPA=3840;
    private static final int ALTO_MAPA=2944;
    private int xInicialRobot = 100;
    private int yInicialRobot = 300;

    //Texturas
    Sprite texturaFondo;
    private Texture texturaPlataforma;
    private Texture texturaFondoNivel2;
    private Texture LUGWalk_Cycle;
    private Texture texturaIman;
    private Texture texturaBanda;
    private Texture texturaBoss;

    //Objetos
    private Plataforma platf1;
    private Plataforma platf2;
    private Plataforma platf3;

    private Banda banda1;
    private Banda banda2;
    private Banda banda3;

    private Iman iman;

    private FixtureDef fix;
    private Enemigo enemigo1;
    private Enemigo enemigo2;
    private Geniallo boss;

    private boolean flagPlat;
    private boolean flagIman;
    private boolean flagPuerta;

    public Nivel2(Juego j, EstadoMusica estadoMusicaGeneral,EstadoSonido estadoSonidoGeneral) {
        super(j, estadoMusicaGeneral, "Mapas/Nivel2.tmx", "Sonidos/BringTheFoxhoundToMe.mp3",estadoSonidoGeneral);
        this.juego=j;
        setManager(j.getAssetManager());
    }

    @Override
    public void cargarTexturasExtras() {
        texturaPlataforma = getManager().get("NivelPlataforma.png");
        texturaFondoNivel2 = getManager().get("Fondos/Fondo2.jpg");
        LUGWalk_Cycle = getManager().get("Personaje/LUG7 Walk_Cycle.png");
        texturaIman = getManager().get("NivelIman.png");
        texturaBanda = getManager().get("NivelBandas.png");
        texturaBoss = getManager().get("NivelBoss.png");
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
        fix.density = .1f;
        enemigo1 = new Enemigo(LUGWalk_Cycle,3f,64,832,Enemigo.EstadoMovimiento.MOV_DERECHA,
                getWorld(), BodyDef.BodyType.KinematicBody,fix);
        enemigo2 = new Enemigo(LUGWalk_Cycle,3f,1408,1536,Enemigo.EstadoMovimiento.MOV_DERECHA,
                getWorld(), BodyDef.BodyType.KinematicBody,fix);
        boss = new Geniallo(texturaBoss, 3f, 320, 400,
                getWorld(), BodyDef.BodyType.KinematicBody,fix);
    }

    private void crearObjetos() {
        crearMundo();
        texturaFondo=new Sprite(texturaFondoNivel2);
        texturaFondo.setPosition(0,0);
        createCollisionListener();

        platf1 = new Plataforma(texturaPlataforma, 3, 3, 1536, 800,
                Plataforma.EstadoMovimiento.MOV_ARRIBA, getWorld());
        platf2 = new Plataforma(texturaPlataforma, 2.7f, 2.7f, 2560, 740,
                Plataforma.EstadoMovimiento.MOV_ARRIBA, getWorld());
        platf3 = new Plataforma(texturaPlataforma, 2.9f, 2.9f, 192, 1472,
                Plataforma.EstadoMovimiento.MOV_ARRIBA, getWorld());
        banda1 = new Banda(texturaBanda, 7*PantallaNivel.getTtoP()+22,33*PantallaNivel.getTtoP()+19,fix,getWorld(),true);
        banda2 = new Banda(texturaBanda, 20*PantallaNivel.getTtoP()+22,33*PantallaNivel.getTtoP()+19,fix,getWorld(),true);
        banda3 = new Banda(texturaBanda, 32*PantallaNivel.getTtoP()+22,33*PantallaNivel.getTtoP()+19,fix,getWorld(),true);
        iman = new Iman(texturaIman,2.5f,2.5f, 52*PantallaNivel.getTtoP(),42*PantallaNivel.getTtoP(), Plataforma.EstadoMovimiento.MOV_ABAJO,getWorld());
        //iman = new Iman(texturaIman,2.5f,2.5f, 10*PantallaNivel.getTtoP(),10*PantallaNivel.getTtoP(), Plataforma.EstadoMovimiento.MOV_ABAJO,getWorld());

        //Debugger
        //debugRenderer = new Box2DDebugRenderer();
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    protected void revisarMuertePorCaida() {
        //Gdx.app.log("x "+getRobot().sprite.getX(),"  y "+getRobot().sprite.getY());
        //Gdx.app.log("Cámara:   x "+camara.position.x,"  y "+camara.position.y);
        if(getRobot().getBody().getTransform().getPosition().y< 0.0f) {
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
            //Dibujando plataforma
            if(flagPlat) {
                platf1.dibujar(getBatch());
                platf1.mover(1536, 1536, 192, 870);
            }
            platf2.dibujar(getBatch());
            platf2.mover(2560,3280,740,740);
            platf3.dibujar(getBatch());
            platf3.mover(192,1344,1472,1344);

            banda1.dibujar(getBatch(),delta);
            banda2.dibujar(getBatch(),delta);
            banda3.dibujar(getBatch(),delta);

            iman.dibujar(getBatch());
            if(flagIman)
                iman.mover(52*PantallaNivel.getTtoP(),52*PantallaNivel.getTtoP(),21*PantallaNivel.getTtoP(),43*PantallaNivel.getTtoP());

            enemigo1.dibujar(getBatch(),delta);
            enemigo1.mover(64,640);
            enemigo2.dibujar(getBatch(),delta);
            enemigo2.mover(1408,1984);
            //boss.dibujar(getBatch());

            moverRobotConBanda(banda1);
            elevarRobotConIman(iman);


            buscarMiniVis();
            moverPalanca(getMapa());
            //para mostrar el puntaje de mini vis
            getTexto().mostrarMensaje(getBatch(), Integer.toString(getContadorMiniVis()), camara.position.x+ANCHO/2-50, camara.position.y+ALTO/2-40);
            getRobot().dibujar(getBatch());
            getBatch().end();

            getWorld().step(delta,6,2);
            getBatch().setProjectionMatrix(getCamaraHUD().combined);
            getEscenaHUD().draw();
            //Gdx.app.log("MI estado de robot es:", ""+getRobot().getEstadoSalto());
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
        for(int j=3; j<=5;j++){
            TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(j);
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
                        if(j==3){
                            //Gdx.app.log("ACA DEBO:"," APARECER UNA PLATAFORMA");
                            flagPlat = true;
                        }
                        else if(j==4){
                            //Aqui abre la puerta
                            Gdx.app.log("Palanca","Debloqueo la salida");
                            mapa.getLayers().get(1).setVisible(!mapa.getLayers().get(1).isVisible());
                        }
                        else if(j==5){
                            flagIman = true;
                        }
                        return true;
                    }
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
