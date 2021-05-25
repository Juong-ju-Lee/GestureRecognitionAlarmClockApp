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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //수정코드//수정코드//수정코드//수정코드//수정코드
    private AudioManager audio;
    private Calendar calendar;
    //수정코드//수정코드//수정코드//수정코드//수정코드

    private ArrayList<AlarmList> arrayList;
    private AlarmAdapter alarmAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    int count =0;

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

        findViewById(R.id.btnAdd).setOnClickListener(mClickListener);
        findViewById(R.id.btnCalendar).setOnClickListener(mClickListener);

        //수정:볼륨조절
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        setVolumeControlStream(AudioManager.STREAM_MUSIC); // 볼륨 키를 누를 때 기본 값으로 미디어 음량으로 조절하게 한다.
        audio.setStreamVolume(AudioManager.STREAM_RING, audio.getStreamMaxVolume(AudioManager.STREAM_RING), AudioManager.FLAG_SHOW_UI); // 음량을 최대로 설정
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI); // 음량을 최대로 설정


        /*알람 리스트 생성*/
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        alarmAdapter = new AlarmAdapter(arrayList);
        recyclerView.setAdapter(alarmAdapter);

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


    //수정코드//수정코드//수정코드//수정코드//수정코드//수정코드 볼륨조절
    public boolean onKeyDown(int keyCode, KeyEvent event) { // 볼륨 키를 눌러도 소리를 최대로 출력하도록 함
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP : // 볼륨 업

            case KeyEvent.KEYCODE_VOLUME_DOWN: // 볼륨다운
                // 첫번째 인자는 벨소리 음악소리등의 타입, 두번째 인자는 볼륨의 크기, 세번째인자는 플래그(변경후 UI or 소리출력)
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI); // 음량을 최대로 설정
                return true;

            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return false;
    }

    //Opencv 수정코드
    //알람 리스트 길게 눌렀을 때 동작인식
public void faceCapture() {
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

        /*알람 리스트 생성*/
        count++;
        AlarmList alarmList = new AlarmList(R.mipmap.ic_a_round, "알람"+count, ""+alarmDate);
        arrayList.add(alarmList);
        alarmAdapter.notifyDataSetChanged();
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
            }

        }
    };



    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView alarmImg;
        protected TextView alarmName;
        protected TextView alarmContent;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.alarmImg=(ImageView) itemView.findViewById(R.id.alarmImg);
            this.alarmName=(TextView) itemView.findViewById(R.id.alarmName);
            this.alarmContent=(TextView) itemView.findViewById(R.id.alarmContent);
        }
    }


    public class AlarmAdapter extends RecyclerView.Adapter<com.dhkdw.androidopencv.MainActivity.CustomViewHolder>{
        private ArrayList<AlarmList> arrayList;

        public AlarmAdapter(ArrayList<AlarmList> arrayList){
            this.arrayList=arrayList;
        }

        @NonNull
        @Override
        public com.dhkdw.androidopencv.MainActivity.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
            CustomViewHolder holder = new CustomViewHolder(view);
            return holder;
        }


        @Override
        public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {
            holder.alarmImg.setImageResource(arrayList.get(position).getAlarmImg());
            holder.alarmName.setText(arrayList.get(position).getAlarmName());
            holder.alarmContent.setText(arrayList.get(position).getAlarmContent());
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String curName = holder.alarmName.getText().toString();
                    Toast.makeText(v.getContext(),curName, Toast.LENGTH_SHORT).show();
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    faceCapture(); // 동작인식 화면 띄우기
                    remove(holder.getAdapterPosition()); // 알람목록삭제
                    return true;
                }
            });
        }



        @Override
        public int getItemCount() {
            return (null != arrayList ? arrayList.size() :0);
        }


        public void remove(int position){
            try{
                arrayList.remove(position);
                notifyItemRemoved(position);
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }


    }

}