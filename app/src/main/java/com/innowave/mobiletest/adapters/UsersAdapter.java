package com.innowave.mobiletest.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.innowave.mobiletest.R;
import com.innowave.mobiletest.activities.UserDetailsActivity;
import com.innowave.mobiletest.models.UsersResponse.Item;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class UsersAdapter extends
        RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<Item> listItems;
    private Context ctx;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_user, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    public UsersAdapter(Context context) {
        ctx = context;

    }

    public void addData(List<Item> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            final Item user = listItems.get(position);
            holder.tvName.setText(user.getLogin());
            Glide.with(ctx).load(user.getAvatarUrl()).apply(new RequestOptions()
                    .placeholder(R.drawable.ic_user).error(R.drawable.ic_user)
                    .centerCrop()).into(holder.ivUser);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx, UserDetailsActivity.class);
                    intent.putExtra("user", user.getLogin());
                    ctx.startActivity(intent);
                    if(ctx instanceof UserDetailsActivity)
                        ((UserDetailsActivity) ctx).finish();
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void clearData() {
        if (listItems != null) {
            listItems.clear();
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView ivUser;
        public TextView tvName;
        public TextView tvComment;
        public TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ivUser = itemView.findViewById(R.id.ivUser);
            tvName = itemView.findViewById(R.id.tvName);

        }

        @Override
        public void onClick(View v) {

        }
    }

}
