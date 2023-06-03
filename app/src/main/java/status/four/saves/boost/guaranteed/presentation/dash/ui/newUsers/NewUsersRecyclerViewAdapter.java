package status.four.saves.boost.guaranteed.presentation.dash.ui.newUsers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import status.four.saves.boost.guaranteed.R;
import status.four.saves.boost.guaranteed.domain.user.User;


public class NewUsersRecyclerViewAdapter extends RecyclerView.Adapter<NewUsersRecyclerViewAdapter.ViewHolder> {
    private ArrayList<User> newUsers = new ArrayList<>();
    private NewUserViewModel newUserViewModel;


    public NewUsersRecyclerViewAdapter(NewUserViewModel newUserViewModel) {
        this.newUserViewModel = newUserViewModel;
    }

    public void setData(ArrayList<User> newUsers) {
        this.newUsers = newUsers;
        this.newUserViewModel = newUserViewModel;
    }

    @NonNull
    @Override
    public NewUsersRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewUsersRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.populateViews(newUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return newUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        User user;
        TextView phoneTextView;
        Button savePhoneButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            savePhoneButton = itemView.findViewById(R.id.savePhoneButton);
            savePhoneButton.setOnClickListener(this);
        }

        public void populateViews(User user) {
            this.user = user;
            phoneTextView.setText(String.valueOf(user.getPhone()));
        }

        @Override
        public void onClick(View view) {
            if(view == savePhoneButton) {
                newUserViewModel.saveContact(user);
            }
        }
    }
}
