package com.ostocy.yooxi.shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.ostocy.yooxi.action.JshopMGoodsListAction;
import com.ostocy.yooxi.holder.GoodsListViewHolder;
import com.ostocy.yooxi.sqlite.DBHelper;
import com.ostocy.yooxiatcafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class YooxiCoffeeGoodsDetail extends Activity {
	//初始化sqlite所需类
	private final DBHelper dbhelper=new DBHelper(this);
	//初始化页面控件
	private GoodsListViewHolder holder = new GoodsListViewHolder();
	private ArrayList<HashMap<String,Object>> goodslists=new ArrayList<HashMap<String,Object>>();
	private JshopMGoodsListAction jmGoodsListAction = new JshopMGoodsListAction();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);//设置无标题窗口
		super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏模式
		super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
		this.setContentView(R.layout.yooxicafe_goodslist);
		Intent intent=this.getIntent();
		String goodsid=intent.getStringExtra("goodsid");
		Cursor c=dbhelper.queryByParamgoodsid(dbhelper.GOODS_TM_NAME, goodsid);
		try {
			goodslists=jmGoodsListAction.getGoodsListSQLiteNoZip(c);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.close();
		if(!goodslists.isEmpty()){
			holder.setGoodsimage((ImageView) this.findViewById(R.id.goodsimage));
			holder.setGoodsname((TextView) this.findViewById(R.id.goodsname));
			holder.setWeight((TextView) this.findViewById(R.id.valueweight));
			holder.setUnitname((TextView) this.findViewById(R.id.unitname));
			holder.setMemberprice((TextView) this.findViewById(R.id.memberprice));
			holder.setDetail((TextView) this.findViewById(R.id.goodsdetail));
			
			holder.getGoodsimage().setImageBitmap((Bitmap) goodslists.get(0).get("pictureurl"));
			holder.getGoodsname().setText(goodslists.get(0).get("goodsname").toString());
			holder.getMemberprice().setText(goodslists.get(0).get("memberprice").toString());
			holder.getUnitname().setText(goodslists.get(0).get("unitname").toString());
			holder.getWeight().setText(goodslists.get(0).get("weight").toString());
		
		
		}
		
		
		
	}
	
	
	
}
