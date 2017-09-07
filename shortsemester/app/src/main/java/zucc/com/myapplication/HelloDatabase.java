
package zucc.com.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by admin on 2017/7/6.
 */

public class HelloDatabase extends SQLiteOpenHelper {
    //创建表SQL语句
    private static final String CREATE_HELLOCHARTT="create table hellochart(id integer primary key autoincrement," +
            "cnyrate text)";

    //SQLiteDatabase实例
    private SQLiteDatabase db;
    private Context mContext;
    /*
     * 构造方法
     */
    public  HelloDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory , int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    /*
     * 创建表
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_HELLOCHARTT);
        Toast.makeText(mContext,"create successed",Toast.LENGTH_SHORT).show();
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("drop table if exists hellochart");
        onCreate(db);
    }
}
