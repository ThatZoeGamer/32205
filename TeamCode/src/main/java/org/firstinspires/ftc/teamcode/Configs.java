package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

public class Configs {
//    @Config
    public static final class Toggles {
        public static boolean blueTeam = true;
    }
    @Config
    public static final class IntakeConfigs {
        public static final double loweredPosition = 0.0;
        public static final double midPosition = 0.0;
        public static final double highPosition = 0.0;
        public static final double openPosition = 0.0;
        public static final double closedPosition = 0.0;
    }
    @Config
    public static final class TurretConfigs {
        public static double turretP = 0.01;
        public static double turretI = 0;
        public static double turretD = 0;
        public static double setpoint = 0;
        public static double positionTolerance = 1.0;

    }

    public static final class ShooterConfigs {
        @Config
        public static final class LeftShooter {
            public static double LshooterP = 0.000165;//000019
            public static double LshooterKs = 0.155;//13
            public static  double LshooterKv = 0.000165;//00016
//            public static double manuelLeftVelocity = 1;
        }
        @Config
        public static final class RightShooter {
            public static double RshooterP = 0.00021;//00024
            public static double RshooterKs = 0.155;
            public static  double RshooterKv = 0.000137;//000128
//            public static double manuelRightVelocity = 1;
        }
        @Config
        public static final class Misc {
            public static double NominalVoltage = 12; //V
            public static double manualShooterVelocity = 1000.0;
            public static double shooterTolerance = 70;
            public static double ballShootingThreshold = 2.7;
        }
        @Config
        public  static  final class Compressor {
            public static double pollenPosition = 0.0;
            public static double nectarPosition = 0.0;
        }

    }
}
