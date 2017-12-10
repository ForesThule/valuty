package forest.les.metronomic.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.model.Valute;
import timber.log.Timber;

/**
 * TODO: document your custom view class.
 */
public class CalculatorView extends LinearLayout {

        Context context;
        View rootView;

//        @BindView(R.id.frame)LinearLayout frame;

        private List<Valute> data;


        public CalculatorView(Context context) {
            super(context);
            this.context = context;
            init(context);
        }

        public CalculatorView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            this.context = context;
            init(context);
        }

        private void init(Context context){

            try {
                rootView = inflate(context,R.layout.calc_layout,this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ButterKnife.bind(this);

            for (Valute v : data) {
                Button myButton = new Button(context);
                myButton.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));

//                addView(myButton);
//                frame.addView(myButton);
            }
        }


        //public void setImageViewerPager(HackyViewPager imageViewerPager) {
        //  this.imageViewerPager = imageViewerPager;
        //}

//        public void showAlbum() {
//
//            List<String> photoLinks = data.getPhotoLinks();
//
//            for (ImageView iv : album) {
//                iv.setImageResource(R.drawable.ic_android_image);
//            }
//
//            for (int i = 0; i < photoLinks.size(); i++) {
//
//                if (i < album.size()) {
//                    ImageView target = album.get(i);
//                    Uri link = Uri.parse(photoLinks.get(i));
//
//                    Picasso.with(context)
//                            .load(link)
//                            .fit()
//                            .centerInside()
//                            .placeholder(R.drawable.ic_loop)
//                            .into(target);
//
//                    target.invalidate();
//                } else {
//                    Timber.i("Album index out of bound: album size = %d, index = %d", album.size(), i);
//                }
//            }
//        }

        public void addData(Valute valute){
            data.add(valute);
        }

        public void setData(List<Valute> data) {
            this.data = data;
//            showAlbum();
        }
    }