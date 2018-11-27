package com.innowave.mobiletest.activities;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.innowave.mobiletest.R;
import com.innowave.mobiletest.adapters.UsersAdapter;
import com.innowave.mobiletest.apis.RestCaller;
import com.innowave.mobiletest.apis.RetrofitClient;
import com.innowave.mobiletest.application.BaseActivity;
import com.innowave.mobiletest.callbacks.ResponseHandler;
import com.innowave.mobiletest.models.UsersResponse.Item;
import com.innowave.mobiletest.models.UsersResponse.UsersResults;
import com.innowave.mobiletest.utils.Config;

import java.util.List;

import retrofit2.http.Path;

public class DashboardActivity extends BaseActivity implements ResponseHandler {
    private SearchView searchView;
    private ProgressBar progress;
    private RecyclerView rvUsers;
    private UsersAdapter adapter;
    private TextView tvNoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        progress = findViewById(R.id.progress);
        searchView = findViewById(R.id.searchView);
        rvUsers = findViewById(R.id.rvUsers);
        tvNoResult = findViewById(R.id.tvNoResult);
        adapter=new UsersAdapter(this);
        searchView.setActivated(true);
        searchView.setQueryHint("Search user...");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchUser(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void searchUser(String keyword) {
        progress.setVisibility(View.VISIBLE);
        new RestCaller(this, this, RetrofitClient.getInstance().getUsers(keyword, Config.AUTH_TOKEN), 1);
    }
    private void setData(List<Item> items){
        tvNoResult.setVisibility(View.GONE);
        adapter.addData(items);
        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        rvUsers.setLayoutManager(layoutManager);
        rvUsers.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvUsers.getContext(),
                layoutManager.getOrientation());
        rvUsers.addItemDecoration(dividerItemDecoration);

    }

    @Override
    public <T> void onSuccess(Object response, int reqCode) {
        progress.setVisibility(View.GONE);
        if (reqCode == 1) {
            UsersResults results = (UsersResults) response;
            if(results.getItems()!=null && results.getItems().size()>0){
                setData(results.getItems());
            }
            else{
                adapter.clearData();
                tvNoResult.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onFailure(Throwable t, int reqCode) {
        progress.setVisibility(View.GONE);
        t.printStackTrace();
    }
}
