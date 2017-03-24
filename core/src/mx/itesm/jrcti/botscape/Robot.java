package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


/**
 * Created by rayoc on 08/03/2017.
 */

public class Robot extends Objeto {
    private final float VELOCIDAD_X = 4;      // Velocidad horizontal

    private Animation<TextureRegion> spriteAnimado;         // Animación caminando
    private float timerAnimacion;                           // Tiempo para cambiar frames de la animación

    private EstadoMovimiento estadoMovimiento = EstadoMovimiento.QUIETO;
    private EstadoMovimiento ultimoEstadoMov = EstadoMovimiento.QUIETO;
    private EstadoSalto estadoSalto = EstadoSalto.EN_PISO;
    private Habilidad habilidad = Habilidad.NADA;

    private Body body;
    private BodyDef bodydef;
    private PolygonShape shape;

    // Recibe una imagen con varios frames (ver marioSprite.png)
    public Robot(Texture textura, float x, float y, World world, BodyDef.BodyType type,
                 FixtureDef fix) {
        super(textura, x, y);
        // Lee la textura como región
        TextureRegion texturaCompleta = new TextureRegion(textura);

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
        // Crea el sprite con el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[0][0]);    // QUIETO
        sprite.setPosition(x, y);// Posición inicial

        bodydef = new BodyDef();
        bodydef.type = type;
        bodydef.fixedRotation = true;
        bodydef.position.set((sprite.getX() + sprite.getWidth() / 2) / 100f,
                (sprite.getY() + sprite.getHeight() / 2) / 100f);
        shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 3 / 100f, sprite.getHeight() / 2 / 100f);
        fix.shape = shape;
        body = world.createBody(bodydef);
        body.setUserData(this);
        /*body.setUserData(this.getClass());
        body = world.createBody(bodydef);*/
        body.createFixture(fix);
    }

    // Dibuja el personaje
    public void dibujar(SpriteBatch batch) {
        // Dibuja el personaje dependiendo del estadoMovimiento
        sprite.setPosition((body.getPosition().x * NivelTutorial.PIXELS_TO_METERS) - sprite.getWidth() / 2,
                (body.getPosition().y * NivelTutorial.PIXELS_TO_METERS) - sprite.getHeight()/ 2);
        switch (estadoMovimiento) {
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                timerAnimacion += Gdx.graphics.getDeltaTime();
                // Frame que se dibujará
                TextureRegion region = spriteAnimado.getKeyFrame(timerAnimacion);
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
                break;
            case QUIETO:
                if(ultimoEstadoMov==EstadoMovimiento.MOV_DERECHA){
                    if(sprite.isFlipX())
                        sprite.flip(true,false);
                } else if(ultimoEstadoMov==EstadoMovimiento.MOV_IZQUIERDA){
                    if(!sprite.isFlipX()){
                        sprite.flip(true,false);
                    }
                }

                sprite.draw(batch); // Dibuja el sprite estático
                break;
        }
    }

    // Actualiza el sprite, de acuerdo al estadoMovimiento y estadoSalto
    public void actualizar(TiledMap mapa) {
        switch (estadoMovimiento) {
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                moverHorizontal(mapa);
                break;
        }
        switch (estadoSalto) {
            case SUBIENDO:
            case BAJANDO:
                moverVertical(mapa);
                break;
        }
    }

    private void moverVertical(TiledMap mapa) {
        if(estadoSalto==EstadoSalto.SUBIENDO)
            body.applyForceToCenter(0f,80f,true);
        estadoSalto=EstadoSalto.BAJANDO;
    }



    // Mueve el personaje a la derecha/izquierda, prueba choques con paredes
    private void moverHorizontal(TiledMap mapa) {
        TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(1);
        if(estadoMovimiento==EstadoMovimiento.MOV_DERECHA) {
            // Revisar si toca una moneda (pies)
            int x = (int)((sprite.getX()/64)+64);
            int y = (int)((sprite.getY()/64)+64);
            TiledMapTileLayer.Cell celda = capa.getCell(x,y);
            if (celda!=null ) {
                Object tipo = celda.getTile().getProperties().get("tipo");
                if (!"puerta".equals(tipo) ) {
                    Gdx.app.log("moverHorizontal","No choco con puerta");
                    if(body.getLinearVelocity().x<3f)
                        body.applyForceToCenter(10f, 0f, true);
                    else
                        body.setLinearVelocity(3f,body.getLinearVelocity().y);
                } else{
                    Gdx.app.log("verga","verga");
                    body.setLinearVelocity(0f,body.getLinearVelocity().y);
                }
            } else {
                if (body.getLinearVelocity().x > 3f)
                    body.applyForceToCenter(10f, 0f, true);
                else
                    body.setLinearVelocity(3f, body.getLinearVelocity().y);
            }
        }
        else if(estadoMovimiento==EstadoMovimiento.MOV_IZQUIERDA) {
            int x = (int)((sprite.getX()/64));
            int y = (int)((sprite.getY()/64)+64);
            TiledMapTileLayer.Cell celda = capa.getCell(x,y);
            if (celda!=null ) {
                Object tipo = celda.getTile().getProperties().get("tipo");
                if (!"puerta".equals(tipo) ) {
                    Gdx.app.log("moverHorizontal","Choco con puerta");
                    if(body.getLinearVelocity().x<-3f)
                        body.applyForceToCenter(-10f, 0f, true);
                    else
                        body.setLinearVelocity(-3f,body.getLinearVelocity().y);
                } else{
                    body.setLinearVelocity(0f,body.getLinearVelocity().y);
                }
            } else {
                if (body.getLinearVelocity().x > -3f)
                    body.applyForceToCenter(-10f, 0f, true);
                else
                    body.setLinearVelocity(-3f, body.getLinearVelocity().y);
            }
        }
    }

    public void frenar(){
        if(estadoMovimiento==EstadoMovimiento.MOV_DERECHA){
            body.setLinearVelocity(body.getLinearVelocity().x/10,body.getLinearVelocity().y);

        } else if(estadoMovimiento==EstadoMovimiento.MOV_IZQUIERDA){
            body.setLinearVelocity(body.getLinearVelocity().x/10,body.getLinearVelocity().y);

        }
        setEstadoMovimiento(EstadoMovimiento.QUIETO);
        //estadoMovimiento = EstadoMovimiento.QUIETO;
    }

    public boolean recolectarMiniVi(TiledMap mapa) {
        // Revisar si toca una moneda (pies)
        TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(2);
        int x = (int)((sprite.getX()/64));
        int y = (int)(sprite.getY()/64);
        TiledMapTileLayer.Cell celda = capa.getCell(x,y);
        if (celda!=null ) {
            Object tipo = celda.getTile().getProperties().get("tipo");
            if ("miniVi".equals(tipo) ) {
                Gdx.app.log("robot","true, atrapo miniVi");
                capa.setCell(x,y,null);    // Borra la moneda del mapa
                //capa.setCell(x, y, capa.getCell(0, 4)); // Cuadro azul en lugar de la moneda
                return true;
            }
        }
        return false;
    }

    public boolean moverPalanca(TiledMap mapa) {
        // Revisar si toca una moneda (pies)
        TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(2);
        int x = (int)(sprite.getX()/64);
        int y = (int)(sprite.getY()/64)+1;
        TiledMapTileLayer.Cell celda = capa.getCell(x,y);
        if (celda!=null ) {
            Object tipo = celda.getTile().getProperties().get("tipo");
            if ( "palanca".equals(tipo) ) {
                //capa.setCell(x,y,null);    // Borra la moneda del mapa
                capa.setCell(x,y,capa.getCell(0,4)); // Cuadro azul en lugar de la moneda
                mapa.getLayers().remove(1);
                return true;
            }
        }
        return false;
    }


    // Accesor de estadoMovimiento
    public EstadoMovimiento getEstadoMovimiento() {
        return estadoMovimiento;
    }

    public EstadoSalto getEstadoSalto() {
        return estadoSalto;
    }

    public Habilidad getHabilidad(){
        return habilidad;
    }

    // Modificador de estadoMovimiento
    public void setEstadoMovimiento(EstadoMovimiento estadoMovimiento) {
        if (this.estadoMovimiento!=EstadoMovimiento.QUIETO) {
            ultimoEstadoMov = this.getEstadoMovimiento();
        }
        this.estadoMovimiento = estadoMovimiento;
    }
    public void setEstadoSalto(EstadoSalto estadoSalto) {
        this.estadoSalto = estadoSalto;
    }

    public void setHabilidad(Habilidad habilidad) {

        this.habilidad = habilidad;
    }


    public enum EstadoMovimiento {
        QUIETO,
        MOV_IZQUIERDA,
        MOV_DERECHA,
    }

    public enum Habilidad {
        CORRER,
        JETPACK,
        ARMA,
        INVULNERABLE,
        NADA
    }

    public enum EstadoSalto {
        SUBIENDO,
        BAJANDO,
        EN_PISO,
    }
}
