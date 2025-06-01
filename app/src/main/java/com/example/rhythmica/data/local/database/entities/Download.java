package com.example.rhythmica.data.local.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

@Entity(
        tableName = "downloads",
        foreignKeys = @ForeignKey(
                entity = Song.class,
                parentColumns = "id",
                childColumns = "song_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class Download {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "song_id")
    public int songId;

    @ColumnInfo(name = "download_url")
    public String downloadUrl;

    @ColumnInfo(name = "status")
    public DownloadStatus status;

    @ColumnInfo(name = "progress")
    public int progress; // 0-100

    @ColumnInfo(name = "file_size")
    public long fileSize;

    @ColumnInfo(name = "downloaded_size")
    public long downloadedSize;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    @ColumnInfo(name = "completed_at")
    public Long completedAt;

    public enum DownloadStatus {
        PENDING,
        DOWNLOADING,
        COMPLETED,
        FAILED,
        PAUSED
    }

    public Download() {
        this.createdAt = System.currentTimeMillis();
        this.status = DownloadStatus.PENDING;
    }
}