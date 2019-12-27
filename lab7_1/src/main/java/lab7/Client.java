package lab7;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.Scanner;
import java.util.StringTokenizer;

public class Client {
    public static void main(String[] args){
        //ZMQ.Context context = ZMQ.context(1);
        try(ZContext context = new ZContext()) {
            ZMQ.Socket client = context.createSocket(SocketType.REQ);
            client.setHWM(0);
            client.connect("tcp://localhost:5555");

            System.out.println("Client started");

            Scanner in = new Scanner(System.in);

            while (true) {
                String msg = in.nextLine();
                ZMsg req = new ZMsg();
                req.addString(msg);
                req.send(client);
                ZMsg ans = ZMsg.recvMsg(client);
                String str = ans.popString();
                System.out.println(str);
                ans.destroy();
            }
        }
    }
}
