package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;


/**
 * Created by rayoc on 08/03/2017.
 */

public class Robot extends Objeto {
    private final float VELOCIDAD_X = 4;      // Velocidad horizontal

    private Animation<TextureRegion> spriteAnimadoMov;         // Animación caminando
    private Animation<TextureRegion> spriteAnimadoSalto;         // Animación saltando
    private float timerAnimacion;                           // Tiempo para cambiar frames de la animación

    private EstadoMovimiento estadoMovimiento = EstadoMovimiento.QUIETO;
    private EstadoMovimiento ultimoEstadoMov = EstadoMovimiento.QUIETO;
    private EstadoSalto estadoSalto = EstadoSalto.EN_PISO;
    private Habilidad habilidad = Habilidad.NADA;

    private Body body;
    private BodyDef bodydef;
    private PolygonShape shape;

    private int vidas = 3;
    private final int TIEMPO_INV_INICIAL = 90;
    private int tiempoInv = 90;

    // Recibe una imagen con varios frames (ver marioSprite.png)
    public Robot(Texture textura, float x, float y, World world, BodyDef.BodyType type,
                 FixtureDef fix) {
        super(textura, x, y);
        // Lee la textura como región
        TextureRegion texturaCompleta = new TextureRegion(textura);

        TextureRegion[][] texturaPersonaje = texturaCompleta.split(233, 195);
        // Crea la animación con tiempo de 0.10 segundos entre frames.
        spriteAnimadoMov = new Animation(0.10f, texturaPersonaje[0][1],
                texturaPersonaje[0][2], texturaPersonaje[0][3], texturaPersonaje[0][4],
                texturaPersonaje[0][5], texturaPersonaje[0][6], texturaPersonaje[0][7],
                texturaPersonaje[0][8], texturaPersonaje[0][9], texturaPersonaje[0][10],
                texturaPersonaje[0][11], texturaPersonaje[0][12]);
        //Crea la animación de salto
        spriteAnimadoSalto = new Animation(0.25f, texturaPersonaje[0][13],
                texturaPersonaje[0][14], texturaPersonaje[0][15], texturaPersonaje[0][16],
                texturaPersonaje[0][17], texturaPersonaje[0][18], texturaPersonaje[0][19],
                texturaPersonaje[0][20], texturaPersonaje[0][21], texturaPersonaje[0][22],
                texturaPersonaje[0][23], texturaPersonaje[0][24]);
        // Animación infinita
        spriteAnimadoMov.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite con el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[0][0]);    // QUIETO
        sprite.setPosition(x, y);// Posición inicial

