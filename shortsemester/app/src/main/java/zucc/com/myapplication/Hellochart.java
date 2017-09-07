
package zucc.com.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by admin on 2017/7/5.
 */

public class Hellochart extends Activity {
    private LineChartView lineChart;
    private HelloDatabase dbHelper;
    private ArrayList<Hello_info> hellolist;
    private ArrayList<Hello_info> mData = new ArrayList<>();
    ArrayList<Integer> date = new ArrayList<Integer>();
    ArrayList<String> score = new ArrayList<String>();
    private String mKey;
    public static final String RATES = "rates";
    public static final String URL_BASE =
            "http://openexchangerates.org/api/latest.json?app_id=";
    //used to format data from openexchangerates.org
    private static final DecimalFormat DECIMAL_FORMAT = new
            DecimalFormat("#,##0.00000");
    Handler myhandler = new Handler();
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private SQLiteDatabase sd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        lineChart = (LineChartView) findViewById(R.id.line_chart);
        dbHelper = new HelloDatabase(this, "hellochart.db", null, 1);
        hellolist = new ArrayList<>();
        sd = dbHelper.getWritableDatabase();
        mKey = getKey("open_key");
        getfirst();
        myhandler.postDelayed(runnable, 1000);
/*Cursor cursor = sd.rawQuery("select * from hellochart", null);
        double rate = 5.0;
        if (cursor.moveToFirst()) {
            do {
                Integer date1 = cursor.getInt(cursor.getColumnIndex("id"));
                String score1 = cursor.getString(cursor.getColumnIndex("cnyrate"));

                Hello_info st = new Hello_info(date1, score1);    //student_info存一个条目的数据
                hellolist.add(st);//把数据库的每一行加入数组中
                date.add(date1);
                score.add(score1);


            } while (cursor.moveToNext());


        }
        cursor.close();
        getAxisXLables1();//��ȡx��ı�ע
        getAxisPoints1();//��ȡ�����
        initLineChart();//��ʼ��*//*
 */
    }


private  void getfirst(){
    Cursor cursor = sd.rawQuery("select * from hellochart", null);
    double rate = 5.0;
        if (cursor.moveToFirst()) {
            do {
                Integer date1 = cursor.getInt(cursor.getColumnIndex("id"));
                String score1 = cursor.getString(cursor.getColumnIndex("cnyrate"));
                date.add(date1);
                score.add(score1);
            } while (cursor.moveToNext());
            cursor.close();
        }
    if(date.size()==0){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues rates = new ContentValues();
        rates.put("cnyrate", "6.0");
        db.insert("hellochart", null, rates);
        rates.clear();
        date.clear();
        score.clear();
    }
else {
        getAxisXLables1();//��ȡx��ı�ע
        getAxisPoints1();//��ȡ�����
        initLineChart();
        date.clear();
        score.clear();
    }
}



    /**
     * ��ʼ��LineChart��һЩ����
     */
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
if(!isFinishing()){

            new CurrencyConverterTask().execute(URL_BASE + mKey);
            Cursor cursor = sd.rawQuery("select * from hellochart", null);

            if (cursor.moveToFirst()) {
                do {
                    Integer date1 = cursor.getInt(cursor.getColumnIndex("id"));
                    String score1 = cursor.getString(cursor.getColumnIndex("cnyrate"));

                    Hello_info st = new Hello_info(date1, score1);    //student_info存一个条目的数据
                    hellolist.add(st);//把数据库的每一行加入数组中
                    date.add(date1);
                    score.add(score1);


                } while (cursor.moveToNext());


            }
            cursor.close();
            getAxisXLables();//��ȡx��ı�ע
            getAxisPoints();//��ȡ�����
            initLineChart();//��ʼ��
            date.clear();
            score.clear();
            myhandler.postDelayed(this, 5000);
        }}


    };






    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#CC33FF"));  //���ߵ���ɫ
        List<Line> lines = new ArrayList<Line>();
        LineChartValueFormatter chartValueFormatter = new SimpleLineChartValueFormatter(4);
        line.setFormatter(chartValueFormatter);
        line.setShape(ValueShape.SQUARE);//����ͼ��ÿ�����ݵ����״  ������Բ�� �������� ��ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE��
        line.setCubic(false);//�����Ƿ�ƽ��
//	    line.setStrokeWidth(3);//�����Ĵ�ϸ��Ĭ����3
        line.setFilled(false);//�Ƿ�������ߵ����
        line.setHasLabels(true);//���ߵ����������Ƿ���ϱ�ע
//		line.setHasLabelsOnlyForSelected(true);//�������������ʾ���ݣ����������line.setHasLabels(true);����Ч��
        line.setHasLines(true);//�Ƿ���ֱ����ʾ�����Ϊfalse ��û������ֻ�е���ʾ
        line.setHasPoints(true);//�Ƿ���ʾԲ�� ���Ϊfalse ��û��ԭ��ֻ�е���ʾ
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //������
        Axis axisX = new Axis(); //X��
        axisX.setHasTiltedLabels(true);  //X������������������б����ʾ����ֱ�ģ�true��б����ʾ
//	    axisX.setTextColor(Color.WHITE);  //����������ɫ
        axisX.setTextColor(Color.parseColor("#000000"));//��ɫ

//	    axisX.setName("δ�����������");  //�������
        axisX.setTextSize(11);//���������С
        axisX.setMaxLabelChars(7); //��༸��X�����꣬��˼�������������X�������ݵĸ���7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //���X�����������
        data.setAxisXBottom(axisX); //x ���ڵײ�
//	    data.setAxisXTop(axisX);  //x ���ڶ���
        axisX.setHasLines(true); //x ��ָ���


        Axis axisY = new Axis();  //Y�
        axisY.setName("");//y���ע
        axisY.setTextSize(11);
        axisY.setMaxLabelChars(5);
        data.setAxisYLeft(axisY);
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 3);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }





    private void getAxisXLables() {
        for (int i = date.size()-1; i < date.size(); i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date.get(i) + ""));
        }
    }

    private void getAxisPoints() {
        for (int i = score.size()-1; i < score.size(); i++) {
            mPointValues.add(new PointValue(i, Float.parseFloat(score.get(i))));
        }
    }

    private void getAxisXLables1() {
        for (int i = 0; i < date.size(); i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date.get(i) + ""));
        }
    }

    private void getAxisPoints1() {
        for (int i = 0; i < score.size(); i++) {
            mPointValues.add(new PointValue(i, Float.parseFloat(score.get(i))));
        }
    }

    private String getKey(String keyName) {
        AssetManager assetManager = this.getResources().getAssets();
        Properties properties = new Properties();
        try {
            InputStream inputStream = assetManager.open("keys.properties");
            properties.load(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(keyName);

    }


          private class CurrencyConverterTask extends AsyncTask<String, Void, JSONObject> {
            private ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(Hellochart.this);
                progressDialog.setTitle("汇率获取中...");
                progressDialog.setMessage("请稍等...");
                progressDialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                return new JSONParser().getJSONFromUrl(params[0]);

            }


        protected void onPostExecute(JSONObject jsonObject) {

            double cnyrate = 0.0;

            try {

                JSONObject jsonRates = jsonObject.getJSONObject(RATES);
                cnyrate = jsonRates.getDouble("CNY");

            } catch (JSONException e) {
                Toast.makeText(
                        Hellochart.this,
                        "There's been a JSON exception: " + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();

                e.printStackTrace();
            }
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues rates = new ContentValues();
            rates.put("cnyrate", cnyrate);
            db.insert("hellochart", null, rates);
            rates.clear();

            progressDialog.dismiss();



        }


    }


}
