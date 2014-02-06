package com.purplerain.watchmyride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;

public class WatchPaymentActivity extends Activity {

    public static final String EXTRA_USER_EMAIL = "com.purplerain.watchmyride.userEmail";
    public static final String EXTRA_WATCHER_EMAIL = "com.purplerain.watchmyride.watcherEmail";

    public static final double WATCH_PRICE_ILS = 5;
    public static final String PURCHASE_DESCRIPTION = "Bike Watch";

    private static final String CLIENT_ID = "com.purplerain.watchmyride";
    private static final int PAYMENT_REQUEST_CODE = 0;

    private String mUserIdentity;
    private String mWatcherIdentity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_payment);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserIdentity = getIntent().getStringExtra(EXTRA_USER_EMAIL);
        mWatcherIdentity = getIntent().getStringExtra(EXTRA_WATCHER_EMAIL);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_SANDBOX);
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CLIENT_ID);

        startService(intent);
    }

    public void onBuyPressed(View pressed) {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(WATCH_PRICE_ILS), "ILS", PURCHASE_DESCRIPTION);

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_SANDBOX);

        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CLIENT_ID);

        intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, mUserIdentity);
        intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, mWatcherIdentity);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, PAYMENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode != PAYMENT_REQUEST_CODE) {
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                // TODO: Let the server know the payment was successful.
                // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                // for more details.

                return;
            }
        }

        // TODO: Let the server know the payment didn't come through.
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
