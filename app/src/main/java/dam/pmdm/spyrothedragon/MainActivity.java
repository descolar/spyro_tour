package dam.pmdm.spyrothedragon;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private VideoView videoView;
    private ConstraintLayout fullscreenVideoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflamos el layout usando ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializamos referencias a los elementos del video
        videoView = findViewById(R.id.videoView);
        fullscreenVideoView = findViewById(R.id.fullscreen_video_view);

        // Configuramos la navegación
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.navView, navController);
            NavigationUI.setupActionBarWithNavController(this, navController);
        }

        // Configuramos los eventos del menú inferior
        binding.navView.setOnItemSelectedListener(this::selectedBottomMenu);

        // Ajustamos la visibilidad del botón de retroceso según la pantalla actual
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_characters ||
                    destination.getId() == R.id.navigation_worlds ||
                    destination.getId() == R.id.navigation_collectibles) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });
    }

    private boolean selectedBottomMenu(@NonNull MenuItem menuItem) {
        // Manejamos la navegación según la opción seleccionada en el menú inferior
        if (menuItem.getItemId() == R.id.nav_characters)
            navController.navigate(R.id.navigation_characters);
        else if (menuItem.getItemId() == R.id.nav_worlds)
            navController.navigate(R.id.navigation_worlds);
        else
            navController.navigate(R.id.navigation_collectibles);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú con las opciones de la aplicación
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejamos la selección de opciones en el menú superior
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        // Mostramos un cuadro de diálogo con información sobre la aplicación
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_about)
                .setMessage(R.string.text_about)
                .setPositiveButton(R.string.accept, null)
                .show();
    }
    /**
     * Método para reproducir un sonido en la aplicación.
     * Recibe el contexto y el identificador del sonido a reproducir.
     */
    public static void playSound(Context context, int soundId) {
        // Creamos un objeto MediaPlayer con el recurso de sonido indicado
        MediaPlayer mediaPlayer = MediaPlayer.create(context, soundId);
        // Iniciamos la reproducción del sonido
        mediaPlayer.start();
    }

    /**
     * Método para obtener el objeto de binding de la actividad principal.
     * Esto nos permite acceder a los elementos de la interfaz sin necesidad de findViewById.
     * @return Retorna el objeto ActivityMainBinding que contiene la referencia a los elementos de la UI.
     */
    public ActivityMainBinding getBinding() {
        return binding;
    }

    /**
     * Método para reproducir un video oculto dentro de la aplicación (Easter Egg).
     * Se encarga de cargar el video desde los recursos y mostrarlo en pantalla completa.
     */
    public void playEasterEggVideo() {
        // Verificamos que el VideoView y el contenedor de pantalla completa no sean nulos antes de continuar
        if (videoView == null || fullscreenVideoView == null) return;

        // Construimos la ruta del video ubicado en la carpeta raw de los recursos
        String path = "android.resource://" + getPackageName() + "/" + R.raw.video;
        videoView.setVideoURI(Uri.parse(path));

        // Hacemos visible el contenedor para mostrar el video en pantalla completa
        fullscreenVideoView.setVisibility(VideoView.VISIBLE);

        // Configuramos un listener para iniciar la reproducción cuando el video esté listo
        videoView.setOnPreparedListener(mp -> mp.start());

        // Si el usuario toca la pantalla, detenemos la reproducción y ocultamos el video
        videoView.setOnClickListener(v -> {
            videoView.stopPlayback();
            fullscreenVideoView.setVisibility(VideoView.GONE);
        });

        // Cuando el video termine, detenemos la reproducción y ocultamos la vista
        videoView.setOnCompletionListener(mp -> {
            videoView.stopPlayback();
            fullscreenVideoView.setVisibility(VideoView.GONE);
        });
    }

}
