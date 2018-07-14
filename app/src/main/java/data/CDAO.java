package data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CDAO {
    @Insert(onConflict = REPLACE)
    void insertC(Data c);

    @Query("SELECT * FROM char")
    List<Data> getC();



    @Query ("SELECT * FROM char WHERE name = :n")
    Data fetchC (String n);

    //include any methods to fetch specific user
    // @Query("SELECT * FROM User")
}

