package com.example.rhythmica.data.local.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "songs")
public class Song {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "api_id")
    public Long apiId; // ID из Deezer API, может быть null для локальных файлов

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "artist")
    public String artist;

    @ColumnInfo(name = "genre")
    public String genre;

    @ColumnInfo(name = "duration")
    public long duration; // Длительность в миллисекундах

    @ColumnInfo(name = "file_path")
    public String filePath; // Путь к локальному файлу

    @ColumnInfo(name = "album_art_path")
    public String albumArtPath; // Путь к обложке альбома

    @ColumnInfo(name = "is_local")
    public boolean isLocal; // true для локальных файлов, false для загруженных

    @ColumnInfo(name = "date_added")
    public long dateAdded; // Timestamp когда песня была добавлена

    // Конструкторы
    public Song() {
        this.dateAdded = System.currentTimeMillis();
    }

    public Song(String title, String artist, String filePath) {
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
        this.isLocal = true;
        this.dateAdded = System.currentTimeMillis();
    }

    public Song(String title, String artist, String genre, String filePath, Long apiId) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.filePath = filePath;
        this.apiId = apiId;
        this.isLocal = false;
        this.dateAdded = System.currentTimeMillis();
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAlbumArtPath() {
        return albumArtPath;
    }

    public void setAlbumArtPath(String albumArtPath) {
        this.albumArtPath = albumArtPath;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    // Утилитарные методы
    public String getDisplayName() {
        if (title != null && artist != null) {
            return title + " - " + artist;
        } else if (title != null) {
            return title;
        } else {
            return "Unknown";
        }
    }

    public String getDurationFormatted() {
        if (duration <= 0) return "0:00";

        long seconds = duration / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;

        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", genre='" + genre + '\'' +
                ", isLocal=" + isLocal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;

        if (id != 0 && song.id != 0) {
            return id == song.id;
        }

        // Сравнение по title и artist для локальных файлов
        if (title != null ? !title.equals(song.title) : song.title != null) return false;
        return artist != null ? artist.equals(song.artist) : song.artist == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        return result;
    }
}