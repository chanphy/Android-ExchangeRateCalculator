
package zucc.com.myapplication;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

/**
 * Created by who i am on 2016/11/13.
 */
public class Database extends SQLiteOpenHelper{
    //创建表SQL语句
    private static final String CREATE_HISTORY="create table history(id integer primary key autoincrement," +
            "mAmount text" +
            ",fornumber text" +
            ",homnumber text" +
            ",mConverted text)";

    //SQLiteDatabase实例
    private SQLiteDatabase db;
    private Context mContext;
    /*
     * 构造方法
     */
    public  Database(Context context, String name, SQLiteDatabase.CursorFactory factory , int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    /*
     * 创建表
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_HISTORY);
        Toast.makeText(mContext,"create successed",Toast.LENGTH_SHORT).show();
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("drop table if exists history");
        onCreate(db);
    }
}