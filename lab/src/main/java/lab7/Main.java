package lab7;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args){
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket subscriber = context.socket(SocketType.SUB);
        subscriber.connect("tcp://locallhost:5556");
        String filter = (args.length > 0) ? args[0] : "10001";
        subscriber.subscribe(filter.getBytes());
        int update_nbr;
        long total_temp = 0;
        for (update_nbr = 0; update_nbr < 100; update_nbr++) {
            String string = subscriber.recvStr(0).trim();
            StringTokenizer sscanf = new StringTokenizer(string, "");
            int zipcode = Integer.valueOf(sscanf.nextToken());
            int temperature = Integer.valueOf(sscanf.nextToken());
            total_temp += temperature;
        }
        System.out.println("Average temrature for zipcode" + filter + "was" +(int)(total_temp/update_nbr));
        subscriber.close();
        context.term();
    }
}
