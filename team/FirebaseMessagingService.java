package edu.ucsd.cse110.team19.walkwalkrevolution.team;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import edu.ucsd.cse110.team19.walkwalkrevolution.MainActivity;
import edu.ucsd.cse110.team19.walkwalkrevolution.R;
import edu.ucsd.cse110.team19.walkwalkrevolution.RouteViewActivity;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void showNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "edu.ucsd.cse110.team19.walkwalkrevolution.test";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("WWR Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        Intent resultIntent = new Intent(this, TeamViewActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent broadcastIntent = new Intent( this, NotificationReceiver.class );
        broadcastIntent.putExtra("response", getEmailFromBody(body));
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,0,broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);


        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification)
                .setContentText(title)
                .setContentText(body)
                .setContentInfo("Info")
                .setContentIntent(resultPendingIntent)
                .addAction(R.mipmap.ic_launcher, "Accept", actionIntent)
                .build();

        notificationManager.notify(new Random().nextInt(),notificationBuilder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Map<String,Object> userProfile = new HashMap<>();
        userProfile.put("token", s);

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .set(userProfile);
    }

    // Helper method to retrieve user's email
    private String getEmailFromBody(String body) {
        String result = body.substring(0,body.lastIndexOf(" "));
        return result.substring(0,result.lastIndexOf(" "));
    }

}
