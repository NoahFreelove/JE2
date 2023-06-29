package org.JE.JE2.Utility;

import org.JE.JE2.IO.Logging.Errors.JE2Error;
import org.JE.JE2.IO.Logging.Logger;

/**
 * Creates new instance of class if input is null
 * @param <T> Class type
 */
public class ForceNonNull<T> {

    Class<T> clazz;
    public ForceNonNull(Class<T> clazz){
        this.clazz = clazz;
    }

    public T forceNonNull(Object o){
        // if null use reflect to create new instance with the default constructor
        if(o == null){
            try {
                return clazz.getConstructor().newInstance();
            } catch (Exception e) {
                Logger.log(new JE2Error("Force Non Null Error: You cannot use this method on a class that does not have a default constructor. Please create a default constructor for the class: " + clazz.getName()));
            }
        }
        return (T) o;
    }

    public static Object forceNonNull(Object o, Class<?> type){
        // if null use reflect to create new instance with the default constructor
        if(o == null){
            try {
                return type.getConstructor().newInstance();
            } catch (Exception e) {
                Logger.log(new JE2Error("Force Non Null Error: You cannot use this method on a class that does not have a default constructor. Please create a default constructor for the class: " + type.getName()));
            }
        }
        return o;
    }
}
