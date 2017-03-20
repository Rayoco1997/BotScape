package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.Menu;
import java.sql.Time;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;

/**
 * Created by Julio on 08/03/2017.
 */

public class PantallaSplashTec extends Pantalla {
    private AssetManager manager;
    private Music musicaFondo;

    private final int TIEMPO_DE_ESPERA = 3000;
    private SpriteBatch batch;
    private Stage escenaTec;
    private long tiempoDeEspera;

    private Texture fondoTec;

    private Sprite cambioDeFondo;

    private float cambioAlpha = (float)1/180;
    private float alpha = (float) 1;
    private int cuenta = 0;

    public PantallaSplashTec(Juego juego){
        super();
        this.juego = juego;
        tiempoDeEspera = TimeUtils.millis();
        manager= juego.getAssetManager();
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
        cargarMusica();
    }

    private void cargarMusica() {
        musicaFondo = manager.get("audio.mpe");
        musicaFondo.setLooping(true);

    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaTec = new Stage(vista, batch);
        cambioDeFondo = new Sprite(fondoTec);
        cambioDeFondo.setPosition(0,0);
    }

    private void cargarTexturas() {
        fondoTec = new Texture("Fondos/FondoSplashTec.jpg");
    }

    @Override
    public void render(float delta) {

        borrarPantalla();
        escenaTec.draw();
        batch.begin();
        cambioDeFondo.draw(batch);
        cambioDeFondo.setAlpha(alpha);
        if((TimeUtils.millis() - tiempoDeEspera)> TIEMPO_DE_ESPERA){
            juego.setScreen(new MenuPrincipal(juego,musicaFondo));
        }
        alpha -= cambioAlpha;
        batch.end();
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
