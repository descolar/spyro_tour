package dam.pmdm.spyrothedragon.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Collectible;

public class CollectiblesAdapter extends RecyclerView.Adapter<CollectiblesAdapter.CollectiblesViewHolder> {

    private List<Collectible> list;
    private int gemsClicked = 0;
    private final VideoView videoView;
    private final FrameLayout fullscreenVideoView;

    public CollectiblesAdapter(List<Collectible> collectibleList, VideoView videoView, FrameLayout fullscreenVideoView) {
        this.list = collectibleList;
        this.videoView = videoView;
        this.fullscreenVideoView = fullscreenVideoView;
    }

    @NonNull
    @Override
    public CollectiblesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CollectiblesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectiblesViewHolder holder, int position) {
        Collectible collectible = list.get(position);
        holder.nameTextView.setText(collectible.getName());

        // Cargar la imagen
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(
                collectible.getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);

        // Solo activar Easter Egg si el coleccionable es "Gemas"
        if (collectible.getName().equals("Gemas")) {
            holder.itemView.setOnClickListener(view -> {
                gemsClicked++;

                if (gemsClicked == 4) { // Se activa tras 4 clics
                    gemsClicked = 0; // Reiniciamos el contador

                    // 📌 Verificamos que `videoView` y `fullscreenVideoView` no sean `null`
                    if (videoView == null || fullscreenVideoView == null) return;

                    // 📌 Mostrar el VideoView y cargar el video
                    String path = "android.resource://" + holder.itemView.getContext().getPackageName() + "/" + R.raw.video;
                    videoView.setVideoURI(Uri.parse(path));
                    fullscreenVideoView.setVisibility(View.VISIBLE);

                    // 📌 Configurar el comportamiento del VideoView
                    videoView.setOnPreparedListener(mp -> {
                        mp.start();
                        Toast.makeText(holder.itemView.getContext(), R.string.easter_egg_activated, Toast.LENGTH_SHORT).show();
                    });

                    // 📌 Cerrar el video si el usuario toca la pantalla
                    videoView.setOnClickListener(v -> {
                        videoView.stopPlayback();
                        fullscreenVideoView.setVisibility(View.GONE);
                    });

                    // 📌 Cerrar el video cuando termine
                    videoView.setOnCompletionListener(mp -> {
                        videoView.stopPlayback();
                        fullscreenVideoView.setVisibility(View.GONE);
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CollectiblesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CollectiblesViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }
}
