package status.four.saves.boost.guaranteed.presentation.dash.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import status.four.saves.boost.guaranteed.databinding.FragmentDashboardBinding;
import status.four.saves.boost.guaranteed.domain.user.User;
import status.four.saves.boost.guaranteed.shared.Logger;


public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private RecyclerView newUserRecyclerView;
    private NewUsersRecyclerViewAdapter newUsersRecyclerViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        newUsersRecyclerViewAdapter = new NewUsersRecyclerViewAdapter();

        newUserRecyclerView = binding.newUsersRecyclerView;
        newUserRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        newUserRecyclerView.setAdapter(newUsersRecyclerViewAdapter);
        newUserRecyclerView.addOnScrollListener(recyclerViewOnScrollListener());

        dashboardViewModel.getUsers().observe(
                getViewLifecycleOwner(),
                new Observer<ArrayList<User>>() {
                    @Override
                    public void onChanged(ArrayList<User> users) {
                        newUsersRecyclerViewAdapter.setData(users);
                        newUsersRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
        );

        dashboardViewModel.getNewUsers();

        return root;
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1)) {
                    Logger.d("DashboardFragment recyclerViewOnScrollListener onScrolled, !recyclerView.canScrollVertically");
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
