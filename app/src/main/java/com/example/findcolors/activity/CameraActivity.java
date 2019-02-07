package com.example.findcolors.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findcolors.R;

/*
استخدام مكتبة palette الخاصة بجوجل للحصول علي الالوان من الصور
تتطلب اندوريد 5 (lollipop) علي الاقل

 */
public class CameraActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private ImageView imageView;
    private TextView colorIndicator, red, green, blue,colors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        // عرض زرار الرجوع للخلف في الاكتفتي
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // الحصول علي ال views الموجودة في ال layout
        imageView = findViewById(R.id.imageView);
        colorIndicator = findViewById(R.id.text);
        red = findViewById(R.id.red);
        green = findViewById(R.id.green);
        blue = findViewById(R.id.blue);
        colors = findViewById(R.id.textView4);

        // زرار فتح  الكاميرا
        FloatingActionButton takePhoto = findViewById(R.id.fab);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*
                اولا : معرفة اذا كان صلاحية الوصول للكاميرا متاح ولا لا من اول اندوريد 6 مارشيملو يتطلب هذا الامر
                اذا كان لا يوجد تصريح لاستخدام الكاميرا يتم طلب امر الحصول علي الاذن مع رقم الامر لمعرفة اذا كان اليوزر وافق علي الامر ام لا
                اذا كان التصريح موجود يتم فتح الكاميرا واانتظار استقبال الصورة منها مع رقم الامر لاستخدامه في استقبال الصورة
                 */
                if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA},
                            CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });


        // الضغط علي زرار فتح الكاميرا بمجرد فتح الاكتفتي
        takePhoto.performClick();
    }


    /*
    استقبال رد الفعل الخاص بطلب صلاحية الوصول للكاميرا
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            /* اذا تمت الموافقة علي الصلاحية يتم فتح الكاميرا مباشرا
            اذا لم يتم الموافقة يتم عرض رسالة
             */
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "صلاحية استخدام الكاميرا مرفوضة", Toast.LENGTH_LONG).show();
            }

        }
    }


    /* استقبال الصورة */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /* حالة التقاط الصورة */
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            /* استقبال الصورة في متغير وعرضها في الاكتفتي */
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);


            /* استخراج الالوان من الصورة
            PaletteAsyncListener : to get colors off the main thread

             */

            Palette.from(photo).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {
                    /*
                    getDominantSwatch : الحصول علي اللون الاكثر انتشارا في الصورة
                    واستخراج المعلومات الخاصة بالالوان
                     */
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch != null) {
                        int color = swatch.getRgb();
                        colorIndicator.setBackgroundColor(color);
                        colorIndicator.setTextColor(swatch.getTitleTextColor());
                        colorIndicator.setText("اللون المسيطر");

                        red.setText("الاحمر : " + Color.red(color));
                        green.setText("الاخضر : "+ Color.green(color) );
                        blue.setText("الازرق : " +Color.blue(color));
                        colors.setVisibility(View.VISIBLE);


                    } else {
                        colorIndicator.setText("خطأ في التعرف علي اللون");
                    }

                }
            });
        }
    }

    // استقبال ال action الخاص بزرار الرجوع الخاص بالاكتفتي

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
