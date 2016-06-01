package edu.galileo.android.tipcalc.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.galileo.android.tipcalc.R;
import edu.galileo.android.tipcalc.TipCalcApp;
import edu.galileo.android.tipcalc.fragments.TipHistoryListFragment;
import edu.galileo.android.tipcalc.fragments.TipHistoryListFragmentListener;


public class MainActivity extends AppCompatActivity {

    private TipHistoryListFragmentListener fragmentListener;

    private final static int TIP_STEP_CHANGE = 1;
    private final static int DEFAULT_TIP_PERCENTAGE = 10;
    @BindView(R.id.inputBill)
    EditText inputBill;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.inputPercentage)
    EditText inputPercentage;
    @BindView(R.id.btnIncrease)
    Button btnIncrease;
    @BindView(R.id.btnDecrease)
    Button btnDecrease;
    @BindView(R.id.btnClear)
    Button btnClear;
    @BindView(R.id.txtTip)
    TextView txtTip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        TipHistoryListFragment fragment = (TipHistoryListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentList);

        fragment.setRetainInstance(true);
        fragmentListener= (TipHistoryListFragmentListener)fragment;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            about();
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * Ocultar Teclado
    */
    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException npe) {
            Log.e(getLocalClassName(), Log.getStackTraceString(npe));
        }
    }

    private void about() {
        TipCalcApp app = (TipCalcApp) getApplication();
        String strUrl = app.getAboutUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(strUrl));
        startActivity(intent);
    }

    @OnClick(R.id.btnSubmit)//Evento click de submit *Se usa butterknife
    public void handleClickSubmit() {
        hideKeyboard();
        //.trim() quita espacios ap principio y final
        String strInputTotal= inputBill.getText().toString().trim();
        if(!strInputTotal.isEmpty()){
            double total= Double.parseDouble(strInputTotal);
            int tipPercentage = getTipPercentage();
            double tip = total*(tipPercentage/100d);

            String strTip = String.format(getString(R.string.global_message_tip),tip);
            fragmentListener.action(strTip);
            txtTip.setVisibility(View.VISIBLE);
            txtTip.setText(strTip);
        }
    }

    @OnClick(R.id.btnIncrease)//Evento click de submit *Se usa butterknife
    public void handleClickIncrease() {
        hideKeyboard();
        handleTipChange(TIP_STEP_CHANGE);
    }
    @OnClick(R.id.btnDecrease)//Evento click de submit *Se usa butterknife
    public void handleClickDecrease() {
        hideKeyboard();
        handleTipChange(-TIP_STEP_CHANGE);
    }

    //Cambiar porcentagede propina
    private void handleTipChange(int change) {
        int tipPercentage=getTipPercentage();
        tipPercentage+= change;
        if(tipPercentage>0){
            inputPercentage.setText(String.valueOf(tipPercentage));
        }
    }

    public int getTipPercentage() {
        int tipPercentage=DEFAULT_TIP_PERCENTAGE;
        String strInputTotal= inputPercentage.getText().toString().trim();
        if(!strInputTotal.isEmpty()){
            tipPercentage= Integer.parseInt(strInputTotal);
        }else {
            inputPercentage.setText(String.valueOf(tipPercentage));
        }
        return tipPercentage;
    }
}
