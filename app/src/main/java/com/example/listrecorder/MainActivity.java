package com.example.listrecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ListActivity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText e1;
    Button b1, b2;
    ListView l1;
    boolean counter = false;
    int id = 0;
    final int REQUEST_CODE = 1000;
    String pathSave;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList<String> li = new ArrayList<String>();
        final ArrayList<String> ai = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, li);
        e1 = findViewById(R.id.editText);
        b1 = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
        l1 = findViewById(R.id.listView);
        l1.setAdapter(adapter);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                li.add(e1.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });
        if (checkPermissionFromDevice()) {
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(counter == false) {
                        counter = true;
                        pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                                        + id + ".3gp";
                        ai.add(pathSave);
                        id++;
                        setupMediaRecorder();
                        try {
                                    mediaRecorder.prepare();
                                    mediaRecorder.start();
                        } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getApplicationContext(), "Recording.."+ pathSave, Toast.LENGTH_LONG).show();

                            }


                    else {
                        counter = false;
                        mediaRecorder.stop();
                        Toast.makeText(getApplicationContext(), "Recorded", Toast.LENGTH_SHORT).show();
                    }
                }
                public void setupMediaRecorder() {
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.setOutputFile(ai.get(id-1));
                }
            });

            l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(ai.get(i));
                        Log.i("mesg: ", ai.get(id-1));
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                }
            });
        } else {
            requestPermission();
        }
    }private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_CODE);
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }
}