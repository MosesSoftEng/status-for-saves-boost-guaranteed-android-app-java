package status.four.saves.boost.guaranteed.presentation.dash.ui.newUsers;

import static status.four.saves.boost.guaranteed.shared.Config.SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER;
import static status.four.saves.boost.guaranteed.shared.Config.PAGINATION_NUMBER;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.util.ArrayList;

import status.four.saves.boost.guaranteed.data.api.ContactsApi;
import status.four.saves.boost.guaranteed.data.api.UsersApi;
import status.four.saves.boost.guaranteed.data.storage.ContactsRepo;
import status.four.saves.boost.guaranteed.data.storage.SharedPreferencesHelper;
import status.four.saves.boost.guaranteed.domain.user.User;
import status.four.saves.boost.guaranteed.shared.Logger;

public class NewUserViewModel extends AndroidViewModel {
    private Activity activity;
    private UsersApi usersApi;
    private ContactsApi contactsApi;
    private ContactsRepo contactsRepo;
    private SharedPreferencesHelper sharedPreferencesHelper;

    private final MutableLiveData<ArrayList<User>> users;
    private long lastIndex = 0;

    public NewUserViewModel(@NonNull Application application, Activity activity) {
        super(application);
        this.activity = activity;

        users = new MutableLiveData<>();

        usersApi = UsersApi.getInstance(application.getApplicationContext());
        contactsApi = ContactsApi.getInstance(application.getApplicationContext());
        contactsRepo = ContactsRepo.getInstance(activity);
    }

    public LiveData<ArrayList<User>> getUsers() {
        return users;
    }

    public void fetchUsers() {
        usersApi.getUsers(lastIndex, PAGINATION_NUMBER, new UsersApi.Callback() {
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


    /**
     * Removes the given user from the user list.
     *
     * @param user The user to be removed from the list.
     */
    public void removeUser(User user) {
        ArrayList<User> userList = users.getValue();

        if (userList != null) {
            userList.remove(user);
            users.setValue(userList);
        }
    }

    /*
     * Contacts methods.
     */
    public void saveContact(User user) {
        contactsRepo.saveContact(user, new ContactsRepo.Callback() {
            @Override
            public void onSuccess(String message) {
                removeUser(user);
            }

            @Override
            public void onError(Throwable throwable) {
                Snackbar.make(activity.getWindow().getDecorView().getRootView(), "Saving contact failed.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", v -> saveContact(user))
                        .show();
            }
        });
    }
}
