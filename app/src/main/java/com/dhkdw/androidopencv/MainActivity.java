package com.dhkdw.androidopencv;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class MainActivity extends AppCompatActivity {
    private AudioManager audio;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 레이아웃 생성, 초기화 컴포넌트를 불러온다.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // activity_main.xml 파일을 불러온다.

        this.alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        this.timePicker = findViewById(R.id.timePicker);
        this.calendar=Calendar.getInstance();
        // 현재 날짜 표시
        displayDate();

        findViewById(R.id.btnStop).setOnClickListener(mClickListener);
        findViewById(R.id.btnAdd).setOnClickListener(mClickListener);
        findViewById(R.id.btnCalendar).setOnClickListener(mClickListener);

        // 볼륨조절
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        setVolumeControlStream(AudioManager.STREAM_MUSIC); // 볼륨 키를 누를 때 기본 값으로 미디어 음량으로 조절하게 한다.
        audio.setStreamVolume(AudioManager.STREAM_RING, audio.getStreamMaxVolume(AudioManager.STREAM_RING), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE); // 음량을 최대로 설정
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE); // 음량을 최대로 설정
    }

    // 날짜표시
    private void displayDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        ((TextView) findViewById(R.id.txtDate)).setText(format.format(this.calendar.getTime()));
    }

    private void showDatePicker(){
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DATE,dayOfMonth);
                // 날짜표시
                displayDate();
            }
        }, this.calendar.get(Calendar.YEAR),this.calendar.get(Calendar.MONTH),this.calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) { // 볼륨 키를 눌러도 소리를 최대로 출력하도록 함
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE); // 음량을 최대로 설정
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP : // 볼륨 업
            case KeyEvent.KEYCODE_VOLUME_DOWN: // 볼륨다운
                // 첫번째 인자는 벨소리 음악소리등의 타입, 두번째 인자는 볼륨의 크기, 세번째인자는 플래그(변경후 UI or 소리출력)
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI); // 음량을 최대로 설정
                return true;
        }
        return false;
    }

    public void faceCapture() {    // 눈인식을 위해 화면 전환
            Intent intent = new Intent(this, AndroidOpencv.class);
            startActivity(intent);
        }

    /* 알람 시작 */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startA() {
        // 알람 시간 설정
        this.calendar.set(Calendar.HOUR_OF_DAY, this.timePicker.getHour());
        this.calendar.set(Calendar.MINUTE, this.timePicker.getMinute());
        this.calendar.set(Calendar.SECOND, 0);

        // 현재일보다 이전이면 등록 실패
        if (this.calendar.before(Calendar.getInstance())) {
            Toast.makeText(this, "알람시간이 현재시간보다 이전일 수 없습니다.", Toast.LENGTH_LONG).show();
            return;
        }

        // Receiver 설정
        Intent intent = new Intent(this, AlarmReceiver.class);

        // state 값이 on 이면 알람시작, off 이면 중지
        intent.putExtra("state", "on");
        this.pendingIntent = PendingIntent.getBroadcast(this, 20, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 알람 설정
        this.alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        // Toast 보여주기 (알람 시간 표시)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String alarmDate =format.format(calendar.getTime());
        Toast.makeText(this, "Alarm : " + alarmDate, Toast.LENGTH_LONG).show();
    }

    /* 알람 중지*/
    public void stopA() {
        if (this.pendingIntent == null) {
            return;
        }

        // 알람 취소
        this.alarmManager.cancel(this.pendingIntent);

        // 알람 중지 Broadcast
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("state","off");

        sendBroadcast(intent);

        this.pendingIntent = null;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCalendar:
                    // 달력
                    showDatePicker();
                    break;

                case R.id.btnAdd:
                    // 알람 시작
                    startA();
                    break;

                case R.id.btnStop:
                    // 알람중지를 위해 동작인식 창으로 화면전환
                    faceCapture();
                    break;
            }

        }
    };

}