package com.example.e_comerce.module_customer.cart;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.entity.OrderEntity;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvSubTotal, tvDiscount, tvFinalTotal;
    private EditText edtNote;
    private Spinner spinnerVoucher;
    private RadioButton rbCash;
    private RadioGroup radioGroupDelivery;
    private Button btnConfirm;

    private double originalPrice = 0;
    private double discountAmount = 0;
    private double finalPrice = 0;
    private String itemsSummary = "";

    // Danh sách Voucher mẫu
    private final String[] vouchers = {
            "Chọn mã giảm giá",
            "CHAOBANMOI - Giảm 10.000đ",
            "FREESHIP - Giảm 15.000đ",
            "VUIIVE - Giảm 5%"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // 1. Nhận dữ liệu từ CartFragment gửi sang
        originalPrice = getIntent().getDoubleExtra("TOTAL_PRICE", 0);
        itemsSummary = getIntent().getStringExtra("ITEMS_SUMMARY");

        initViews();
        setupVoucherSpinner();

        // Tính toán hiển thị lần đầu
        updatePriceUI();

        // 2. Xử lý nút Đặt hàng
        btnConfirm.setOnClickListener(v -> processOrder());

        findViewById(android.R.id.content).setOnClickListener(v -> finish());
    }

    private void initViews() {
        tvSubTotal = findViewById(R.id.tvSubTotal);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvFinalTotal = findViewById(R.id.tvFinalTotal);
        edtNote = findViewById(R.id.edtNote);
        spinnerVoucher = findViewById(R.id.spinnerVoucher);
        rbCash = findViewById(R.id.rbCash);
        radioGroupDelivery = findViewById(R.id.radioGroupDelivery); // Ánh xạ RadioGroup mới
        btnConfirm = findViewById(R.id.btnConfirmOrder);
    }

    private void setupVoucherSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vouchers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVoucher.setAdapter(adapter);

        spinnerVoucher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Logic tính giảm giá giả lập
                switch (position) {
                    case 0: discountAmount = 0; break;
                    case 1: discountAmount = 10000; break;
                    case 2: discountAmount = 15000; break;
                    case 3: discountAmount = originalPrice * 0.05; break; // 5%
                }
                updatePriceUI();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updatePriceUI() {
        finalPrice = originalPrice - discountAmount;
        if (finalPrice < 0) finalPrice = 0;

        tvSubTotal.setText(formatMoney(originalPrice));
        tvDiscount.setText("- " + formatMoney(discountAmount));
        tvFinalTotal.setText(formatMoney(finalPrice));
    }

    private void processOrder() {
        // Lấy thông tin từ giao diện
        String note = edtNote.getText().toString().trim();
        String paymentMethod = rbCash.isChecked() ? "Tiền mặt" : "Chuyển khoản";

        // Lấy hình thức nhận hàng
        String deliveryMethod = "Giao hàng (Ship)";
        if (radioGroupDelivery.getCheckedRadioButtonId() == R.id.rbPickup) {
            deliveryMethod = "Nhận tại quán";
        }

        String dateNow = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        OrderEntity newOrder = new OrderEntity(
                dateNow,
                finalPrice,
                "Đang chờ",
                itemsSummary,
                paymentMethod,
                note,
                discountAmount,
                deliveryMethod // Truyền tham số mới vào
        );

        // Lưu vào DB ở luồng phụ
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            // 1. Lưu đơn hàng
            db.orderDao().insertOrder(newOrder);

            // 2. Xóa giỏ hàng sau khi đặt thành công
            db.cartDao().clearCart();

            // 3. Thông báo và quay về
            runOnUiThread(() -> {
                Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                finish();
            });
        }).start();
    }

    private String formatMoney(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(amount);
    }
}