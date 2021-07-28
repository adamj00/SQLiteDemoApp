package com.example.sqldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_add, btn_viewAll;
    EditText et_name, et_age;
    Switch sw_activeCustomer;
    ListView lv_customerList;
    ArrayAdapter<CustomerModel> customerArrayAdapter;
    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create an instance of DataBaseHelper
        dataBaseHelper = new DataBaseHelper(MainActivity.this);


        // references to controls on the layout
        btn_add = findViewById(R.id.btn_add);
        btn_viewAll = findViewById(R.id.btn_viewAll);
        et_age = findViewById(R.id.et_age);
        et_name = findViewById(R.id.et_name);
        sw_activeCustomer = findViewById(R.id.sw_active);
        lv_customerList = findViewById(R.id.lv_customers);
        showList();

        // button listeners

        // "Add" button listener
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerModel customerModel;

                // inserting customer to the database using input data
                try {
                    customerModel = new CustomerModel(-1, et_name.getText().toString(),  Integer.parseInt(et_age.getText().toString()), sw_activeCustomer.isChecked());
                    //Toast.makeText(MainActivity.this, customerModel.toString(), Toast.LENGTH_SHORT).show();
                }
                // if creating customer fails, insert default customer
                catch (Exception e) {
                    //Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    customerModel = new CustomerModel(-1, "error",  0, false);
                }


                // show if the insert succeed
                boolean success = dataBaseHelper.addOne(customerModel);
                if (success) {
                    Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
                }

                // show the list
                showList();
            }
        });

        // "View All" button listener
        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // show the list
                showList();
            }
        });

        // set lv item click listener
        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // get the customer that was clicked
                CustomerModel clickedCustomer = (CustomerModel) adapterView.getItemAtPosition(i);

                // delete this customer
                dataBaseHelper.deleteOne(clickedCustomer);

                // update the list
                showList();
            }
        });
    }

    private void showList() {
        // show list
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryOne());
        lv_customerList.setAdapter(customerArrayAdapter);
    }
}