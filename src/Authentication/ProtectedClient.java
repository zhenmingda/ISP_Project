package Authentication;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class ProtectedClient {
    public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException {
        Date date = new Date();
        long timeStamp1 = date.getTime();

        double randomNumer1 = Math.random() * 10.0 + 1.0;
        byte[] mush = Protection.makeDigest(user, password, timeStamp1, randomNumer1);
        Date date2 = new Date();
        long timeStamp2 = date2.getTime();
        double randomNumer2 = Math.random() * 10.0 + 1.0;

        byte[] double_password1 = Protection.makeDigest(mush, timeStamp2, randomNumer2);
        DataOutputStream out = new DataOutputStream(outStream);
        out.writeUTF(user);
        out.writeLong(timeStamp1);
        out.writeDouble(randomNumer1);
        out.writeLong(timeStamp2);
        out.writeDouble(randomNumer2);
        out.writeInt(double_password1.length);
        out.write(double_password1);
        out.flush();
        out.close();
    }

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 7999;
        String user = "dashu";
        String password = "19931126";
        Socket s = new Socket(host, port);
        ProtectedClient client = new ProtectedClient();
        client.sendAuthentication(user, password, s.getOutputStream());
        s.close();
    }


}