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
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.databinding.FragmentTourResumenBinding;

/**
 * Este fragmento muestra el resumen final del tutorial.
 * Permite finalizar el tour y restaurar la navegación normal en la aplicación.
 */
public class FragmentTourResumen extends Fragment {

    private FragmentTourResumenBinding binding;

    /**
     * Inflamos la vista del fragmento utilizando View Binding.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTourResumenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Método que se ejecuta cuando la vista ha sido creada.
     * Configuramos el botón para finalizar el tour.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configuramos el botón para que al hacer clic se finalice el tour
        binding.btnFinalizarTour.setOnClickListener(v -> finalizarTour());
    }

    /**
     * Método para finalizar el tour y restaurar la navegación de la aplicación.
     */
    private void finalizarTour() {
        if (!isAdded() || getActivity() == null) return;

        // Guardamos en `SharedPreferences` que el usuario ha completado el Tour
        SharedPreferences preferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("tourCompleted", true).apply();

        // Eliminamos la vista del fondo del tour si aún está visible
        View fondoTour = requireActivity().findViewById(R.id.fondoTour);
        if (fondoTour != null) {
            fondoTour.setVisibility(View.GONE);
        }

        // Habilitamos nuevamente los botones del menú de navegación inferior
        BottomNavigationView bottomNavView = requireActivity().findViewById(R.id.navView);
        if (bottomNavView != null) {
            bottomNavView.setEnabled(true);
            for (int i = 0; i < bottomNavView.getMenu().size(); i++) {
                bottomNavView.getMenu().getItem(i).setEnabled(true);
            }
        }

        // Aplicamos una pequeña espera antes de cambiar de fragmento para evitar cortes bruscos
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!isAdded() || getActivity() == null) return;

            // Obtenemos el controlador de navegación
            NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);

            // Aplicamos una animación en la transición al fragmento de personajes
            navController.navigate(R.id.navigation_characters, null,
                    new NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_left)  // Animación de entrada
                            .setExitAnim(R.anim.slide_out_right)  // Animación de salida
                            .build());

            // Eliminamos este fragmento después de completar la navegación
            getParentFragmentManager().beginTransaction()
                    .remove(this)
                    .commitAllowingStateLoss();
        }, 300);
    }

    /**
     * Liberamos el binding para evitar pérdidas de memoria al destruir la vista.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
