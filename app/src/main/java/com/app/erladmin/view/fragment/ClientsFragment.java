package com.app.erladmin.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erladmin.R;
import com.app.erladmin.adapter.ClientsAdapter;
import com.app.erladmin.databinding.FragmentClientsBinding;
import com.app.erladmin.model.entity.info.ClientInfo;
import com.app.erladmin.model.entity.response.ClientsResponse;
import com.app.erladmin.util.AppConstant;
import com.app.erladmin.util.AppUtils;
import com.app.erladmin.util.LoginViewModelFactory;
import com.app.erladmin.util.ResourceProvider;
import com.app.erladmin.viewModel.DashboardViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.StringHelper;

import java.util.List;

public class ClientsFragment extends BaseFragment implements View.OnClickListener {
    private final int LAYOUT_ACTIVITY = R.layout.fragment_clients;
    private FragmentClientsBinding binding;
    private Context mContext;
    private DashboardViewModel dashboardViewModel;
    private int pastVisibleItems, visibleItemCount, offset = 0, totalItemCount = 0;
    private boolean loading = true, mIsLastPage = false;
    private String searchUser = "";
    private ClientsResponse ClientsData;
    private ClientsAdapter adapter;

    public static final ClientsFragment newInstance() {
        return new ClientsFragment();
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

        dashboardViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(DashboardViewModel.class);
        dashboardViewModel.createView(this);
        dashboardViewModel.mClientsResponse()
                .observe(this, getClientsResponse());
        loadData(true);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            searchUser = "";
            loadData(false);
        });

        binding.imgSearch.setOnClickListener(v -> {
            searchUser();
        });

        binding.txtClearSearch.setOnClickListener(v -> {
            searchUser = "";
            binding.edtSearch.setText("");
            loadData(true);
        });

        binding.edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchUser();
                return true;
            }
            return false;
        });

        return binding.getRoot();
    }

    public void loadData(boolean isProgress) {
        dashboardViewModel.getClientsRequest(isProgress, AppConstant.DATA_PER_PAGE, offset, searchUser);
    }

    public void searchUser() {
        if (!StringHelper.isEmpty(binding.edtSearch.getText().toString().trim())) {
            offset = 0;
            searchUser = binding.edtSearch.getText().toString().trim();
            loadData(true);
        }
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

    private void setAdapter(List<ClientInfo> list) {
        if (list != null && list.size() > 0) {
            binding.rvClientsList.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            binding.rvClientsList.setLayoutManager(linearLayoutManager);
            binding.rvClientsList.setHasFixedSize(true);
            adapter = new ClientsAdapter(mContext, list);
            binding.rvClientsList.setAdapter(adapter);
            recyclerViewScrollListener(linearLayoutManager);
        } else {
            binding.rvClientsList.setVisibility(View.GONE);
            adapter = null;
        }
    }

    private void recyclerViewScrollListener(LinearLayoutManager layoutManager) {
        binding.rvClientsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            if (!mIsLastPage) {
                                loading = false;
                                binding.loadMore.setVisibility(View.VISIBLE);
                                loadData(false);
                            }
                        }
                    }
                }
            }
        });
    }

    public Observer getClientsResponse() {
        return (Observer<ClientsResponse>) response -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            binding.loadMore.setVisibility(View.GONE);
            if (!StringHelper.isEmpty(searchUser))
                binding.txtClearSearch.setVisibility(View.VISIBLE);
            else
                binding.txtClearSearch.setVisibility(View.GONE);
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setClientsData(response);
                    if (offset == 0) {
                        setAdapter(response.getInfo());
                    } else if (response.getInfo() != null && !response.getInfo().isEmpty()) {
                        if (adapter != null) {
                            adapter.addData(response.getInfo());
                            binding.loadMore.setVisibility(View.GONE);
                            loading = true;
                        }
                    } else if (response.getOffset() == 0) {
                        binding.loadMore.setVisibility(View.GONE);
                        loading = true;
                    }
                    offset = response.getOffset();

                    if (offset == 0)
                        mIsLastPage = true;
                    else
                        mIsLastPage = false;
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {

            }
        };
    }

    public ClientsResponse getClientsData() {
        return ClientsData;
    }

    public void setClientsData(ClientsResponse clientsData) {
        ClientsData = clientsData;
    }
}
