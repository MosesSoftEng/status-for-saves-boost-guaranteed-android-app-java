package status.four.saves.boost.guaranteed.ui.webview;

import static status.four.saves.boost.guaranteed.shared.Config.URL_TERMS_OF_USE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import status.four.saves.boost.guaranteed.R;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");

        if (isInvalidData(url, title)) {
            finish();
            return;
        }

        setTitle(title);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    /*
     * Events.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*
     * Methods.
     */
    /**
     * Checks if the provided URL and title are valid.
     * @param url The URL string.
     * @param title The title string.
     * @return {@code true} if either the URL or the title is null or empty, {@code false} otherwise.
     */
    private boolean isInvalidData(String url, String title) {
        return url == null || url.isEmpty() || title == null || title.isEmpty();
    }
}
