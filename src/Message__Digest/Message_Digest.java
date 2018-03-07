package Message__Digest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dashu on 2016-11-14.
 */
public class Message_Digest {
    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new
                InputStreamReader(System.in));
        while (true) {
            System.out.println("Input Something");
            String i = br.readLine();

            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            md.update(i.getBytes());

            byte[] result = md.digest();
            String answer = byte2hex(result);
            System.out.println("MD5:" + answer);

            MessageDigest md1 = null;
            try {
                md1 = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            md1.update(i.getBytes());

            byte[] result1 = md1.digest();
            String answer1 = byte2hex(result1);
            System.out.println("SHA:" + answer1);
        }

    }


    public static String byte2hex(byte[] buffer) {
        String hexResult = "";

        for (int i = 0; i < buffer.length; i++) {
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            hexResult = hexResult + temp;
        }

        return hexResult;

    }
}