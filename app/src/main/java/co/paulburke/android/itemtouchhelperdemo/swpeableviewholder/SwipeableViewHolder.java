package co.paulburke.android.itemtouchhelperdemo.swpeableviewholder;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import co.paulburke.android.itemtouchhelperdemo.R;
import co.paulburke.android.itemtouchhelperdemo.helper.ItemTouchHelperViewHolder;

/**
 * Created by juan.ocampo on 29/07/2016.
 */

public abstract class SwipeableViewHolder extends RecyclerView.ViewHolder implements
        ItemTouchHelperViewHolder {

    public final View swipeableViewLayout;
    public final View swipeableMainContainer;

    public SwipeableViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.swipeable_item_indicator, parent, false));

        swipeableViewLayout = itemView.findViewById(R.id.swipeable_layout);
        swipeableMainContainer = itemView.findViewById(R.id.swipeable_parent_container);

        FrameLayout parentContainer = (FrameLayout) itemView.findViewById(R.id.swipeable_container);
        View swipeableViewIndicatorParent = LayoutInflater.from(itemView.getContext()).inflate(getViewHolderType(), parentContainer, false);
        parentContainer.addView(swipeableViewIndicatorParent);
    }

    protected abstract @LayoutRes int getViewHolderType();

    @Override
    public void onRestoreItem() {

        int startPos = -swipeableMainContainer.getWidth();
        swipeableMainContainer.setTranslationX(startPos);
        for (int dx = startPos; dx <= 0 ; dx++ ) {
            swipeableMainContainer.setTranslationX(dx);
        }
    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(0);
    }

}
