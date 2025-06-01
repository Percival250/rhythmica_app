package com.example.rhythmica.data.local.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.example.rhythmica.data.local.database.dao.PlaylistDao;
import com.example.rhythmica.data.local.database.dao.SongDao;
import com.example.rhythmica.data.local.database.dao.UserDao;
import com.example.rhythmica.data.local.database.entities.Download;
import com.example.rhythmica.data.local.database.entities.Playlist;
import com.example.rhythmica.data.local.database.entities.Song;
import com.example.rhythmica.data.local.database.entities.User;

@Database(
        entities = {
                User.class,
                Song.class,
                Download.class,
                Playlist.class
        },
        version = 1,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class RhythmicaDatabase extends RoomDatabase {

    private static volatile RhythmicaDatabase INSTANCE;
    private static final String DATABASE_NAME = "rhythmica_db";

    // DAO methods
    public abstract UserDao userDao();
    public abstract SongDao songDao();
    public abstract PlaylistDao playlistDao();

    public static RhythmicaDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RhythmicaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    RhythmicaDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}