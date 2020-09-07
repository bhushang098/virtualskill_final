package com.twilio.video.app.CanvasAll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.twilio.video.RemoteParticipant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kotlin.collections.IntIterator;

/**
 * Collaborative drawing view inspired by the smooth signatures blog from Square.
 * <p>
 * https://medium.com/square-corner-blog/smoother-signatures-be64515adb33
 * <p>
 * This view will draw the path of the local participant as well as all other participants.
 */
public class CollaborativeDrawingView extends View {
    float STROKE_WIDTH = 5f;
    final String TAG = "DrawingView";
    //float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    int color;
    private List<Stroke> _allStrokes; //all strokes that need to be drawn
    private SparseArray<Stroke> _activeStrokes; //use to retrieve the currently drawn strokes


    /*
     * Attributes used to draw the local participant's path.
     */
    private Paint paint = new Paint();
    private Path path = new Path();
    private float lastTouchX;
    private float lastTouchY;
    private final RectF dirtyRect = new RectF();


    /*
     * Listener of local drawing events.
     */
    private Listener listener;

    /*
     * Maintains the path and paint object for each remote participant.
     */
    private final Map<RemoteParticipant, List<Pair<Path, Paint>>> remoteParticipantPalettes =
            new HashMap<>();

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getSTROKE_WIDTH() {
        return STROKE_WIDTH;
    }

    public void setSTROKE_WIDTH(float STROKE_WIDTH) {
        this.STROKE_WIDTH = STROKE_WIDTH;
    }

