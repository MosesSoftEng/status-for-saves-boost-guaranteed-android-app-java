package status.four.saves.boost.guaranteed.presentation.dash.ui.Saved;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import status.four.saves.boost.guaranteed.domain.contact.Contact;

public class SavedViewModel extends AndroidViewModel {

    private final MutableLiveData<ArrayList<Contact>> contacts;

    public SavedViewModel(@NonNull Application application) {
        super(application);

        contacts = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Contact>> getContacts() {
        return contacts;
    }
}