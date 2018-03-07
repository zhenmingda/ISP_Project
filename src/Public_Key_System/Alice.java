package Public_Key_System;



import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Scanner;

/**
 * Created by dashu on 2016-11-16.
 */
public class Alice {
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
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("\\ISP_Project\\KeyFileForPKSAlice.txt"));
        out.writeUTF(publicKey);
        out.close();

        String host = "127.0.0.1";
        int port = 7999;
//both

        Socket s = new Socket(host, port);
        System.out.println("Enter Message");
        BufferedReader br = new BufferedReader(new
                InputStreamReader(System.in));
        String message = br.readLine();
        ObjectOutputStream outputStream = new ObjectOutputStream(s.getOutputStream());
        byte[] encryptedMessage1 = RSAUtils.encryptByPrivateKey(message.getBytes(), privateKey);

        FileInputStream fin = new FileInputStream("\\ISP_Project\\KeyFileForPKSBob.txt");
        ObjectInputStream ois = new ObjectInputStream(fin);
        String BobPublicKey = ois.readUTF();
        byte[] encryptedMessage2 = RSAUtils.encryptByPublicKey(encryptedMessage1, BobPublicKey);
        outputStream.writeInt(encryptedMessage2.length);
        outputStream.write(encryptedMessage2);
        outputStream.flush();
        //confidentiality
        byte[] encryptedMessageForConfidentiality = RSAUtils.encryptByPublicKey(message.getBytes(), BobPublicKey);
        outputStream.writeInt(encryptedMessageForConfidentiality.length);
        outputStream.write(encryptedMessageForConfidentiality);
        outputStream.flush();

        //integrity
        byte[] encryptedMessageForIntegrity = RSAUtils.encryptByPrivateKey(message.getBytes(), privateKey);
        outputStream.writeInt(encryptedMessageForIntegrity.length);
        outputStream.write(encryptedMessageForIntegrity);
        outputStream.flush();

        outputStream.close();


    }


}
