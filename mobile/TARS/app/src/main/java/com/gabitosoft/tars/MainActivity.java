package com.gabitosoft.tars;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import com.gabitosoft.model.Command;
import com.gabitosoft.net.HttpManager;


public class MainActivity extends ActionBarActivity {

    private HttpManager httpManager;
    private final String REST_SERVICE_URL = "http://192.168.150.1:3000/";

    //Speaking variables
    private final int REQ_CODE_SPEECH_INPUT = 100;

    // Fields
    private Button speakButton;
    private TextView textSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        speakButton = (Button)findViewById(R.id.buttonSpeak);
        textSpeak = (TextView) findViewById(R.id.textSpeak);

        httpManager = new HttpManager();

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
    }

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

    public void processCommand(String text) {

        try {

            String[] command = text.split(" ");

            if (httpManager.isNetworkAvailable(this) && command.length > 0) {

                Command c = buildCommand(command);
                httpManager.sendPost(REST_SERVICE_URL, c.toString());
            } else {

                Log.i("No network available", "processCommand");
            }
        } catch(Exception  ex) {

            Log.e("processCommand", ex.getMessage());
        }
    }

    public Command buildCommand(String[] textArray) {

        Command command = new Command();
        if (2 <= textArray.length ) {

            command.setAction(textArray[0]);
            command.setName(textArray[1]);
        } else {

            command.setAction(textArray[0]);
        }

        return command;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textSpeak.setText(result.get(0));
                    processCommand(result.get(0));
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
