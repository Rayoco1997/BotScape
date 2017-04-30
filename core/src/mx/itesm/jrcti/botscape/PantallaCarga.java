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
    private EstadoMusica estadoMusica;

    public PantallaCarga(Juego juego, Pantallas siguientePantalla, Music musica, EstadoMusica estadoMusica, EstadoMusica estadoMusicaGeneral, EstadoSonido estadoSonidoGeneral){
        this.juego = juego;
        this.siguientePantalla = siguientePantalla;
        this.estadoMusicaGeneral= estadoMusicaGeneral;
        this.estadoSonidoGeneral= estadoSonidoGeneral;
        musicaFondo= musica;
        this.estadoMusica= estadoMusica;
        batch=new SpriteBatch();
    }
    @Override
    public void show() {
        texturaCargando = new Texture(Gdx.files.internal("loading.png"));
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(ANCHO/2-spriteCargando.getWidth()/2,ALTO/2-spriteCargando.getHeight()/2);
        cargarRecursosSigPantalla();
        if(!estadoMusicaGeneral.equals(EstadoMusica.APAGADO)) {
            cargarMusica();
        }
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
            case NIVEL2:
                cargarRecursosNivel2();
                break;
        }
    }

    private void cargarMusica() {

        if(siguientePantalla.equals(Pantallas.NIVEL) || siguientePantalla.equals(Pantallas.NIVEL2)){
            musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("Sonidos/BringTheFoxhoundToMe.mp3"));
        } else if (estadoMusica != EstadoMusica.REPRODUCCION){
            musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("Sonidos/Avoiding Danger1.mp3"));
        }

        musicaFondo.setLooping(true);

    }

    private void cargarRecursosMenu() {
        manager.load("Fondos/PrincipalFondo.jpg", Texture.class);
        manager.load("Botones/PrincipalBtnPlay.png", Texture.class);
        manager.load("Botones/PrincipalBtnCredits.png", Texture.class);
        manager.load("Textos/PrincipalTitle.png", Texture.class);
        manager.load("Botones/OptionsButtonMenuPrin.png", Texture.class);

    }


    private void cargarRecursosNivelTutorial(){
        manager.load("Fondos/NivelTutorialFondo.jpg",Texture.class);
        manager.load("Mapas/Tilesv2.png", Texture.class);
        manager.load("Mapas/Map_TutorialV2.tmx", TiledMap.class);
        manager.load("Personaje/VIUWalk_Cycle.png", Texture.class);
        manager.load("NivelEnemigo.png", Texture.class);
        manager.load("NivelPlataforma.png", Texture.class);
        manager.load("NivelBoton.png", Texture.class);
        manager.load("NivelIman.png", Texture.class);
        manager.load("NivelMiniVI.png", Texture.class);
        manager.load("NivelPiso.png", Texture.class);
        manager.load("NivelEscalon.png", Texture.class);
        manager.load("NivelSalida.png", Texture.class);
        manager.load("Fondos/PausaFondo.jpg", Texture.class);
        manager.load("NivelPausa.png", Texture.class);
        manager.load("NivelPiso2.png", Texture.class);
        manager.load("Tutorial2.png", Texture.class);
        manager.load("Botones/PausaButtonMenuPrin.png", Texture.class);
        manager.load("Botones/PausaButtonReanudar.png", Texture.class);
        manager.load("Botones/PausaButtonSeleccionarNivel.png", Texture.class);
        manager.load("Textos/PausaTextTittle.png",Texture.class);
        manager.load("Personaje/Mini Vi.png",Texture.class);
        manager.load("Botones/MovIzqButton.png",Texture.class);
        manager.load("Botones/MovDerButton.png",Texture.class);
        manager.load("Botones/MovUpButton.png",Texture.class);
        manager.load("Personaje/LUG7 Walk_Cycle.png",Texture.class);
        manager.load("VidasVIU.png", Texture.class);
        manager.load("Textos/Ganaste.png",Texture.class);
        manager.load("Fondos/PantallaGanaste.jpg", Texture.class);
        manager.load("Fondos/PantallaPerdiste.jpg", Texture.class);
        manager.load("Textos/TextoGanaste.png", Texture.class);
        manager.load("Botones/PantallaNextLevel.png", Texture.class);
        manager.load("Botones/PantallaRetry.png", Texture.class);
        manager.load("PantallaEstrella.png", Texture.class);
        //BOTONES PARA SONIDO Y MUSICA
        manager.load("Botones/PausaBtnMusicOffMini.png",Texture.class);
        manager.load("Botones/PausaBtnMusicOnMini.png",Texture.class);
        manager.load("Botones/PausaBtnSoundOffMini.png",Texture.class);
        manager.load("Botones/PausaBtnSoundOnMini.png",Texture.class);
        manager.load("Viñeta.png",Texture.class);




    }

    private void cargarRecursosNivel2(){
        manager.load("Fondos/Fondo2.jpg",Texture.class);
        manager.load("Mapas/Tilesv3.png", Texture.class);
        manager.load("Mapas/Nivel2.tmx", TiledMap.class);
        manager.load("Personaje/VIUWalk_Cycle.png", Texture.class);
        manager.load("NivelEnemigo.png", Texture.class);
        manager.load("NivelPlataforma.png", Texture.class);
        manager.load("NivelBoton.png", Texture.class);
        manager.load("NivelIman.png", Texture.class);
        manager.load("NivelMiniVI.png", Texture.class);
        manager.load("NivelPiso.png", Texture.class);
        manager.load("NivelEscalon.png", Texture.class);
        manager.load("NivelSalida.png", Texture.class);
        manager.load("Fondos/PausaFondo.jpg", Texture.class);
        manager.load("NivelPausa.png", Texture.class);
        manager.load("NivelPiso2.png", Texture.class);
        manager.load("Tutorial2.png", Texture.class);
        manager.load("Botones/PausaButtonMenuPrin.png", Texture.class);
        manager.load("Botones/PausaButtonReanudar.png", Texture.class);
        manager.load("Botones/PausaButtonSeleccionarNivel.png", Texture.class);
        manager.load("Textos/PausaTextTittle.png",Texture.class);
        manager.load("Personaje/Mini Vi.png",Texture.class);
        manager.load("Botones/MovIzqButton.png",Texture.class);
        manager.load("Botones/MovDerButton.png",Texture.class);
        manager.load("Botones/MovUpButton.png",Texture.class);
        manager.load("Personaje/LUG7 Walk_Cycle.png",Texture.class);
        manager.load("VidasVIU.png", Texture.class);
        manager.load("Textos/Ganaste.png",Texture.class);
        manager.load("Fondos/PantallaGanaste.jpg", Texture.class);
        manager.load("Fondos/PantallaPerdiste.jpg", Texture.class);
        manager.load("Textos/TextoGanaste.png", Texture.class);
        manager.load("Botones/PantallaNextLevel.png", Texture.class);
        manager.load("Botones/PantallaRetry.png", Texture.class);
        manager.load("PantallaEstrella.png", Texture.class);
        manager.load("Viñeta.png",Texture.class);
    }

    private void cargarRecursosSelNivel(){
        manager.load("Fondos/SeleccionNivelFondo.jpg",Texture.class);
        manager.load("Botones/SeleccionNivelBtnNivel1.png",Texture.class);
        manager.load("Botones/SeleccionNivelBtnLocked.png",Texture.class);
        manager.load("Botones/SeleccionNivelBtnLocked.png",Texture.class);
        manager.load("Botones/SeleccionNivelBtnBack.png",Texture.class);
        manager.load("Botones/PerdisteBtnReintentar.png", Texture.class);
        manager.load("Textos/Perdiste.png",Texture.class);

        //NIVELES CON ESTRELLAS
        manager.load("Botones/Stars/Level1.png",Texture.class);
        manager.load("Botones/Stars/Level1_1.png",Texture.class);
        manager.load("Botones/Stars/Level1_2.png",Texture.class);
        manager.load("Botones/Stars/Level1_3.png",Texture.class);
        manager.load("Botones/Stars/Level2.png",Texture.class);
        manager.load("Botones/Stars/Level2_1.png",Texture.class);
        manager.load("Botones/Stars/Level2_2.png",Texture.class);
        manager.load("Botones/Stars/Level2_3.png",Texture.class);
        manager.load("Botones/Stars/Level3.png",Texture.class);
        manager.load("Botones/Stars/Level3_1.png",Texture.class);
        manager.load("Botones/Stars/Level3_2.png",Texture.class);
        manager.load("Botones/Stars/Level3_3.png",Texture.class);

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
                    juego.setScreen(new MenuPrincipal(juego, musicaFondo,estadoMusicaGeneral,estadoSonidoGeneral));   // 100% de carga
                    break;
                case NIVEL:
                    juego.setScreen(new NivelTutorial(juego,estadoMusicaGeneral,estadoSonidoGeneral));   // 100% de carga
                    break;
                case NIVEL2:
                    juego.setScreen(new Nivel2(juego,estadoMusicaGeneral,estadoSonidoGeneral));   // 100% de carga
                    break;
                case SELECCION_NIVEL:
                    juego.setScreen(new MenuSeleccionNivel(juego, musicaFondo,estadoMusicaGeneral,estadoSonidoGeneral));
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
