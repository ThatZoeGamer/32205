package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

public class Configs {
    @Config
    public static final class toggles {
        public static boolean blueTeam = true;
        public static boolean intakeTelemetry = true;
        public static boolean shooterTelemetry = true;
        public static boolean odomTelemetry = true;
        public static boolean limelightTelemetry = true;
        public static boolean colourSensorTelemetry = true;
    }
    @Config
    public static final class intakeConfigs {
        public static double intakeTriggerThresh = 0;
        public static double outtakeTriggerThresh = 0;
    }
    @Config
    public static final class shooterConfigs {
        public static double LshooterP = 0.01;
        public static double LshooterI = 0;
        public static double LshooterD = 0.0;
        public static double RshooterP = 0.01;
        public static double RshooterI = 0;
        public static double RshooterD = 0;
        public static double LshooterKs = 0.000001;
        public static  double LshooterKv = 0.000019;
        public static double RshooterKs = 0.000005;
        public static  double RshooterKv = 0.000019;
        public static double shooterSetpointTol = 10;
        public static double maxSpeed = 1000;
        public static double manuelLeftVelocity = 1;
        public static double manuelRightVelocity = 1;
        public static double manualShooterVelocity = 0.0;
        public static double baseCurrent = 3.7;
        public static double pollenCurrentThresh = 10;
        public static double nectarCurrentThresh = 10;
        public static double jamTime = 5;

    }


//    public static void minMaxMan(double newPower) {
//        if (newPower > shooterConfigs.maxPower) {
//            shooterConfigs.manualShooterSpeed = shooterConfigs.maxPower;
//        } else if (newPower < 0) {
//            shooterConfigs.manualShooterSpeed = 0;
//        }
//    }
}
