package status.four.saves.boost.guaranteed.presentation.dash.ui.newUsers;

import static status.four.saves.boost.guaranteed.shared.Config.SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER;
import static status.four.saves.boost.guaranteed.shared.Config.PAGINATION_NUMBER;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;

import java.util.ArrayList;

import status.four.saves.boost.guaranteed.data.api.ContactsApi;
import status.four.saves.boost.guaranteed.data.api.UsersApi;
import status.four.saves.boost.guaranteed.data.storage.ContactsRepo;
import status.four.saves.boost.guaranteed.data.storage.SharedPreferencesHelper;
import status.four.saves.boost.guaranteed.domain.user.User;
import status.four.saves.boost.guaranteed.shared.Logger;

public class NewUserViewModel extends AndroidViewModel {
    UsersApi usersApi;
    ContactsApi contactsApi;
    ContactsRepo contactsRepo;

    private SharedPreferencesHelper sharedPreferencesHelper;
    private final MutableLiveData<ArrayList<User>> users;
    private final MutableLiveData<User> savedUser;
    private long lastIndex = 0;

    public NewUserViewModel(@NonNull Application application, Activity activity) {
        super(application);
        users = new MutableLiveData<>();
        savedUser = new MutableLiveData<>();

        usersApi = UsersApi.getInstance(application.getApplicationContext());
        contactsApi = ContactsApi.getInstance(application.getApplicationContext());
        contactsRepo = ContactsRepo.getInstance(activity);
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(application.getApplicationContext());
    }

    public LiveData<ArrayList<User>> getUsers() {
        return users;
    }

    public LiveData<User> getSavedUsers() {
        return savedUser;
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

    public void saveContact(User user) {
        savedUser.setValue(user);
        contactsRepo.saveContactManual(user);
    }

    /**
     * Removes the given user from the user list if a corresponding contact exists.
     * If the contact exists, the user will be removed from the list.
     *
     * @param user The user to be checked and removed if a contact exists.
     */
    public void removeUserIfContactExists(User user) {
        String contactName = "4saves" + user.getPhone();
        String phoneNumber = "" + user.getPhone();

        if (contactsRepo.checkContactExists(contactName, phoneNumber)) {
            String loggedInUserPhoneNumber = sharedPreferencesHelper.getString(SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER, "");

            contactsApi.saveContact(loggedInUserPhoneNumber, user.getPhone(), new ContactsApi.Callback() {
                @Override
                public void onSuccess(String message) {
                    Logger.d(message);
                }

                @Override
                public void onError(Throwable throwable) {
                    Logger.d(throwable.getMessage());
                }
            });

            removeUser(user);
        }
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
}
