package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by Julio on 31/03/2017.
 */

public abstract class PantallaNivel extends Pantalla {

    private static final float PIXELS_TO_METERS = 64f;
    private EstadoJuego estado = EstadoJuego.JUGANDO;
    private Robot robot;
    private FixtureDef fix;
    private Texture VIUWalk_Cycle;
    private World world;
    private OrthogonalTiledMapRenderer renderarMapa;
    private SpriteBatch batch;
    private TiledMap mapa;
    private Texture texturaBtnPausa;
    private Texture texturaBtnIzquierda;
    private Texture texturaBtnDerecha;
    private Texture texturaBtnSaltar;
    private Stage escenaHUD;
    private EscenaPausa escenaPausa;
    private EscenaPerdiste escenaPerdiste;
    private EscenaGanaste escenaGanaste;
    private AssetManager manager;
    private StretchViewport vistaHUD;
    private int contadorMiniVis=0;
    private Texto texto= new Texto();
    private Music musicaFondo;
    private Texture texturaMiniVI;
    private Texture texturaVidasVIU;
    private Camera camaraHUD;
    private Stage escenaVidasVIU;

    private String nombreMapa;
    private String nombreMusicaFondo;



    private ArrayList<Image> listaVidasVIU = new ArrayList<Image>();
    int altoVidasVIU;


    public PantallaNivel(Juego j,EstadoMusica estadoMusicaGeneral, String nombreMapa, String nombreMusica){
        super();
        this.juego=j;
        this.estadoMusicaGeneral= estadoMusicaGeneral;
        this.manager = j.getAssetManager();
        this.nombreMapa = nombreMapa;
        this.nombreMusicaFondo = nombreMusica;
    }


    protected Robot getRobot(){
        return robot;
    }

    protected SpriteBatch getBatch(){
        return batch;
    }
    protected TiledMap getMapa(){
        return mapa;
    }
    protected World getWorld(){
        return world;
    }
    protected OrthogonalTiledMapRenderer getMapRenderer(){
        return renderarMapa;
    }

    protected Camera getCamaraHUD(){
        return camaraHUD;
    }

    protected Stage getEscenaHUD(){
        return escenaHUD;
    }

    protected StretchViewport getVistaHUD(){
        return vistaHUD;
    }

    protected EstadoJuego getEstadoJuego(){
        return estado;
    }

    protected EscenaGanaste getEscenaGanaste(){
        return escenaGanaste;
    }

    protected EscenaPausa getEscenaPausa(){
        return escenaPausa;
    }

    protected EscenaPerdiste getEscenaPerdiste(){
        return escenaPerdiste;
    }

    public static float getPtM(){
        return PIXELS_TO_METERS;
    }

    protected void setEstadoJuego(EstadoJuego estado){
        this.estado=estado;
    }

    protected void setEscenaGanaste (EscenaGanaste escenaGanaste){
        this.escenaGanaste=escenaGanaste;
    }

    protected void setEscenaPerdiste(EscenaPerdiste escenaPerdiste){
        this.escenaPerdiste=escenaPerdiste;
    }

    protected void setEscenaPausa(EscenaPausa escenaPausa){
        this.escenaPausa=escenaPausa;
    }


    public void leerMapa(TiledMap map){
        /*get cell(x,y)
        * if(is tipo.equals"piso" && x1!=0)
        *   puntox1=cell.pos.x
        * if(is tipo.equals"piso")
        *   count++;
        * else if(x1!=0){
        *   x2= x1 + count*64
        *   crear static body(x1,y1,x2,y1)*/
    }

    protected void crearRobot(int x, int y) {
        fix = new FixtureDef();
        fix.density=.1f;
        fix.restitution=0.1f;
        fix.friction=.3f;
        robot = new Robot(VIUWalk_Cycle,x,y,world, BodyDef.BodyType.DynamicBody,fix);
    }

