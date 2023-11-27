package com.my.gymcenter.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.gymcenter.R;
import com.my.gymcenter.entity.Course;

import java.util.List;

public  class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {

    private final Context mContext;
    private final ViewStruct mholder= new ViewStruct();
    private List<Course> mlist;
    public OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View parent,int position) ;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener=onItemClickListener;
    }

    public CourseListAdapter(Context context, List<Course> mlist){
        this.mContext=context;
        this.mlist=mlist;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Course> mList) {
        this.mlist = mList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view =LayoutInflater.from(mContext).inflate(R.layout.item_lesson,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.setData(mlist.get(position));
    }

    @Override
    public int getItemCount() {
        if(mlist!=null){
            return mlist.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mholder.title = itemView.findViewById(R.id.found_list_item_title);
            mholder.username = itemView.findViewById(R.id.found_list_item_username);
            mholder.teaname= itemView.findViewById(R.id.lesson_coach_name);
            if (mOnItemClickListener != null){
                itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v,getAdapterPosition()));
            }
        }

        public void setData(Course course) {
            mholder.title.setText(course.get_course_name());
            mholder.teaname.setText("");
            mholder.username.setText(course.get_course_describe());
        }
    }
    private static class ViewStruct {
        public TextView title;
        public TextView username;
        public TextView teaname;
    }
}
