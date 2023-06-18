package status.four.saves.boost.guaranteed.ui.dash;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import status.four.saves.boost.guaranteed.R;
import status.four.saves.boost.guaranteed.data.remote.FCMTokenApi;
import status.four.saves.boost.guaranteed.data.local.SharedPreferencesHelper;
import status.four.saves.boost.guaranteed.databinding.ActivityDashBinding;
import status.four.saves.boost.guaranteed.shared.Logger;
import status.four.saves.boost.guaranteed.shared.Permission;

import static status.four.saves.boost.guaranteed.shared.Config.IS_EXITING;
import static status.four.saves.boost.guaranteed.shared.Config.SHARED_PREFS_KEY_FCM_PUSH_NOTIFICATION_TOKEN;
import static status.four.saves.boost.guaranteed.shared.Config.SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER;

public class DashActivity extends AppCompatActivity {
    private Permission permission;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private FCMTokenApi fcmTokenApi;

    private ActivityDashBinding binding;

    private static final String CHANNEL_ID = "channel_id";
    private static final int NOTIFICATION_ID = 1;


    /*
     * Events and Handlers.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permission = Permission.getInstance(this);
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(getApplicationContext());
        fcmTokenApi = FCMTokenApi.getInstance(getApplicationContext());

        permission.requestPermission(Manifest.permission.WRITE_CONTACTS, 1);
        permission.requestPermission(Manifest.permission.READ_CONTACTS, 2);

        permission.requestPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY, 3);
        permission.requestPermission(Manifest.permission.POST_NOTIFICATIONS, 4);

        binding = ActivityDashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_dash);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        Logger.i("SHARED_PREFS_KEY_FCM_PUSH_NOTIFICATION_TOKEN", sharedPreferencesHelper.getString(SHARED_PREFS_KEY_FCM_PUSH_NOTIFICATION_TOKEN, ""));

        // Save push token.
        // TODO: Move to own class FCMTokenService.
        if(!sharedPreferencesHelper.hasString(SHARED_PREFS_KEY_FCM_PUSH_NOTIFICATION_TOKEN)) {
            Logger.d("Save token");

            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        Logger.d("Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    String token = task.getResult();

                    Logger.d("Token: ", token);

                    String loggedInUserPhoneNumber = sharedPreferencesHelper.getString(SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER, "");
                    fcmTokenApi.saveFCMToken(loggedInUserPhoneNumber, token, new FCMTokenApi.Callback() {
                        @Override
                        public void onSuccess(String message) {
                            sharedPreferencesHelper.saveString(SHARED_PREFS_KEY_FCM_PUSH_NOTIFICATION_TOKEN, token);
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    });
                }
            });
        }

        createNotificationChannel();

        // Check if notification permission is granted
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            // Notifications are enabled
            showNotification();
        } else {
            // Notifications are not enabled, request permission
            requestNotificationPermission();
        }
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_contact_page_24)
                .setContentTitle("Notification Title")
                .setContentText("Notification Text")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void requestNotificationPermission() {
        Intent intent= new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Open app notification settings
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        } else {
            // Open application settings
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
        }
        startActivity(intent);
    }

    /*
     * Events
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dash, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        IS_EXITING = true;
    }
}