    protected void crearMundo() {
        world = new World(new Vector2(0f,-5f), true);
    }

    protected void crearHUD() {
        camaraHUD = new OrthographicCamera(ANCHO, ALTO);
        camaraHUD.position.set(ANCHO / 2, ALTO / 2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);
        escenaVidasVIU = new Stage(vistaHUD, batch);
        escenaHUD = new Stage(vistaHUD);

        crearBotones();

        //CREANDO ICONO DE MINIVI Contador
        Image iconoMiniVi= new Image(texturaMiniVI);
        iconoMiniVi.setPosition(ANCHO-2*iconoMiniVi.getWidth()-35,ALTO-iconoMiniVi.getHeight()-20);
        escenaHUD.addActor(iconoMiniVi);

        Gdx.input.setInputProcessor(escenaHUD);

        altoVidasVIU= (int)ALTO-texturaMiniVI.getHeight()-10;
        for(int i=0;i<robot.getVidas();i++){
            Image vida = new Image(texturaVidasVIU);
            if(i!=0) {
                vida.setPosition(listaVidasVIU.get(i - 1).getX() + vida.getWidth() + 10, altoVidasVIU);
            } else{
                vida.setPosition(ANCHO/2,altoVidasVIU);
            }
            listaVidasVIU.add(vida);
            escenaHUD.addActor(listaVidasVIU.get(i));
        }


    }

    public void crearBotones(){
        dibujarBtnPausa(texturaBtnPausa, escenaHUD);
        dibujarBtnIzq(texturaBtnIzquierda, escenaHUD);
        dibujarBtnDer(texturaBtnDerecha, escenaHUD);
        dibujarBtnSalto(texturaBtnSaltar, escenaHUD);
    }

    protected void cargarMapa() {
        mapa = manager.get(nombreMapa);
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal(nombreMusicaFondo));
        musicaFondo.setLooping(true);
        if(estadoMusicaGeneral!= EstadoMusica.APAGADO) {
            musicaFondo.play();
        }
        //musicaFondo.play();

