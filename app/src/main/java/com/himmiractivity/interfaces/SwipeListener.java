package com.himmiractivity.interfaces;

import com.himmiractivity.zlistview.ZSwipeItem;

public interface SwipeListener {

	public void onStartOpen(ZSwipeItem layout);

	public void onOpen(ZSwipeItem layout);

	public void onStartClose(ZSwipeItem layout);

	public void onClose(ZSwipeItem layout);

	public void onUpdate(ZSwipeItem layout, int leftOffset, int topOffset);

	public void onHandRelease(ZSwipeItem layout, float xvel, float yvel);

}
