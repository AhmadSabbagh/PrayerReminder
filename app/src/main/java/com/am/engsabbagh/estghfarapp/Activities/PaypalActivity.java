package com.am.engsabbagh.estghfarapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.am.engsabbagh.estghfarapp.R;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PaypalActivity extends AppCompatActivity {

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    private static final String CONFIG_CLIENT_ID = "Adwv2e49azIuRA55XwDbAig2b4b6uXDAHLVO13pSE_ONjMgLM56e9Io3yy5IN-eRev5hdtVaZ5aDs4Xq";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Hipster Store")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.example.com/legal"));

    PayPalPayment thingToBuy;
    Button OrderBu;
    EditText amount;
    String amout_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        OrderBu = (Button) findViewById(R.id.order);
        amount = (EditText) findViewById(R.id.et_id);
        OrderBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amout_str = amount.getText().toString();
                if (amout_str.equals("")) {
                    Toast.makeText(PaypalActivity.this, "حدد المبلغ", Toast.LENGTH_LONG).show();
                } else {
                    thingToBuy = new PayPalPayment(new BigDecimal(amout_str), "USD",
                            "amount", PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(PaypalActivity.this,
                            PaymentActivity.class);

                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

                    startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                }
            }
        });

    }

    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(PaypalActivity.this,
                PayPalFuturePaymentActivity.class);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject()
                                .toString(4));

                        Toast.makeText(getApplicationContext(), "Order placed",
                                Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("The user canceled.");
                Log.e("paypaltag","canceld");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                System.out
                        .println("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
                Log.e("paypaltag","An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");

            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.e("paypaltag", auth.toJSONObject()
                                .toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.e("paypaltag", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(getApplicationContext(),
                                "Future Payment code received from PayPal",
                                Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("paypaltag",
                                "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("paypaltag", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("paypaltag",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

    }

    public void onFuturePaymentPurchasePressed(View pressed) {
        // Get the Application Correlation ID from the SDK
        String correlationId = PayPalConfiguration
                .getApplicationCorrelationId(this);

        Log.i("FuturePaymentExample", "Application Correlation ID: "
                + correlationId);

        // TODO: Send correlationId and transaction details to your server for
        // processing with
        // PayPal...
        Toast.makeText(getApplicationContext(),
                "App Correlation ID received from SDK", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}

