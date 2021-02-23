package hx.kit.data;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by RoseHongXin on 2018/4/8 0008.
 */

public class JSONHelper {
    
    private ObjectMapper mObjMapper;
    
    private static JSONHelper mHelper; 
    private JSONHelper(){
        mObjMapper = new ObjectMapper();
        mObjMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        mObjMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mObjMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mObjMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mObjMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mObjMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mObjMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
//        mObjMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        mObjMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
//        mObjMapper.configure(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL, true);
//        mObjMapper.configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, false);
//        SerializationConfig serializationConfig = mObjMapper.getSerializationConfig();
//        DeserializationConfig deserializationConfig = mObjMapper.getDeserializationConfig();

    }
    public static ObjectMapper mapper(){
        if(mHelper == null) mHelper = new JSONHelper();
        return mHelper.mObjMapper;
    }
    
    public static <D> D parse(String original, Class<D> clz){
        if(TextUtils.isEmpty(original)) return null;
        try {
            return mapper().readValue(original, clz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Deprecated
    public static <D> D parse(Object obj, Class<D> clz){
        obj = obj == null ? "" : obj;
        D d = null;
        try {
            d = mapper().readValue(obj instanceof String ? (String) obj : toJSONStr(obj), clz);
        } catch (Exception e) {
            e.printStackTrace();
            d = JSON.parseObject(obj instanceof String ? (String) obj : toJSONStr(obj), clz);
        }
        return d;
    }

    public static <D> List<D> parseArray(Object obj, Class<D> clz){
        return parseArray(obj, clz, new String[]{});
    }
    private static <D> List<D> parseArray(Object obj, Class<D> clz, String ... ignoreFields){
        obj = obj == null ? "[]" : obj;
        try {
            ObjectMapper mapper = mapper();
            CollectionType type = mapper.getTypeFactory().constructCollectionType(ArrayList.class, clz);
            String content = obj instanceof String ? (String) obj : ignoreFields.length == 0 ? toJSONStr(obj) : toJSONStr(obj, ignoreFields);
            return mapper.readValue(content, type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static String toJSONStr(Object obj, String ... ignoreFields){
        List<String> ignoreFieldSet = Arrays.asList(ignoreFields);
        ObjectMapper mapper = mapper();
        if(obj instanceof List){
            StringBuilder builder = new StringBuilder();
            for(Object o : (List)obj){
                ObjectNode node = mapper.valueToTree(o);
                node.without(ignoreFieldSet);
                builder.append(node.toString())
                        .append(",");
            }
            String content = builder.toString();
            if(content.length() > 0) content = content.substring(0, content.length() - 1);
            return "[" + content + "]";
        }else {
//                PropertyFilter propertyFilter = SimpleBeanPropertyFilter.serializeAllExcept(ignoreFields);
//                SimpleFilterProvider filterProvider = new SimpleFilterProvider();
//                filterProvider.addFilter("cityFilter", propertyFilter);
//                mapper.setFilterProvider(filterProvider);
//                ObjectWriter writer = mapper.writer(filterProvider);
            ObjectNode node = mapper.valueToTree(obj);
            node.without(ignoreFieldSet);
            return node.toString();
        }
    }
    public static String toJSONStr(Object obj){
        try {
            return mapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
    
}
