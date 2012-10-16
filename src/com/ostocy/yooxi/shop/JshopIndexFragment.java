package com.ostocy.yooxi.shop;

import com.ostocy.yooxiatcafe.R;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class JshopIndexFragment extends Fragment {
	private Fragment childFragment;
	int mLabel;
	public static JshopIndexFragment newInstance(int label){
		JshopIndexFragment f = new JshopIndexFragment();
		Bundle b = new Bundle();
		b.putInt("label",label);
		f.setArguments(b);
		//f.childFragment = childFragment;
		return f;
	}

	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onInflate(activity, attrs, savedInstanceState);
		
	}

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
	    if (args != null) {
	    	   mLabel = args.getInt("label", mLabel);
	     }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.yooxi_start_page, container,false);
		return v;
		//return this.childFragment.getView();
		
		
		
	}

	public Fragment getChildFragment() {
		return childFragment;
	}

	public void setChildFragment(Fragment childFragment) {
		this.childFragment = childFragment;
	}
	
	
	
	
}
