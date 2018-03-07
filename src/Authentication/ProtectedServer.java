package Authentication;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProtectedServer {


    public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException {

        DataInputStream in = new DataInputStream(inStream);
        String user = in.readUTF();
        String password = lookupPassword(user);
        long timeStamp1 = in.readLong();
        double randomNumer1 = in.readDouble();
        long timeStamp2 = in.readLong();
        double randomNumer2 = in.readDouble();
        int length = in.readInt();
        byte[] result = new byte[length];
        in.readFully(result);
        in.close();
        byte[] mush = Protection.makeDigest(user, password, timeStamp1, randomNumer1);
        byte[] double_password1 = Protection.makeDigest(mush, timeStamp2, randomNumer2);
        if (MessageDigest.isEqual(result, double_password1)) {
            return true;
        } else
            return false;
    }
    protected String lookupPassword(String user) {
        return "19931126";
    }

    public static void main(String[] args) throws Exception {
        int port = 7999;
        ServerSocket s = new ServerSocket(port);
        Socket client = s.accept();

        ProtectedServer server = new ProtectedServer();

        if (server.authenticate(client.getInputStream()))
            System.out.println("Client logged in.");
        else
            System.out.println("Client failed to log in.");

        s.close();
        client.close();
    }
}
