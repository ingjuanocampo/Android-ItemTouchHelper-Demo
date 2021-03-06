/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.paulburke.android.itemtouchhelperdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.paulburke.android.itemtouchhelperdemo.helper.ItemTouchHelperAdapter;
import co.paulburke.android.itemtouchhelperdemo.helper.ItemTouchHelperViewHolder;
import co.paulburke.android.itemtouchhelperdemo.helper.SimpleItemTouchHelperCallback;
import co.paulburke.android.itemtouchhelperdemo.swpeableviewholder.SwipeableViewHolder;

/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerListAdapter extends ItemTouchHelperAdapter {



    private static final int PAR = 1;


    private final List<String> mItems = new ArrayList<>();

    private int IMPAR = 2;

    public RecyclerListAdapter(Context context, SwipeAdapterActions listener, RecyclerView recyclerView) {
        super(listener, recyclerView);


        mItems.addAll(Arrays.asList(context.getResources().getStringArray(R.array.dummy_items)));
    }

    @Override
    public void onBindSwipeViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == PAR) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.textView.setText(mItems.get(position));

        } else if (viewType == IMPAR) {
            SwipeableItemViewHolder swipeableViewHolder = (SwipeableItemViewHolder) holder;
            swipeableViewHolder.textView.setText(mItems.get(position));
        }

        if (holder instanceof SwipeableViewHolder ) {
            ((SwipeableViewHolder) holder).onRestoreItem();
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position%2==0 ? PAR : IMPAR ;
    }



    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSwipeViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        RecyclerView.ViewHolder itemViewHolder = viewType == IMPAR ? new SwipeableItemViewHolder(parent) : new ItemViewHolder(view);
        return itemViewHolder;
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class SwipeableItemViewHolder extends SwipeableViewHolder {

        public final TextView textView;
        public final ImageView handleView;

        public SwipeableItemViewHolder(ViewGroup viewGroup) {
            super(viewGroup);
            textView = (TextView) itemView.findViewById(R.id.text);
            handleView = (ImageView) itemView.findViewById(R.id.handle);
        }

        @Override
        protected int getViewHolderType() {
            return R.layout.item_main;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public final TextView textView;
        public final ImageView handleView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            handleView = (ImageView) itemView.findViewById(R.id.handle);
        }
    }




}
