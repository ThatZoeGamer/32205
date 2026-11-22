package org.firstinspires.ftc.teamcode.Subsystems.Intake;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
    private final DcMotorEx intakeMotor;
    private final Servo lifterServo;
    private final CRServo intakeServo;
    private final Servo stopperServo;

    public Intake(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotorEx.class, IntakeConstants.intakeMotorId);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lifterServo = hardwareMap.get(Servo.class, Constants.IntakeConstants.lifterServoId);
        lifterServo.setPosition(Configs.IntakeConfigs.highPosition);
        lifterServo.setDirection(Servo.Direction.FORWARD);

        intakeServo = hardwareMap.get(CRServo.class, Constants.IntakeConstants.intakeServoId);
        intakeServo.setDirection(DcMotorSimple.Direction.FORWARD);

        stopperServo = hardwareMap.get(Servo.class, Constants.IntakeConstants.stopperServoId);
        stopperServo.setPosition(Configs.IntakeConfigs.openPosition);
        stopperServo.setDirection(Servo.Direction.FORWARD);
    }

    public void runIntake(double power) {
        intakeMotor.setPower(power);
    }
    public void runIntake() {
        intakeMotor.setPower(0.8);
    }
    public void stop() {
        intakeMotor.setPower(0);
    }
    public void transfer(double power) {
        intakeServo.setPower(power);
    }
    public void transfer() {
        intakeServo.setPower(1);
    }

    public void closeStopper() {
        stopperServo.setPosition(Configs.IntakeConfigs.closedPosition);
    }
    public void openStopper() {
        stopperServo.setPosition(Configs.IntakeConfigs.openPosition);
    }
    public double getStopperPosition() {
        return stopperServo.getPosition();
    }
    public boolean isStopperClosed() {
        return Math.abs(getStopperPosition() - Configs.IntakeConfigs.closedPosition) <= 1;
    }
    public boolean isStopperOpen() {
        return Math.abs(getStopperPosition() - Configs.IntakeConfigs.openPosition) <= 1;
    }

    public void lowerIntake() {
        lifterServo.setPosition(Configs.IntakeConfigs.loweredPosition);
    }
    public void liftIntake() {
        lifterServo.setPosition(Configs.IntakeConfigs.highPosition);
    }
    public void maxLiftIntake() {
        lifterServo.setPosition(Configs.IntakeConfigs.highPosition);
    }
    public double getLifterPosition() {
        return lifterServo.getPosition();
    }
    public boolean isIntakeLow() {
        return Math.abs(getLifterPosition() - Configs.IntakeConfigs.loweredPosition) <= 1;
    }
    public boolean isIntakeMid() {
        return Math.abs(getStopperPosition() - Configs.IntakeConfigs.midPosition) <= 1;
    }
    public boolean isIntakeHigh() {
        return Math.abs(getStopperPosition() - Configs.IntakeConfigs.highPosition) <= 1;
    }

    public void periodic(MultipleTelemetry telemetry) {
        if (telemetry != null) {
            telemetry.addData("I0 :----------INTAKE----------", "I");
            telemetry.addData("I0: Direction", intakeMotor.getDirection());
            telemetry.addData("I0: Power", intakeMotor.getPower());
            telemetry.addData("I1 :-------LIFTER-------", "I1");
            telemetry.addData("I1: Is Intake Low?", isIntakeLow());
            telemetry.addData("I1: Is Intake Mid?", isIntakeMid());
            telemetry.addData("I1: Is Intake High?", isIntakeHigh());
            telemetry.addData("I1: Lifter Position", getLifterPosition());
            telemetry.addData("I2 :-------STOPPER-------", "I2");
            telemetry.addData("I2: Stopper Position", getStopperPosition());
            telemetry.addData("I2: Is Stopper Closed?", isStopperClosed());
            telemetry.addData("I2: Is Stopper Open?", isStopperOpen());

        }
    }

}