package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Tommy on 15/02/2017.
 */

public class PantallaPausa extends Pantalla {
    //Texturas
    private Texture texturaFondo;
    private Texture texturaButtonReanudar;
    private Texture texturaButtonSelecNivel;
    private Texture texturaButtonMenuPrincipal;

    //SpriteBatch
    private SpriteBatch batch;

    //escena
    private Stage escenaPantallaPausa;

    public PantallaPausa(Juego juego){
        this.juego= juego;

    }
    @Override
    public void show() {
        cargarTexturas();
        crearObjetos();


    }

    private void crearObjetos() {
        batch= new SpriteBatch();
        escenaPantallaPausa= new Stage(vista,batch);
        Image imgFondo= new Image(texturaFondo);
        Image imgButtonReanudar = new Image(texturaButtonReanudar);
        Image imgButtonSeleecionarNivel= new Image(texturaButtonSelecNivel);
        Image imgButtonMenuPrin= new Image(texturaButtonMenuPrincipal);

        //Para los botones
        TextureRegionDrawable trdButtonReanudar= new TextureRegionDrawable(new TextureRegion(texturaButtonReanudar));
        TextureRegionDrawable trdButtonSeleccionarNivel= new TextureRegionDrawable(new TextureRegion(texturaButtonSelecNivel));
        TextureRegionDrawable trdButtonMenuPrincipal= new TextureRegionDrawable(new TextureRegion(texturaButtonMenuPrincipal));

        escenaPantallaPausa.addActor(imgFondo);

    }

    //cargar las texturas
    private void cargarTexturas() {
        texturaFondo= new Texture("PausaFondo.png");
        texturaButtonReanudar= new Texture("PausaButtonReanudar.png");
        texturaButtonMenuPrincipal= new Texture("PausaButtonMenuPrin.png");
        texturaButtonSelecNivel= new Texture("PausaButtonSeleccionarNivel.png");

    }


    /*private void crearCamara() {
        camara= new OrthographicCamera(ANCHO,ALTO);
        camara.position.set(ANCHO/2,ALTO/2,0);
        camara.update();
        vista = new StretchViewport(ANCHO,ALTO,camara);
    }*/

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
