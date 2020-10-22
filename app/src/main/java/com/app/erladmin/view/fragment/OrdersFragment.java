package com.app.erladmin.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erladmin.R;
import com.app.erladmin.databinding.FragmentClientsBinding;
import com.app.erladmin.databinding.FragmentOrdersBinding;
import com.app.erladmin.util.AppConstant;
import com.app.erladmin.view.activity.PriceListActivity;

import org.parceler.Parcels;

public class OrdersFragment extends BaseFragment implements View.OnClickListener {
    private final int LAYOUT_ACTIVITY = R.layout.fragment_orders;
    private FragmentOrdersBinding binding;
    private Context mContext;

    public static final OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, LAYOUT_ACTIVITY, container, false);
        mContext = getActivity();
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
//            case R.id.txtStartWork:
//                startStopWork();
//                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dashboard_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(mContext, PriceListActivity.class);
            startActivityForResult(intent, AppConstant.IntentKey.PRICE_LIST);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("test", "onActivityResult OrdersFragment");
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConstant.IntentKey.PRICE_LIST:
                Log.e("test", "PRICE_LIST");
                if (resultCode == 1) {

                }
                break;
        }
    }
}
