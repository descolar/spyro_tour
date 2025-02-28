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

        // 📌 Bloquear el menú de navegación mientras el tour está activo
        bloquearMenu(true);

        binding.btnTourStart.setOnClickListener(v -> {
            // Guardamos que la guía ya fue vista
            sharedPreferences.edit().putBoolean("tourCompleted", true).apply();

            // 📌 Habilitar el menú de navegación antes de la transición
            bloquearMenu(false);

            // Navegar a la pantalla de personajes
            NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
            navController.navigate(R.id.action_tourFragment_to_navigation_characters);

            // Mostrar `FragmentTourPersonaje` encima después de la navegación
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                FragmentTourPersonaje tourFragment = new FragmentTourPersonaje();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, tourFragment) // Se superpone sobre toda la pantalla
                        .commit();
            }, 500); // Pequeño retraso para asegurarnos de que la navegación se complete antes
        });
    }

    private void bloquearMenu(boolean bloquear) {
        if (bottomNavView != null) {
            bottomNavView.setEnabled(!bloquear);
            bottomNavView.setClickable(!bloquear);
        }
    }
}
