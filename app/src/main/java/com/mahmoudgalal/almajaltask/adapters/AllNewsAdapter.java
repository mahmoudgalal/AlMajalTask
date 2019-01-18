/*
 * Copyright (c) 2019. Mahmoud Galal.
 */

package com.mahmoudgalal.almajaltask.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mahmoudgalal.almajaltask.R;
import com.mahmoudgalal.almajaltask.model.NewsItem;

import java.util.List;

public class AllNewsAdapter extends RecyclerView.Adapter<AllNewsAdapter.ViewHolder> {

    public List<NewsItem> getItems() {
        return items;
    }

    public void setItems(List<NewsItem> items) {
        this.items = items;
    }

    private List<NewsItem> items;

    public AllNewsAdapter(List<NewsItem> items){
        this.items = items ;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.news_item,parent,false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NewsItem item = items.get(position);
        holder.newsIdTxt.setText(item.title);
        holder.newsDateTxt.setText(item.creationDate);
        holder.detailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickedListener != null){
                    onItemClickedListener.onItemClicked(view,holder.imageView,item,items.indexOf(item));
                }
            }
        });
        Glide.with(holder.imageView.getContext()).load(item.mainImg).crossFade().
                placeholder(R.drawable.ic_launcher_background).
                error(R.drawable.image_error_background).
                centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items != null?items.size(): 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView newsIdTxt,newsDateTxt ;
        ImageView imageView,detailsView ;
        public ViewHolder(View itemView) {
            super(itemView);
            newsIdTxt = itemView.findViewById(R.id.news_id_txt);
            newsDateTxt = itemView.findViewById(R.id.item_date_txt);
            imageView = itemView.findViewById(R.id.main_image);
            detailsView = itemView.findViewById(R.id.details_view);
            newsDateTxt.setSelected(true);
            newsDateTxt.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        }
    }

    public interface OnItemClickedListener{
        void onItemClicked(View clickedView,View mainImage,NewsItem item,int position);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    private OnItemClickedListener onItemClickedListener;
}
