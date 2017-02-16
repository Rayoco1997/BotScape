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
    private Texture textCinth;
    private Texture textTommy;
    private Texture textRay;
    private Texture textJulio;
    private Texture textZuren;


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
        //Posición fotos
        Image fotoRay = new Image(textureRay);
        fotoRay.setPosition(465,150);
        Image fotoTommy = new Image(textureTommy);
        fotoTommy.setPosition(1425,620);
        Image fotoJulio = new Image(textureJulio);
        fotoJulio.setPosition(1100,150);
        Image fotoZuren = new Image(textureZuren);
        fotoZuren.setPosition(785,620);
        Image fotoCinth = new Image(textureCinth);
        //Primero de arriba
        fotoCinth.setPosition(145,620);
        //Posición de los datos
        Image textoCinth = new Image (textCinth);
        textoCinth.setPosition(fotoCinth.getX(),fotoCinth.getY()-160);
        Image textoTommy = new Image (textTommy);
        textoTommy.setPosition(fotoTommy.getX(),fotoTommy.getY()-160);
        Image textoRay = new Image (textRay);
        textoRay.setPosition(fotoRay.getX(),fotoRay.getY()-160);
        Image textoJulio = new Image (textJulio);
        textoJulio.setPosition(fotoJulio.getX(),fotoJulio.getY()-160);
        Image textoZuren = new Image (textZuren);
        textoZuren.setPosition(fotoZuren.getX(),fotoZuren.getY()-160);


        //Dibujando los actores en pantalla
        escenaMenuCreditos.addActor(btnBack);
        escenaMenuCreditos.addActor(fotoRay);
        escenaMenuCreditos.addActor(fotoJulio);
        escenaMenuCreditos.addActor(fotoTommy);
        escenaMenuCreditos.addActor(fotoZuren);
        escenaMenuCreditos.addActor(fotoCinth);
        //Dibujando datos en pantalla
        escenaMenuCreditos.addActor(textoCinth);
        escenaMenuCreditos.addActor(textoTommy);
        escenaMenuCreditos.addActor(textoRay);
        escenaMenuCreditos.addActor(textoJulio);
        escenaMenuCreditos.addActor(textoZuren);


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
        //Añadiendo textura de fotografías
        textureRay= new Texture("FotoRay.jpg");
        textureTommy= new Texture("FotoTommy.jpg");
        textureJulio =new Texture("FotoJulio.jpg");
        textureZuren = new Texture("FotoZuren.jpg");
        textureCinth = new Texture("FotoCinth.jpg");
        //Añadiendo Texturas de los datos
        textCinth = new Texture("TextoCinthya.png");
        textTommy= new Texture("TextoTommy.png");
        textJulio = new Texture("TextoJulio.png");
        textRay = new Texture("TextoRay.png");
        textZuren = new Texture("TextoZuren.png");

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
