package com.app.erladmin.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erladmin.R;
import com.app.erladmin.adapter.ServiceSelectedItemsTitleListAdapter;
import com.app.erladmin.callback.SelectTimeListener;
import com.app.erladmin.callback.SelectedServiceItemListener;
import com.app.erladmin.databinding.ActivityCreateOrderBinding;
import com.app.erladmin.model.entity.info.ItemInfo;
import com.app.erladmin.model.entity.info.ModuleInfo;
import com.app.erladmin.model.entity.info.ModuleSelection;
import com.app.erladmin.model.entity.info.PickUpTimeInfo;
import com.app.erladmin.model.entity.info.ServiceItemInfo;
import com.app.erladmin.model.entity.response.ModuleResponse;
import com.app.erladmin.model.entity.response.OrderResourcesResponse;
import com.app.erladmin.util.AppConstant;
import com.app.erladmin.util.AppUtils;
import com.app.erladmin.util.LoginViewModelFactory;
import com.app.erladmin.util.PopupMenuHelper;
import com.app.erladmin.util.ResourceProvider;
import com.app.erladmin.view.dialog.SelectClientDialog;
import com.app.erladmin.viewModel.DashboardViewModel;
import com.app.utilities.callbacks.OnDateSetListener;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.DateFormatsConstants;
import com.app.utilities.utils.DateHelper;
import com.app.utilities.utils.StringHelper;
import com.app.utilities.utils.ToastHelper;
import com.app.utilities.utils.ValidationUtil;
import com.app.utilities.view.fragments.DatePickerFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateOrderActivity extends BaseActivity implements View.OnClickListener
        , SelectedServiceItemListener, OnDateSetListener
        , SelectTimeListener {
    private ActivityCreateOrderBinding binding;
    private Context mContext;
    private String fromTime, toTime;
    private int serviceHourTypeId = 0, orderType = 0, clientId;
    private ServiceSelectedItemsTitleListAdapter adapter;
    private String DATE_PICKER = "DATE_PICKER", DELIVER_DATE_PICKER = "DELIVER_DATE_PICKER";
    private DashboardViewModel dashboardViewModel;
    private OrderResourcesResponse orderData;
    private List<ItemInfo> listItems;
    private ModuleResponse clientsData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_order);
        mContext = this;
        setupToolbar(getString(R.string.lbl_confirm_order), true);
        listItems = new ArrayList<>();
        dashboardViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(DashboardViewModel.class);
        dashboardViewModel.createView(this);
        dashboardViewModel.moduleResponse()
                .observe(this, getAllClientsResponse());
        dashboardViewModel.orderResourcesResponse()
                .observe(this, orderResourcesResponse());

        binding.txtNext.setOnClickListener(this);
        binding.edtSelectDate.setOnClickListener(this);
        binding.edtSelectTime.setOnClickListener(this);
        binding.edtSelectDeliverDate.setOnClickListener(this);
        binding.edtSelectDeliverTime.setOnClickListener(this);
        binding.txtChangeAddress.setOnClickListener(this);

        dashboardViewModel.getAllClientsRequest();

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null) {
            if (Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.ITEMS_LIST)) != null)
                listItems = Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.ITEMS_LIST));

            serviceHourTypeId = getIntent().getIntExtra(AppConstant.IntentKey.SERVICE_HOUR_TYPE_ID, 0);
            orderType = getIntent().getIntExtra(AppConstant.IntentKey.ORDER_TYPE, 0);
            dashboardViewModel.getSaveOrderRequest().setLu_service_hour_type_id(serviceHourTypeId);
            dashboardViewModel.getSaveOrderRequest().setType(orderType);

            if (orderType == 0) {
                binding.routOrderList.setVisibility(View.VISIBLE);
                setSelectedItemsAdapter();
            } else {
                binding.routOrderList.setVisibility(View.GONE);
            }

