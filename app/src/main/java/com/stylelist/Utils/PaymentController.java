package com.stylelist.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PaymentController {

    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Constants.PAYPAL_CLIENT_ID);

    private static PaymentController sharedInstance = null;

    public static PaymentController sharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new PaymentController();
        }
        return sharedInstance;
    }

    public void startPaypalService(Activity activity) {
        Intent intent = new Intent(activity, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        activity.startService(intent);
    }

    public void stopPaypalService(Activity activity) {
        activity.stopService(new Intent(activity, PayPalService.class));
    }

    public void buyItem(Activity activity, float imagePrice, String currencyUnit, String itemTitle) {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(imagePrice), currencyUnit, itemTitle, PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(activity, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        activity.startActivityForResult(intent, 700);
    }

    public boolean activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 700) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.d("Buying Item: ", confirm.toJSONObject().toString(4));
                        // TODO: Buy success and next operation
                        return true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("Buying Item: ", "User canceled buying Item.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.d("Buying Item: ", "Invalid payment or payment configuration was submitted.");
            }
        }
        return false;
    }
}
