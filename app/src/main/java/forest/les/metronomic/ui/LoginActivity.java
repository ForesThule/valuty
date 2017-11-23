package forest.les.metronomic.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.support.design.widget.RxBottomNavigationView;
import com.jakewharton.rxbinding2.support.v17.leanback.widget.RxSearchEditText;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.stephentuso.welcome.WelcomeHelper;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import forest.les.metronomic.R;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static android.util.Patterns.EMAIL_ADDRESS;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_login) EditText etLogin;
    @BindView(R.id.et_password) EditText ePassword;
    BottomNavigationView navigation;
    WelcomeHelper welcomeScreen;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        welcomeScreen = new WelcomeHelper(this, SplashActivity.class);
        welcomeScreen.forceShow();

        etLogin = (EditText) findViewById(R.id.et_login);
        navigation = (BottomNavigationView) findViewById(R.id.nav_login);


        RxTextView.textChanges(etLogin)
                .map(t -> EMAIL_ADDRESS.matcher(t).matches())
                .map(b -> b ? Color.BLACK : Color.RED)
                .subscribe(c -> etLogin.setTextColor(c));


        navigation.setOnNavigationItemSelectedListener(item -> {

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            return true;
        });






//        RxBottomNavigationView.itemSelections(binding.navigation)
//                .filter(menuItem -> menuItem.getItemId()==R.id.navigation_login)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(menuItem -> {
//
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));;
//                });


    }


}
