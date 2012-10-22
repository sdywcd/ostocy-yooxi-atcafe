package com.ostocy.yooxi.shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ostocy.yooxi.action.JshopMGoodsListAction;
import com.ostocy.yooxi.action.JshopMelectrocartAction;
import com.ostocy.yooxi.holder.ElecartListViewHolder;
import com.ostocy.yooxi.holder.GoodsListViewHolder;
import com.ostocy.yooxi.sqlite.DBHelper;
import com.ostocy.yooxi.util.Arith;
import com.ostocy.yooxiatcafe.R;

public class YooxiCoffeeGoodsDetail extends Activity {
	//初始化sqlite所需类
	private final DBHelper dbhelper=new DBHelper(this);
	//初始化页面控件
	private TextView getback;//返回
	private TextView countplus;//加入餐车
	private TextView checkselected;//查看已选
	//我的菜单保存对象
	private ListView listViewForCart;//used by cartlist
	private GoodsListViewHolder holder = new GoodsListViewHolder();
	private ElecartListViewHolder eleholder = new ElecartListViewHolder();
	private Double total=0.0;
	private ArrayList<HashMap<String,Object>> goodslists=new ArrayList<HashMap<String,Object>>();
	//保存我的菜单数据对象
	private ArrayList<HashMap<String, Object>> electrocartgoodslists = new ArrayList<HashMap<String, Object>>();
	
