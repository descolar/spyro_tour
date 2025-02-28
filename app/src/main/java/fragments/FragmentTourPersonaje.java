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
/**
 * Este fragmento gestiona la primera parte del tutorial guiado.
 * Se encarga de mostrar animaciones y ayudar al usuario en los primeros pasos del juego.
 */
public class FragmentTourPersonaje extends Fragment {

    private FragmentTourPersonajeBinding binding;
    private int currentStep = 1; // 🔹 Controla en qué paso estamos

    /**
     * Inflamos la vista del fragmento utilizando View Binding.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTourPersonajeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    /**
     * Método que se ejecuta cuando la vista ha sido creada.
     * Configuramos los botones y verificamos si el tutorial ya fue completado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recuperamos las preferencias para comprobar si el tutorial ya ha sido completado
        SharedPreferences preferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        boolean tourCompleted = preferences.getBoolean("tourCompleted", false);

        // Si el tutorial ya fue completado, eliminamos el fragmento y salimos
        if (tourCompleted) {
            getParentFragmentManager().beginTransaction().remove(this).commit();
            return;
        }

        // Esperamos a que la UI termine de renderizarse antes de mostrar la mano
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            binding.handPointer.bringToFront();
            binding.handPointer.setTranslationZ(10f);  // Mayor prioridad en la jerarquía de vistas
            binding.handPointer.setVisibility(View.VISIBLE);
        }, 200);

        // Iniciamos el tutorial guiado desde la sección de personajes
        view.post(() -> moverDedoDesdeCentro(R.id.nav_characters));
        // Configuramos la funcionalidad de los botones
        binding.btnSeguir.setOnClickListener(v -> {
            playFireSound();
            avanzarTutorial();
        });

        // Configuramos el botón de salida para reiniciar el tour al salir
        binding.btnSalir.setOnClickListener(v -> {
            cerrarTutorial();
            preferences.edit().putBoolean("tourCompleted", false).apply(); //Restablecemos el tour para que se muestre la próxima vez
        });
    }
    /**
     * Avanza al siguiente etapa del tutorial.
     * Se aplican animaciones para hacer la transición más fluida.
     */
    private void avanzarTutorial() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);

        // Aplicamos una animación de desvanecimiento antes de cambiar de wtapa
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(binding.tutorialBox, "alpha", 1f, 0f);
        fadeOut.setDuration(300);

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Cambiamos de etapa y navegamos al fragmento correspondiente
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

                // Aplicamos la animación de entrada del nuevo contenido
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(binding.tutorialBox, "alpha", 0f, 1f);
                fadeIn.setDuration(300);
                fadeIn.start();
            }
        });

        fadeOut.start();
    }

    /**
     * Muestra el fragmento de resumen cuando el tutorial ha finalizado.
     */
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

        // ✅ Retraso antes de iniciar la transición para que la animación sea visible
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FragmentTourResumen tourResumenFragment = new FragmentTourResumen();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)  // ✅ Aplicar animaciones
                    .replace(android.R.id.content, tourResumenFragment)
                    .addToBackStack(null)  // ✅ Asegurar que se puede volver atrás con animación
                    .commitAllowingStateLoss();  // ✅ Evita que la animación se interrumpa
        }, 300);
    }

    /**
     * Reinicia el tutorial cuando cambiamos de paso.
     */

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
    /**
     * Mueve la dedo desde el centro de la pantalla hasta el destino indicado.
     */
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
    /**
     * Ejecuta la animación de movimiento de la mano.
     */
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
    /**
     * Ejecuta la animación de movimiento de la mano.
     */
    private void mostrarBotonesConAnimacion() {

        binding.btnSeguir.setVisibility(View.VISIBLE);
        binding.btnSalir.setVisibility(View.VISIBLE);

        ObjectAnimator animSeguir = ObjectAnimator.ofFloat(binding.btnSeguir, "alpha", 0f, 1f);
        ObjectAnimator animSalir = ObjectAnimator.ofFloat(binding.btnSalir, "alpha", 0f, 1f);

        animSeguir.setDuration(1000);
        animSalir.setDuration(1000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animSeguir, animSalir);


        animatorSet.start();
    }
    /**
     * Reproduce el sonido del fuego cuando se interactúa con el tutorial.
     */
    private void playFireSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.fire);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release); // Liberar memoria al terminar
        mediaPlayer.start();
    }

    /**
     * Cierra el tutorial y guarda en las preferencias que ya ha sido completado.
     */
    private void cerrarTutorial() {
        playFireSound();
        SharedPreferences preferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("tourCompleted", true).apply(); // ✅ Guardamos que el usuario ya completó la guía
        getParentFragmentManager().beginTransaction().remove(this).commit();

        // ✅ Habilitar botones del `BottomNavigationView`
        BottomNavigationView bottomNavView = requireActivity().findViewById(R.id.navView);
        if (bottomNavView != null) {
            bottomNavView.setEnabled(true);
            for (int i = 0; i < bottomNavView.getMenu().size(); i++) {
                bottomNavView.getMenu().getItem(i).setEnabled(true);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
