package lab7;

//import org.zeromq.*;

//import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.zeromq.*;
import org.zeromq.ZMQ.Socket;

public class Proxy {
    private  static HashMap<ZFrame, GetStorageData> storageDataHashMap;

    public static void main(String[] args){
        storageDataHashMap = new HashMap<>();
        ZContext context = new ZContext();
        Socket frontend = context.createSocket(SocketType.ROUTER);
        Socket backend = context.createSocket(SocketType.ROUTER);
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
                if(msg == null)
                    break;

                String[] strings = msg.getLast().toString().split(" ");
                boolean found = false;
                for(Map.Entry<ZFrame, GetStorageData> entry : storageDataHashMap.entrySet()) {
                    if(entry.getValue().getLeft() <= Integer.parseInt(strings[1]) && entry.getValue().getRight() > Integer.parseInt(strings[1])) {
                        if(!found) {
                            System.out.println(entry.getValue().getLeft());
                            System.out.println(entry.getValue().getRight());
                            System.out
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
                if(msg == null)
                    break;

                ZFrame address = msg.unwrap();
                if(msg.getFirst().toString().equals("NOTIFY")){
                    msg.pop();
                    int left = Integer.parseInt(msg.popString());
                    int right = Integer.parseInt(msg.popString());
                    storageDataHashMap.put(address, new GetStorageData(left, right, System.currentTimeMillis()));
                    System.out.println(address.toString() + "--" + left + "--" + right);
                } else {
                    System.out.println(msg.getLast().toString());
                    msg.send(frontend);
                }
            }
        }
    }
}
