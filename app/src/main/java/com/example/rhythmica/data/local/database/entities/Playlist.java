package com.example.rhythmica.data.local.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "playlists")
public class Playlist {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "cover_art_path")
    public String coverArtPath;

    @ColumnInfo(name = "song_ids")
    public String songIds; // JSON array of song IDs

    @ColumnInfo(name = "is_local")
    public boolean isLocal; // true для локальных плейлистов

    @ColumnInfo(name = "created_at")
    public long createdAt;

    @ColumnInfo(name = "updated_at")
    public long updatedAt;

    public Playlist() {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.isLocal = true;
    }

    public Playlist(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public void updateTimestamp() {
        this.updatedAt = System.currentTimeMillis();
    }
}

// Converters.java - для Room TypeConverters
package com.example.rhythmica.data.local.database;

import androidx.room.TypeConverter;
import com.example.rhythmica.data.local.database.entities.Download;

public class Converters {

    @TypeConverter
    public static String fromDownloadStatus(Download.DownloadStatus status) {
        return status == null ? null : status.name();
    }

    @TypeConverter
    public static Download.DownloadStatus toDownloadStatus(String status) {
        return status == null ? null : Download.DownloadStatus.valueOf(status);
    }
}