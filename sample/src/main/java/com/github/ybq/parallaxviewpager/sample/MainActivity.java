package com.github.ybq.parallaxviewpager.sample;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.github.ybq.parallaxviewpager.Mode;
import com.github.ybq.parallaxviewpager.ParallaxViewPager;


public class MainActivity extends AppCompatActivity {

    private ParallaxViewPager mParallaxViewPager;
    @SuppressWarnings("SpellCheckingInspection")
    private String[] mImages = new String[]{
            "https://drscdn.500px.org/photo/127892951/h%3D600_k%3D1_a%3D1/3487a549dbbe46e2d803a37281543322",
            "https://drscdn.500px.org/photo/127893495/h%3D600_k%3D1_a%3D1/8462ac67727eecd23c104612ab998633",
            "https://drscdn.500px.org/photo/127892351/h%3D600_k%3D1_a%3D1/883a524bfaf3aa66ef39652928b61f51",
            "https://drscdn.500px.org/photo/127891921/h%3D600_k%3D1_a%3D1/c5aec47c6c924d733f58cec483dc41a6",
            "https://drscdn.500px.org/photo/127913833/h%3D600_k%3D1_a%3D1/7aee64d43cdbe4a1d291effb834137e8",
            "https://drscdn.500px.org/photo/127900863/h%3D600_k%3D1_a%3D1/e63c59888014392bac32cfb9c383bb9e",
            "https://drscdn.500px.org/photo/127870627/h%3D600_k%3D1_a%3D1/df562860314d42dd9a4f8bf4ee0ac0e5",
            "https://drscdn.500px.org/photo/127883901/h%3D600_k%3D1_a%3D1/1ce1dcfbf374fd9d60df960bff046f92",
            "https://drscdn.500px.org/photo/127875875/h%3D600_k%3D1_a%3D1/9e667207de3ee01b72fec699a61a156f",
            "https://drscdn.500px.org/photo/127910615/h%3D600_k%3D1_a%3D1/9832834ff48dee33cca9a63c3680c391",
            "https://drscdn.500px.org/photo/127917691/h%3D600_k%3D1_a%3D1/569744eb7f6b0be651ef95b05409f283",
            "https://drscdn.500px.org/photo/127895191/h%3D600_k%3D1_a%3D1/6a8dd4932e237244a690b7683d18c184",
            "https://drscdn.500px.org/photo/127895003/h%3D600_k%3D1_a%3D1/aa9ba5e17219b6523e3576914281d014",
            "https://drscdn.500px.org/photo/127891201/h%3D600_k%3D1_a%3D1/11e7b89d61b3633d58e80bb4b91cfb96",
            "https://drscdn.500px.org/photo/127876087/h%3D600_k%3D1_a%3D1/beb9f8d4341e4c99aec0918081c29dfe",
            "https://drscdn.500px.org/photo/127866171/h%3D600_k%3D1_a%3D1/5100cdeb7006968a012ecf106c0fe28b",
            "https://drscdn.500px.org/photo/127868593/h%3D600_k%3D1_a%3D1/02ed979046028b417bb6e2214a8403e4",
            "https://drscdn.500px.org/photo/127868963/h%3D600_k%3D1_a%3D1/be27239695e8002979124bfdeb9730ad",
            "https://drscdn.500px.org/photo/127879079/h%3D600_k%3D1_a%3D1/d00277578f457e84eb36faa7740f4374",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mParallaxViewPager = (ParallaxViewPager) findViewById(R.id.viewpager);
        initViewPager();
    }

    private void initViewPager() {
        PagerAdapter adapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object obj) {
                container.removeView((View) obj);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = View.inflate(container.getContext(), R.layout.pager_item, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.item_img);
                Glide.with(MainActivity.this).load(mImages[position % mImages.length]).into(imageView);
                container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                return view;
            }

            @Override
            public int getCount() {
                return 40;
            }
        };
        mParallaxViewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.left_overlay:
                mParallaxViewPager.setMode(Mode.LEFT_OVERLAY);
                break;
            case R.id.right_overlay:
                mParallaxViewPager.setMode(Mode.RIGHT_OVERLAY);
                break;
            case R.id.none:
                mParallaxViewPager.setMode(Mode.NONE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
