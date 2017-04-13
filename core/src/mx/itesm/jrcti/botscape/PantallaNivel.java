package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
    public Sound sonidoGanaste;
    public Sound sonidoPerdiste;
    private Texture texturaMiniVI;
    private Texture texturaVidasVIU;
    private Camera camaraHUD;
    private Stage escenaVidasVIU;

    private String nombreMapa;
    private String nombreMusicaFondo;
    private int TtoP = 64;



    private ArrayList<Image> listaVidasVIU = new ArrayList<Image>();
    int altoVidasVIU;


    public PantallaNivel(Juego j,EstadoMusica estadoMusicaGeneral, String nombreMapa, String nombreMusica, EstadoSonido estadoSonidoGeneral){
        super();
        this.juego=j;
        this.estadoMusicaGeneral= estadoMusicaGeneral;
        this.manager = j.getAssetManager();
        this.nombreMapa = nombreMapa;
        this.nombreMusicaFondo = nombreMusica;
        this.estadoSonidoGeneral= estadoSonidoGeneral;

        //CARGANDO LOS SONIDOS
        sonidoGanaste= Gdx.audio.newSound(Gdx.files.internal("Sonidos/Sound Effects/Win/collect_item_14.mp3"));
        sonidoPerdiste= Gdx.audio.newSound(Gdx.files.internal("Sonidos/Sound Effects/Lose/Impacts and Hits - Sub Zero 02.mp3"));
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


    public void leerMapa(){
        MapProperties prop = mapa.getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        TiledMapTileLayer capaArq = (TiledMapTileLayer) mapa.getLayers().get(0);
        int count = 0;
        boolean flagPrimero = true;
        int xInicial = 0;
        int yInicial = 0;
        Body bodyPiso;
        BodyDef bodyDefPiso = new BodyDef();
        FixtureDef fixPiso = new FixtureDef();
        PolygonShape cuadro;
        bodyDefPiso.type = BodyDef.BodyType.StaticBody;
        fixPiso.friction = 0.01f;
        fixPiso.density = 1f;

        //Revisar horizontal con tipo piso y techo
        for(int fila = 0; fila < mapHeight; fila++){
            for(int col = 0; col < mapWidth; col++) {
                //Gdx.app.log("Recorro","fila " + fila + " columna " + col);
                if (capaArq.getCell(col, fila) != null) {
                    //Gdx.app.log("Y ademas","Dije que no era null");
                    Object body = capaArq.getCell(col, fila).getTile().getProperties().get("body");
                    if (body != null && (body.equals("piso") || body.equals("techo"))) {
                        //Gdx.app.log("Leer mapa: ","Detecto piso en " + fila + ", " + col);
                        if (flagPrimero) {
                            count = 0;
                            xInicial = col * TtoP;
                            Gdx.app.log("Leer mapa:","Creo un body en fila " + fila + " columna " + col + " con xInicial en " + xInicial);
                            flagPrimero = false;
                            count++;
                        } else {
                            //Gdx.app.log("Leer mapa","Detecte piso en fila " + fila + " columna " +col);
                            count++;
                        }
                    } else if(!flagPrimero) {
                        Gdx.app.log("Deje de detecar piso en ","fila " + fila + " columna " + col + " con count en " + count);
                        flagPrimero = true;
                        cuadro = new PolygonShape();
                        if(col == 0) {
                            bodyDefPiso.position.set((xInicial + (TtoP * count) / 2) / getPtM(), (((fila-1) * TtoP) + 32) / getPtM());
                        } else{
                            bodyDefPiso.position.set((xInicial + (TtoP * count) / 2) / getPtM(), ((fila * TtoP) + 32) / getPtM());
                        }
                        cuadro.setAsBox((TtoP*count/2)/getPtM(),32/getPtM());
                        fixPiso.shape = cuadro;
                        bodyPiso = world.createBody(bodyDefPiso);
                        bodyPiso.createFixture(fixPiso);
                        bodyPiso.setUserData("piso");
                    }
                } else if (!flagPrimero){
                    Gdx.app.log("Deje de detecar piso en ","fila " + fila + " columna " + col + " con count en " + count);
                    flagPrimero = true;
                    cuadro = new PolygonShape();
                    if(col == 0) {
                        bodyDefPiso.position.set((xInicial + (TtoP * count) / 2) / getPtM(), (((fila-1) * TtoP) + 32) / getPtM());
                    } else{
                        bodyDefPiso.position.set((xInicial + (TtoP * count) / 2) / getPtM(), ((fila * TtoP) + 32) / getPtM());
                    }
                    cuadro.setAsBox((TtoP*count/2)/getPtM(),32/getPtM());
                    fixPiso.shape = cuadro;
                    bodyPiso = world.createBody(bodyDefPiso);
                    bodyPiso.createFixture(fixPiso);
                    bodyPiso.setUserData("piso");
                }
            }
        }

        if(!flagPrimero && count != 0){
            flagPrimero = true;
            cuadro = new PolygonShape();
            bodyDefPiso.position.set((xInicial + (TtoP * count) / 2) / getPtM(), (((mapHeight-1) * TtoP) + 32) / getPtM());
            cuadro.setAsBox((TtoP*count/2)/getPtM(),32/getPtM());
            fixPiso.shape = cuadro;
            bodyPiso = world.createBody(bodyDefPiso);
            bodyPiso.createFixture(fixPiso);
            bodyPiso.setUserData("piso");
        }

        flagPrimero = true;
        count = 0;

        for(int col = 0; col < mapWidth; col++){
            for(int fila = 0; fila < mapHeight; fila++) {
                //Gdx.app.log("Recorro","columna " + col + " fila " + fila);
                if (capaArq.getCell(col, fila) != null) {
                    //Gdx.app.log("Y ademas","Dije que no era null");
                    Object body = capaArq.getCell(col, fila).getTile().getProperties().get("body");
                    if (body != null && body.equals("columna")) {
                        //Gdx.app.log("Leer mapa: ","Detecto columna en  columna" + col + ", fila " + fila);
                        if (flagPrimero) {
                            count = 0;
                            yInicial = fila * TtoP;
                            //Gdx.app.log("Leer mapa:","Creo un body columna en fila " + fila + " columna " + col + " con yInicial en " + yInicial);
                            flagPrimero = false;
                            count++;
                        } else {
                            //Gdx.app.log("Leer mapa","Detecte piso en fila " + fila + " columna " +col);
                            count++;
                        }
                    } else if(!flagPrimero) {
                        //Gdx.app.log("Deje de detecar piso en ","fila " + fila + " columna " + col + " con count en " + count);
                        flagPrimero = true;
                        cuadro = new PolygonShape();
                        if(fila == 0) {
                            bodyDefPiso.position.set(((col-1)*TtoP+TtoP/2)/getPtM(), (yInicial+(TtoP*count)/2)/getPtM());
                        } else{
                            bodyDefPiso.position.set(((col)*TtoP+TtoP/2)/getPtM(), (yInicial+(TtoP*count)/2)/getPtM());
                        }
                        cuadro.setAsBox(32/getPtM(),(TtoP*count/2)/getPtM());
                        fixPiso.shape = cuadro;
                        bodyPiso = world.createBody(bodyDefPiso);
                        bodyPiso.createFixture(fixPiso);
                        bodyPiso.setUserData("columna");
                    }
                } else if (!flagPrimero){
                    //Gdx.app.log("Deje de detecar piso en ","fila " + fila + " columna " + col + " con count en " + count);
                    flagPrimero = true;
                    cuadro = new PolygonShape();
                    if(fila == 0) {
                        bodyDefPiso.position.set(((col-1)*TtoP+TtoP/2)/getPtM(), (yInicial+(TtoP*count)/2)/getPtM());
                    } else{
                        bodyDefPiso.position.set(((col)*TtoP+TtoP/2)/getPtM(), (yInicial+(TtoP*count)/2)/getPtM());
                    }
                    cuadro.setAsBox(32/getPtM(),(TtoP*count/2)/getPtM());
                    fixPiso.shape = cuadro;
                    bodyPiso = world.createBody(bodyDefPiso);
                    bodyPiso.createFixture(fixPiso);
                    bodyPiso.setUserData("columna");
                }
            }
        }

        if(!flagPrimero &&  count != 0){
            flagPrimero = true;
            cuadro = new PolygonShape();
            bodyDefPiso.position.set(((mapWidth-1)*TtoP+TtoP/2)/getPtM(), (yInicial+(TtoP*count)/2)/getPtM());
            cuadro.setAsBox(32/getPtM(),(TtoP*count/2)/getPtM());
            fixPiso.shape = cuadro;
            bodyPiso = world.createBody(bodyDefPiso);
            bodyPiso.createFixture(fixPiso);
            bodyPiso.setUserData("columna");
        }
        count = 0;


    }

    protected void crearRobot(int x, int y) {
        fix = new FixtureDef();
        fix.density=.3f;
        fix.restitution=.1f;
        fix.friction=10f;
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

                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }

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
            Texture texturaBtnSelecNivel = manager.get("Botones/PausaButtonSeleccionarNivel.png");
            TextureRegionDrawable trdSeleccion = new TextureRegionDrawable(new TextureRegion(texturaBtnSelecNivel));
            ImageButton btnSeleccionar = new ImageButton(trdSeleccion);
            btnSeleccionar.setPosition(ANCHO / 2 - btnSeleccionar.getWidth() / 2 + 66, 1 * ALTO / 3 - 80);
            btnSeleccionar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    // Regresa a la seleccion de nivel
                    musicaFondo.stop();
                    manager.unload(nombreMapa);
                    if (estadoSonidoGeneral == EstadoSonido.ENCENDIDO) {
                        sonidoBoton.play(volumenSonido);
                    }
                    juego.setScreen(new PantallaCarga(juego, Pantallas.SELECCION_NIVEL, musicaFondo, EstadoMusica.DENIDO, estadoMusicaGeneral, estadoSonidoGeneral));
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
                    // Regresa al menú
                    musicaFondo.stop();
                    manager.unload(nombreMapa);
                    if (estadoSonidoGeneral == EstadoSonido.ENCENDIDO) {
                        sonidoBoton.play(volumenSonido);
                    }
                    juego.setScreen(new PantallaCarga(juego, Pantallas.MENU, musicaFondo, EstadoMusica.DENIDO, estadoMusicaGeneral, estadoSonidoGeneral));
                }
            });
            this.addActor(btnSalir);

            // Continuar
            Texture texturabtnReanudar = manager.get("Botones/PausaButtonReanudar.png");
            TextureRegionDrawable trdReintentar = new TextureRegionDrawable(
                    new TextureRegion(texturabtnReanudar));
            ImageButton btnReanudar = new ImageButton(trdReintentar);
            btnReanudar.setPosition(ANCHO / 2 - btnReanudar.getWidth() / 2 + 66, ALTO / 2 - 50);

            btnReanudar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Continuar el juego
                    estado = EstadoJuego.JUGANDO;
                    // Regresa el control a la pantalla
                    if (estadoSonidoGeneral == EstadoSonido.ENCENDIDO) {
                        sonidoBoton.play(volumenSonido);
                    }
                    Gdx.input.setInputProcessor(escenaHUD);
                }
            });
            this.addActor(btnReanudar);

            //TEXTO DE PAUSA
            Texture texturaTxtPausa = manager.get("Textos/PausaTextTittle.png");
            Image imgTxtPausa = new Image(texturaTxtPausa);
            imgTxtPausa.setPosition(ANCHO / 2 - imgTxtPausa.getWidth() / 2 + 66, 5 * ALTO / 6);
            this.addActor(imgTxtPausa);

            //BOTONES PARA ACTIVAR Y DESACTIVAR SONIDOS Y MUSICA

            //BOTON DE LA MUSICA
            Texture texturaBtnMusicOn = manager.get("Botones/MovUpButton.png");
            //FALTA EL DE OFF
            TextureRegionDrawable trdBtnMusicOn = new TextureRegionDrawable(new TextureRegion(texturaBtnMusicOn));

            //AQUI FALTA EL CODIGO PARA DECIDIR CON QUE IMAGEN VA INICIAR EL BOTON
            final ImageButton btnMusic= new ImageButton(trdBtnMusicOn);
            btnMusic.setPosition(ANCHO-2*btnMusic.getMinWidth(),10);
            this.addActor(btnMusic);

            btnMusic.addListener(new ClickListener(){
                public void clicked(InputEvent event, float x, float y){
                    Gdx.app.log("Aviso", "POS ME VOY AL MENU PRINCIPAL");
                    //AQUI VA EL CODIO PARA DESCATIVAR LA MUSICA
                    if(estadoMusicaGeneral==EstadoMusica.APAGADO){
                        estadoMusicaGeneral= EstadoMusica.REPRODUCCION;
                        //btnMusic.getStyle().imageUp= trdButtonMusica;

                        Gdx.app.log("Aviso", estadoMusicaGeneral.toString());



                        musicaFondo.play();
                    }else{
                        estadoMusicaGeneral= EstadoMusica.APAGADO;
                        Gdx.app.log("Aviso", estadoMusicaGeneral.toString());
                        //buttonMusica.getStyle().imageUp= trdButtonMusicaOff;
                        musicaFondo.stop();
                    }
                    //SONIDO DE OPRIMIR ESTE BOTON
                    if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                        sonidoBoton.play(volumenSonido);
                    }

                }
            });

            //BOTON DE LOS SONIDOS
            Texture texturaBtnSoundOn = manager.get("Botones/MovIzqButton.png");
            //FALTA EL DE OFF
            TextureRegionDrawable trdBtnSoundOn = new TextureRegionDrawable(new TextureRegion(texturaBtnSoundOn));
            //AQUI FALTA EL CODIGO PARA DECIDIR CON QUE IMAGEN VA INICIAR EL BOTON
            ImageButton btnSound= new ImageButton(trdBtnSoundOn);
            btnSound.setPosition(ANCHO-btnSound.getWidth(),10);
            this.addActor(btnSound);

            btnSound.addListener(new ClickListener(){
                public void clicked(InputEvent event, float x, float y){
                    Gdx.app.log("Aviso", "POS ME VOY AL MENU PRINCIPAL");
                    if(estadoSonidoGeneral==EstadoSonido.APAGADO){

                        estadoSonidoGeneral= EstadoSonido.ENCENDIDO;
                        //buttonSonido.getStyle().imageUp= trdButtonSoundOn;


                        Gdx.app.log("Aviso", estadoSonidoGeneral.toString());

                        sonidoBoton.play(volumenSonido);
                    }else{
                        estadoSonidoGeneral= EstadoSonido.APAGADO;
                        //buttonSonido.getStyle().imageUp= trdButtonSoundOff;
                    }

                }
            });




        }
    }

    protected class EscenaPerdiste extends Stage {
        public EscenaPerdiste(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            Texture texturaRectangulo = manager.get("Fondos/PantallaPerdiste.jpg");
            Image imgRectangulo = new Image(texturaRectangulo);
            this.addActor(imgRectangulo);

            //SELECCION DE NIVEL
            Texture texturaBtnSelecNivel = manager.get("Botones/PausaButtonSeleccionarNivel.png");
            TextureRegionDrawable trdSeleccion = new TextureRegionDrawable(new TextureRegion(texturaBtnSelecNivel));
            ImageButton btnSeleccionar = new ImageButton(trdSeleccion);
            btnSeleccionar.setPosition(3* ANCHO /5, ALTO / 10 );
            btnSeleccionar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("clicked", "CLICK SELECCION NIVEL PERDISTE");
                    // Regresa a la seleccion de nivel
                    musicaFondo.stop();
                    manager.unload(nombreMapa);
                    if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                        sonidoBoton.play(volumenSonido);
                    }
                    juego.setScreen(new PantallaCarga(juego, Pantallas.SELECCION_NIVEL, musicaFondo, EstadoMusica.DENIDO, estadoMusicaGeneral,estadoSonidoGeneral));
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
                    if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                        sonidoBoton.play(volumenSonido);
                    }
                    juego.setScreen(new PantallaCarga(juego, Pantallas.MENU, musicaFondo, EstadoMusica.DENIDO, estadoMusicaGeneral,estadoSonidoGeneral));
                }
            });
            this.addActor(btnSalir);

            // REINTENTAR
            Texture texturabtnReintentar = manager.get("Botones/PantallaRetry.png");
            TextureRegionDrawable trdReintentar = new TextureRegionDrawable(
                    new TextureRegion(texturabtnReintentar));
            ImageButton btnReanudar = new ImageButton(trdReintentar);
            btnReanudar.setPosition(3* ANCHO / 4 , ALTO / 2 - 90);

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
                    if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                        sonidoBoton.play(volumenSonido);
                    }
                    juego.setScreen(new PantallaCarga(juego, Pantallas.NIVEL, musicaFondo, EstadoMusica.REPRODUCCION,estadoMusicaGeneral,estadoSonidoGeneral));
                }
            });
            this.addActor(btnReanudar);

            //TEXTO DE QUE PERDIO
            Texture texturaTxtPausa = manager.get("Textos/Perdiste.png");
            Image imgTxtPausa = new Image(texturaTxtPausa);
            imgTxtPausa.setPosition(ANCHO / 2, 5 * ALTO / 6-100);
            this.addActor(imgTxtPausa);

            if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                sonidoPerdiste.play(volumenSonido);
            }

        }
    }

    protected class EscenaGanaste extends Stage{
        public EscenaGanaste(Viewport vista, SpriteBatch batch){
            super(vista, batch);
            Texture texturaRectangulo = manager.get("Fondos/PantallaGanaste.jpg");
            Image imgRectangulo = new Image(texturaRectangulo);
            this.addActor(imgRectangulo);



            // REINTENTAR
            Texture texturabtnReintentar = manager.get("Botones/PantallaRetry.png");
            TextureRegionDrawable trdReintentar = new TextureRegionDrawable(
                    new TextureRegion(texturabtnReintentar));
            ImageButton btnReintentar = new ImageButton(trdReintentar);
            btnReintentar.setPosition(ANCHO / 2, ALTO / 2 - btnReintentar.getHeight());

            btnReintentar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("clicked", "CLICK REINTENTAR PERDISTE");
                    // REINTENTAR NIVEL
                    estado = EstadoJuego.JUGANDO;
                    musicaFondo.stop();
                    manager.unload(nombreMapa);
                    // Regresa el control a la pantalla
                    //Gdx.input.setInputProcessor(escenaNivelTutorial);
                    if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                        sonidoBoton.play(volumenSonido);
                    }
                    juego.setScreen(new PantallaCarga(juego, Pantallas.NIVEL, musicaFondo, EstadoMusica.REPRODUCCION,estadoMusicaGeneral,estadoSonidoGeneral));
                }
            });
            this.addActor(btnReintentar);
            //BOTON SIGUIENTE NIVEL

            Texture texturaBtnNextLevel = manager.get("Botones/PantallaNextLevel.png");
            TextureRegionDrawable trdNextLevel = new TextureRegionDrawable(new TextureRegion(texturaBtnNextLevel));
            ImageButton btnNextLevel = new ImageButton(trdNextLevel);
            btnNextLevel.setPosition(3*ANCHO / 4 , ALTO/2-btnNextLevel.getHeight());
            btnNextLevel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("clicked", "CLICK SELECCION NIVEL PERDISTE");
                    // Regresa a la seleccion de nivel
                    musicaFondo.stop();
                    manager.unload(nombreMapa);
                    if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                        sonidoBoton.play(volumenSonido);
                    }
                    juego.setScreen(new PantallaCarga(juego, Pantallas.SELECCION_NIVEL, musicaFondo, EstadoMusica.DENIDO, estadoMusicaGeneral,estadoSonidoGeneral));
                }
            });
            this.addActor(btnNextLevel);

            //SELECCION DE NIVEL
            Texture texturaBtnSelecNivel = manager.get("Botones/PausaButtonSeleccionarNivel.png");
            TextureRegionDrawable trdSeleccion = new TextureRegionDrawable(new TextureRegion(texturaBtnSelecNivel));
            ImageButton btnSeleccionar = new ImageButton(trdSeleccion);
            btnSeleccionar.setPosition(ANCHO / 2+60 , ALTO/32);
            btnSeleccionar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("clicked", "CLICK SELECCION NIVEL PERDISTE");
                    // Regresa a la seleccion de nivel
                    musicaFondo.stop();
                    manager.unload(nombreMapa);
                    if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                        sonidoBoton.play(volumenSonido);
                    }
                    juego.setScreen(new PantallaCarga(juego, Pantallas.SELECCION_NIVEL, musicaFondo, EstadoMusica.DENIDO, estadoMusicaGeneral,estadoSonidoGeneral));
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
                    if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                        sonidoBoton.play(volumenSonido);
                    }
                    juego.setScreen(new PantallaCarga(juego, Pantallas.MENU, musicaFondo, EstadoMusica.DENIDO, estadoMusicaGeneral,estadoSonidoGeneral));
                }
            });
            this.addActor(btnSalir);

            //DIBUJANDO LAS ESTRELLAS
            Texture texturaEstrella= manager.get("PantallaEstrella.png");
            DibujaEstrellas(texturaEstrella,3);


            //TEXTO DE QUE GANO
            Texture texturaTxtPausa = manager.get("Textos/TextoGanaste.png");
            Image imgTxtPausa = new Image(texturaTxtPausa);
            imgTxtPausa.setPosition(ANCHO / 2 , 5 * ALTO / 6);
            this.addActor(imgTxtPausa);

            if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                sonidoGanaste.play(volumenSonido);
            }



        }
        private void DibujaEstrellas(Texture textureEstrella, int num){

            float ancho= ANCHO/2;
            float alto= ALTO /2 +30;
            Image imgEstrella1= new Image(textureEstrella);
            imgEstrella1.setPosition(ancho,alto);
            Image imgEstrella2= new Image(textureEstrella);
            imgEstrella2.setPosition(imgEstrella1.getX()+ imgEstrella2.getWidth(),alto);
            Image imgEstrella3= new Image(textureEstrella);
            imgEstrella3.setPosition(imgEstrella2.getX()+imgEstrella3.getWidth(),alto);
            ArrayList <Image> listaEstrellas= new ArrayList<Image>();
            listaEstrellas.add(imgEstrella1);
            listaEstrellas.add(imgEstrella2);
            listaEstrellas.add(imgEstrella3);

            for (int i=0; i<num; i++){
                this.addActor(listaEstrellas.get(i));
            }
        }

    }


    public abstract void cargarTexturasExtras();

}
