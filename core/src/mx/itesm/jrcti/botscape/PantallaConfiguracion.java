package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

/**
 * Created by Tommy on 15/03/2017.
 */

public class PantallaConfiguracion extends Pantalla {
    //CREANDO LAS TEXTURAS PARA LA PANTALLA
    private Texture texturaFondo;
    private Texture texturaButtonSonido;
    private Texture texturaButtonMusica;
    private Texture texturaTextConfig;
    private Texture texturaButtonRegresar;

    private SpriteBatch batch;

    private Stage escenaPantallaConfig;

    public PantallaConfiguracion(Juego juego){
        this.juego= juego;
    }

    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();

    }

    private void crearObjetos() {
    }

    private void cargarTexturas(){
        texturaFondo = new Texture("Fondos/PausaFondo.jpg");


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
