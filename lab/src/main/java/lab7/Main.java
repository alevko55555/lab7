package lab7;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket subscriber = context.socket(SocketType.SUB);
        subscriber.connect()
    }
}
