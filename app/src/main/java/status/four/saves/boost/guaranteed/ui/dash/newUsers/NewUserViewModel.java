package status.four.saves.boost.guaranteed.ui.dash.newUsers;

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

import status.four.saves.boost.guaranteed.data.remote.ContactsApi;
import status.four.saves.boost.guaranteed.data.remote.UsersApi;
import status.four.saves.boost.guaranteed.data.local.ContactsRepo;
import status.four.saves.boost.guaranteed.data.local.SharedPreferencesHelper;
import status.four.saves.boost.guaranteed.domain.contact.Contact;
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
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(application.getApplicationContext());
    }

    public LiveData<ArrayList<User>> getUsers() {
        return users;
    }

    public void fetchUsers() {
        String loggedInUserPhone = sharedPreferencesHelper.getString(SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER, "");

        usersApi.getUsers(loggedInUserPhone, lastIndex, PAGINATION_NUMBER, new UsersApi.Callback() {
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

    public ArrayList<User> filterUsersBySavedContacts(ArrayList<User> newUsersSavedMeList, ArrayList<Contact> savedContacts) {
        // Create a new list to store the filtered User objects
        ArrayList<User> filteredUsers = new ArrayList<>();

        // Iterate over the newUsersSavedMeList
        for (User user : newUsersSavedMeList) {
            // Check if the phone number exists in the savedContacts
            boolean phoneExists = false;
            for (Contact contact : savedContacts) {
                if (String.valueOf(user.getPhone()).equals(contact.getPhone())) {
                    phoneExists = true;
                    break;
                }
            }

            // If the phone number does not exist in savedContacts, add it to the filteredUsers list
            if (!phoneExists) {
                filteredUsers.add(user);
            }
        }

        // Return the filtered list of User objects
        return filteredUsers;
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
                String loggedInUserPhoneNumber = sharedPreferencesHelper.getString(SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER, "");

                contactsApi.saveContact(loggedInUserPhoneNumber, user.getPhone(), new ContactsApi.Callback() {
                    @Override
                    public void onSuccess(String message) {
                        removeUser(user);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
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
