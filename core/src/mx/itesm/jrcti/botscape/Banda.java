package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Julio on 21/04/2017.
 */

public class Banda extends Objeto {
    private final float VELOCIDAD_X = 4;      // Velocidad horizontal

    private Animation<TextureRegion> spriteAnimadoMov;
    private float timerAnimacion;

    private Body body, bodySen;
    private BodyDef bodydef, bodydefSen;
    private PolygonShape shape, shapeSen;
    private World world;
    private FixtureDef fix, fixSen;
    private boolean derecha;

    private TextureRegion[][] texturaBanda;

    public Banda(Texture textura, float x, float y, FixtureDef fix,World world, boolean derecha) {
        super(textura, x, y);

        TextureRegion texturaCompleta = new TextureRegion(textura);
        this.fix=fix;
        texturaBanda = texturaCompleta.split(646, 109);
        this.derecha = derecha;
        if(derecha) {
            spriteAnimadoMov = new Animation(0.10f, texturaBanda[2][0], texturaBanda[3][0],
                    texturaBanda[4][0]);
        } else{
            spriteAnimadoMov = new Animation(0.10f, texturaBanda[4][0],
                    texturaBanda[3][0], texturaBanda[2][0]);
        }
        spriteAnimadoMov.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;
        sprite = new Sprite(texturaBanda[0][0]);    // QUIETO
        sprite.setPosition(x, y);// Posición inicial


        shape = new PolygonShape();
        bodydef = new BodyDef();
        bodydef.type = BodyDef.BodyType.StaticBody;
        fix = new FixtureDef();
        fix.friction = .6f;
        fix.density = .1f;
        fix.restitution = 0f;
        bodydef.fixedRotation =  true;
        bodydef.position.set((sprite.getX()+sprite.getWidth()/2)/PantallaNivel.getPtM()
            , (sprite.getY()+sprite.getHeight()/2)/PantallaNivel.getPtM());
        shape.setAsBox((sprite.getWidth()/2-10)/PantallaNivel.getPtM(),
                sprite.getHeight()/2/ PantallaNivel.getPtM());
        fix.shape = shape;
        body = world.createBody(bodydef);
        body.setUserData(this);
        body.createFixture(fix);

        fixSen = new FixtureDef();
        bodydefSen = new BodyDef();
        fixSen.isSensor = true;
        fixSen.friction = 0f;
        fixSen.density = 0f;
        bodydefSen.type = BodyDef.BodyType.StaticBody;
        bodydefSen.position.set((sprite.getX() + sprite.getWidth()/2)/PantallaNivel.getPtM()
                , (sprite.getY()+sprite.getHeight()+5)/PantallaNivel.getPtM());
        shapeSen = new PolygonShape();
        shapeSen.setAsBox((sprite.getWidth()/2-20)/PantallaNivel.getPtM(),1/PantallaNivel.getPtM());
        fixSen.shape = shapeSen;
        bodySen = world.createBody(bodydefSen);
        bodySen.setUserData("sensorBanda");
        bodySen.createFixture(fixSen);

    }

    public void dibujar(SpriteBatch batch, float delta) {
        timerAnimacion += delta;
        // Frame que se dibujará
        TextureRegion region = spriteAnimadoMov.getKeyFrame(timerAnimacion);
        batch.draw(region, sprite.getX(), sprite.getY());
    }

    public boolean esDerecha(){
        return derecha;
    }
}
