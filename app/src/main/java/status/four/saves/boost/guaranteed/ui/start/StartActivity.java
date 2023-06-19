package status.four.saves.boost.guaranteed.ui.start;

import static status.four.saves.boost.guaranteed.shared.Config.IS_EXITING;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import status.four.saves.boost.guaranteed.R;
import status.four.saves.boost.guaranteed.domain.user.UsersService;
import status.four.saves.boost.guaranteed.ui.dash.DashActivity;
import status.four.saves.boost.guaranteed.ui.login.LoginActivity;

public class StartActivity extends AppCompatActivity {
    UsersService usersService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        usersService = new UsersService(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(IS_EXITING) {
            this.finish();
        }

        if(usersService.isUserLoggedIn())
            startActivity(new Intent(StartActivity.this, DashActivity.class));
        else
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
    }
}
