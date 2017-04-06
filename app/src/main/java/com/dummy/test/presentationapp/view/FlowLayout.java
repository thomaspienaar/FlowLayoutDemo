package com.dummy.test.presentationapp.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

public class FlowLayout extends ViewGroup {
    public interface OnItemClickListener {
        public void onItemClick(ViewGroup parent, View view, int position, long itemId);

    }

    private final int INVALID_POSITION = -1;

    private int positionClickDown = INVALID_POSITION;

    private OnItemClickListener onItemClickListener = null;

    private Adapter mAdapter;
    private FlowDataSetObserver dataSetObserver = new FlowDataSetObserver();

    public FlowLayout(final Context context) {
        this(context, null);
    }

    public FlowLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.setWillNotDraw(false);
    }

    public void setAdapter(Adapter adapter) {

        if (mAdapter != null)
            mAdapter.unregisterDataSetObserver(dataSetObserver);

        mAdapter = adapter;
        mAdapter.registerDataSetObserver(dataSetObserver);

        buildViews();

    }

    private void buildViews() {
        this.removeAllViews();

        for (int i = 0; i < mAdapter.getCount(); ++i) {
            addView(mAdapter.getView(i, null, this));
        }
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec,
                             final int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec)
                - getPaddingRight();
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        final boolean growHeight = widthMode != MeasureSpec.UNSPECIFIED;

        int width = 0;
        int height = getPaddingTop();

        int currentWidth = getPaddingLeft();
        int currentHeight = 0;

        boolean newLine = false;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            if (growHeight
                    && (currentWidth
                    + child.getMeasuredWidth() > widthSize)) {
                height += currentHeight;
                currentHeight = 0;
                width = Math.max(width, currentWidth);
                currentWidth = getPaddingLeft();
                newLine = true;
            } else {
                newLine = false;
            }

            lp.x = currentWidth;
            lp.y = height;

            currentWidth += child.getMeasuredWidth();
            currentHeight = Math.max(currentHeight, child.getMeasuredHeight());


        }

        if (newLine) {
            height += currentHeight;
            width = Math.max(width, currentWidth );
        }

        width += getPaddingRight();
        height += getPaddingBottom();

        setMeasuredDimension(resolveSize(width, widthMeasureSpec),
                resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onLayout(final boolean changed, final int l, final int t,
                            final int r, final int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y
                    + child.getMeasuredHeight());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: case MotionEvent.ACTION_POINTER_DOWN:
                if (onItemClickListener != null) {
                    setSelectedChildPressed(false);
                    positionClickDown = positionForCoordinates(event);
                    setSelectedChildPressed(true);
                }
                return true;
            case MotionEvent.ACTION_UP:
                setSelectedChildPressed(false);
                if (onItemClickListener != null && positionClickDown != INVALID_POSITION && positionForCoordinates(event) == positionClickDown) {
                    View child = getChildAt(positionClickDown);
                    if(child != null) {
                        onItemClickListener.onItemClick(this, child, positionClickDown, mAdapter.getItemId(positionClickDown));
                    }
                }
                positionClickDown = INVALID_POSITION;
                return true;
            case MotionEvent.ACTION_MOVE:
                if(positionClickDown != INVALID_POSITION) {
                    if(positionForCoordinates(event) != positionClickDown) {
                        setSelectedChildPressed(false);
                        positionClickDown = INVALID_POSITION;
                    }
                }
                return true;
            case MotionEvent.ACTION_CANCEL: case MotionEvent.ACTION_POINTER_UP:
                setSelectedChildPressed(false);
                positionClickDown = INVALID_POSITION;
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void setSelectedChildPressed(final boolean isPressed) {
        if(positionClickDown != INVALID_POSITION) {
            View child = getChildAt(positionClickDown);
            if(child != null) {
                child.setPressed(isPressed);
            }
        }
    }

    private int positionForCoordinates(MotionEvent ev) {
        for (int i = 0; i < this.getChildCount(); ++i) {
            View childView = this.getChildAt(i);
            int loc [] = new int[2];
            childView.getLocationOnScreen(loc);
            Rect rect = new Rect(loc[0], loc[1], loc[0] + childView.getWidth(),  loc[1] + childView.getHeight());
            if (rect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                return i;
            }
        }
        return INVALID_POSITION;
    }


    @Override
    protected boolean checkLayoutParams(final ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(final AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(final ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        int x;
        int y;


        public LayoutParams(final Context context, final AttributeSet attrs) {
            super(context, attrs);
        }

        public LayoutParams(final int w, final int h) {
            super(w, h);
        }
    }

    class FlowDataSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            buildViews();
            super.onChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public View findChildForPosition(int position) {
        if(position >= 0 && position < getChildCount())
            return getChildAt(position);
        else
            return null;
    }
}