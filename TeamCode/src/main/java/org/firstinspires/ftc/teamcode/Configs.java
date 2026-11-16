package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Utility;

public class Configs {
//    @Config
    public static final class Toggles {
        public static boolean blueTeam = true;
    }
//    @Config
    public static final class IntakeConfigs {
        public static double intakeTriggerThresh = 0;
        public static double outtakeTriggerThresh = 0;
    }
    @Config
    public static final class TurretConfigs {
        public static double turretP = 0.01;
        public static double turretI = 0;
        public static double turretD = 0;
        public static double setpoint = 10;
        public static double positionTolerance = 1.0;

    }

    public static final class ShooterConfigs {
        @Config
        public static final class LeftShooter {
            public static double LshooterP = 0.0001717;//000019
            public static double LshooterKs = 0.13;//13
            public static  double LshooterKv = 0.00016;//00016
//            public static double manuelLeftVelocity = 1;
        }
        @Config
        public static final class RightShooter {
            public static double RshooterP = 0.0002169;//00024
            public static double RshooterKs = 0.14;
            public static  double RshooterKv = 0.000129;//000128
//            public static double manuelRightVelocity = 1;
        }
        @Config
        public static final class Misc {
            public static double NominalVoltage = 12.3; //V
            public static double manualShooterVelocity = 1000.0;
            public static double shooterTolerance = 70;
            public static double ballShootingThreshold = 2.7;
        }
    }
}
