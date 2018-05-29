package top.mikoto.sangnam.Models;

public class AlarmListViewItem {
    private String Time;
    private String DayOfTheWeek;
    private boolean Run;

    public boolean isRun() {
        return Run;
    }

    public void setRun(boolean run) {
        Run = run;
    }

    public String getDayOfTheWeek() {
        return DayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        DayOfTheWeek = dayOfTheWeek;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
