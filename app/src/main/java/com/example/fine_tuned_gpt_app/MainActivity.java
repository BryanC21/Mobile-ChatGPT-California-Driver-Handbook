package com.example.fine_tuned_gpt_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    TextView answer;
    EditText input;
    Button ask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        answer = findViewById(R.id.textView);
        input = findViewById(R.id.editTextText2);
        ask = findViewById(R.id.button);

        ask.setOnClickListener(v -> {
            String question = input.getText().toString();
            if (question.isEmpty()) {
                answer.setText("Please enter a question");
                return;
            }
            answer.setText("Loading...");
            // get response from trained GPT-3 API
            String url = "http://54.241.90.187:6006";
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                String res = fetchData(url, question);
                String response = "Answer: "+ res;
                runOnUiThread(() -> answer.setText(response));
            });

        });

    }

    private String fetchData(String urlString, String question) {
        urlString += "?q=" + question;
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result.toString();
    }

}