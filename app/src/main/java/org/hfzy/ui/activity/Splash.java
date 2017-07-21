package org.hfzy.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.hfzy.R;

import org.hfzy.globle.SeviceUrl;
import org.hfzy.util.Cache;
import org.hfzy.util.OkHttpUtil;
import org.hfzy.util.UIUtils;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class Splash extends AppCompatActivity {

    private ImageView small;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initSeviceData();
        ImageView ivSplash = (ImageView) this.findViewById(R.id.iv_background);
        small = (ImageView) findViewById(R.id.iv_small);

        final RotateAnimation animRotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF,
                0.5f);
        animRotate.setDuration(3000);// 动画时间
        animRotate.setFillAfter(true);// 保持动画结束状态

        animRotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish(); //退出当前Activity
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //透明度动画
        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f); //float
        animation.setDuration(500);

        //启动动画（任何控件都有启动动画的能力）
        ivSplash.startAnimation(animation);

        //动画播放完成后，进到MainActivity
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                small.startAnimation(animRotate);

            }
        });



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
    }

