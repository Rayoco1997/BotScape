package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Julio on 15/02/2017.
 */

public class MenuSeleccionNivel extends Pantalla {

    //Imagenes a usar
    private Texture texturaFondo;
    private Texture texturaBtnNivel1;
    private Texture texturaBtnNivel2;
    private Texture texturaBtnNivel3;
    private Texture texturaBtnRegresar;

    // Ayuda a dibujar
    private SpriteBatch batch;

    //Los 4 botones que muestra el menu
    private final int NUM_BOTONES = 4;
    private Array<ImageButton> arrBtn;

    //Escenas
    private Stage escenaSeleccionNivel;

    public MenuSeleccionNivel(Juego juego){
        super();
        this.juego=juego;
    }

    @Override
    public void show() {
        cargarTexturas();
    }

    private void cargarTexturas() {

        texturaFondo = new Texture("SeleccionNivelFondo.png");
        texturaBtnNivel1 = new Texture("SeleccionNivelBtnNivel1.png");
        texturaBtnNivel2 = new Texture("SeleccionNivelBtnNivel2.png");
        texturaBtnNivel3 = new Texture("SeleccionNivelBtnNivel3.png");
        texturaBtnRegresar = new Texture("SeleccionNivelBtnBack.png");

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
