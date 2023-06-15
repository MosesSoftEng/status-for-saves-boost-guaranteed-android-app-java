package status.four.saves.boost.guaranteed.shared;

import static status.four.saves.boost.guaranteed.shared.Config.SHARED_PREFS_KEY_FCM_PUSH_NOTIFICATION_TOKEN;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import status.four.saves.boost.guaranteed.R;
import status.four.saves.boost.guaranteed.data.local.SharedPreferencesHelper;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Logger.d("New Token: ", token);

        SharedPreferencesHelper.getInstance(getApplicationContext()).removeString(SHARED_PREFS_KEY_FCM_PUSH_NOTIFICATION_TOKEN);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Logger.d("FCM noti title: ", message.getData().get("title"));
        Logger.d("FCM noti message: ", message.getData().get("message"));

        // Create a notification builder with the channel ID
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.baseline_contact_page_24)
                .setContentTitle(message.getData().get("title"))
                .setContentText(message.getData().get("message"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}
