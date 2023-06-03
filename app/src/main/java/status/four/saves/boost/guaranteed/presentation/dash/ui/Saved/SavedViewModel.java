package status.four.saves.boost.guaranteed.presentation.dash.ui.Saved;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SavedViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SavedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Saved fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}