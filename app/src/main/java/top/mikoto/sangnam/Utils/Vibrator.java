package top.mikoto.sangnam.Utils;

import android.content.Context;

public class Vibrator {
    private static Vibrator instance;
    private Context context;
    private android.os.Vibrator vibrator;

    public Vibrator(Context context) {
        this.context = context;
        vibrator = (android.os.Vibrator)this.context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static Vibrator getInstance(Context context) {
        if(instance!=null)
        {
            return instance;
        }
        else
        {
            return instance = new Vibrator(context.getApplicationContext());
        }
    }

    public void vibrate(int millisecond)
    {
        vibrator.vibrate(millisecond);
    }
}
