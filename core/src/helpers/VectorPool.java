package helpers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import gameobjects.Square;

/**
 * Created by RobII on 15/12/2015.
 */
public class VectorPool implements Poolable {

    //Active vectors
    public static Array<Vector2> activeVectors = new Array<Vector2>();

    //square pool
    public static final Pool<Vector2> vectorPool = new Pool<Vector2>() {
        @Override
        protected Vector2 newObject() {
            return new Vector2();
        }
    };

    public static void init(Vector2 v, Vector2 val){
        v.set(val);
        activeVectors.add(v);
    }

    public static void init(Vector2 v, float x, float y){
        v.set(x, y);
        activeVectors.add(v);
    }

    public static void releaseVectors(){
        vectorPool.freeAll(activeVectors);
        activeVectors.clear();
    }

    @Override
    public void reset() {
    }
}
