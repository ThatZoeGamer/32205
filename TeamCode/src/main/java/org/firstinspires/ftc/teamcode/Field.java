package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Field {
    public enum Hives {
        eastHive,
        westHive
    }

    public static class HivePositions {
        public static final Pose2D westHive = new Pose2D(DistanceUnit.INCH, 72.0, 60, AngleUnit.DEGREES, 0);
        public static final Pose2D eastHive = new Pose2D(DistanceUnit.INCH, 96.0, 60, AngleUnit.DEGREES, 0);
//        public static final Position blueHiveSouth = new Position();
//        public static final Position blueHiveNorth = new Position();
    }
    public static class FlowerPositions {
        public static final Pose2D northFlower = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
        public static final Pose2D eastFlower = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
        public static final Pose2D southFlower = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
        public static final Pose2D westFlower = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
    }
    public static class StartingPositions {
        public static final Pose2D pose1 = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
        public static final Pose2D pose2 = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
        public static final Pose2D pose3 = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
        public static final Pose2D pose4 = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
    }
    public static class AutoPaths {
        public static final Pose2D startPose = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
        public static final Pose2D point1 = new Pose2D(DistanceUnit.INCH, -32, 0, AngleUnit.DEGREES, 0);
        public static final Pose2D point2 = new Pose2D(DistanceUnit.INCH, -35, 90, AngleUnit.DEGREES, -90);
    }
}
