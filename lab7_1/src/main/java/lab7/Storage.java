package lab7;

import org.zeromq.ZContext;

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
        ZContext
    }
}