//            dashboardViewModel.getOrderResourcesRequest();
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        Calendar c = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.txtNext:
                if (validate()) {
//                    AlertDialogHelper.showDialog(mContext, null, getString(R.string.msg_place_order), getString(R.string.yes), getString(R.string.no), true, this, AppConstant.DialogIdentifier.PLACE_ORDER);
                    dashboardViewModel.getSaveOrderRequest().setDelivery_note(binding.edtNote.getText().toString().trim());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppConstant.IntentKey.ITEMS_LIST, Parcels.wrap(listItems));
                    bundle.putParcelable(AppConstant.IntentKey.ORDER_DATA, Parcels.wrap(dashboardViewModel.getSaveOrderRequest()));
                    bundle.putInt(AppConstant.IntentKey.SERVICE_HOUR_TYPE_ID, serviceHourTypeId);
                    bundle.putInt(AppConstant.IntentKey.ORDER_TYPE, orderType);

                    Intent intent = new Intent(mContext, ConfirmOrderActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, AppConstant.IntentKey.VIEW_CART);
                }
                break;
            case R.id.edtSelectDate:
                if (!StringHelper.isEmpty(binding.edtSelectDate.getText().toString())) {
                    String date = DateHelper.changeDateFormat(binding.edtSelectDate.getText().toString(), DateFormatsConstants.DD_MMM_YYYY_SPACE, DateFormatsConstants.DD_MM_YYYY_DASH);
                    showDatePicker(c.getTime().getTime(), 0, DATE_PICKER, date);
                } else {
                    showDatePicker(c.getTime().getTime(), 0, DATE_PICKER, null);
                }
                break;
            case R.id.edtSelectTime:
                if (getOrderData() != null
                        && getOrderData().getPickup_hours() != null
                        && getOrderData().getPickup_hours().size() > 0) {
                    showSelectTimeDialog(AppConstant.DialogIdentifier.SELECT_TIME, v);
                } else {
                    ToastHelper.error(mContext, getString(R.string.msg_currently_no_service_available), Toast.LENGTH_SHORT, false);
                }
                break;
            case R.id.edtSelectDeliverDate:
                String date = DateHelper.changeDateFormat(binding.edtSelectDate.getText().toString(), DateFormatsConstants.DD_MMM_YYYY_SPACE, DateFormatsConstants.DD_MM_YYYY_DASH);
                if (!StringHelper.isEmpty(date)) {
                    try {
                        c.setTime(DateHelper.stringToDate(date, DateFormatsConstants.DD_MM_YYYY_DASH));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int day = 0;
                    switch (serviceHourTypeId) {
                        case 1:
                            day = 3;
                            break;
                        case 2:
                            day = 2;
                            break;
                        case 3:
                            day = 0;
                            break;
                    }
                    c.add(Calendar.DAY_OF_WEEK, day);
                    Date newDate = c.getTime();

                    if (!StringHelper.isEmpty(binding.edtSelectDate.getText().toString())) {
                        String date_ = DateHelper.changeDateFormat(binding.edtSelectDate.getText().toString(), DateFormatsConstants.DD_MMM_YYYY_SPACE, DateFormatsConstants.DD_MM_YYYY_DASH);
                        showDatePicker(newDate.getTime(), 0, DELIVER_DATE_PICKER, date_);
                    } else {
                        showDatePicker(newDate.getTime(), 0, DELIVER_DATE_PICKER, null);
                    }
                } else {
                    ToastHelper.error(mContext, getString(R.string.error_empty_order_date), Toast.LENGTH_LONG, false);
                }
                break;
            case R.id.edtSelectDeliverTime:
                if (!StringHelper.isEmpty(binding.edtSelectDeliverDate.getText().toString().trim())) {
                    if (getOrderData() != null
                            && getOrderData().getPickup_hours() != null
                            && getOrderData().getPickup_hours().size() > 0) {
                        showSelectTimeDialog(AppConstant.DialogIdentifier.SELECT_DELIVER_TIME, v);
                    } else {
                        ToastHelper.error(mContext, getString(R.string.msg_currently_no_service_available), Toast.LENGTH_SHORT, false);
                    }
                } else {
                    ToastHelper.error(mContext, getString(R.string.error_empty_delivery_date), Toast.LENGTH_SHORT, false);
                }
                break;
            case R.id.txtChangeAddress:
//                checkPermission();
                if (getClientsData() != null && !getClientsData().getInfo().isEmpty()) {
                    showClientListDialog(AppConstant.DialogIdentifier.SELECT_CLIENT, "", getClientsData().getInfo());
                }
                break;
        }
    }

    private void setSelectedItemsAdapter() {
        if (listItems.size() > 0) {
            binding.routOrderList.setVisibility(View.VISIBLE);
            binding.rvSelectedItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            binding.rvSelectedItems.setHasFixedSize(true);
            adapter = new ServiceSelectedItemsTitleListAdapter(mContext, listItems, this, false);
            binding.rvSelectedItems.setAdapter(adapter);
        } else {
            binding.routOrderList.setVisibility(View.GONE);
        }
    }

    public Observer orderResourcesResponse() {
        return (Observer<OrderResourcesResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setOrderData(response);
                    binding.edtSelectTime.setText("");
                    binding.edtSelectDeliverTime.setText("");
                    dashboardViewModel.getSaveOrderRequest().setPickup_hour_id(0);
                    dashboardViewModel.getSaveOrderRequest().setDeliver_hour_id(0);
                    if (getOrderData().getPickup_hours() != null && getOrderData().getInfo().getId() != 0) {
                        binding.txtAddress.setText(getOrderData().getInfo().getAddress());
                        dashboardViewModel.getSaveOrderRequest().setAddress(getOrderData().getInfo().getAddress());
                        dashboardViewModel.getSaveOrderRequest().setAddress_id(getOrderData().getInfo().getId());
                    } else {
                        binding.txtAddress.setText("");
                        dashboardViewModel.getSaveOrderRequest().setAddress("");
                        dashboardViewModel.getSaveOrderRequest().setAddress_id(0);
                    }
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Observer getAllClientsResponse() {
        return (Observer<ModuleResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setClientsData(response);

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
        Log.e("test", "rootPosition:" + rootPosition + "  " + "itemPosition:" + itemPosition + "  " + "quantity:" + quantity);
        listItems.get(rootPosition).getServiceList().get(itemPosition).setQuantity(quantity);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (view.getTag().toString().equals(DATE_PICKER)) {
            Calendar dobDate = Calendar.getInstance();
            dobDate.set(year, month, day);
            SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatsConstants.DD_MMMM_YYYY_SPACE, Locale.US);
            binding.edtSelectDate.setText(dateFormat.format(dobDate.getTime()));
            SimpleDateFormat dateFormat1 = new SimpleDateFormat(DateFormatsConstants.YYYY_MM_DD_DASH, Locale.US);
            dashboardViewModel.getSaveOrderRequest().setPickup_date(dateFormat1.format(dobDate.getTime()));

            binding.edtSelectDeliverDate.setText("");
            dashboardViewModel.getSaveOrderRequest().setDeliver_date("");

            binding.edtSelectDeliverTime.setText("");
            dashboardViewModel.getSaveOrderRequest().setDeliver_hour_id(0);

        } else if (view.getTag().toString().equals(DELIVER_DATE_PICKER)) {
            Calendar dobDate = Calendar.getInstance();
            dobDate.set(year, month, day);
            SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatsConstants.DD_MMMM_YYYY_SPACE, Locale.US);
            binding.edtSelectDeliverDate.setText(dateFormat.format(dobDate.getTime()));
            SimpleDateFormat dateFormat1 = new SimpleDateFormat(DateFormatsConstants.YYYY_MM_DD_DASH, Locale.US);
            dashboardViewModel.getSaveOrderRequest().setDeliver_date(dateFormat1.format(dobDate.getTime()));
        }
    }

    @Override
    public void onSelectTime(String fromTime, String toTime, int identifier) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        binding.edtSelectTime.setText(String.format(getString(R.string.lbl_display_time), fromTime, toTime));
    }

    public void showDatePicker(long minDate, long maxDate, String tag, String selDate) {
        DialogFragment newFragment = DatePickerFragment.newInstance(minDate, maxDate, selDate, DateFormatsConstants.DD_MM_YYYY_DASH, tag);
        newFragment.show(getSupportFragmentManager(), tag);
    }

    public void showSelectTimeDialog(int dialogIdentifier, View v) {
        List<ModuleInfo> list = new ArrayList<>();
        ModuleInfo moduleInfo = null;
        for (PickUpTimeInfo info : getOrderData().getPickup_hours()) {
            moduleInfo = new ModuleInfo();
            moduleInfo.setId(info.getId());
            moduleInfo.setName(String.format(getString(R.string.lbl_display_time), info.getStart_time(), info.getEnd_time()));
            list.add(moduleInfo);
        }
        PopupMenuHelper.showPopupMenu(mContext, v, list, dialogIdentifier);
    }

    public void showClientListDialog(int type, String title, List<ModuleInfo> list) {
        FragmentManager fm = getSupportFragmentManager();
        SelectClientDialog dropdownDialogFragment = SelectClientDialog.newInstance(mContext, title, list, type);
        dropdownDialogFragment.show(fm, "SelectClientDialog");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConstant.IntentKey.SELECT_ADDRESS:
                if (resultCode == 1 && data != null) {
                    dashboardViewModel.getSaveOrderRequest().setClient_id(clientId);
                    int addressId = data.getIntExtra(AppConstant.IntentKey.ADDRESS_ID, 0);
                    dashboardViewModel.getOrderResourcesRequest(addressId);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ModuleSelection moduleInfo) {
        if (moduleInfo != null) {
            if (moduleInfo.getType() == AppConstant.DialogIdentifier.SELECT_TIME) {
                binding.edtSelectTime.setText(moduleInfo.getInfo().getName());
                dashboardViewModel.getSaveOrderRequest().setPickup_hour_id(moduleInfo.getInfo().getId());
                dashboardViewModel.getSaveOrderRequest().setPickup_hour(moduleInfo.getInfo().getName());
            } else if (moduleInfo.getType() == AppConstant.DialogIdentifier.SELECT_DELIVER_TIME) {
                binding.edtSelectDeliverTime.setText(moduleInfo.getInfo().getName());
                dashboardViewModel.getSaveOrderRequest().setDeliver_hour_id(moduleInfo.getInfo().getId());
                dashboardViewModel.getSaveOrderRequest().setDeliver_hour(moduleInfo.getInfo().getName());
            } else if (moduleInfo.getType() == AppConstant.DialogIdentifier.SELECT_CLIENT) {
                clientId = moduleInfo.getInfo().getId();
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstant.IntentKey.USER_ID, moduleInfo.getInfo().getId());
                moveActivityForResult(mContext, SelectAddressActivity.class, false, false, AppConstant.IntentKey.SELECT_ADDRESS, bundle);
            }
        }
    }

    private boolean validate() {
        boolean valid = true;
        if (ValidationUtil.isEmptyEditText(dashboardViewModel.getSaveOrderRequest().getPickup_date())) {
            ToastHelper.error(mContext, getString(R.string.error_empty_order_date), Toast.LENGTH_LONG, false);
            valid = false;
            return valid;
        }
        if (dashboardViewModel.getSaveOrderRequest().getPickup_hour_id() == 0) {
            ToastHelper.error(mContext, getString(R.string.error_empty_order_time), Toast.LENGTH_LONG, false);
            valid = false;
            return valid;
        }
        if (ValidationUtil.isEmptyEditText(dashboardViewModel.getSaveOrderRequest().getDeliver_date())) {
            ToastHelper.error(mContext, getString(R.string.error_empty_delivery_date), Toast.LENGTH_LONG, false);
            valid = false;
            return valid;
        }
        if (dashboardViewModel.getSaveOrderRequest().getDeliver_hour_id() == 0) {
            ToastHelper.error(mContext, getString(R.string.error_empty_delivery_time), Toast.LENGTH_LONG, false);
            valid = false;
            return valid;
        }
        if (dashboardViewModel.getSaveOrderRequest().getAddress_id() == 0) {
            ToastHelper.error(mContext, getString(R.string.error_empty_address), Toast.LENGTH_LONG, false);
            valid = false;
            return valid;
        }

        if (orderType == 0) {
            List<ServiceItemInfo> order = new ArrayList<>();
            for (int i = 0; i < listItems.size(); i++) {
                for (int j = 0; j < listItems.get(i).getServiceList().size(); j++) {
                    ServiceItemInfo info = listItems.get(i).getServiceList().get(j);
                    if (info.getQuantity() > 0) {
                        ServiceItemInfo serviceItemInfo = new ServiceItemInfo(listItems.get(i).getId(), info.getId(), info.getQuantity());
                        order.add(serviceItemInfo);
                    }
                }
            }
            dashboardViewModel.getSaveOrderRequest().setOrder(order);

            if (dashboardViewModel.getSaveOrderRequest().getOrder().size() == 0) {
                ToastHelper.error(mContext, getString(R.string.error_select_at_least_one_item), Toast.LENGTH_LONG, false);
                valid = false;
                return valid;
            }
        }

        return valid;
    }

    public OrderResourcesResponse getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderResourcesResponse orderData) {
        this.orderData = orderData;
    }

    public ModuleResponse getClientsData() {
        return clientsData;
    }

    public void setClientsData(ModuleResponse clientsData) {
        this.clientsData = clientsData;
    }
}
