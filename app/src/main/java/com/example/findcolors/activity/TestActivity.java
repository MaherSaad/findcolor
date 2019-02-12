package com.example.findcolors.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.findcolors.R;
import com.example.findcolors.adapter.PhotoAdapter;
import com.example.findcolors.util.Config;
import com.example.findcolors.util.PhotosModel;

import java.util.List;

/*
   - استخدام مكتبة Fast Android Networking Library
   لتحميل المعلومات من الويب سيرفس
    */
public class TestActivity extends AppCompatActivity implements PhotoAdapter.PhotoClickListener {
    private List<PhotosModel> photosModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
        // عرض زرار الرجوع للخلف في الاكتفتي
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // تعريف الادابتر الخاص بالنصائح
        final PhotoAdapter photoAdapter = new PhotoAdapter(this);

        // تحديد اتجاه افقي لعرض الداتا واضافه الادابتر ل recylcer view
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(photoAdapter);

        // عمل وعرض dialog لتنبيه المستخدم بوجود تحميل لمحتوي
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // الحصول علي الداتا من الويب سيرفس
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.get(Config.BASE_URL + "Api/photos")
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObjectList(PhotosModel.class, new ParsedRequestListener<List<PhotosModel>>() {

                    @Override
                    public void onResponse(List<PhotosModel> response) {
                        photosModels = response;
                        progressDialog.dismiss();
                        photoAdapter.loadItems(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(TestActivity.this, "خطأ في الحصول علي المعلومات", Toast.LENGTH_LONG).show();
                    }
                });

    }

    // استقبال ال action الخاص بزرار الرجوع الخاص بالاكتفتي
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(final int pos) {
        /*
        استقبال عند الضغط علي زرار عرض الرقم
        اظهار الرقم الصحيح في alert dialog
         */


        final EditText input = new EditText(TestActivity.this);
        input.setHint("اكتب الرقم هنا");
        input.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TestActivity.this);
        alertDialog.setMessage("تحقق من الرقم الموجود بالصورة");
        alertDialog.setPositiveButton("تحقق", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String answer = input.getText().toString();
                if (answer.isEmpty()) {
                    Toast.makeText(TestActivity.this, "الرقم مطلوب", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    String result = "";
                    String number = photosModels.get(pos).getPhoto_number();
                    if (answer.equals(number)) {
                        result = "إجابة صحيحة";
                    } else {
                        result = "خطأ \nالاجابة الصحيحة هي : " + number;
                    }

                    Toast.makeText(TestActivity.this, result, Toast.LENGTH_LONG).show();

                }
            }
        });
        alertDialog.setNegativeButton("إالغاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });


        alertDialog.setView(input);
        alertDialog.show();
        // }

    }
}
