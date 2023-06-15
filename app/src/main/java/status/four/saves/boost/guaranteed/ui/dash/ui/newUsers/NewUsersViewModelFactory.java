package status.four.saves.boost.guaranteed.ui.dash.ui.newUsers;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NewUsersViewModelFactory implements ViewModelProvider.Factory {
    private final Activity activity;

    public NewUsersViewModelFactory(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewUserViewModel.class)) {
            return (T) new NewUserViewModel(activity.getApplication(), activity);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}

