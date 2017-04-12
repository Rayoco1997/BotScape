package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Julio on 15/02/2017.
 */

public class MenuSeleccionNivel extends Pantalla {

    //Imagenes a usar
    private Texture texturaFondo;
    private Texture texturaBtnNivel1;
    private Texture texturaBtnNivel2;
    private Texture texturaBtnNivel3;
    private Texture texturaBtnRegresar;

    // Ayuda a dibujar
    private SpriteBatch batch;

    //Los 4 botones que muestra el menu
    private final int NUM_BOTONES = 4;
    private Array<ImageButton> arrBtn;

    //Escenas
    private Stage escenaSeleccionNivel;

    private Music musica;
    private String musicalvl1= "Sonidos/BringTheFoxhoundToMe.mp3";

    private AssetManager manager;

    public MenuSeleccionNivel(Juego juego,Music musicaFondo, EstadoMusica estadoMusicaGeneral){
        super();
        this.juego=juego;
        this.estadoMusicaGeneral= estadoMusicaGeneral;
        musica= musicaFondo;
        manager = juego.getAssetManager();
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
        if(estadoMusicaGeneral!= EstadoMusica.APAGADO) {
            musica.play();
        }

    }

    private void crearObjetos() {
        batch = new SpriteBatch();

        escenaSeleccionNivel = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escenaSeleccionNivel.addActor(imgFondo);

        TextureRegionDrawable trdBtnNivel1 = new TextureRegionDrawable(new TextureRegion(texturaBtnNivel1));
        ImageButton btnNivel1 = new ImageButton(trdBtnNivel1);
        btnNivel1.setPosition(ANCHO/4-btnNivel1.getWidth()/2, ALTO/2-btnNivel1.getWidth()/2);

        TextureRegionDrawable trdBtnNivel2 = new TextureRegionDrawable(new TextureRegion(texturaBtnNivel2));
        ImageButton btnNivel2 = new ImageButton(trdBtnNivel2);
        btnNivel2.setPosition(ANCHO/2-btnNivel2.getWidth()/2, ALTO/2-btnNivel2.getWidth()/2);

        TextureRegionDrawable trdBtnNivel3 = new TextureRegionDrawable(new TextureRegion(texturaBtnNivel3));
        ImageButton btnNivel3 = new ImageButton(trdBtnNivel3);
        btnNivel3.setPosition(3*ANCHO/4-btnNivel3.getWidth()/2, ALTO/2-btnNivel3.getWidth()/2);

        TextureRegionDrawable trdBtnBack = new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));
        ImageButton btnBack = new ImageButton(trdBtnBack);
        btnBack.setPosition(10, 10);

        escenaSeleccionNivel.addActor(btnNivel1);
        escenaSeleccionNivel.addActor(btnNivel2);
        escenaSeleccionNivel.addActor(btnNivel3);
        escenaSeleccionNivel.addActor(btnBack);

        btnNivel1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click lvl1");
                musica.stop();
                //musica = Gdx.audio.newMusic(Gdx.files.internal(musicalvl1));
                juego.setScreen(new PantallaCarga(juego, Pantallas.NIVEL, musica, EstadoMusica.REPRODUCCION,estadoMusicaGeneral));
            }
        });

        btnNivel2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click lvl2");
                juego.setScreen(new PantallaCarga(juego, Pantallas.NIVEL2, musica, EstadoMusica.REPRODUCCION,estadoMusicaGeneral));
            }
        });

        btnNivel3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click lvl3");
                juego.setScreen(new PantallaCarga(juego, Pantallas.NIVEL, musica, EstadoMusica.REPRODUCCION,estadoMusicaGeneral));
            }
        });

        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click back");
                musica.pause();
                juego.setScreen(new PantallaCarga(juego, Pantallas.MENU, musica, EstadoMusica.REPRODUCCION,estadoMusicaGeneral));
            }
        });

        Gdx.input.setInputProcessor(escenaSeleccionNivel);
        Gdx.input.setCatchBackKey(true);

    }

    private void cargarTexturas() {

        texturaFondo = manager.get("Fondos/SeleccionNivelFondo.jpg");
        texturaBtnNivel1 = manager.get("Botones/SeleccionNivelBtnNivel1.png");
        texturaBtnNivel2 = manager.get("Botones/SeleccionNivelBtnLocked.png");
        texturaBtnNivel3 = manager.get("Botones/SeleccionNivelBtnLocked.png");
        texturaBtnRegresar = manager.get("Botones/SeleccionNivelBtnBack.png");

    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        escenaSeleccionNivel.draw();

        //Teclado
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            musica.pause();
            juego.setScreen(new PantallaCarga(juego,Pantallas.MENU,musica,EstadoMusica.DENIDO,estadoMusicaGeneral));
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
}
