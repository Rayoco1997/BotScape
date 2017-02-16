package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Tommy on 15/02/2017.
 */

public class PantallaPausa extends Pantalla {
    //Texturas
    private Texture texturaFondo;
    private Texture texturaButtonReanudar;
    private Texture texturaButtonSelecNivel;
    private Texture texturaButtonMenuPrincipal;
    private Texture texturaTextPausa;

    //SpriteBatch
    private SpriteBatch batch;

    //escena
    private Stage escenaPantallaPausa;

    public PantallaPausa(Juego juego){
        this.juego= juego;

    }
    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();


    }

    private void crearObjetos() {
        batch= new SpriteBatch();
        escenaPantallaPausa= new Stage(vista,batch);
        Image imgFondo= new Image(texturaFondo);
        Image imgButtonReanudar = new Image(texturaButtonReanudar);
        Image imgButtonSeleecionarNivel= new Image(texturaButtonSelecNivel);
        Image imgButtonMenuPrin= new Image(texturaButtonMenuPrincipal);
        Image imgTextPausa = new Image(texturaTextPausa);

        //Para los botones
        TextureRegionDrawable trdButtonReanudar= new TextureRegionDrawable(new TextureRegion(texturaButtonReanudar));
        TextureRegionDrawable trdButtonSeleccionarNivel= new TextureRegionDrawable(new TextureRegion(texturaButtonSelecNivel));
        TextureRegionDrawable trdButtonMenuPrincipal= new TextureRegionDrawable(new TextureRegion(texturaButtonMenuPrincipal));

        //2da parte de crear botones
        ImageButton buttonReanudar = new ImageButton(trdButtonReanudar);
        ImageButton buttonSeleecionarNivel = new ImageButton(trdButtonSeleccionarNivel);
        ImageButton buttonMenuPrincipal = new ImageButton(trdButtonMenuPrincipal);

        //UBICANDO LOS BOTONES EN LA PANTALLA
        buttonReanudar.setPosition(ANCHO/2-buttonReanudar.getWidth()/2,ALTO/2);
        buttonSeleecionarNivel.setPosition(ANCHO/2-buttonSeleecionarNivel.getWidth()/2,1*ALTO/3);
        buttonMenuPrincipal.setPosition(ANCHO/2-buttonMenuPrincipal.getWidth()/2,ALTO/6);

        imgTextPausa.setPosition(ANCHO/2-imgFondo.getWidth()/2,5*ALTO/6);
        imgFondo.setPosition(0,0);

        //AGREGANDO A LA ESCENA
        escenaPantallaPausa.addActor(buttonReanudar);
        escenaPantallaPausa.addActor(buttonMenuPrincipal);
        escenaPantallaPausa.addActor(buttonSeleecionarNivel);
        escenaPantallaPausa.addActor(imgFondo);
        escenaPantallaPausa.addActor(imgTextPausa);

        buttonReanudar.addListener(new ClickListener(){
           public void clicked(InputEvent event, float x, float y){
               Gdx.app.log("Aviso", "POS ME VOY A REANUDAR");
               juego.setScreen(new NivelTutorial(juego));
           }
        });

        buttonMenuPrincipal.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.log("Aviso", "POS ME VOY AL MENU PRINCIPAL");
                juego.setScreen(new MenuPrincipal(juego));
            }
        });

        buttonSeleecionarNivel.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.log("Aviso", "POS ME VOY A LA SELECCION DE NIVEL");
                juego.setScreen(new MenuSeleccionNivel(juego));
            }
        });

        Gdx.input.setInputProcessor(escenaPantallaPausa);
        Gdx.input.setCatchBackKey(true);


    }

    //cargar las texturas
    private void cargarTexturas() {
        texturaFondo= new Texture("PausaFondoBlanco.png");
        texturaButtonReanudar= new Texture("PrincipalBtnPlay.png");
        texturaButtonMenuPrincipal= new Texture("PrincipalBtnPlay.png");
        texturaButtonSelecNivel= new Texture("PrincipalBtnPlay.png");
        texturaTextPausa= new Texture("PausaTextTittle.png");

    }



    /*private void crearCamara() {
        camara= new OrthographicCamera(ANCHO,ALTO);
        camara.position.set(ANCHO/2,ALTO/2,0);
        camara.update();
        vista = new StretchViewport(ANCHO,ALTO,camara);
    }*/

    @Override
    public void render(float delta) {
        borrarPantalla();
        escenaPantallaPausa.draw();

        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            juego.setScreen(new NivelTutorial(juego));
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
