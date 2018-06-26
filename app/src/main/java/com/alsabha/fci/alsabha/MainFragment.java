package com.alsabha.fci.alsabha;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.alsabha.fci.alsabha.Utillities.Constants;

public class MainFragment extends Fragment {


    String[]countNumbers={"1","2","3","4","5","6","7","8","9","10"};
    final boolean[]checked={false,false};
    int selelctedZaker=0;

    AlertDialog.Builder builder1 = null;
    AlertDialog.Builder builder2 = null;
    AlertDialog dialog = null;


    TextView zekarText , counterText ;
    Button settingbtn,resetbtn,menubtn;
    LinearLayout areaClick ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        Constants.settingitems = getResources().getStringArray(R.array.setting_items);
        settingbtn = fragmentView.findViewById(R.id.setting_btn);
        resetbtn = fragmentView.findViewById(R.id.reset_btn);
        menubtn = fragmentView.findViewById(R.id.menu_btn);
        zekarText = fragmentView.findViewById(R.id.zakertext);
        counterText = fragmentView.findViewById(R.id.counterText);
        areaClick = fragmentView.findViewById(R.id.areaClick);
        setData();
        initViews();
        return fragmentView ;
    }
    private void initViews(){

        /**
         * Alert Dialog for selected zekar (Menu)
         * */

        builder1 = new AlertDialog.Builder(getContext() , AlertDialog.THEME_HOLO_DARK);
        builder1.setTitle(R.string.zekarAlertTitle)
                .setSingleChoiceItems(Constants.alazkaritems, -1
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                countNumbers[selelctedZaker] = counterText.getText().toString();
                                selelctedZaker = position;
                                saveData();
                                zekarText.setText(Constants.alazkaritems[position]);
                                dialog.dismiss();
                            }
                        });
        /**
         * Alert Dialog for Setting
         * */
        builder2 = new AlertDialog.Builder(getContext(),AlertDialog.THEME_HOLO_DARK);
        builder2.setTitle(R.string.setting)
                .setMultiChoiceItems(Constants.settingitems, checked
                        , new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                                checked[position] = isChecked;
                                saveData();
                            }
                        })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });

        /**
         * area click change Counter when Clicked
         * */
        areaClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checked[0]) {
                    MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.sound);
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.reset();
                            mp.release();
                        }

                        ;
                    });
                }
                if (checked[1]) {
                    Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(100);
                }

                int num = Integer.parseInt(String.valueOf(counterText.getText()));
                countNumbers[selelctedZaker]= String.valueOf(++num);
                counterText.setText(String.valueOf(num));
                saveData();
            }
        });
        /**
         * setting Button action
         * */

        settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checked[0]) {
                    MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.sound);
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.reset();
                            mp.release();
                        }

                        ;
                    });
                }

                dialog = builder2.create();
                dialog.show();

            }
        });

        /**
         * reset Button action
         * */
        resetbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                countNumbers[selelctedZaker] = "0";
                saveData();
                if (checked[0]) {

                    final MediaPlayer audio = MediaPlayer.create(getContext() , R.raw.sound);
                    audio.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    audio.start();
                    audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            audio.reset();
                            audio.release();
                        }
                    });
                }

            }
        });
        /**
         * menu Button Action
         * */
        menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checked[0]) {
                    MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.sound);
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.reset();
                            mp.release();
                        }

                        ;
                    });
                }

                dialog = builder1.create();
                dialog.show();

            }
        });
    }

    /**
     * save Setting
     * */

    public void saveData(){

        String countersNumbersString = countNumbers[0];
        for (int i = 1 ; i < countNumbers.length ; i++ ) {
            countersNumbersString += "," + countNumbers[i];
        }

        SharedPreferences data = getContext().getSharedPreferences("Setting"
                ,getContext().MODE_PRIVATE);
        SharedPreferences.Editor editData = data.edit();
        editData.putInt("selectedZaker",selelctedZaker);
        editData.putBoolean("sound",checked[0]);
        editData.putBoolean("vibration",checked[1]);
        editData.putString("counters",countersNumbersString);
        editData.commit();
        setData();
    }

    public void setData(){

        SharedPreferences data = getContext().getSharedPreferences("Setting"
                , getContext().MODE_PRIVATE);
        selelctedZaker = data.getInt("selectedZaker",0);
        checked[0] = data.getBoolean("sound",true);
        checked[1] = data.getBoolean("vibration" ,true);
        String countersNumbersString = data.getString("counters","0,0,0,0,0,0,0,0,0,0");
        countNumbers = countersNumbersString.split(",");
        counterText.setText(countNumbers[selelctedZaker]);
        zekarText.setText(Constants.alazkaritems[selelctedZaker]);
    }

}
