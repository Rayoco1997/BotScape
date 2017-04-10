package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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


    public static final int ANCHO_MAPA=3840;
    private int xInicialRobot = 100;
    private int yInicialRobot = 300;

    //Texturas
    Sprite texturaFondo;

    private Texture texturaPlataforma;
    private Texture texturaFondoTutorial;
    private Texture LUGWalk_Cycle;
    private Plataforma plat1;

    //Asset Manager
    private AssetManager manager;

    private int contadorMiniVis=0;
    private Texto texto= new Texto();


    private ArrayList<Image> listaVidasVIU = new ArrayList<Image>();

    private BodyDef bodyDefPiso;
    private Body bodyPiso;
    private FixtureDef fixPiso;
    private EdgeShape edgeShape;
    private Body bodyPlat1;

    private FixtureDef fix;


    private Enemigo enemigo;


    public NivelTutorial(Juego j,EstadoMusica estadoMusicaGeneral){
        super(j, estadoMusicaGeneral,"Mapas/Map_TutorialV2.tmx", "Sonidos/BringTheFoxhoundToMe.mp3");
        this.juego=j;
        manager = j.getAssetManager();
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
        bodyDefPiso = new BodyDef();
        bodyDefPiso.type = BodyDef.BodyType.StaticBody;
        float x1= -100f/PantallaNivel.getPtM();
        float y1=128f/PantallaNivel.getPtM();
        float x2= 4000/PantallaNivel.getPtM();
        float y2=128f/PantallaNivel.getPtM();
        bodyDefPiso.position.set(0,0);
        fixPiso = new FixtureDef();
        edgeShape = new EdgeShape();
        edgeShape.set(x1,y1,x2,y2);
        fixPiso.shape=edgeShape;
        fixPiso.friction=.01f;
        bodyPiso = getWorld().createBody(bodyDefPiso);
        bodyPiso.createFixture(fixPiso);
        bodyPiso.setUserData("piso");

        x1 = 128/PantallaNivel.getPtM();
        y1 = 0/PantallaNivel.getPtM();
        x2 = 128/PantallaNivel.getPtM();
        y2 = ALTO/PantallaNivel.getPtM();
        edgeShape = new EdgeShape();
        edgeShape.set(x1,y1,x2,y2);
        fixPiso.shape=edgeShape;
        fixPiso.friction=.01f;
        bodyPlat1 = getWorld().createBody(bodyDefPiso);
        bodyPlat1.createFixture(fixPiso);
        bodyPlat1.setUserData("piso");


        x1 = 576/PantallaNivel.getPtM();
        y1 = 192/PantallaNivel.getPtM();
        x2 = 896/PantallaNivel.getPtM();
        y2 = 192/PantallaNivel.getPtM();
        edgeShape = new EdgeShape();
        edgeShape.set(x1,y1,x2,y2);
        fixPiso.shape=edgeShape;
        fixPiso.friction=.01f;
        bodyPlat1 = getWorld().createBody(bodyDefPiso);
        bodyPlat1.createFixture(fixPiso);
        bodyPlat1.setUserData("piso");

        x1 = 1216/PantallaNivel.getPtM();
        x2 = 1536/PantallaNivel.getPtM();
        edgeShape = new EdgeShape();
        edgeShape.set(x1,y1,x2,y2);
        fixPiso.shape=edgeShape;
        fixPiso.friction=.01f;
        bodyPlat1 = getWorld().createBody(bodyDefPiso);
        bodyPlat1.createFixture(fixPiso);
        bodyPlat1.setUserData("piso");

        x1 = 2304/PantallaNivel.getPtM();
        x2 = 2624/PantallaNivel.getPtM();
        edgeShape = new EdgeShape();
        edgeShape.set(x1,y1,x2,y2);
        fixPiso.shape=edgeShape;
        fixPiso.friction=.01f;
        bodyPlat1 = getWorld().createBody(bodyDefPiso);
        bodyPlat1.createFixture(fixPiso);
        bodyPlat1.setUserData("piso");

        x1 = 3584/PantallaNivel.getPtM();
        y1 =  320/PantallaNivel.getPtM();
        x2 = 3840/PantallaNivel.getPtM();
        y2 = 320/PantallaNivel.getPtM();
        edgeShape = new EdgeShape();
        edgeShape.set(x1,y1,x2,y2);
        fixPiso.shape=edgeShape;
        fixPiso.friction=.01f;
        bodyPlat1 = getWorld().createBody(bodyDefPiso);
        bodyPlat1.createFixture(fixPiso);
        bodyPlat1.setUserData("piso");

        edgeShape.dispose();

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

    private void createCollisionListener() {
        ContactListener conList = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if((contact.getFixtureA().getBody().getUserData() instanceof Robot &&
                    contact.getFixtureB().getBody().getUserData() instanceof Plataforma)||

                        (contact.getFixtureA().getBody().getUserData() instanceof Plataforma &&
                        contact.getFixtureB().getBody().getUserData() instanceof Robot) ||

                        (contact.getFixtureA().getBody().getUserData() instanceof Robot &&
                                contact.getFixtureB().getBody().getUserData().equals("piso"))||

                        (contact.getFixtureA().getBody().getUserData().equals("piso") &&
                                contact.getFixtureB().getBody().getUserData() instanceof Robot)){
                    getRobot().setEstadoSalto(Robot.EstadoSalto.EN_PISO);
                }
                if((contact.getFixtureA().getBody().getUserData() instanceof Robot &&
                        contact.getFixtureB().getBody().getUserData() instanceof Enemigo)||

                        (contact.getFixtureA().getBody().getUserData() instanceof Enemigo &&
                                contact.getFixtureB().getBody().getUserData() instanceof Robot)){
                    //Gdx.app.log("DAño","Khe");
                    if(getRobot().getHabilidad()!=Robot.Habilidad.INVULNERABLE) {
                        getEscenaHUD().getActors().get(getEscenaHUD().getActors().size-1).remove();
                        getRobot().setHabilidad(Robot.Habilidad.INVULNERABLE);
                        getRobot().recibirDano(contact.getWorldManifold());

                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };
        getWorld().setContactListener(conList);
    }

    @Override
    public void cargarTexturasExtras(){
        texturaPlataforma = manager.get("NivelPlataforma.png");
        texturaFondoTutorial = manager.get("Fondos/NivelTutorialFondo.jpg");
        LUGWalk_Cycle = manager.get("Personaje/LUG7 Walk_Cycle.png");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        actualizarCamara();
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
            enemigo.dibujar(getBatch());
            enemigo.mover(1600,2300);
            plat1.dibujar(getBatch());
            plat1.mover(100, 900, 100, 600);

            buscarMiniVis();
            getRobot().moverPalanca(getMapa());

            //para mostrar el puntaje de mini vis
            texto.mostrarMensaje(getBatch(), Integer.toString(contadorMiniVis), camara.position.x+ANCHO/2-50, camara.position.y+ALTO/2-40);
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
                setEscenaGanaste(new EscenaGanaste(getVistaHUD(), getBatch()));
            }
            Gdx.input.setInputProcessor(getEscenaGanaste());
        }
        if(getEstadoJuego()== EstadoJuego.GANADO){
            getEscenaGanaste().draw();
        }

        if(getRobot().getVidas()==0){
            setEstadoJuego(EstadoJuego.PIERDE);
            if(getEscenaPerdiste()==null) {
                setEscenaPerdiste(new EscenaPerdiste(getVistaHUD(), getBatch()));
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

    private void actualizarCamara(){
        float posX = getRobot().sprite.getX();
        if (posX>=ANCHO/2 && posX<=ANCHO_MAPA-ANCHO/2){
            camara.position.set((int)posX, camara.position.y, 0);
        }else if (posX>ANCHO_MAPA-ANCHO/2){
            camara.position.set(ANCHO_MAPA-ANCHO/2,camara.position.y, 0);
        }else if (posX<ANCHO/2){
            camara.position.set(ANCHO/2,ALTO/2,0);
        }
        camara.update();
    }

    private void buscarMiniVis(){
        if(getRobot().recolectarMiniVi(getMapa()))
            contadorMiniVis++;
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
