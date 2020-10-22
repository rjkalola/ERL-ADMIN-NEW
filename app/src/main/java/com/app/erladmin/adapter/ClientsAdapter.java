package com.app.erladmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erladmin.R;
import com.app.erladmin.databinding.RowClientsBinding;
import com.app.erladmin.model.entity.info.ClientInfo;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;

import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ClientInfo> list;

    public ClientsAdapter(Context context, List<ClientInfo> list) {
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_clients, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ClientInfo info = list.get(position);
        itemViewHolder.getData(info);
        setImage(itemViewHolder.binding.imgUser, info.getImage());
        itemViewHolder.binding.txtId.setText("#" + info.getId());

//        if (AppUtils.getPermissionSettings(mContext) != null
//                && AppUtils.getPermissionSettings(mContext).isDelete_team()) {
//            itemViewHolder.binding.check.setVisibility(View.VISIBLE);
//            if (user != null && (user.getUser_type_id() == AppConstant.UserType.ADMIN
//                    || user.getId() == info.getUser_id()))
//                itemViewHolder.binding.check.setEnabled(true);
//            else
//                itemViewHolder.binding.check.setEnabled(false);
//        } else {
//            itemViewHolder.binding.check.setVisibility(View.GONE);
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowClientsBinding binding;

        public void getData(ClientInfo info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void addData(List<ClientInfo> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }

//    // Filter Class
//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        list.clear();
//
//        if (charText.length() == 0) {
//            list.addAll(listUsers);
//        } else {
//            for (TeamInfo wp : listUsers) {
//                try {
//                    if (String.valueOf(wp.getTitle()).toLowerCase(Locale.getDefault()).contains(charText)) {
//                        list.add(wp);
//                    }
//                } catch (Exception e) {
//                    Log.e(getClass().getName(), "Exception in Filter :" + e.getMessage());
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

    private void setImage(ImageView imageView, String url) {
        if (!StringHelper.isEmpty(url)) {
            GlideUtil.loadImageUsingGlideTransformation(url, imageView, Constant.TransformationType.CIRCLECROP_TRANSFORM, null, null, Constant.ImageScaleType.CENTER_CROP, 0, 0, "", 0, null);
        }
    }

    public List<ClientInfo> getList() {
        return list;
    }

    public void setList(List<ClientInfo> list) {
        this.list = list;
    }

}
