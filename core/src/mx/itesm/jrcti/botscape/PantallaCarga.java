package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private Pantallas siguientePantalla;
    private int avance; // % de carga
    //private Texto texto;

    private Music musicaFondo;

    private Texture texturaCargando;

    private SpriteBatch batch;

    public PantallaCarga(Juego juego, Pantallas siguientePantalla){
        this.juego = juego;
        this.siguientePantalla = siguientePantalla;
        batch=new SpriteBatch();
    }
    @Override
    public void show() {
        texturaCargando = new Texture(Gdx.files.internal("loading.png"));
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(ANCHO/2-spriteCargando.getWidth()/2,ALTO/2-spriteCargando.getHeight()/2);
        cargarRecursosSigPantalla();
        cargarMusica();
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
            case SELECCION_NIVEL:
                cargarRecursosSelNivel();
                break;
            case NIVEL:
                cargarRecursosNivelTutorial();
                break;
        }
    }

    private void cargarMusica() {

        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("Sonidos/Avoiding Danger1.mp3"));

        musicaFondo.setLooping(true);

    }

    private void cargarRecursosMenu(){
        manager.load("Fondos/PrincipalFondo.jpg",Texture.class);
        manager.load("Botones/PrincipalBtnPlay.png",Texture.class);
        manager.load("Botones/PrincipalBtnCredits.png",Texture.class);
        manager.load("Textos/PrincipalTitle.png",Texture.class);
    }

    private void cargarRecursosNivelTutorial(){
        manager.load("Mapas/Tilesv2.png", Texture.class);
        manager.load("Mapas/tutorialv2.tmx", TiledMap.class);
        manager.load("NivelVIU.png", Texture.class);
        manager.load("NivelEnemigo.png", Texture.class);
        manager.load("NivelPlataforma.png", Texture.class);
        manager.load("NivelBoton.png", Texture.class);
        manager.load("NivelIman.png", Texture.class);
        manager.load("NivelMiniVI.png", Texture.class);
        manager.load("NivelVida.png", Texture.class);
        manager.load("NivelContador.png", Texture.class);
        manager.load("NivelPiso.png", Texture.class);
        manager.load("NivelEscalon.png", Texture.class);
        manager.load("NivelSalida.png", Texture.class);
        manager.load("Fondos/PausaFondo.jpg", Texture.class);
        manager.load("NivelPausa.png", Texture.class);
        manager.load("NivelIzquierda.png", Texture.class);
        manager.load("NivelDerecha.png", Texture.class);
        manager.load("NivelSaltar.png", Texture.class);
        manager.load("NivelUsar.png", Texture.class);
        manager.load("NivelPiso2.png", Texture.class);
        manager.load("Tutorial2.png", Texture.class);
    }

    private void cargarRecursosSelNivel(){
        manager.load("Fondos/SeleccionNivelFondo.jpg",Texture.class);
        manager.load("Botones/SeleccionNivelBtnNivel1.png",Texture.class);
        manager.load("Botones/SeleccionNivelBtnLocked.png",Texture.class);
        manager.load("Botones/SeleccionNivelBtnLocked.png",Texture.class);
        manager.load("Botones/SeleccionNivelBtnBack.png",Texture.class);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        spriteCargando.draw(batch);
        //texto.mostrarMensaje(batch,avance+" %",ANCHO/2,ALTO/2);
        batch.end();
        // Actualizar
        timerAnimacion -= delta;
        if (timerAnimacion<=0) {
            timerAnimacion = TIEMPO_ENTRE_FRAMES;
            spriteCargando.rotate(20);
        }
        // Actualizar carga
        actualizarCargaRecursos();
    }

    private void actualizarCargaRecursos() {
        if (manager.update()) { // Terminó?
            switch (siguientePantalla) {
                case MENU:
                    juego.setScreen(new MenuPrincipal(juego, musicaFondo));   // 100% de carga
                    break;
                case NIVEL:
                    juego.setScreen(new NivelTutorial(juego));   // 100% de carga
                    break;
                case SELECCION_NIVEL:
                    juego.setScreen(new MenuSeleccionNivel(juego, musicaFondo));
                    break;
            }
        }
        avance = (int)(manager.getProgress()*100);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaCargando.dispose();
    }
}