/**
 * 
 */
package com.daguo.ui.before;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.daguo.R;
/**
 * @author  ：BugsRabbit
 * @version 创建时间：2015-11-24 上午11:40:46
 * @function ：
 */
/**
 * @author : BugsRabbit 
 * @email 395360255@qq.com
 * @version 创建时间：2015-11-24 上午11:40:46
 * @function ： 
 */
public class MainRegisterAty1 extends FragmentActivity {

    private int currIndex = 0;// 当前页卡编号
    private int zero = 0;// 动画图片偏移量
    private int one;// 单个水平动画位移
    private int two;
    public MainRegister_Step1Fragment step1Fragment;
    public MainRegister_Step2Fragment step2Fragment;
    public MainRegister_Step3Fragment step3Fragment;
    public FrameLayout viewPager;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        setContentView(R.layout.aty_mainregister1);
       
        initview();
    
     
    }
    private void initview() {
        viewPager = (FrameLayout) findViewById(R.id.framelayout);
     
        step1Fragment = new MainRegister_Step1Fragment();
        step2Fragment = new MainRegister_Step2Fragment();
        step3Fragment = new MainRegister_Step3Fragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.framelayout, step1Fragment);
        ft.commit();
    }
    /**
     * 翻页适配
     * 
     * @author Bugs_Rabbit 時間： 2015-10-20 下午5:25:09
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
            case 0:
          
               
                if (currIndex == 1) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, 0, 0, 0);
                }
                break;
            case 1:
             
              
                if (currIndex == 0) {
                    animation = new TranslateAnimation(zero, one, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, one, 0, 0);
                }
                break;
            case 2:
       
               
                if (currIndex == 0) {
                    animation = new TranslateAnimation(zero, two, 0, 0);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(one, two, 0, 0);
                }
                break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(150);
            // mTabImg.startAnimation(animation);
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
   
    
}

