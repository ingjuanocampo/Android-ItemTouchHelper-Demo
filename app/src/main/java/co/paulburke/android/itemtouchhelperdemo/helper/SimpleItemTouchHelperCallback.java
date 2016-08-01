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

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import co.paulburke.android.itemtouchhelperdemo.swpeableviewholder.SwipeableViewHolder;

/**
 * An implementation of {@link ItemTouchHelper.Callback} that enables basic drag & drop and
 * swipe-to-dismiss. Drag events are automatically started by an item long-press.<br/>
 * </br/>
 * Expects the <code>RecyclerView.Adapter</code> to listen for {@link
 * ItemTouchHelperAdapter} callbacks and the <code>RecyclerView.ViewHolder</code> to implement
 * {@link ItemTouchHelperViewHolder}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {


    private final ItemTouchHelperAdapter mAdapter;
    private float currentDxTranslation;
    private Canvas canvas;
    private RecyclerView recycler;
    private boolean isSwiped;
    private Map<RecyclerView.ViewHolder, Integer> viewHolderDxMap;
    private RecyclerView.ViewHolder lastSelectedViewHolder;
    private static int lastSelectedPosition;


    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
        this.viewHolderDxMap = new HashMap<>();
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Set movement flags based on the layout manager
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        // Notify the adapter of the dismissal


        /*for (int dx = viewHolderDxMap.get(viewHolder); dx <= 0 ; dx = dx + ) {
            onChildDraw(canvas, recycler, viewHolder, dx, 0, ItemTouchHelper.ACTION_STATE_SWIPE, false);
        }*/

        //clearView(recycler, viewHolder);

        if (!isSwiped) {
            isSwiped = true;
            lastSelectedPosition = lastSelectedViewHolder.getAdapterPosition();
            mAdapter.onItemDismiss(lastSelectedPosition);
        }

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && viewHolder instanceof SwipeableViewHolder) {
            if (isDxProportionalToTheLastState(viewHolder, (int) dX)) {
                SwipeableViewHolder swipeableViewHolder = (SwipeableViewHolder) viewHolder;
                currentDxTranslation = getSwipeXTranslation(dX);
                swipeableViewHolder.swipeableMainContainer.setTranslationX(currentDxTranslation);
                swipeableViewHolder.swipeableViewLayout.setBackgroundColor(swipeableViewHolder.itemView.getContext().getResources().getColor(android.R
                        .color.white));
                //viewHolder.itemView.setTranslationX(getSwipeXTranslation(dX));
                viewHolderDxMap.put(viewHolder, (int) dX);
            }
            Log.e("dx, dy", " "+ dX + " " + dY);
            this.canvas = c;
            this.recycler = recyclerView;
        }
    }

    private boolean isDxProportionalToTheLastState(RecyclerView.ViewHolder lastViewHolder, int newDx) {
        return viewHolderDxMap.containsKey(lastViewHolder) ?  Math.abs(viewHolderDxMap.get(lastViewHolder) - newDx) < 350 : true;
    }

    private float getSwipeXTranslation(float dX) {
        return dX < 0 ? dX/5 : 0;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // We only want the active item to change
        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            if (lastSelectedViewHolder instanceof SwipeableViewHolder && !isSwiped) {
                // Let the view holder know that this item is being moved or dragged
                isSwiped = true;
                lastSelectedPosition = lastSelectedViewHolder.getAdapterPosition();
                mAdapter.onItemDismiss(lastSelectedPosition);
                ((SwipeableViewHolder) lastSelectedViewHolder).onRestoreItem();
            }
        }
        this.lastSelectedViewHolder = viewHolder;
        Log.w("onSelected", "actionState " + actionState);

        super.onSelectedChanged(viewHolder, actionState);
    }

    public int getLastSelectedPosition() {
        return lastSelectedPosition;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        //viewHolder.itemView.setAlpha(ALPHA_FULL);

        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            // Tell the view holder it's time to restore the idle state
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }
}
