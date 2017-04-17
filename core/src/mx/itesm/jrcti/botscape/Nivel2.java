package mx.itesm.jrcti.botscape;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

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
    private Plataforma plat1;




    public Nivel2(Juego j, EstadoMusica estadoMusicaGeneral,EstadoSonido estadoSonidoGeneral) {
        super(j, estadoMusicaGeneral, "Mapas/Nivel2.tmx", "Sonidos/BringTheFoxhoundToMe.mp3",estadoSonidoGeneral);
        this.juego=j;
        setManager(j.getAssetManager());
    }

    @Override
    public void cargarTexturasExtras() {
        texturaPlataforma = getManager().get("NivelPlataforma.png");
        texturaFondoTutorial = getManager().get("Fondos/NivelTutorialFondo.jpg");
        LUGWalk_Cycle = getManager().get("Personaje/LUG7 Walk_Cycle.png");
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
        crearPiso();
    }

    private void crearPiso() {
        leerMapa();
    }

    private void crearObjetos() {
        crearMundo();
        texturaFondo=new Sprite(texturaFondoTutorial);
        texturaFondo.setPosition(0,0);
        createCollisionListener();

        //Debugger
        debugRenderer = new Box2DDebugRenderer();
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
                                (contact.getFixtureB().getBody().getUserData().equals("piso") ||
                                        contact.getFixtureB().getBody().getUserData().equals("columna")))||
                        ((contact.getFixtureA().getBody().getUserData().equals("piso") ||
                                (contact.getFixtureA().getBody().getUserData().equals("columna")) &&
                                        contact.getFixtureB().getBody().getUserData() instanceof Robot))){
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

            //algo
            getBatch().begin();

            buscarMiniVis();

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

    @Override
    public boolean moverPalanca(TiledMap mapa) {
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
