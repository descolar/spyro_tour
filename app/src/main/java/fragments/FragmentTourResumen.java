package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.databinding.FragmentTourResumenBinding;

public class FragmentTourResumen extends Fragment {

    private FragmentTourResumenBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTourResumenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnFinalizarTour.setOnClickListener(v -> finalizarTour());
    }
    /*
    private void finalizarTour() {
        if (!isAdded() || getActivity() == null) return;

        // Guardamos en `SharedPreferences` que el usuario completó el Tour
        SharedPreferences preferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("tourCompleted", true).apply();
        boolean tourCompleted = preferences.getBoolean("tourCompleted", false);
       // Log.d("TourDebug", "¿El tour ya fue completado?: " + tourCompleted);
        // Eliminamos este fragmento
        getParentFragmentManager().beginTransaction().remove(this).commit();


    }
   */
    private void finalizarTour() {
        if (!isAdded() || getActivity() == null) return;

        // ✅ Guardamos en SharedPreferences que el usuario completó el Tour
        SharedPreferences preferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("tourCompleted", true).apply();

        // ✅ Asegurar que `fondoTour` desaparece antes de cerrar
        View fondoTour = requireActivity().findViewById(R.id.fondoTour);
        if (fondoTour != null) {
            fondoTour.setVisibility(View.GONE);
        }

        // ✅ Habilitar botones del `BottomNavigationView`
        BottomNavigationView bottomNavView = requireActivity().findViewById(R.id.navView);
        if (bottomNavView != null) {
            bottomNavView.setEnabled(true);
            for (int i = 0; i < bottomNavView.getMenu().size(); i++) {
                bottomNavView.getMenu().getItem(i).setEnabled(true);
            }
        }

        // ✅ Pequeño retraso antes de navegar para evitar interferencias
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!isAdded() || getActivity() == null) return;

            // ✅ Navegar a `CharactersFragment`
            NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
            navController.navigate(R.id.navigation_characters);

            // ✅ Eliminar `FragmentTourResumen` después de navegar
            getParentFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }, 300);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
