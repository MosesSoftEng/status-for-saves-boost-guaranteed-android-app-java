package status.four.saves.boost.guaranteed.ui.dash.saveBack;

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


public class SaveBackRecyclerViewAdapter extends RecyclerView.Adapter<SaveBackRecyclerViewAdapter.ViewHolder> {
    private ArrayList<User> newUsers = new ArrayList<>();
    private SaveBackViewModel saveBackViewModel;


    public SaveBackRecyclerViewAdapter(SaveBackViewModel saveBackViewModel) {
        this.saveBackViewModel = saveBackViewModel;
    }

    public void setData(ArrayList<User> newUsers) {
        this.newUsers = newUsers;
        this.saveBackViewModel = saveBackViewModel;
    }

    @NonNull
    @Override
    public SaveBackRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaveBackRecyclerViewAdapter.ViewHolder holder, int position) {
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

            String phoneNumber = String.valueOf(user.getPhone());
            String maskedPhoneNumber = phoneNumber.substring(0, phoneNumber.length() - 3) + "XXX";
            phoneTextView.setText(maskedPhoneNumber);
        }

        @Override
        public void onClick(View view) {
            if(view == savePhoneButton) {
                saveBackViewModel.saveContact(user);
            }
        }
    }
}
