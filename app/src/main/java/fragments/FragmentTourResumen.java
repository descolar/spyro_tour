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

        // 🔹 Guardamos en SharedPreferences que el usuario completó el Tour
        SharedPreferences preferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("tourCompleted", true).apply();

        // 🔹 Quitamos cualquier fondo del tour antes de eliminar el fragmento
        View rootView = requireActivity().findViewById(android.R.id.content);
        rootView.setBackground(null); // ✅ Elimina el fondo residual

        // 🔹 Eliminamos este fragmento de manera segura
        getParentFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();

        // 🔹 Pequeño delay para asegurar que el fragmento se ha eliminado antes de navegar
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!isAdded() || getActivity() == null) return;

            // 🔹 Navegamos a la pestaña de personajes
            NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
            navController.navigate(R.id.navigation_characters);
        }, 300); // ⏳ Espera 300ms para evitar conflictos
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
