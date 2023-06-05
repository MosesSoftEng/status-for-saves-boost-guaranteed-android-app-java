package status.four.saves.boost.guaranteed.presentation.dash.ui.Saved;

import static status.four.saves.boost.guaranteed.shared.Config.SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import status.four.saves.boost.guaranteed.data.api.ContactsApi;
import status.four.saves.boost.guaranteed.data.storage.ContactsRepo;
import status.four.saves.boost.guaranteed.data.storage.SharedPreferencesHelper;
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
            removeContactFromContactsList(contact);

            // TODO: delete contact online
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
