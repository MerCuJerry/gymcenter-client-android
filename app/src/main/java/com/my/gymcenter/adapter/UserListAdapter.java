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
import com.my.gymcenter.entity.User;

import java.util.List;

public  class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private final Context mContext;
    private final ViewStruct mHolder= new ViewStruct();
    private List<User> mlist;
    public OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View parent,int position) ;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener=onItemClickListener;
    }

    public UserListAdapter(Context context, List<User> mList){
        this.mContext=context;
        this.mlist=mList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<User> mList) {
        this.mlist = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view =LayoutInflater.from(mContext).inflate(R.layout.item_user,viewGroup,false);
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
            mHolder.title = itemView.findViewById(R.id.list_item_title);
            mHolder.username = itemView.findViewById(R.id.list_item_username);
            mHolder.phone = itemView.findViewById(R.id.phone_num);
            if (mOnItemClickListener != null){
                itemView.setOnClickListener(v -> {
                    mOnItemClickListener.onItemClick(v,getAdapterPosition());
                });
            }
        }

        @SuppressLint("SetTextI18n")
        public void setData(User news) {
            switch (news.get_permission()) {
                case "admin":
                    mHolder.title.setText("管理员");
                    break;
                case "coach":
                    mHolder.title.setText("教练");
                    break;
                case "user":
                    mHolder.title.setText("会员");
                    break;
            }
            mHolder.username.setText("姓名："+news.get_username());
            mHolder.phone.setText("电话："+news.get_phone_num());
        }

    }
    private static class ViewStruct {
        public TextView title;
        public TextView username;
        public TextView phone;
    }
}
