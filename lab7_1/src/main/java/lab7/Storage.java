package lab7;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

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
        storage.connect("tcp://localhost:5556");
        ZMQ.Poller poller = context.createPoller(1);
        poller.register(storage, ZMQ.Poller.POLLIN);
        long start = System.currentTimeMillis();
        System.out.println("Storage started!");
        while (!Thread.currentThread().isInterrupted()) {
            poller.poll(1);
            if(System.currentTimeMillis() - start > 5000) {
                ZMsg msg = new ZMsg();
                msg.addLast("");
                msg.addLast("NOTIFY");
                msg.addLast(Integer.toString(left));
                msg.addLast(Integer.toString(right));
                msg.addString(left + "-" + right);
                msg.send(storage);
                start = System.currentTimeMillis();
            }
            if (poller.pollin(0)) {
                ZMsg zMsg = ZMsg.recvMsg(storage);
                zMsg.unwrap();
                String[] strings = zMsg.pollLast().toString().split(" ");
                if (strings[0].equals("GET")) {
                    zMsg.addLast("VALUE=" + str.charAt(Integer.parseInt(strings[1]) - left));
                } else {
                    if(strings[0].equals("PUT")) {
                        str = replaceChar(str, strings[2], Integer.parseInt(strings[1]) - left);
                    }
                }
                zMsg.send(storage);
            }
        }
    }

    public static String replaceChar(String string, String ch, int i) {
        return string.substring(0, i) + ch + str.substring(i + 1);
    }
}
