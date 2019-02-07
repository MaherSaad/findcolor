package com.example.findcolors.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.findcolors.R;
import com.example.findcolors.adapter.AdviceAdapter;
import com.example.findcolors.util.AdviceModel;
import com.example.findcolors.util.Config;

import java.util.List;

public class AdviceActivity extends AppCompatActivity {
    /*
    - استخدام مكتبة Fast Android Networking Library
    لتحميل المعلومات من الويب سيرفس
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        // عرض زرار الرجوع للخلف في الاكتفتي
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        // تعريف الادابتر الخاص بالنصائح
        final AdviceAdapter adviceAdapter = new AdviceAdapter();
        // تحديد اتجاه رأسي لعرض الداتا واضافه الادابتر ل recylcer view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adviceAdapter);

        // عمل وعرض dialog لتنبيه المستخدم بوجود تحميل لمحتوي
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // الحصول علي الداتا من الويب سيرفس
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.get(Config.BASE_URL + "Api/questionAnswer")
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObjectList(AdviceModel.class, new ParsedRequestListener<List<AdviceModel>>() {

                    @Override
                    public void onResponse(List<AdviceModel> response) {
                        progressDialog.dismiss();
                        adviceAdapter.loadItems(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(AdviceActivity.this, "خطأ في الحصول علي المعلومات", Toast.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // استقبال ال action الخاص بزرار الرجوع الخاص بالاكتفتي
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
