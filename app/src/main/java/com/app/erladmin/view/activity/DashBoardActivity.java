package com.app.erladmin.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erladmin.ERLApp;
import com.app.erladmin.R;
import com.app.erladmin.databinding.ActivityDashboardBinding;
import com.app.erladmin.model.entity.response.BaseResponse;
import com.app.erladmin.util.AppConstant;
import com.app.erladmin.util.AppUtils;
import com.app.erladmin.util.LoginViewModelFactory;
import com.app.erladmin.util.ResourceProvider;
import com.app.erladmin.view.fragment.ChatFragment;
import com.app.erladmin.view.fragment.ClientsFragment;
import com.app.erladmin.view.fragment.DashboardFragment;
import com.app.erladmin.view.fragment.OrdersFragment;
import com.app.erladmin.viewModel.UserAuthenticationViewModel;
import com.app.utilities.callbacks.DialogButtonClickListener;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.StringHelper;

public class DashBoardActivity extends BaseActivity implements View.OnClickListener, DialogButtonClickListener {
    private ActivityDashboardBinding binding;
    private UserAuthenticationViewModel userAuthenticationViewModel;
    private Context mContext;
    private ActionBarDrawerToggle toggle;
    private int tabType = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        setTransparentStatusBar();
        mContext = this;
        userAuthenticationViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(UserAuthenticationViewModel.class);
        userAuthenticationViewModel.createView(this);
        userAuthenticationViewModel.mBaseResponse()
                .observe(this, logoutResponse());

        setSupportActionBar(binding.appBarLayout.toolbar);
        setupToolbar(getString(R.string.dashboard), false);

        toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBarLayout.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navHeader.routDashBoard.setOnClickListener(this);
        binding.navHeader.routClients.setOnClickListener(this);
        binding.navHeader.routOrders.setOnClickListener(this);
        binding.navHeader.routChat.setOnClickListener(this);
        binding.navHeader.txtLogout.setOnClickListener(this);

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra(AppConstant.IntentKey.TYPE)) {
            tabType = getIntent().getIntExtra(AppConstant.IntentKey.TYPE, 0);
            switch (tabType) {
                case AppConstant.Type.ORDERS_TAB:
                    binding.navHeader.routOrders.setBackgroundColor(mContext.getResources().getColor(R.color.colorNavigationItemSelected));
                    binding.appBarLayout.toolBarNavigation.setText(getString(R.string.orders));
                    addFragment(R.id.container, OrdersFragment.newInstance(), false);
                    break;
            }
        } else {
            binding.navHeader.routDashBoard.setBackgroundColor(mContext.getResources().getColor(R.color.colorNavigationItemSelected));
            binding.appBarLayout.toolBarNavigation.setText(getString(R.string.dashboard));
            addFragment(R.id.container, DashboardFragment.newInstance(), false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.routDashBoard:
                resetNavigationDrawerItemsBg();
                binding.navHeader.routDashBoard.setBackgroundColor(mContext.getResources().getColor(R.color.colorNavigationItemSelected));
                binding.appBarLayout.toolBarNavigation.setText(getString(R.string.dashboard));
                replaceFragment(R.id.container, DashboardFragment.newInstance(), false);
                break;
            case R.id.routClients:
                resetNavigationDrawerItemsBg();
                binding.navHeader.routClients.setBackgroundColor(mContext.getResources().getColor(R.color.colorNavigationItemSelected));
                binding.appBarLayout.toolBarNavigation.setText(getString(R.string.clients));
                replaceFragment(R.id.container, ClientsFragment.newInstance(), false);
                break;
            case R.id.routOrders:
                resetNavigationDrawerItemsBg();
                binding.navHeader.routOrders.setBackgroundColor(mContext.getResources().getColor(R.color.colorNavigationItemSelected));
                binding.appBarLayout.toolBarNavigation.setText(getString(R.string.orders));
                replaceFragment(R.id.container, OrdersFragment.newInstance(), false);
                break;
            case R.id.routChat:
                resetNavigationDrawerItemsBg();
                binding.navHeader.routChat.setBackgroundColor(mContext.getResources().getColor(R.color.colorNavigationItemSelected));
                binding.appBarLayout.toolBarNavigation.setText(getString(R.string.chat));
                replaceFragment(R.id.container, ChatFragment.newInstance(), false);
                break;
            case R.id.txtLogout:
                AlertDialogHelper.showDialog(mContext, "", getString(R.string.logout_msg), getString(R.string.yes), getString(R.string.no), false, this, AppConstant.DialogIdentifier.LOGOUT);
                break;
        }
        closeDrawer();
    }


    public void setupHomeButton(boolean isBackEnable, String title, boolean isClearTitle) {
        if (isBackEnable) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        } else {
            toggle = new ActionBarDrawerToggle(
                    this, binding.drawerLayout, binding.appBarLayout.toolbar
                    , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            toggle.setDrawerIndicatorEnabled(false);

            toggle.setToolbarNavigationClickListener(view -> binding.drawerLayout.openDrawer(GravityCompat.START));
            toggle.setHomeAsUpIndicator(R.drawable.ic_navigation_menu);
            binding.drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }

        TextView txtTitle = findViewById(R.id.toolBarNavigation);
        if (txtTitle != null) {
            if (!StringHelper.isEmpty(title))
                txtTitle.setText(title);
            else if (isClearTitle)
                txtTitle.setText("");
        }
    }

    public Observer logoutResponse() {
        return (Observer<BaseResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    ERLApp.get().clearData();
                    moveActivity(mContext, LoginActivity.class, true, true, null);
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
            }
        };
    }

    public void navigationItemClick(int id) {
        switch (id) {
            case R.id.routDashBoard:
                break;
            case R.id.routClients:
                break;
        }
    }

    public void closeDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void resetNavigationDrawerItemsBg() {
        binding.navHeader.routDashBoard.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routOffers.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routServiceItems.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routCity.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routServiceArea.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routOperatingHours.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routServiceCategories.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routServiceHours.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routItemMapper.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routClients.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routRiders.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routOrders.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routChat.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routOurService.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routStoreLocator.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
        binding.navHeader.routStaticPages.setBackgroundResource(R.drawable.img_navigation_drawer_item_bg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier) {
        if (dialogIdentifier == AppConstant.DialogIdentifier.LOGOUT) {
            userAuthenticationViewModel.logoutRequest();
        }
    }

    @Override
    public void onNegativeButtonClicked(int dialogIdentifier) {

    }

}
