package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
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
    private Texture texturaConfiguracion;
    private Texture texturaAyuda;

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

    private EscenaAyuda escenaAyuda;

    Preferences estadoAyuda = Gdx.app.getPreferences("estadoAyuda");


    public MenuPrincipal (Juego juego, Music musicaFondo, EstadoMusica estadoMusicaGeneral, EstadoSonido estadoSonidoGeneral){
        super();
        this.juego=juego;

        this.estadoMusicaGeneral= estadoMusicaGeneral;
        this.estadoSonidoGeneral= estadoSonidoGeneral;

        Gdx.app.log("Aviso", estadoMusicaGeneral.toString());
        musica= musicaFondo;
        musica.setLooping(true);

        /*if(estadoMusicaGeneral.equals(EstadoMusica.APAGADO)){
            musica.stop();
            musicaFondo.stop();
        }*/

        /*if(estadoMusicaGeneral!= EstadoMusica.APAGADO) {
            musica.play();
        }*/
        manager = juego.getAssetManager();

    }

    @Override
    public void show() {
        cargarTexturas();
        estadoAyuda.putInteger("ayuda",0);
        crearObjetos();
        if(estadoMusicaGeneral!= EstadoMusica.APAGADO) {
            musica.play();
        }else{
            musica.pause();
        }


    }



    private void crearObjetos() {
        batch=new SpriteBatch();
        escenaMenu=new Stage(vista,batch);
        Image imgFondo=new Image(texturaFondo);
        escenaMenu.addActor(imgFondo);

        Image imgTitulo = new Image(texturaTitulo);
        imgTitulo.setPosition(5*ANCHO/10+20,3*ALTO/4);
        escenaMenu.addActor(imgTitulo);

        //Botón
        TextureRegionDrawable trdBtnJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));
        ImageButton btnJugar = new ImageButton(trdBtnJugar);
        btnJugar.setPosition(5*ANCHO/8 + btnJugar.getWidth()/8, 15*ALTO/24-btnJugar.getHeight()/2);

        TextureRegionDrawable trdBtnCreditos = new TextureRegionDrawable(new TextureRegion(texturaBtnCreditos));
        ImageButton btnCreditos =new ImageButton(trdBtnCreditos);
        btnCreditos.setPosition(btnJugar.getX(), 15*ALTO/24-btnJugar.getHeight()*1.8f);

        //boton de configuracion
        TextureRegionDrawable trdBtnConfig = new TextureRegionDrawable(new TextureRegion(texturaConfiguracion));
        ImageButton btnConfig = new ImageButton(trdBtnConfig);
        btnConfig.setPosition(5*ANCHO/8 + btnJugar.getWidth()/8,10);

        //BOTON DE AYUDA
        TextureRegionDrawable trdBtnAyuda= new TextureRegionDrawable(new TextureRegion(texturaAyuda));
        ImageButton btnAyuda= new ImageButton(trdBtnAyuda);
        btnAyuda.setPosition(btnConfig.getX()+btnAyuda.getWidth()+10,btnConfig.getY());

        escenaMenu.addActor(btnJugar);
        escenaMenu.addActor(btnCreditos);
        escenaMenu.addActor(btnConfig);
        escenaMenu.addActor(btnAyuda);

        btnJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }
                Gdx.app.log("clicked","Me hicieron click");
                //musica.pause();


                juego.setScreen(new PantallaCarga(juego,Pantallas.SELECCION_NIVEL,musica,EstadoMusica.REPRODUCCION,estadoMusicaGeneral,estadoSonidoGeneral));
            }
        });

        btnCreditos.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click CREDITOS");
                //musica.pause();
                Gdx.app.log("Estado Sonido",""+ estadoSonidoGeneral);
                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }
                juego.setScreen(new MenuCreditos(juego,musica,estadoMusicaGeneral,estadoSonidoGeneral));
            }
        });

        btnConfig.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click CREDITOS");
                //musica.pause();
                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }
                juego.setScreen(new PantallaConfiguracion(juego,musica,estadoMusicaGeneral,estadoSonidoGeneral));
            }
        });
        btnAyuda.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click CREDITOS");
                //musica.pause();
                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }

                estadoAyuda.putInteger("ayuda",1);
                estadoAyuda.flush();

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
        texturaConfiguracion= manager.get("Botones/OptionsButtonMenuPrin.png");
        //CAMBIAR ESTA TEXTURA
        texturaAyuda= new Texture(Gdx.files.internal("Botones/Ayuda.png"));
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        //Gdx.app.log("ESTADO DE LA AYUDA", "" + estadoAyuda.getInteger("ayuda",0));
        if(estadoAyuda.getInteger("ayuda",0)==0) {
            escenaMenu.draw();
        }else{
            if(escenaAyuda==null){
                escenaAyuda= new EscenaAyuda(vista,batch,musica,estadoMusicaGeneral,estadoSonidoGeneral,this);

            }
            Gdx.input.setInputProcessor(escenaAyuda);
            Gdx.input.setCatchBackKey(true);
            //Teclado
            if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
                musica.pause();
                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }
                juego.setScreen(new PantallaCarga(juego,Pantallas.MENU,musica,EstadoMusica.REPRODUCCION,estadoMusicaGeneral,estadoSonidoGeneral));
            }
            escenaAyuda.draw();
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

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
    }
}
