package lab7;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Storage {
    private static String str;
    private static int left;
    private static int right;

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String temp = in.nextLine();
        left = Integer.parseInt(in.nextLine());
        right = Integer.parseInt(in.nextLine());
        str = temp.substring(left, right);
        ZContext context = new ZContext();
        ZMQ.Socket storage = context.createSocket(SocketType.DEALER);
        storage.setHWM(0);
        storage.connect()
    }
}
