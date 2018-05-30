package top.mikoto.sangnam.Models;

public class AlarmModel {
    private int _id;
    private String time;
    private String days;
    private int run;

    public AlarmModel(int _id, String time, String days, int run) {
        this._id = _id;
        this.time = time;
        this.days = days;
        this.run = run;
    }

    public AlarmModel() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }
}
