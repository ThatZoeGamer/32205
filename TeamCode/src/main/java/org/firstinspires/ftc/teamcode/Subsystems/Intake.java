package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Constants;

public class Intake extends SubsystemBase {
    private final DcMotorEx intakeMotor;

    public Intake(HardwareMap hardwaremap) {
        intakeMotor = hardwaremap.get(DcMotorEx.class, Constants.intakeConstants.intakeMotorId);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void runIntake(double power) {
        intakeMotor.setPower(power);
    }

    public void stop() {
        intakeMotor.setPower(0);
    }

    public void periodic(MultipleTelemetry telemetry) {
//        telemetry.addLine("Intake: ")
        telemetry.addData("Direction", intakeMotor.getDirection());
        telemetry.addData("Power", intakeMotor.getPower());
        telemetry.addData("Intake Current", intakeMotor.getCurrent(CurrentUnit.AMPS));

    }

}