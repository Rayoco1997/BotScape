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
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

import javax.swing.plaf.TextUI;

/**
 * Created by Julio on 15/02/2017.
 */

public class MenuSeleccionNivel extends Pantalla {

    //Imagenes a usar
    private Texture texturaFondo;
    private Texture texturaBtnNivel1;
    private Texture texturaBtnNivel2;
    private Texture texturaBtnNivel3;
    private Texture texturaBtnBloqueado;
    private Texture texturaBtnRegresar;

    // Ayuda a dibujar
    private SpriteBatch batch;

    //Los 4 botones que muestra el menu
    private final int NUM_BOTONES = 4;
    private Array<ImageButton> arrBtn;

    //Varibales para los estados de cada nivel
    private int estadoNivel1,estadoNivel2,estadoNivel3;

    //LISTAS DE LAS IMAGENES PARA LOS NIVELES
    ArrayList<Texture> texturasNivel1= new ArrayList<Texture>();
    ArrayList<Texture> texturasNivel2= new ArrayList<Texture>();
    ArrayList<Texture> texturasNivel3= new ArrayList<Texture>();
    //Escenas
    private Stage escenaSeleccionNivel;

    private Music musica;
    private String musicalvl1= "Sonidos/BringTheFoxhoundToMe.mp3";

    private AssetManager manager;

