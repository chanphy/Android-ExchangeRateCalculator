package zucc.com.myapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.R.attr.name;

/**
 * Created by admin on 2017/7/2.
 */

public class HistoryActivity extends AppCompatActivity {

    private EditText eSearch;
    private Database dbHelper;
    private SQLiteDatabase sd;
    private ArrayList<History_info> historylist;
    private ListView lv;
    private BaseAdapter adapter;
    private ArrayList<History_info> mData=new ArrayList<>();
    ArrayList<String> mAmount = new ArrayList<String>();
    ArrayList<String> fornumber  = new ArrayList<String>();
    ArrayList<String> homnumber = new ArrayList<String>();
    ArrayList<String> mConverted = new ArrayList<String>();
    Handler myhandler = new Handler();



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_open_helper);
        dbHelper = new Database(this, "history.db", null, 1);
        sd = dbHelper.getWritableDatabase();
        historylist = new ArrayList<>();
        Cursor cursor = sd.rawQuery("select * from history", null);






        if (cursor.moveToFirst()) {
            do {
                String mAmount = cursor.getString(cursor.getColumnIndex("mAmount"));
                String fornumber = cursor.getString(cursor.getColumnIndex("fornumber"));
                String homnumber = cursor.getString(cursor.getColumnIndex("homnumber"));
                String mConverted = cursor.getString(cursor.getColumnIndex("mConverted"));
                History_info st = new History_info(mAmount, fornumber, homnumber, mConverted);    //student_info存一个条目的数据
                historylist.add(st);//把数据库的每一行加入数组中
            } while (cursor.moveToNext());
        }
        cursor.close();


        lv = (ListView) findViewById(R.id.lv);
        initDatas();


        adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return historylist.size();
            }
            //ListView的每一个条目都是一个view对象
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;

                if(convertView==null){
                    view = View.inflate(getBaseContext(),R.layout.studentlayout,null);
                }
                else{
                    view = convertView;
                }

                //从并通过Adapter把currencieslist的信息显示到ListView中取出一行数据，position相当于数组下标,可以实现逐行取数据
                History_info st = historylist.get(position);
                TextView shuru = (TextView) view.findViewById(R.id.shuru);
                TextView waibi = (TextView) view.findViewById(R.id.waibi);
                TextView benbi = (TextView) view.findViewById(R.id.benbi);
                TextView shuchu = (TextView) view.findViewById(R.id.shuchu);
                shuru.setText(st.getmAmount());
                waibi.setText(st.getFornumber());
                benbi.setText(st.getHomnumber());
                shuchu.setText(st.getmConverted());
                return view;
            }
            @Override
            public Object getItem(int position) {
                return null;
            }
            @Override
            public long getItemId(int position) {
                return 0;
            }
        };
        lv.setAdapter(adapter);
        set_eSearch_TextChanged();




    }

    private void set_eSearch_TextChanged()
    {
        eSearch = (EditText) findViewById(R.id.etSearch);

        eSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                //这是文本框改变之前会执行的动作
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                /**这是文本框改变之后 会执行的动作
                 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
                 * 所以这里我们就需要加上数据的修改的动作了。
                 */


                    myhandler.post(eChanged);

            }
        });

    }


    Runnable eChanged = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String data = eSearch.getText().toString();

            mData.clear();

            getmDataSub(mData, data);

            adapter.notifyDataSetChanged();

        }
    };

    private void getmDataSub(ArrayList<History_info> mDataSubs, String data)
    {
        int length = homnumber.size();
        for(int i = 0; i < length; ++i){
            if(mAmount.get(i).contains(data)||fornumber.get(i).contains(data)||homnumber.get(i).contains(data) || mConverted.get(i).contains(data)){
                History_info item = new History_info();
                item.setmAmount( mAmount.get(i)+"");
                item.setFornumber(fornumber.get(i)+"");
                item.setHomnumber(homnumber.get(i)+"");
                item.setmConverted(mConverted.get(i)+"");
                mDataSubs.add(item);
            }
        }
    }

    private void initDatas()
    {
        mData =historylist;
        for (History_info d:mData )
        {
            if(d!=null) {
                mAmount.add(d.getmAmount());
                fornumber.add(d.getFornumber());
                homnumber.add(d.getHomnumber());
                mConverted.add(d.getmConverted());
            }
        }
    }




}



