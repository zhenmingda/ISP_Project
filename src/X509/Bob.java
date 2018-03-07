package X509;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PrivateKey;

/**
 * Created by zhenmingda on 2016/11/29.
 */
public class Bob {
    public static void main(String[] args) throws Exception {
        ObjectOutputStream objectOutputStream = null;
        int port = 7999;
        ServerSocket server = new ServerSocket(port);
        Socket s = server.accept();
        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
        String request = in.readUTF();
        if (request.equals("Give me the certificate")) {

            String crt = "\\ISP_Project\\dashukey.crt";
            objectOutputStream = new ObjectOutputStream(s.getOutputStream());
            objectOutputStream.writeUTF(crt);
            objectOutputStream.flush();
        }

        int len = in.readInt();
        byte[] encryptedDataFromClient = new byte[len];
        in.readFully(encryptedDataFromClient);
        FileInputStream fileInputStreamForKeyStore = new FileInputStream("\\ISP_Project\\Mingda_Zhen.keystore");
        KeyStore keyStore = KeyStore.getInstance("JKS");         // load key store
        char[] keyStorePass = "dashu12138".toCharArray();          // keystore pass
        char[] keyPass = "19931126".toCharArray();          // keypass
        keyStore.load(fileInputStreamForKeyStore, keyStorePass);              // load certificate
        PrivateKey privateKey = (PrivateKey) keyStore.getKey("dashukey", keyPass);     // get private key of certificate

        fileInputStreamForKeyStore.close();
        Cipher decipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        decipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] orginalData = decipher.doFinal(encryptedDataFromClient);            // decrypt message

        //print orginal data
        System.out.println("The Result is " + new String(orginalData));
        fileInputStreamForKeyStore.close();
        in.close();
        objectOutputStream.close();
        s.close();
        server.close();
    }
}
