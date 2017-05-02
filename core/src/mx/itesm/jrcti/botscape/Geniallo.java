package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Julio on 01/05/2017.
 */

public class Geniallo extends Objeto{


    public Geniallo(Texture texturaBoss, float speedX, float posX, float posY, World world, BodyDef.BodyType type, FixtureDef fix) {
        super(texturaBoss, posX, posY);
    }


}
