package status.four.saves.boost.guaranteed.presentation.dash;

import android.Manifest;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import status.four.saves.boost.guaranteed.R;
import status.four.saves.boost.guaranteed.databinding.ActivityDashBinding;
import status.four.saves.boost.guaranteed.shared.Permission;

public class DashActivity extends AppCompatActivity {
    private ActivityDashBinding binding;
    private Permission permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        permission = Permission.getInstance(this);

        permission.requestPermission(Manifest.permission.WRITE_CONTACTS, 1);
        permission.requestPermission(Manifest.permission.READ_CONTACTS, 2);
    }
}
