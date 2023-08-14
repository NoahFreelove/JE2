package org.JE.JE2.Utility.Timeline;

import org.JE.JE2.Utility.Timeline.InterpolateFunctions.InterpolateFunc;

import java.util.Comparator;

public class Track<T> {
    private TrackPoint<T>[] points = new TrackPoint[0];
    private int currentPoint = 0;
    private int nextPoint = 0;
    private InterpolateFunc<T> func;
    private T recentValue;
    private float latestTime = 0;

    @SafeVarargs
    public Track(InterpolateFunc<T> func, TrackPoint<T>... points) {
        if(points.length < 1)
            throw new IllegalArgumentException("Track must have at least one point");

        this.points = points;
        this.func = func;
        this.recentValue = points[0].value();
        sortByTime();
    }

    private void sortByTime() {
        Comparator<TrackPoint<T>> comparer = (o1, o2) -> Float.compare(o1.time(), o2.time());
        java.util.Arrays.sort(points, comparer);

    }

    public void update(float time){
        currentPoint = getCurrentPoint(time);
        nextPoint = currentPoint+1;
        if(nextPoint >= points.length)
            nextPoint--;
        latestTime = time;
        float percent = (time - points[currentPoint].time())/(points[nextPoint].time() - points[currentPoint].time());
        if(Float.isNaN(percent))
            percent = 1;
        recentValue = func.interpolate(points[currentPoint].value(), points[nextPoint].value(), percent);

    }

    private int getCurrentPoint(float time) {
        int i = 0;
        for (int j = 0; j < points.length; j++) {
            if(points[j].time() <= time)
                i = j;
        }
        return i;
    }

    public T getRecentValue() {
        return recentValue;
    }

    @SafeVarargs
    public final void setPoints(TrackPoint<T>... points) {
        this.points = points;
        sortByTime();
        update(latestTime);
    }
}
