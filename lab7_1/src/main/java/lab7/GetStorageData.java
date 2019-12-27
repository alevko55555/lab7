package lab7;

public class GetStorageData {
    private int left;
    private int right;
    private long time;

    public GetStorageData(int left, int right, long time){
        this.left = left;
        this.right = right;
        this.time = time;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
