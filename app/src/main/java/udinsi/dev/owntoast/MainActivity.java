package udinsi.dev.owntoast;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button test;
    Button test2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test = findViewById(R.id.test);
        test2 = findViewById(R.id.test2);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MPopUp mPopUp = new MPopUp();
                final View customview = mPopUp.customLayout(R.layout.popup_dialog, MainActivity.this);
                Button ok = customview.findViewById(R.id.btn_ok);
                Button cancel = customview.findViewById(R.id.btn_cancel);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPopUp.dismiss();
                        Toast.makeText(MainActivity.this, "Di klik dengan custom view", Toast.LENGTH_SHORT).show();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPopUp.dismiss();
                    }
                });
            }
        });

        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MPopUp mPopUp = new MPopUp();
                mPopUp.in(MainActivity.this)
                        .setTitle("Title")
                        .setMessage("Pesan")
                        .setBtPositiveBg(R.drawable.button_primary_round)
                        .setBtNegativeBg(R.drawable.button_primary_round_white)
                        .setBtPositiveTextColor(getResources().getColor(R.color.white))
                        .setBtNegativeTextColor(getResources().getColor(R.color.primary))
                        .setBgColor(R.color.primary)
                        .setRadius(20.0f)
                        .show();
                mPopUp.setOnOkClickListener(new MPopUp.OnClick() {
                    @Override
                    public void onClick() {
                        mPopUp.dismiss();
                        Toast.makeText(MainActivity.this, "Di klik tanpa custom", Toast.LENGTH_SHORT).show();
                    }
                });

                mPopUp.setOnCancleClickListener(new MPopUp.OnClick() {
                    @Override
                    public void onClick() {
                        mPopUp.dismiss();
                    }
                });
            }
        });
    }
}
