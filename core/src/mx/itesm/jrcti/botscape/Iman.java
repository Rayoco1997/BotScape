package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
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
    private Body bodySen;
    private FixtureDef fix;
    private FixtureDef fixSen;
    private BodyDef bodyDef;
    private BodyDef bodyDefSen;
    private PolygonShape shapeSen, shape;

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
        shape = new PolygonShape();
        fix = new FixtureDef();
        fix.friction = 0.1f;
        fix.density = .1f;
        fix.restitution = 0f;
        bodyDef.fixedRotation = true;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2)/PantallaNivel.getPtM()
                ,(sprite.getY() + sprite.getHeight()/67)/PantallaNivel.getPtM());
        shape.setAsBox(sprite.getWidth()/2/PantallaNivel.getPtM(),sprite.getHeight()/64/PantallaNivel.getPtM());
        fix.shape = shape;
        body = world.createBody(bodyDef);
        body.setUserData(this);
        body.createFixture(fix);

        fixSen = new FixtureDef();
        fixSen.isSensor = true;
        fixSen.friction = 0f;
        fixSen.density = 0.01f;
        bodyDefSen = new BodyDef();
        bodyDefSen.type = BodyDef.BodyType.KinematicBody;
        bodyDefSen.position.set((sprite.getX() + sprite.getWidth()/2)/PantallaNivel.getPtM()
                ,(sprite.getY()-sprite.getHeight()/6)/PantallaNivel.getPtM());
        shapeSen = new PolygonShape();
        shapeSen.setAsBox(sprite.getWidth()/2/PantallaNivel.getPtM(),350/PantallaNivel.getPtM());
        fixSen.shape = shapeSen;
        bodySen = world.createBody(bodyDefSen);
        bodySen.setUserData("sensorIman");
        bodySen.createFixture(fixSen);
        //Gdx.app.log("Iman creadoSprite","x " + sprite.getX()+" y "+sprite.getY());
        //Gdx.app.log("Iman creadoBody","x "+body.getPosition().x*PantallaNivel.getPtM()+" y "+body.getPosition().y*PantallaNivel.getPtM());
    }

    @Override
    public void mover(float xMin, float xMax, float yMin, float yMax) {
        sprite.setPosition((body.getPosition().x * PantallaNivel.getPtM()) - sprite.getWidth()/2,
                (body.getPosition().y * PantallaNivel.getPtM())-sprite.getHeight()/50);


        //Gdx.app.log("Body","x "+body.getPosition().x*PantallaNivel.getPtM() + " y " + body.getPosition().y*PantallaNivel.getPtM());
        //Gdx.app.log("Sprite","x "+sprite.getX() + " y "+sprite.getY());
        switch (estadoMovimiento) {
            case MOV_DERECHA:
                if (sprite.getX()+sprite.getWidth() >= xMax) {
                    estadoMovimiento = Plataforma.EstadoMovimiento.MOV_ARRIBA;
                } else {
                    //sprite.setPosition(sprite.getX() + velX, sprite.getY());
                    body.setLinearVelocity(velX,0f);
                    bodySen.setLinearVelocity(velX,0f);
                }
                break;
            case MOV_ARRIBA:
                if (sprite.getY() > yMax) {
                    //Gdx.app.log("Iman","Pase de moverme arriba hacia la izquierda con y " + sprite.getY()+ " y yMax en " + yMax);
                    //Gdx.app.log("Iman","Body y "+body.getPosition().y*PantallaNivel.getPtM());
                    estadoMovimiento = Plataforma.EstadoMovimiento.MOV_IZQUIERDA;
                } else {
                    //sprite.setPosition(sprite.getX(), sprite.getY() + velY);
                    body.setLinearVelocity(0f,velY);
                    bodySen.setLinearVelocity(0f,velY);

                }
                break;
            case MOV_IZQUIERDA:
                if (sprite.getX() <= xMin) {
                    estadoMovimiento = Plataforma.EstadoMovimiento.MOV_ABAJO;
                } else {
                    //sprite.setPosition(sprite.getX() - velX, sprite.getY());
                    body.setLinearVelocity(-velX,0f);
                    bodySen.setLinearVelocity(-velX,0f);
                }
                break;
            case MOV_ABAJO:
                if (sprite.getY() <= yMin) {
                    estadoMovimiento = Plataforma.EstadoMovimiento.MOV_DERECHA;
                } else {
                    //sprite.setPosition(sprite.getX(), sprite.getY() - velY);
                    body.setLinearVelocity(0f,-velY);
                    bodySen.setLinearVelocity(0f,-velY);
                }
                break;
        }
    }

    @Override
    public void mover(float xMin, float xMax) {

    }


}
