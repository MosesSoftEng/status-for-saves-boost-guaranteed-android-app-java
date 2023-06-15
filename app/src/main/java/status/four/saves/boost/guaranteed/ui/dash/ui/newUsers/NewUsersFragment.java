package status.four.saves.boost.guaranteed.ui.dash.ui.newUsers;

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

import status.four.saves.boost.guaranteed.databinding.FragmentNewUserBinding;
import status.four.saves.boost.guaranteed.domain.user.User;
import status.four.saves.boost.guaranteed.shared.Logger;


/**
 * Fragment class for displaying the dashboard screen.
 */
public class NewUsersFragment extends Fragment {
    private NewUserViewModel newUserViewModel;
    private FragmentNewUserBinding binding;
    private SwipeRefreshLayout newUsersSwipeRefreshLayout;
    private RecyclerView newUserRecyclerView;
    private NewUsersRecyclerViewAdapter newUsersRecyclerViewAdapter;

    /**
     * Creates the view for the dashboard fragment.
     *
     * @param inflater           The layout inflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The saved instance state of the fragment, or null if this is a new instance.
     * @return The root view of the fragment's layout.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newUserViewModel = new ViewModelProvider(this, new NewUsersViewModelFactory(getActivity())).get(NewUserViewModel.class);


        binding = FragmentNewUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        newUsersSwipeRefreshLayout = binding.newUsersSwipeRefreshLayout;
        newUsersSwipeRefreshLayout.setOnRefreshListener(this::refreshData);

        newUsersRecyclerViewAdapter = new NewUsersRecyclerViewAdapter(newUserViewModel);

        newUserRecyclerView = binding.newUsersRecyclerView;
        newUserRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        newUserRecyclerView.setAdapter(newUsersRecyclerViewAdapter);
        newUserRecyclerView.addOnScrollListener(recyclerViewOnScrollListener());

        newUserViewModel.getUsers().observe(getViewLifecycleOwner(), this::updateUserList);

        fetchUsers();

        return root;
    }

    /**
     * Fetches the list of users from the ViewModel.
     */
    private void fetchUsers() {
        newUsersSwipeRefreshLayout.setRefreshing(true);
        newUserViewModel.fetchUsers();
    }

    /**
     * Updates the user list in the RecyclerView adapter.
     *
     * @param users The list of users to be displayed.
     */
    private void updateUserList(ArrayList<User> users) {
        Logger.d("DashboardFragment updateUserList, users: " + users.toString());

        newUsersRecyclerViewAdapter.setData(users);
        newUsersRecyclerViewAdapter.notifyDataSetChanged();
        newUsersSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Refreshes the data by clearing the user list and fetching new users.
     */
    private void refreshData() {
        newUserViewModel.clearUsers();
        fetchUsers();
    }

    /**
     * Returns an instance of RecyclerView.OnScrollListener for handling scroll events in the RecyclerView.
     *
     * @return The RecyclerView.OnScrollListener instance.
     */
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1)) {
                    Logger.d("DashboardFragment recyclerViewOnScrollListener onScrolled, !recyclerView.canScrollVertically");

                    fetchUsers();
                }
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}