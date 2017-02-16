package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private Texture textureRay;
    private Texture textureTommy;
    private Texture textureJulio;
    private Texture textureZuren;
    private Texture textureCinth;


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
        Image fotoRay = new Image(textureRay);
        fotoRay.setPosition(465,80);
        Image fotoTommy = new Image(textureTommy);
        fotoTommy.setPosition(1425,620);
        Image fotoJulio = new Image(textureJulio);
        fotoJulio.setPosition(1100,80);
        Image fotoZuren = new Image(textureZuren);
        fotoZuren.setPosition(785,620);
        Image fotoCinth = new Image(textureCinth);
        fotoCinth.setPosition(145,620);
        escenaMenuCreditos.addActor(btnBack);
        escenaMenuCreditos.addActor(fotoRay);
        escenaMenuCreditos.addActor(fotoJulio);
        escenaMenuCreditos.addActor(fotoTommy);
        escenaMenuCreditos.addActor(fotoZuren);
        escenaMenuCreditos.addActor(fotoCinth);


        btnBack.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked", "me hicieron CLICK");
                juego.setScreen(new MenuPrincipal(juego));
            }
        });

        Gdx.input.setInputProcessor(escenaMenuCreditos);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas(){
        fondoCreditos= new Texture("PausaFondo.jpg");
        texturabtnBack=new Texture("SeleccionNivelBtnBack.png");
        textureRay= new Texture("FotoRay.jpg");
        textureTommy= new Texture("FotoTommy.jpg");
        textureJulio =new Texture("FotoJulio.jpg");
        textureZuren = new Texture("FotoZuren.jpg");
        textureCinth = new Texture("FotoCinth.jpg");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        escenaMenuCreditos.draw();

        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            juego.setScreen(new MenuPrincipal(juego));
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
