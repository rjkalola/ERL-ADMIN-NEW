package com.app.erladmin.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erladmin.R;
import com.app.erladmin.adapter.AddressListAdapter;
import com.app.erladmin.callback.SelectItemListener;
import com.app.erladmin.databinding.ActivitySelectAddressBinding;
import com.app.erladmin.model.entity.response.AddressListResponse;
import com.app.erladmin.util.AppConstant;
import com.app.erladmin.util.AppUtils;
import com.app.erladmin.util.LoginViewModelFactory;
import com.app.erladmin.util.ResourceProvider;
import com.app.erladmin.viewModel.DashboardViewModel;
import com.app.utilities.utils.AlertDialogHelper;

public class SelectAddressActivity extends BaseActivity implements SelectItemListener {
    private ActivitySelectAddressBinding binding;
    private Context mContext;
    private AddressListResponse addressData;
    private AddressListAdapter adapter;
    private DashboardViewModel dashboardViewModel;
    private int clientId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_address);
        mContext = this;
        setupToolbar(getString(R.string.select_client_address), true);
        dashboardViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(DashboardViewModel.class);
        dashboardViewModel.createView(this);
        dashboardViewModel.addressListResponse()
                .observe(this, getAddressListResponse());

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra(AppConstant.IntentKey.USER_ID)) {
            clientId = getIntent().getIntExtra(AppConstant.IntentKey.USER_ID, 0);
            dashboardViewModel.getAddressesRequest(clientId);
        }
    }

    private void setAddressAdapter() {
        if (getAddressData() != null
                && getAddressData().getInfo() != null
                && getAddressData().getInfo().size() > 0) {
            binding.routDetailsView.setVisibility(View.VISIBLE);
            binding.routEmptyView.setVisibility(View.GONE);

            binding.rvAddressList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            binding.rvAddressList.setHasFixedSize(true);
            adapter = new AddressListAdapter(mContext, getAddressData().getInfo(), this);
            binding.rvAddressList.setAdapter(adapter);
        } else {
            binding.routDetailsView.setVisibility(View.GONE);
            binding.routEmptyView.setVisibility(View.VISIBLE);
        }
    }

    public Observer getAddressListResponse() {
        return (Observer<AddressListResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setAddressData(response);
                    setAddressAdapter();
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public void onSelectItem(int position, int action) {
        switch (action) {
            case AppConstant.Action.SELECT_ADDRESS:
                Intent i = new Intent();
                i.putExtra(AppConstant.IntentKey.ADDRESS_ID, getAddressData().getInfo().get(position).getId());
                setResult(1, i);
                finish();
                break;
        }
    }

    public AddressListResponse getAddressData() {
        return addressData;
    }

    public void setAddressData(AddressListResponse addressData) {
        this.addressData = addressData;
    }

}
