package org.firstinspires.ftc.teamcode.Subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configs.ShooterConfigs;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Constants.ShooterConstants;
import org.firstinspires.ftc.teamcode.Utilities.InterpolatingDoubleTreeMap;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;


public class Shooter extends SubsystemBase {
    private final DcMotorEx motorL;
    private final DcMotorEx motorR;
//    private final ServoEx axisMotor;
    private final PIDController LshooterController;
    private final SimpleMotorFeedforward LshooterFF;
    private final PIDController RshooterController;
    private final SimpleMotorFeedforward RshooterFF;
    private double Lsetpoint;
    private double Rsetpoint;
    private double currentVelocity;
    private boolean lastState;
    InterpolatingDoubleTreeMap shooterMap;
    private boolean currentState;
    private final VoltageSensor vs;
    public Shooter (HardwareMap hardwareMap, VoltageSensor voltageSensor) {
        vs = voltageSensor;
        motorL = hardwareMap.get(DcMotorEx.class, ShooterConstants.shooterMotorLid);
        motorR = hardwareMap.get(DcMotorEx.class, ShooterConstants.shooterMotorRid);

        LshooterController = new PIDController(
                ShooterConfigs.LeftShooter.LshooterP, 0, 0);
        RshooterController = new PIDController(
                ShooterConfigs.RightShooter.RshooterP, 0, 0);
//
        LshooterFF = new SimpleMotorFeedforward(
                ShooterConfigs.LeftShooter.LshooterKs,
                ShooterConfigs.LeftShooter.LshooterKv);
        RshooterFF = new SimpleMotorFeedforward(
                ShooterConfigs.RightShooter.RshooterKs,
                ShooterConfigs.RightShooter.RshooterKv);
//        RshooterFF.maxAchievableAcceleration()

        motorL.setDirection(DcMotorSimple.Direction.FORWARD);
        motorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motorR.setDirection(DcMotorSimple.Direction.REVERSE);
        motorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }
    public void setLSetpoint(double desiredSetpoint) {
        Lsetpoint = desiredSetpoint;
    }
    public void setRSetpoint(double desiredSetpoint) {
        Rsetpoint = desiredSetpoint;
    }
    public double getLeftSetpoint() {
        return Lsetpoint;
    }
    public double getRightSetpoint() {
        return Rsetpoint;
    }
    public boolean atSetpoint() {
        return Math.abs(((getLeftSetpoint() + getRightSetpoint()) / 2) - motorL.getVelocity()) <= Math.abs(ShooterConfigs.Misc.shooterTolerance);
    }
    public void setPower(double power) {
        motorL.setPower(power);
        motorR.setPower(power);
    }
    public DcMotorEx getLeftMotor() {
        return motorL;
    }
    public DcMotorEx getRightMotor() {
        return motorR;
    }
    public double getPower() {
        return (motorL.getPower() + motorR.getPower()) / 2;
    }
    public double getRightRPM() {
        double motorRPM = ((motorR.getVelocity() / ShooterConstants.ticksPerRev) * 60);
        return motorRPM / ShooterConstants.gearRatio;
    }

    public double getLeftRPM() {
        double motorRPM = ((motorL.getVelocity() / ShooterConstants.ticksPerRev) * 60);
        return motorRPM / ShooterConstants.gearRatio;
    }
    public void runShooterR(double desiredVelocity) {
        double targetMotorTicksPerSec = desiredVelocity * ShooterConstants.gearRatio;

        double dv = RshooterController.calculate(motorR.getVelocity(), targetMotorTicksPerSec)
                + RshooterFF.calculate(targetMotorTicksPerSec);
        double compensatedPower = dv * (ShooterConfigs.Misc.NominalVoltage / vs.getVoltage());
        motorR.setPower(compensatedPower);
    }
    public void runShooterL(double desiredVelocity) {
        double targetMotorTicksPerSec = desiredVelocity * ShooterConstants.gearRatio;

        double dv = LshooterController.calculate(motorL.getVelocity(), targetMotorTicksPerSec)
                + LshooterFF.calculate(targetMotorTicksPerSec);
        double compensatedPower = dv * (ShooterConfigs.Misc.NominalVoltage / vs.getVoltage());
        motorL.setPower(compensatedPower);
    }
    public double getCurrent() {
        return (motorL.getCurrent(CurrentUnit.AMPS) + motorR.getCurrent(CurrentUnit.AMPS)) / 2;
    }
    public boolean justShotBall() {
        if (!atSetpoint()) {
            currentState = false;
            return false;
        }

        boolean lastState = currentState;
        currentState = getCurrent() > ShooterConfigs.Misc.ballShootingThreshold;
        return lastState && !currentState;
    }
    public void stopShooters() {
        setPower(0);
    }

    public void periodic(Telemetry telemetry) {
        LshooterController.setP(ShooterConfigs.LeftShooter.LshooterP);
        RshooterController.setP(ShooterConfigs.RightShooter.RshooterP);
        runShooterR(Rsetpoint);
        runShooterL(Lsetpoint);

        if (telemetry != null) {
            telemetry.addData("L :----------LEFT MOTOR----------", "L");
            telemetry.addData("L: Left Current", motorL.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("L: Left Setpoint", getLeftSetpoint());
            telemetry.addData("L: Left TPS", motorL.getVelocity());
            telemetry.addData("L: Left Power", motorL.getPower());
            telemetry.addData("L: Left Voltage", vs.getVoltage() * motorL.getPower());

            telemetry.addData("R :----------RIGHT MOTOR----------", "R");
            telemetry.addData("R: Right Current", motorR.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("R: Right Setpoint", getRightSetpoint());
            telemetry.addData("R: Right TPS", motorR.getVelocity());
            telemetry.addData("R: Right Power", motorR.getPower());
            telemetry.addData("R: Right Voltage", vs.getVoltage() * motorR.getPower());
        }
    }


}
