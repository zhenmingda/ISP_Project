package X509;

import javax.crypto.Cipher;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

/**
 * Created by zhenmingda on 2016/11/29.
 */
public class Alice {
    public static void main(String[] args) throws Exception {
        String request = "Give me the certificate";
        String host = "127.0.0.1";
        int port = 7999;
        Socket s = new Socket(host, port);
        ObjectOutputStream outputStream = new ObjectOutputStream(s.getOutputStream());
        outputStream.writeUTF(request);
        outputStream.flush();
        ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
        String addrCertificate = objectInputStream.readUTF();//read certificate address from server
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        FileInputStream fileInputStreamForCrt = new FileInputStream(addrCertificate); // certificate
        X509Certificate cf = (X509Certificate) certificateFactory.generateCertificate(fileInputStreamForCrt);//cast certificate class to X509
        System.out.println(cf.toString());//output information of certificate
        PublicKey publicKey = cf.getPublicKey();//get public key

        try {
            cf.checkValidity(); //check whether the date is valid
            cf.verify(publicKey); //check public key
        } catch (CertificateExpiredException | InvalidKeyException | CertificateNotYetValidException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        System.out.println("Enter something:");
        BufferedReader br = new BufferedReader(new
                InputStreamReader(System.in));
        String input = br.readLine();
        byte[] message = input.getBytes();

        // use public key to encrypt
        Cipher encipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");      // RSA Algorithm
        encipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedData = encipher.doFinal(message);            // encrypted data
        outputStream.writeInt(encryptedData.length);
        outputStream.write(encryptedData);
        outputStream.flush();
        outputStream.close();
        objectInputStream.close();
        fileInputStreamForCrt.close();
        s.close();

    }
}