        batch = new SpriteBatch();
        renderarMapa = new OrthogonalTiledMapRenderer(mapa, batch);
        renderarMapa.setView(camara);
    }

    public void cargarTexturasEscenciales(){
        VIUWalk_Cycle = manager.get("Personaje/VIUWalk_Cycle.png");
        texturaMiniVI = manager.get("NivelMiniVI.png");
        texturaBtnPausa = manager.get("NivelPausa.png");
        texturaBtnIzquierda = manager.get("Botones/MovIzqButton.png");
        texturaBtnDerecha =  manager.get("Botones/MovDerButton.png");
        texturaBtnSaltar = manager.get("Botones/MovUpButton.png");
        texturaVidasVIU = manager.get("VidasVIU.png");
    }



    private void dibujarBtnSalto(Texture texturaBtnSaltar, Stage escenaHUD) {
        TextureRegionDrawable trdBtnMovSalto= new TextureRegionDrawable(new TextureRegion(texturaBtnSaltar));
        ImageButton btnMovSalto = new ImageButton(trdBtnMovSalto);
        btnMovSalto.setPosition(6*ANCHO/8,50);
        escenaHUD.addActor(btnMovSalto);
        btnMovSalto.addListener( new InputListener(){

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //Gdx.app.log("click", "Salto");
                if (robot.getEstadoSalto() == Robot.EstadoSalto.EN_PISO) {
                    robot.setEstadoSalto(Robot.EstadoSalto.SUBIENDO);
                }
                return true;
            }
        });
    }

    private void dibujarBtnDer(Texture texturaBtnDerecha, Stage escenaHUD) {
        TextureRegionDrawable trdBtnMovDerecha = new TextureRegionDrawable(new TextureRegion(texturaBtnDerecha));
        ImageButton btnMovDer = new ImageButton(trdBtnMovDerecha);
        btnMovDer.setPosition(ANCHO/4,50);
        escenaHUD.addActor(btnMovDer);
        btnMovDer.addListener( new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                //Gdx.app.log("Click","Mover derecha");
                robot.setEstadoMovimiento(Robot.EstadoMovimiento.MOV_DERECHA);
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                //Gdx.app.log("UnClick", "Mover derecha");
                robot.frenar();

            }
        });
    }

    private void dibujarBtnIzq(Texture texturaBtnIzquierda, Stage escenaHUD) {
        TextureRegionDrawable trdBtnMovIzquierda = new TextureRegionDrawable(new TextureRegion(texturaBtnIzquierda));
        ImageButton btnMovIzq = new ImageButton(trdBtnMovIzquierda);
        btnMovIzq.setPosition(ANCHO/8,50);
        escenaHUD.addActor(btnMovIzq);
        btnMovIzq.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                //Gdx.app.log("Click","Mover izquierda");
                robot.setEstadoMovimiento(Robot.EstadoMovimiento.MOV_IZQUIERDA);
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                //Gdx.app.log("UnClick", "Mover izquierda");
                robot.frenar();

            }
        });
    }

    private void dibujarBtnPausa(Texture texturaBtnPausa, Stage escenaHUD) {
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
    }


    // La escena que se muestra cuando el juego se pausa
    protected class EscenaPausa extends Stage
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
            btnSeleccionar.setPosition(ANCHO/2-btnSeleccionar.getWidth()/2+66,1*ALTO/3-80);
            btnSeleccionar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    // Regresa a la seleccion de nivel
                    musicaFondo.stop();
                    manager.unload(nombreMapa);
                    juego.setScreen(new PantallaCarga(juego,Pantallas.SELECCION_NIVEL,musicaFondo, EstadoMusica.DENIDO,estadoMusicaGeneral));
                }
            });
            this.addActor(btnSeleccionar);

            // Salir
            Texture texturaBtnSalir = manager.get("Botones/PausaButtonMenuPrin.png");
            TextureRegionDrawable trdSalir = new TextureRegionDrawable(
                    new TextureRegion(texturaBtnSalir));
            ImageButton btnSalir = new ImageButton(trdSalir);
            btnSalir.setPosition(ANCHO-btnSalir.getWidth()-10,10);
            btnSalir.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Regresa al menú
                    musicaFondo.stop();
                    manager.unload(nombreMapa);
                    juego.setScreen(new PantallaCarga(juego,Pantallas.MENU,musicaFondo, EstadoMusica.DENIDO,estadoMusicaGeneral));
                }
            });
            this.addActor(btnSalir);

            // Continuar
            Texture texturabtnReanudar = manager.get("Botones/PausaButtonReanudar.png");
            TextureRegionDrawable trdReintentar = new TextureRegionDrawable(
                    new TextureRegion(texturabtnReanudar));
            ImageButton btnReanudar = new ImageButton(trdReintentar);
            btnReanudar.setPosition(ANCHO/2-btnReanudar.getWidth()/2+66,ALTO/2-50);

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
            imgTxtPausa.setPosition(ANCHO/2-imgTxtPausa.getWidth()/2+66,5*ALTO/6);
            this.addActor(imgTxtPausa);
        }
    }

    protected class EscenaPerdiste extends Stage {
        public EscenaPerdiste(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            Texture texturaRectangulo = manager.get("Fondos/PausaFondo.jpg");
            Image imgRectangulo = new Image(texturaRectangulo);
            this.addActor(imgRectangulo);

            //SELECCION DE NIVEL
            Texture texturaBtnSelecNivel = manager.get("Botones/PausaButtonSeleccionarNivel.png");
            TextureRegionDrawable trdSeleccion = new TextureRegionDrawable(new TextureRegion(texturaBtnSelecNivel));
            ImageButton btnSeleccionar = new ImageButton(trdSeleccion);
            btnSeleccionar.setPosition(ANCHO / 2 - btnSeleccionar.getWidth() / 2 + 66, 1 * ALTO / 3 - 80);
            btnSeleccionar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("clicked", "CLICK SELECCION NIVEL PERDISTE");
                    // Regresa a la seleccion de nivel
                    musicaFondo.stop();
                    manager.unload(nombreMapa);
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
                    manager.unload(nombreMapa);
                    juego.setScreen(new PantallaCarga(juego, Pantallas.MENU, musicaFondo, EstadoMusica.DENIDO, estadoMusicaGeneral));
                }
            });
            this.addActor(btnSalir);

            // REINTENTAR
            Texture texturabtnReanudar = manager.get("Botones/PerdisteBtnReintentar.png");
            TextureRegionDrawable trdReintentar = new TextureRegionDrawable(
                    new TextureRegion(texturabtnReanudar));
            ImageButton btnReanudar = new ImageButton(trdReintentar);
            btnReanudar.setPosition(ANCHO / 2 - btnReanudar.getWidth() / 2 + 66, ALTO / 2 - 90);

            btnReanudar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("clicked", "CLICK REINTENTAR PERDISTE");
                    // REINTENTAR NIVEL
                    estado = EstadoJuego.JUGANDO;
                    musicaFondo.stop();
                    manager.unload(nombreMapa);
                    // Regresa el control a la pantalla
                    //Gdx.input.setInputProcessor(escenaNivelTutorial);
                    juego.setScreen(new PantallaCarga(juego, Pantallas.NIVEL, musicaFondo, EstadoMusica.REPRODUCCION,estadoMusicaGeneral));
                }
            });
            this.addActor(btnReanudar);

            //TEXTO DE QUE PERDIO
            Texture texturaTxtPausa = manager.get("Textos/Perdiste.png");
            Image imgTxtPausa = new Image(texturaTxtPausa);
            imgTxtPausa.setPosition(ANCHO / 2 - imgTxtPausa.getWidth() / 2 + 66, 5 * ALTO / 6-100);
            this.addActor(imgTxtPausa);

        }
    }

    protected class EscenaGanaste extends Stage{
        public EscenaGanaste(Viewport vista, SpriteBatch batch){
            super(vista, batch);
            Texture texturaRectangulo = manager.get("Fondos/PausaFondo.jpg");
            Image imgRectangulo = new Image(texturaRectangulo);
            this.addActor(imgRectangulo);

            //SELECCION DE NIVEL
            Texture texturaBtnSelecNivel = manager.get("Botones/PausaButtonSeleccionarNivel.png");
            TextureRegionDrawable trdSeleccion = new TextureRegionDrawable(new TextureRegion(texturaBtnSelecNivel));
            ImageButton btnSeleccionar = new ImageButton(trdSeleccion);
            btnSeleccionar.setPosition(ANCHO / 2 - btnSeleccionar.getWidth() / 2 + 66, ALTO/2);
            btnSeleccionar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("clicked", "CLICK SELECCION NIVEL PERDISTE");
                    // Regresa a la seleccion de nivel
                    musicaFondo.stop();
                    manager.unload(nombreMapa);
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
                    manager.unload(nombreMapa);
                    juego.setScreen(new PantallaCarga(juego, Pantallas.MENU, musicaFondo, EstadoMusica.DENIDO, estadoMusicaGeneral));
                }
            });
            this.addActor(btnSalir);

            //TEXTO DE QUE GANO
            Texture texturaTxtPausa = manager.get("Textos/Ganaste.png");
            Image imgTxtPausa = new Image(texturaTxtPausa);
            imgTxtPausa.setPosition(ANCHO / 2 - imgTxtPausa.getWidth() / 2 + 66, 5 * ALTO / 6-100);
            this.addActor(imgTxtPausa);



        }

    }


    public abstract void cargarTexturasExtras();

}
