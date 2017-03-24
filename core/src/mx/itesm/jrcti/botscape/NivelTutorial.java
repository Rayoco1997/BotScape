package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by Cinthya on 15/02/2017.
 */

public class NivelTutorial extends Pantalla {

    private World world;
    private OrthogonalTiledMapRenderer renderarMapa;
    private SpriteBatch batch;
    private TiledMap mapa;
    public static final float PIXELS_TO_METERS=100f;
    public static final int ANCHO_MAPA=3840;

    //Texturas
    private Texture VIUWalk_Cycle;
    private Texture texturaEnemigo;
    private Texture texturaPlataforma;
    private Texture texturaBoton;
    private Texture texturaIman;
    private Texture texturaMiniVI;
    private Texture texturaVida;
    private Texture texturaMiniVIRecolectados;
    private Texture texturaPiso;
    private Texture texturaTutorial;
    private Texture texturaEscalon;
    private Texture texturaPisoVerde;
    private Texture texturaSalida;
    private Texture texturaFondoTutorial;
    private Texture texturaBtnPausa;
    private Texture texturaBtnIzquierda;
    private Texture texturaBtnDerecha;
    private Texture texturaBtnSaltar;
    private Texture texturaBtnUsar;
    private Texture texturaReintentar;
    private Camera camaraHUD;
    private StretchViewport vistaHUD;

    private Plataforma plat1;


    //Escenas
    private Stage escenaNivelTutorial;
    private Stage escenaVidasVIU;
    private Stage escenaHUD;



    // Sonidos largos
    private Music musicaFondo;

    //Asset Manager
    private AssetManager manager;

    private EstadoJuego estado = EstadoJuego.JUGANDO;
    private EscenaPausa escenaPausa;
    private EscenaPerdiste escenaPerdiste;
    private EscenaGanaste escenaGanaste;
    private EstadoJuego estadoJuego;

    private int contadorMiniVis=0;
    private Texto texto= new Texto();

    private int vidasVIU=3;
    private ArrayList<Image> listaVidasVIU = new ArrayList<Image>();
    int altoVidasVIU;

    BodyDef bodyDefPiso;
    Body bodyEdge;
    FixtureDef fixPiso;
    EdgeShape edgeShape;

    FixtureDef fix;
    Robot robot;

    public NivelTutorial(Juego j,EstadoMusica estadoMusicaGeneral){
        super();
        this.juego=j;
        this.estadoMusicaGeneral= estadoMusicaGeneral;
        manager = j.getAssetManager();
    }

    @Override
    public void show() {
        cargarMapa();
        cargarTexturas();
        crearObjetos();
        crearCuerpos();
        crearHUD();
        //Gdx.input.setInputProcessor(this);
    }

