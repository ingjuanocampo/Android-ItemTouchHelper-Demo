package co.paulburke.android.itemtouchhelperdemo.swipeable_adapter;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import co.paulburke.android.itemtouchhelperdemo.helper.ItemTouchHelperAdapter;
import co.paulburke.android.itemtouchhelperdemo.swpeableviewholder.SwipeableViewHolder;

/**
 * Created by juan.ocampo on 05/08/2016.
 */

public class SwipeableHelperCallback extends ItemTouchHelper.Callback {

    private static final int NO_POSITION_CACHED = -1;

    private boolean isSwiped;
    private Map<RecyclerView.ViewHolder, Integer> viewHolderDxMap;
    private float currentDxTranslation;
    private RecyclerView.ViewHolder lastSelectedViewHolder;
    private final ItemTouchHelperAdapter adapter;


    private static int lastSelectedPosition = NO_POSITION_CACHED;

    public SwipeableHelperCallback(ItemTouchHelperAdapter adapter) {
        this.adapter = adapter;
        this.viewHolderDxMap = new HashMap<>();
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        if (!isSwiped && viewHolder instanceof SwipeableViewHolder) {
            isSwiped = true;
            lastSelectedPosition = lastSelectedViewHolder.getAdapterPosition();
            adapter.onItemDismiss(lastSelectedPosition);
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
                viewHolderDxMap.put(viewHolder, (int) dX);
                makeParallaxAnimation(dX, viewHolder);
            }
            Log.e("dx, dy", " "+ dX + " " + dY);
        }
    }

    private void makeParallaxAnimation(float dX, RecyclerView.ViewHolder viewHolderDragging) {
        for (RecyclerView.ViewHolder viewHolderAnimate : adapter.getSwipeableViewHolders()) {
            if (viewHolderAnimate != null && viewHolderAnimate != viewHolderDragging) {
                viewHolderAnimate.itemView.setTranslationX(dX/10);
            }
        }
    }

    private float getSwipeXTranslation(float dX) {
        return dX < 0 ? dX/5 : 0;
    }


    private boolean isDxProportionalToTheLastState(RecyclerView.ViewHolder lastViewHolder, int newDx) {
        return viewHolderDxMap.containsKey(lastViewHolder) ?  Math.abs(viewHolderDxMap.get(lastViewHolder) - newDx) < 350 : true;
    }

}
