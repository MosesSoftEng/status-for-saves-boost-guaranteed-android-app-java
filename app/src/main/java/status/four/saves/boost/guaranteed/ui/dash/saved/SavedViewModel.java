package status.four.saves.boost.guaranteed.ui.dash.saved;

import static status.four.saves.boost.guaranteed.shared.Config.SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import status.four.saves.boost.guaranteed.data.remote.ContactsApi;
import status.four.saves.boost.guaranteed.data.local.ContactsRepo;
import status.four.saves.boost.guaranteed.data.local.SharedPreferencesHelper;
import status.four.saves.boost.guaranteed.domain.contact.Contact;

public class SavedViewModel extends AndroidViewModel {
    private ContactsRepo contactsRepo;
    private ContactsApi contactsApi;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public MutableLiveData<ArrayList<Contact>> contactList;

    public SavedViewModel(@NonNull Application application, Activity activity) {
        super(application);

        contactsRepo = ContactsRepo.getInstance(activity);
        contactsApi = ContactsApi.getInstance(application.getApplicationContext());
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(application.getApplicationContext());

        contactList = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Contact>> getContactList() {
        return contactList;
    }

    public void fetchContacts() {
        contactList.setValue(contactsRepo.getContacts());
    }

    public void deleteContact(Contact contact) {
        if(contactsRepo.deleteContact(contact)){

            String loggedInUserPhoneNumber = sharedPreferencesHelper.getString(SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER, "");

            contactsApi.deleteContact(loggedInUserPhoneNumber, contact.getPhone(), new ContactsApi.Callback() {
                @Override
                public void onSuccess(String message) {
                    removeContactFromContactsList(contact);
                }

                @Override
                public void onError(Throwable throwable) {
                    // TODO: Display retry notification.
                }
            });
        }
    }

    /**
     * Removes the given contact from the contact list.
     *
     * @param contact The contact to be removed from the list.
     */
    public void removeContactFromContactsList(Contact contact) {
        ArrayList<Contact> contactList = this.contactList.getValue();

        if (contactList != null) {
            contactList.remove(contact);
            this.contactList.setValue(contactList);
        }
    }
}
