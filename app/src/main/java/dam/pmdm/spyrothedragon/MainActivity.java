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

    public static void playSound(Context context, int soundId) {
        // Reproducimos un sonido en la aplicación
        MediaPlayer mediaPlayer = MediaPlayer.create(context, soundId);
        mediaPlayer.start();
    }


    public ActivityMainBinding getBinding() {
        return binding;     // Devuelve el binding
    }

    public void playEasterEggVideo() {
        // Validamos que el VideoView y el contenedor de pantalla completa existen antes de continuar
        if (videoView == null || fullscreenVideoView == null) return;

        // Cargamos el video desde la carpeta raw
        String path = "android.resource://" + getPackageName() + "/" + R.raw.video;
        videoView.setVideoURI(Uri.parse(path));

        // Mostramos el video en pantalla completa
        fullscreenVideoView.setVisibility(VideoView.VISIBLE);

        // Reproducimos el video cuando esté listo
        videoView.setOnPreparedListener(mp -> {
            mp.start();
        });

        // Si el usuario toca la pantalla, detenemos el video y ocultamos la vista
        videoView.setOnClickListener(v -> {
            videoView.stopPlayback();
            fullscreenVideoView.setVisibility(VideoView.GONE);
        });

        // Al finalizar el video, detenemos la reproducción y ocultamos la vista
        videoView.setOnCompletionListener(mp -> {
            videoView.stopPlayback();
            fullscreenVideoView.setVisibility(VideoView.GONE);
        });
    }
}
