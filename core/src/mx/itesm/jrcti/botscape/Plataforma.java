package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Cinthya on 15/03/2017.
 */

public class Plataforma extends Objeto {
    private float velX=0;
    private float velY=0;
    private float posInicialX=0;
    private float posInicialY=0;
    private EstadoMovimiento estadoMovimiento;


    public Plataforma(Texture texturaPlat,float speedX, float speedY, float posX, float posY, EstadoMovimiento primerEstado){
        velX=speedX;
        velY=speedY;
        sprite= new Sprite(texturaPlat);
        sprite.setPosition(posX,posY);
        posInicialX=this.getCentroX();
        posInicialY=this.getCentroY();
        estadoMovimiento=primerEstado;
   }



    public void mover(float xMin, float xMax, float yMin, float yMax, float delta) {
        switch (estadoMovimiento) {
            case MOV_DERECHA:
                if (sprite.getX()+sprite.getWidth() >= xMax) {
                    estadoMovimiento = EstadoMovimiento.MOV_ARRIBA;
                } else {
                    sprite.setPosition(this.getCentroX() + velX * delta, this.getCentroY());
                }
                break;
            case MOV_ARRIBA:
                if (sprite.getY()+sprite.getHeight() >= yMax) {
                    estadoMovimiento = EstadoMovimiento.MOV_IZQUIERDA;
                } else {
                    sprite.setPosition(this.getCentroX(), this.getCentroY() + velY * delta);
                }
                break;
            case MOV_IZQUIERDA:
                if (sprite.getX() <= xMin) {
                    estadoMovimiento = EstadoMovimiento.MOV_ABAJO;
                } else {
                    sprite.setPosition(this.getCentroX() - velX * delta, this.getCentroY());
                }
                break;
            case MOV_ABAJO:
                if (sprite.getY() <= yMax) {
                    estadoMovimiento = EstadoMovimiento.MOV_DERECHA;
                } else {
                    sprite.setPosition(this.getCentroX(), this.getCentroY() - velY * delta);
                }
                break;
        }
    }

    public enum EstadoMovimiento{
        MOV_DERECHA,
        MOV_IZQUIERDA,
        MOV_ARRIBA,
        MOV_ABAJO
    }

}
