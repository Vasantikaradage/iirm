package com.indiainsure.android.MB360.utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

public class CustomDividerItemDecoration extends DividerItemDecoration {
    private Drawable divider;
    private int lastItemPosition = -1;

    public CustomDividerItemDecoration(Context context, int orientation, @DrawableRes int drawableResId) {
        super(context, orientation);
        divider = ContextCompat.getDrawable(context, drawableResId);
    }

    public void setLastItemPosition(int lastItemPosition) {
        this.lastItemPosition = lastItemPosition;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int itemCount = parent.getAdapter().getItemCount();

        for (int i = 0; i < itemCount - 1; i++) {
            if (i != lastItemPosition) {
                View child = parent.getChildAt(i);
                if (child != null) {
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    int top = child.getBottom() + params.bottomMargin;
                    int bottom = top + divider.getIntrinsicHeight();
                    divider.setBounds(parent.getLeft(), top, parent.getRight(), bottom);
                    divider.draw(c);
                }
            }
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition < state.getItemCount() - 1 && itemPosition != lastItemPosition) {
            outRect.set(0, 0, 0, divider.getIntrinsicHeight());
        } else {
            outRect.setEmpty();
        }
    }
}
