package com.example.electricitybills;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText etUnitInput;
    SeekBar sbRebate;
    TextView tvUnitOutput, tvRebateOutput, tvFinalOutput;
    Button btnCalculate, btnClear;
    ImageView ivCalculator, ivAbout;
    Toolbar tbTopBar;
    double totalCharge = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.design_default_color_error));  // Toolbar color
        window.setNavigationBarColor(getResources().getColor(R.color.design_default_color_error));  // Toolbar color

        etUnitInput = findViewById(R.id.etUnitInput);
        sbRebate = findViewById(R.id.sbRebate);
        tvRebateOutput = findViewById(R.id.tvRebateOutput);
        tvUnitOutput = findViewById(R.id.tvUnitOutput);
        tvFinalOutput = findViewById(R.id.tvFinalOutput);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnClear = findViewById(R.id.btnClear);
        ivCalculator = findViewById(R.id.ivCalculator);
        ivAbout = findViewById(R.id.ivAbout);
        tbTopBar = findViewById(R.id.tbTopBar);

        setSupportActionBar(tbTopBar);

        sbRebate.setEnabled(false);
        btnCalculate.setOnClickListener(v -> {
            String unit = etUnitInput.getText().toString();
            try {
                double units = Double.parseDouble(unit);
                totalCharge = 0.0;

                if (units >= 1 && units <= 200) {
                    totalCharge = units * 0.218;
                } else if (units >= 201 && units <= 300) {
                    totalCharge = 200 * 0.218 + (units - 200) * 0.334;
                } else if (units >= 301 && units <= 600) {
                    totalCharge = 200 * 0.218 + 100 * 0.334 + (units - 300) * 0.516;
                } else if (units >= 601) {
                    totalCharge = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (units - 600) * 0.546;
                }

                tvUnitOutput.setText(String.format("Cost before rebate: RM%.2f", totalCharge));
                tvFinalOutput.setText(String.format("RM%.2f", totalCharge));
                sbRebate.setEnabled(true);
            } catch (NumberFormatException nfe) {
                Toast.makeText(getApplicationContext(), "Please input electricity usage (kWh)", Toast.LENGTH_SHORT).show();
            }

        });

        btnClear.setOnClickListener(v -> {
            etUnitInput.setText("");
            tvUnitOutput.setText("Cost before rebate: RM0.00");
            tvRebateOutput.setText("0%");
            tvFinalOutput.setText("RM 0.00");
            totalCharge = 0.0;
            sbRebate.setProgress(0);
            sbRebate.setEnabled(false);
        });

        sbRebate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvRebateOutput.setText(progress + "%");

                if (fromUser && totalCharge == 0) {
                    Toast.makeText(MainActivity.this, "Please calculate the electricity usage charge first", Toast.LENGTH_SHORT).show();
                } else if (totalCharge > 0) {
                    tvRebateOutput.setText(progress + "%");
                    double rebatePercent = totalCharge * (progress / 100.0);
                    double finalCost = totalCharge - rebatePercent;
                    tvFinalOutput.setText(String.format("RM %.2f", finalCost));
                } else {
                    tvRebateOutput.setText("0%");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        ivAbout.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        });

        etUnitInput.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(5),  // Limit length
                (source, start, end, dest, dstart, dend) -> source.toString().matches("^[0-9]*$") ? source : ""  // Only numbers
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about_us) {

            new android.app.AlertDialog.Builder(MainActivity.this)
                    .setTitle("App Instructions")
                    .setMessage("1. Enter your electricity usage in kWh.\n" +
                            "2. Press 'Calculate' to compute the cost.\n" +
                            "3. Use the 'Rebate %' slider to apply a discount.\n" +
                            "4. Press 'Clear' to reset all fields.\n" +
                            "5. The final charge is displayed after applying the rebate.\n\n" +
                            "Note: Make sure to input a valid value for the units.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())  // Close the dialog
                    .setCancelable(true)

                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
