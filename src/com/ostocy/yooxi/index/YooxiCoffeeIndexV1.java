package com.ostocy.yooxi.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ostocy.yooxi.action.JshopMGoodsListAction;
import com.ostocy.yooxi.action.JshopMgoodscategoryListAction;
import com.ostocy.yooxi.holder.YooxiCoffeeIndexViewHolder;
import com.ostocy.yooxi.shop.YooxiCoffeeGoodsDetail;
import com.ostocy.yooxi.sqlite.DBHelper;
import com.ostocy.yooxiatcafe.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
public class YooxiCoffeeIndexV1 extends Activity {
	
	
	private final DBHelper dbhelper=new DBHelper(this); //sqlite
	
	private ViewGroup maingroup;	//viewGroup
	private ViewPager viewPager;	//viewPager	
	private ArrayList<View>pageViews = new ArrayList<View>(); //viewlist for viewPager
	
	
	//保存商品分类数据对象
	private List<Map<String,Object>>goodscategoryList=new ArrayList<Map<String,Object>>();
	//商品分类数据操作集合
	private JshopMgoodscategoryListAction jmgclAction=new JshopMgoodscategoryListAction();
	
	//保存商品列表数据对象
	private ArrayList<HashMap<String, Object>> goodslists = new ArrayList<HashMap<String, Object>>();
	//商品列表数据操作集合
	private JshopMGoodsListAction jmGoodslistAction=new JshopMGoodsListAction();
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);//设置无标题窗口
		super.setContentView(R.layout.yooxicafe_index);
		
		//TODO 拿到商品分类顶级分类的name 然后拿到商品的详细
		Cursor c=dbhelper.query(DBHelper.GOODS_CATEGORY_TM_NAME);
		goodscategoryList=jmgclAction.getGoodsCategoryListtoSQLite(c);
		c.close();
		if(goodscategoryList.isEmpty()){
			goodscategoryList=jmgclAction.getGoodsCategoryList();
			//缓存goodscategorylist
			jmgclAction.setGoodsCategoryListtoSQLite(goodscategoryList, this.getApplicationContext());
		}
		collectSqliteGoodsList(goodscategoryList.get(0).get("name").toString());
		
		startViewPager();
		
		
		
	}
	
	private void startViewPager(){
		viewPager = (ViewPager) this.findViewById(R.id.indexViewPager);
		YooxiIndexPagerAdapter mPagerAdapter = new YooxiIndexPagerAdapter(goodslists);
		viewPager.setAdapter(mPagerAdapter);
	}
	
	/**
	 * 根据商品分类名称获取商品列表
	 * @param tag
	 */
	@SuppressWarnings("unused")
	private  void collectSqliteGoodsList(String tag){
		
		Cursor c=dbhelper.queryByParamGoodsCategoryTName(DBHelper.GOODS_TM_NAME,tag);
		try {
			goodslists=jmGoodslistAction.getGoodsListSQLite(c);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		c.close();
	}
	
	public class YooxiIndexPagerAdapter extends PagerAdapter {
		private ArrayList<View> mListViews;
		private Context ctx;
		private ArrayList<HashMap<String, Object>> goodslists = new ArrayList<HashMap<String, Object>>();
		private YooxiCoffeeIndexViewHolder holder = new YooxiCoffeeIndexViewHolder();
		//private JshopMGoodsListAction jmGoodsListAction = new JshopMGoodsListAction();
		public YooxiIndexPagerAdapter(ArrayList<HashMap<String, Object>> goodslists){
			this.goodslists = goodslists;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return goodslists.size();
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			// TODO Auto-generated method stub
			
			//View v = new View(ctx);
			LayoutInflater inflater=getLayoutInflater();			
			View v=(View)inflater.inflate(R.layout.yooxi_start_page, null);
			holder.setLefttopimage((ImageView) v.findViewById(R.id.indexlefttopimage));
			holder.setRighttopimage((ImageView) v.findViewById(R.id.indexrighttopimage));
			holder.setLeftbottomimage((ImageView) v.findViewById(R.id.indexleftbottomimage));
			holder.setRightbottomimage((ImageView) v.findViewById(R.id.indexrightbottomimage));
			holder.setLefttoptext((TextView) v.findViewById(R.id.lefttopimage_name));
			holder.setRighttoptext((TextView) v.findViewById(R.id.righttopimage_name));
			holder.setLeftbottomtext((TextView) v.findViewById(R.id.leftbottomimage_name));
			holder.setRighttoptext((TextView) v.findViewById(R.id.righttopimage_name));
			final int i0;
			final int i1;
			final int i2;
			int i3;
			i0=4*position+0;
			i1=4*position+1;
			i2=4*position+2;
			i3=4*position+3;
			if(goodslists.get(i0)!=null){
				holder.getLefttoptext().setText(goodslists.get(i0).get("goodsname").toString());
				try {
					holder.getLefttopimage().setImageBitmap(jmGoodslistAction.GetLocalOrNetBitmapWithoutScale(goodslists.get(i0).get("pictureurl").toString(),2));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				holder.getLefttopimage().setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//进入对应的详细页去
						Intent intent= new Intent(YooxiCoffeeIndexV1.this, YooxiCoffeeGoodsDetail.class);
						intent.putExtra("goodsid", goodslists.get(i0).get("goodsid").toString());
						startActivity(intent);
						
					}
				});
			}
			if(goodslists.get(i1)!=null){
				holder.getLeftbottomtext().setText(goodslists.get(i1).get("goodsname").toString());
				try {
					holder.getLeftbottomimage().setImageBitmap(jmGoodslistAction.GetLocalOrNetBitmapWithoutScale(goodslists.get(i1).get("pictureurl").toString(),2));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				holder.getLeftbottomimage().setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//进入对应的详细页去
						Intent intent= new Intent(YooxiCoffeeIndexV1.this, YooxiCoffeeGoodsDetail.class);
						intent.putExtra("goodsid", goodslists.get(i1).get("goodsid").toString());
						startActivity(intent);
						
					}
				});
			}
			if(goodslists.get(i2)!=null){
				holder.getRighttoptext().setText(goodslists.get(i2).get("goodsname").toString());
				try {
					holder.getRighttopimage().setImageBitmap(jmGoodslistAction.GetLocalOrNetBitmapWithoutScale(goodslists.get(i2).get("pictureurl").toString(),2));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				holder.getRighttopimage().setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//进入对应的详细页去
						Intent intent= new Intent(YooxiCoffeeIndexV1.this, YooxiCoffeeGoodsDetail.class);
						intent.putExtra("goodsid", goodslists.get(i2).get("goodsid").toString());
						startActivity(intent);
						
					}
				});
			}
//			if(goodslists.get(i3)!=null){
//				holder.getRightbottomtext().setText(goodslists.get(i3).get("goodsname").toString());
//				try {
//					holder.getRightbottomimage().setImageBitmap(jmGoodslistAction.GetLocalOrNetBitmapWithoutScale(goodslists.get(i3).get("pictureurl").toString(),2));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			
//			}
			
			((ViewPager) container).addView(v);
			return v;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
			//return false;
		}
		
		@Override
		public void setPrimaryItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			//getGoodsDetail(goodslists,position);
			super.setPrimaryItem(container, position, object);
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(container);
		}
		@Override
		public void finishUpdate(View container) {
			// TODO Auto-generated method stub
			super.finishUpdate(container);
		}
		@Override
		public void startUpdate(View container) {
			// TODO Auto-generated method stub
			super.startUpdate(container);
		}
		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}
		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
			// TODO Auto-generated method stub
			super.restoreState(state, loader);
		}
		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return super.saveState();
		}
		
	}
	
	
	
	
	
	
	
}
