package gabitosoft.com.tars;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.HttpManager;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    private HttpManager httpManager;
    private final String REST_SERVICE_URL = "http://www.gabitosoft.com/gpstracker/public/position";


    private long last_update = 0, last_movement = 0;
    private float prevX = 0, prevY = 0, prevZ = 0;
    private float curX = 0, curY = 0, curZ = 0;

    //Speaking variables
    private final int REQ_CODE_SPEECH_INPUT = 100;

    // Fields
    private Button speakButton;
    private TextView txtAccX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Context context = this;
        speakButton = (Button)findViewById(R.id.speakButton);
        txtAccX = (TextView) findViewById(R.id.txtAccX);

        speakButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            promptSpeechInput();
                        } catch (Exception ex) {

                            ex.printStackTrace();
                            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        httpManager = new HttpManager();

    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtAccX.setText(result.get(0));
                }
                break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
//        synchronized(this) {
//
//            long current_time = event.timestamp;
//
//            curX = event.values[0];
//            curY = event.values[1];
//            curZ = event.values[2];
//
//            if (prevX == 0 && prevY == 0 && prevZ == 0) {
//                last_update = current_time;
//                last_movement = current_time;
//                prevX = curX;
//                prevY = curY;
//                prevZ = curZ;
//            }
//
//            long time_difference = current_time - last_update;
//            if (time_difference > 0) {
//                float movement = Math.abs((curX + curY + curZ) - (prevX - prevY - prevZ)) / time_difference;
//
//                int limit = 1500;
//                float min_movement = 1E-6f;
//                if (movement > min_movement) {
//                    if (current_time - last_movement >= limit) {
//                        //Toast.makeText(getApplicationContext(), "Hay movimiento de " + movement, Toast.LENGTH_SHORT).show();
//                    }
//                    last_movement = current_time;
//                }
//
//                prevX = curX;
//                prevY = curY;
//                prevZ = curZ;
//                last_update = current_time;
//            }
//
//            ((TextView) findViewById(R.id.txtAccX)).setText("X: " + curX);
//            ((TextView) findViewById(R.id.txtAccY)).setText("Y: " + curY);
//            ((TextView) findViewById(R.id.txtAccZ)).setText("Z: " + curZ);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        //List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        //if (sensors.size() > 0) {
        //    sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        //}
    }

    @Override
    protected void onStop() {
        //SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        //sm.unregisterListener(this);
        super.onStop();
    }
}
