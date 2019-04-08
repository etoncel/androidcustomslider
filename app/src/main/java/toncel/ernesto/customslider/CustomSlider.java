package toncel.ernesto.customslider;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

/*
* Importante: La imagen del recurso del thumb debe llamarse thumb_slider_ic
* Y el id del icono del thumb debe ser thumbImage
* 2. Deben llamarse a los métodos de posición inicial y valor máximo
* */

public class CustomSlider extends FrameLayout {

    interface OnSliderMove{
        void onSliderMove(int value);
    }

    private OnSliderMove mListener;

    private float mStartPosY;

    private int MAX_VALUE = 100;

    private boolean isMoving = false;



    public CustomSlider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Colocar el thumb según el valor de la posición actual

        //Drawable drawable = getResources().getDrawable(R.drawable.thumb_slider_ic);
        //int drawableHeight = drawable.getIntrinsicHeight();

        //Calculo de la posición debe ser el porcentaje del valor inicial en el valor total del slider
        // y su equivalente a la altura del slider
        // Ejemplo: 10 / 50 = 0.2
        // h = 360 * 0.2 = 72
        // mStartPosY = 72
        double value = mStartPosY / MAX_VALUE;
        mStartPosY = (float) (h * (1 - value));
//        mStartPosY = h - ((float) (h * value));

        ImageView thumb = findViewById(R.id.thumbImage);
        thumb.setTranslationY(mStartPosY);
    }

    public void setInitialValue(int pos){
        //Valor inicial del grosor
        mStartPosY = pos;
    }

    public void setMaxValue(int maxValue) {
        this.MAX_VALUE = maxValue;
    }

    public void addOnSliderMove(OnSliderMove mListener){
        this.mListener = mListener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        ImageView thumb = findViewById(R.id.thumbImage);

        Log.d("Ernesto","se ejecuta touchevent");

        /*
            1. Se obtiene la posición del view padre en la pantalla para tomarla como referencia
            y luego la posición del view en pantalla.
            2. Se calcula entonces la posición del view con respecto al padre
        */

        float y = event.getY();
        int[] location = new int[2];
        thumb.getLocationInWindow(location);

        int[] parentLocation = new int[2];
        this.getLocationInWindow(parentLocation);

        int thumbPos = location[1] - parentLocation[1];

        Log.d("Ernesto", "pos: " + thumbPos);

        int relativeTop = thumbPos;

        Rect rect = new Rect();
        thumb.getDrawingRect(rect);
        int relativeBottom = relativeTop + rect.bottom;


            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    //Guarda la posición inicial del thumb

                    // Comprobar que el toque del usuario es dentro del view
                    if (y >= relativeTop && y <= relativeBottom) {
                        mStartPosY = y;
                        isMoving = true;
                    }else {
                        isMoving = false;
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    // Si se está tocando la pantalla entonces calcular la diferencia de la posición actual con la nueva posición donde
                    // el usuario está arrastrando

                    if (isMoving) {
                        float dy = y - mStartPosY;

                        // Luego calcular la nueva posición y arrastrar hacia ese punto
                        float newPos = thumbPos + dy;
                        // Calcular si el próximo movimiento se sale de los límites del view padre
                        boolean maxBound = newPos + rect.bottom > this.getHeight();
                        boolean minBound = newPos <  0;

                        if(!maxBound && !minBound ){
                            thumb.setTranslationY(thumbPos + dy);
                            // Guardar la última posición donde terminó el arrastre sin dejar de tocar la pantalla
                            mStartPosY = thumbPos + dy;
                            int h = this.getHeight();
                            int value = (int) (MAX_VALUE *  (mStartPosY / this.getHeight()));
                            mListener.onSliderMove(MAX_VALUE - value);
                        }
                    }
                    break;
            }
            invalidate();

        return true;
    }


}
