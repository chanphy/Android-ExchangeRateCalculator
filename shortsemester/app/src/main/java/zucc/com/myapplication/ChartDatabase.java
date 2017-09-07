
package zucc.com.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by admin on 2017/7/5.
 */

public class ChartDatabase extends SQLiteOpenHelper {
    //创建表SQL语句
    private static final String CREATE_CHARTT="create table chart(id integer primary key autoincrement," +
            "cnyrate text)";

    //SQLiteDatabase实例
    private SQLiteDatabase db;
    private Context mContext;
    /*
     * 构造方法
     */
    public  ChartDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory , int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    /*
     * 创建表
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CHARTT);
        Toast.makeText(mContext,"create successed",Toast.LENGTH_SHORT).show();
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("drop table if exists chart");
        onCreate(db);
    }
}