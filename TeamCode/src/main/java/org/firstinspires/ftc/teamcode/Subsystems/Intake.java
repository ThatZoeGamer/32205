package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Constants;

public class Intake extends SubsystemBase {
    private final DcMotorEx intakeMotor;
    private final MultipleTelemetry telemetry;

    public Intake(HardwareMap hardwaremap, MultipleTelemetry telemetry) {
        intakeMotor = hardwaremap.get(DcMotorEx.class, Constants.intakeConstants.intakeMotorId);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.telemetry = telemetry;
    }

    public void runIntake(double power) {
        intakeMotor.setPower(power);
    }

    public void runOuttake(double power) {
        intakeMotor.setPower(-power);
    }

    public void stop() {
        runIntake(0);
    }

    public void periodic(MultipleTelemetry telemetry) {
        telemetry.addData("Direction", intakeMotor.getDirection());
        telemetry.addData("Power", intakeMotor.getPower());
    }

}