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

package co.paulburke.android.itemtouchhelperdemo.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.paulburke.android.itemtouchhelperdemo.swpeableviewholder.SwipeableViewHolder;

/**
 * Interface to listen for a move or dismissal event from a {@link ItemTouchHelper.Callback}.
 *
 * @author Paul Burke (ipaulpro)
 */
public abstract class ItemTouchHelperAdapter extends RecyclerView.Adapter {

    public interface SwipeAdapterActions {
        void swiped(int position);
    }


    private final SimpleItemTouchHelperCallback callback;
    private SwipeAdapterActions listener;
    private List<RecyclerView.ViewHolder> swipeableViewHolders;


    public ItemTouchHelperAdapter(SwipeAdapterActions listener, RecyclerView recyclerView) {
        callback = new SimpleItemTouchHelperCallback(this);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        this.listener = listener;
        this.swipeableViewHolders = new ArrayList<>();
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder itemViewHolder = onCreateSwipeViewHolder(parent, viewType);
        swipeableViewHolders.add(itemViewHolder);
        return itemViewHolder;
    }

    @Override
    public final void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        onBindSwipeViewHolder(holder, position);
        if (position == callback.getLastSelectedPosition()) {
            restoreSwipedItem();
        }
    }

    protected abstract void onBindSwipeViewHolder(RecyclerView.ViewHolder holder, int position);


    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract RecyclerView.ViewHolder onCreateSwipeViewHolder(ViewGroup parent, int viewType);

    /**
     * Called when an item has been dismissed by a swipe.<br/>
     * <br/>
     * Implementations should call {@link RecyclerView.Adapter#notifyItemRemoved(int)} after
     * adjusting the underlying data to reflect this removal.
     *
     * @param position The position of the item dismissed.
     *
     * @see RecyclerView#getAdapterPositionFor(RecyclerView.ViewHolder)
     * @see RecyclerView.ViewHolder#getAdapterPosition()
     */
    protected void onItemDismiss(int position) {
        listener.swiped(position);
    }


    private void restoreSwipedItem() {
        RecyclerView.ViewHolder viewHolder = swipeableViewHolders.get(callback.getLastSelectedPosition());

        if (viewHolder != null && viewHolder instanceof SwipeableViewHolder) {
            SwipeableViewHolder swipeableViewHolder = (SwipeableViewHolder) viewHolder;
            int startPos = -swipeableViewHolder.swipeableMainContainer.getWidth();
            swipeableViewHolder.swipeableMainContainer.setTranslationX(startPos);
            for (int dx = startPos; dx <= 0 ; dx++ ) {
                swipeableViewHolder.swipeableMainContainer.setTranslationX(dx);
            }
        }

    }
}
