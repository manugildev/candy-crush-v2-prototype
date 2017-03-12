package helpers;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import gameobjects.GameObject;
import gameobjects.Square;

/**
 * Created by RobII on 10/12/2015.
 */
public class GlobalPools {

    //preload holders
    private static Array<Object> preloadHolder = new Array<Object>();

    //square pool
    public static final Pool<Square> squarePool = new Pool<Square>() {
        @Override
        protected Square newObject() {
            return new Square();
        }
    };

    //circle shape pool
    public static final Pool<Circle> circlePool = new Pool<Circle>() {
        @Override
        protected Circle newObject() {
            return new Circle();
        }
    };

    //rectangle shape pool
    public static final Pool<Rectangle> rectanglePool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };

    public static void preloadPools(float total){

        //squares
        for(int i = 0; i < total; i++){
            preloadHolder.add(squarePool.obtain());
        }

        for(int i = 0; i < total; i++){
            squarePool.free((Square)preloadHolder.get(i));
        }
        preloadHolder.clear();

        //circle
        for(int i = 0; i < total; i++){
            preloadHolder.add(circlePool.obtain());
        }

        for(int i = 0; i < total; i++){
            circlePool.free((Circle)preloadHolder.get(i));
        }
        preloadHolder.clear();

        //rectangle
        for(int i = 0; i < total; i++){
            preloadHolder.add(rectanglePool.obtain());
        }

        for(int i = 0; i < total; i++){
            rectanglePool.free((Rectangle)preloadHolder.get(i));
        }
        preloadHolder.clear();
    }
}
