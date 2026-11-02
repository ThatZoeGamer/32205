package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Limelight {
    Limelight3A limelight;
    public Limelight(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    public void periodic(MultipleTelemetry telemetry) {
        LLResult result = limelight.getLatestResult();
        LLStatus status = limelight.getStatus();
        telemetry.addData("Pipeline", "%d - %s",
                status.getPipelineIndex(),
                status.getPipelineType());
        telemetry.addData("LIMELIGHT FPS", status.getFps());
    }
}
