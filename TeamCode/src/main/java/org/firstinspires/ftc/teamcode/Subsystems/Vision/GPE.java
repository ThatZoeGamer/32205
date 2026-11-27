package org.firstinspires.ftc.teamcode.Subsystems.Vision;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Field;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;

public class GPE {
    Limelight limelight;
    Odometry pods;
    Pose3D botPose;
    public GPE(Limelight s_limelight, Odometry odometry) {
        limelight = s_limelight;
        pods = odometry;
    }



    public void turnToTag(Field.HivePositions hive) {}
    public void periodic(MultipleTelemetry m_telemetry, Telemetry telemetry) {
        if (!Configs.Toggles.compMode) {
            m_telemetry.addData("GPE :----------GLOBAL POSE ESTIMATION----------", "GPE");
            m_telemetry.addData("GPE: Distance To Tag", limelight.getTargetRange());
            m_telemetry.addData("GPE: Pitch", limelight.getPitch());
            m_telemetry.addData("GPE: Roll", limelight.getRoll());
            m_telemetry.addData("GPE: Yaw", limelight.getYaw());
            m_telemetry.addData("GPE: Tx", limelight.getTx());
            m_telemetry.addData("GPE: Ty", limelight.getTy());
        } else {
            telemetry.addData("GPE :----------GLOBAL POSE ESTIMATION----------", "GPE");
            telemetry.addData("GPE: Distance To Tag", limelight.getTargetRange());
            telemetry.addData("GPE: Pitch", limelight.getPitch());
            telemetry.addData("GPE: Roll", limelight.getRoll());
            telemetry.addData("GPE: Yaw", limelight.getYaw());
            telemetry.addData("GPE: Tx", limelight.getTx());
            telemetry.addData("GPE: Ty", limelight.getTy());
        }
    }
}
