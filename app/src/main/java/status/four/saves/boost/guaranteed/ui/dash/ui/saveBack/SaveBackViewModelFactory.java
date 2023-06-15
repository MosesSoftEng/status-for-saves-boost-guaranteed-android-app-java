package status.four.saves.boost.guaranteed.ui.dash.ui.saveBack;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SaveBackViewModelFactory implements ViewModelProvider.Factory {
    private final Activity activity;

    public SaveBackViewModelFactory(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SaveBackViewModel.class)) {
            return (T) new SaveBackViewModel(activity.getApplication(), activity);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}

