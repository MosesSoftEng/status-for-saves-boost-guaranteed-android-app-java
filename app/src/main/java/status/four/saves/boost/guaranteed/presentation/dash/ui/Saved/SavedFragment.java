package status.four.saves.boost.guaranteed.presentation.dash.ui.Saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import status.four.saves.boost.guaranteed.databinding.FragmentSavedBinding;

public class SavedFragment extends Fragment {
    SavedViewModel savedViewModel;

    private FragmentSavedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        savedViewModel = new ViewModelProvider(this, new SavedViewModelFactory(getActivity())).get(SavedViewModel.class);

        binding = FragmentSavedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        savedViewModel.fetchContacts();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}