package com.project.mycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.project.mycalculator.*;

import java.math.BigDecimal;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_0;
    Button btn_1;
    Button btn_2;
    Button btn_3;
    Button btn_4;
    Button btn_5;
    Button btn_6;
    Button btn_7;
    Button btn_8;
    Button btn_9;
    Button btn_del;
    Button btn_left;
    Button btn_right;
    Button btn_minus;
    Button btn_plus;
    Button btn_mul;
    Button btn_div;
    Button btn_dot;
    Button btn_equal;
    EditText editText;
    EditText editText_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        init();
    }
//    double d1 = 0, d2 = 0;
//    String operator = "";
    boolean flag = false;
    @Override
    public void onClick(View view) {
        String str = editText.getText().toString();
        switch (view.getId()) {
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
            case R.id.btn_dot:
            case R.id.btn_left:
            case R.id.btn_right:
            case R.id.btn_plus:
            case R.id.btn_minus:
            case R.id.btn_multiple:
            case R.id.btn_divide:
                if (flag) {
                    str = "";
                    flag = false;
                    editText_2.setText("");
                }
                editText.setText(str+ ((Button) view).getText().toString());
                break;

//                d1 = Double.parseDouble(editText.getText().toString());
//                editText.setText(str + ((Button) view).getText().toString());
//                operator = ((Button) view).getText().toString();
//                break;
            case R.id.btn_del:
                if (str != null && !str.equals("")) {
                    str = str.substring(0, str.length() - 1);
                    if (str.equals("")) editText_2.setText("");
                    editText.setText(str);
                }
                break;
            case R.id.btn_equal:
//                int start = str.lastIndexOf(operator);
//                d2 = Double.parseDouble(str.substring(start + 1, str.length()));
//                getResult(d1, d2, operator);
                if (flag) {
                    str = "";
                    flag = false;
                    editText.setText("");
                    editText_2.setText("");
                } else getResult(str);
                break;


        }
    }
    private void getResult(String s) {
        if (s == null || s.isEmpty()) return;
        if (!isValid(s)) {
            editText_2.setText("input invalid");
        } else {
            //if (s.charAt(0) == '-' || s.charAt(0) == '+') s = 0 + s;
            BigDecimal res = new FormulaUtil().caculate(s);
            if (res == null) {
                editText_2.setText("divisor is zero");
                flag = true;
                return;
            }
            String r = res +"";

            // limit length and rounding
            if (r.length() > 15)  {
                if (r.charAt(15) - '0' >= 5) {
                    char c = (char)(r.charAt(15) + 1);
                    r = r.substring(0, 14);
                    r = r + c;
                } else r = r.substring(0, 15);
            }
            int idx = r.indexOf('.');
            if (idx != -1) {
                int i = idx+1;
                for (; i < r.length(); i++) {
                    if (r.charAt(i) != '0') break;
                }
                if (i == r.length()) r = r.substring(0, idx);
            }

             editText_2.setText(r);
        }


        flag = true;

    }


    private boolean isValid(String s) {

        int left = 0, right = 0;
        //*123 is invalid
        if (s.charAt(0) == '*' || s.charAt(0) == '/' || (!Character.isDigit(s.charAt(s.length() - 1)) && s.charAt(s.length() - 1) != ')')) return false;
        int dot = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '.') dot++;
            if (dot > 1) return false;
            if (c == '+' || c == '-' || c =='*' || c =='/' || c == '(' || c == ')') dot = 0;

            // check '(' and ')' valid
            if (c == '(') {
                left++;
                if (i != 0 && (s.charAt(i-1) == ')' || Character.isDigit(s.charAt(i-1)))) return false;
            }
            else if (c == ')') {
                if (i == 0 || s.charAt(i-1) == '(' || s.charAt(i-1) == '+' || s.charAt(i-1) == '-' || s.charAt(i-1) == '*' || s.charAt(i-1) == '/') return false;
                right++;
            }
            if (right > left) return false;

            //the front of '*' and '/' is ')' or digit
            if (c == '/' || c =='*') {
                if (s.charAt(i-1) != ')' && !Character.isDigit(s.charAt(i-1))) return false;
            }

            if (c == '/') {
                if (i == s.length() - 1) return false;
            }
            // the front of '-' must be '(' , ')' or digit
            if (c == '-') {
                if (i != 0 && s.charAt(i-1) != '(' && s.charAt(i-1) != ')' && !Character.isDigit(s.charAt(i-1))) return false;
            }

            // the operator cannot be adjacent
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                if (i != 0 && (s.charAt(i-1) == '+' || s.charAt(i-1) == '-' || s.charAt(i-1) == '*' || s.charAt(i-1) == '/')) return false;
            }

        }

        return left == right;
    }

//    private void getResult(double d1, double d2, String op) {
//        String str = editText.getText().toString();
//        double res = 0;
//        if (operator.equals("+")) {
//            res = d1 + d2;
//        } else if (operator.equals("-")) {
//            res = d1 - d2;
//        } else if (operator.equals("*")) {
//            res = d1 * d2;
//        } else if (operator.equals("/")) {
//            if (d2 == 0) {
//                res = -1;
//            }
//            else res = d1 / d2;
//        }
//
//        if (res == -1) {
//            editText_2.setText("divisor cannot be zero");
//        } else {
//            if (!str.contains(".") && !operator.equals("/")) {
//                editText_2.setText((int)res + "");
//            } else {
//                editText_2.setText(res + "");
//            }
//        }
//    }


    protected void init() {
        btn_0 = (Button)findViewById(R.id.btn_0);
        btn_1 = (Button)findViewById(R.id.btn_1);
        btn_2 = (Button)findViewById(R.id.btn_2);
        btn_3 = (Button)findViewById(R.id.btn_3);
        btn_4 = (Button)findViewById(R.id.btn_4);
        btn_5 = (Button)findViewById(R.id.btn_5);
        btn_6 = (Button)findViewById(R.id.btn_6);
        btn_7 = (Button)findViewById(R.id.btn_7);
        btn_8 = (Button)findViewById(R.id.btn_8);
        btn_9 = (Button)findViewById(R.id.btn_9);
        btn_del = (Button)findViewById(R.id.btn_del);
        btn_left = (Button)findViewById(R.id.btn_left);
        btn_right = (Button)findViewById(R.id.btn_right);
        btn_minus = (Button)findViewById(R.id.btn_minus);
        btn_plus = (Button)findViewById(R.id.btn_plus);
        btn_mul = (Button)findViewById(R.id.btn_multiple);
        btn_div = (Button)findViewById(R.id.btn_divide);
        btn_dot = (Button)findViewById(R.id.btn_dot);
        btn_equal = (Button)findViewById(R.id.btn_equal);
        editText = (EditText)findViewById(R.id.et_Text);
        editText_2 = (EditText)findViewById(R.id.et_Text_2);

        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                editText.setText("");
                editText_2.setText("");
                return false;
            }
        });
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_plus.setOnClickListener(this);
        btn_mul.setOnClickListener(this);
        btn_div.setOnClickListener(this);
        btn_dot.setOnClickListener(this);
        btn_equal.setOnClickListener(this);
    }


}
