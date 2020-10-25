package com.app.erladmin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erladmin.R;
import com.app.erladmin.callback.SelectItemListener;
import com.app.erladmin.databinding.RowChatUsersBinding;
import com.app.erladmin.model.entity.info.ChatUserInfo;
import com.app.erladmin.model.entity.info.OrderInfo;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatUserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ChatUserInfo> list, listUsers;
    private SelectItemListener listener;

    public ChatUserListAdapter(Context context, List<ChatUserInfo> list, SelectItemListener listener) {
        this.mContext = context;
        this.listUsers = new ArrayList<>();
        this.listUsers.addAll(list);
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_users, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ChatUserInfo info = list.get(position);
        itemViewHolder.getData(info);
        setImage(itemViewHolder.binding.imgUser, info.getImage());

        if (info.getUn_read_msg_count() > 0) {
            itemViewHolder.binding.txtUnreadCount.setVisibility(View.VISIBLE);
            itemViewHolder.binding.txtUnreadCount.setText(String.valueOf(info.getUn_read_msg_count()));
        } else {
            itemViewHolder.binding.txtUnreadCount.setVisibility(View.GONE);
        }

        itemViewHolder.binding.cvMain.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelectItem(position, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowChatUsersBinding binding;

        public void getData(ChatUserInfo info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    //    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();

        if (charText.length() == 0) {
            list.addAll(listUsers);
        } else {
            for (ChatUserInfo wp : listUsers) {
                try {
                    if (String.valueOf(wp.getName()).toLowerCase(Locale.getDefault()).contains(charText)) {
                        list.add(wp);
                    }
                } catch (Exception e) {
                    Log.e(getClass().getName(), "Exception in Filter :" + e.getMessage());
                }
            }
        }
        notifyDataSetChanged();
    }

    private void setImage(ImageView imageView, String url) {
        if (!StringHelper.isEmpty(url)) {
            GlideUtil.loadImageUsingGlideTransformation(url, imageView, Constant.TransformationType.CIRCLECROP_TRANSFORM, null, null, Constant.ImageScaleType.CENTER_CROP, 0, 0, "", 0, null);
        }
    }

    public List<ChatUserInfo> getList() {
        return list;
    }

    public void setList(List<ChatUserInfo> list) {
        this.list = list;
    }

}
