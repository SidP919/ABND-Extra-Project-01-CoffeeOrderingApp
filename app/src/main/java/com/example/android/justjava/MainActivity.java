package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    public int quantity = 1;
    boolean whipCr = false;
    boolean choco = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout = findViewById(R.id.layout);
        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideSoftKeyboard(view);
                return false;
            }
        });
    }

    public void hideSoftKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrders(View view) {
        hideSoftKeyboard(view);
        EditText yourName = findViewById(R.id.your_Name);
        String name = yourName.getText().toString();

        if (name.isEmpty())
            Toast.makeText(this, getString(R.string.unknownName), Toast.LENGTH_SHORT).show();

        else {
            int price = 0;
            CheckBox chocolateCheckBox = findViewById(R.id.chocolate_check_box);
            CheckBox whipCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
            boolean choco = chocolateCheckBox.isChecked();
            boolean whipCr = whipCreamCheckBox.isChecked();
            price = calculatePrice(price, choco, whipCr);
            String message = createOrderSummary(name, price, whipCr, choco);
            //displayMessage(message);

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(getResources().getString(R.string.mailTo))); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.orderBy, name));
            intent.putExtra(Intent.EXTRA_TEXT, message);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    /**
     * Calculates the price of the order
     */
    private int calculatePrice(int price, boolean choco, boolean whipCr) {
        price = quantity * 5;
        if (choco && price > 0) {
            price = price + quantity * 2;
        } else if (whipCr && price > 0) {
            price = price + quantity;
        }

        return price;
    }

    /*
     *check only one checkbox
     */
    public void checkTopping(View view) {
        hideSoftKeyboard(view);
        CheckBox checkBox1 = findViewById(R.id.whipped_cream_checkbox);
        CheckBox checkBox2 = findViewById(R.id.chocolate_check_box);
        if (checkBox1.isChecked() && !whipCr) {
            checkBox2.setChecked(false);
            whipCr = true;
            choco = false;
        }
        if (checkBox2.isChecked() && !choco) {
            checkBox1.setChecked(false);
            choco = true;
            whipCr = false;
        }
    }

    /**
     * Calculates the price of the order
     */
    private String createOrderSummary(String name, int priceOfOrder, boolean whipCr, boolean choco) {
        String message = getString(R.string.name,name)
                + "\n" + getResources().getString(R.string.quantity) + " " + quantity
                + "\n" + getResources().getString(R.string.whippedCream) + " " + (whipCr?getString(R.string.isTrue):getString(R.string.isFalse))
                + "\n" + getResources().getString(R.string.addChocolate) + " " + (choco?getString(R.string.isTrue):getString(R.string.isFalse))
                + "\n" + getResources().getString(R.string.priceOrder) + priceOfOrder + ".00"
                + "\n" + getResources().getString(R.string.thankYou);
        return message;
    }

    /*
    This method is called when "+" clicked
     */
    public void increment(View view) {
        hideSoftKeyboard(view);
        if (quantity < 35) {
            quantity = quantity + 1;
            displayQuantity(quantity);
        } else
            Toast.makeText(this, getResources().getString(R.string.toast1), Toast.LENGTH_SHORT).show();
    }

    /*
    This method is called when "-" clicked
     */
    public void decrement(View view) {
        hideSoftKeyboard(view);
        if (quantity == 1) {
            Toast.makeText(this, getResources().getString(R.string.toast2), Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(number));
    }

    /*
     * This method displays the given text on the screen.

    private void displayMessage(String message){
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }
*/
}