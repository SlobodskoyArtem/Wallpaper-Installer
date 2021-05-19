package com.example.wallpaperinstaller;

import android.graphics.Canvas;
import android.graphics.Movie;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.logging.Handler;

public class GIFWallpaperServise extends WallpaperService {

    @Override
    public WallpaperService.Engine onCreateEngine() {
        try {
            Movie movie = Movie.decodeStream(getResources().getAssets().open("orig.gif"));
            return  new GIFWallpaperEngine(movie);
        } catch (IOException e) {
            Log.d("GIF", "Could not load assets");
            return null;
        }
    }

    private class GIFWallpaperEngine extends  WallpaperService.Engine {
        private  final int frameDuration = 20;

        private SurfaceHolder holder;
        private  Movie movie;
        private boolean visible;
        private Handler handler;

        public GIFWallpaperEngine(Movie movie) {
            this.movie = movie;
            handler = new Handler();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.holder = surfaceHolder;
        }

        private Runnable drqwGIF = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        private void draw(){
            if(visible){
                Canvas canvas = holder.lockCanvas();
                //отрегулировка размер и положение вашего фонового изображения
                canvas.scale(4f, 4f);
                movie.draw(canvas,-100, 0);
                canvas.restore();
                holder.unlockCanvasAndPost(canvas);
                movie.setTime((int) (System.currentTimeMillis() % movie.duration()));

                handler.removeCallbacks(drqwGIF);
                handler.postDelayes(drqwGIF, frameDuration);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible){
            this.visible = visible;
            if (visible){
                handler.post(drqwGIF);
            }
            else {
                handler.removeCallbacks(drqwGIF);
            }
        }

        @Override
        public void onDestroy(){
            super.onDestroy();
            handler.removeCallbacks(drqwGIF);
        }
    }
}
