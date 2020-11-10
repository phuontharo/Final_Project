package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.final_project.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.final_project.MainActivity.musicBackgroundService;

public class Information extends AppCompatActivity {
    int currentPlayer = 0;
    int possIMG = 0, preIMG, nextIMG, avatar1, avatar2;
    EditText inputName;
    ImageView avatar, btnPre, btnNext;
    ArrayList<Integer> listImg;
    Player[] playerInfor;
    Intent intent;
    private Bitmap operation;
    int buttonEffect = R.raw.choose_sound;
    int slideSound = R.raw.slide_sound;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_information);
        getSupportActionBar().hide();
        setUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (musicBackgroundService != null) {
            musicBackgroundService.resumeMusic();
        }
    }

    private void setUp() {
        btnPre = findViewById(R.id.imagePre);
        btnNext = findViewById(R.id.imageNext);
        avatar = findViewById(R.id.avatar);
        inputName = findViewById(R.id.name);
        listImg = getListAvatar();
        pref = getSharedPreferences("PLAYER_INFORMATION", MODE_PRIVATE); // ten cua file luu tru
        editor = pref.edit();
        preRegisterInfor();
        setScreenInformation(playerInfor[0]);
        intent = new Intent(this, LocalGameActivity.class);
    }

    private void preRegisterInfor() { // set infor default for player
        String name = pref.getString("NAME_1", null);
        avatar1 = pref.getInt("AVARTAR_1", -1);
        String name2 = pref.getString("NAME_2", null);
        avatar2 = pref.getInt("AVARTAR_2", -1);

        playerInfor = new Player[2];
        Player p1, p2;
        if (name == null || avatar1 == -1
                || name2 == null || avatar2 == -1) {
            p1 = new Player("Joe", listImg.get(0));
            p2 = new Player("Akaly", listImg.get(1));
            avatar1 = 0;
            avatar2 = 1;
        } else {
            p1 = new Player(name, listImg.get(avatar1));
            p2 = new Player(name2, listImg.get(avatar2));
        }
        playerInfor[0] = p1;
        playerInfor[1] = p2;
    }

    private void setScreenInformation(Player player) {
        inputName.setHint("Player " + (currentPlayer + 1));
        inputName.setText(player.getName());
        if (currentPlayer == 0) possIMG = avatar1;
        else possIMG = avatar2;
        avatar.setImageResource(player.getImgId());
        setImageForButton();
    }

    private ArrayList<Integer> getListAvatar() {
        ArrayList<Integer> listAvt = new ArrayList<>();
        String[] imgAvt = getResources().getStringArray(R.array.list_name);
        for (int i = 0; i < imgAvt.length; i++) {
            int idImg = getResources().getIdentifier(imgAvt[i], "drawable", getPackageName());
            listAvt.add(idImg);
        }
        return listAvt;
    }

    // create new player from information in screen
    Player getInforScreen() {
        String name = inputName.getText().toString().trim();
        int imgId = listImg.get(possIMG);
        return new Player(name, imgId);
    }

    private void setImageForButton() {
        System.out.println("Pre IMG " + preIMG);
        System.out.println("Poss IMG " + possIMG);
        preIMG = possIMG == 0 ? listImg.size() - 1 : possIMG - 1;
        System.out.println("Pre IMG " + preIMG);
        btnPre.setImageResource(listImg.get(preIMG));
        btnPre.setImageBitmap(effectForPicture(((BitmapDrawable) btnPre.getDrawable()).getBitmap()));
        nextIMG = possIMG == listImg.size() - 1 ? 0 : possIMG + 1;
        btnNext.setImageResource(listImg.get(nextIMG));
        btnNext.setImageBitmap(effectForPicture(((BitmapDrawable) btnNext.getDrawable()).getBitmap()));
    }

    // go to pre picture
    public void onClickPre(View view) {
        MediaPlayer mPlayer = MediaPlayer.create(this, slideSound);
        mPlayer.start();

        possIMG = possIMG == 0 ? listImg.size() - 1 : possIMG - 1;
        avatar.setImageResource(listImg.get(possIMG));
        setImageForButton();
    }

    // go to next picture
    public void onClickNext(View view) {
        MediaPlayer mPlayer = MediaPlayer.create(this, slideSound);
        mPlayer.start();

        possIMG = possIMG == listImg.size() - 1 ? 0 : possIMG + 1;
        avatar.setImageResource(listImg.get(possIMG));
        setImageForButton();
    }

    public Bitmap effectForPicture(Bitmap bmp) {
        //create bitmap
        Float darkness = 2f;
        int opacity = 50;
        operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                // set px in picture
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
                // change color
                r = r / darkness < 0 ? 0 : (int) (r / darkness);
                g = g / darkness < 0 ? 0 : (int) (g / darkness);
                b = b / darkness < 0 ? 0 : (int) (b / darkness);
                alpha = alpha - opacity < 0 ? 0 : alpha - opacity;
                // set picture
                operation.setPixel(i, j, Color.argb(alpha, r, g, b));
            }
        }
        return operation;
    }

    // transmit information of player
    public void onClickOk(View view) {
        MediaPlayer mPlayer = MediaPlayer.create(this, buttonEffect);
        mPlayer.start();

        Bundle bundle = getIntent().getExtras();
        playerInfor[currentPlayer] = getInforScreen();
        if (currentPlayer == 0) { // save infor user = infor player1
            saveFile();
            currentPlayer++;
            if (bundle.get("mode").toString().equals("double")) // if Local then go to set player2
                setScreenInformation(playerInfor[1]);
            else {//if Lan then get player2 infor
                getPlayerFromLAN();
                startActivity(intent);
            }
        } else {
            saveFile();
            startActivity(intent);
        }
    }

    private void getPlayerFromLAN() {
        playerInfor[1] = new Player("Player 2 hi", listImg.get(6));
        intent.putExtra("player2", playerInfor[1]);
    }

    public void saveFile() {
        intent.putExtra("player" + (currentPlayer + 1), playerInfor[currentPlayer]);
        editor.remove("NAME_" + (currentPlayer + 1));
        editor.commit();
        editor.putString("NAME_" + (currentPlayer + 1), playerInfor[currentPlayer].getName());
        editor.putInt("AVARTAR_" + (currentPlayer + 1), possIMG);
        editor.commit();
    }
}