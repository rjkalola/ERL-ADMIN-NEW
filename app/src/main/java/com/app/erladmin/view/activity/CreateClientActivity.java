package com.app.erladmin.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erladmin.R;
import com.app.erladmin.databinding.ActivityCreateClientBinding;
import com.app.erladmin.model.entity.response.BaseResponse;
import com.app.erladmin.util.AppConstant;
import com.app.erladmin.util.AppUtils;
import com.app.erladmin.util.LoginViewModelFactory;
import com.app.erladmin.util.ResourceProvider;
import com.app.erladmin.viewModel.DashboardViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.ToastHelper;
import com.app.utilities.utils.ValidationUtil;

import org.parceler.Parcels;

public class CreateClientActivity extends BaseActivity implements View.OnClickListener {
    private ActivityCreateClientBinding binding;
    private DashboardViewModel dashboardViewModel;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_client);
        mContext = this;
        dashboardViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(DashboardViewModel.class);
        dashboardViewModel.createView(this);
        dashboardViewModel.mBaseResponse()
                .observe(this, storeClientResponse());

        binding.txtSave.setOnClickListener(this);

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent() != null
                && getIntent().getExtras() != null
                && getIntent().hasExtra(AppConstant.IntentKey.CLIENT_INFO)) {
            setupToolbar(getString(R.string.update_client), true);
            if (Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.CLIENT_INFO)) != null) {
                dashboardViewModel.setAddClientRequest(Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.CLIENT_INFO)));
                binding.setDashboardViewModel(dashboardViewModel);
            }
        } else {
            setupToolbar(getString(R.string.add_client), true);
            binding.setDashboardViewModel(dashboardViewModel);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSave:
                if (isValid()) {
                    if (AppUtils.isNetworkConnected(mContext))
                        dashboardViewModel.storeClientRequest();
                    else
                        ToastHelper.error(mContext, getString(R.string.error_internet_connection), Toast.LENGTH_SHORT, false);
                }
                break;
        }
    }

    public Observer storeClientResponse() {
        return (Observer<BaseResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setResult(1);
                    finish();
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public boolean isValid() {
        boolean isValid = true;

        if (!ValidationUtil.isEmptyEditText(dashboardViewModel.getAddClientRequest().getPassword())) {
            binding.edtPassword.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtPassword, mContext.getString(R.string.error_empty_password));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(dashboardViewModel.getAddClientRequest().getPhone())) {
            binding.edtPhoneNumber.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtPhoneNumber, mContext.getString(R.string.error_empty_phone));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(dashboardViewModel.getAddClientRequest().getEmail())) {
            if (ValidationUtil.isValidEmail(binding.edtEmail.getText().toString())) {
                binding.edtEmail.setError(null);
            } else {
                ValidationUtil.setErrorIntoEditext(binding.edtEmail, mContext.getString(R.string.error_invalid_email));
                isValid = false;
            }
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtEmail, mContext.getString(R.string.error_empty_email));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(dashboardViewModel.getAddClientRequest().getName())) {
            binding.edtName.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtName, mContext.getString(R.string.error_empty_name));
            isValid = false;
        }

        return isValid;
    }

}
