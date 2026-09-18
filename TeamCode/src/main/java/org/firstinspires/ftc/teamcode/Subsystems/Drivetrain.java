package org.firstinspires.ftc.teamcode.Subsystems;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Constants;

public class Drivetrain extends SubsystemBase {
//    private final DcMotor frontLeft;
//    private final DcMotor frontRight;
//    private final DcMotor backLeft;
    private final DcMotor backRight;

    private final BHI260IMU IMU;

    private double yawOffset;

    public Drivetrain(HardwareMap hardwaremap) {
//        frontLeft = hardwaremap.get(DcMotor.class, Constants.DrivetrainConstants.frontLeftMotor);
//        frontRight = hardwaremap.get(DcMotor.class, Constants.DrivetrainConstants.frontRightMotor);
//        backLeft = hardwaremap.get(DcMotor.class, Constants.DrivetrainConstants.backLeftMotor);
        backRight = hardwaremap.get(DcMotor.class, Constants.DrivetrainConstants.backRightMotor);
//
//        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
//        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
//        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

//        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        IMU = hardwaremap.get(BHI260IMU.class, "imu");
        IMU.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)
        )
        );
        //what

        yawOffset = IMU.getRobotYawPitchRollAngles().getYaw() - Constants.DrivetrainConstants.controlHubOffset;
    }

    public void runMoter () {
        backRight.setPower(1);
    }

    public void drive(double driveX, double driveY, double rotation) {

        double botHeading = getHeading();
        double headingRadians = Math.toRadians(botHeading);

        double sin =  Math.sin(-headingRadians);
        double cos =  Math.cos(-headingRadians);

        double fieldOrientedX = driveX * cos - driveY * sin;
        double fieldOrientedY = driveX * sin + driveY * cos;

        fieldOrientedX *= Constants.DrivetrainConstants.strafingBalancer;

        double denominator = Math.max(Math.abs(fieldOrientedY) + Math.abs(fieldOrientedX) + Math.abs(rotation), 1);

        double frontLeftPower = (fieldOrientedY + fieldOrientedX + rotation) / denominator;
        double frontRightPower = (fieldOrientedY - fieldOrientedX - rotation) / denominator;
        double backLeftPower = (fieldOrientedY - fieldOrientedX + rotation) / denominator;
        double backRightPower = (fieldOrientedY + fieldOrientedX - rotation) / denominator;

//        frontLeft.setPower(frontLeftPower);
//        frontRight.setPower(frontRightPower);
//        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }

    public double getRawHeading() {
        Orientation angles = IMU.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.firstAngle;
    }
    public double getHeading() {
        double heading = getRawHeading() - yawOffset;

        if(heading > 180) {
            heading -= 360;
        } else if (heading < -180) {
            heading += 360;
        }

        return heading;
    }

    public void resetYaw() {
        yawOffset = getRawHeading() - Constants.DrivetrainConstants.controlHubOffset;
    }

    public void periodic(Telemetry telemetry) {
        telemetry.addLine("Drive train");
        telemetry.addData("Heading: ", getHeading());
//
//        telemetry.addData("Front Left Power: ", frontLeft.getPower());
//
//        telemetry.addData("Front Right Power: ", frontRight.getPower());
//
//        telemetry.addData("Back Left Power: ", backLeft.getPower());

        telemetry.addData("Back Right Power: ", backRight.getPower());
    }


}