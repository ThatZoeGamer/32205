package org.firstinspires.ftc.teamcode.Subsystems.Intake;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
    private final DcMotorEx intakeMotor1;
//    private final DcMotorEx intakeMotor2;
    private final Servo lifterServo;

    private final Servo stopperServo;

    public Intake(HardwareMap hardwareMap) {
        intakeMotor1 = hardwareMap.get(DcMotorEx.class, IntakeConstants.intakeMotor1Id);
        intakeMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        intakeMotor2 = hardwareMap.get(DcMotorEx.class, IntakeConstants.intakeMotor2Id);
//        intakeMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
//        intakeMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lifterServo = hardwareMap.get(Servo.class, Constants.IntakeConstants.lifterServoId);
//        lifterServo.setPosition(4);
        lifterServo.setDirection(Servo.Direction.FORWARD);
        lifterServo.scaleRange(0, 1);
        lifterServo.getController().pwmEnable();

        stopperServo = hardwareMap.get(Servo.class, Constants.IntakeConstants.stopperServoId);
        stopperServo.setPosition(Configs.IntakeConfigs.closedPosition);
        stopperServo.setDirection(Servo.Direction.FORWARD);
        stopperServo.scaleRange(0.1, 0.3);
    }

    public void runIntake(double power) {
        intakeMotor1.setPower(power);
        stopperServo.getController().pwmEnable();
//        intakeMotor2.setPower(3*power/5);
    }
    public void runIntake() {
        intakeMotor1.setPower(-0.8);
        stopperServo.getController().pwmEnable();
//        intakeMotor2.setPower(-0.6);
    }
    public void stop() {
        intakeMotor1.setPower(0);
//        intakeMotor2.setPower(0);
    }
    public void setPose(double pose) {
        lifterServo.setPosition(pose);
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
        lifterServo.getController().pwmDisable();
    }
    public void liftIntake() {
        lifterServo.getController().pwmEnable();
        lifterServo.setPosition(Configs.IntakeConfigs.highPosition);

    }
    public double getLifterPosition() {
        return lifterServo.getPosition();
    }
    public boolean isIntakeLow() {
        return Math.abs(getLifterPosition() - Configs.IntakeConfigs.loweredPosition) <= 1;
    }
    public boolean isIntakeMid() {
        return Math.abs(getLifterPosition() - Configs.IntakeConfigs.midPosition) <= 1;
    }
    public boolean isIntakeHigh() {
        return Math.abs(getLifterPosition() - Configs.IntakeConfigs.highPosition) <= 1;
    }

    public void periodic(MultipleTelemetry m_telemetry, Telemetry telemetry) {
        if (!Configs.Toggles.compMode) {
            m_telemetry.addData("I0 :----------INTAKE----------", "I");
            m_telemetry.addData("I0 :----------INTAKE----------", "I");
            m_telemetry.addData("I0: Direction 1", intakeMotor1.getDirection());
            m_telemetry.addData("I0: Power 1", intakeMotor1.getPower());
//            m_telemetry.addData("I0: Direction 2", intakeMotor2.getDirection());
//            m_telemetry.addData("I0: Power 2", intakeMotor2.getPower());

            m_telemetry.addData("I1 :-------LIFTER-------", "I1");
            m_telemetry.addData("I1: Is Intake Low?", isIntakeLow());
            m_telemetry.addData("I1: Is Intake Mid?", isIntakeMid());
            m_telemetry.addData("I1: Is Intake High?", isIntakeHigh());
            m_telemetry.addData("I1: Lifter Position", getLifterPosition());

            m_telemetry.addData("I2 :-------STOPPER-------", "I2");
            m_telemetry.addData("I2: Stopper Position", getStopperPosition());
            m_telemetry.addData("I2: Is Stopper Closed?", isStopperClosed());
            m_telemetry.addData("I2: Is Stopper Open?", isStopperOpen());
        } else {
            telemetry.addData("I0 :----------INTAKE----------", "I");
            telemetry.addData("I0: Direction 1", intakeMotor1.getDirection());
            telemetry.addData("I0: Power 1", intakeMotor1.getPower());
//            telemetry.addData("I0: Direction 2", intakeMotor2.getDirection());
//            telemetry.addData("I0: Power 2", intakeMotor2.getPower());

//            telemetry.addData("I1: Is Intake Low?", isIntakeLow());
//            telemetry.addData("I1: Is Intake Mid?", isIntakeMid());
//            telemetry.addData("I1: Is Intake High?", isIntakeHigh());

//            telemetry.addData("I1: Lifter Position", getLifterPosition());
//            telemetry.addData("I2 :-------STOPPER-------", "I2");
//            telemetry.addData("I2: Stopper Position", getStopperPosition());
//            telemetry.addData("I2: Is Stopper Closed?", isStopperClosed());
//            telemetry.addData("I2: Is Stopper Open?", isStopperOpen());
        }
    }

}