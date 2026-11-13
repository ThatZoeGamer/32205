package org.firstinspires.ftc.teamcode.Subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants;

public class Transmission extends SubsystemBase {
    CRServo intakeServo;
    public Transmission(HardwareMap hardwareMap) {
        intakeServo = hardwareMap.get(CRServo.class, Constants.intakeConstants.intakeServoId);
        intakeServo.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    public void transfer(GamepadEx gamepad) {
        if(gamepad.getButton(GamepadKeys.Button.LEFT_BUMPER)) {
            intakeServo.setPower(-gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER));
        } else {
            if(gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)>0) {
                intakeServo.setPower(gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER));
            } else {
                intakeServo.setPower(gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER));
            }
        }
    }
}
