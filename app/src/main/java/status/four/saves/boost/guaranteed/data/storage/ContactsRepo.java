package status.four.saves.boost.guaranteed.data.storage;


import status.four.saves.boost.guaranteed.domain.user.User;

public class ContactsRepo {
    private static ContactsRepo instance;

    public ContactsRepo() {
    }

    public static synchronized ContactsRepo getInstance() {
        if (instance == null) {
            instance = new ContactsRepo();
        }
        return instance;
    }

    public void saveContact(User user) {
    }
}
