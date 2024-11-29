package com.example.electricitybills;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    ImageView ivCalculator, ivAbout;
    Window window;
    TextView tvLink;
    ImageView ivMe;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);

        ivCalculator = findViewById(R.id.ivCalculator);
        ivAbout = findViewById(R.id.ivAbout);
        tvLink = findViewById(R.id.tvLink);
        ivMe = findViewById(R.id.ivMe);
        window = getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.design_default_color_error));
        window.setNavigationBarColor(getResources().getColor(R.color.design_default_color_error));

        tvLink.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/YourGitHubRepo"));
            startActivity(browserIntent);
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_iriana);
        setCircularImage(ivMe, bitmap);

        ivCalculator.setOnClickListener(v -> {
            startActivity(new Intent(AboutActivity.this, MainActivity.class));
        });

    }

    public void setCircularImage(ImageView imageView, Bitmap bitmap) {
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(circleBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        float radius = Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2f;
        canvas.drawCircle(bitmap.getWidth() / 2f, bitmap.getHeight() / 2f, radius, paint);

        imageView.setImageBitmap(circleBitmap);
    }
}
