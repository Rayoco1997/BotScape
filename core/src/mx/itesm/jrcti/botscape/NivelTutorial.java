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
    //heyyy baby
    //Texturas
    private Texture texturaVIU;
    private Texture texturaEnemigo;
    private Texture texturaPlataforma;
    private Texture texturaPalanca;
    private Texture texturaIman;
    private Texture texturaMiniVI;
    private Texture texturaVida;
    private Texture texturaMiniVIRecolectados;
    private Texture texturaPiso;
    private Texture texturaEscalon;
    private Texture texturaSalida;
    private Texture texturaFondoTutorial;
    private Texture texturaBtnPausa;
    private Texture texturaBtnIzquierda;
    private Texture texturaBtnDerecha;
    private Texture texturaBtnSaltar;
    private Texture texturaBtnUsar;

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


        Image imgFondo = new Image(texturaFondoTutorial);
        escenaNivelTutorial.addActor(imgFondo);

        Image imgPiso = new Image(texturaPiso);
        //imgPiso.setPosition();
        escenaNivelTutorial.addActor(imgPiso);

        Image imgPlataforma = new Image(texturaPlataforma);
        //imgPiso.setPosition();
        escenaNivelTutorial.addActor(imgFondo);


        Image imgEscalon = new Image(texturaEscalon);
        //imgPiso.setPosition();
        escenaNivelTutorial.addActor(imgFondo);


        Image imgSalida = new Image(texturaSalida);
        //imgPiso.setPosition();
        escenaNivelTutorial.addActor(imgFondo);


        Image imgMiniVI = new Image(texturaMiniVI);
        //imgPiso.setPosition();
        escenaNivelTutorial.addActor(imgFondo);


        Image imgIman = new Image(texturaIman);
        //imgPiso.setPosition();
        escenaNivelTutorial.addActor(imgFondo);


        Image imgEnemigo = new Image(texturaEnemigo);
        // imgPiso.setPosition();
        escenaNivelTutorial.addActor(imgFondo);


        Image imgVIU = new Image(texturaVIU);
        //imgPiso.setPosition();
        escenaNivelTutorial.addActor(imgVIU);



        //Botón para ir al menu de pausa
        TextureRegionDrawable trdBtnPausa = new TextureRegionDrawable(new TextureRegion(texturaBtnPausa));
        ImageButton btnPausa = new ImageButton(trdBtnPausa);
        btnPausa.setPosition(10,ALTO-btnPausa.getHeight());
        escenaNivelTutorial.addActor(btnPausa);

        btnPausa.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked", "me hicieron CLICK");
                juego.setScreen(new PantallaPausa(juego));
            }
        });


        //Botón para moverse a la izquierda
        TextureRegionDrawable trdBtnIzquierda = new TextureRegionDrawable(new TextureRegion(texturaBtnIzquierda));
        ImageButton btnIzquierda = new ImageButton(trdBtnIzquierda);
        btnIzquierda.setPosition(10,ALTO-btnIzquierda.getHeight());
        escenaNivelTutorial.addActor(btnIzquierda);

        /*btnIzquierda.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked", "me hicieron CLICK");
                juego.setScreen(new PantallaPausa(juego));
            }
        });*/



        //Botón para moverse a la derecha
        TextureRegionDrawable trdBtnDerecha = new TextureRegionDrawable(new TextureRegion(texturaBtnDerecha));
        ImageButton btnDerecha = new ImageButton(trdBtnDerecha);
        btnDerecha.setPosition(10,ALTO-btnDerecha.getHeight());
        escenaNivelTutorial.addActor(btnDerecha);

        /*btnPausa.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked", "me hicieron CLICK");
                juego.setScreen(new PantallaPausa(juego));
            }
        });*/




        //Botón para saltar
        TextureRegionDrawable trdBtnSaltar = new TextureRegionDrawable(new TextureRegion(texturaBtnSaltar));
        ImageButton btnSaltar = new ImageButton(trdBtnSaltar);
        btnSaltar.setPosition(10,ALTO-btnSaltar.getHeight());
        escenaNivelTutorial.addActor(btnSaltar);

        /*btnPausa.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked", "me hicieron CLICK");
                juego.setScreen(new PantallaPausa(juego));
            }
        });*/



        //Botón para usar
        TextureRegionDrawable trdBtnUsar = new TextureRegionDrawable(new TextureRegion(texturaBtnUsar));
        ImageButton btnUsar = new ImageButton(trdBtnUsar);
        btnUsar.setPosition(10,ALTO-btnUsar.getHeight());
        escenaNivelTutorial.addActor(btnUsar);

        /*btnPausa.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("clicked", "me hicieron CLICK");
                juego.setScreen(new PantallaPausa(juego));
            }
        });*/






        Gdx.input.setInputProcessor(escenaNivelTutorial);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas(){
        texturaVIU = new Texture("NivelVIU.png");
        texturaEnemigo = new Texture("NivelEnemigo.png");
        texturaPlataforma = new Texture("NivelPlataforma.png");
        texturaPalanca = new Texture("NivelPalanca.png");
        texturaIman = new Texture("NivelIman.png");
        texturaMiniVI = new Texture("NivelMiniVI.png");
        texturaVida = new Texture("NivelVida.png");
        texturaMiniVIRecolectados = new Texture("NivelContador.png");
        texturaPiso = new Texture("NivelPiso.png");
        texturaEscalon = new Texture("NivelPiso.png");
        texturaSalida = new Texture("NivelSalida.png");
        texturaFondoTutorial = new Texture("CreditosFondo.png");
        texturaBtnPausa = new Texture("NivelPausa.png");
        texturaBtnIzquierda = new Texture("NivelIzquierda.png");
        texturaBtnDerecha = new Texture("NivelDerecha.png");
        texturaBtnSaltar = new Texture("NivelSaltar.png");
        texturaBtnUsar = new Texture("NivelUsar.png");
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
