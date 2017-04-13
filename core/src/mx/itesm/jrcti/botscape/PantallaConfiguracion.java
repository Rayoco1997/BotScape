package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

/**
 * Created by Tommy on 15/03/2017.
 */

public class PantallaConfiguracion extends Pantalla {
    //CREANDO LAS TEXTURAS PARA LA PANTALLA
    private Texture texturaFondo;
    private Texture texturaButtonSonido;
    private Texture texturaButtonMusicaOff;
    private Texture texturaButtonMusicaOn;
    private Texture texturaTextConfig;
    private Texture texturaButtonRegresar;
    private Texture texturaButtonSonidoOff;
    private Texture texturaButtonSonidoOn;


    private ImageButton buttonMusica;
    private ImageButton buttonSonido;

    private SpriteBatch batch;

    private Stage escenaPantallaConfig;

    private Music musica;

    private float btnMusicaX;
    private float btnMusicaY;

    public PantallaConfiguracion(Juego juego, Music musica, EstadoMusica estadoMusicaGeneral, EstadoSonido estadoSonidoGeneral){

        this.juego= juego;
        this.musica= musica;
        this.estadoMusicaGeneral= estadoMusicaGeneral;
        this.estadoSonidoGeneral= estadoSonidoGeneral;
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
        batch= new SpriteBatch();
        escenaPantallaConfig= new Stage(vista,batch);
        Image imgFondo= new Image(texturaFondo);
        /*Image imgButtonRegresar= new Image(texturaButtonRegresar);
        Image imgButtonMusica= new Image(texturaButtonMusica);
        Image imgButtonSonido = new Image(texturaButtonSonido);*/
        Image imgTextConfig= new Image(texturaTextConfig);

        //CREANDO LOS BOTONES
        TextureRegionDrawable trdButtonRegresar = new TextureRegionDrawable(new TextureRegion(texturaButtonRegresar));
        final TextureRegionDrawable trdButtonMusicaOff = new TextureRegionDrawable(new TextureRegion(texturaButtonMusicaOff));
        final TextureRegionDrawable trdButtonMusicaOn = new TextureRegionDrawable(new TextureRegion(texturaButtonMusicaOn));

        final TextureRegionDrawable trdButtonSoundOff= new TextureRegionDrawable(new TextureRegion(texturaButtonSonidoOff));
        final TextureRegionDrawable trdButtonSoundOn= new TextureRegionDrawable(new TextureRegion(texturaButtonSonidoOn));
        //TextureRegionDrawable trdButtonSonido= new TextureRegionDrawable(new TextureRegion(texturaButtonSonido));

        final ImageButton buttonRegresar = new ImageButton(trdButtonRegresar);

        if (estadoMusicaGeneral== EstadoMusica.APAGADO) {
            buttonMusica = new ImageButton(trdButtonMusicaOff);
        }else{
            buttonMusica= new ImageButton(trdButtonMusicaOn);
        }

        if (estadoSonidoGeneral== EstadoSonido.APAGADO) {
            buttonSonido = new ImageButton(trdButtonSoundOff);
        }else{
            buttonSonido= new ImageButton(trdButtonSoundOn);
        }
        //ImageButton buttonSonido= new ImageButton(trdButtonSonido);

        //UBICANDO TODOS LOS BOTONES
        buttonRegresar.setPosition(10,10);
        imgTextConfig.setPosition(ANCHO/2- imgTextConfig.getWidth()/2,4*ALTO/5-imgTextConfig.getHeight()/2);
        btnMusicaX= ANCHO/2- buttonMusica.getWidth()/2;
        btnMusicaY= ALTO/2-buttonMusica.getHeight()/2;
        buttonMusica.setPosition(btnMusicaX,btnMusicaY);

        buttonSonido.setPosition(buttonMusica.getX(),buttonMusica.getY()-2*buttonSonido.getHeight());
        //buttonSonido.setPosition(ANCHO/2- buttonSonido.getWidth()/2,2*ALTO/5-buttonSonido.getHeight()/2);

        //AGREGANDO A LA ESCENA
        escenaPantallaConfig.addActor(imgFondo);
        escenaPantallaConfig.addActor(buttonMusica);
        escenaPantallaConfig.addActor(buttonSonido);
        escenaPantallaConfig.addActor(buttonRegresar);
        //escenaPantallaConfig.addActor(buttonSonido);
        escenaPantallaConfig.addActor(imgTextConfig);

        buttonRegresar.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.log("Aviso", "POS ME VOY AL MENU PRINCIPAL");
                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }
                juego.setScreen(new MenuPrincipal(juego,musica, estadoMusicaGeneral,estadoSonidoGeneral));
            }
        });


        buttonMusica.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.log("Aviso", "POS ME VOY AL MENU PRINCIPAL");
                if(estadoMusicaGeneral==EstadoMusica.APAGADO){
                    estadoMusicaGeneral= EstadoMusica.REPRODUCCION;
                    buttonMusica.getStyle().imageUp= trdButtonMusicaOn;

                    Gdx.app.log("Aviso", estadoMusicaGeneral.toString());

                    musica=Gdx.audio.newMusic(Gdx.files.internal("Sonidos/Avoiding Danger1.mp3"));

                    musica.play();
                }else{
                    estadoMusicaGeneral= EstadoMusica.APAGADO;
                    Gdx.app.log("Aviso", estadoMusicaGeneral.toString());
                    buttonMusica.getStyle().imageUp= trdButtonMusicaOff;
                    musica.stop();
                }
                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }
                //AQUI VA EL CODIO PARA DESCATIVAR LA MUSICA
            }
        });
        buttonSonido.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.log("Aviso", "POS ME VOY AL MENU PRINCIPAL");
                if(estadoSonidoGeneral==EstadoSonido.APAGADO){

                    estadoSonidoGeneral= EstadoSonido.ENCENDIDO;
                    buttonSonido.getStyle().imageUp= trdButtonSoundOn;


                    Gdx.app.log("Aviso", estadoSonidoGeneral.toString());

                    sonidoBoton.play(volumenSonido);
                }else{
                    estadoSonidoGeneral= EstadoSonido.APAGADO;
                    buttonSonido.getStyle().imageUp= trdButtonSoundOff;
                }
                //AQUI VA EL CODIO PARA DESCATIVAR LA MUSICA
            }
        });




        /*buttonSonido.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.log("Aviso", "POS ME VOY AL MENU PRINCIPAL");
                //AQUI VA EL CODIO PARA DESCATIVAR LOS SONIDOS
            }
        });*/

        Gdx.input.setInputProcessor(escenaPantallaConfig);
        Gdx.input.setCatchBackKey(true);



    }

    private void cargarTexturas(){
        texturaFondo = new Texture("Fondos/PausaFondo.jpg");
        texturaButtonRegresar= new Texture("Botones/ConfiguracionBtnBack.png");
        texturaButtonMusicaOff= new Texture("Botones/ConfiguracionBtnMusicOff.png");
        texturaButtonMusicaOn= new Texture("Botones/ConfiguracionBtnMusicOn.png");
        texturaButtonSonidoOff= new Texture("Botones/ConfiguracionBtnSoundOff.png");
        texturaButtonSonidoOn= new Texture("Botones/ConfiguracionBtnSoundOn.png");
        //texturaButtonSonido= new Texture("Botones/PausaButtonReanudar.png");
        texturaTextConfig= new Texture("Textos/OptionsText.png");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();

        escenaPantallaConfig.draw();

        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                sonidoBoton.play(volumenSonido);
            }
            juego.setScreen(new MenuPrincipal(juego,musica,estadoMusicaGeneral,estadoSonidoGeneral));
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
