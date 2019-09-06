package com.stylelist.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.Models.DeliveryModel;
import com.stylelist.R;
import com.stylelist.Utils.UtilFunctions;

import java.text.ParseException;

import faranjit.currency.edittext.CurrencyEditText;

import static com.stylelist.Utils.StyleListApp.currentItemDeliverySetting;

public class DeliveryActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    private Switch meetPerson, shipping, domesticShipping, internationalShipping;
    private ConstraintLayout shippingDetailContainer;
    private CurrencyEditText edtDomesticPrice, edtInternationalPrice;
    private CustomFontButton btnReadTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        meetPerson = findViewById(R.id.switch_delivery_meet_person);
        shipping = findViewById(R.id.switch_delivery_shipping);
        domesticShipping = findViewById(R.id.switch_delivery_domestic_shipping);
        internationalShipping = findViewById(R.id.switch_delivery_international_shipping);
        shippingDetailContainer = findViewById(R.id.delivery_shipping_detail_container);
        edtDomesticPrice = findViewById(R.id.edit_delivery_domestic_price);
        edtInternationalPrice = findViewById(R.id.edit_delivery_international_price);
        btnReadTips = findViewById(R.id.btn_delivery_tips);

        CustomFontTextView mTitleTextView = findViewById(R.id.title_text);
        mTitleTextView.setText("Delivery");

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        shipping.setOnCheckedChangeListener(this);
        domesticShipping.setOnCheckedChangeListener(this);
        internationalShipping.setOnCheckedChangeListener(this);

        if (currentItemDeliverySetting != null) {
            domesticShipping.setChecked(currentItemDeliverySetting.isDomesticShipping);
            internationalShipping.setChecked(currentItemDeliverySetting.isInternationalShipping);
            shipping.setChecked(currentItemDeliverySetting.isShipping);
            meetPerson.setChecked(currentItemDeliverySetting.isMeetPerson);
            edtDomesticPrice.setText("$" + currentItemDeliverySetting.domesticPrice);
            edtInternationalPrice.setText("$" + currentItemDeliverySetting.internationalPrice);
        }
    }

    private void checkValidation() {
        if (!meetPerson.isChecked() && !shipping.isChecked()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need to choose delivery method to proceed")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (shipping.isChecked() && !domesticShipping.isChecked() && !internationalShipping.isChecked()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("A shipping option is required to use shipping.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            shipping.setChecked(false);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else
            saveAndFinish();
    }

    private void saveAndFinish() {
        currentItemDeliverySetting = new DeliveryModel();
        currentItemDeliverySetting.isDomesticShipping = domesticShipping.isChecked();
        currentItemDeliverySetting.isInternationalShipping = internationalShipping.isChecked();
        currentItemDeliverySetting.isShipping = shipping.isChecked();
        currentItemDeliverySetting.isMeetPerson = meetPerson.isChecked();
        try {
            if (!edtDomesticPrice.getText().toString().equals(""))
                currentItemDeliverySetting.domesticPrice = String.valueOf(edtDomesticPrice.getCurrencyDouble());
            else
                currentItemDeliverySetting.domesticPrice = "0.00";
            if (!edtInternationalPrice.getText().toString().equals(""))
                currentItemDeliverySetting.internationalPrice = String.valueOf(edtInternationalPrice.getCurrencyDouble());
            else
                currentItemDeliverySetting.internationalPrice = "0.00";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == shipping) {
            if (isChecked)
                shippingDetailContainer.setVisibility(View.VISIBLE);
            else
                shippingDetailContainer.setVisibility(View.GONE);
        } else if (buttonView == domesticShipping) {
            if (isChecked)
                edtDomesticPrice.setVisibility(View.VISIBLE);
            else
                edtDomesticPrice.setVisibility(View.INVISIBLE);
        } else if (buttonView == internationalShipping) {
            if (isChecked)
                edtInternationalPrice.setVisibility(View.VISIBLE);
            else
                edtInternationalPrice.setVisibility(View.INVISIBLE);
        }
    }
}
