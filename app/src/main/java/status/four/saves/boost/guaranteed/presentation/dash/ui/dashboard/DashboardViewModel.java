package status.four.saves.boost.guaranteed.presentation.dash.ui.dashboard;

import static status.four.saves.boost.guaranteed.shared.Config.paginationCount;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;

import java.util.ArrayList;

import status.four.saves.boost.guaranteed.data.api.UsersApi;
import status.four.saves.boost.guaranteed.data.storage.ContactsRepo;
import status.four.saves.boost.guaranteed.domain.user.User;
import status.four.saves.boost.guaranteed.shared.Logger;

public class DashboardViewModel extends AndroidViewModel {
    UsersApi usersApi;
    ContactsRepo contactsRepo;
    private final MutableLiveData<ArrayList<User>> users;
    private final MutableLiveData<User> savedUser;
    private long lastIndex = 0;

    public DashboardViewModel(@NonNull Application application, Activity activity) {
        super(application);
        users = new MutableLiveData<>();
        savedUser = new MutableLiveData<>();

        usersApi = UsersApi.getInstance(application.getApplicationContext());
        contactsRepo = ContactsRepo.getInstance(activity);
    }

    public LiveData<ArrayList<User>> getUsers() {
        return users;
    }

    public LiveData<User> getSavedUsers() {
        return savedUser;
    }

    public void fetchUsers() {
        usersApi.getUsers(lastIndex, paginationCount, new UsersApi.Callback() {
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

        if (!usersList.isEmpty()) {
            User lastUser = usersList.get(usersList.size() - 1);
            lastIndex = lastUser.getPhone();
        }
    }

    public void clearUsers() {
        users.setValue(new ArrayList<>());
        lastIndex = 0;
    }

    public void saveContact(User user) {
        Logger.d("Add to contact: ", user.toString());

        savedUser.setValue(user);

        contactsRepo.saveContact(user);
    }

    public void removeUser(User user) {
        ArrayList<User> userList = users.getValue();
        if (userList != null) {
            userList.remove(user);
            users.setValue(userList);
        }
    }
}
