package io.bushe.fastbuilder.Core.Golang;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import io.bushe.fastbuilder.UI.Activity.MainActivity;
import io_bushe_fastbuilder_family_core_go.Io_bushe_fastbuilder_family_core_go;

import static io.bushe.fastbuilder.FastBuilder.TAG;

public class GoBuilderService extends Service {
    public GoBuilderService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("BuSheNotificationChannel", "BuShe Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(notificationChannel);
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            Notification notification = new NotificationCompat.Builder(this, "BuSheNotificationChannel")
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: Io_bushe_fastbuilder_family_core_go");
                Io_bushe_fastbuilder_family_core_go.newGoBuilder();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
