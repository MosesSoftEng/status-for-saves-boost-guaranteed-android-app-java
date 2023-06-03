package status.four.saves.boost.guaranteed.presentation.dash.ui.Saved;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import status.four.saves.boost.guaranteed.domain.contact.Contact;

public class SavedViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Contact>> contacts;

    public SavedViewModel() {
        contacts = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Contact>> getContacts() {
        return contacts;
    }
}