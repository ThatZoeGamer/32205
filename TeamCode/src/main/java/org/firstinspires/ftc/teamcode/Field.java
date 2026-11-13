package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class Field {
    public static class Hive {
        public static final Pose3D redHiveSouth = new Pose3D(new Position(), null);
        public static final Pose3D redHiveNorth = new Pose3D(new Position(), null);
        public static final Pose3D blueHiveSouth = new Pose3D(new Position(), null);
        public static final Pose3D blueHiveNorth = new Pose3D(new Position(), null);
    }
    public static class Flower {
        public static final Pose2D northFlower = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
        public static final Pose2D eastFlower = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
        public static final Pose2D southFlower = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
        public static final Pose2D westFlower = new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0);
    }

}
