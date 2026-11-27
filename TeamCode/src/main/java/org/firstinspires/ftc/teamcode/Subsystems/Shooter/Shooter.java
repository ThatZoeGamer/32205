package org.firstinspires.ftc.teamcode.Subsystems.Shooter;

import static org.firstinspires.ftc.teamcode.Configs.ShooterConfigs.Misc.kSync;
import static org.firstinspires.ftc.teamcode.Configs.ShooterConfigs.Misc.shooterTolerance;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Configs.ShooterConfigs.LeftShooter;
import org.firstinspires.ftc.teamcode.Configs.ShooterConfigs.RightShooter;
import org.firstinspires.ftc.teamcode.Configs.ShooterConfigs.Misc;
import org.firstinspires.ftc.teamcode.Constants.ShooterConstants;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.SlewRateLimiter;


public class Shooter extends SubsystemBase {
    private final DcMotorEx motorL;
    private final DcMotorEx motorR;
    private final PIDController LshooterController;
    private final SimpleMotorFeedforward LshooterFF;
    private final PIDController RshooterController;
    private final SimpleMotorFeedforward RshooterFF;

    private final SlewRateLimiter leftRamp = new SlewRateLimiter(4000);
    private final SlewRateLimiter rightRamp = new SlewRateLimiter(4000);
    private double Lsetpoint;
    private double Rsetpoint;
    private boolean currentState;
    private final VoltageSensor vs;

    public Shooter(HardwareMap hardwareMap, VoltageSensor voltageSensor) {
        vs = voltageSensor;
        motorL = hardwareMap.get(DcMotorEx.class, ShooterConstants.shooterMotorLid);
        motorR = hardwareMap.get(DcMotorEx.class, ShooterConstants.shooterMotorRid);

        LshooterController = new PIDController(LeftShooter.lShooterP, LeftShooter.lShooterI, LeftShooter.lShooterD);
        RshooterController = new PIDController(RightShooter.RshooterP, RightShooter.RshooterI, RightShooter.RshooterD);

        LshooterFF = new SimpleMotorFeedforward(
                LeftShooter.lShooterKs,
                LeftShooter.lShooterKv);
        RshooterFF = new SimpleMotorFeedforward(
                RightShooter.RshooterKs,
                RightShooter.RshooterKv);

        motorL.setDirection(DcMotorSimple.Direction.FORWARD);
        motorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        motorR.setDirection(DcMotorSimple.Direction.REVERSE);
        motorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
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
        double targetL = Lsetpoint * ShooterConstants.gearRatio;
        double targetR = Rsetpoint * ShooterConstants.gearRatio;
        double tol = Math.abs(Misc.shooterTolerance);
        return Math.abs(targetL - motorL.getVelocity()) <= tol
                && Math.abs(targetR - motorR.getVelocity()) <= tol;
    }

    public void runShooterR(double desiredVelocity) {
        double velR = motorR.getVelocity();
        double rampedVelocity = rightRamp.calculate(desiredVelocity);
        double targetMotorTicksPerSec = rampedVelocity * ShooterConstants.gearRatio;

        if (Math.abs(desiredVelocity) < 200) {
            rightRamp.reset(velR / ShooterConstants.gearRatio);
            motorR.setPower(0);
            return;
        }

        if (Math.abs(targetMotorTicksPerSec - velR) < 50) {
            RshooterController.setI(RightShooter.RshooterI);
        } else {
            RshooterController.setI(0);
            RshooterController.reset();
        }

        double syncVolts = (Lsetpoint == Rsetpoint)
                ? kSync * Math.max(0, motorL.getVelocity() - velR)
                : 0;

        double targetVolts = RshooterController.calculate(velR, targetMotorTicksPerSec)
                + RshooterFF.calculate(targetMotorTicksPerSec)
                + syncVolts;

        motorR.setPower(targetVolts);
    }

    public void runShooterL(double desiredVelocity) {
        double velL = motorL.getVelocity();
        double rampedVelocity = leftRamp.calculate(desiredVelocity);
        double targetMotorTicksPerSec = rampedVelocity * ShooterConstants.gearRatio;

        if (Math.abs(desiredVelocity) < 200) {
            leftRamp.reset(velL / ShooterConstants.gearRatio);
            motorL.setPower(0);
            return;
        }

        if (Math.abs(targetMotorTicksPerSec - velL) < shooterTolerance) {
            LshooterController.setI(LeftShooter.lShooterI);
        } else {
            LshooterController.setI(0);
            LshooterController.reset();
        }

        double syncVolts = (Lsetpoint == Rsetpoint)
                ? kSync * Math.max(0, motorR.getVelocity() - velL)
                : 0;

        double targetVolts = LshooterController.calculate(velL, targetMotorTicksPerSec)
                + LshooterFF.calculate(targetMotorTicksPerSec)
                + syncVolts;

        motorL.setPower(targetVolts);
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
        currentState = getCurrent() > Misc.ballShootingThreshold;
        return lastState && !currentState;
    }

    public void periodic(MultipleTelemetry m_telemetry, Telemetry telemetry) {
        LshooterController.setPID(LeftShooter.lShooterP, LeftShooter.lShooterI, LeftShooter.lShooterD);
        RshooterController.setPID(RightShooter.RshooterP, RightShooter.RshooterI, RightShooter.RshooterD);
        runShooterR(Rsetpoint);
        runShooterL(Lsetpoint);

        if (!Configs.Toggles.compMode) {
            m_telemetry.addData("S :----------SHOOTER----------", "S");
            m_telemetry.addData("SL :-------LEFT MOTOR-------", "SL");
            m_telemetry.addData("SL: Left Current", motorL.getCurrent(CurrentUnit.AMPS));
            m_telemetry.addData("SL: Left Setpoint", getLeftSetpoint());
            m_telemetry.addData("SL: Left TPS", motorL.getVelocity());
            m_telemetry.addData("SL: Left Power", motorL.getPower());
            m_telemetry.addData("SL: Left Voltage", vs.getVoltage() * motorL.getPower());

            m_telemetry.addData("SR: :-------RIGHT MOTOR-------", "SR");
            m_telemetry.addData("SR: Right Current", motorR.getCurrent(CurrentUnit.AMPS));
            m_telemetry.addData("SR: Right Setpoint", getRightSetpoint());
            m_telemetry.addData("SR: Right TPS", motorR.getVelocity());
            m_telemetry.addData("SR: Right Power", motorR.getPower());
            m_telemetry.addData("SR: Right Voltage", vs.getVoltage() * motorR.getPower());
        } else {
            telemetry.addData("S :----------SHOOTER----------", "S");
            telemetry.addData("SL :-------LEFT MOTOR-------", "SL");
            telemetry.addData("SL: Left Current", motorL.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("SL: Left Setpoint", getLeftSetpoint());
            telemetry.addData("SL: Left TPS", motorL.getVelocity());
            telemetry.addData("SL: Left Power", motorL.getPower());
            telemetry.addData("SL: Left Voltage", vs.getVoltage() * motorL.getPower());

            telemetry.addData("SR: :-------RIGHT MOTOR-------", "SR");
            telemetry.addData("SR: Right Current", motorR.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("SR: Right Setpoint", getRightSetpoint());
            telemetry.addData("SR: Right TPS", motorR.getVelocity());
            telemetry.addData("SR: Right Power", motorR.getPower());
            telemetry.addData("SR: Right Voltage", vs.getVoltage() * motorR.getPower());
        }
    }
}
