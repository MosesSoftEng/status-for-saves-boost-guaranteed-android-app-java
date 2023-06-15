package status.four.saves.boost.guaranteed.ui.dash.saveBack;

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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import status.four.saves.boost.guaranteed.databinding.FragmentHomeBinding;
import status.four.saves.boost.guaranteed.domain.user.User;

public class SaveBackFragment extends Fragment {
    private SaveBackViewModel saveBackViewModel;

    private FragmentHomeBinding binding;

    private SwipeRefreshLayout savedBackSwipeRefreshLayout;
    private RecyclerView savedBackRecyclerView;
    private SaveBackRecyclerViewAdapter savedBackRecyclerViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        saveBackViewModel = new ViewModelProvider(this, new SaveBackViewModelFactory(getActivity())).get(SaveBackViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        savedBackSwipeRefreshLayout = binding.savedBackSwipeRefreshLayout;
        savedBackSwipeRefreshLayout.setOnRefreshListener(this::refreshData);
        savedBackRecyclerViewAdapter = new SaveBackRecyclerViewAdapter(saveBackViewModel);
        savedBackRecyclerView = binding.savedBackRecyclerView;
        savedBackRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        savedBackRecyclerView.setAdapter(savedBackRecyclerViewAdapter);

        saveBackViewModel.getUsersSavedMeList().observe(getViewLifecycleOwner(), this::updateUsersSavedMeList);

        fetchUsersSavedMe();

        return root;
    }

    private void updateUsersSavedMeList(ArrayList<User> usersSavedMeList) {
        savedBackRecyclerViewAdapter.setData(usersSavedMeList);
        savedBackRecyclerViewAdapter.notifyDataSetChanged();

        savedBackSwipeRefreshLayout.setRefreshing(false);
    }

    /*
     * Methods.
     */
    private void fetchUsersSavedMe() {
        savedBackSwipeRefreshLayout.setRefreshing(true);
        saveBackViewModel.fetchUsersSavedMe(new SaveBackViewModel.Callback() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onError(Throwable throwable) {
                Snackbar.make(binding.getRoot(), "Connection failed.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", v -> fetchUsersSavedMe())
                        .show();

                savedBackSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /*
     * Events and Handlers.
     */
    private void refreshData() {
        saveBackViewModel.clearUsers();
        fetchUsersSavedMe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
