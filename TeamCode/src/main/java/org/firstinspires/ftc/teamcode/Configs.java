package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

public class Configs {
//    @Config
    public static final class Toggles {
        public static boolean blueTeam = true;
        public static boolean compMode = false;
    }
    @Config
    public static final class DriveConfigs {
        public static double driveP = 0.08;
        public static double driveI = 0;
        public static double driveD = 0;
        public static double strafeP = 0.2;
        public static double strafeI = 0;
        public static double strafeD = 0;
        public static double turnP = 0.001;
        public static double turnI = 0;
        public static double turnD = 0;
    }
    @Config
    public static final class IntakeConfigs {
        public static double loweredPosition = 0;
        public static double midPosition = 0.6;
        public static double highPosition = 1;
        public static double openPosition = 0.3;
        public static double closedPosition = 0.1;
    }
//    @Config
//    public static final class TurretConfigs {
//        public static double turretP = 0.01;
//        public static double turretI = 0;
//        public static double turretD = 0;
//        public static double setpoint = 0;
//        public static double positionTolerance = 1.0;
//
//    }

    public static final class ShooterConfigs {
        @Config
        public static final class LeftShooter {
//            public static double lowLshooterP = 0.001;//0.000175
//            public static double lowLshooterI = 0.0;//0.000175
//            public static double lowLshooterD = 0.00;//0.000175
//            public static double lowLshooterKs = 0.000;//13
//            public static  double lowLshooterKv = 0.00069;//0.00076
            public static double lShooterP = 0.0001;//0.000175
            public static double lShooterI = 0.0;//0.000175
            public static double lShooterD = 0.00;//0.000175
            public static double lShooterKs = 0.000;//
            public static  double lShooterKv = 0.00074;//0.00076
//            public static double manuelLeftVelocity = 1;
        }
        @Config
        public static final class RightShooter {
//            public static double lowRshooterP = 0.0009;//0.00096
//            public static double lowRshooterI = 0.0006;//0.00107
//            public static double lowRshooterD = 0;//0.00032
//            public static double lowRshooterKs = 0.000;
//            public static  double lowRshooterKv = 0.00085;//0.001

            public static double RshooterP = 0.00095;//0.00096
            public static double RshooterI = 0.0006;//0.00107
            public static double RshooterD = 0;//0.00032
            public static double RshooterKs = 0.000;
            public static  double RshooterKv = 0.00102;//0.001
//            public static double manuelRightVelocity = 1;
        }
        @Config
        public static final class Misc {
            public static double manualShooterVelocity = 730;
            public static double shooterTolerance = 20;
            public static double ballShootingThreshold = 2.7;
            public static double kSync = 0.0013;
        }
        @Config
        public  static  final class Compressor {
            public static double pollenPosition = 0.0;
            public static double nectarPosition = 0.0;
        }

    }
}
