package hx.kit.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class JACKSON {

    private final ObjectMapper mObjMapper;

    private static JACKSON mHelper;
    private JACKSON(){
        mObjMapper = new ObjectMapper();
        mObjMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        mObjMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mObjMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mObjMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mObjMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    }
    public static ObjectMapper mapper(){
        if(mHelper == null) mHelper = new JACKSON();
        return mHelper.mObjMapper;
    }

    public static <D> D parseObject(Object obj, TypeReference<D> typeRef){
        if(obj == null) { return null; }
        D d = null;
        try {
            d = mapper().readValue(obj instanceof String ? (String) obj : toString(obj), typeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }
    public static <D> D parseObject(Object obj, Class<D> clz){
        if(obj == null) { return null; }
        D d = null;
        try {
            d = mapper().readValue(obj instanceof String ? (String) obj : toString(obj), clz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public static <D> List<D> parseArray(Object obj, Class<D> clz, String ... ignoreFields){
        if(obj == null) { return new ArrayList<>(); }
        try {
            ObjectMapper mapper = mapper();
            CollectionType type = mapper.getTypeFactory().constructCollectionType(ArrayList.class, clz);
            String content = obj instanceof String ? (String) obj : toString(obj, ignoreFields);
            return mapper.readValue(content, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static String toString(Object obj, String ... ignoreFields) {
        ObjectMapper mapper = mapper();
        try {
            if(obj instanceof Collection){
                String jarrayString = mapper.writeValueAsString(obj);
                List<HashMap<String,Object>> jarray = mapper.readValue(jarrayString, new TypeReference<List<HashMap<String, Object>>>() {});
                for(HashMap<String, Object> it : jarray){
                    for(String fd : ignoreFields){ it.remove(fd); }
                }
                return mapper.writeValueAsString(jarray);
            }else{
                String jsonString = mapper.writeValueAsString(obj);
                HashMap<String,Object> json = mapper.readValue(jsonString, new TypeReference<HashMap<String, Object>>() {});
                for(String fd : ignoreFields){ json.remove(fd); }
                return mapper.writeValueAsString(json);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
