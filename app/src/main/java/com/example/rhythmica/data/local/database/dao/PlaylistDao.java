package com.example.rhythmica.data.local.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.lifecycle.LiveData;

import com.example.rhythmica.data.local.database.entities.Playlist;
import java.util.List;

@Dao
public interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPlaylist(Playlist playlist);

    @Update
    void updatePlaylist(Playlist playlist);

    @Delete
    void deletePlaylist(Playlist playlist);

    @Query("SELECT * FROM playlists ORDER BY updated_at DESC")
    LiveData<List<Playlist>> getAllPlaylists();

    @Query("SELECT * FROM playlists WHERE is_local = 1 ORDER BY name ASC")
    LiveData<List<Playlist>> getLocalPlaylists();

    @Query("SELECT * FROM playlists WHERE is_local = 0 ORDER BY updated_at DESC")
    LiveData<List<Playlist>> getSyncedPlaylists();

    @Query("SELECT * FROM playlists WHERE id = :playlistId LIMIT 1")
    LiveData<Playlist> getPlaylistById(int playlistId);

    @Query("SELECT * FROM playlists WHERE id = :playlistId LIMIT 1")
    Playlist getPlaylistByIdSync(int playlistId);

    @Query("SELECT * FROM playlists WHERE name LIKE '%' || :query || '%'")
    LiveData<List<Playlist>> searchPlaylists(String query);

    @Query("DELETE FROM playlists WHERE is_local = 0")
    void deleteSyncedPlaylists();

    @Query("SELECT COUNT(*) FROM playlists")
    int getPlaylistCount();
}