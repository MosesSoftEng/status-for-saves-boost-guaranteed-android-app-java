package status.four.saves.boost.guaranteed.shared;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission {
    private static Permission instance;
    private Activity context;

    public Permission(Activity context) {
        this.context = context;
    }

    public static synchronized Permission getInstance(Activity context) {
        if (instance == null) {
            instance = new Permission(context);
        }
        return instance;
    }

    public boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
    }

    public boolean isPermissionGranted(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    public boolean shouldShowRequestPermissionRationale(String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(context, permission);
    }
}
