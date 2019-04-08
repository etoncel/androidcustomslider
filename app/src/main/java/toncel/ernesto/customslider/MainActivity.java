package toncel.ernesto.customslider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CustomSlider.OnSliderMove {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomSlider slider = findViewById(R.id.CustomSlider);
        slider.addOnSliderMove(this);
        slider.setMaxValue(50);
        slider.setInitialValue(30);

    }

    @Override
    public void onSliderMove(int value) {
        TextView textView = findViewById(R.id.textView);
        textView.setText("valor: " + value);
    }
}
