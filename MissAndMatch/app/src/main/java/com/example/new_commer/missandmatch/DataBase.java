package com.example.new_commer.missandmatch;

/**
 * Created by Changgil on 2016-12-05.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name,factory,version);
    }
    //새로생성할때 호출되는함수
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE db_record (_id INTEGER PRIMARY KEY AUTOINCREMENT, score INTEGER, round INTEGER, time INTEGER)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS db_record");
        onCreate(db);
    }


    public void insert(int score, int round, int time) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO db_record VALUES(null, '" + score + " ', " + round + ",' " + time + "');");
        db.close();
    }

    public void delete() {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM db_record");
        db.close();
    }

    public String getResult() {

        int a=1;
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "No  Score  round   time" +"\n" ;

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM db_record order by score desc ,round asc,time asc", null);
        while (cursor.moveToNext())
        {
            //round
            if(a==11)
                break;
            else if(a==10) {
                result += String.format("% 3d", a++);
            }
            else {
                result += String.format("% 4d", a++);
            }

            //score
            if(cursor.getInt(1)/100 != 0)
            {
                result +=  String.format("% 9d",cursor.getInt(1));
            }
            else if(cursor.getInt(1)/10 != 0)
            {
                result +=  String.format("% 10d",cursor.getInt(1));
            }
            else
            {
                result +=  String.format("% 11d",cursor.getInt(1));
            }
            //round
            if(cursor.getInt(2)/100 != 0)
            {
                result +=  String.format("% 9d",cursor.getInt(2));
            }
            else if(cursor.getInt(2)/10 != 0)
            {
                result +=  String.format("% 10d",cursor.getInt(2));
            }
            else
            {
                result +=  String.format("% 11d",cursor.getInt(2));
            }
            //time
            if(cursor.getInt(3)/100 != 0)
            {
                result +=  String.format("% 7d",cursor.getInt(3))+ "\n";
            }
            else if(cursor.getInt(3)/10 != 0)
            {
                result +=  String.format("% 8d",cursor.getInt(3))+ "\n";
            }
            else
            {
                result +=  String.format("% 9d",cursor.getInt(3))+ "\n";
            }
        }
        return result;
    }
}
