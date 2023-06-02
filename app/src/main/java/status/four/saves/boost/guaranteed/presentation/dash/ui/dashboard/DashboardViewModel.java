package status.four.saves.boost.guaranteed.presentation.dash.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;

import java.util.ArrayList;

import status.four.saves.boost.guaranteed.data.api.UsersApi;
import status.four.saves.boost.guaranteed.domain.user.User;
import status.four.saves.boost.guaranteed.shared.Logger;

public class DashboardViewModel extends AndroidViewModel {
    UsersApi usersApi;
    private final MutableLiveData<ArrayList<User>> users;
    private String lastIndex = "0";

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        users = new MutableLiveData<>();
        users.setValue(new ArrayList<>());

        usersApi = UsersApi.getInstance(application.getApplicationContext());
    }

    public LiveData<ArrayList<User>> getUsers() {
        return users;
    }

    public void getNewUsers() {
        usersApi.getUsers(0, 2, new UsersApi.Callback() {
            @Override
            public void onSuccess(String message) {
                Logger.d("DashboardViewModel fetchNewUsers usersApi.getUsers, message:", message);

                try {
                    appendUsersToList(User.fromJsonArray(message), users);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    private void appendUsersToList(ArrayList<User> usersList, MutableLiveData<ArrayList<User>> newUsers) {
        ArrayList<User> currentList = newUsers.getValue();

        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.addAll(usersList);
        newUsers.setValue(currentList);

        // Assign lastIndex User phone value
//        if (!usersList.isEmpty()) {
//            User lastUser = usersList.get(usersList.size() - 1);
//            lastIndex = String.valueOf(lastUser.getPhone());
//        }
    }
}
