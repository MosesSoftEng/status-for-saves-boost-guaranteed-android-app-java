package status.four.saves.boost.guaranteed.domain.user;

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
}
