package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Tommy on 15/02/2017.
 */

public class PantallaPausa extends Pantalla {
    //Texturas
    private Texture texturaFondo;
    private Texture buttonReanudar;
    private Texture buttonSelecNivel;
    private Texture buttonMenuPrincipal;

    //SpriteBatch
    private SpriteBatch batch;

    //escena
    private Stage escenaPantallaPausa;

    public PantallaPausa(Juego juego){
        this.juego= juego;

    }
    @Override
    public void show() {
        crearCamara();
        cargarTexturas();
        crearObjetos();


    }

    private void cargarTexturas() {
        texturaFondo= new Texture("")

    }


    private void crearCamara() {
        camara= new OrthographicCamera(ANCHO,ALTO);
        camara.position.set(ANCHO/2,ALTO/2,0);
        camara.update();
        vista = new StretchViewport(ANCHO,ALTO,camara);
    }

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
