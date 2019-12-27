package lab7;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class Proxy {
    public void exampleProxy() {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket fronted = context.socket(SocketType.XSUB);
        fronted.bind("tcp://*:5559");
        ZMQ.Socket backend = context.socket(SocketType.XPUB);
        backend.bind("tcp://*:5560");
        ZMQ.proxy(fronted, backend, null);
        fronted.close();
        backend.close();
        context.term();
    }
}
