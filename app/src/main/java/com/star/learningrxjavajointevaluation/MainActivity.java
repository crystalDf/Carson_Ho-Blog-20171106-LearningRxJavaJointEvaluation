package com.star.learningrxjavajointevaluation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RxJava";

    private EditText mName;
    private EditText mAge;
    private EditText mJob;
    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mName = findViewById(R.id.name);
        mAge = findViewById(R.id.age);
        mJob = findViewById(R.id.job);
        mSubmit = findViewById(R.id.submit);

        Observable<CharSequence> nameObservable = RxTextView.textChanges(mName).skip(1);
        Observable<CharSequence> ageObservable = RxTextView.textChanges(mAge).skip(1);
        Observable<CharSequence> jobObservable = RxTextView.textChanges(mJob).skip(1);

        // 传入EditText控件，点击任1个EditText撰写时，都会发送数据事件 = Function3()的返回值
        // 采用skip(1)原因：跳过 一开始EditText无任何输入时的空值

        Observable
                .combineLatest(nameObservable, ageObservable, jobObservable,
                        (charSequence, charSequence2, charSequence3) -> {
                            boolean isUserNameValid = !TextUtils.isEmpty(mName.getText());
                            boolean isUserAgeValid = !TextUtils.isEmpty(mAge.getText());
                            boolean isUserJobValid = !TextUtils.isEmpty(mJob.getText());

                            return isUserNameValid && isUserAgeValid && isUserJobValid;
                        })
                .subscribe(aBoolean -> {

                    Log.d(TAG, "提交按钮是否可点击: " + aBoolean);
                    mSubmit.setEnabled(aBoolean);
                });
    }
}
