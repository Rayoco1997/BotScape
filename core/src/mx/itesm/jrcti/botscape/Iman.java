package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Julio on 17/04/2017.
 */

public class Iman extends Objeto implements MovimientoAutomatico {

    private float velX=0;
    private float velY=0;
    private float posInicialX=0;
    private float posInicialY=0;
    private Plataforma.EstadoMovimiento estadoMovimiento;
    private Body body;
    private FixtureDef fix;
    private BodyDef bodyDef;
    private EdgeShape shape;

    public Iman(Texture texturaIman, float speedX, float speedY, float posX, float posY,
                Plataforma.EstadoMovimiento primerEstado, World world){
        super(texturaIman, posX, posY);
        velX=speedX;
        velY=speedY;
        posInicialX=this.getCentroX();
        posInicialY=this.getCentroY();
        estadoMovimiento=primerEstado;
        bodyDef = new BodyDef();
        bodyDef.type= BodyDef.BodyType.KinematicBody;
        shape = new EdgeShape();
        fix = new FixtureDef();
        fix.friction = 10f;
        fix.density = .1f;
        fix.restitution = 0f;
        bodyDef.fixedRotation = true;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2)/PantallaNivel.getPtM()
                ,(sprite.getY()+sprite.getHeight()/11)/PantallaNivel.getPtM());
        shape.set((sprite.getX()+20)/PantallaNivel.getPtM(),sprite.getY()/PantallaNivel.getPtM(),
                (sprite.getX()+sprite.getWidth())/PantallaNivel.getPtM(),
                sprite.getY()/PantallaNivel.getPtM());
        fix.shape = shape;
        body = world.createBody(bodyDef);
        body.setUserData(this);
        body.createFixture(fix);
    }

    @Override
    public void mover(float xMin, float xMax, float yMin, float yMax) {
        sprite.setPosition((body.getPosition().x * PantallaNivel.getPtM()) + 3*sprite.getWidth()/4,
                (body.getPosition().y * PantallaNivel.getPtM()) + 3*sprite.getHeight()/11);
        switch (estadoMovimiento) {
            case MOV_DERECHA:
                if (sprite.getX()+sprite.getWidth() >= xMax) {
                    estadoMovimiento = Plataforma.EstadoMovimiento.MOV_ARRIBA;
                } else {
                    //sprite.setPosition(sprite.getX() + velX, sprite.getY());
                    body.setLinearVelocity(velX,0f);
                }
                break;
            case MOV_ARRIBA:
                if (sprite.getY()+2*sprite.getHeight()/10 >= yMax) {
                    estadoMovimiento = Plataforma.EstadoMovimiento.MOV_IZQUIERDA;
                } else {
                    //sprite.setPosition(sprite.getX(), sprite.getY() + velY);
                    body.setLinearVelocity(0f,velY);

                }
                break;
            case MOV_IZQUIERDA:
                if (sprite.getX() <= xMin) {
                    estadoMovimiento = Plataforma.EstadoMovimiento.MOV_ABAJO;
                } else {
                    //sprite.setPosition(sprite.getX() - velX, sprite.getY());
                    body.setLinearVelocity(-velX,0f);
                }
                break;
            case MOV_ABAJO:
                if (sprite.getY() <= yMin) {
                    estadoMovimiento = Plataforma.EstadoMovimiento.MOV_DERECHA;
                } else {
                    //sprite.setPosition(sprite.getX(), sprite.getY() - velY);
                    body.setLinearVelocity(0f,-velY);
                }
                break;
        }
    }

    @Override
    public void mover(float xMin, float xMax) {

    }
}
