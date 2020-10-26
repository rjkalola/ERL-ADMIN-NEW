package com.app.erladmin.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erladmin.R;
import com.app.erladmin.adapter.ChatUserListAdapter;
import com.app.erladmin.adapter.ClientsAdapter;
import com.app.erladmin.callback.SelectItemListener;
import com.app.erladmin.databinding.FragmentChatListBinding;
import com.app.erladmin.databinding.FragmentClientsBinding;
import com.app.erladmin.model.entity.info.ChatUserInfo;
import com.app.erladmin.model.entity.info.ClientInfo;
import com.app.erladmin.model.entity.response.ChatListResponse;
import com.app.erladmin.model.entity.response.ClientsResponse;
import com.app.erladmin.util.AppConstant;
import com.app.erladmin.util.AppUtils;
import com.app.erladmin.util.LoginViewModelFactory;
import com.app.erladmin.util.ResourceProvider;
import com.app.erladmin.view.activity.ChatActivity;
import com.app.erladmin.view.activity.CreateClientActivity;
import com.app.erladmin.viewModel.DashboardViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.StringHelper;

import org.parceler.Parcels;

import java.util.List;

public class ChatFragment extends BaseFragment implements View.OnClickListener, SelectItemListener {
    private final int LAYOUT_ACTIVITY = R.layout.fragment_chat_list;
    private FragmentChatListBinding binding;
    private Context mContext;
    private DashboardViewModel dashboardViewModel;
    private ChatListResponse chatListData;
    private ChatUserListAdapter adapter;
    private int unreadMessage = 0;

    public static final ChatFragment newInstance() {
        return new ChatFragment();
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
        dashboardViewModel.chatListResponse()
                .observe(this, getChatUserResponse());
        loadData(true);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            loadData(false);
        });

        binding.imgSearch.setOnClickListener(v -> {
            binding.edtSearch.setText("");
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(binding.edtSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();
    }

    public void loadData(boolean isProgress) {
        dashboardViewModel.getChatListRequest(isProgress);
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

    public void searchUser(String charText) {
        if (adapter != null)
            adapter.filter(charText);
    }

    private void setAdapter(List<ChatUserInfo> list) {
        if (list != null && list.size() > 0) {
            binding.rvChatList.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            binding.rvChatList.setLayoutManager(linearLayoutManager);
            binding.rvChatList.setHasFixedSize(true);
            adapter = new ChatUserListAdapter(mContext, list, this);
            binding.rvChatList.setAdapter(adapter);
        } else {
            binding.rvChatList.setVisibility(View.GONE);
            adapter = null;
        }
    }

    public Observer getChatUserResponse() {
        return (Observer<ChatListResponse>) response -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            binding.loadMore.setVisibility(View.GONE);
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setChatListData(response);
                    setAdapter(response.getInfo());
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {

            }
        };
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.dashboard_menu, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_add) {
//            Intent intent = new Intent(mContext, CreateClientActivity.class);
//            startActivityForResult(intent, AppConstant.IntentKey.ADD_CLIENT);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConstant.IntentKey.VIEW_CHAT:
                if (resultCode == 1) {
                    loadData(true);
                } else {
                    if (unreadMessage > 0) {
                        unreadMessage = 0;
                        loadData(false);
                    }
                }

                break;
        }
    }

    @Override
    public void onSelectItem(int position, int action) {
        if (adapter != null) {
            unreadMessage = adapter.getList().get(position).getUn_read_msg_count();
            Intent intent = new Intent(mContext, ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstant.IntentKey.USER_ID, adapter.getList().get(position).getTo_user());
            bundle.putString(AppConstant.IntentKey.USER_NAME, adapter.getList().get(position).getName());
            intent.putExtras(bundle);
            startActivityForResult(intent, AppConstant.IntentKey.VIEW_CHAT);
        }
    }

    public ChatListResponse getChatListData() {
        return chatListData;
    }

    public void setChatListData(ChatListResponse chatListData) {
        this.chatListData = chatListData;
    }
}
