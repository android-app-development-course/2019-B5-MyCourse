package com.android.mycourse.reminder;

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDissmiss(int position);
}
