package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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


    public NivelTutorial(Juego j){
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
        mapa = manager.get("Mapas/tutorial.tmx");
        musicaFondo = manager.get("Sonidos/Bring the foxhound to me.mp3");
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

        //Botón para ir al menu de pausa
        TextureRegionDrawable trdBtnPausa = new TextureRegionDrawable(new TextureRegion(texturaBtnPausa));
        ImageButton btnPausa = new ImageButton(trdBtnPausa);
        btnPausa.setPosition(10,ALTO-btnPausa.getHeight());
        escenaNivelTutorial.addActor(btnPausa);

        btnPausa.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked", "me hicieron CLICK");
                juego.setScreen(new PantallaPausa(juego));
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
        escenaNivelTutorial.draw();

        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            juego.setScreen(new PantallaPausa(juego));
        }

        batch.begin();
        plat1.dibujar(batch);
        plat1.mover(30,500,30,600);
        batch.end();

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
