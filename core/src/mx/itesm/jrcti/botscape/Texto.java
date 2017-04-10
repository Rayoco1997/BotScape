package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by rayoc on 20/03/2017.
 */

public class Texto {
    private BitmapFont font;

    public Texto() {
        font = new BitmapFont(Gdx.files.internal("Textos/fuenteCuadro.fnt"));
    }

    public Texto(String nivel){
        font= new BitmapFont(Gdx.files.internal("Textos/FuenteNivelNumero.fnt"));

    }

    public void mostrarMensaje(SpriteBatch batch, String mensaje, float x, float y) {
        GlyphLayout glyp = new GlyphLayout();
        glyp.setText(font, mensaje);
        float anchoTexto = glyp.width;
        font.draw(batch, glyp, x-anchoTexto/2, y);
    }
}
