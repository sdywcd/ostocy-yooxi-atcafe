package com.ostocy.yooxi.index;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.ostocy.yooxi.shop.JshopIndexFragment;
import com.ostocy.yooxiatcafe.R;

public class yooxiCoffeeIndex extends FragmentActivity {
	
	private ViewPager mViewPager;
	private MyFragmentPagerAdatper mMyFragmentPagerAdaper;
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.setContentView(R.layout.yooxicafe_index);				//设置布局页面		
		mViewPager = (ViewPager) findViewById(R.id.indexViewPager);	//引用页面上viewPager对象		
		mMyFragmentPagerAdaper = new MyFragmentPagerAdatper(getSupportFragmentManager(),this);	//实例一个适配器MyFragmentPagerAdatper
		
		mViewPager.setAdapter(mMyFragmentPagerAdaper);
		
		
	}
	
/*	public void showOtherPage(){
		this.mMyFragmentPagerAdaper.removeByIndex(this.mMyFragmentPagerAdaper.getCurrentItem());
	}*/
	
    @Override
	public void onBackPressed() {
		try {
			if (this.mMyFragmentPagerAdaper.getCount() > 1) {
					this.mMyFragmentPagerAdaper.removeLast();
					this.mViewPager.setCurrentItem(this.mViewPager.getChildCount()-1);
			} else {
				super.onBackPressed();
			}
		} catch (Throwable e) {
			Log.e("TesteLayoutByYouTabletActivity OnBackPressed", "OnBackPressed: " + e.getMessage());
		}
	}
	private class MyFragmentPagerAdatper extends FragmentStatePagerAdapter{

		private Context context;
		
		public MyFragmentPagerAdatper(FragmentManager fm,Context context) {
			super(fm);
			this.context = context;
			// TODO Auto-generated constructor stub
		}

		public void removeLast() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public Fragment getItem(int index) {
			// TODO Auto-generated method stub
			Fragment fragment = null;
			
			switch(index){
			case 0:
	            fragment = JshopIndexFragment.newInstance(index);
	            break;
	        default:
	        	break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub
			super.restoreState(arg0, arg1);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return super.isViewFromObject(view, object);
		}
		
		
		
	}
}
