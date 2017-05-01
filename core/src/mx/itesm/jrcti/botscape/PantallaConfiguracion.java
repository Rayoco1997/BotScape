package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.plaf.TextUI;
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

    private EscenaConfirmacion escenaConfirmacion;

    //booleano para confirmacion
    private boolean enConfirmacion=false;


    private ImageButton buttonMusica;
    private ImageButton buttonSonido;
    private ImageButton buttonReset;

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

        //BOTON DE RESETEAR JUEGO

        buttonReset= new ImageButton(trdButtonMusicaOff);
        buttonReset.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.log("Aviso", "POS ME RESETEOL");
                if (escenaConfirmacion==null) {
                    escenaConfirmacion = new EscenaConfirmacion(vista, batch);
                }
                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }
                //escenaConfirmacion.draw();
                enConfirmacion= true;

                Gdx.input.setInputProcessor(escenaConfirmacion);

            }
        });
        buttonReset.setPosition(ANCHO/2,0);

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
        escenaPantallaConfig.addActor(buttonReset);
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
                //AQUI VA EL CODIO PARA DESCATIVAR LA MUSICA
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
                //SONIDO DE OPRIMIR ESTE BOTON
                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }

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
        if(!enConfirmacion){
            escenaPantallaConfig.draw();
            if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }
                juego.setScreen(new MenuPrincipal(juego,musica,estadoMusicaGeneral,estadoSonidoGeneral));
            }
        }else{
            escenaPantallaConfig.draw();
            escenaConfirmacion.draw();

        }





    }
    protected class EscenaConfirmacion extends Stage
    {
        public EscenaConfirmacion(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            //Texture textoConirmacion= new Texture(Gdx.files.internal("Textos/ConfiguracionTextoConfirmacion.png"));
            Pixmap pixmap = new Pixmap((int)(ANCHO*0.5f), (int)(ALTO*0.5f), Pixmap.Format.RGBA8888 );
            pixmap.setColor( 0.4941f, 0.4980f, 0.5058f, 1.0f );
            pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
            Texture texturaRectangulo = new Texture( pixmap );
            pixmap.dispose();
            Image imgRectangulo = new Image(texturaRectangulo);
            imgRectangulo.setPosition(ANCHO/2-imgRectangulo.getWidth()/2, ALTO/2-imgRectangulo.getHeight()/2);
            this.addActor(imgRectangulo);

            //BOTON DE SI
            Texture texturaBtnSi= new Texture(Gdx.files.internal("Botones/PausaBtnMusicOnMini.png"));
            TextureRegionDrawable trdBtnSi= new TextureRegionDrawable(new TextureRegion(texturaBtnSi));
            ImageButton btnSi = new ImageButton(trdBtnSi);
            btnSi.setPosition(ANCHO/2-btnSi.getWidth(),ALTO/2-btnSi.getHeight()*2);
            btnSi.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //RESETEA EL ESTADO DEL JUUEGO
                    final Preferences estadoNiveles= Gdx.app.getPreferences("estadoNiveles");
                    if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                        sonidoBoton.play(volumenSonido);
                    }
                    estadoNiveles.putInteger("estado1",0);
                    estadoNiveles.putInteger("estado2",4);
                    estadoNiveles.putInteger("estado3",4);
                    estadoNiveles.flush();
                    // Regresa al menú
                    Gdx.input.setInputProcessor(escenaPantallaConfig);
                    enConfirmacion=false;
                }
            });
            this.addActor(btnSi);

            //BOTON DE NO
            Texture textureBtnNo= new Texture(Gdx.files.internal("Botones/PausaBtnMusicOffMini.png"));
            TextureRegionDrawable trdBtnNo= new TextureRegionDrawable(new TextureRegion(textureBtnNo));
            ImageButton btnNo= new ImageButton(trdBtnNo);
            btnNo.setPosition(ANCHO/2+10,ALTO/2-btnNo.getHeight()*2);
            btnNo.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //RESETEA EL ESTADO DEL JUUEGO
                    if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                        sonidoBoton.play(volumenSonido);
                    }
                    Gdx.input.setInputProcessor(escenaPantallaConfig);
                    enConfirmacion=false;
                }
            });
            this.addActor(btnNo);
            //TEXTO DE CONFIRMACION
            Texture textoConirmacion= new Texture(Gdx.files.internal("Textos/ConfiguracionTextoConfirmacion.png"));
            Image imgTextConfirmacion= new Image(textoConirmacion);
            imgTextConfirmacion.setPosition(ANCHO/2-imgTextConfirmacion.getWidth()/2,ALTO/2);
            this.addActor(imgTextConfirmacion);
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
