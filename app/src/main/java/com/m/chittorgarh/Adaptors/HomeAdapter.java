/*
 * Copyright 2015 Bartosz Lipinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.m.chittorgarh.Adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.m.chittorgarh.Halper.FormattedTextView;
import com.m.chittorgarh.Model.HomeSetter;
import com.m.chittorgarh.R;


import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<HomeSetter> products = new ArrayList<>();
    public HomeAdapter(Context context, ArrayList<HomeSetter> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_two, parent, false);
        return new itemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeSetter setter = products.get(position);
        itemViewHolder itemViewHolder = (itemViewHolder)holder;
        int displayWidth = context.getResources().getDisplayMetrics().heightPixels;
        itemViewHolder.benners.getLayoutParams().height = (int) (displayWidth / 4.2);
        Glide.with(context)
                .load(setter.getProductImage())
                .placeholder(R.drawable.ic_all_inclusive)
                .error(R.drawable.ic_all_inclusive)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemViewHolder.benners);
        itemViewHolder.producName.setText(setter.getProductName());
    }

    @Override
    public int getItemCount() {
        return  products.size();
    }
    private static class itemViewHolder extends RecyclerView.ViewHolder {
        ImageView benners;
        FormattedTextView producName;
        public itemViewHolder(View view) {
            super(view);
            benners = (ImageView)view.findViewById(R.id.banner);
            producName = (FormattedTextView)view.findViewById(R.id.productName);
        }
    }
}