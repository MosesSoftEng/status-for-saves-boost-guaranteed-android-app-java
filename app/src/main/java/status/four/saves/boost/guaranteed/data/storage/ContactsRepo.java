package status.four.saves.boost.guaranteed.data.storage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import status.four.saves.boost.guaranteed.domain.contact.Contact;
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

                activity.startActivity(intent);
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

    /**
     * Checks if a contact with the given name and phone number exists in the phone contacts.
     *
     * @param name        The name of the contact to check.
     * @param phoneNumber The phone number of the contact to check.
     * @return {@code true} if the contact exists, {@code false} otherwise.
     */
    public boolean checkContactExists(String name, String phoneNumber) {
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ? AND " +
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?";
        String[] selectionArgs = {name, phoneNumber};

        try (Cursor cursor = activity.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        )) {
            return cursor != null && cursor.getCount() > 0;
        } catch (Exception e) {
            Logger.e("Error checking contact existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves the saved contacts from the phone contacts.
     *
     * @return The list of saved contacts.
     */
    public ArrayList<Contact> getContacts() {
        ArrayList<Contact> contactList = new ArrayList<>();

        // TODO: find a better way to check for permissions.
        if(permission.isPermissionGranted(Manifest.permission.WRITE_CONTACTS)) {
            if(permission.isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
                String[] projection = {
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };

                String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ?";
                String[] selectionArgs = {"4saves%"};

                Cursor cursor = activity.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        projection,
                        selection,
                        selectionArgs,
                        null
                );

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        Logger.d(name, phoneNumber);

                        Contact contact = new Contact(name.replace("4saves", ""), phoneNumber);
                        contactList.add(contact);
                    }

                    cursor.close();
                }
            } else {
                permission.requestPermission(Manifest.permission.READ_CONTACTS, 2);
            }
        } else {
            permission.requestPermission(Manifest.permission.WRITE_CONTACTS, 1);
        }

        return contactList;
    }

    public boolean deleteContact(Contact contact) {
        ContentResolver contentResolver = activity.getContentResolver();

        // Create the contact URI
        Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, getContactId("4saves" + contact.getPhone(), ""+contact.getPhone()));

        // Delete the contact
        int rowsDeleted = contentResolver.delete(contactUri, null, null);

        if (rowsDeleted > 0) {
            // Contact deleted successfully
            Logger.d("Contact deleted");
            return true;
        } else {
            // Failed to delete contact
            Logger.d("Failed to delete contact");
            return false;
        }
    }

    @SuppressLint("Range")
    public String getContactId(String displayName, String phoneNumber) {
        Logger.d(displayName, phoneNumber);

        String contactId = null;

        // Query the ContactsContract Data table
        Cursor cursor = activity.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data.CONTACT_ID},
                ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
                        ContactsContract.Data.MIMETYPE + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?",
                new String[]{displayName, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, phoneNumber},
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID));
            }
            cursor.close();
        }

        return contactId;
    }
}
