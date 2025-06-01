package com.example.rhythmica;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.rhythmica.databinding.ActivityMainBinding;
import com.example.rhythmica.services.MusicPlayerService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements MusicPlayerService.MusicPlayerListener {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private ActivityMainBinding binding;
    private MusicPlayerService musicPlayerService;
    private boolean isServiceBound = false;

    // UI элементы мини-плеера
    private LinearLayout miniPlayer;
    private ImageView miniAlbumArt;
    private TextView miniSongTitle;
    private TextView miniArtistName;
    private ImageButton miniPlayPause;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayerService.MusicBinder binder = (MusicPlayerService.MusicBinder) service;
            musicPlayerService = binder.getService();
            musicPlayerService.setMusicPlayerListener(MainActivity.this);
            isServiceBound = true;

            // Обновляем UI если уже играет музыка
            if (musicPlayerService.isPlaying()) {
                updateMiniPlayer();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
            musicPlayerService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Инициализация UI элементов
        initViews();

        // Проверка разрешений
        checkPermissions();

        // Настройка Navigation
        setupNavigation();

        // Запуск и привязка к сервису плеера
        startAndBindMusicService();
    }

    private void initViews() {
        miniPlayer = binding.miniPlayer;
        miniAlbumArt = binding.miniAlbumArt;
        miniSongTitle = binding.miniSongTitle;
        miniArtistName = binding.miniArtistName;
        miniPlayPause = binding.miniPlayPause;

        // Обработчик клика на кнопку воспроизведения в мини-плеере
        miniPlayPause.setOnClickListener(v -> {
            if (isServiceBound && musicPlayerService != null) {
                if (musicPlayerService.isPlaying()) {
                    musicPlayerService.pauseMusic();
                } else {
                    musicPlayerService.resumeMusic();
                }
            }
        });

        // Клик по мини-плееру открывает полный плеер
        miniPlayer.setOnClickListener(v -> {
            // Переключаемся на Dashboard где находится полный плеер
            BottomNavigationView navView = binding.navView;
            navView.setSelectedItemId(R.id.navigation_dashboard);
        });
    }

    private void setupNavigation() {
        BottomNavigationView navView = binding.navView;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard, R.id.navigation_home, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.WAKE_LOCK
        };

        boolean allGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (!allGranted) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (!allGranted) {
                // Показать диалог объяснения необходимости разрешений
                // Можно добавить AlertDialog здесь
            }
        }
    }

    private void startAndBindMusicService() {
        Intent serviceIntent = new Intent(this, MusicPlayerService.class);
        startService(serviceIntent);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isServiceBound) {
            Intent serviceIntent = new Intent(this, MusicPlayerService.class);
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    // Методы интерфейса MusicPlayerListener
    @Override
    public void onSongChanged(String title, String artist, String albumArtPath) {
        runOnUiThread(this::updateMiniPlayer);
    }

    @Override
    public void onPlaybackStateChanged(boolean isPlaying) {
        runOnUiThread(() -> {
            if (isPlaying) {
                miniPlayPause.setImageResource(R.drawable.ic_pause);
                showMiniPlayer();
            } else {
                miniPlayPause.setImageResource(R.drawable.ic_play_arrow);
            }
        });
    }

    @Override
    public void onPlaybackStopped() {
        runOnUiThread(this::hideMiniPlayer);
    }

    private void updateMiniPlayer() {
        if (isServiceBound && musicPlayerService != null) {
            String currentTitle = musicPlayerService.getCurrentSongTitle();
            String currentArtist = musicPlayerService.getCurrentArtist();

            if (currentTitle != null && currentArtist != null) {
                miniSongTitle.setText(currentTitle);
                miniArtistName.setText(currentArtist);

                // Обновляем кнопку play/pause
                if (musicPlayerService.isPlaying()) {
                    miniPlayPause.setImageResource(R.drawable.ic_pause);
                } else {
                    miniPlayPause.setImageResource(R.drawable.ic_play_arrow);
                }

                // TODO: Загрузить обложку альбома если есть
                // Glide.with(this).load(albumArtPath).into(miniAlbumArt);

                showMiniPlayer();
            }
        }
    }

    private void showMiniPlayer() {
        if (miniPlayer.getVisibility() != View.VISIBLE) {
            miniPlayer.setVisibility(View.VISIBLE);
        }
    }

    private void hideMiniPlayer() {
        miniPlayer.setVisibility(View.GONE);
    }

    // Публичные методы для доступа к сервису из фрагментов
    public MusicPlayerService getMusicPlayerService() {
        return isServiceBound ? musicPlayerService : null;
    }

    public boolean isServiceBound() {
        return isServiceBound;
    }
}