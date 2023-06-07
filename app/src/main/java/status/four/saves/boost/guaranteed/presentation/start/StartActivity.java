package status.four.saves.boost.guaranteed.presentation.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import status.four.saves.boost.guaranteed.R;
import status.four.saves.boost.guaranteed.domain.user.UsersService;
import status.four.saves.boost.guaranteed.presentation.dash.DashActivity;
import status.four.saves.boost.guaranteed.presentation.login.LoginActivity;
import status.four.saves.boost.guaranteed.shared.Logger;

public class StartActivity extends AppCompatActivity {
    UsersService usersService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // TODO: Check if terms and private policy are accepted.
        usersService = new UsersService(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(usersService.isUserLoggedIn())
            startActivity(new Intent(StartActivity.this, DashActivity.class));
        else
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
    }
}