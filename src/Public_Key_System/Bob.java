package Public_Key_System;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by dashu on 2016-11-16.
 */
public class Bob {
    private static String publicKey;
    private static String privateKey;

    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey pk = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey prk = (RSAPrivateKey) keyPair.getPrivate();
        publicKey = Base64Utils.encode(pk.getEncoded());
        privateKey = Base64Utils.encode(prk.getEncoded());
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("\\ISP_Project\\KeyFileForPKSBob.txt"));
        out.writeUTF(publicKey);
        out.close();
        int port = 7999;
        ServerSocket server = new ServerSocket(port);
        Socket s = server.accept();

        FileInputStream fin = new FileInputStream("\\ISP_Project\\KeyFileForPKSAlice.txt");
        ObjectInputStream ois = new ObjectInputStream(fin);
        String AlicePublicKey = ois.readUTF();
        ois.close();
        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
        int len = in.readInt();
        byte[] result = new byte[len];
        in.readFully(result);
        byte[] decryptedMessage1 = RSAUtils.decryptByPrivateKey(result, privateKey);
        byte[] decryptedMessage2 = RSAUtils.decryptByPublicKey(decryptedMessage1, AlicePublicKey);

        System.out.println(new String(decryptedMessage2));
        //confidentiality
        int len1 = in.readInt();
        byte[] result1 = new byte[len1];
        in.readFully(result1);
        byte[] decryptedMessage3 = RSAUtils.decryptByPrivateKey(result1, privateKey);
        System.out.println(new String(decryptedMessage3));
        //integrity
        int len2 = in.readInt();
        byte[] result2 = new byte[len2];
        in.readFully(result2);
        byte[] decryptedMessage4 = RSAUtils.decryptByPublicKey(result2, AlicePublicKey);
        System.out.println(new String(decryptedMessage4));
        in.close();

    }
}