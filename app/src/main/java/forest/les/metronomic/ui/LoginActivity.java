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

import java.util.regex.Pattern;

import forest.les.metronomic.R;
import forest.les.metronomic.databinding.ActivityLoginBinding;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static android.util.Patterns.EMAIL_ADDRESS;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        RxTextView.textChanges(binding.etLogin)
                .map(t -> EMAIL_ADDRESS.matcher(t).matches())
                .map(b -> b ? Color.BLACK : Color.RED)
                .subscribe(c -> binding.etLogin.setTextColor(c));


        binding.navigation.setOnNavigationItemSelectedListener(item -> {

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            return false;
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
