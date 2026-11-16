package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
    private final DcMotorEx intakeMotor;

    public Intake(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotorEx.class, IntakeConstants.intakeMotorId);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void runIntake(GamepadEx gamepad) {
        if(gamepad.getButton(GamepadKeys.Button.LEFT_BUMPER)) {
            intakeMotor.setPower(-gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER));
        } else {
            intakeMotor.setPower(gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER));
        }
    }
    public void runIntake() {
        intakeMotor.setPower(1);
    }
    public void stop() {
        intakeMotor.setPower(0);
    }

    public void periodic(MultipleTelemetry telemetry) {
        if (telemetry != null) {
            telemetry.addData("I :----------INTAKE----------", "I");
            telemetry.addData("I: Direction", intakeMotor.getDirection());
            telemetry.addData("I: Power", intakeMotor.getPower());
        }
    }

}