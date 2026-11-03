package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

public class Configs {
    @Config
    public static final class intakeConfigs {
        public static double intakeTriggerThresh = 0;
        public static double outtakeTriggerThresh = 0;
    }
    @Config
    public static final class shooterConfigs {
        public static double shooterP = 0.001;
        public static double shooterI = 0;
        public static double shooterD = 0;
        public static double shooterKs = 0.000;
        public static  double shooterKv = 0.000;
        public static double shooterSetpointTol = 10;
        public static double maxSpeed = 1;
        public static double manualShooterVelocity = 0.25;
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
