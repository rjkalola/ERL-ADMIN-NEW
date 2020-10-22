package com.app.erladmin.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erladmin.R;
import com.app.erladmin.adapter.ServiceSelectedItemsTitleListAdapter;
import com.app.erladmin.callback.SelectedServiceItemListener;
import com.app.erladmin.databinding.ActivityConfirmOrderBinding;
import com.app.erladmin.model.entity.info.ItemInfo;
import com.app.erladmin.model.entity.info.ServiceItemInfo;
import com.app.erladmin.model.entity.response.BaseResponse;
import com.app.erladmin.util.AppConstant;
import com.app.erladmin.util.AppUtils;
import com.app.erladmin.util.LoginViewModelFactory;
import com.app.erladmin.util.ResourceProvider;
import com.app.utilities.callbacks.DialogButtonClickListener;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.StringHelper;
import com.app.utilities.utils.ToastHelper;
import com.app.utilities.utils.ValidationUtil;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener
        , SelectedServiceItemListener, DialogButtonClickListener {
    private ActivityConfirmOrderBinding binding;
    private Context mContext;
    private ServiceSelectedItemsTitleListAdapter adapter;
    private String[] LOCATION_PERMISSION = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
//    private ManageOrderViewModel manageOrderViewModel;

    private List<ItemInfo> listItems;
    private int totalAmount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_order);
        mContext = this;
        listItems = new ArrayList<>();
//        manageOrderViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageOrderViewModel.class);
//        manageOrderViewModel.createView(this);
//        manageOrderViewModel.mBaseResponse()
//                .observe(this, saveOrderResponse());

        binding.txtNext.setOnClickListener(this);

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null) {
            if (Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.ITEMS_LIST)) != null)
                listItems = Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.ITEMS_LIST));

//            if (Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.ORDER_DATA)) != null)
//                manageOrderViewModel.setSaveOrderRequest(Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.ORDER_DATA)));
//
//            if (manageOrderViewModel.getSaveOrderRequest() != null) {
//                Log.e("test", "isDeduct_wallet:" + manageOrderViewModel.getSaveOrderRequest().isDeduct_wallet());
//                binding.setSaveOrderRequest(manageOrderViewModel.getSaveOrderRequest());
//                binding.txtPickUpTime.setText(manageOrderViewModel.getSaveOrderRequest().getPickup_date() + ", " + manageOrderViewModel.getSaveOrderRequest().getPickup_hour());
//                binding.txtDeliverTime.setText(manageOrderViewModel.getSaveOrderRequest().getDeliver_date() + ", " + manageOrderViewModel.getSaveOrderRequest().getDeliver_hour());
//
//                if (!StringHelper.isEmpty(manageOrderViewModel.getSaveOrderRequest().getDelivery_note()))
//                    binding.routOrderNote.setVisibility(View.VISIBLE);
//                else
//                    binding.routOrderNote.setVisibility(View.GONE);
//
//                if (manageOrderViewModel.getSaveOrderRequest().getType() == 0) {
//                    binding.routOrderList.setVisibility(View.VISIBLE);
//                    binding.routManageAmount.setVisibility(View.VISIBLE);
//                    calculateTotalAmount();
//                    setSelectedItemsAdapter();
//                }else {
//                    binding.routOrderList.setVisibility(View.GONE);
//                    binding.routManageAmount.setVisibility(View.GONE);
//                }
//            }
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        Calendar c = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.txtNext:
//                AlertDialogHelper.showDialog(mContext, null, getString(R.string.msg_place_order), getString(R.string.yes), getString(R.string.no), true, this, AppConstant.DialogIdentifier.PLACE_ORDER);
                break;
        }
    }

    private void setSelectedItemsAdapter() {
        if (listItems.size() > 0) {
            binding.routOrderList.setVisibility(View.VISIBLE);
            binding.rvSelectedItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            binding.rvSelectedItems.setHasFixedSize(true);
            adapter = new ServiceSelectedItemsTitleListAdapter(mContext, listItems, this, true);
            binding.rvSelectedItems.setAdapter(adapter);
        } else {
            binding.routOrderList.setVisibility(View.GONE);
        }
    }

    public Observer saveOrderResponse() {
        return (Observer<BaseResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
//                    moveActivity(mContext, OrderCompletedActivity.class, true, true, null);
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public void onSelectServiceItem(int rootPosition, int itemPosition, int quantity) {
        listItems.get(rootPosition).getServiceList().get(itemPosition).setQuantity(quantity);
    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier) {
//        if (dialogIdentifier == AppConstant.DialogIdentifier.PLACE_ORDER) {
//            manageOrderViewModel.saveAddressRequest();
//        }
    }

    @Override
    public void onNegativeButtonClicked(int dialogIdentifier) {

    }


}
