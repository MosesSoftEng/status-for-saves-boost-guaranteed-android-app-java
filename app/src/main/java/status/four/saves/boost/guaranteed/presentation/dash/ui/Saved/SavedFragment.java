package status.four.saves.boost.guaranteed.presentation.dash.ui.Saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import status.four.saves.boost.guaranteed.databinding.FragmentSavedBinding;
import status.four.saves.boost.guaranteed.domain.contact.Contact;

public class SavedFragment extends Fragment {
    SavedViewModel savedViewModel;

    private FragmentSavedBinding binding;

    private SwipeRefreshLayout savedSwipeRefreshLayout;
    private RecyclerView savedRecyclerView;
    private SavedRecyclerViewAdapter savedRecyclerViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        savedViewModel = new ViewModelProvider(this, new SavedViewModelFactory(getActivity())).get(SavedViewModel.class);

        binding = FragmentSavedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        savedSwipeRefreshLayout = binding.savedSwipeRefreshLayout;
        savedSwipeRefreshLayout.setOnRefreshListener(this::refreshData);

        savedRecyclerViewAdapter = new SavedRecyclerViewAdapter(savedViewModel);

        savedRecyclerView = binding.savedRecyclerView;
        savedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        savedRecyclerView.setAdapter(savedRecyclerViewAdapter);

        savedViewModel.getContactList().observe(getViewLifecycleOwner(), this::updateContactsList);

        fetchContacts();

        return root;
    }

    private void fetchContacts() {
        savedSwipeRefreshLayout.setRefreshing(true);
        savedViewModel.fetchContacts();
    }

    private void updateContactsList(ArrayList<Contact> contacts) {
        savedRecyclerViewAdapter.setData(contacts);
        savedRecyclerViewAdapter.notifyDataSetChanged();

        savedSwipeRefreshLayout.setRefreshing(false);
    }

    private void refreshData() {
        savedViewModel.contactList.setValue(new ArrayList<>());
        fetchContacts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
