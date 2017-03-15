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
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

/**
 * Created by Tommy on 15/03/2017.
 */

public class PantallaConfiguracion extends Pantalla {
    //CREANDO LAS TEXTURAS PARA LA PANTALLA
    private Texture texturaFondo;
    private Texture texturaButtonSonido;
    private Texture texturaButtonMusica;
    private Texture texturaTextConfig;
    private Texture texturaButtonRegresar;

    private SpriteBatch batch;

    private Stage escenaPantallaConfig;

    public PantallaConfiguracion(Juego juego){
        this.juego= juego;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();

    }

    private void crearObjetos() {
        batch= new SpriteBatch();
        escenaPantallaConfig= new Stage(vista,batch);
        Image imgFondo= new Image(texturaFondo);
        Image imgButtonRegresar= new Image(texturaButtonRegresar);
        Image imgButtonMusica= new Image(texturaButtonMusica);
        Image imgButtonSonido = new Image(texturaButtonSonido);
        Image imgTextConfig= new Image(texturaTextConfig);

        //CREANDO LOS BOTONES
        TextureRegionDrawable trdButtonRegresar = new TextureRegionDrawable(new TextureRegion(texturaButtonRegresar));
        TextureRegionDrawable trdButtonMusica = new TextureRegionDrawable(new TextureRegion(texturaButtonMusica));
        TextureRegionDrawable trdButtonSonido= new TextureRegionDrawable(new TextureRegion(texturaButtonSonido));

        ImageButton buttonRegresar = new ImageButton(trdButtonRegresar);
        ImageButton buttonMusica= new ImageButton(trdButtonMusica);
        ImageButton buttonSonido= new ImageButton(trdButtonSonido);

        //UBICANDO TODOS LOS BOTONES
        buttonRegresar.setPosition(10,10);
        imgTextConfig.setPosition(ANCHO/2- imgTextConfig.getWidth()/2,4*ALTO/5-imgTextConfig.getHeight()/2);
        buttonMusica.setPosition(ANCHO/2- buttonMusica.getWidth()/2,3*ALTO/5-buttonMusica.getHeight()/2);
        buttonSonido.setPosition(ANCHO/2- buttonSonido.getWidth()/2,2*ALTO/5-buttonSonido.getHeight()/2);

        //AGREGANDO A LA ESCENA
        escenaPantallaConfig.addActor(imgFondo);
        escenaPantallaConfig.addActor(buttonMusica);
        escenaPantallaConfig.addActor(buttonRegresar);
        escenaPantallaConfig.addActor(buttonSonido);
        escenaPantallaConfig.addActor(imgTextConfig);

        buttonRegresar.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.log("Aviso", "POS ME VOY AL MENU PRINCIPAL");
                juego.setScreen(new MenuPrincipal(juego));
            }
        });

        buttonMusica.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.log("Aviso", "POS ME VOY AL MENU PRINCIPAL");
                //AQUI VA EL CODIO PARA DESCATIVAR LA MUSICA
            }
        });

        buttonSonido.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.log("Aviso", "POS ME VOY AL MENU PRINCIPAL");
                //AQUI VA EL CODIO PARA DESCATIVAR LOS SONIDOS
            }
        });

        Gdx.input.setInputProcessor(escenaPantallaConfig);
        Gdx.input.setCatchBackKey(true);



    }

    private void cargarTexturas(){
        texturaFondo = new Texture("Fondos/PausaFondo.jpg");
        texturaButtonRegresar= new Texture("Botones/ConfiguracionBtnBack.png");
        texturaButtonMusica= new Texture("Botones/PausaButtonReanudar.png");
        texturaButtonSonido= new Texture("Botones/PausaButtonReanudar.png");
        texturaTextConfig= new Texture("Textos/PausaTextTittle.png");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        escenaPantallaConfig.draw();

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
