package lab7;

import org.zeromq.*;

import java.net.Socket;
import java.util.HashMap;

public class Proxy {
    private  static HashMap<ZFrame, GetStorageData> storageDataHashMap;

    public static void main(String[] args){
        storageDataHashMap = new HashMap<>();
        ZContext context = new ZContext();
        ZMQ.Socket frontend = context.createSocket(SocketType.ROUTER);
        ZMQ.Socket backend = context.createSocket(SocketType.ROUTER);
        frontend.setHWM(0);
        backend.setHWM(0);
        frontend.bind("tcp://*:5555");
        backend.bind("tcp://*:5556");
        ZMQ.Poller items = context.createPoller(2);
        items.register(frontend, ZMQ.Poller.POLLIN);
        items.register(backend, ZMQ.Poller.POLLIN);
        System.out.println("Proxy started!");
        while (!Thread.currentThread().isInterrupted()) {
            items.poll();
            if(items.pollin(0)) {
                ZMsg msg = ZMsg.recvMsg(frontend);
                if(msg == null) {
                    break;
                }
                String[] strings = msg.getLast().toString().split(" ");
                
            }
        }
    }
}
