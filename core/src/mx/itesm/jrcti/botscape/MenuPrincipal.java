package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
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

/**
 * Created by Equipo 1 on 15/02/2017.
 */

public class MenuPrincipal extends Pantalla {
    //Comentario prueb
    //Imagenes a usar
    private Texture texturaFondo;
    private Texture texturaBtnCreditos;
    private Texture texturaBtnJugar;
    private Texture texturaTitulo;

    //Dibujar
    private SpriteBatch batch;

    //Escena
    private Stage escenaMenu;

    //Menu
    //private final Juego juego;

    //MUSICA Y SONIDOS
    //hjhiu

    private Music musica;
    //MANAGER
    private AssetManager manager;


    public MenuPrincipal (Juego juego, Music musicaFondo){
        super();
        this.juego=juego;
        musica= musicaFondo;
        manager = juego.getAssetManager();

    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
        musica.play();

    }



    private void crearObjetos() {
        batch=new SpriteBatch();
        escenaMenu=new Stage(vista,batch);
        Image imgFondo=new Image(texturaFondo);
        escenaMenu.addActor(imgFondo);

        Image imgTitulo = new Image(texturaTitulo);
        imgTitulo.setPosition(3*ANCHO/5,3*ALTO/4);
        escenaMenu.addActor(imgTitulo);

        //Botón
        TextureRegionDrawable trdBtnJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));
        ImageButton btnJugar = new ImageButton(trdBtnJugar);
        btnJugar.setPosition(5*ANCHO/8 + btnJugar.getWidth()/8, ALTO/2-btnJugar.getHeight()/2);

        TextureRegionDrawable trdBtnCreditos = new TextureRegionDrawable(new TextureRegion(texturaBtnCreditos));
        ImageButton btnCreditos =new ImageButton(trdBtnCreditos);
        btnCreditos.setPosition(btnJugar.getX(), ALTO/2-btnJugar.getHeight()*2);

        escenaMenu.addActor(btnJugar);
        escenaMenu.addActor(btnCreditos);

        btnJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click");
                musica.pause();


                juego.setScreen(new PantallaCarga(juego,Pantallas.SELECCION_NIVEL,musica,EstadoMusica.REPRODUCCION));
            }
        });

        btnCreditos.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click CREDITOS");
                musica.pause();
                juego.setScreen(new MenuCreditos(juego,musica));
            }
        });

        Gdx.input.setInputProcessor(escenaMenu);
        Gdx.input.setCatchBackKey(false);
    }

    private void cargarTexturas() {
        texturaFondo = manager.get("Fondos/PrincipalFondo.jpg");
        texturaBtnJugar = manager.get("Botones/PrincipalBtnPlay.png");
        texturaBtnCreditos = manager.get("Botones/PrincipalBtnCredits.png");
        texturaTitulo = manager.get("Textos/PrincipalTitle.png");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        escenaMenu.draw();
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

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
    }
}
