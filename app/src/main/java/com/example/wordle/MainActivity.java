package com.example.wordle;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int letter_position = 0;
    String correct_word;
    int word_pos;
    String word="";
    boolean gameActive = true;
    List<String> allwordsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            ReadTextFile();
        }
        catch (Exception e)
        {

        }
        playMusic(R.raw.background_music);
        word_pos = (int) (Math.random()*2500);
        correct_word = allwordsList.get(word_pos);
    }
    public static MediaPlayer music;
    public void playMusic(int id)
    {
        music = MediaPlayer.create(MainActivity.this, id);
        music.setLooping(true);
        music.start();
    }
    public void ReadTextFile() throws IOException {
        String string = "";
        InputStream is = this.getResources().openRawResource(R.raw.words_list);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                if ((string = reader.readLine()) == null) break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            allwordsList.add(string);
        }
        is.close();
    }

    public void tapIn(View v)
    {
        if(word.length()<5 && gameActive)
        {
            Button button = findViewById(v.getId());
            word+=button.getText();
            GridLayout grid = findViewById(R.id.grid);
            TextView tv = (TextView) grid.getChildAt(letter_position);
            tv.setText(button.getText());
            letter_position++;
        }
    }

    public void enter(View v)
    {
        if(word.length() != 5 && gameActive) {
            Toast.makeText(this, "Not Enough Letters", Toast.LENGTH_SHORT).show();
        }
        else if(!allwordsList.contains(word) && gameActive)
        {
            Toast.makeText(this, "Not in the words list.", Toast.LENGTH_SHORT).show();
        }
        else if(gameActive)
        {

            GridLayout grid = findViewById(R.id.grid);
            letter_position-=5;
            int i;
            StringBuilder string = new StringBuilder(correct_word);
            for(i=0;i<5;i++)
            {
                TextView tv = (TextView) grid.getChildAt(letter_position);
                if(word.charAt(i) == string.charAt(i))
                {
                    tv.setBackgroundResource(R.drawable.box_4);
                    string.setCharAt(i, ' ');
                }
                letter_position++;
            }

            letter_position-=5;
            for(i=0;i<5;i++)
            {
                TextView tv = (TextView) grid.getChildAt(letter_position);
                if(string.indexOf(Character.toString(word.charAt(i))) != -1)
                {
                    tv.setBackgroundResource(R.drawable.box_3);
                    string.setCharAt(string.indexOf(Character.toString(word.charAt(i))), '*');
                }
                else if(string.charAt(i) != ' ')
                    tv.setBackgroundResource(R.drawable.box_2);
                letter_position++;
            }
            if(word.equals(correct_word))
            {
                gameActive = false;
                TextView tv = findViewById(R.id.display);
                tv.setText("Perfect! \nTry another word.");
                LinearLayout ll = findViewById(R.id.keyboard);
                ll.setVisibility(View.INVISIBLE);
                ll = findViewById(R.id.newgame);
                ll.setVisibility(View.VISIBLE);

            }
            else if(letter_position == 30)
            {
                gameActive = false;
                TextView tv = findViewById(R.id.display);
                tv.setText("Oops! You missed the correct word. \nThe correct word is "+correct_word+".\nTry another word.");
                LinearLayout ll = findViewById(R.id.keyboard);
                ll.setVisibility(View.INVISIBLE);
                ll = findViewById(R.id.newgame);
                ll.setVisibility(View.VISIBLE);
            }
            word="";
        }
        else
            Toast.makeText(this, "Not Enough Letters", Toast.LENGTH_SHORT).show();
    }

    public void backspace(View v)
    {
        if(word.length()!=0)
        {
            letter_position--;
            word = word.substring(0,word.length()-1);
            GridLayout grid = findViewById(R.id.grid);
            TextView tv = (TextView) grid.getChildAt(letter_position);
            tv.setText("");
        }
    }

    public void tryAnother(View v)
    {
        GridLayout grid = findViewById(R.id.grid);
        for(int i=0; i<30; i++)
        {
            TextView tv = (TextView) grid.getChildAt(i);
            tv.setBackgroundResource(R.drawable.box_1);
            tv.setText("");
        }
        LinearLayout ll = findViewById(R.id.keyboard);
        ll.setVisibility(View.VISIBLE);
        ll = findViewById(R.id.newgame);
        ll.setVisibility(View.INVISIBLE);
        letter_position=0;
        word_pos = (int) (Math.random()*2500);
        correct_word = allwordsList.get(word_pos);
        gameActive = true;
    }
    MediaPlayer media;
    public void platTapSound()
    {
        media= MediaPlayer.create(MainActivity.this, R.raw.tap_sound);
        media.start();
        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
            }
        });
    }
    @Override
    protected void onPause(){
        super.onPause();
        music.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        music.start();
    }
}