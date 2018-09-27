package hx.kit.data;

import android.text.TextUtils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rose on 9/23/2016.
 */
public class Formater {

    private static final String EMAIL_FORMAT = "^([a-zA-Z0-9_\\.\\-])+(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})$";
    private static final String MOBILE_FORMAT = "^(13[0-9]|15[^4]|17[0,6,7,8]|18[0-9]|14[5,7])\\d{8}$";
    public static final String PHONE_FORMAT = "0\\d{2,3}-?\\d{5,9}|0\\d{2,3}-?\\d{5,9}|\\d{8}";
    public static final String QQ_FORMAT = "\\d{5,11}";
    public static final String PHONE_MASK_FORMAT = "(\\d{3})\\d{4}(\\d{4})";


    private static final String ID_FORMAT = "^[1-9][0-9]{5}(19[0-9]{2}|200[0-9]|2010)(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9xX]$";

    private static final String IP_FORMAT = "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
            "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
            "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
            "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])";
    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t");


    public static boolean isMobile(String mobile){
        return Pattern.compile(MOBILE_FORMAT).matcher(mobile).matches();
    }

    public static String phoneMask(String phone){
        return TextUtils.isEmpty(phone) ? phone : phone.replaceAll(PHONE_MASK_FORMAT, "$1****$2");
    }

    public static boolean isIdNo(String idNo){
        return Pattern.compile(ID_FORMAT).matcher(idNo).matches();
    }

    public static boolean isEmail(String email){
        return Pattern.compile(EMAIL_FORMAT).matcher(email).matches();
    }

    public static boolean hasCharacter(String str){
        for(Character c : str.toCharArray()){
            if(Character.isLetterOrDigit(c)) return true;
        }
        return false;
    }
    public static boolean containNullCharacter(String str){
        for(Character c: str.toCharArray()){
            if(Character.isSpaceChar(c) || Character.isWhitespace(c)) continue;
            return false;
        }
        return true;
        //return !hasCharacter(str);
    }

    public static String getIpAddress(int ipVal){
        return String.format(Locale.CHINA, "%d.%d.%d.%d", (ipVal & 0xff), (ipVal >> 8 & 0xff), (ipVal >> 16 & 0xff), (ipVal >> 24 & 0xff));
    }
    public static boolean isIpAddr(String ip){
        return !TextUtils.isEmpty(ip) && Pattern.compile(IP_FORMAT).matcher(ip).matches();
    }

    public static boolean containSpecialChar(String str){
        Matcher m = SPECIAL_CHARS_PATTERN.matcher(str);
        return m.find();
    }

}
