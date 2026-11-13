package org.firstinspires.ftc.teamcode.Subsystems.Vision;

import static org.firstinspires.ftc.teamcode.Configs.toggles.blueTeam;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
//import org.firstinspires.ftc.teamcode.Utilities.LimitedQueue;

import java.util.List;



public class Limelight {
    private int[] desiredTagID;
    public LLResultTypes.FiducialResult desiredTag;
    private final Limelight3A limelight;
    public boolean targetFound;
    public static double tY;
    public static double tX;
    public static double roll;
    public static double yaw;
    public static double pitch;
    public static Pose3D botPose;
    public Limelight(HardwareMap hardwareMap, MultipleTelemetry telemetry) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        if(blueTeam) {
            limelight.pipelineSwitch(0);
        } else {
            limelight.pipelineSwitch(1);
        }
        limelight.start();
    }
    public void refreshDesiredID() {
        int shootID = blueTeam ? 0 : 1;
        desiredTagID = new int[] {shootID};
    }

    public boolean checkDesiredTagID(int tagID) {
        for (int i : desiredTagID) {
            if (tagID == i) {
                return true;
            }
        }
        return false;
    }
    public void getAprilTagData(MultipleTelemetry m_telemetry) {
        targetFound = false;
//        tX = 0;
//        tY = 0;
//        roll = 0;
//        pitch = 0;
//        yaw = 0;
        LLResult result = limelight.getLatestResult();
        if (result.isValid()) {
            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
            for (LLResultTypes.FiducialResult fr : fiducialResults) {
//                m_telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f",
//                        fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());
//                refreshDesiredID();
//                m_telemetry.addData("CORRECT TAG ID?", checkDesiredTagID(fr.getFiducialId()));
                if (checkDesiredTagID(fr.getFiducialId())) {
                    targetFound = true;
                    desiredTag = fr;
                    tX = fr.getTargetXDegrees();
                    tY = fr.getTargetYDegrees();
                    botPose = result.getBotpose();
                    pitch = botPose.getOrientation().getPitch();
                    roll = botPose.getOrientation().getRoll();
                    yaw = botPose.getOrientation().getYaw();
                } else {
                    m_telemetry.addData("Skipping", "Tag ID %d is not desired", fr.getFiducialId());
                }
            }
        }
    }

    public double getTargetRange() {
        double angleToGoalRadians = Math.toRadians(65 + tY);
        return (50 - 11)/(Math.tan(angleToGoalRadians));
    }
    public double getYaw() {
        return yaw;
    }
    public double getPitch() {
        return pitch;
    }
    public double getRoll() {
        return roll;
    }
    public Pose3D getBotPose() {
        return botPose;
    }
    public boolean foundTarget() {
        return targetFound;
    }

    public double getTx() {
        return tX;
    }

    public double getTy() {
        return tY;
    }

    public void periodic(MultipleTelemetry telemetry) {
        refreshDesiredID();

        LLStatus status = limelight.getStatus();
        telemetry.addData("Name", "%s",
                status.getName());
        telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d",
                status.getTemp(), status.getCpu(),(int)status.getFps());
        telemetry.addData("Pipeline", "Index: %d, Type: %s",
                status.getPipelineIndex(), status.getPipelineType());
        telemetry.addData("targetFound", foundTarget());
        getAprilTagData(telemetry);
        tX = limelight.getLatestResult().getTx();
        tY = limelight.getLatestResult().getTy();
        botPose = limelight.getLatestResult().getBotpose();
        yaw = botPose.getOrientation().getYaw();
    }
}
