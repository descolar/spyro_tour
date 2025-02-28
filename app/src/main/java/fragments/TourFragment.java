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

/**
 * Este fragmento se encarga de iniciar el tour guiado.
 * Permite deshabilitar la navegación hasta que el usuario comience el tour
 * y luego lo redirige a la sección de personajes con las instrucciones iniciales.
 */
public class TourFragment extends Fragment {

    private FragmentTourBinding binding;
    private SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNavView;

    /**
     * Inflamos la vista del fragmento utilizando View Binding.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTourBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Método que se ejecuta cuando la vista ha sido creada.
     * Configuramos la funcionalidad de inicio del tour y bloqueamos la navegación.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtenemos la referencia a las preferencias del usuario para verificar si el tour ya ha sido completado
        sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        bottomNavView = requireActivity().findViewById(R.id.navView);

        // Deshabilitamos los botones de navegación mientras el usuario esté en el tour
        if (bottomNavView != null) {
            bottomNavView.setEnabled(false);
            for (int i = 0; i < bottomNavView.getMenu().size(); i++) {
                bottomNavView.getMenu().getItem(i).setEnabled(false);
            }
        }

        // Configuramos el botón de inicio del tour
        binding.btnTourStart.setOnClickListener(v -> {
            // Guardamos en SharedPreferences que el usuario ha iniciado el tour
            sharedPreferences.edit().putBoolean("tourCompleted", true).apply();

            // Primero navegamos a `CharactersFragment`
            NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
            navController.navigate(R.id.navigation_characters);

            // Luego iniciamos `FragmentTourPersonaje`
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                FragmentTourPersonaje tourFragment = new FragmentTourPersonaje();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, tourFragment)
                        .addToBackStack(null)
                        .commit();
            }, 16); // Equivalente a un frame en 60Hz para evitar parpadeos sin retrasos perceptibles

        });
    }

    /**
     * Método que permite bloquear o desbloquear el menú de navegación.
     * @param bloquear `true` para deshabilitar el menú, `false` para habilitarlo.
     */
    private void bloquearMenu(boolean bloquear) {
        if (bottomNavView != null) {
            bottomNavView.setEnabled(!bloquear);
            bottomNavView.setClickable(!bloquear);
        }
    }
}
