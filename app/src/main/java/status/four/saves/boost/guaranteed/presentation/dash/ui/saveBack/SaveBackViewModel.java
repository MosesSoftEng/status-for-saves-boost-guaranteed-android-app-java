package status.four.saves.boost.guaranteed.presentation.dash.ui.saveBack;

import static status.four.saves.boost.guaranteed.shared.Config.PAGINATION_NUMBER;
import static status.four.saves.boost.guaranteed.shared.Config.SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER;

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


public class SaveBackViewModel extends AndroidViewModel {
    Activity activity;
    private UsersApi usersApi;
    private ContactsApi contactsApi;
    private ContactsRepo contactsRepo;
    private SharedPreferencesHelper sharedPreferencesHelper;
    public MutableLiveData<ArrayList<User>> usersSavedMeList;

    private long lastIndex = 0;

    public SaveBackViewModel(@NonNull Application application, Activity activity) {
        super(application);

        this.activity = activity;
        usersApi = UsersApi.getInstance(application.getApplicationContext());
        contactsApi = ContactsApi.getInstance(application.getApplicationContext());
        contactsRepo = ContactsRepo.getInstance(activity);
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(application.getApplicationContext());

        usersSavedMeList = new MutableLiveData<>();
    }

    public LiveData<ArrayList<User>> getUsersSavedMeList() {
        return usersSavedMeList;
    }

    public void fetchUsersSavedMe(Callback callback) {
        String loggedInUserPhone = sharedPreferencesHelper.getString(SHARED_PREFS_KEY_USER_WHATSAPP_MOBILE_NUMBER, "");

        usersApi.getUsersSavedMe(loggedInUserPhone, lastIndex, PAGINATION_NUMBER, new UsersApi.Callback() {
            @Override
            public void onSuccess(String message) {
                Logger.d(message);

                try {
                    appendUsersToList(User.fromJsonArray(message), usersSavedMeList);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Logger.d(throwable.getMessage());
                callback.onError(throwable);
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

    /**
     * Removes the given user from the user list.
     *
     * @param user The user to be removed from the list.
     */
    public void removeUser(User user) {
        ArrayList<User> userList = usersSavedMeList.getValue();

        if (userList != null) {
            userList.remove(user);
            usersSavedMeList.setValue(userList);
        }
    }

    public void clearUsers() {
        usersSavedMeList.setValue(new ArrayList<>());
        lastIndex = 0;
    }

    /**
     * The callback interface for handling login results.
     */
    public interface Callback {
        /**
         * Called when the login operation is successful.
         *
         * @param message The success message.
         */
        void onSuccess(String message);

        /**
         * Called when an error occurs during the login operation.
         *
         * @param throwable The error that occurred.
         */
        void onError(Throwable throwable);
    }
}
