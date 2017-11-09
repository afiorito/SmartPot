package com.smartpot.botanicaljournal;
/**
 * Created by MG on 2017-11-05.
 */
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.smartpot.botanicaljournal.DisplayMode.READ_ONLY;

import com.smartpot.botanicaljournal.R;


public class ProfileField extends LinearLayout{

    private DisplayMode displayMode = READ_ONLY;
    private String text = "";

    // VIEWS
    private TextView fieldLabel;
    private TextView fieldTextView;
    private EditText fieldEditText;

    // PROPERTIES
    private int labelColor;
    private int labelTextSize;
    private int textColor;
    private int textSize;
    private Drawable background;

    public ProfileField(Context context) {
        super(context);
        init(context);
    }

    public ProfileField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // Load attributes from XML
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProfileField, 0, 0);


        // Set views properties
        final String labelText = a.getString(R.styleable.ProfileField_labelText);
        final String hintText = a.getString(R.styleable.ProfileField_hintText);
        labelColor = a.getColor(R.styleable.ProfileField_labelColor, 0);
        labelTextSize = a.getDimensionPixelSize(R.styleable.ProfileField_labelTextSize, 0);
        textColor = a.getColor(R.styleable.ProfileField_textColor, 0);
        textSize = a.getDimensionPixelSize(R.styleable.ProfileField_textSize, 0);
        background = a.getDrawable(R.styleable.ProfileField_background);

        init(context);
        a.recycle();

        fieldLabel.setText(labelText);
        fieldEditText.setHint(hintText);
    }

    /**
     * Common initialization of the layout
     * @param context the current context
     */
    private void init(Context context) {
        setOrientation(VERTICAL);
        layoutSubviews(context);
    }

    /**
     * Setup Views
     * @param context the current context
     */
    private void layoutSubviews(Context context) {
        fieldLabel = new TextView(context);
        fieldLabel.setPadding(ViewHelper.dpToPixels(context, 4), 0, ViewHelper.dpToPixels(context, 4), 0);
        fieldLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize);
        fieldLabel.setTextColor(labelColor);

        fieldEditText = new EditText(context);
        fieldEditText.setPadding(0, 0, fieldEditText.getPaddingRight(), 0);
        fieldEditText.setTextSize((textSize/getResources().getDisplayMetrics().scaledDensity));
        fieldEditText.setText(text);
        fieldEditText.setTextColor(getResources().getColor(R.color.hintColor));
        fieldEditText.setHintTextColor(getResources().getColor(R.color.hintColor));
        fieldEditText.setBackground(background);
        fieldEditText.setSingleLine(true);
        fieldEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        fieldEditText.setOnEditorActionListener(editorListener);
        fieldEditText.addTextChangedListener(fieldWatcher);

        fieldTextView = new TextView(context);
        fieldTextView.setPadding(0, 0, fieldEditText.getPaddingRight(), 0);
        fieldTextView.setTextSize((textSize/getResources().getDisplayMetrics().scaledDensity));
        fieldTextView.setTextColor(textColor);
        fieldTextView.setBackground(background);
        fieldTextView.setText(text);

        // READ_ONLY MODE by default
        addView(fieldLabel);
        addView(fieldTextView);
    }

    /**
     * Updates the displaymode of the layout
     * @param displayMode the mode to switch to
     *                    READ_ONLY displays text using a TextView
     *                    WRITE displays text using an EditText
     */
    public void setDisplayMode(DisplayMode displayMode) {
        if (this.displayMode != displayMode) {
            switch (displayMode) {
                case READ_ONLY:
                    removeView(fieldEditText);
                    fieldTextView.setText(text);
                    addView(fieldTextView);
                    break;
                case WRITE:
                    removeView(fieldTextView);
                    fieldEditText.setText(text);
                    addView(fieldEditText);
            }
            this.displayMode = displayMode;
        }
    }

    /**
     * Update the state "text when user types into editText
     */
    private TextWatcher fieldWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            text = charSequence.toString();
        }
    };

    private EditText.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if(i == EditorInfo.IME_ACTION_DONE){
                //Clear focus here from edittext
                fieldEditText.clearFocus();
                final Activity activity = (Activity) getContext();
                ViewHelper.hideSoftKeyboard(activity);
            }
            return false;
        }
    };

    // SETTERS
    public void setText(String text) {
        this.text = text;
        fieldTextView.setText(text);
        fieldEditText.setText(text);
    }

    // GETTERS
    public String getText() {
        return text;
    }
}
