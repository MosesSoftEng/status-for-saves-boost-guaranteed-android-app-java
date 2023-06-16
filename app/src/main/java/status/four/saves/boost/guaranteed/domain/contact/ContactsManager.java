package status.four.saves.boost.guaranteed.domain.contact;

import java.util.ArrayList;

/*
 * Singleton class to manage saved contacts.
 */
public class ContactsManager {
    private static ContactsManager instance;

    private ArrayList<Contact> savedContacts = new ArrayList<>();

    public static synchronized ContactsManager getInstance() {
        if(instance == null) {
            instance = new ContactsManager();
        }

        return instance;
    }

    public ArrayList<Contact> getSavedContacts() {
        return savedContacts;
    }
}
