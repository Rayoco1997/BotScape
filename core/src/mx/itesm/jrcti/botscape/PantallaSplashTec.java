package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.Menu;
import java.sql.Time;

/**
 * Created by Julio on 08/03/2017.
 */

public class PantallaSplashTec extends Pantalla {


    private SpriteBatch batch;
    private Stage escenaTec;
    private int tiempoDeEspera;

    private Texture fondoTec;

    public PantallaSplashTec(Juego juego){
        super();
        this.juego = juego;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaTec = new Stage(vista, batch);
        Image imgFondo = new Image(fondoTec);

    }

    private void cargarTexturas() {
        fondoTec = new Texture("fondoSplashTec");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        escenaTec.draw();
        if((TimeUtils.millis() - tiempoDeEspera)> 3000){
            juego.setScreen(new MenuPrincipal(juego));
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
