package status.four.saves.boost.guaranteed.data.storage;

import static status.four.saves.boost.guaranteed.shared.Config.CONTACT_INSERT_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;

import status.four.saves.boost.guaranteed.domain.user.User;
import status.four.saves.boost.guaranteed.shared.Logger;
import status.four.saves.boost.guaranteed.shared.Permission;

public class ContactsRepo {
    private static ContactsRepo instance;
    private Permission permission;

    Activity activity;

    public ContactsRepo(Activity activity) {
        this.activity = activity;
        permission = Permission.getInstance(activity);
    }

    public static synchronized ContactsRepo getInstance(Activity activity) {
        if (instance == null) {
            instance = new ContactsRepo(activity);
        }
        return instance;
    }

    public void saveContact(User user) {
        if(permission.isPermissionGranted(Manifest.permission.WRITE_CONTACTS)) {
            if(permission.isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
                Logger.d("Save contact");

                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                intent.putExtra(ContactsContract.Intents.Insert.NAME, "4saves" + user.getPhone());
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, "" + user.getPhone());

                activity.startActivityForResult(intent, CONTACT_INSERT_REQUEST_CODE);
            } else {
                if (!permission.shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                    // TODO: Guide user to set permission manually.
                    Logger.d("READ_CONTACTS Permission permanently denied.");
                } else {
                    permission.requestPermission(Manifest.permission.READ_CONTACTS, 2);
                }
            }
        } else {
            if (!permission.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                // TODO: Guide user to set permission manually.
                Logger.d("WRITE_CONTACTS Permission permanently denied.");
            } else {
                permission.requestPermission(Manifest.permission.WRITE_CONTACTS, 1);
            }
        }
    }
}
