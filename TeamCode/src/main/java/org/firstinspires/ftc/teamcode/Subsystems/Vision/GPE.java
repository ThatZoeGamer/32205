package org.firstinspires.ftc.teamcode.Subsystems.Vision;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.Field;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;

public class GPE {
    MultipleTelemetry telemetry;
    Limelight limelight;
    Odometry pods;
    Pose3D botPose;
    public GPE(MultipleTelemetry m_telemetry, Limelight s_limelight, Odometry odometry) {
        telemetry = m_telemetry;
        limelight = s_limelight;
        pods = odometry;
    }
    public void turnToTag(Field.Hive hive) {}
    public void periodic(MultipleTelemetry telemetry) {
        telemetry.addData("distance to tag", limelight.getTargetRange());
        telemetry.addData("pitch", limelight.getPitch());
        telemetry.addData("roll", limelight.getRoll());
        telemetry.addData("yaw", limelight.getYaw());
//        telemetry.addData("tag estimated Pose", limelight.getBotPose().getOrientation());
        telemetry.addData("tx", limelight.getTx());
        telemetry.addData("ty", limelight.getTy());
    }
}
