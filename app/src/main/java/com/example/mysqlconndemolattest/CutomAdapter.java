package com.example.mysqlconndemolattest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CutomAdapter  extends BaseAdapter {

    public int id;
    public String fullname;
    public String address;
    public String contact;
    Activity context;
    List<Employee> employeeList;
    LayoutInflater inflater;
    public CutomAdapter(Activity context, List<Employee> employeeList) {

        this.context = context;
        this.employeeList = employeeList;
        inflater =context.getLayoutInflater();

    }

    @Override
    public int getCount() {
        return employeeList.size();
    }

    @Override
    public Object getItem(int i) {
        return employeeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

       //View view1=inflater.inflate(R.layout.list_item_view,viewGroup);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view1 = inflater.inflate(R.layout.list_item_view, null, true);

        TextView txtfullname=view1.findViewById(R.id.txtfullname);
        TextView txtaddress=view1.findViewById(R.id.txtaddress);
        TextView txtcontact=view1.findViewById(R.id.txtcontact);

        Employee employee=employeeList.get(i);
        txtfullname.setText(employee.getFullname());
        txtaddress.setText(employee.getAddress());
        txtcontact.setText(employee.getContact());

        return view1;
    }
}
