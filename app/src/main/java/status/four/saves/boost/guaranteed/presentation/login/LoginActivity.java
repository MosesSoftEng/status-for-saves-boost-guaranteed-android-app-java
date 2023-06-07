package status.four.saves.boost.guaranteed.presentation.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import status.four.saves.boost.guaranteed.R;
import status.four.saves.boost.guaranteed.domain.user.UsersService;
import status.four.saves.boost.guaranteed.presentation.dash.DashActivity;
import status.four.saves.boost.guaranteed.presentation.start.StartActivity;
import status.four.saves.boost.guaranteed.shared.Logger;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout whatsAppPhoneNumberTextInputLayout;
    Button saveWhatsAppPhoneNumberButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        whatsAppPhoneNumberTextInputLayout = findViewById(R.id.whatsAppPhoneNumberTextInputLayout);
        saveWhatsAppPhoneNumberButton = findViewById(R.id.saveWhatsAppPhoneNumberButton);

        saveWhatsAppPhoneNumberButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Logger.d("LoginActivity onClick saveWhatsAppPhoneNumberButton");

        if(view == saveWhatsAppPhoneNumberButton) {
            String whatsAppPhoneNumber = whatsAppPhoneNumberTextInputLayout.getEditText().getText().toString();

            if(whatsAppPhoneNumber.equals("")) {
                whatsAppPhoneNumberTextInputLayout.setErrorEnabled(true);
                whatsAppPhoneNumberTextInputLayout.setError("Required");
            } else
                whatsAppPhoneNumberTextInputLayout.setErrorEnabled(false);

            UsersService usersService = new UsersService(getApplicationContext());

            usersService.loginUser(whatsAppPhoneNumber, new UsersService.Callback() {
                @Override
                public void onSuccess(String message) {
                    Logger.d("LoginActivity onClick usersService.loginUser, message:", message);

                    startActivity(new Intent(LoginActivity.this, StartActivity.class));
                }

                @Override
                public void onError(Throwable throwable) {
                    Logger.d(throwable.getMessage());

                    Snackbar.make(view, "Connection problem, try gain", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }
}