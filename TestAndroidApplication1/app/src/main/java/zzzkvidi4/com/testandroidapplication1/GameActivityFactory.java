package zzzkvidi4.com.testandroidapplication1;

import android.app.Activity;
import android.util.Pair;
import android.util.SparseArray;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Roman on 05.11.2017.
 * In package ${PACKAGE_NAME}.
 */

class GameActivityFactory {
    private SparseArray<Constructor<? extends Activity>> constructors;
    private static GameActivityFactory _factory = null;

    static  GameActivityFactory getInstance(){
        if (_factory != null){
            return _factory;
        }
        _factory = new GameActivityFactory();
        return _factory;
    }

    private GameActivityFactory(){
        constructors = new SparseArray<>();
    }

    void registerConstructor(int key, Class<? extends Activity> clazz) throws NoSuchMethodException {
        Class[] paramTypes = new Class[] {};
        Constructor<? extends Activity> constructor = clazz.getConstructor(paramTypes);
        constructors.append(key, constructor);
    }

    Activity createNewActivity(int key) throws InstantiationException, IllegalAccessException, InvocationTargetException{
        return constructors.get(key).newInstance();
    }
}
