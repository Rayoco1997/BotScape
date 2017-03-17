package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Cinthya on 17/03/2017.
 */

public class Enemigo extends Objeto implements MovimientoAutomatico {
    private float velX=0;
    private float velY=0;
    private float posInicialX=0;
    private float posInicialY=0;
    private EstadoMovimiento estadoMovimiento;


    public Enemigo(Texture texturaLug, float speedX, float speedY, float posX, float posY, Enemigo.EstadoMovimiento primerEstado){
        super(texturaLug, posX, posY);
        velX=speedX;
        velY=speedY;
        posInicialX=this.getCentroX();
        posInicialY=this.getCentroY();
        estadoMovimiento=primerEstado;
    }


    @Override
    public void mover(float xMin, float xMax, float yMin, float yMax) {
        return;
    }

    @Override
    public void mover(float xMin, float xMax) {
        switch (estadoMovimiento) {
            case MOV_DERECHA:
                if (sprite.getX()+sprite.getWidth() >= xMax) {
                    estadoMovimiento = Enemigo.EstadoMovimiento.MOV_IZQUIERDA;
                    System.out.println(this.getCentroX());
                } else {
                    System.out.println(sprite.getX());
                    sprite.setPosition(sprite.getX() + velX, sprite.getY());
                }
                break;
            case MOV_IZQUIERDA:
                if (sprite.getX() <= xMin) {
                    estadoMovimiento = Enemigo.EstadoMovimiento.MOV_DERECHA;
                } else {
                    sprite.setPosition(sprite.getX() - velX, sprite.getY());
                }
                break;
        }

    }



    public enum EstadoMovimiento{
        MOV_DERECHA,
        MOV_IZQUIERDA,
    }

}
