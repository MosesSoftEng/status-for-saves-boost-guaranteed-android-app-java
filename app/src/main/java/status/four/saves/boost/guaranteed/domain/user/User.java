package status.four.saves.boost.guaranteed.domain.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    private long phone;
    private long date;

    public User(long phone, long date) {
        this.phone = phone;
        this.date = date;
    }

    public long getPhone() {
        return phone;
    }

    public long getDate() {
        return date;
    }

    public static ArrayList<User> fromJsonArray(String jsonString) throws JSONException, JSONException {
        ArrayList<User> userList = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            long phone = jsonObject.getLong("phone");
            long date = jsonObject.getLong("date");

            User user = new User(phone, date);
            userList.add(user);
        }

        return userList;
    }

    @Override
    public String toString() {
        return "User{" +
                "phone=" + phone +
                ", date=" + date +
                '}';
    }
}
