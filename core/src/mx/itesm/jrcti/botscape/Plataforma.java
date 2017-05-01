package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Cinthya on 15/03/2017.
 */

public class Plataforma extends Objeto implements MovimientoAutomatico {
    private float velX=0;
    private float velY=0;
    private float posInicialX=0;
    private float posInicialY=0;
    private EstadoMovimiento estadoMovimiento;
    private Body body;
    private FixtureDef fix;
    private BodyDef bodyDef;
    private PolygonShape shape;





    public Plataforma(Texture texturaPlat,float speedX, float speedY, float posX, float posY,
                      EstadoMovimiento primerEstado, World world){
        super(texturaPlat, posX, posY);
        velX=speedX;
        velY=speedY;
        posInicialX=this.getCentroX();
        posInicialY=this.getCentroY();
        estadoMovimiento=primerEstado;
        bodyDef = new BodyDef();
        bodyDef.type= BodyDef.BodyType.KinematicBody;
        shape = new PolygonShape();
        fix = new FixtureDef();
        fix.friction = 1.1f;
        fix.density = .1f;
        fix.restitution = 0f;
        bodyDef.fixedRotation = true;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2)/PantallaNivel.getPtM()
                ,(sprite.getY()+10*sprite.getHeight()/11)/PantallaNivel.getPtM());
        shape.setAsBox(sprite.getWidth()/2/PantallaNivel.getPtM(),
                sprite.getHeight()/11/PantallaNivel.getPtM());
        fix.shape = shape;
        body = world.createBody(bodyDef);
        body.setUserData(this);
        body.createFixture(fix);

   }


    @Override
    public void mover(float xMin, float xMax, float yMin, float yMax) {
        sprite.setPosition((body.getPosition().x * PantallaNivel.getPtM()) - sprite.getWidth() / 2,
            (body.getPosition().y * PantallaNivel.getPtM()) - 10*sprite.getHeight()/11);
        switch (estadoMovimiento) {
            case MOV_DERECHA:
                if (sprite.getX()+sprite.getWidth() >= xMax) {
                    estadoMovimiento = EstadoMovimiento.MOV_ARRIBA;
                } else {
                    //sprite.setPosition(sprite.getX() + velX, sprite.getY());
                    body.setLinearVelocity(velX,0f);
                }
                break;
            case MOV_ARRIBA:
                if (sprite.getY()+sprite.getHeight() >= yMax) {
                    estadoMovimiento = EstadoMovimiento.MOV_IZQUIERDA;
                } else {
                    //sprite.setPosition(sprite.getX(), sprite.getY() + velY);
                    body.setLinearVelocity(0f,velY);

                }
                break;
            case MOV_IZQUIERDA:
                if (sprite.getX() <= xMin) {
                    estadoMovimiento = EstadoMovimiento.MOV_ABAJO;
                } else {
                    //sprite.setPosition(sprite.getX() - velX, sprite.getY());
                    body.setLinearVelocity(-velX,0f);
                }
                break;
            case MOV_ABAJO:
                if (sprite.getY() <= yMin) {
                    estadoMovimiento = EstadoMovimiento.MOV_DERECHA;
                } else {
                    //sprite.setPosition(sprite.getX(), sprite.getY() - velY);
                    body.setLinearVelocity(0f,-velY);
                }
                break;
        }


    }

    @Override
    public void mover(float xMin, float xMax) {
        return;
    }


    public enum EstadoMovimiento{
        MOV_DERECHA,
        MOV_IZQUIERDA,
        MOV_ARRIBA,
        MOV_ABAJO
    }

}
