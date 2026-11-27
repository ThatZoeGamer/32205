package org.firstinspires.ftc.teamcode.Commands;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Field;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.Limelight;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;

public class AimToHive {
    private final Odometry odometry;
    private final Drivetrain drivetrain;
    private final PIDController turnController;
    Limelight limelight;

    public AimToHive(Odometry odometry, Drivetrain drivetrain, Limelight limelight) {
        this.odometry = odometry;
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        this.turnController = new PIDController(Configs.DriveConfigs.turnP, 0, 0);

    }
    public void aim(Field.Hives hive) {
        if (limelight.foundTarget()) {
            double hError = limelight.getTx();
            double turnPower = turnController.calculate(hError, 0);
            drivetrain.drive(0,0, turnPower);
        } else {
            Pose2D currentPose = odometry.getPose();
            Pose2D hivePose = (hive == Field.Hives.eastHive)
                    ? Field.HivePositions.eastHive
                    : Field.HivePositions.westHive;

            double xError = hivePose.getX(DistanceUnit.INCH) - currentPose.getX(DistanceUnit.INCH);
            double yError = hivePose.getY(DistanceUnit.INCH) - currentPose.getY(DistanceUnit.INCH);

            double targetHeading = Math.toDegrees(Math.atan2(yError, xError));
            double currentHeading = currentPose.getHeading(AngleUnit.DEGREES);

            double headingError = normalizeAngleDegrees(targetHeading - currentHeading);
            double turnPower = turnController.calculate(0, headingError);

            drivetrain.drive(0, 0, turnPower);
        }
    }


    private double normalizeAngleDegrees(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }
}
