package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
 * Created by Cinthya on 15/02/2017.
 */

public class MenuCreditos extends Pantalla {

    //Texturas
    private Texture fondoCreditos;
    private Texture texturabtnBack;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage escenaMenuCreditos;

    public MenuCreditos(Juego j){
        this.juego=j;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos(){
        batch= new SpriteBatch();
        escenaMenuCreditos = new Stage(vista,batch);
        Image imgCreditos = new Image(fondoCreditos);
        escenaMenuCreditos.addActor(imgCreditos);
        //Botón para regresar al menú principal
        TextureRegionDrawable trdBtnBack = new TextureRegionDrawable(new TextureRegion(texturabtnBack));
        ImageButton btnBack = new ImageButton(trdBtnBack);
        btnBack.setPosition(0,0);
        escenaMenuCreditos.addActor(btnBack);

        btnBack.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked", "me hicieron CLICK");
                juego.setScreen(new MenuPrincipal(juego));
            }
        });
    }

    private void cargarTexturas(){
        fondoCreditos= new Texture("CreditosFondo.jpg");
        texturabtnBack=new Texture("CreditosBtnBack.png");
    }

    @Override
    public void render(float delta) {

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
