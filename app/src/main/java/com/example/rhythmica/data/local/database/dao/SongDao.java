package com.example.rhythmica.data.local.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.lifecycle.LiveData;

import com.example.rhythmica.data.local.database.entities.Song;
import java.util.List;

@Dao
public interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSong(Song song);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSongs(List<Song> songs);

    @Update
    void updateSong(Song song);

    @Delete
    void deleteSong(Song song);

    @Query("SELECT * FROM songs ORDER BY date_added DESC")
    LiveData<List<Song>> getAllSongs();

    @Query("SELECT * FROM songs WHERE is_local = 1 ORDER BY title ASC")
    LiveData<List<Song>> getLocalSongs();

    @Query("SELECT * FROM songs WHERE is_local = 0 ORDER BY date_added DESC")
    LiveData<List<Song>> getDownloadedSongs();

    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
    LiveData<List<Song>> searchSongs(String query);

    @Query("SELECT * FROM songs WHERE genre = :genre ORDER BY title ASC")
    LiveData<List<Song>> getSongsByGenre(String genre);

    @Query("SELECT * FROM songs WHERE artist = :artist ORDER BY title ASC")
    LiveData<List<Song>> getSongsByArtist(String artist);

    @Query("SELECT DISTINCT genre FROM songs WHERE genre IS NOT NULL ORDER BY genre ASC")
    LiveData<List<String>> getAllGenres();

    @Query("SELECT DISTINCT artist FROM songs WHERE artist IS NOT NULL ORDER BY artist ASC")
    LiveData<List<String>> getAllArtists();

    @Query("DELETE FROM songs WHERE is_local = 0")
    void deleteDownloadedSongs();

    @Query("SELECT COUNT(*) FROM songs")
    int getSongCount();

    @Query("SELECT * FROM songs WHERE id = :songId LIMIT 1")
    Song getSongById(int songId);
}
