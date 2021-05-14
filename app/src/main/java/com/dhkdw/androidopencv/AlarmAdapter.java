package com.dhkdw.androidopencv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class AlarmAdapter extends RecyclerView.Adapter<com.dhkdw.androidopencv.AlarmAdapter.CustomViewHolder> {

    private ArrayList<AlarmList> arrayList;

    public AlarmAdapter(ArrayList<AlarmList> arrayList){
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public com.dhkdw.androidopencv.AlarmAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final com.dhkdw.androidopencv.AlarmAdapter.CustomViewHolder holder, int position) {
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

               remove(holder.getAdapterPosition());
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
}