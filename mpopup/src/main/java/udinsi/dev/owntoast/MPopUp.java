package udinsi.dev.owntoast;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MPopUp {
    private OnClick listener;
    private OnClick cancleListener;

    private String message;
    private String title;
    private String positiveButton = "Ok";
    private String negativeButton = "Cancle";
    private float radius = 14.0f;
    private int bgColor = Color.WHITE;
    private int btPositiveBg = R.drawable.button_primary_round;
    private int btNegativeBg = R.drawable.button_primary_round_white;
    private int btPositiveTextColor = Color.WHITE;
    private int btNegativeTextColor = Color.GRAY;

    private Dialog dialog;
    private Context context;
    private TextView textView;
    private TextView textViewTitle;
    private Button btnCancle;
    private Button btnOk;
    private CardView cardBackground;
    private LinearLayout llBackground;

    public MPopUp() {
        this.listener = null;
    }

    public MPopUp in(Context context) {
        this.context = context;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public MPopUp setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MPopUp setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPositiveButton() {
        return positiveButton;
    }

    public MPopUp setPositiveButton(String positiveButton) {
        this.positiveButton = positiveButton;
        return this;
    }

    public String getNegativeButton() {
        return negativeButton;
    }

    public MPopUp setNegativeButton(String negativeButton) {
        this.negativeButton = negativeButton;
        return this;
    }

    public float getRadius() {
        return radius;
    }

    public MPopUp setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    public int getBgColor() {
        return bgColor;
    }

    public MPopUp setBgColor(int bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public int getBtPositiveBg() {
        return btPositiveBg;
    }

    public MPopUp setBtPositiveBg(int btPositiveBg) {
        this.btPositiveBg = btPositiveBg;
        return this;
    }

    public int getBtNegativeBg() {
        return btNegativeBg;
    }

    public MPopUp setBtNegativeBg(int btNegativeBg) {
        this.btNegativeBg = btNegativeBg;
        return this;
    }

    public int getBtPositiveTextColor() {
        return btPositiveTextColor;
    }

    public MPopUp setBtPositiveTextColor(int btPositiveTextColor) {
        this.btPositiveTextColor = btPositiveTextColor;
        return this;
    }

    public int getBtNegativeTextColor() {
        return btNegativeTextColor;
    }

    public MPopUp setBtNegativeTextColor(int btNegativeTextColor) {
        this.btNegativeTextColor = btNegativeTextColor;
        return this;
    }

    public MPopUp show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.popup_dialog, null, false);

        textView = view.findViewById(R.id.messages);
        textViewTitle = view.findViewById(R.id.title);

        btnCancle = view.findViewById(R.id.btn_cancel);
        btnOk = view.findViewById(R.id.btn_ok);

        cardBackground = view.findViewById(R.id.pop_background);
        llBackground = view.findViewById(R.id.ll_background);

        cardBackground.setRadius(this.radius);
        llBackground.setBackgroundColor(this.bgColor);

        textViewTitle.setText(getTitle());
        textView.setText(getMessage());

        btnOk.setText(this.positiveButton);
        btnOk.setBackgroundResource(this.btPositiveBg);
        btnOk.setTextColor(this.btPositiveTextColor);

        btnCancle.setText(this.negativeButton);
        btnCancle.setBackgroundResource(this.btNegativeBg);
        btnCancle.setTextColor(this.btNegativeTextColor);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick();
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancleListener.onClick();
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        return this;
    }

    public View customLayout(int layout, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, null, false);
        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        return view;
    }

    public MPopUp dismiss(){
        this.dialog.dismiss();
        return this;
    }
    public MPopUp setOnOkClickListener(OnClick listener) {
        this.listener = listener;
        return this;
    }
    public MPopUp setOnCancleClickListener(OnClick listener) {
        this.cancleListener = listener;
        return this;
    }

    public interface OnClick{
        void onClick();
    }
}
