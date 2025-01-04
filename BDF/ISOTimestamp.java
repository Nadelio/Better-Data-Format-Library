package BDF;

public class ISOTimestamp {
    private int hour;
    private int minute;
    private int second;

    public ISOTimestamp(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getHour() { return this.hour; }
    public int getMinute() { return this.minute; }
    public int getSecond() { return this.second; }
    public int[] getFullTimestamp(){ return new int[]{this.hour, this.minute, this.second}; }

    public String toString(){
        return this.hour + ":" + this.minute + ":" + this.second;
    }
}
