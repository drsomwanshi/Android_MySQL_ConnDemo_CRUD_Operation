package com.example.mysqlconndemolattest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

//    String url="http://sislatur.org.in/drs/getAllEmp.php";
//    String url1="http://sislatur.org.in/drs/insertEmp.php";
//    String updateempurl="http://sislatur.org.in/drs/updateEmp.php";
//    String deleteempurl="http://sislatur.org.in/drs/deleteEmp.php";

    String url="http://reslatur.org.in/drs/getAllEmp.php";
    String url1="http://reslatur.org.in/drs/insertEmp.php";
    String updateempurl="http://reslatur.org.in/drs/updateEmp.php";
    String deleteempurl="http://reslatur.org.in/drs/deleteEmp.php";

    public List<Employee> allemp;
    ListView lst;
   // ArrayAdapter adapter;
   CutomAdapter adapter;
    Employee selectedemp=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loademp();
        allemp=new ArrayList<Employee>();
        lst=findViewById(R.id.lst1);
        registerForContextMenu(lst);
       lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               selectedemp=(Employee) adapterView.getItemAtPosition(i);
               return false;
           }
       });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.my_menu,menu);

    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

       if(item.getItemId()==R.id.update)
       {
           AlertDialog.Builder b=new AlertDialog.Builder(MainActivity.this);
           final View customLayout = getLayoutInflater().inflate(R.layout.update_alert_view, null);

           //Employee emp=(Employee) lst.getSelectedItem();
         //  Toast.makeText(this, "" + emp.toString(), Toast.LENGTH_SHORT).show();

           EditText txtfullname=customLayout.findViewById(R.id.txtfullname);
           EditText txtaddress=customLayout.findViewById(R.id.txtaddress);
           EditText txtcontact=customLayout.findViewById(R.id.txtcontact);

           txtfullname.setText("" + selectedemp.getFullname().toString());
           txtaddress.setText("" + selectedemp.getAddress().toString());
           txtcontact.setText("" + selectedemp.getContact().toString());

           b.setView(customLayout);

           AlertDialog d=b.create();
           d.show();

           Button b1=customLayout.findViewById(R.id.btnupdate);
           b1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   String fullname=txtfullname.getText().toString();
                   String address=txtaddress.getText().toString();
                   String contact=txtcontact.getText().toString();


                   int id=selectedemp.getId();

                   updateemp(id,fullname,address,contact);
                   lst.invalidateViews();

               }
           });

       }
        if(item.getItemId()==R.id.delete)
        {
            int id=selectedemp.getId();
            deleteemp(id);
            lst.invalidateViews();

        }
        if(item.getItemId()==R.id.exit)
        {
            finish();
        }

        return super.onContextItemSelected(item);
    }

    private void loademp() {

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                try {

                    allemp=new ArrayList<Employee>();

                JSONArray array = new JSONArray(response);
                    String data[]=new String[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject emp = array.getJSONObject(i);
                    int id=emp.getInt("id");
                    String fullname=emp.getString("fullname");
                    String address=emp.getString("address");
                    String contact=emp.getString("contact");
                    allemp.add(new Employee(id,fullname,address,contact));
                    data[i]=id + "\n" + fullname + "\n" + address + "\n" + contact + "\n";
                }
               // adapter=new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,data);
                adapter=new CutomAdapter(MainActivity.this,allemp);
                lst.setAdapter(adapter);

            }
            catch(JSONException e)
            {
                e.fillInStackTrace();
                System.out.println( "Error1: " + e.getMessage());
            }
            }
        },
        new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.fillInStackTrace();
                System.out.println( "Error2: " + error.getMessage());
            }
        }
        );
//adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }


    public void adddialog(View view)
    {
        AlertDialog.Builder b=new AlertDialog.Builder(MainActivity.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.update_alert_view, null);


        EditText txtfullname=customLayout.findViewById(R.id.txtfullname);
        EditText txtaddress=customLayout.findViewById(R.id.txtaddress);
        EditText txtcontact=customLayout.findViewById(R.id.txtcontact);

        b.setView(customLayout);

        AlertDialog d=b.create();
        d.show();

        Button b1=customLayout.findViewById(R.id.btnupdate);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullname=txtfullname.getText().toString();
                String address=txtaddress.getText().toString();
                String contact=txtcontact.getText().toString();

                insertemp(fullname,address,contact);
                lst.invalidateViews();

            }
        });

    }

    public void insertemp(String fullname,String address, String contact) {

    StringRequest stringRequest=new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Toast.makeText(MainActivity.this, "Msg:" + response.toString(), Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
            loademp();
        }
    },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
    ) {
        protected Map<String, String> getParams() throws AuthFailureError {

            Map<String, String> params = new HashMap<>();
            //Adding the parameters to the request
            params.put("fullname", "" + fullname);
            params.put("address", "" + address);
            params.put("contact", "" + contact);

            return params;
        }
    };
        Volley.newRequestQueue(this).add(stringRequest);

    }

    public void updateemp(int id, String fullname,String address, String contact) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, updateempurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(MainActivity.this, "Msg:" + response.toString(), Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                loademp();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("id", "" + id);
                params.put("fullname", "" + fullname);
                params.put("address", "" + address);
                params.put("contact", "" + contact);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }

    public void deleteemp(int id) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, deleteempurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(MainActivity.this, "Msg:" + response.toString(), Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                loademp();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("id", "" + id);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }

    public void show(View view) {

        //loademp();
        adapter.notifyDataSetChanged();
        loademp();

    }
}