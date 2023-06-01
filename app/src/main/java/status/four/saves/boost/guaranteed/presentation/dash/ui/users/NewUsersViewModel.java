package status.four.saves.boost.guaranteed.presentation.dash.ui.users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewUsersViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public NewUsersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}