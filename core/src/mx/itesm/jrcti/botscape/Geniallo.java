package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
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
    private boolean vivo ;

    private Animation<TextureRegion> spriteAnimadoMuerte;
    private boolean resetTimer;


    public Geniallo(Texture texturaBoss, Texture texturaMuerto, float speedX, float posX, float posY, World world, Geniallo.EstadoMovimiento primerMovimiento) {
        super(texturaBoss, posX, posY);
        TextureRegion texturaCompleta = new TextureRegion(texturaBoss);
        TextureRegion[][] texturaPersonaje = texturaCompleta.split(680,576);

        TextureRegion texturaMuerte = new TextureRegion(texturaMuerto);
        TextureRegion[][] texturaExplotar = texturaMuerte.split(737,576);
        Gdx.app.log("Geniallo","Me divido en " + texturaExplotar[0].length);

        vivo = true;
        resetTimer = false;
        spriteAnimado = new Animation(0.25f,texturaPersonaje[0][0], texturaPersonaje[0][1], texturaPersonaje[0][2], texturaPersonaje[0][3]);
        spriteAnimadoMuerte = new Animation(0.20f, texturaExplotar[0][0],texturaExplotar[0][1],texturaExplotar[0][2],texturaExplotar[0][3],texturaExplotar[0][4],
                texturaExplotar[0][5],texturaExplotar[0][6],texturaExplotar[0][7],texturaExplotar[0][8],texturaExplotar[0][9],texturaExplotar[0][10],texturaExplotar[0][11],
                texturaExplotar[0][12],texturaExplotar[0][13],texturaExplotar[0][14],texturaExplotar[0][15],texturaExplotar[0][16],texturaExplotar[0][17],
                texturaExplotar[0][18],texturaExplotar[0][19]);
        spriteAnimado.setPlayMode(Animation.PlayMode.LOOP);
        spriteAnimadoMuerte.setPlayMode(Animation.PlayMode.NORMAL);
        timerAnimacion = 0;
        sprite = new Sprite(texturaPersonaje[0][0]);
        sprite.setPosition(posX,posY);
        estadoMovimiento = primerMovimiento;

        this.speedX = speedX;
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set((sprite.getX() + 5*sprite.getWidth()/8+20)/PantallaNivel.getPtM(),
                (sprite.getY()+sprite.getHeight()/2)/PantallaNivel.getPtM());
        fix = new FixtureDef();
        fix.density = 10f;
        fix.friction = 1f;
        circle = new CircleShape();
        circle.setRadius(240/PantallaNivel.getPtM());
        fix.shape = circle;
        body = world.createBody(bodyDef);
        body.setUserData(this);
        body.createFixture(fix);


    }


    @Override
    public void mover(float xMin, float xMax, float yMin, float yMax) {


    }

    @Override
    public void mover(float xMin, float xMax) {

        switch (estadoMovimiento) {
            case MOV_DERECHA:
                if (sprite.getX() + sprite.getWidth()/2 >= xMax) {
                    estadoMovimiento = EstadoMovimiento.MOV_IZQUIERDA;
                    //System.out.println(this.getCentroX());
                } else {
                    //System.out.println(sprite.getX());
                    body.setLinearVelocity(speedX,0f);
                }
                break;
            case MOV_IZQUIERDA:
                if (sprite.getX() + sprite.getWidth()/2 <= xMin) {
                    estadoMovimiento = EstadoMovimiento.MOV_DERECHA;
                } else {
                    body.setLinearVelocity(-speedX,0f);
                    break;
                }

        }
        sprite.setPosition((body.getPosition().x*PantallaNivel.getPtM())-sprite.getWidth()/3,
                (body.getPosition().y*PantallaNivel.getPtM())-3*sprite.getHeight()/7);
    }

    public void dibujar(SpriteBatch batch, float delta) {
        if (vivo) {
            TextureRegion region = spriteAnimado.getKeyFrame(timerAnimacion);
            timerAnimacion += delta;

            batch.draw(region, sprite.getX(), sprite.getY());
        }/* else {
            if (resetTimer) {
                timerAnimacion = 0;
                resetTimer = false;
            }
            if(!spriteAnimadoMuerte.isAnimationFinished(timerAnimacion)) {
                TextureRegion region = spriteAnimadoMuerte.getKeyFrame(timerAnimacion);
                timerAnimacion += delta;
                batch.draw(region, sprite.getX(), sprite.getY());
            }
        }*/

    }

    public void morir(World world){
        world.destroyBody(body);
        vivo = false;
        resetTimer = true;
    }

    public boolean isVivo() {
        return vivo;
    }

    public enum EstadoMovimiento{
        MOV_DERECHA,
        MOV_IZQUIERDA,
    }
}
