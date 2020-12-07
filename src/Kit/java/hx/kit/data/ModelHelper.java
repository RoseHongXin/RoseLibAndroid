package hx.kit.data;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RoseHongXin on 2018/4/21 0021.
 */

public class ModelHelper {

    public static <D> D copy(Object src, D dst){
        return copy(src, dst, false);
    }
    public static <D> D copy(Object src, D dst, boolean sensitive){
        if(src == null || dst == null) return dst;
        Field[] srcFields = src.getClass().getDeclaredFields();
        Field[] dstFields = dst.getClass().getDeclaredFields();
        for(Field sf : srcFields){
            String sName = sensitive ? sf.getName() : sf.getName().toLowerCase();
            Class sType= sf.getType();
            for(Field df : dstFields){
                df.setAccessible(true);
                String dName = sensitive ? df.getName() : df.getName().toLowerCase();
                Class dType= df.getType();
                if(TextUtils.equals(dName, sName)){
                    sf.setAccessible(true);
                    try {
                        Object value = sf.get(src);
                        if(dType == sType) {
                            df.set(dst, value);
                        }else if(dType == String.class && value != null){
                            if(sType == JSONObject.class) df.set(dst, ((JSONObject)value).toJSONString());
                            else df.set(dst, JSON.toJSONString(value));
                        }else if(sType == String.class && sType != dType && value != null){
                            if(dType == JSONObject.class) df.set(dst, JSON.parseObject((String)value));
                            else df.set(dst, JSON.parseObject((String) value, dType));
                        }
                        break;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        return dst;
    }

    public static Object append(@NonNull Object dst, @NonNull String fieldName, Object value){
        return append(dst, fieldName, value, false);
    }
    public static Object append(@NonNull Object dst, @NonNull String fieldName, Object value, boolean sensitive){
        Field[] dstFields = dst.getClass().getDeclaredFields();
        fieldName = sensitive ? fieldName : fieldName.toLowerCase();
        for(Field df : dstFields) {
            String dName = sensitive ? df.getName() : df.getName().toLowerCase();
            if (TextUtils.equals(dName, fieldName)) {
                df.setAccessible(true);
                try {
                    df.set(dst, value);
                    return dst;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return dst;
    }

    public static <D> D realmCopy(Object src, D dst){
        return realmCopy(src, dst, false);
    }
    public static <D> D realmCopy(Object src, D dst, boolean sensitive){
        if(src == null) return null;
        Method[] _srcFields = src.getClass().getDeclaredMethods();
        List<Method> srcMethods = new ArrayList<>();
        for(Method m : _srcFields){
            if(m.getName().contains("Get")) srcMethods.add(m);
        }
        Field[] dstFields = dst.getClass().getDeclaredFields();
        try {
            for (Field df : dstFields) {
                String dName = sensitive ? df.getName() : df.getName().toLowerCase();
                Class dType = df.getType();
                df.setAccessible(true);
                for(Method sm : srcMethods){
                    String sName = sensitive ? sm.getName() : sm.getName().toLowerCase();
                    sName = sName.substring(sName.lastIndexOf('$') + 1);
                    Class sType = sm.getReturnType();
                    if(TextUtils.equals(sName, dName)){
                        sm.setAccessible(true);
                        Object value = sm.invoke(src);
                        if(dType == sType) {
                            df.set(dst, value);
                        }else if(sType == String.class){
//                            if(dType instanceof Collection){
//
//                            }
//                            ParameterizedType pt = (ParameterizedType) df.getGenericType();
//                            if(pt != null){
//                                Type t = pt.getActualTypeArguments()[0];
//                            }
                            df.set(dst, JACKSON.parseObject((String)value, dType));
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dst;
    }


}
