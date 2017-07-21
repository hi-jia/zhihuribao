package org.hfzy.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.hfzy.R;

import org.hfzy.bean.Data;
import org.hfzy.bean.ListItem;
import org.hfzy.bean.TopList;
import org.hfzy.globle.SeviceUrl;
import org.hfzy.ui.activity.DetailPager;
import org.hfzy.util.Cache;
import org.hfzy.util.OkHttpUtil;
import org.hfzy.util.UIUtils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Guojia on 2017/7/18 0018.
 */
public class HomeFragment extends BaseFragment {

    private String TAG = "HomeFragment";
    private ViewPager viewPager;
    private ArrayList<ImageView> imageViewList;
    private LinearLayout ll_point_container;
    private ArrayList<String> contentDescs;
    private TextView tv_desc;
    private int previousSelectedPosition = 0;
    boolean isRunning = false;
    private ListView lv;
    private Data data;
    private ArrayList<TopList> top_stories;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MylistViewAdapter mylistViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initSeviceData();
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public View initView() {
    // initSeviceData();
        String chche = Cache.GetChche(SeviceUrl.HOME, "", UIUtils.getContext());

        if (chche == "") {
            Log.e(TAG,"if没有缓冲");
            initSeviceData();

        } else {
            getServicesData(chche);
//            data = JSON.parseObject(chche, Data.class);
//            top_stories = data.getTop_stories();
            Log.e(TAG,"else有缓冲");
        }
       // initSeviceData();

        View view = UIUtils.inflate(R.layout.home_fragment);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        lv = (ListView) view.findViewById(R.id.lv);
        View lvHeard = View.inflate(UIUtils.getContext(), R.layout.viewpager, null);
        ll_point_container = (LinearLayout) lvHeard.findViewById(R.id.ll_point_container);
        tv_desc = (TextView) lvHeard.findViewById(R.id.tv_desc);
        viewPager = (ViewPager) lvHeard.findViewById(R.id.viewpager);
        lv.addHeaderView(lvHeard);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 新的条目被选中时调用
                System.out.println("onPageSelected: " + position);
                int newPosition = position % imageViewList.size();
                //设置文本
                tv_desc.setText(contentDescs.get(newPosition));
                // 把之前的禁用, 把最新的启用, 更新指示器
                ll_point_container.getChildAt(previousSelectedPosition).setEnabled(false);
                ll_point_container.getChildAt(newPosition).setEnabled(true);
                // 记录之前的位置
                previousSelectedPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });// 设置页面更新监听
//		viewPager.setOffscreenPageLimit(1);// 左右各保留几个对象

        return view;
    }


    @Override
    public void initData() {
        View pointView;
        LayoutParams layoutParams;
       //initSeviceData();
        contentDescs = new ArrayList<String>();
        imageViewList = new ArrayList<ImageView>();
        if (top_stories!=null) {
            for (int i = 0; i < top_stories.size(); i++) {
                TopList topList = top_stories.get(i);
                String title = topList.getTitle();
                String imageUrl = topList.getImage();
                ImageView iv = new ImageView(UIUtils.getContext());
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(UIUtils.getContext()).load(imageUrl).into(iv);
                imageViewList.add(iv);
                contentDescs.add(title);
                // 加小白点, 指示器
                pointView = new View(UIUtils.getContext());
                pointView.setBackgroundResource(R.drawable.selector_bg_point);
                layoutParams = new LinearLayout.LayoutParams(5, 5);
                if (i != 0)
                    layoutParams.leftMargin = 10;
                // 设置默认所有都不可用
                pointView.setEnabled(false);
                ll_point_container.addView(pointView, layoutParams);
            }
        }
        initAdapter();

        // 开启轮询
        new Thread() {
            public void run() {
                isRunning = true;
                while (isRunning) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 往下跳一位
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("设置当前位置: " + viewPager.getCurrentItem());
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        }
                    });
                }
            };
        }.start();

        mylistViewAdapter = new MylistViewAdapter();
        lv.setAdapter(mylistViewAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(UIUtils.getContext(),DetailPager.class);
                int i = data.getStories().get(position-1).getId();
                intent.putExtra("id",i);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void getServicesData(String result) {

        Log.e(TAG,"....................");
         data = JSON.parseObject(result, Data.class);
        top_stories = data.getTop_stories();
    }


    class MylistViewAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return data.getStories().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHold viewHold;
            ListItem item = data.getStories().get(position);
            if (convertView==null){
                convertView = View.inflate(UIUtils.getContext(), R.layout.listview_item, null);
                viewHold=new ViewHold();
                viewHold.img= (ImageView) convertView.findViewById(R.id.homelv_img);
               viewHold.text= (TextView) convertView.findViewById(R.id.homelv_tv);
                convertView.setTag(viewHold);
            }else{
                viewHold=(ViewHold) convertView.getTag();
            }

            Glide.with(UIUtils.getContext()).load(item.getImages().get(0)).into(viewHold.img);
            viewHold.text.setText(item.getTitle());
            return convertView;
        }


        class ViewHold{
            ImageView img;
            TextView text;
        }
    }


    private void initAdapter() {
        ll_point_container.getChildAt(0).setEnabled(true);
        tv_desc.setText(contentDescs.get(0));
        previousSelectedPosition = 0;

        // 设置适配器
        viewPager.setAdapter(new MyAdapter());

        // 默认设置到中间的某个位置
        int pos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % imageViewList.size());
        // 2147483647 / 2 = 1073741823 - (1073741823 % 5)
        viewPager.setCurrentItem(5000000); // 设置到某个位置
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        // 3. 指定复用的判断逻辑, 固定写法
        @Override
        public boolean isViewFromObject(View view, Object object) {
//			System.out.println("isViewFromObject: "+(view == object));
            // 当划到新的条目, 又返回来, view是否可以被复用.
            // 返回判断规则
            return view == object;
        }

        // 1. 返回要显示的条目内容, 创建条目
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            System.out.println("instantiateItem初始化: " + position);
            // container: 容器: ViewPager
            // position: 当前要显示条目的位置 0 -> 4

//			newPosition = position % 5
            int newPosition = position % imageViewList.size();

            ImageView imageView = imageViewList.get(newPosition);
            // a. 把View对象添加到container中
            container.addView(imageView);
            // b. 把View对象返回给框架, 适配器
            return imageView; // 必须重写, 否则报异常

        }

        // 2. 销毁条目
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // object 要销毁的对象
            System.out.println("destroyItem销毁: " + position);
            container.removeView((View) object);
        }
    }




    private void refreshData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        initSeviceData();
                        mylistViewAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }





    private void initSeviceData() {
        OkHttpUtil.sendOkHttpRequest(SeviceUrl.HOME, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("splash", "无网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Cache.SetChche(SeviceUrl.HOME, result, UIUtils.getContext());
                Log.e("splash", result);
                //  Log.e(TAG,"data:"+ data.getStories().get(0).getTitle());
            }
        });



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning=false;
    }
}
