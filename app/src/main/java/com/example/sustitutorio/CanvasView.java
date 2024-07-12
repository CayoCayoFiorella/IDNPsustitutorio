package com.example.sustitutorio;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class CanvasView extends View {

    private static final int SCALE_FACTOR = 100;

    private Paint polygonPaint;
    private Paint circlePaint;
    private Paint textPaint;
    private Path path;
    private List<Room> rooms = new ArrayList<>();
    private List<Picture> pictures = new ArrayList<>();

    public CanvasView(Context context) {
        super(context);
        init();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        polygonPaint = createPaint(Color.BLUE, Paint.Style.STROKE, 5);
        circlePaint = createPaint(Color.CYAN, Paint.Style.FILL, 0);
        textPaint = createPaint(Color.BLACK, Paint.Style.FILL, 60);
        textPaint.setTextSize(35);
        path = new Path();

        // Leer datos de los archivos CSV
        rooms.addAll(readRoomsFromCSV(getContext(), "rooms.csv"));
        pictures.addAll(readPicturesFromCSV(getContext(), "pictures.csv"));
    }

    private Paint createPaint(int color, Paint.Style style, float strokeWidth) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(style);
        paint.setStrokeWidth(strokeWidth);
        return paint;
    }

    private List<Room> readRoomsFromCSV(Context context, String fileName) {
        List<Room> rooms = new ArrayList<>();
        List<String[]> roomData = CSVReader.readCSV(context, fileName);
        for (String[] room : roomData) {
            if (room.length < 9) {
                // Verificar que haya al menos 9 elementos (nombre + 8 coordenadas)
                continue;
            }
            String name = room[0];
            float[][] coordinates = new float[(room.length - 1) / 2][2];
            for (int i = 1, j = 0; i < room.length; i += 2, j++) {
                coordinates[j][0] = Float.parseFloat(room[i]);
                coordinates[j][1] = Float.parseFloat(room[i + 1]);
            }
            rooms.add(new Room(name, coordinates));
        }
        return rooms;
    }

    private List<Picture> readPicturesFromCSV(Context context, String fileName) {
        List<Picture> pictures = new ArrayList<>();
        List<String[]> pictureData = CSVReader.readCSV(context, fileName);
        for (String[] picture : pictureData) {
            if (picture.length < 3) {
                // Verificar que haya al menos 3 elementos (nombre + 2 coordenadas)
                continue;
            }
            String label = picture[0];
            float[] coordinates = new float[2];
            coordinates[0] = Float.parseFloat(picture[1]);
            coordinates[1] = Float.parseFloat(picture[2]);
            pictures.add(new Picture(coordinates, label));
        }
        return pictures;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Dibujar polígonos
        for (Room room : rooms) {
            path.reset();
            float[][] coordinates = room.getCoordinates();
            path.moveTo(coordinates[0][0] * SCALE_FACTOR, coordinates[0][1] * SCALE_FACTOR);
            for (int j = 1; j < coordinates.length; j++) {
                path.lineTo(coordinates[j][0] * SCALE_FACTOR, coordinates[j][1] * SCALE_FACTOR);
            }
            path.close();
            canvas.drawPath(path, polygonPaint);
            // Dibujar nombre del salón
            canvas.drawText(room.getName(), coordinates[0][0] * SCALE_FACTOR + 20,
                    coordinates[0][1] * SCALE_FACTOR + 120, textPaint);
        }

        // Dibujar pinturas
        for (Picture picture : pictures) {
            float[] coordinates = picture.getCoordinates();
            canvas.drawCircle(coordinates[0] * SCALE_FACTOR, coordinates[1] * SCALE_FACTOR, 30, circlePaint);
            canvas.drawText(picture.getLabel(), coordinates[0] * SCALE_FACTOR - 15,
                    coordinates[1] * SCALE_FACTOR + 15, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX() / SCALE_FACTOR;
            float y = event.getY() / SCALE_FACTOR;
            for (Room room : rooms) {
                if (room.containsPoint(x, y)) {
                    openRoomDescription(room.getName());
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void openRoomDescription(String roomName) {
        FragmentActivity activity = (FragmentActivity) getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment descriptionFragment = FragmentDescripcion.newInstance(roomName);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, descriptionFragment)
                .addToBackStack(null)
                .commit();
    }
}

