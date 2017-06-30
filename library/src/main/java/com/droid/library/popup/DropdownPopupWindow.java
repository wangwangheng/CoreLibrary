package com.droid.library.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.xinye.lib.R;


/**
 * DropdownPopupWindow
 *
 * @author wangheng
 */
public class DropdownPopupWindow extends PopupWindow {

    public DropdownPopupWindow(Context context, View view,boolean matchParentWidth){

        // 设置SelectPicPopupWindow的View
        setContentView(view);

        // 设置SelectPicPopupWindow弹出窗体的宽
        if(matchParentWidth){
            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        }else {
            this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);

        // 点back键和其他地方使其消失,设置了这个才能触发OnDismissListener ，设置其他控件变化等操作
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupDropdown);


        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        mMenuView.setOnTouchListener(new OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });
    }
}
