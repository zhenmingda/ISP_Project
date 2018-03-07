package Public_Key_System;

/**
 * Created by zhenmingda on 2016/11/29.
 */

import org.bouncycastle.util.encoders.Base64;

public class Base64Utils {

    //decode from base64 to bytes
    public static byte[] decode(String base64) throws Exception {
        return Base64.decode(base64.getBytes());
    }

    //encode from  bytes to base64
    public static String encode(byte[] bytes) throws Exception {
        return new String(Base64.encode(bytes));
    }


}