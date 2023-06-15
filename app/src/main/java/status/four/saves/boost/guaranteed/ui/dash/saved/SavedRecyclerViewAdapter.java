package status.four.saves.boost.guaranteed.ui.dash.saved;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import status.four.saves.boost.guaranteed.R;
import status.four.saves.boost.guaranteed.domain.contact.Contact;


public class SavedRecyclerViewAdapter extends RecyclerView.Adapter<SavedRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Contact> contacts = new ArrayList<>();
    private SavedViewModel savedViewModel;


    public SavedRecyclerViewAdapter(SavedViewModel savedViewModel) {
        this.savedViewModel = savedViewModel;
    }

    public void setData(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public SavedRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_item, parent, false);
        return new SavedRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.populateViews(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Contact contact;
        TextView phoneTextView;
        Button deletePhoneButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            deletePhoneButton = itemView.findViewById(R.id.deletePhoneButton);
            deletePhoneButton.setOnClickListener(this);
        }

        public void populateViews(Contact contact) {
            this.contact = contact;
            phoneTextView.setText(String.valueOf(contact.getPhone()));
        }

        @Override
        public void onClick(View view) {
            if(view == deletePhoneButton) {
                savedViewModel.deleteContact(contact);
            }
        }
    }
}
