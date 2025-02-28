package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.databinding.FragmentTourBinding;

public class TourFragment extends Fragment {

    private FragmentTourBinding binding;
    private SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNavView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTourBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        bottomNavView = requireActivity().findViewById(R.id.navView);

        // ✅ Deshabilitar los botones pero dejarlos visibles
        if (bottomNavView != null) {
            bottomNavView.setEnabled(false);
            for (int i = 0; i < bottomNavView.getMenu().size(); i++) {
                bottomNavView.getMenu().getItem(i).setEnabled(false);
            }
        }

        binding.btnTourStart.setOnClickListener(v -> {
            sharedPreferences.edit().putBoolean("tourCompleted", true).apply();

            // ✅ Primero navegamos a `CharactersFragment`
            NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
            navController.navigate(R.id.navigation_characters);

            // ✅ Luego iniciamos `FragmentTourPersonaje`
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                FragmentTourPersonaje tourFragment = new FragmentTourPersonaje();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, tourFragment) // 🔹 Se superpone sobre `CharactersFragment`
                        .addToBackStack(null)
                        .commit();
            }, 500);
        });
    }






    private void bloquearMenu(boolean bloquear) {
        if (bottomNavView != null) {
            bottomNavView.setEnabled(!bloquear);
            bottomNavView.setClickable(!bloquear);
        }
    }
}