    public MenuSeleccionNivel(Juego juego,Music musicaFondo, EstadoMusica estadoMusicaGeneral, EstadoSonido estadoSonidoGeneral){
        super();
        this.juego=juego;
        this.estadoMusicaGeneral= estadoMusicaGeneral;
        this.estadoSonidoGeneral= estadoSonidoGeneral;
        musica= musicaFondo;
        manager = juego.getAssetManager();
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


        batch = new SpriteBatch();

        escenaSeleccionNivel = new Stage(vista, batch);
        Image imgFondo = new Image(texturaFondo);
        escenaSeleccionNivel.addActor(imgFondo);




        TextureRegionDrawable trdBtnNivel1 = new TextureRegionDrawable(new TextureRegion(texturaBtnNivel1));
        ImageButton btnNivel1 = new ImageButton(trdBtnNivel1);
        btnNivel1.setPosition(ANCHO/4-btnNivel1.getWidth()/2, ALTO/2-btnNivel1.getWidth()/2);

        TextureRegionDrawable trdBtnNivel2 = new TextureRegionDrawable(new TextureRegion(texturaBtnNivel2));
        ImageButton btnNivel2 = new ImageButton(trdBtnNivel2);
        btnNivel2.setPosition(ANCHO/2-btnNivel2.getWidth()/2, ALTO/2-btnNivel2.getWidth()/2);

        TextureRegionDrawable trdBtnNivel3 = new TextureRegionDrawable(new TextureRegion(texturaBtnNivel3));
        ImageButton btnNivel3 = new ImageButton(trdBtnNivel3);
        btnNivel3.setPosition(3*ANCHO/4-btnNivel3.getWidth()/2, ALTO/2-btnNivel3.getWidth()/2);

        TextureRegionDrawable trdBtnBack = new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));
        ImageButton btnBack = new ImageButton(trdBtnBack);
        btnBack.setPosition(10, 10);

        escenaSeleccionNivel.addActor(btnNivel1);
        escenaSeleccionNivel.addActor(btnNivel2);
        escenaSeleccionNivel.addActor(btnNivel3);
        escenaSeleccionNivel.addActor(btnBack);
        final Preferences estadoNiveles= Gdx.app.getPreferences("estadoNiveles");

        btnNivel1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click lvl1");
                musica.stop();
                //musica = Gdx.audio.newMusic(Gdx.files.internal(musicalvl1));
                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }
                juego.setScreen(new PantallaCarga(juego, Pantallas.NIVEL, musica, EstadoMusica.REPRODUCCION,estadoMusicaGeneral,estadoSonidoGeneral));
            }
        });

        btnNivel2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click lvl2");
                musica.stop();
                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }
                //if(estadoNiveles.getInteger("estado2")!=4) {
                    juego.setScreen(new PantallaCarga(juego, Pantallas.NIVEL2, musica, EstadoMusica.REPRODUCCION, estadoMusicaGeneral, estadoSonidoGeneral));
                //}
            }
        });

        btnNivel3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click lvl3");
                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }
                //if(estadoNiveles.getInteger("estado3")!=4) {
                    juego.setScreen(new PantallaCarga(juego, Pantallas.NIVEL3, musica, EstadoMusica.REPRODUCCION, estadoMusicaGeneral, estadoSonidoGeneral));
                //}
            }
        });

        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked","Me hicieron click back");
                musica.pause();
                if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                    sonidoBoton.play(volumenSonido);
                }
                juego.setScreen(new PantallaCarga(juego, Pantallas.MENU, musica, EstadoMusica.REPRODUCCION,estadoMusicaGeneral,estadoSonidoGeneral));
            }
        });

        Gdx.input.setInputProcessor(escenaSeleccionNivel);
        Gdx.input.setCatchBackKey(true);

    }

    private void cargarTexturas() {
        //PARA OBTENER EL ESTADO DEL JUEGO
        //ESTADOS 0= DESBLOQUEADO, 1= 1 ESTRELLA, 2= DOS ESTRELLAS, 3= TRES ESTRELAS, 4= BLOQUEADO
        Preferences estadoNiveles= Gdx.app.getPreferences("estadoNiveles");
        if(estadoNiveles.getInteger("estado1",100)==100){
            estadoNiveles.putInteger("estado1",0);
        }
        if (estadoNiveles.getInteger("estado2",100)==100){
            estadoNiveles.putInteger("estado2",4);
        }
        if (estadoNiveles.getInteger("estado3",100)==100){
            estadoNiveles.putInteger("estado3",4);
        }
        estadoNiveles.flush();
        estadoNivel1= estadoNiveles.getInteger("estado1");
        estadoNivel2= estadoNiveles.getInteger("estado2");
        estadoNivel3= estadoNiveles.getInteger("estado3");

        texturaFondo = manager.get("Fondos/SeleccionNivelFondo.jpg");
        for(int i= 0; i<4; i++) {
            if(i==0) {
                texturasNivel1.add((Texture) manager.get("Botones/Stars/Level1.png"));
                texturasNivel2.add((Texture) manager.get("Botones/Stars/Level2.png"));
                texturasNivel3.add((Texture) manager.get("Botones/Stars/Level3.png"));
            }else if (i==1){
                texturasNivel1.add((Texture) manager.get("Botones/Stars/Level1_1.png"));
                texturasNivel2.add((Texture) manager.get("Botones/Stars/Level2_1.png"));
                texturasNivel3.add((Texture) manager.get("Botones/Stars/Level3_1.png"));
            }else if(i==2){
                texturasNivel1.add((Texture) manager.get("Botones/Stars/Level1_2.png"));
                texturasNivel2.add((Texture) manager.get("Botones/Stars/Level2_2.png"));
                texturasNivel3.add((Texture) manager.get("Botones/Stars/Level3_2.png"));
            }else{
                texturasNivel1.add((Texture) manager.get("Botones/Stars/Level1_3.png"));
                texturasNivel2.add((Texture) manager.get("Botones/Stars/Level2_3.png"));
                texturasNivel3.add((Texture) manager.get("Botones/Stars/Level3_3.png"));
            }
            //FALTA REMPLAZAR ESTAS POR LAS DE LA LISTA EN EL CODIGO

        }
        Gdx.app.log("ESTADO DEL NIVEL 1: ", ""+estadoNivel1);
        Gdx.app.log("ESTADO DEL NIVEL 2: ", ""+estadoNivel2);
        Gdx.app.log("ESTADO DEL NIVEL 3: ", ""+estadoNivel3);
        texturaBtnBloqueado= manager.get("Botones/SeleccionNivelBtnLocked.png");
        if (estadoNivel1==4){
            texturaBtnNivel1= texturaBtnBloqueado;
        }else{
            texturaBtnNivel1= texturasNivel1.get(estadoNivel1);
        }

        if (estadoNivel2==4){
            texturaBtnNivel2= texturaBtnBloqueado;
        }else{
            texturaBtnNivel2= texturasNivel2.get(estadoNivel2);
        }
        if (estadoNivel3==4){
            texturaBtnNivel3= texturaBtnBloqueado;
        }else{
            texturaBtnNivel3= texturasNivel3.get(estadoNivel3);
        }

        texturaBtnRegresar = manager.get("Botones/SeleccionNivelBtnBack.png");


    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        escenaSeleccionNivel.draw();

        //Teclado
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            musica.pause();
            if (estadoSonidoGeneral== EstadoSonido.ENCENDIDO){
                sonidoBoton.play(volumenSonido);
            }
            juego.setScreen(new PantallaCarga(juego,Pantallas.MENU,musica,EstadoMusica.DENIDO,estadoMusicaGeneral,estadoSonidoGeneral));
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
