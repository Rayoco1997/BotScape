package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
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

    //Imagenes a usar
    private Texture texturaFondo;
    private Texture texturaBtnCreditos;
    private Texture texturaBtnJugar;

    //Dibujar
    private SpriteBatch batch;

    //Escena
    private Stage escenaMenu;

    //Menu
    private final Juego juego;


    public MenuPrincipal (Juego juego){
        super();
        this.juego=juego;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch=new SpriteBatch();
        escenaMenu=new Stage(vista,batch);
        Image imgFondo=new Image(texturaFondo);
        escenaMenu.addActor(imgFondo);

        //Botón
        TextureRegionDrawable trdBtnJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));
        ImageButton btnJugar = new ImageButton(trdBtnJugar);
        btnJugar.setPosition(ANCHO/2-btnJugar.getWidth()/2, ANCHO/2-btnJugar.getWidth());

        escenaMenu.addActor(btnJugar);

        btnJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click");
                //juego.setScreen();
            }
        });

        Gdx.input.setInputProcessor(escenaMenu);
        Gdx.input.setCatchBackKey(false);
    }

    private void cargarTexturas() {
        texturaFondo = new Texture("fondoMenu.jpg");
        texturaBtnJugar = new Texture("playButton.png");
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
