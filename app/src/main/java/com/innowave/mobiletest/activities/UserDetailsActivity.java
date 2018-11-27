package com.innowave.mobiletest.activities;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.innowave.mobiletest.R;
import com.innowave.mobiletest.adapters.UsersAdapter;
import com.innowave.mobiletest.apis.RestCaller;
import com.innowave.mobiletest.apis.RetrofitClient;
import com.innowave.mobiletest.application.BaseActivity;
import com.innowave.mobiletest.callbacks.ResponseHandler;
import com.innowave.mobiletest.models.UsersResponse.Item;
import com.innowave.mobiletest.utils.Config;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsActivity extends BaseActivity implements View.OnClickListener, ResponseHandler {
    private ImageButton ibBack;
    private CircleImageView ivUser;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvNoResult;
    private TextView tvFollowers;
    private RecyclerView rvFollowers;
    private ProgressBar progress;
    private ProgressBar progress1;
    private String user;
    private UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        adapter = new UsersAdapter(this);
        ibBack = findViewById(R.id.ibBack);
        ivUser = findViewById(R.id.ivUser);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvFollowers = findViewById(R.id.tvFollowers);
        tvNoResult = findViewById(R.id.tvNoResult);
        rvFollowers = findViewById(R.id.rvFollowers);
        progress = findViewById(R.id.progress);
        progress1 = findViewById(R.id.progress1);
        ibBack.setOnClickListener(this);
        if (getIntent().hasExtra("user")) {
            user = getIntent().getStringExtra("user");
            progress1.setVisibility(View.VISIBLE);
            progress.setVisibility(View.VISIBLE);
            new RestCaller(this, this, RetrofitClient.getInstance().getUserDetails(user, Config.AUTH_TOKEN), 1);
            new RestCaller(this, this, RetrofitClient.getInstance().getFollowers(user, Config.AUTH_TOKEN), 2);
        }
    }

    private void setData(Item user) {
        if (user.getName() != null && !user.getName().isEmpty())
            tvName.setText(user.getName());
        else
            tvName.setText(user.getLogin());
        Glide.with(this).load(user.getAvatarUrl()).apply(new RequestOptions()
                .placeholder(R.drawable.ic_user).error(R.drawable.ic_user)
                .centerCrop()).into(ivUser);
        tvFollowers.setText(user.getFollowers() + " Followers");
        if (user.getEmail() != null && !user.getEmail().isEmpty())
            tvEmail.setText(user.getEmail());
        else
            tvEmail.setText("Email Not Available");
    }

    private void setFollowersData(List<Item> items) {
        adapter.addData(items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvFollowers.setLayoutManager(layoutManager);
        rvFollowers.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvFollowers.getContext(),
                layoutManager.getOrientation());
        rvFollowers.addItemDecoration(dividerItemDecoration);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public <T> void onSuccess(Object response, int reqCode) {
        if (reqCode == 1) {
            progress1.setVisibility(View.GONE);
            Item results = (Item) response;
            if (results != null) {
                setData(results);
            }
        } else if (reqCode == 2) {
            progress.setVisibility(View.GONE);
            List<Item> results = (List<Item>) response;
            if (results != null && results.size() > 0) {
                setFollowersData(results);
            } else {
                tvNoResult.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onFailure(Throwable t, int reqCode) {

        progress.setVisibility(View.GONE);
        progress1.setVisibility(View.GONE);
        t.printStackTrace();
    }
}
