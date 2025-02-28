package fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
import dam.pmdm.spyrothedragon.databinding.FragmentTourPersonajeBinding;

public class FragmentTourPersonaje extends Fragment {

    private FragmentTourPersonajeBinding binding;
    private int currentStep = 1; // 🔹 Controla en qué paso estamos

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTourPersonajeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        boolean tourCompleted = preferences.getBoolean("tourCompleted", false);

        Log.d("TourDebug", "¿El tour ya fue completado?: " + tourCompleted);
        if (tourCompleted) {
            //  Si el tour ya fue completado, cerramos el fragmento y no lo mostramos
            getParentFragmentManager().beginTransaction().remove(this).commit();
            return;
        }

        //  Si el tour no ha sido completado, iniciamos el tutorial
        view.post(() -> moverDedoDesdeCentro(R.id.nav_characters));

        binding.btnSeguir.setOnClickListener(v -> {
            playFireSound();
            avanzarTutorial();
        });

        binding.btnSalir.setOnClickListener(v -> cerrarTutorial());
    }

    private void avanzarTutorial() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);

        if (currentStep == 1) {
            currentStep = 2;
            navController.navigate(R.id.navigation_worlds);
            reiniciarTutorial(getString(R.string.text_tour_world), R.id.nav_worlds);
        } else if (currentStep == 2) {
            currentStep = 3;
            navController.navigate(R.id.navigation_collectibles);
            reiniciarTutorial(getString(R.string.text_tour_collect), R.id.nav_collectibles);
        } else if (currentStep == 3) {
            currentStep = 4;
            mostrarResumen();
        }
    }


    private void mostrarResumen() {
        // ✅ Asegurar que el fondo desaparezca ANTES de navegar al resumen
        View fondoTour = requireActivity().findViewById(R.id.fondoTour);
        if (fondoTour != null) {
            fondoTour.setVisibility(View.GONE);
        }

        // ✅ Ocultar elementos de `FragmentTourPersonaje`
        binding.tutorialBox.setVisibility(View.GONE);
        binding.handPointer.setVisibility(View.GONE);
        binding.btnSeguir.setVisibility(View.GONE);
        binding.btnSalir.setVisibility(View.GONE);

        // ✅ Pequeño delay antes de mostrar `FragmentTourResumen` para evitar parpadeos
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FragmentTourResumen tourResumenFragment = new FragmentTourResumen();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, tourResumenFragment)  // ✅ Reemplazar en la pantalla completa
                    .commit();
        }, 300);
    }




    private void reiniciarTutorial(String nuevoTexto, int nuevoDestino) {
        // 🔹 Ocultar tutorial y resetear posición del dedo
        binding.tutorialBox.setVisibility(View.GONE);
        binding.handPointer.setVisibility(View.INVISIBLE);
        binding.btnSeguir.setVisibility(View.GONE);
        binding.btnSalir.setVisibility(View.GONE);

        // 📌 Esperar antes de iniciar la siguiente animación
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            binding.tutorialText.setText(nuevoTexto);
            moverDedoDesdeCentro(nuevoDestino);
        }, 1000);
    }

    private void moverDedoDesdeCentro(int destinoId) {
        // 📌 Iniciar movimiento desde el centro de la pantalla
        float centroX = binding.getRoot().getWidth() / 2f;
        float centroY = binding.getRoot().getHeight() / 2f;

        // 📌 Obtener coordenadas del destino
        BottomNavigationView bottomNavView = requireActivity().findViewById(R.id.navView);
        View menuItemView = bottomNavView.findViewById(destinoId);

        int[] posicionIcono = new int[2];
        menuItemView.getLocationOnScreen(posicionIcono);

        float destinoX = posicionIcono[0] + (menuItemView.getWidth() / 2f);
        float destinoY = posicionIcono[1] - (menuItemView.getHeight() / 2f) - 200; // Ajuste en Y

        iniciarAnimacion(centroX, centroY, destinoX, destinoY);
    }

    private void iniciarAnimacion(float inicioX, float inicioY, float destinoX, float destinoY) {
        binding.handPointer.setVisibility(View.VISIBLE);
        binding.handPointer.setX(inicioX);
        binding.handPointer.setY(inicioY);

        ObjectAnimator animX = ObjectAnimator.ofFloat(binding.handPointer, "x", destinoX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(binding.handPointer, "y", destinoY);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animX, animY);
        animatorSet.setDuration(1500);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.handPointer.setX(destinoX);
                binding.handPointer.setY(destinoY);
                binding.tutorialBox.setVisibility(View.VISIBLE);

                new Handler(Looper.getMainLooper()).postDelayed(() -> mostrarBotonesConAnimacion(), 500);
            }
        });

        animatorSet.start();
    }

    private void mostrarBotonesConAnimacion() {
        if (currentStep == 3) {
            binding.btnSeguir.setVisibility(View.VISIBLE); // Se mantiene el botón de Seguir
            binding.btnSalir.setVisibility(View.VISIBLE);
        } else {
            binding.btnSeguir.setVisibility(View.VISIBLE);
            binding.btnSalir.setVisibility(View.VISIBLE);
        }

        ObjectAnimator animSeguir = ObjectAnimator.ofFloat(binding.btnSeguir, "alpha", 0f, 1f);
        ObjectAnimator animSalir = ObjectAnimator.ofFloat(binding.btnSalir, "alpha", 0f, 1f);

        animSeguir.setDuration(1000);
        animSalir.setDuration(1000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animSeguir, animSalir);
     /*   if (currentStep == 3) {
           // animatorSet.play(animSeguir); // Solo animar "Seguir" en el último paso
            animatorSet.playTogether(animSeguir, animSalir);
        } else {
            animatorSet.playTogether(animSeguir, animSalir);
        }
*/

        animatorSet.start();
    }

    private void playFireSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.fire);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release); // Liberar memoria al terminar
        mediaPlayer.start();
    }


    private void cerrarTutorial() {
        playFireSound();
        SharedPreferences preferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("tourCompleted", true).apply(); // ✅ Guardamos que el usuario ya completó la guía
        getParentFragmentManager().beginTransaction().remove(this).commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