    public CollaborativeDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        _allStrokes = new ArrayList<Stroke>();
        _activeStrokes = new SparseArray<Stroke>();

    }



    /**
     * Listen for local drawing events.
     */
    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    /**
     * Update the drawn path of a remote participant. This method can be called from any thread.
     *
     * @param remoteParticipant participant who sent motion event.
     * @param actionEvent       the {@link MotionEvent} issued by the participant.
     * @param eventX            the x coordinate of the motion event.
     * @param eventY            the y coordinate of the motion event.
     */
    public void onRemoteTouchEvent(RemoteParticipant remoteParticipant,
                                   int actionEvent,
                                   float eventX,
                                   float eventY,int color,float STROKE_WIDTH_REMOTE) {

        Pair<Path, Paint> remoteParticipantPalette;
        if (remoteParticipantPalettes.containsKey(remoteParticipant))
        {
            if(remoteParticipantPalettes.get(remoteParticipant).get(remoteParticipantPalettes.get(remoteParticipant).size()-1).second.getColor()==color
                    && remoteParticipantPalettes.get(remoteParticipant).get(remoteParticipantPalettes.get(remoteParticipant).size()-1).second.getStrokeWidth()==STROKE_WIDTH_REMOTE ){
                remoteParticipantPalette = remoteParticipantPalettes.get(remoteParticipant).get(remoteParticipantPalettes.get(remoteParticipant).size()-1);
            }else {
                remoteParticipantPalette = addnewPathAndPaint(remoteParticipant,color,STROKE_WIDTH_REMOTE);
            }
        }
        else{
            //remoteParticipantPalette = insertAndGetRemoteParticipantPalette(remoteParticipant, color);
            remoteParticipantPalette = addnewRemoteParticipant(remoteParticipant,color,STROKE_WIDTH_REMOTE);

        }


        // Process action event
        switch (actionEvent) {
            case MotionEvent.ACTION_DOWN:
                remoteParticipantPalette.first.moveTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                remoteParticipantPalette.first.lineTo(eventX, eventY);
                break;
            default:
                Log.d(TAG, "Ignored remote touch event: " + actionEvent);
        }


        post(this::invalidate);
    }

    private Pair<Path, Paint> addnewRemoteParticipant(RemoteParticipant remoteParticipant, int color,float STROKE_WIDTH_REMOTE) {

        Path path = new Path();
        Paint paint = getRemoteParticipantPaint(remoteParticipant,color,STROKE_WIDTH_REMOTE);
        Pair<Path, Paint> remoteParticipantPalette = new Pair<>(path, paint);
        List<Pair<Path,Paint>> fistList = new ArrayList<Pair<Path,Paint>>();
        fistList.add(remoteParticipantPalette);

        remoteParticipantPalettes.put(remoteParticipant, fistList);

        return remoteParticipantPalette;
    }

    private Pair<Path, Paint> addnewPathAndPaint(RemoteParticipant remoteParticipant, int color,float STROKE_WIDTH_REMOTE) {

        Path path = new Path();
        Paint paint = getRemoteParticipantPaint(remoteParticipant,color,STROKE_WIDTH_REMOTE);
        Pair<Path, Paint> remoteParticipantPalette = new Pair<>(path, paint);

        remoteParticipantPalettes.get(remoteParticipant).add(remoteParticipantPalette);

            return remoteParticipantPalette;
    }

    /**
     * Clear the local drawing.
     */
    public void clear() {
        // Clear the local path
        path.reset();

        // Invalidate the view
        post(this::invalidate);
    }

    /**
     * Remove drawing of the remote participant. This method can be called from any thread.
     *
     * @param remoteParticipant participant to clear.
     */
    public void clear(RemoteParticipant remoteParticipant) {
        // Remove the remote participant palette
        remoteParticipantPalettes.remove(remoteParticipant);

        // Invalidate the view
        post(this::invalidate);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Only draw when enabled
        if (!isEnabled()) {
            return;
        }
        if (_allStrokes != null) {
            for (Stroke stroke: _allStrokes) {
                if (stroke != null) {
                    Path path = stroke.getPath();
                    Paint painter = stroke.getPaint();
                    if ((path != null) && (painter != null)) {
                        canvas.drawPath(path, painter);
                    }
                }
            }
        }
        // Draw local path
        //canvas.drawPath(path, paint);

        // Draw remote paths
        for (List<Pair<Path, Paint>> remoteParticipantList : remoteParticipantPalettes.values()) {

            for (Pair<Path,Paint> remoteParticipantPalette : remoteParticipantList
                 ) {
                canvas.drawPath(remoteParticipantPalette.first, remoteParticipantPalette.second);
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        final int pointerCount = event.getPointerCount();
        // Only handle touch events when enabled
        if (!isEnabled()) {
            return false;
        }

        float eventX = event.getX();
        float eventY = event.getY();
        int actionEvent = event.getAction();

        // Notify listener of action event
        if (listener != null) {
            listener.onTouchEvent(actionEvent, eventX, eventY);
        }

        // Process action event
//        switch (actionEvent) {
//            case MotionEvent.ACTION_DOWN:
//
//                //path.moveTo(eventX, eventY);
//                lastTouchX = eventX;
//                lastTouchY = eventY;
//                path.moveTo(eventX,eventY);
//                // There is no end point yet, so don't waste cycles invalidating.
//                return true;
//            case MotionEvent.ACTION_MOVE:
//            case MotionEvent.ACTION_UP:
//                // Start tracking the dirty region.
//                resetDirtyRect(eventX, eventY);
//
//                /*
//                 * When the hardware tracks events faster than they are delivered, the event will
//                 * contain a history of those skipped points.
//                 */
//                int historySize = event.getHistorySize();
//                for (int i = 0; i < historySize; i++) {
//                    float historicalX = event.getHistoricalX(i);
//                    float historicalY = event.getHistoricalY(i);
//                    expandDirtyRect(historicalX, historicalY);
//                    path.lineTo(historicalX, historicalY);
//                }
//                // After replaying history, connect the line to the touch point.
//                path.lineTo(eventX, eventY);
//                break;
//            default:
//                Log.d(TAG, "Ignored touch event: " + event.toString());
//                return false;
//        }
//
//        // Include half the stroke width to avoid clipping.
//        invalidate(
//                (int) (dirtyRect.left - HALF_STROKE_WIDTH),
//                (int) (dirtyRect.top - HALF_STROKE_WIDTH),
//                (int) (dirtyRect.right + HALF_STROKE_WIDTH),
//                (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));
//        lastTouchX = eventX;
//        lastTouchY = eventY;
//        return true;

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                pointDown((int)event.getX(), (int)event.getY(), event.getPointerId(0));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                for (int pc = 0; pc < pointerCount; pc++) {
                    pointMove((int) event.getX(pc), (int) event.getY(pc), event.getPointerId(pc));
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                for (int pc = 0; pc < pointerCount; pc++) {
                    pointDown((int)event.getX(pc), (int)event.getY(pc), event.getPointerId(pc));
                }
                break;
            }
        }
        invalidate();
        return true;
    }

    /*
     * Called when replaying history to ensure the dirty region includes all
     * points.
     */
    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }
        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    /*
     * Resets the dirty region when the motion event occurs.
     */
    private void resetDirtyRect(float eventX, float eventY) {
        // The lastTouchX and lastTouchY were set when the ACTION_DOWN
        // motion event occurred.
        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
        dirtyRect.bottom = Math.max(lastTouchY, eventY);
    }

    /*
     * Assigns a new path and palette to a remote participant.
     */
    private Pair<Path, Paint> insertAndGetRemoteParticipantPalette(RemoteParticipant remoteParticipant,int color) {
        Path path = new Path();
       // Paint paint = getRemoteParticipantPaint(remoteParticipant,color);
        Pair<Path, Paint> remoteParticipantPalette = new Pair<>(path, paint);

        //remoteParticipantPalettes.put(remoteParticipant, remoteParticipantPalette);

        return remoteParticipantPalette;
    }

    /*
     * Assigns a paint instance to a remote participant.
     */
    private Paint getRemoteParticipantPaint(RemoteParticipant remoteParticipant,int color, float STROKE_WIDTH) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);
        return paint;
    }



    private void pointDown(int x, int y, int id) {
        //create a paint with random color
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(color);

        //create the Stroke
        Point pt = new Point(x, y);
        Stroke stroke = new Stroke(paint);
        stroke.addPoint(pt);
        _activeStrokes.put(id, stroke);
        _allStrokes.add(stroke);
    }

    private void pointMove(int x, int y, int id) {
        //retrieve the stroke and add new point to its path
        Stroke stroke = _activeStrokes.get(id);
        if (stroke != null) {
            Point pt = new Point(x, y);
            stroke.addPoint(pt);
        }
    }

    /**
     * Listener for drawing events.
     */
    public interface Listener {
        /**
         * Notifies of a local touch event from the view on the UI thread.
         *
         * @param actionEvent the {@link MotionEvent} action.
         * @param x           the event x coordinate.
         * @param y           the event y coordinate.
         */
        void onTouchEvent(int actionEvent, float x, float y);
    }
}
