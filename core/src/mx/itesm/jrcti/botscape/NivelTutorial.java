package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Cinthya on 15/02/2017.
 */

public class NivelTutorial extends Pantalla{

    private OrthogonalTiledMapRenderer renderarMapa;
    private SpriteBatch batch;
    private TiledMap mapa;

    //Texturas
    private Texture texturaVIU;
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

    private Plataforma plat1;


    //Escenas
    private Stage escenaNivelTutorial;

    private final int NUM_PISO = 7;
    private Array<Image> arrPiso;

    private final int NUM_PISO_VERDE = 2;
    private Array<Image> arrPisoVerde;

    private final int NUM_PLAT = 4;
    private Array<Image> arrPlat;

    // Sonidos largos
    private Music musicaFondo;

    //Asset Manager
    private AssetManager manager;

    private EstadoJuego estado = EstadoJuego.JUGANDO;
    private EscenaPausa escenaPausa;
    private EstadoJuego estadoJuego;

    private int contadorMiniVis=1;
    private Texto texto= new Texto();


    public NivelTutorial(Juego j){
        super();
        this.juego=j;
        manager = j.getAssetManager();
    }

    @Override
    public void show() {
        cargarMapa();
        cargarTexturas();
        crearObjetos();
    }

    private void cargarMapa() {
        mapa = manager.get("Mapas/tutorialv2.tmx");
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("Sonidos/BringTheFoxhoundToMe.mp3"));
        musicaFondo.setLooping(true);
        musicaFondo.play();

        batch = new SpriteBatch();
        renderarMapa = new OrthogonalTiledMapRenderer(mapa, batch);
        renderarMapa.setView(camara);
    }

    private void crearObjetos(){

        escenaNivelTutorial = new Stage(vista,batch);
        /*
        Image imgFondo = new Image(texturaFondoTutorial);
        escenaNivelTutorial.addActor(imgFondo);


        arrPiso = new Array<Image>(NUM_PISO);
        for(int x=0; x<=NUM_PISO; x++){
            float posX = x * ANCHO/7-texturaPiso.getWidth()/2;
            Image imgPiso = new Image(texturaPiso);
            imgPiso.setPosition(posX,10);
            escenaNivelTutorial.addActor(imgPiso);
        }

        arrPiso = new Array<Image>(NUM_PISO);
        for(int x=0; x<NUM_PISO_VERDE; x++){
            float posX = x * ANCHO/7-texturaPiso.getWidth()/2;
            Image imgPisoVerde = new Image(texturaPisoVerde);
            imgPisoVerde.setPosition(posX+1700,700);
            escenaNivelTutorial.addActor(imgPisoVerde);
        }
*/
        plat1 = new Plataforma(texturaPlataforma, 3, 3, 30, 30, Plataforma.EstadoMovimiento.MOV_DERECHA);

        //CREANDO ICONO DE MINIVI

        Image iconoMiniVi= new Image(texturaMiniVI);
        iconoMiniVi.setPosition(ANCHO-2*iconoMiniVi.getWidth()-20,ALTO-iconoMiniVi.getHeight()-20);
        escenaNivelTutorial.addActor(iconoMiniVi);

        //Botón para ir al menu de pausa
        TextureRegionDrawable trdBtnPausa = new TextureRegionDrawable(new TextureRegion(texturaBtnPausa));
        ImageButton btnPausa = new ImageButton(trdBtnPausa);
        btnPausa.setPosition(10,ALTO-btnPausa.getHeight());
        escenaNivelTutorial.addActor(btnPausa);

        btnPausa.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked", "me hicieron CLICK");
                estado = estado==EstadoJuego.PAUSADO?EstadoJuego.JUGANDO:EstadoJuego.PAUSADO;
                if (estado==EstadoJuego.PAUSADO) {
                    // Activar escenaPausa y pasarle el control
                    if (escenaPausa==null) {
                        escenaPausa = new EscenaPausa(vista, batch);
                    }
                    Gdx.input.setInputProcessor(escenaPausa);
                }
            }
        });


        Gdx.input.setInputProcessor(escenaNivelTutorial);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas(){
        texturaVIU = manager.get("NivelVIU.png");
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
    }

    @Override
    public void render(float delta) {

        borrarPantalla();


        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            estado = estado==EstadoJuego.PAUSADO?EstadoJuego.JUGANDO:EstadoJuego.PAUSADO;
            if (estado==EstadoJuego.PAUSADO) {
                // Activar escenaPausa y pasarle el control
                if (escenaPausa==null) {
                    escenaPausa = new EscenaPausa(vista, batch);
                }
                Gdx.input.setInputProcessor(escenaPausa);
            }else{
                // Continuar el juego
                estado = EstadoJuego.JUGANDO;
                // Regresa el control a la pantalla
                Gdx.input.setInputProcessor(escenaNivelTutorial);
            }
            //Gdx.input.setInputProcessor(escenaNivelTutorial);

        }

        /*renderarMapa.render();
        escenaNivelTutorial.draw();
        batch.begin();
        plat1.dibujar(batch);
        plat1.mover(30,500,30,600);
        batch.end();*/

        if (estado==EstadoJuego.PAUSADO) {

            escenaPausa.draw();
        }else{
            renderarMapa.render();
            escenaNivelTutorial.draw();
            batch.begin();
            plat1.dibujar(batch);
            plat1.mover(30,500,30,600);
            texto.mostrarMensaje(batch,Integer.toString(contadorMiniVis),ANCHO-50,ALTO-50);
            batch.end();
        }
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

    // La escena que se muestra cuando el juego se pausa
    private class EscenaPausa extends Stage
    {
        public EscenaPausa(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            /*// Crear triángulo transparente
            Pixmap pixmap = new Pixmap((int)(ANCHO), (int)(ALTO), Pixmap.Format.RGBA8888 );
            pixmap.setColor( 0.2f, 0, 0.3f, 0.65f );
            //pixmap.fillTriangle(0,pixmap.getHeight(),pixmap.getWidth(),pixmap.getHeight(),pixmap.getWidth()/2,0);
            pixmap.fillRectangle(0,0,(int)ANCHO,(int)ALTO);*/
            Texture texturaRectangulo = manager.get("Fondos/PausaFondo.jpg");
            //pixmap.dispose();
            Image imgRectangulo = new Image(texturaRectangulo);
            //imgRectangulo.setPosition(0.15f*ANCHO, 0.1f*ALTO);
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
                    juego.setScreen(new PantallaCarga(juego,Pantallas.SELECCION_NIVEL,musicaFondo, EstadoMusica.DENIDO));
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
                    juego.setScreen(new PantallaCarga(juego,Pantallas.MENU,musicaFondo, EstadoMusica.DENIDO));
                }
            });
            this.addActor(btnSalir);

            // Continuar
            Texture texturabtnReanudar = manager.get("Botones/PausaButtonReanudar.png");
            TextureRegionDrawable trdReintentar = new TextureRegionDrawable(
                    new TextureRegion(texturabtnReanudar));
            ImageButton btnReanudar = new ImageButton(trdReintentar);
            btnReanudar.setPosition(ANCHO/2-btnReanudar.getWidth()/2+66/*100*/,ALTO/2-50/*50*/);
            contadorMiniVis++;
            btnReanudar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Continuar el juego
                    estado = EstadoJuego.JUGANDO;
                    // Regresa el control a la pantalla
                    Gdx.input.setInputProcessor(escenaNivelTutorial);
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
}
