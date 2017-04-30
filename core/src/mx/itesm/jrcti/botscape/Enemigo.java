package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Cinthya on 17/03/2017.
 */

public class Enemigo extends Objeto implements MovimientoAutomatico {
    private float velX=0;
    private float velY=0;
    private float posInicialX=0;
    private float posInicialY=0;

    private Body body;
    private BodyDef bodydef;
    private PolygonShape shape;
    private Animation<TextureRegion> spriteAnimado;         // Animación caminando
    private float timerAnimacion;                           // Tiempo para cambiar frames de la animación

    private EstadoMovimiento estadoMovimiento;


    public Enemigo(Texture texturaLug, float speedX, float posX, float posY,
                   Enemigo.EstadoMovimiento primerEstado, World world, BodyDef.BodyType type,
                   FixtureDef fix){
        super(texturaLug, posX, posY);
        TextureRegion texturaCompleta = new TextureRegion(texturaLug);

        TextureRegion[][] texturaPersonaje = texturaCompleta.split(233, 195);
        // Crea la animación con tiempo de 0.10 segundos entre frames.

        spriteAnimado = new Animation(0.10f, texturaPersonaje[0][1],
                texturaPersonaje[0][2], texturaPersonaje[0][3], texturaPersonaje[0][4],
                texturaPersonaje[0][5], texturaPersonaje[0][6], texturaPersonaje[0][7],
                texturaPersonaje[0][8], texturaPersonaje[0][9], texturaPersonaje[0][10],
                texturaPersonaje[0][11], texturaPersonaje[0][12]);
        // Animación infinita
        spriteAnimado.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        sprite = new Sprite(texturaPersonaje[0][0]);
        sprite.setPosition(posX,posY);
        estadoMovimiento=primerEstado;


        velX=speedX;
        bodydef = new BodyDef();
        bodydef.type = type;
        bodydef.fixedRotation = true;
        bodydef.position.set((sprite.getX() + sprite.getWidth() / 2) / PantallaNivel.getPtM(),
                (sprite.getY() + sprite.getHeight() / 2) / PantallaNivel.getPtM());
        shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 4 / PantallaNivel.getPtM(), 2*sprite.getHeight() / 5 /PantallaNivel.getPtM());
        fix.shape = shape;
        body = world.createBody(bodydef);
        body.setUserData(this);
        /*body.setUserData(this.getClass());
        body = world.createBody(bodydef);*/
        body.createFixture(fix);
    }


    @Override
    public void mover(float xMin, float xMax, float yMin, float yMax) {
        return;
    }

    @Override
    public void mover(float xMin, float xMax) {
        switch (estadoMovimiento) {
            case MOV_DERECHA:
                if (sprite.getX() + sprite.getWidth() >= xMax) {
                    estadoMovimiento = Enemigo.EstadoMovimiento.MOV_IZQUIERDA;
                    //System.out.println(this.getCentroX());
                } else {
                    //System.out.println(sprite.getX());
                    body.setLinearVelocity(velX,0f);
                }
                break;
            case MOV_IZQUIERDA:
                if (sprite.getX() <= xMin) {
                    estadoMovimiento = Enemigo.EstadoMovimiento.MOV_DERECHA;
                } else {
                    body.setLinearVelocity(-velX,0f);
                    break;
                }

        }
        sprite.setPosition((body.getPosition().x*PantallaNivel.getPtM())-sprite.getWidth()/2,
                (body.getPosition().y*PantallaNivel.getPtM())-sprite.getHeight()/2);
    }

    public void dibujar(SpriteBatch batch, float delta){
        TextureRegion region = spriteAnimado.getKeyFrame(timerAnimacion);

        timerAnimacion += delta;
        // Frame que se dibujará
        if (estadoMovimiento == EstadoMovimiento.MOV_IZQUIERDA) {
            if (!region.isFlipX()) {
                region.flip(true, false);
            }
        } else {
            if (region.isFlipX()) {
                region.flip(true, false);
            }
        }
        batch.draw(region, sprite.getX(), sprite.getY());


    }



    public enum EstadoMovimiento{
        MOV_DERECHA,
        MOV_IZQUIERDA,
    }

}
