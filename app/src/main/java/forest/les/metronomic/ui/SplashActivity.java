package forest.les.metronomic.ui;

import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

import forest.les.metronomic.R;

/**
 * Created by root on 30.07.17.
 */

public class SplashActivity extends WelcomeActivity {
    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.background)
                .page(new TitlePage(R.drawable.ic_attach_money_black_24dp,
                        "Курс валют и валютный калькулятор").parallax(true)

                )

//                .page(new BasicPage(R.drawable.ic_attach_money_black_24dp,
//                        "",
//                        "More text.")
//                        .background(R.color.red_background)
//                )
//                .page(new BasicPage(R.drawable.calculator,
//                        "Lorem ipsum",
//                        "dolor sit amet.")
//                )
                .swipeToDismiss(true)
                .build();
    }
}
