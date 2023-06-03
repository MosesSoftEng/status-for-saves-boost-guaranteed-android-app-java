package status.four.saves.boost.guaranteed.presentation.dash.ui.Saved;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import status.four.saves.boost.guaranteed.data.storage.ContactsRepo;
import status.four.saves.boost.guaranteed.domain.contact.Contact;

public class SavedViewModel extends AndroidViewModel {
    ContactsRepo contactsRepo;

    private final MutableLiveData<ArrayList<Contact>> contacts;

    public SavedViewModel(@NonNull Application application, Activity activity) {
        super(application);

        contactsRepo = ContactsRepo.getInstance(activity);

        contacts = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Contact>> getContacts() {
        return contacts;
    }

    public void fetchContacts() {
        contacts.setValue(contactsRepo.getContacts());
    }
}