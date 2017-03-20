package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Created by rayoc on 20/03/2017.
 */

public class PantallaCarga extends Pantalla{
    // Animación cargando
    private static final float TIEMPO_ENTRE_FRAMES = 0.05f;
    private Sprite spriteCargando;
    private float timerAnimacion = TIEMPO_ENTRE_FRAMES;

    // AssetManager
    private AssetManager manager;

    private Juego juego;
    private Pantalla siguientePantalla;
    private int avance; // % de carga
    //private Texto texto;

    private Texture texturaCargando;

    public PantallaCarga(Juego juego, Pantalla siguientePantalla){
        this.juego = juego;
        this.siguientePantalla = siguientePantalla;
    }
    @Override
    public void show() {
        texturaCargando = new Texture(Gdx.files.internal("cargando/loading.png"));
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(ANCHO/2-spriteCargando.getWidth()/2,ALTO/2-spriteCargando.getHeight()/2);
        cargarRecursosSigPantalla();
        //texto = new Texto("fuentes/fuente.fnt");
    }

    // Carga los recursos de la siguiente pantalla
    private void cargarRecursosSigPantalla() {
        manager = juego.getAssetManager();
        avance = 0;
        switch (siguientePantalla) {
            case MENU:
                cargarRecursosMenu();
                break;
            case NIVEL_MARIO:
                cargarRecursosMario();
                break;
            case NIVEL_WHACK_A_MOLE:
                cargarRecursosWhackAMole();
                break;
        }
    }

    private void cargarRecursosMenu(){}

    private void cargarRecursosMario()

    @Override
    public void render(float delta) {

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
