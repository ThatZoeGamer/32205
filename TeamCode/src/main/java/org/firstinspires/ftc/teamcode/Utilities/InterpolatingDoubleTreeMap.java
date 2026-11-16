package org.firstinspires.ftc.teamcode.Utilities;

import java.util.Map;
import java.util.TreeMap;

public class InterpolatingDoubleTreeMap extends TreeMap<Double, Double> {

    public Double getInterpolated(double key) {
        if (isEmpty()) return null;

        // Return exact match if present
        Double exact = get(key);
        if (exact != null) return exact;

        Map.Entry<Double, Double> lower = floorEntry(key);
        Map.Entry<Double, Double> upper = ceilingEntry(key);

        // Clamp to boundary values if out of range
        if (lower == null) return upper.getValue();
        if (upper == null) return lower.getValue();

        // Linear interpolation: y = y1 + (x - x1) * (y2 - y1) / (x2 - x1)
        double t = (key - lower.getKey()) / (upper.getKey() - lower.getKey());
        return lower.getValue() + t * (upper.getValue() - lower.getValue());
    }
}
