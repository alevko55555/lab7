package lab7;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;

import java.util.HashMap;

public class Proxy {
    private  static HashMap<ZFrame, GetStorageData> storageDataHashMap;

    public static void main(String[] args){
        storageDataHashMap = new HashMap<>();
        ZContext context = new ZContext();
        ZMQ.Socket frontend = context.createSocket(SocketType.ROUTER)
    }
}
