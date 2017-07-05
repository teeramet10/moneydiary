package app.teeramet.money.moneydiary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import app.teeramet.moneydiary.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class DisplayImageActivity extends Activity {
    ImageView imageView;
    ImageButton imbClose;

    Context context = this;

    String mPathimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        imageView = (ImageView) findViewById(R.id.displayimage);
        imbClose = (ImageButton) findViewById(R.id.menuclose);

        Intent intent = getIntent();
        mPathimage = intent.getStringExtra(DescriptionActivity.PATHIMAGE);

        if (mPathimage !=null) {
            File file = new File(mPathimage);
            if (file.exists()) {
                Picasso.with(context).load(file).error(R.color.black).into(imageView);
            }
        } else Picasso.with(context).load(mPathimage).error(R.color.black).into(imageView);

        imbClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
