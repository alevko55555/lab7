package lab7;

import org.zeromq.*;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
                boolean found = false;
                for(Map.Entry<ZFrame, GetStorageData> entry : storageDataHashMap.entrySet()) {
                    if(entry.getValue().getLeft() <= Integer.parseInt(strings[1]) && entry.getValue().getRight() > Integer.parseInt(strings[1])) {
                        if(!found) {
                            if(System.currentTimeMillis() - entry.getValue().getTime() > 10000) {
                                storageDataHashMap.remove(entry.getKey());
                                continue;
                            }
                            msg.wrap(entry.getKey().duplicate());
                        }
                        found = true;
                        msg.send(backend);
                        if(strings[0].equals("GET")) {
                            break;
                        }
                    }
                }
                if(!found) {
                    msg.pollLast();
                    msg.addLast("No value with such index");
                    msg.send(frontend);
                }
            }
            if(items.pollin(1)) {
                ZMsg msg = ZMsg.recvMsg(backend);
                if(msg == null) {
                    break;
                }
                ZFrame address = msg.unwrap();
                if(msg){

                }
            }
        }
    }
}
