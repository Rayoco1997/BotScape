
package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Representa un elemento gráfico del juego
 */

public class Objeto
{
    protected Sprite sprite;    // Imagen
    protected float centroX=sprite.getX()+sprite.getWidth()/2;
    protected float centroY=sprite.getY()+sprite.getHeight()/2;

    public Objeto() {

    }

    public Objeto(Texture textura, float x, float y) {
        sprite = new Sprite(textura);
        sprite.setPosition(x, y);
    }

    public void setCentroX(float x){
        sprite.setPosition(x-sprite.getWidth()/2,sprite.getY());
    }

    public void setCentroY(float y){
        sprite.setPosition(sprite.getX(),y-sprite.getHeight()/2);
    }

    public float getCentroY(){
        return centroY;
    }

    public float getCentroX(){
        return centroX;
    }

    public void dibujar(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
