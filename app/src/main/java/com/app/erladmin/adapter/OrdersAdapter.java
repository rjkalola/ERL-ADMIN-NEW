package com.app.erladmin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erladmin.R;
import com.app.erladmin.databinding.RowClientsBinding;
import com.app.erladmin.databinding.RowOrdersBinding;
import com.app.erladmin.model.entity.info.ClientInfo;
import com.app.erladmin.model.entity.info.OrderInfo;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<OrderInfo> list;

    public OrdersAdapter(Context context, List<OrderInfo> list) {
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_orders, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        OrderInfo info = list.get(position);
        itemViewHolder.getData(info);
        itemViewHolder.binding.txtOrderNumber.setText(String.format(mContext.getString(R.string.lbl_display_order_no), info.getOrder_no()));

        if (!StringHelper.isEmpty(info.getStatus_colour()))
            itemViewHolder.binding.imgOrderStatus.setColorFilter(Color.parseColor(info.getStatus_colour()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowOrdersBinding binding;

        public void getData(OrderInfo info) {
            binding.setOrderInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void addData(List<OrderInfo> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }

    public List<OrderInfo> getList() {
        return list;
    }

    public void setList(List<OrderInfo> list) {
        this.list = list;
    }

}
