package dam.pmdm.spyrothedragon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * La clase FireAnimationView es una vista personalizada que dibuja una animación de fuego.
 * Utilizamos un objeto Canvas para dibujar el efecto y un Handler para gestionar la animación.
 */
public class FireAnimationView extends View {

    // Definimos el pincel que usaremos para pintar la llama
    private final Paint flamePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // Creamos un Path que nos permite definir la forma del fuego
    private final Path flamePath = new Path();

    // Usamos un Handler para actualizar la animación en un intervalo de tiempo
    private final Handler handler = new Handler(Looper.getMainLooper());

    // Variable de control para saber si la animación está activa
    private boolean isAnimating = false;

    // Gradiente lineal para dar un efecto de color más realista al fuego
    private LinearGradient gradient;

    /**
     * Constructor de la vista de animación de fuego.
     * @param context Contexto de la aplicación necesario para inicializar la vista.
     */
    public FireAnimationView(Context context) {
        super(context);
        setWillNotDraw(false); // Forzamos que la vista se redibuje cuando sea necesario
    }

    /**
     * Método encargado de dibujar la animación en la pantalla.
     * Se ejecuta automáticamente cada vez que se llama a invalidate().
     * @param canvas Lienzo en el que se dibuja la animación.
     */
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // Obtenemos el ancho y el alto de la vista
        int width = getWidth();
        int height = getHeight();

        // Si la vista no tiene dimensiones válidas, salimos para evitar errores
        if (width == 0 || height == 0) return;

        // Aplicamos el gradiente al pincel
        flamePaint.setShader(gradient);

        // Reiniciamos el Path para dibujar una nueva llama
        flamePath.reset();
        flamePath.moveTo(width / 2f, height); // Punto de inicio en la parte inferior

        // Dibujamos la curva del fuego con valores aleatorios para hacerlo dinámico
        flamePath.quadTo(width * 0.2f, height * 0.5f + (float) Math.random() * 20,
                width / 2f, height * 0.1f + (float) Math.random() * 30);
        flamePath.quadTo(width * 0.8f, height * 0.5f + (float) Math.random() * 20,
                width / 2f, height);

        // Dibujamos el fuego en el lienzo con el Path definido
        canvas.drawPath(flamePath, flamePaint);
    }

    /**
     * Inicia la animación del fuego.
     */
    public void startAnimation() {
        if (isAnimating) return; // Si ya está en marcha, no hacemos nada
        isAnimating = true;
        handler.post(animationRunnable); // Iniciamos la animación
    }

    /**
     * Detiene la animación del fuego.
     */
    public void stopAnimation() {
        isAnimating = false;
        handler.removeCallbacks(animationRunnable); // Eliminamos las llamadas pendientes
    }

    /**
     * Método que se ejecuta cuando la vista cambia de tamaño.
     * Aquí generamos un nuevo gradiente con los colores del fuego.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Creamos un gradiente que va del rojo al amarillo con una transición transparente
        gradient = new LinearGradient(0, h, 0, 0,
                new int[]{Color.RED, Color.YELLOW, Color.TRANSPARENT},
                new float[]{0.2f, 0.6f, 1f},
                Shader.TileMode.CLAMP);
    }

    /**
     * Runnable que actualiza la animación de manera periódica.
     * Se ejecuta cada 100 milisegundos para dar un efecto de movimiento fluido.
     */
    private final Runnable animationRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isAnimating) return; // Si la animación ha sido detenida, no hacemos nada
            invalidate(); // Forzamos que se vuelva a dibujar la vista
            handler.postDelayed(this, 100); // Volvemos a llamar al Runnable en 100ms
        }
    };
}
