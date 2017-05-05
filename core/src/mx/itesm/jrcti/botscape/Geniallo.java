package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Julio on 01/05/2017.
 */

public class Geniallo extends Objeto implements MovimientoAutomatico{

    private float speedX;
    private float speedY;
    private CircleShape circle;
    private BodyDef bodyDef;
    private Body body;
    private FixtureDef fix;
    private Animation<TextureRegion> spriteAnimado;
    private float timerAnimacion;

    private EstadoMovimiento estadoMovimiento;


    public Geniallo(Texture texturaBoss, float speedX, float posX, float posY, World world, Geniallo.EstadoMovimiento primerMovimiento) {
        super(texturaBoss, posX, posY);
        /*TextureRegion texturaCompleta = new TextureRegion(texturaBoss);
        TextureRegion[][] texturaPersonaje = texturaCompleta.split();

        spriteAnimado = new Animation(0.10f,);
        spriteAnimado.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;
        sprite = new Sprite(texturaPersonaje[][]);
        sprite.setPosition(posX,posY);
        estadoMovimiento = primerMovimiento;

        this.speedX = speedX;
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set();
        fix = new FixtureDef();
        fix.density = 10f;
        fix.friction = 1f;
        circle = new CircleShape();
        circle.setRadius();
        body = world.createBody(bodyDef);
        body.setUserData(this);
        body.createFixture(fix);
        */

    }


    @Override
    public void mover(float xMin, float xMax, float yMin, float yMax) {


    }

    @Override
    public void mover(float xMin, float xMax) {

    }

    public void dibujar(SpriteBatch batch, float delta){

    }

    public enum EstadoMovimiento{
        MOV_DERECHA,
        MOV_IZQUIERDA,
    }
}