	//商品列表数据操作集合
	private JshopMGoodsListAction jmGoodsListAction = new JshopMGoodsListAction();
	//我的菜单数据操作集合
	private JshopMelectrocartAction jmelecart=new JshopMelectrocartAction();
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
		
		
		//处理返回事件响应
		getback=(TextView) this.findViewById(R.id.getback);
		getback.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		//处理加入餐车事件
		countplus=(TextView) this.findViewById(R.id.countplus);
		countplus.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				addtoCart(goodslists,0);
			}
		});
		
		//查看已选
		checkselected=(TextView)this.findViewById(R.id.checkselected);
		checkselected.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setElecartListView();
			}
		});
	}
	
	/**
	 * 加入餐车
	 * @param goodslists
	 * @param arg2
	 */
	public void addtoCart(final ArrayList<HashMap<String, Object>> goodslists,final int arg2){

		AlertDialog.Builder bulider=new AlertDialog.Builder(this);
		bulider.setMessage("确定加入我的菜单吗?").setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String goodsid=goodslists.get(arg2).get("goodsid").toString();
				String goodsname=goodslists.get(arg2).get("goodsname").toString();
				String memberprice=goodslists.get(arg2).get("memberprice").toString();
				String pictureurl=goodslists.get(arg2).get("pictureurl").toString();
				String needquantity="1";
				jmelecart.setGoodsToElecartSQLite(goodsid,goodsname,memberprice,pictureurl,needquantity, getApplicationContext());
				setElecartListView();
				
			}

		}).setNegativeButton("取消", null);
		AlertDialog alert=bulider.create();
		alert.show();
	}
	/**
	 * 读取我的菜单数据
	 */
	public void setElecartListView(){
		electrocartgoodslists.clear();
		//读取ele_cart缓存
		Cursor ec=dbhelper.query(DBHelper.ELE_CART_TM_NAME);
		electrocartgoodslists=jmelecart.getElecarttoSQLite(ec);
		ec.close();
		/**
		 * 构建弹出框
		 */
		AlertDialog.Builder builder;
		Context mContext=YooxiCoffeeGoodsDetail.this;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View alertCartlist = inflater.inflate(R.layout.yooxicafe_alertlist,null);
		listViewForCart=(ListView)alertCartlist.findViewById(R.id.yooxicartlistview);//我的菜单listview
		builder = new AlertDialog.Builder(mContext);
		listViewForCart.setAdapter(new JshopMyElecartListViewAdapter(electrocartgoodslists,this.getApplicationContext()));
		//初始化控件
		TextView confirm=(TextView) alertCartlist.findViewById(R.id.confirm);
		TextView clearall=(TextView) alertCartlist.findViewById(R.id.clearall);
		//计算总价
		total=0.0;
		if(!electrocartgoodslists.isEmpty()){
			for(int i=0;i<electrocartgoodslists.size();i++){
				total=Arith.add(total, Arith.mul(Double.parseDouble(electrocartgoodslists.get(i).get("memberprice").toString()), Double.parseDouble(electrocartgoodslists.get(i).get("needquantity").toString())));
			}
			TextView countmoney=(TextView) alertCartlist.findViewById(R.id.countmoney);
			countmoney.setText("￥"+total);
		}else{
			TextView countmoney=(TextView) alertCartlist.findViewById(R.id.countmoney);
			countmoney.setText("￥"+total);
		}
		builder.setTitle("已点清单").setView(alertCartlist);
		final AlertDialog alert=builder.create();
		alert.show();
		//confirm事件响应
		confirm.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alert.dismiss();
			}
			
		});
		//清空菜单clearall
		clearall.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearList();
				alert.dismiss();

			}
			
		});
		
	}
	
	
	
	/**
	 * 清空订单列表
	 */
	private void clearList(){
		AlertDialog.Builder bulider=new AlertDialog.Builder(this);
		bulider.setMessage("确定清空点菜列表吗?").setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				clearElecartList();
			}
		}).setNegativeButton("取消", null);
		AlertDialog alert=bulider.create();
		alert.show();
	}
	/**
	 * 清空我的菜单数据
	 */
	public void clearElecartList(){
		
		dbhelper.deleteAllData(DBHelper.ELE_CART_TM_NAME);
	
		Cursor ec=dbhelper.query(DBHelper.ELE_CART_TM_NAME);
		electrocartgoodslists=jmelecart.getElecarttoSQLite(ec);
		ec.close();
		//提示菜单清楚
		String msg="您的菜单已被删除";
		Toast t=Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
		t.show();
		
	}
	/**
	 * 隐重新计算elecart内容
	 */
	public void calnewElecartListView(){
		electrocartgoodslists.clear();
		//读取ele_cart缓存
		Cursor ec=dbhelper.query(DBHelper.ELE_CART_TM_NAME);
		electrocartgoodslists=jmelecart.getElecarttoSQLite(ec);
		ec.close();
		Context mContext=YooxiCoffeeGoodsDetail.this;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View alertCartlist = inflater.inflate(R.layout.yooxicafe_alertlist,null);
		listViewForCart=(ListView)alertCartlist.findViewById(R.id.yooxicartlistview);//我的菜单listview
		listViewForCart.setAdapter(new JshopMyElecartListViewAdapter(electrocartgoodslists,this.getApplicationContext()));

	}
	
	
	/**
	 * 我的elecart的适配器
	 * @author "chenda"
	 *
	 */
	public class JshopMyElecartListViewAdapter extends BaseAdapter {
		private final ArrayList<HashMap<String, Object>> list;
		private LayoutInflater myInflater;

		public JshopMyElecartListViewAdapter(
				ArrayList<HashMap<String, Object>> list, Context context) {
			//this.list.clear();
			this.list = list;
			
			this.myInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			
			if (convertView == null) {
				eleholder = new ElecartListViewHolder();
				convertView = myInflater.inflate(
						R.layout.yooxicafe_listviewdetail, null);
				eleholder.setGoodsname((TextView) convertView
						.findViewById(R.id.goodsname));
				eleholder.setMemberprice((TextView) convertView
						.findViewById(R.id.memberprice));
				eleholder.setNeedquantity((TextView) convertView.findViewById(R.id.needquantity));
				eleholder.setPlus((TextView) convertView.findViewById(R.id.plus));
				eleholder.setMinus((TextView) convertView.findViewById(R.id.minus));
				convertView.setTag(eleholder);
			} else {
				eleholder = (ElecartListViewHolder) convertView.getTag();
			}
			
			eleholder.getGoodsname().setText(
					list.get(position).get("goodsname").toString());
			eleholder.getMemberprice().setText(
					list.get(position).get("memberprice").toString());
			eleholder.getNeedquantity().setText(
					list.get(position).get("needquantity").toString());
			eleholder.getPlus().setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							jmelecart.plusorMinusElecart(list, position, "plus", v.getContext());
							//setElecartListView();
							//setTotalMemberprice();
							calnewElecartListView();
							int iplus=Integer.parseInt(list.get(position).get("needquantity").toString());
							String tn=String.valueOf(iplus);
							eleholder.getNeedquantity().setText(tn);
							notifyDataSetChanged();
						}
					});
			
			eleholder.getMinus().setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							jmelecart.plusorMinusElecart(list, position, "minus", v.getContext());
							//setElecartListView();
							//setTotalMemberprice();
							calnewElecartListView();
							if(!list.isEmpty()){
								int iplus=Integer.parseInt(list.get(position).get("needquantity").toString());
								String tn=String.valueOf(iplus-1);
								eleholder.getNeedquantity().setText(tn);
							}
							notifyDataSetChanged();//这个是关键
						}
					});
			
			return convertView;
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}
	}
}
