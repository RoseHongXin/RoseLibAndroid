package hx.toolkit;

import java.util.HashMap;

/**
 * Created by Rose on 3/22/2017.
 */

public class ObjConverter {

    public static <T> HashMap<String, Object> bean2Map(T bean){
//        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        return new HashMap<>();
    }

    public static <T> T map2Bean(HashMap<String, Object> map, T bean){
        return bean;
    }

}
