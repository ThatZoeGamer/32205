package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Utilities.PIDController;
import com.qualcomm.robotcore.hardware.Servo;
import com.acmerobotics.dashboard.config.Config;
public class Constants {
    public static final class DrivetrainConstants {
        public static final String frontLeftMotor = "frontLeft0";
        public static final String frontRightMotor = "frontRight1";
        public static final String backLeftMotor = "backLeft2";
        public static final String backRightMotor = "backRight3";

        public static final double CountsPerMotorRev = 537.7;   // eg: GoBILDA 312 RPM Yellow Jacket
        public static final double DriveGearReduction = 1.0;     // No External Gearing.
        public static final double WheelDiameterInches = 4.0;     // For figuring circumference
        public static final double CountsPerInch = (CountsPerMotorRev * DriveGearReduction /
                (WheelDiameterInches * 3.1415));

        public static final double strafingBalancer = 1.0;

        public static final double controlHubOffset =  90;
    }

    public static class intakeConstants {
        public static String intakeMotorId = "intake";
    }

    public static class shooterConstants {
        public static final String shooterMotorLid = "sMotorL"; //1
        public static final String shooterMotorRid = "sMotorR"; //0
        public static final String axisServoId = "axisServo";
        public static final double ticksPerRev = 24;
    }

}