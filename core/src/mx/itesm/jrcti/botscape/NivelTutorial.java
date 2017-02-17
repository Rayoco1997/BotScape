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
import com.badlogic.gdx.utils.Array;

/**
 * Created by Cinthya on 15/02/2017.
 */

public class NivelTutorial extends Pantalla{

    //Texturas
    private Texture texturaVIU;
    private Texture texturaEnemigo;
    private Texture texturaPlataforma;
    private Texture texturaBoton;
    private Texture texturaIman;
    private Texture texturaMiniVI;
    private Texture texturaVida;
    private Texture texturaMiniVIRecolectados;
    private Texture texturaPiso;
    private Texture texturaEscalon;
    private Texture texturaPisoVerde;
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

    private final int NUM_PISO = 7;
    private Array<Image> arrPiso;

    private final int NUM_PISO_VERDE = 2;
    private Array<Image> arrPisoVerde;

    private final int NUM_PLAT = 4;
    private Array<Image> arrPlat;


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


        arrPiso = new Array<Image>(NUM_PISO);
        for(int x=0; x<=NUM_PISO; x++){
            float posX = x * ANCHO/7-texturaPiso.getWidth()/2;
            Image imgPiso = new Image(texturaPiso);
            imgPiso.setPosition(posX,10);
            escenaNivelTutorial.addActor(imgPiso);
        }

        arrPiso = new Array<Image>(NUM_PISO);
        for(int x=0; x<NUM_PISO_VERDE; x++){
            float posX = x * ANCHO/7-texturaPiso.getWidth()/2;
            Image imgPisoVerde = new Image(texturaPisoVerde);
            imgPisoVerde.setPosition(posX+1700,700);
            escenaNivelTutorial.addActor(imgPisoVerde);
        }

        arrPlat = new Array<Image>(NUM_PLAT);
        for(int x = 0; x<NUM_PLAT; x++) {
            Image imgPlataforma = new Image(texturaPlataforma);
            imgPlataforma.setPosition(((x+2)*ANCHO / 8)+400, ALTO / 3);
            escenaNivelTutorial.addActor(imgPlataforma);
        }

        Image imgBoton = new Image(texturaBoton);
        imgBoton.setPosition((6*ANCHO/8),ALTO/3+130);
        escenaNivelTutorial.addActor(imgBoton);


        Image imgEscalon = new Image(texturaEscalon);
        imgEscalon.setPosition(ANCHO/3,10);
        escenaNivelTutorial.addActor(imgEscalon);


        Image imgSalida = new Image(texturaSalida);
        imgSalida.setPosition(5*ANCHO/6,2*ALTO/3);
        escenaNivelTutorial.addActor(imgSalida);


        Image imgMiniVI = new Image(texturaMiniVI);
        imgMiniVI.setPosition(ANCHO/5,70);
        escenaNivelTutorial.addActor(imgMiniVI);


        Image imgIman = new Image(texturaIman);
        imgIman.setPosition(2*ANCHO/3, 5*ALTO/6);
        escenaNivelTutorial.addActor(imgIman);


        Image imgEnemigo = new Image(texturaEnemigo);
        imgEnemigo.setPosition((3*ANCHO/8)+400,ALTO/3+130);
        escenaNivelTutorial.addActor(imgEnemigo);


        Image imgVIU = new Image(texturaVIU);
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




        Gdx.input.setInputProcessor(escenaNivelTutorial);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas(){
        texturaVIU = new Texture("NivelVIU.png");
        texturaEnemigo = new Texture("NivelEnemigo.png");
        texturaPlataforma = new Texture("NivelPlataforma.png");
        texturaBoton = new Texture("NivelBoton.png");
        texturaIman = new Texture("NivelIman.png");
        texturaMiniVI = new Texture("NivelMiniVI.png");
        texturaVida = new Texture("NivelVida.png");
        texturaMiniVIRecolectados = new Texture("NivelContador.png");
        texturaPiso = new Texture("NivelPiso.png");
        texturaEscalon = new Texture("NivelEscalon.png");
        texturaSalida = new Texture("NivelSalida.png");
        texturaFondoTutorial = new Texture("PausaFondo.jpg");
        texturaBtnPausa = new Texture("NivelPausa.png");
        texturaBtnIzquierda = new Texture("NivelIzquierda.png");
        texturaBtnDerecha = new Texture("NivelDerecha.png");
        texturaBtnSaltar = new Texture("NivelSaltar.png");
        texturaBtnUsar = new Texture("NivelUsar.png");
        texturaPisoVerde = new Texture ("NivelPiso2.png");
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
