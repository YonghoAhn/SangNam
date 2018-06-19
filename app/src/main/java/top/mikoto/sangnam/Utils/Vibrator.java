package top.mikoto.sangnam.Utils;

import android.content.Context;

public class Vibrator {
    private static Vibrator instance;
    private final android.os.Vibrator vibrator;

    private Vibrator(Context context) {
        Context context1 = context;
        vibrator = (android.os.Vibrator) context1.getSystemService(Context.VIBRATOR_SERVICE);
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
