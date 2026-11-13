package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.BatteryChecker;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.OpModes.Teleop;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;

public class Shooter extends SubsystemBase {
    private final DcMotorEx motorL;
    private final DcMotorEx motorR;
//    private final ServoEx axisMotor;
    private final PIDController LshooterController;
    private final PIDController RshooterController;
    private final SimpleMotorFeedforward LshooterFF;
    private final SimpleMotorFeedforward RshooterFF;
    private double Lsetpoint;
    private double Rsetpoint;
    private double currentVelocity;
    private boolean currentState;
    private final MultipleTelemetry telemetry;
    public Shooter (HardwareMap hardwareMap, MultipleTelemetry telemetry) {
        motorL = hardwareMap.get(DcMotorEx.class, Constants.shooterConstants.shooterMotorLid);
        motorR = hardwareMap.get(DcMotorEx.class, Constants.shooterConstants.shooterMotorRid);
//        axisMotor = hardwareMap.get(ServoEx.class, Constants.axisServoId);

        LshooterController = new PIDController(
                Configs.shooterConfigs.LshooterP,
                Configs.shooterConfigs.LshooterI,
                Configs.shooterConfigs.LshooterD);
        RshooterController = new PIDController(
                Configs.shooterConfigs.RshooterP,
                Configs.shooterConfigs.RshooterI,
                Configs.shooterConfigs.RshooterD);
//
        LshooterFF = new SimpleMotorFeedforward(
                Configs.shooterConfigs.LshooterKs,
                Configs.shooterConfigs.LshooterKv);
        RshooterFF = new SimpleMotorFeedforward(
                Configs.shooterConfigs.RshooterKs,
                Configs.shooterConfigs.RshooterKv);
//        shooterFF.minAchievableAcceleration()

        motorL.setDirection(DcMotorSimple.Direction.FORWARD);
        motorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorR.setDirection(DcMotorSimple.Direction.REVERSE);
        motorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.telemetry = telemetry;

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
    public boolean LatSetpoint() {
        return Math.abs(getLeftSetpoint() - getLeftRPM()) < Math.abs(Configs.shooterConfigs.shooterSetpointTol);
    }
    public boolean RatSetpoint() {
        return Math.abs(getRightSetpoint() - getRightRPM()) < Math.abs(Configs.shooterConfigs.shooterSetpointTol);
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

    public double getRightRPM() {
        double motorRPM = ((motorR.getVelocity()/Constants.shooterConstants.ticksPerRev)*60);
        return motorRPM / Constants.shooterConstants.gearRatio;
    }
    public double getLeftRPM() {
        double motorRPM = ((motorL.getVelocity()/Constants.shooterConstants.ticksPerRev)*60);
        return motorRPM / Constants.shooterConstants.gearRatio;
    }

    public void runShooterR(double desiredRPM) {
        double targetMotorRPM = desiredRPM * Constants.shooterConstants.gearRatio;
        double targetMotorTicksPerSec = (targetMotorRPM / 60.0) * Constants.shooterConstants.ticksPerRev;

        double dv = RshooterController.calculate(motorR.getVelocity(), targetMotorTicksPerSec)
                + RshooterFF.calculate(targetMotorTicksPerSec);
        motorR.setPower(Math.max(Math.min(dv, Configs.shooterConfigs.maxSpeed), 0));
//        double dv = RshooterController.calculate(motorR.getVelocity(), desiredVelocity)
//                + RshooterFF.calculate(motorR.getVelocity(), desiredVelocity);
//        motorR.setPower(Math.max(Math.min(dv, Configs.shooterConfigs.maxSpeed), 0));
    }
    public void runShooterL(double desiredRPM) {
        double targetMotorRPM = desiredRPM * Constants.shooterConstants.gearRatio;
        double targetMotorTicksPerSec = (targetMotorRPM / 60.0) * Constants.shooterConstants.ticksPerRev;

        double dv = RshooterController.calculate(motorR.getVelocity(), targetMotorTicksPerSec)
                + RshooterFF.calculate(targetMotorTicksPerSec);
        motorR.setPower(Math.max(Math.min(dv, Configs.shooterConfigs.maxSpeed), 0));
//        double dv = LshooterController.calculate(motorL.getVelocity(), desiredVelocity)
//                + LshooterFF.calculate(motorL.getVelocity(), desiredVelocity);
//        motorL.setPower(Math.max(Math.min(dv, Configs.shooterConfigs.maxSpeed), 0));
    }
    public double getCurrent() {
        return (motorL.getCurrent(CurrentUnit.AMPS) + motorR.getCurrent(CurrentUnit.AMPS))/2;
    }

    public boolean justShotBall() {
        boolean lastState = currentState;
        currentState = getCurrent() < Configs.shooterConfigs.pollenCurrentThresh;
        return (lastState && !currentState);
    }

    public String lastShotBall() {
        if(justShotBall()) {
            if(getCurrent()<Configs.shooterConfigs.nectarCurrentThresh){
                return "lost nectar";
            } else {
                return "lost pollen";
            }
        } else {
            return "cooked";
        }
    }

    public void stopShooters() {
        setPower(0);
    }

    public void periodic(MultipleTelemetry telemetry, double speed) {
        LshooterController.setPID(Configs.shooterConfigs.LshooterP, Configs.shooterConfigs.LshooterI, Configs.shooterConfigs.LshooterD);
        RshooterController.setPID(Configs.shooterConfigs.RshooterP, Configs.shooterConfigs.RshooterI, Configs.shooterConfigs.RshooterD);
//        setSetpoint(Configs.shooterConfigs.manualShooterVelocity);
        runShooterL(Lsetpoint);
        runShooterR(Rsetpoint);
        setLSetpoint(Configs.shooterConfigs.manualShooterVelocity);
        setRSetpoint(Configs.shooterConfigs.manualShooterVelocity);
//        telemetry.addData("setpoint", setpoint);
        telemetry.addData("Left setpoint", getLeftSetpoint());
        telemetry.addData("Right setpoint", getRightSetpoint());
        telemetry.addData("ticks L", motorL.getVelocity());
        telemetry.addData("ticks R", motorR.getVelocity());
        telemetry.addData("rpm L", getLeftRPM());
        telemetry.addData("rpm R", getRightRPM());

    }
}