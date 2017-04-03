package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Created by Julio on 31/03/2017.
 */

public abstract class PantallaNivel extends Pantalla {

    private static final float PIXELS_TO_METERS = 64f;

    public PantallaNivel(){
        super();
    }

    public void leerMapa(TiledMap map){
        /*get cell(x,y)
        * if(is tipo.equals"piso" && x1!=0)
        *   puntox1=cell.pos.x
        * if(is tipo.equals"piso")
        *   count++;
        * else if(x1!=0){
        *   x2= x1 + count*64
        *   crear static body(x1,y1,x2,y1)*/
    }
    public static float getPtM(){
        return PIXELS_TO_METERS;
    }
}
