package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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

        //Sonido boton
        sonidoBoton = Gdx.audio.newSound(Gdx.files.internal("Sonidos/Sound Effects/UI/Radar.mp3"));

    }

    protected void borrarPantalla(){
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
