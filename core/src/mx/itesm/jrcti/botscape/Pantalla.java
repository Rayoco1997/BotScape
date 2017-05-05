package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by Equipo1 on 15/02/2017.
 */

public abstract class Pantalla implements Screen {
    private Music musicaFondo;

    public static final float ANCHO = 1280;
    public static final float ALTO =  720;

    protected Juego juego;
    public EstadoMusica estadoMusicaGeneral= EstadoMusica.REPRODUCCION;
    public EstadoSonido estadoSonidoGeneral= EstadoSonido.ENCENDIDO;
    public static  final float volumenSonido= 0.8f;

    protected OrthographicCamera camara;
    protected Viewport vista;

    public Sound sonidoBoton;



    public Pantalla(){
        camara = new OrthographicCamera(ANCHO, ALTO);
        camara.position.set(ANCHO/2, ALTO/2,0);
        camara.update();
        vista = new StretchViewport(ANCHO, ALTO, camara);

        Preferences estadoAyuda = Gdx.app.getPreferences("estadoAyuda");
        estadoAyuda.putInteger("ayuda",0);
        estadoAyuda.flush();

        //Sonido boton
        sonidoBoton = Gdx.audio.newSound(Gdx.files.internal("Sonidos/Sound Effects/UI/Mouse_press and release.mp3"));

    }

    protected void borrarPantalla(){
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    protected class EscenaAyuda extends Stage

    {
        private int escenaActual=1;

        public EscenaAyuda(final Viewport vista, final SpriteBatch batch, final Music musicaFondo, final EstadoMusica estadoMusicaGeneral, final EstadoSonido estadoSonidoGeneral, final Object pantalla) {
            super(vista, batch);
            final ArrayList<String> listaFondos= new ArrayList<String>();
            listaFondos.add("Ayuda/ayuda1.png");
            listaFondos.add("Ayuda/ayuda2.png");
            listaFondos.add("Ayuda/ayuda3.png");
            listaFondos.add("Ayuda/ayuda4.png");

            final Texture imagenFondo= new Texture(Gdx.files.internal(listaFondos.get(0)));
            final Image fondo= new Image(imagenFondo);
            this.addActor(fondo);
            //BOTON DE SIGUIENTE
            Texture txtNext= new Texture(Gdx.files.internal("Botones/AyudaDerecha.png"));
            TextureRegionDrawable trdNext= new TextureRegionDrawable(new TextureRegion(txtNext));
            final ImageButton btnNext= new ImageButton(trdNext);
            btnNext.setPosition(ANCHO-btnNext.getWidth(),ALTO/2-btnNext.getHeight()/2);

            //BOTON DE BACK
            Texture txtBack= new Texture(Gdx.files.internal("Botones/AyudaIzquierda.png"));
            TextureRegionDrawable trdBack= new TextureRegionDrawable(new TextureRegion(txtBack));
            final ImageButton btnBack= new ImageButton(trdBack);
            btnBack.setPosition(0,ALTO/2-btnNext.getHeight()/2);
            btnBack.setVisible(false);

            //BOTON DE FIN
            Texture txtFin= new Texture(Gdx.files.internal("Botones/AyudaPalomita.png"));
            TextureRegionDrawable trdFin= new TextureRegionDrawable(new TextureRegion(txtFin));
            final ImageButton btnFin= new ImageButton(trdFin);
            btnFin.setPosition(ANCHO-btnNext.getWidth(),ALTO/2-(btnNext.getHeight()/2)*2);
            btnFin.setVisible(false);

            //LISTENERS
            btnNext.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("ME DIERON CLICK DERECHO, ESTADO ESCENA: " , "" + escenaActual);
                    if(btnNext.isVisible()) {
                        if (estadoSonidoGeneral == EstadoSonido.ENCENDIDO) {
                            sonidoBoton.play();
                        }
                        if (escenaActual==3){
                            btnFin.setVisible(true);
                            btnNext.setVisible(false);
                            escenaActual=4;
                            fondo.setDrawable(new SpriteDrawable(new Sprite(new Texture(listaFondos.get(3)))));

                        }
                        if(escenaActual==2){
                            escenaActual=3;
                            fondo.setDrawable(new SpriteDrawable(new Sprite(new Texture(listaFondos.get(2)))));
                        }
                        if(escenaActual==1){
                            escenaActual=2;
                            btnBack.setVisible(true);
                            fondo.setDrawable(new SpriteDrawable(new Sprite(new Texture(listaFondos.get(1)))));

                        }


                    }
                }
            });
            btnBack.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(btnBack.isVisible()) {
                        if (estadoSonidoGeneral == EstadoSonido.ENCENDIDO) {
                            sonidoBoton.play();
                        }
                        if(escenaActual==2){
                            escenaActual=1;
                            btnBack.setVisible(false);
                            fondo.setDrawable(new SpriteDrawable(new Sprite(new Texture(listaFondos.get(0)))));

                        }
                        if(escenaActual==3){
                            escenaActual=2;

                            fondo.setDrawable(new SpriteDrawable(new Sprite(new Texture(listaFondos.get(1)))));
                        }
                        if(escenaActual==4){
                            escenaActual=3;
                            btnFin.setVisible(false);
                            btnNext.setVisible(true);
                            fondo.setDrawable(new SpriteDrawable(new Sprite(new Texture(listaFondos.get(2)))));
                        }
                    }
                }
            });
            btnFin.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(btnFin.isVisible()) {
                        if (estadoSonidoGeneral == EstadoSonido.ENCENDIDO) {
                            sonidoBoton.play();
                        }
                        Preferences estadoAyuda = Gdx.app.getPreferences("estadoAyuda");
                        if(pantalla instanceof PantallaNivel.EscenaPausa){
                            Gdx.input.setInputProcessor((PantallaNivel.EscenaPausa)pantalla);
                            estadoAyuda.putInteger("ayuda",0);
                        }
                        if (pantalla instanceof  MenuPrincipal){
                            juego.setScreen(new MenuPrincipal(juego,musicaFondo,estadoMusicaGeneral,estadoSonidoGeneral));
                            estadoAyuda.putInteger("ayuda",0);
                        }
                        estadoAyuda.flush();

                    }
                }
            });

            this.addActor(btnBack);
            this.addActor(btnNext);
            this.addActor(btnFin);

        }

    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
    }

    @Override
    public void hide() {
        dispose();
    }
}
