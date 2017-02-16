package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class NivelTutorial extends Pantalla{
    //Texturas
    private Texture fondoTutorial;
    private Texture texturabtnPausa;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage escenaNivelTutorial;

    public NivelTutorial(Juego j){
        this.juego=j;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos(){
        batch= new SpriteBatch();
        escenaNivelTutorial = new Stage(vista,batch);
        Image imgCreditos = new Image(fondoTutorial);
        escenaNivelTutorial.addActor(imgCreditos);
        //Botón para regresar al menú principal
        TextureRegionDrawable trdBtnPausa = new TextureRegionDrawable(new TextureRegion(texturabtnPausa));
        ImageButton btnPausa = new ImageButton(trdBtnPausa);
        btnPausa.setPosition(0,ALTO-btnPausa.getHeight());
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
        fondoTutorial= new Texture("CreditosFondo.jpg");
        texturabtnPausa=new Texture("SeleccionNivelBtnBack.png");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        escenaNivelTutorial.draw();

        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            juego.setScreen(new PantallaPausa(juego));
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
