package status.four.saves.boost.guaranteed.ui.dash.saved;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SavedViewModelFactory implements ViewModelProvider.Factory {
    private final Activity activity;

    public SavedViewModelFactory(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SavedViewModel.class)) {
            return (T) new SavedViewModel(activity.getApplication(), activity);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}