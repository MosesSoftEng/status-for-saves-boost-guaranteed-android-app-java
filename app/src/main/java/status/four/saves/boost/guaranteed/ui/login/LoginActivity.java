package status.four.saves.boost.guaranteed.ui.login;

import static status.four.saves.boost.guaranteed.shared.Config.URL_PRIVATE_POLICY;
import static status.four.saves.boost.guaranteed.shared.Config.URL_TERMS_OF_USE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import status.four.saves.boost.guaranteed.R;
import status.four.saves.boost.guaranteed.domain.user.UsersService;
import status.four.saves.boost.guaranteed.ui.dash.DashActivity;
import status.four.saves.boost.guaranteed.shared.Logger;
import status.four.saves.boost.guaranteed.ui.webview.WebViewActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout whatsAppPhoneNumberTextInputLayout;
    Button saveWhatsAppPhoneNumberButton, termsOfUseButton, privatePolicyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        setOnClickListeners();
    }

    private void initViews() {
        setContentView(R.layout.activity_login);
        whatsAppPhoneNumberTextInputLayout = findViewById(R.id.whatsAppPhoneNumberTextInputLayout);
        saveWhatsAppPhoneNumberButton = findViewById(R.id.saveWhatsAppPhoneNumberButton);
        termsOfUseButton = findViewById(R.id.termsOfUseButton);
        privatePolicyButton = findViewById(R.id.privatePolicyButton);
    }

    private void setOnClickListeners() {
        saveWhatsAppPhoneNumberButton.setOnClickListener(this);
        termsOfUseButton.setOnClickListener(this);
        privatePolicyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == saveWhatsAppPhoneNumberButton) {
            loginUser(view);
        } else if (view == termsOfUseButton) {
            showWebPage(URL_TERMS_OF_USE, "Terms of use.");
        } else if (view == privatePolicyButton) {
            showWebPage(URL_PRIVATE_POLICY, "Private policy.");
        }
    }

    /*
     * Methods.
     */
    private void loginUser(View view) {
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
                startActivity(new Intent(LoginActivity.this, DashActivity.class));
            }

            @Override
            public void onError(Throwable throwable) {
                Logger.d(throwable.getMessage());

                Snackbar.make(view, "Connection problem, try gain", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Shows a web page by starting the WebViewActivity with the provided URL and title.
     * @param url The URL of the web page to display.
     * @param title The title of the web page.
     */
    private void showWebPage(String url, String title) {
        startActivity(new Intent(this, WebViewActivity.class)
                .putExtra("url", url)
                .putExtra("title", title)
        );
    }
}
