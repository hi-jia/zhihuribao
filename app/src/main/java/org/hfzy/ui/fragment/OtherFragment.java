package org.hfzy.ui.fragment;

import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.hfzy.R;

import org.hfzy.bean.Data;
import org.hfzy.bean.ListItem;
import org.hfzy.globle.SeviceUrl;
import org.hfzy.ui.activity.DetailPager;
import org.hfzy.util.Cache;
import org.hfzy.util.OkHttpUtil;
import org.hfzy.util.UIUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class OtherFragment extends BaseFragment {
    private String url;
    private String TAG = "otherFtagment";
    private ImageView backgroud;
    private ListView lv;
    private Data data;
    private String backgroundUrl;
    public OtherFragment() {
        super();
    }

    public OtherFragment(String url) {
        this.url = url;
    }

    @Override
    public View initView() {
        initSevices();
        View view = View.inflate(UIUtils.getContext(), R.layout.other_fragment, null);

        lv = (ListView) view.findViewById(R.id.other_lv);
        return view;
    }

    @Override
    public void initData() {
        String chche = Cache.GetChche(SeviceUrl.NORMOL, "", UIUtils.getContext());
        if (chche == "") {
            initSevices();
            SystemClock.sleep(1000);
            Log.e(TAG,"nullllllllllllllllllllllllllllllllllllll");

        } else {
            data = JSON.parseObject(chche, Data.class);
            Log.e(TAG,"esleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        }
        backgroundUrl = data.getBackground();
        ImageView imageView=new ImageView(UIUtils.getContext());

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(UIUtils.getContext()).load(backgroundUrl).into(imageView);
        lv.addHeaderView(imageView);
        lv.setAdapter(new myAdapter());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(UIUtils.getContext(),DetailPager.class);
                int i = data.getStories().get(position-1).getId();
                intent.putExtra("id",i);
                startActivity(intent);
            }
        });
    }


    private void initSevices() {
        OkHttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "other没有数据");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                data = JSON.parseObject(result, Data.class);
                Cache.SetChche(SeviceUrl.NORMOL, result, UIUtils.getContext());
                Log.e(TAG,"resultOther:"+result);
                SystemClock.sleep(1000);

            }
        });

    }

    class myAdapter extends BaseAdapter {

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
            ViewHolder viewHolder;
            ListItem item = data.getStories().get(position);
            if (convertView==null){
                viewHolder=new ViewHolder();
               convertView = View.inflate(UIUtils.getContext(), R.layout.listview_item, null);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.homelv_tv);
                viewHolder.iv= (ImageView) convertView.findViewById(R.id.homelv_img);
                convertView.setTag(viewHolder);
            }else {
               viewHolder= (ViewHolder) convertView.getTag();
            }


           viewHolder.tv.setText(item.getTitle());
            //记得判空，不然后会空指针异常
            if (item.getImages()==null){
                viewHolder.iv.setVisibility(View.GONE);
            }else {
                viewHolder.iv.setVisibility(View.VISIBLE);
                //用Glide根据URL加载图片
                Glide.with(UIUtils.getContext()).load(item.getImages().get(0)).into(viewHolder.iv);
            }
            return convertView;
        }

        public class ViewHolder {
            public ImageView iv;
            public TextView tv;
        }
    }
}
