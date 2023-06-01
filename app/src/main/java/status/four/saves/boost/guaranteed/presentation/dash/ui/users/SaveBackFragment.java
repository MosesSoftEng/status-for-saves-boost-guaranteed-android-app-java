package status.four.saves.boost.guaranteed.presentation.dash.ui.users;

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

import status.four.saves.boost.guaranteed.databinding.FragmentHomeBinding;
import status.four.saves.boost.guaranteed.domain.user.User;
import status.four.saves.boost.guaranteed.presentation.dash.ui.dashboard.NewUsersRecyclerViewAdapter;
import status.four.saves.boost.guaranteed.shared.Logger;

public class SaveBackFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SaveBackViewModel saveBackViewModel =
                new ViewModelProvider(this).get(SaveBackViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}