        bodydef = new BodyDef();
        bodydef.type = type;
        bodydef.fixedRotation = true;
        bodydef.position.set((sprite.getX() + sprite.getWidth() / 2) / PantallaNivel.getPtM(),
                (sprite.getY() + sprite.getHeight() / 2) / PantallaNivel.getPtM());
        shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 3 / PantallaNivel.getPtM(), sprite.getHeight() / 2 / PantallaNivel.getPtM());
        fix.shape = shape;
        body = world.createBody(bodydef);
        body.setUserData(this);
        body.createFixture(fix);
    }

    // Dibuja el personaje
    public void dibujar(SpriteBatch batch) {
        // Dibuja el personaje dependiendo del estadoMovimiento
        sprite.setPosition((body.getPosition().x * PantallaNivel.getPtM()) - sprite.getWidth() / 2,
                (body.getPosition().y * PantallaNivel.getPtM()) - sprite.getHeight()/ 2);
        switch (estadoMovimiento) {
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                timerAnimacion += Gdx.graphics.getDeltaTime();
                // Frame que se dibujará
                TextureRegion region = spriteAnimadoMov.getKeyFrame(timerAnimacion);
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
        recuperar();

    }

    private void recuperar() {
        if(this.getHabilidad()==Habilidad.INVULNERABLE){
            if(tiempoInv==0){
                this.setHabilidad(Habilidad.NADA);
                tiempoInv=TIEMPO_INV_INICIAL;
            }
            else{
                tiempoInv--;
            }
        }
    }

    private void moverVertical(TiledMap mapa) {
        if(estadoSalto==EstadoSalto.SUBIENDO)
            body.applyForceToCenter(0f,400f,true);
        estadoSalto=EstadoSalto.BAJANDO;
    }



    // Mueve el personaje a la derecha/izquierda, prueba choques con paredes y puerta
    private void moverHorizontal(TiledMap mapa) {
        if(estadoMovimiento==EstadoMovimiento.MOV_DERECHA) {
            if(checarMovDer(mapa)) {
                if (this.getHabilidad() != Habilidad.INVULNERABLE) {
                    if (body.getLinearVelocity().x < 6f)
                        body.applyForceToCenter(20f, 0f, true);
                    else
                        body.setLinearVelocity(6f, body.getLinearVelocity().y);
                } else if (tiempoInv < TIEMPO_INV_INICIAL / 2) {
                    if (body.getLinearVelocity().x < 6f)
                        body.applyForceToCenter(20f, 0f, true);
                    else
                        body.setLinearVelocity(6f, body.getLinearVelocity().y);
                }
            }
        }

        else if(estadoMovimiento==EstadoMovimiento.MOV_IZQUIERDA) {
            if(checarMovIzq(mapa)){
                if(this.getHabilidad()!=Habilidad.INVULNERABLE) {
                    if (body.getLinearVelocity().x < -6f)
                        body.applyForceToCenter(-20f, 0f, true);
                    else
                        body.setLinearVelocity(-6f, body.getLinearVelocity().y);
                } else if(tiempoInv < TIEMPO_INV_INICIAL/2){
                    if (body.getLinearVelocity().x < -6f)
                        body.applyForceToCenter(-20f, 0f, true);
                    else
                        body.setLinearVelocity(-6f, body.getLinearVelocity().y);
                }
            }
        }
    }

    private boolean checarMovDer(TiledMap mapa){
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(1);
        if(capa.isVisible()) {
            int x = (int) ((sprite.getX() + 64) / 64);   // Convierte coordenadas del mundo en coordenadas del mapa
            int y = (int) (sprite.getY() / 64);
            TiledMapTileLayer.Cell celdaDerecha = capa.getCell(x, y);
            if (celdaDerecha != null) {
                Object tipo = (String) celdaDerecha.getTile().getProperties().get("tipo");
                if ("puerta".equals(tipo)) {
                    return false;  // Está chocando
                }
                else {return true;}
            }
        }
        return true;
    }

    private boolean checarMovIzq(TiledMap mapa){
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(1);
        if(capa.isVisible()) {
            int x = (int) ((sprite.getX() - 64) / 64);   // Convierte coordenadas del mundo en coordenadas del mapa
            int y = (int) (sprite.getY() / 64);
            TiledMapTileLayer.Cell celdaIzq = capa.getCell(x, y);
            if (celdaIzq != null) {
                Object tipo = (String) celdaIzq.getTile().getProperties().get("tipo");
                if ("puerta".equals(tipo)) {
                    return false;  // Está chocando
                }
                else {return true;}
            }
        }
        return true;
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

    //Mejorar con or de celda izquierda, centro o derecha
    public boolean recolectarMiniVi(TiledMap mapa) {
        TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(2);
        int x = (int)(((sprite.getX()+sprite.getWidth()-10)/64));
        int y = (int)(sprite.getY()/64);
        TiledMapTileLayer.Cell celda = capa.getCell(x,y);
        if (celda!=null) {
            Object tipo = celda.getTile().getProperties().get("tipo");
            if ("miniVi".equals(tipo) ) {
                capa.setCell(x,y,null);    // Borra la moneda del mapa
                return true;
            }
        }
        return false;
    }

    public boolean moverPalanca(TiledMap mapa) {
        // Revisar si toca una moneda (pies)
        TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(2);
        int x = (int)(((sprite.getX()+sprite.getWidth()-10)/64));
        int y = (int)(sprite.getY()/64);
        TiledMapTileLayer.Cell celda = capa.getCell(x,y);
        if (celda!=null ) {
            Object tipo = celda.getTile().getProperties().get("tipo");
            if ( "palanca".equals(tipo) ) {
                //capa.setCell(x,y,null);// Borra la moneda del mapa
                //capa.setCell(x,y,capa.getCell(0,4)); // Cuadro azul en lugar de la moneda
                capa.setCell(x,y,celda.setFlipHorizontally(true));
                mapa.getLayers().get(1).setVisible(!mapa.getLayers().get(1).isVisible());

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

    public void recibirDano(WorldManifold wm) {
        Vector2 poc = new Vector2(0,0);
        setVidas(getVidas()-1);
        poc = wm.getPoints()[0];
        Gdx.app.log("Daño","colision " + poc.toString());
        Gdx.app.log("Daño","bodyGetX " + this.body.getPosition().x);
        if(this.body.getPosition().x<poc.x){
            Gdx.app.log("Dano","Recibio derecho");
            this.body.setLinearVelocity(-this.body.getLinearVelocity().x-5f,3f);
        }
        else{
            Gdx.app.log("Dano","Recibio izquierdo");
            this.body.setLinearVelocity(this.body.getLinearVelocity().x+5f,3f);

        }

    }

    public void setVidas(int vida){
        vidas = vida;
    }

    public int getVidas(){
        return vidas;
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