    private void crearHUD() {

        camaraHUD = new OrthographicCamera(ANCHO, ALTO);
        camaraHUD.position.set(ANCHO / 2, ALTO / 2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);
        escenaVidasVIU = new Stage(vistaHUD, batch);
        escenaHUD = new Stage(vistaHUD);

        //Botón para ir al menu de pausa
        TextureRegionDrawable trdBtnPausa = new TextureRegionDrawable(new TextureRegion(texturaBtnPausa));
        ImageButton btnPausa = new ImageButton(trdBtnPausa);
        btnPausa.setPosition(10,ALTO-btnPausa.getHeight());
        escenaHUD.addActor(btnPausa);

        btnPausa.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked", "me hicieron CLICK");
                estado = estado==EstadoJuego.PAUSADO?EstadoJuego.JUGANDO:EstadoJuego.PAUSADO;
                if (estado==EstadoJuego.PAUSADO) {
                    // Activar escenaPausa y pasarle el control
                    if (escenaPausa==null) {
                        escenaPausa = new EscenaPausa(vistaHUD, batch);
                    }
                    Gdx.input.setInputProcessor(escenaPausa);
                }
            }
        });

        //Boton de prueba para decrementar vidas
        /*TextureRegionDrawable trdBtnReintentar = new TextureRegionDrawable(new TextureRegion(texturaReintentar));
        ImageButton btnReintentar = new ImageButton(trdBtnReintentar);
        btnReintentar.setPosition(ANCHO/2,0);
        escenaHUD.addActor(btnReintentar);
        btnReintentar.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked", "me hicieron CLICK");
                vidasVIU--;
            }
        });*/

        TextureRegionDrawable trdBtnMovIzquierda = new TextureRegionDrawable(new TextureRegion(texturaBtnIzquierda));
        ImageButton btnMovIzq = new ImageButton(trdBtnMovIzquierda);
        btnMovIzq.setPosition(ANCHO/8,50);
        escenaHUD.addActor(btnMovIzq);
        btnMovIzq.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                Gdx.app.log("Click","Mover izquierda");
                robot.setEstadoMovimiento(Robot.EstadoMovimiento.MOV_IZQUIERDA);
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("UnClick", "Mover izquierda");
                robot.frenar();

            }
        });

        TextureRegionDrawable trdBtnMovDerecha = new TextureRegionDrawable(new TextureRegion(texturaBtnDerecha));
        ImageButton btnMovDer = new ImageButton(trdBtnMovDerecha);
        btnMovDer.setPosition(ANCHO/4,50);
        escenaHUD.addActor(btnMovDer);
        btnMovDer.addListener( new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                Gdx.app.log("Click","Mover derecha");
                robot.setEstadoMovimiento(Robot.EstadoMovimiento.MOV_DERECHA);
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("UnClick", "Mover derecha");
                robot.frenar();

            }
        });

        TextureRegionDrawable trdBtnMovSalto= new TextureRegionDrawable(new TextureRegion(texturaBtnSaltar));
        ImageButton btnMovSalto = new ImageButton(trdBtnMovSalto);
        btnMovSalto.setPosition(6*ANCHO/8,50);
        escenaHUD.addActor(btnMovSalto);
        btnMovSalto.addListener( new InputListener(){

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("click", "Salto");
                if (robot.getEstadoSalto() == Robot.EstadoSalto.EN_PISO) {
                    robot.setEstadoSalto(Robot.EstadoSalto.SUBIENDO);
                }
                return true;
            }
        });



        //CREANDO ICONO DE MINIVI Contador

        Image iconoMiniVi= new Image(texturaMiniVI);
        iconoMiniVi.setPosition(ANCHO-2*iconoMiniVi.getWidth()-35,ALTO-iconoMiniVi.getHeight()-20);
        escenaHUD.addActor(iconoMiniVi);

        Gdx.input.setInputProcessor(escenaHUD);

    }

    private void crearCuerpos() {
        crearRobot();
        crearPiso();
    }

    private void crearPiso() {
        bodyDefPiso = new BodyDef();
        bodyDefPiso.type = BodyDef.BodyType.StaticBody;
        float x1= -100f/PIXELS_TO_METERS;
        float y1=128f/PIXELS_TO_METERS;
        float x2= 4000/PIXELS_TO_METERS;
        float y2=128f/PIXELS_TO_METERS;
        bodyDefPiso.position.set(0,0);
        fixPiso = new FixtureDef();
        edgeShape = new EdgeShape();
        edgeShape.set(x1,y1,x2,y2);
        fixPiso.shape=edgeShape;
        fixPiso.friction=.7f;
        bodyEdge= world.createBody(bodyDefPiso);
        bodyEdge.createFixture(fixPiso);
        bodyEdge.setUserData(plat1);
        edgeShape.dispose();

    }

    private void crearRobot() {
        fix = new FixtureDef();
        fix.density=.1f;
        fix.restitution=0.1f;
        fix.friction=.3f;
        robot = new Robot(VIUWalk_Cycle,100,400,world, BodyDef.BodyType.DynamicBody,fix);
    }

    private void cargarMapa() {
        mapa = manager.get("Mapas/Map_TutorialV2.tmx");
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("Sonidos/BringTheFoxhoundToMe.mp3"));
        musicaFondo.setLooping(true);
        if(estadoMusicaGeneral!= EstadoMusica.APAGADO) {
            musicaFondo.play();
        }
        //musicaFondo.play();

        batch = new SpriteBatch();
        renderarMapa = new OrthogonalTiledMapRenderer(mapa, batch);
        renderarMapa.setView(camara);
    }

    private void crearObjetos(){
        world=new World(new Vector2(0,-5),true);
        createCollisionListener();
        //escenaNivelTutorial = new Stage(vista,batch);
        //escenaVidasVIU= new Stage(vista,batch);

        plat1 = new Plataforma(texturaPlataforma, 3, 3, 30, 30, Plataforma.EstadoMovimiento.MOV_DERECHA);



        //CREANDO AL ARRAY LIST PARA LAS VIDAS DE VIU
        Image vida1= new Image(texturaMiniVI);
        altoVidasVIU= (int)ALTO-texturaMiniVI.getHeight()-10;
        vida1.setPosition(ANCHO/2,altoVidasVIU);
        Image vida2= new Image(texturaMiniVI);
        vida2.setPosition(vida1.getX()+vida2.getWidth()+10,altoVidasVIU);
        Image vida3= new Image(texturaMiniVI);
        vida3.setPosition(vida2.getX()+ vida3.getWidth()+10,altoVidasVIU);

        listaVidasVIU.add(vida1);
        listaVidasVIU.add(vida2);
        listaVidasVIU.add(vida3);



        //Gdx.input.setInputProcessor(escenaNivelTutorial);
        Gdx.input.setCatchBackKey(true);
    }

    private void createCollisionListener() {
        ContactListener conList = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if((contact.getFixtureA().getBody().getUserData() instanceof Robot &&
                    contact.getFixtureB().getBody().getUserData() instanceof Plataforma)||
                        (contact.getFixtureA().getBody().getUserData() instanceof Plataforma &&
                        contact.getFixtureB().getBody().getUserData() instanceof Robot)){
                    robot.setEstadoSalto(Robot.EstadoSalto.EN_PISO);
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
        world.setContactListener(conList);
    }

    private void cargarTexturas(){
        VIUWalk_Cycle = manager.get("Personaje/VIUWalk_Cycle.png");
        texturaEnemigo = manager.get("NivelEnemigo.png");
        texturaPlataforma = manager.get("NivelPlataforma.png");
        texturaBoton = manager.get("NivelBoton.png");
        texturaIman = manager.get("NivelIman.png");
        texturaMiniVI = manager.get("NivelMiniVI.png");
        texturaVida = manager.get("NivelVida.png");
        texturaMiniVIRecolectados = manager.get("NivelContador.png");
        texturaPiso = manager.get("NivelPiso.png");
        texturaEscalon = manager.get("NivelEscalon.png");
        texturaSalida = manager.get("NivelSalida.png");
        texturaFondoTutorial = manager.get("Fondos/PausaFondo.jpg");
        texturaBtnPausa = manager.get("NivelPausa.png");
        texturaBtnIzquierda = manager.get("NivelIzquierda.png");
        texturaBtnDerecha = manager.get("NivelDerecha.png");
        texturaBtnSaltar = manager.get("NivelSaltar.png");
        texturaBtnUsar = manager.get("NivelUsar.png");
        texturaPisoVerde = manager.get ("NivelPiso2.png");
        texturaTutorial = manager.get("Tutorial2.png");
        texturaReintentar= manager.get("Botones/PerdisteBtnReintentar.png");
        texturaBtnIzquierda = manager.get("Botones/MovIzqButton.png");
        texturaBtnDerecha =  manager.get("Botones/MovDerButton.png");
        texturaBtnSaltar = manager.get("Botones/MovUpButton.png");

    }

    @Override
    public void render(float delta) {
        actualizarCamara();
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);

        robot.actualizar(mapa);
        if (vidasVIU==4){
            estado= EstadoJuego.GANADO;
            if(escenaGanaste==null){
                escenaGanaste= new EscenaGanaste(vistaHUD, batch);
            }
            Gdx.input.setInputProcessor(escenaGanaste);
        }
        if(estado== EstadoJuego.GANADO){
            escenaGanaste.draw();
        }

        if(vidasVIU==0){
            estado= EstadoJuego.PIERDE;
            if(escenaPerdiste==null) {
                escenaPerdiste = new EscenaPerdiste(vistaHUD, batch);
            }
            Gdx.input.setInputProcessor(escenaPerdiste);

        }
        if(estado==EstadoJuego.PIERDE){
            escenaPerdiste.draw();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            //estado = estado == EstadoJuego.JUGANDO ? EstadoJuego.PAUSADO: EstadoJuego.JUGANDO;
            if(estado== EstadoJuego.JUGANDO){
                estado= EstadoJuego.PAUSADO;
            }
            if (estado == EstadoJuego.PAUSADO) {
                // Activar escenaPausa y pasarle el control
                if (escenaPausa == null) {
                    escenaPausa = new EscenaPausa(vistaHUD, batch);
                }
                Gdx.input.setInputProcessor(escenaPausa);
            } else if(estado== EstadoJuego.PIERDE||estado== EstadoJuego.GANADO){
                //ESTADO CUANDO EL JUGADOR HA PERDIDO O GANANDO
                musicaFondo.stop();
                juego.setScreen(new PantallaCarga(juego,Pantallas.SELECCION_NIVEL,musicaFondo, EstadoMusica.DENIDO,estadoMusicaGeneral));

            }else {
                // Continuar el juego
                estado = EstadoJuego.JUGANDO;
                // Regresa el control a la pantalla
                Gdx.input.setInputProcessor(escenaHUD);
            }

        }

        if (estado == EstadoJuego.PAUSADO) {

            escenaPausa.draw();
        } else if (estado== EstadoJuego.JUGANDO){

            world.step(delta,6,2);

            renderarMapa.setView(camara);
            renderarMapa.render();
            //mostrar vidas restantes
            mostrarVidas(vidasVIU);
            escenaVidasVIU.draw();

            //escenaNivelTutorial.draw();
            batch.begin();
            plat1.dibujar(batch);
            plat1.mover(30, 500, 30, 600);
            //para mostrar el puntaje de mini vis
            texto.mostrarMensaje(batch, Integer.toString(contadorMiniVis), camara.position.x+ANCHO/2-50, camara.position.y+ALTO/2-40);
            robot.dibujar(batch);
            batch.end();


            batch.setProjectionMatrix(camaraHUD.combined);
            escenaHUD.draw();
        }

    }

    private void mostrarVidas(int vidas) {
        escenaVidasVIU.addActor(listaVidasVIU.get(0));
        escenaVidasVIU.dispose();
        for(int i=0; i<vidas;i++){
            escenaVidasVIU.addActor(listaVidasVIU.get(i));
        }


    }

    private void actualizarCamara(){
        float posX = robot.sprite.getX();
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
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    /*@Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if(screenX<=400 && screenY>ALTO/4){
            System.out.println("Click izquierdo");
            robot.setEstadoMovimiento(Robot.EstadoMovimiento.MOV_IZQUIERDA);
        } else if(screenX>400 && screenY>ALTO/4){
            System.out.println("Click derecho");
            robot.setEstadoMovimiento((Robot.EstadoMovimiento.MOV_DERECHA));
        } else{
            System.out.println("Click salto");
            if(robot.getEstadoSalto()==Robot.EstadoSalto.EN_PISO){
                robot.setEstadoSalto(Robot.EstadoSalto.SUBIENDO);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        robot.frenar();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }*/
    //ESCENA QUE SE MUESTRA CUANDO NO TIENES VIDAS

    // La escena que se muestra cuando el juego se pausa
    private class EscenaPausa extends Stage
    {
        public EscenaPausa(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            Texture texturaRectangulo = manager.get("Fondos/PausaFondo.jpg");
            Image imgRectangulo = new Image(texturaRectangulo);
            this.addActor(imgRectangulo);

            //SELECCION DE NIVEL
            Texture texturaBtnSelecNivel= manager.get("Botones/PausaButtonSeleccionarNivel.png");
            TextureRegionDrawable trdSeleccion= new TextureRegionDrawable(new TextureRegion(texturaBtnSelecNivel));
            ImageButton btnSeleccionar= new ImageButton(trdSeleccion);
            btnSeleccionar.setPosition(ANCHO/2-btnSeleccionar.getWidth()/2+66/*100*/,1*ALTO/3-80/*40*/);
            btnSeleccionar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    // Regresa a la seleccion de nivel
                    musicaFondo.stop();
                    juego.setScreen(new PantallaCarga(juego,Pantallas.SELECCION_NIVEL,musicaFondo, EstadoMusica.DENIDO,estadoMusicaGeneral));
                }
            });
            this.addActor(btnSeleccionar);

            // Salir
            Texture texturaBtnSalir = manager.get("Botones/PausaButtonMenuPrin.png");
            TextureRegionDrawable trdSalir = new TextureRegionDrawable(
                    new TextureRegion(texturaBtnSalir));
            ImageButton btnSalir = new ImageButton(trdSalir);
            btnSalir.setPosition(ANCHO-btnSalir.getWidth()-10/*20*/,10/*20*/);
            btnSalir.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Regresa al menú
                    musicaFondo.stop();
                    juego.setScreen(new PantallaCarga(juego,Pantallas.MENU,musicaFondo, EstadoMusica.DENIDO,estadoMusicaGeneral));
                }
            });
            this.addActor(btnSalir);

            // Continuar
            Texture texturabtnReanudar = manager.get("Botones/PausaButtonReanudar.png");
            TextureRegionDrawable trdReintentar = new TextureRegionDrawable(
                    new TextureRegion(texturabtnReanudar));
            ImageButton btnReanudar = new ImageButton(trdReintentar);
            btnReanudar.setPosition(ANCHO/2-btnReanudar.getWidth()/2+66/*100*/,ALTO/2-50/*50*/);

            btnReanudar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Continuar el juego
                    estado = EstadoJuego.JUGANDO;
                    // Regresa el control a la pantalla
                    Gdx.input.setInputProcessor(escenaHUD);
                }
            });
            this.addActor(btnReanudar);

            //TEXTO DE PAUSA
            Texture texturaTxtPausa = manager.get("Textos/PausaTextTittle.png");
            Image imgTxtPausa= new Image(texturaTxtPausa);
            imgTxtPausa.setPosition(ANCHO/2-imgTxtPausa.getWidth()/2+66/*100*/,5*ALTO/6);
            this.addActor(imgTxtPausa);
        }
    }

    private class EscenaPerdiste extends Stage {
        public EscenaPerdiste(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            Texture texturaRectangulo = manager.get("Fondos/PausaFondo.jpg");
            Image imgRectangulo = new Image(texturaRectangulo);
            this.addActor(imgRectangulo);

            //SELECCION DE NIVEL
            Texture texturaBtnSelecNivel = manager.get("Botones/PausaButtonSeleccionarNivel.png");
            TextureRegionDrawable trdSeleccion = new TextureRegionDrawable(new TextureRegion(texturaBtnSelecNivel));
            ImageButton btnSeleccionar = new ImageButton(trdSeleccion);
            btnSeleccionar.setPosition(ANCHO / 2 - btnSeleccionar.getWidth() / 2 + 66/*100*/, 1 * ALTO / 3 - 80/*40*/);
            btnSeleccionar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("clicked", "CLICK SELECCION NIVEL PERDISTE");
                    // Regresa a la seleccion de nivel
                    musicaFondo.stop();
                    juego.setScreen(new PantallaCarga(juego, Pantallas.SELECCION_NIVEL, musicaFondo, EstadoMusica.DENIDO, estadoMusicaGeneral));
                }
            });
            this.addActor(btnSeleccionar);

            // Salir
            Texture texturaBtnSalir = manager.get("Botones/PausaButtonMenuPrin.png");
            TextureRegionDrawable trdSalir = new TextureRegionDrawable(
                    new TextureRegion(texturaBtnSalir));
            ImageButton btnSalir = new ImageButton(trdSalir);
            btnSalir.setPosition(10, 10);
            btnSalir.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("clicked", "CLICK SALIR PERDISTE");
                    // Regresa al menú
                    musicaFondo.stop();
                    juego.setScreen(new PantallaCarga(juego, Pantallas.MENU, musicaFondo, EstadoMusica.DENIDO, estadoMusicaGeneral));
                }
            });
            this.addActor(btnSalir);

            // REINTENTAR
            Texture texturabtnReanudar = manager.get("Botones/PerdisteBtnReintentar.png");
            TextureRegionDrawable trdReintentar = new TextureRegionDrawable(
                    new TextureRegion(texturabtnReanudar));
            ImageButton btnReanudar = new ImageButton(trdReintentar);
            btnReanudar.setPosition(ANCHO / 2 - btnReanudar.getWidth() / 2 + 66/*100*/, ALTO / 2 - 90/*50*/);

            btnReanudar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("clicked", "CLICK REINTENTAR PERDISTE");
                    // REINTENTAR NIVEL
                    estado = EstadoJuego.JUGANDO;
                    musicaFondo.stop();
                    // Regresa el control a la pantalla
                    //Gdx.input.setInputProcessor(escenaNivelTutorial);
                    juego.setScreen(new PantallaCarga(juego, Pantallas.NIVEL, musicaFondo, EstadoMusica.REPRODUCCION,estadoMusicaGeneral));
                }
            });
            this.addActor(btnReanudar);

            //TEXTO DE QUE PERDIO
            Texture texturaTxtPausa = manager.get("Textos/Perdiste.png");
            Image imgTxtPausa = new Image(texturaTxtPausa);
            imgTxtPausa.setPosition(ANCHO / 2 - imgTxtPausa.getWidth() / 2 + 66/*100*/, 5 * ALTO / 6-100);
            this.addActor(imgTxtPausa);

        }
    }

    private class EscenaGanaste extends Stage{
        public EscenaGanaste(Viewport vista, SpriteBatch batch){
            super(vista, batch);
            Texture texturaRectangulo = manager.get("Fondos/PausaFondo.jpg");
            Image imgRectangulo = new Image(texturaRectangulo);
            this.addActor(imgRectangulo);

            //SELECCION DE NIVEL
            Texture texturaBtnSelecNivel = manager.get("Botones/PausaButtonSeleccionarNivel.png");
            TextureRegionDrawable trdSeleccion = new TextureRegionDrawable(new TextureRegion(texturaBtnSelecNivel));
            ImageButton btnSeleccionar = new ImageButton(trdSeleccion);
            btnSeleccionar.setPosition(ANCHO / 2 - btnSeleccionar.getWidth() / 2 + 66/*100*/, ALTO/2);
            btnSeleccionar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("clicked", "CLICK SELECCION NIVEL PERDISTE");
                    // Regresa a la seleccion de nivel
                    musicaFondo.stop();
                    juego.setScreen(new PantallaCarga(juego, Pantallas.SELECCION_NIVEL, musicaFondo, EstadoMusica.DENIDO, estadoMusicaGeneral));
                }
            });
            this.addActor(btnSeleccionar);

            // Salir
            Texture texturaBtnSalir = manager.get("Botones/PausaButtonMenuPrin.png");
            TextureRegionDrawable trdSalir = new TextureRegionDrawable(
                    new TextureRegion(texturaBtnSalir));
            ImageButton btnSalir = new ImageButton(trdSalir);
            btnSalir.setPosition(10, 10);
            btnSalir.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("clicked", "CLICK SALIR PERDISTE");
                    // Regresa al menú
                    musicaFondo.stop();
                    juego.setScreen(new PantallaCarga(juego, Pantallas.MENU, musicaFondo, EstadoMusica.DENIDO, estadoMusicaGeneral));
                }
            });
            this.addActor(btnSalir);

            //TEXTO DE QUE GANO
            Texture texturaTxtPausa = manager.get("Textos/Perdiste.png");
            Image imgTxtPausa = new Image(texturaTxtPausa);
            imgTxtPausa.setPosition(ANCHO / 2 - imgTxtPausa.getWidth() / 2 + 66/*100*/, 5 * ALTO / 6-100);
            this.addActor(imgTxtPausa);



        }

    }
}
