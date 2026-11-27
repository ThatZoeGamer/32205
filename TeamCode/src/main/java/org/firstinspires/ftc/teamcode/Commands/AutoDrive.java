package org.firstinspires.ftc.teamcode.Commands;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;
import org.firstinspires.ftc.teamcode.Utilities.MathUtil;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;

public class AutoDrive {
    private final PIDController xController;
    private final PIDController yController;
    private final PIDController hController;
    private final Drivetrain drivetrain;
    private final Odometry odometry;
    private final ElapsedTime timer = new ElapsedTime();
    double xError;
    double yError;
    double hError;
    double xPower;
    double yPower;
    double hPower;
    private final double xTolerance = 2.0;
    private final double yTolerance = 2.0;
    private final double hTolerance = 10.0;
    private final double timeoutSeconds = 10.0;
    private final ElapsedTime settleTimer = new ElapsedTime();
    private final double settleTimeSeconds = 0.25;
    private Pose2D target;

    private boolean isFinished = false;

    public AutoDrive(Odometry odometry, Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        this.odometry = odometry;

        xController = new PIDController(Configs.DriveConfigs.driveP, Configs.DriveConfigs.driveI, Configs.DriveConfigs.driveD);
        yController = new PIDController(Configs.DriveConfigs.strafeP, Configs.DriveConfigs.strafeI, Configs.DriveConfigs.strafeD);
        hController = new PIDController(Configs.DriveConfigs.turnP, Configs.DriveConfigs.turnI, Configs.DriveConfigs.turnD);
    }

    public void init() {
        isFinished = false;
        timer.reset();
        xPower = 0;
        yPower = 0;
        hPower = 0;
    }

    public void drive(Pose2D targetPose, MultipleTelemetry m_telemetry, Telemetry telemetry, double maxLinearSpeed, double maxRotSpeed) {
        Pose2D currentPose = odometry.getPose();
        target = targetPose;

        xError = targetPose.getX(DistanceUnit.INCH) - currentPose.getX(DistanceUnit.INCH);
        yError = targetPose.getY(DistanceUnit.INCH) - currentPose.getY(DistanceUnit.INCH);
        hError = AngleUnit.normalizeDegrees(
                targetPose.getHeading(AngleUnit.DEGREES) - currentPose.getHeading(AngleUnit.DEGREES)
        );

        xPower = isFinished? 0: MathUtil.clamp(
                xController.calculate(currentPose.getX(DistanceUnit.INCH), targetPose.getX(DistanceUnit.INCH)),
                -maxLinearSpeed, maxLinearSpeed
        );
        yPower = isFinished? 0: MathUtil.clamp(
                yController.calculate(currentPose.getY(DistanceUnit.INCH), targetPose.getY(DistanceUnit.INCH)),
                -maxLinearSpeed, maxLinearSpeed
        );
        hPower = isFinished? 0: MathUtil.clamp(
                hController.calculate(0, hError),
                -maxRotSpeed, maxRotSpeed
        );

        drivetrain.drive(yPower, -xPower, hPower);

        periodic(m_telemetry, telemetry);

        boolean withinTolerance = Math.abs(xError) < xTolerance && Math.abs(yError) < yTolerance && Math.abs(hError) < hTolerance;
//        boolean withinTolerance = Math.abs(xError) < xTolerance;

        if (!withinTolerance) {
            settleTimer.reset();
        }

        if (settleTimer.seconds() > settleTimeSeconds || timer.seconds() > timeoutSeconds) {
            drivetrain.stop();
            isFinished = true;
        }
    }

    public void periodic(MultipleTelemetry m_telemetry, Telemetry telemetry) {
        if (!Configs.Toggles.compMode) {
            m_telemetry.addData("xOutput", xPower);
            m_telemetry.addData("yOutput", yPower);
            m_telemetry.addData("rOutput", hPower);
            m_telemetry.addData("xError", xError);
            m_telemetry.addData("yError", yError);
            m_telemetry.addData("hError", hError);
            m_telemetry.addData("targetPose x", target.getX(DistanceUnit.INCH));
            m_telemetry.addData("is finished", isFinished);
        } else {
            telemetry.addData("xOutput", xPower);
            telemetry.addData("yOutput", yPower);
            telemetry.addData("rOutput", hPower);
            telemetry.addData("xError", xError);
            telemetry.addData("yError", yError);
            telemetry.addData("hError", hError);
        }
    }


    public boolean isFinished() {
        return isFinished;
    }
}
