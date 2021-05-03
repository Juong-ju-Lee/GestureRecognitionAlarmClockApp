package com.dhkdw.androidopencv;
        import android.app.AlarmManager;
        import android.app.PendingIntent;
        import android.content.Intent;
        import android.os.Build;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.view.GestureDetector;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.TimePicker;
        import android.widget.Toast;

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
    GestureDetector detector;
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

        findViewById(R.id.btnAdd).setOnClickListener(mClickListener);


        /*알람 리스트 생성*/

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        alarmAdapter = new AlarmAdapter(arrayList);
        recyclerView.setAdapter(alarmAdapter);









//수정코드//수정코드//수정코드//수정코드//수정코드//수정코드

        detector = new GestureDetector(this, new GestureDetector.OnGestureListener() {

            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            public void onLongPress(MotionEvent motionEvent) {// 리스트를 길게 눌렀을 때

                // 동작인식 성공시 알람을 끄고 해당 알람 리스트 삭제
                   // faceCapture(); // 동작인식 화면 띄우기


                stopA(); // 알람음 끄기
                alarmAdapter.remove(AlarmAdapter.CustomViewHolder.position); // 알람목록삭제
            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

        });

        View view2 = findViewById(R.id.recyclerView);

        view2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return true;
            }
        });



//수정코드//수정코드//수정코드//수정코드//수정코드





    }




    /* 알람 시작 */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startA() {
        // 시간 설정
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, this.timePicker.getHour());
        calendar.set(Calendar.MINUTE, this.timePicker.getMinute());
        calendar.set(Calendar.SECOND, 0);

        // 현재시간보다 이전이면
        if (calendar.before(Calendar.getInstance())) {
            // 다음날로 설정
            calendar.add(Calendar.DATE, 1);
        }

        // Receiver 설정
        Intent intent = new Intent(this, AlarmReceiver.class);
        // state 값이 on 이면 알람시작, off 이면 중지
        intent.putExtra("state", "on");

        this.pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 알람 설정
        this.alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        // Toast 보여주기 (알람 시간 표시)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Toast.makeText(this, "Alarm : " + format.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
        String alarmDate =format.format(calendar.getTime());



        /*알람 리스트 생성*/
        count++;
        AlarmList alarmList = new AlarmList(R.mipmap.ic_launcher, "알람"+count, ""+alarmDate);
        arrayList.add(alarmList);
        alarmAdapter.notifyDataSetChanged();
    }


/*
    public void faceCapture(){
        //얼굴인식을 위해 카메라를 켠다.
       Intent intent = new Intent(this, AndroidOpencv.class);
       startActivity(intent);
    };
*/

    /* 알람 중지*/

    private void stopA() {
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
                case R.id.btnAdd:
                    // 알람 시작
                    startA();
                    break;
            }

        }
    };



}