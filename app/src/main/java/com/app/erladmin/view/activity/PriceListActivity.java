package com.app.erladmin.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.app.erladmin.R;
import com.app.erladmin.adapter.ServiceHourTypeListAdapter;
import com.app.erladmin.adapter.ServiceItemsListAdapter;
import com.app.erladmin.adapter.ViewPagerAdapter;
import com.app.erladmin.callback.SelectItemListener;
import com.app.erladmin.databinding.ActivityPriceListBinding;
import com.app.erladmin.model.entity.info.ItemInfo;
import com.app.erladmin.model.entity.info.ServiceItemInfo;
import com.app.erladmin.model.entity.response.ServiceItemsResponse;
import com.app.erladmin.util.AppConstant;
import com.app.erladmin.util.AppUtils;
import com.app.erladmin.util.LoginViewModelFactory;
import com.app.erladmin.util.ResourceProvider;
import com.app.erladmin.view.fragment.OrderItemsTabFragment;
import com.app.erladmin.viewModel.DashboardViewModel;
import com.app.utilities.callbacks.DialogButtonClickListener;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.ToastHelper;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PriceListActivity extends BaseActivity implements View.OnClickListener, SelectItemListener, DialogButtonClickListener {
    private ActivityPriceListBinding binding;
    private DashboardViewModel dashBoardViewModel;
    private Context mContext;
    private Menu mMenu;
    private ServiceItemsResponse serviceItemsData;
    private ServiceHourTypeListAdapter serviceHourTypeListAdapter;
    private ViewPagerAdapter pagerAdapter;
    private int selectedHourTypePosition = 0, totalItemCount = 0, totalPrice = 0, itemPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_price_list);
        mContext = this;
        setupToolbar(getString(R.string.lbl_price_list), true);
        dashBoardViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(DashboardViewModel.class);
        dashBoardViewModel.createView(this);
        dashBoardViewModel.serviceItemsResponse()
                .observe(this, serviceItemsResponse());
        dashBoardViewModel.getServiceItemsRequest();
        binding.routCartView.setOnClickListener(this);
        binding.txtLazyOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.routCartView:
                moveToCart();
                break;
            case R.id.txtLazyOrder:
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstant.IntentKey.SERVICE_HOUR_TYPE_ID, getServiceItemsData().getInfo().get(selectedHourTypePosition).getId());
                bundle.putInt(AppConstant.IntentKey.ORDER_TYPE, 1);
                Intent intent = new Intent(mContext, CreateOrderActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, AppConstant.IntentKey.VIEW_CART);
                break;
        }
    }

    public Observer serviceItemsResponse() {
        return (Observer<ServiceItemsResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    binding.routMainView.setVisibility(View.VISIBLE);
                    setServiceItemsData(response);
                    setServiceHourTypeListAdapter();
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private void setServiceHourTypeListAdapter() {
        if (getServiceItemsData() != null
                && getServiceItemsData().getInfo() != null
                && getServiceItemsData().getInfo().size() > 0) {
            binding.rvServiceHourType.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            binding.rvServiceHourType.setLayoutManager(linearLayoutManager);
            binding.rvServiceHourType.setHasFixedSize(true);
            serviceHourTypeListAdapter = new ServiceHourTypeListAdapter(mContext, getServiceItemsData().getInfo(), this);
            binding.rvServiceHourType.setAdapter(serviceHourTypeListAdapter);

            setupViewPager(selectedHourTypePosition);
        } else {
            binding.rvServiceHourType.setVisibility(View.GONE);
        }
    }

    private void setupViewPager(int position) {
        selectedHourTypePosition = position;
        if (getServiceItemsData() != null
                && getServiceItemsData().getInfo().get(position).getPriceList() != null
                && getServiceItemsData().getInfo().get(position).getPriceList().size() > 0) {
            binding.routPriceView.setVisibility(View.VISIBLE);
            pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            for (int i = 0; i < getServiceItemsData().getInfo().get(position).getPriceList().size(); i++) {
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstant.IntentKey.POSITION, i);
                bundle.putParcelable(AppConstant.IntentKey.SERVICE_ITEMS_DATA, Parcels.wrap(getServiceItemsData().getInfo().get(position).getPriceList().get(i).getItems()));
                pagerAdapter.addFrag(OrderItemsTabFragment.newInstance(bundle), getServiceItemsData().getInfo().get(position).getPriceList().get(i).getName());
            }
            binding.vpOrderItems.setAdapter(pagerAdapter);
            binding.vpOrderItems.setCurrentItem(0);
            binding.vpOrderItems.setOffscreenPageLimit(getServiceItemsData().getInfo().get(position).getPriceList().size());
            binding.tabs.setupWithViewPager(binding.vpOrderItems);
            binding.txtSelectedItemName.setText(getServiceItemsData().getInfo().get(position).getPriceList().get(0).getName());

            binding.vpOrderItems.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    binding.txtSelectedItemName.setText(getServiceItemsData().getInfo().get(selectedHourTypePosition).getPriceList().get(position).getName());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            setTotalItemPrice();
        } else {
            binding.routPriceView.setVisibility(View.GONE);
        }

    }

    public void setTotalItemPrice() {
        totalItemCount = 0;
        totalPrice = 0;
        for (int i = 0; i < pagerAdapter.getmFragmentList().size(); i++) {
            if (pagerAdapter.getmFragmentList().get(i) instanceof OrderItemsTabFragment) {
                ServiceItemsListAdapter adapter = ((OrderItemsTabFragment) (pagerAdapter.getmFragmentList().get(i))).getAdapter();
                if (adapter != null) {
                    for (int j = 0; j < adapter.getList().size(); j++) {
                        for (int k = 0; k < adapter.getList().get(j).getServiceList().size(); k++) {
                            if (adapter.getList().get(j).getServiceList().get(k).getQuantity() > 0) {
                                totalItemCount = totalItemCount + 1;
                                totalPrice = totalPrice + adapter.getList().get(j).getServiceList().get(k).getPrice() * adapter.getList().get(j).getServiceList().get(k).getQuantity();
                            }
                        }
                    }
                }
            }
        }

        if (mMenu != null)
            setCartCountMenu(totalItemCount);

        if (totalItemCount > 0) {
            binding.txtTotalItemCount.setText(String.valueOf(totalItemCount));
            binding.txtTotalItemCount.setVisibility(View.VISIBLE);
        } else {
            binding.txtTotalItemCount.setVisibility(View.GONE);
        }

        binding.txtTotalPrice.setText(String.format(mContext.getString(R.string.lbl_display_price), String.valueOf(totalPrice)));
    }


    @Override
    public void onSelectItem(int position, int action) {
        if (action == AppConstant.Action.SELECT_SERVICE_HOUR_TYPE) {
            itemPosition = position;
            if (totalItemCount > 0) {
                AlertDialogHelper.showDialog(mContext, null, getString(R.string.msg_clear_cart), getString(R.string.yes), getString(R.string.no), true, this, AppConstant.DialogIdentifier.CLEAR_CART);
            } else {
                serviceHourTypeListAdapter.setPosition(itemPosition);
                setupViewPager(serviceHourTypeListAdapter.getPosition());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.price_list_menu, menu);
        menu.findItem(R.id.actionArchived).setVisible(false);
        menu.findItem(R.id.actionNotification).setVisible(false);
        menu.findItem(R.id.actionSelectedItemCount).setVisible(true);
        menu.findItem(R.id.actionSearch).setVisible(false);

        if (mMenu != null)
            setCartCountMenu(totalItemCount);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionSelectedItemCount)
            moveToCart();
        return super.onOptionsItemSelected(item);
    }

    public void setCartCountMenu(int cartCount) {
        if (mMenu == null) return;
        MenuItem menuItem = mMenu.findItem(R.id.actionSelectedItemCount);
        RelativeLayout mContainer = (RelativeLayout) menuItem.setActionView(R.layout.layout_toolbar_item_count).getActionView();
        TextView mBadgeCount = mContainer.findViewById(R.id.txtItemCount);
        ImageView mIconBadge = mContainer.findViewById(R.id.icon_badge);
        mIconBadge.setImageResource(R.drawable.ic_selected_item_count);
        Drawable countIcon = getResources().getDrawable(R.drawable.img_white_circle);
        mBadgeCount.setBackground(countIcon);

        mContainer.setOnClickListener(v -> onOptionsItemSelected(menuItem));

        if (cartCount > 0) {
            mBadgeCount.setVisibility(View.VISIBLE);
            mBadgeCount.setText(String.valueOf(cartCount));
        } else {
            mBadgeCount.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier) {
        if (dialogIdentifier == AppConstant.DialogIdentifier.CLEAR_CART) {
            clearCart(serviceHourTypeListAdapter.getPosition());
            serviceHourTypeListAdapter.setPosition(itemPosition);
            setupViewPager(serviceHourTypeListAdapter.getPosition());
        }
    }

    public void clearCart(int position) {
        for (int j = 0; j < getServiceItemsData().getInfo().get(position).getPriceList().size(); j++) {
            for (int k = 0; k < getServiceItemsData().getInfo().get(position).getPriceList().get(j).getItems().size(); k++) {
                for (int l = 0; l < getServiceItemsData().getInfo().get(position).getPriceList().get(j).getItems().get(k).getServiceList().size(); l++) {
                    getServiceItemsData().getInfo().get(position).getPriceList().get(j).getItems().get(k).getServiceList().get(l).setQuantity(0);
                }
            }
        }
    }

    @Override
    public void onNegativeButtonClicked(int dialogIdentifier) {

    }

    public void moveToCart() {
        if (totalItemCount > 0) {
            List<ItemInfo> listItems = new ArrayList<>();
            for (int i = 0; i < pagerAdapter.getmFragmentList().size(); i++) {
                if (pagerAdapter.getmFragmentList().get(i) instanceof OrderItemsTabFragment) {
                    ServiceItemsListAdapter adapter = ((OrderItemsTabFragment) (pagerAdapter.getmFragmentList().get(i))).getAdapter();
                    if (adapter != null) {
                        for (int j = 0; j < adapter.getList().size(); j++) {
                            List<ServiceItemInfo> listServiceItemInfo = new ArrayList<>();
                            for (int k = 0; k < adapter.getList().get(j).getServiceList().size(); k++) {
                                if (adapter.getList().get(j).getServiceList().get(k).getQuantity() > 0) {
                                    ServiceItemInfo cartServiceItemsInfo = new ServiceItemInfo(adapter.getList().get(j).getServiceList().get(k).getId()
                                            , adapter.getList().get(j).getServiceList().get(k).getName()
                                            , adapter.getList().get(j).getServiceList().get(k).getPrice()
                                            , adapter.getList().get(j).getServiceList().get(k).getQuantity()
                                            , adapter.getList().get(j).getServiceList().get(k).getImage());
                                    listServiceItemInfo.add(cartServiceItemsInfo);
                                }
                            }
                            if (listServiceItemInfo.size() > 0) {
                                ItemInfo info = adapter.getList().get(j);
                                ItemInfo itemInfo = new ItemInfo(info.getId(), info.getLu_service_hour_type_id()
                                        , info.getLu_service_category_id(), info.getDry_clean_price()
                                        , info.getIron_dry_clean_price(), info.getIron_price(), info.getName()
                                        , info.getImage(), listServiceItemInfo);
                                listItems.add(itemInfo);
                            }
                        }
                    }
                }
            }
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConstant.IntentKey.ITEMS_LIST, Parcels.wrap(listItems));
            bundle.putInt(AppConstant.IntentKey.SERVICE_HOUR_TYPE_ID, getServiceItemsData().getInfo().get(selectedHourTypePosition).getId());
            bundle.putInt(AppConstant.IntentKey.ORDER_TYPE, 0);
            Intent intent = new Intent(mContext, CreateOrderActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, AppConstant.IntentKey.VIEW_CART);
        } else {
            ToastHelper.error(mContext, getString(R.string.error_select_at_least_one_item), Toast.LENGTH_SHORT, false);
        }
    }

    public ServiceItemsResponse getServiceItemsData() {
        return serviceItemsData;
    }

    public void setServiceItemsData(ServiceItemsResponse serviceItemsData) {
        this.serviceItemsData = serviceItemsData;
    }
}