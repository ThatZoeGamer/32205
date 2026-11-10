package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;

public class Shooter extends SubsystemBase {
    private final DcMotorEx motorL;
    private final DcMotorEx motorR;
//    private final ServoEx axisMotor;
    private final PIDController shooterController;
    private final SimpleMotorFeedforward shooterFF;
    private double setpoint;
    private double currentVelocity;
    private boolean currentState;
    private final MultipleTelemetry telemetry;
    public Shooter (HardwareMap hardwareMap, MultipleTelemetry telemetry) {
        motorL = hardwareMap.get(DcMotorEx.class, Constants.shooterConstants.shooterMotorLid);
        motorR = hardwareMap.get(DcMotorEx.class, Constants.shooterConstants.shooterMotorRid);
//        axisMotor = hardwareMap.get(ServoEx.class, Constants.axisServoId);

        shooterController = new PIDController(
                Configs.shooterConfigs.shooterP,
                Configs.shooterConfigs.shooterI,
                Configs.shooterConfigs.shooterD);
//
        shooterFF = new SimpleMotorFeedforward(
                Configs.shooterConfigs.shooterKs,
                Configs.shooterConfigs.shooterKv);

        motorL.setDirection(DcMotorSimple.Direction.FORWARD);
        motorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorR.setDirection(DcMotorSimple.Direction.REVERSE);
        motorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.telemetry = telemetry;

    }
    public void setSetpoint(double desiredSetpoint) {
        setpoint = desiredSetpoint;
    }
    public double getSetpoint() {
        return setpoint;
    }
    public boolean LatSetpoint() {
        return getSetpoint() - getLeftRPM() < Math.abs(Configs.shooterConfigs.shooterSetpointTol);
    }
    public boolean RatSetpoint() {
        return getSetpoint() - getRightRPM() < Math.abs(Configs.shooterConfigs.shooterSetpointTol);
    }


    public void setPower(double power) {
        motorL.setPower(power);
        motorR.setPower(power);
    }
    public double getPower(String motor) {
        if(motor.equals(motorL.getDeviceName())) {
            return motorL.getPower();
        } else if (motor.equals(motorR.getDeviceName())) {
            return motorR.getPower();
        } else {
            return 6767;
        }
    }
    public DcMotorEx getLeftMotor() {
        return motorL;
    }
    public DcMotorEx getRightMotor() {
        return motorR;
    }

    /**
     * @return avg RPM of both motors
     */
    public double getRightRPM() {
        return ((motorR.getVelocity()/Constants.shooterConstants.ticksPerRev)*60);
    }
    public double getLeftRPM() {
        return ((motorL.getVelocity()/Constants.shooterConstants.ticksPerRev)*60);
    }

    public void runShooterR(double desiredVelocity) {
        motorR.setPower(Math.max(Math.min(
                    (shooterController.calculate(motorR.getVelocity(), desiredVelocity))
                            + shooterFF.calculate(desiredVelocity),
                    Configs.shooterConfigs.maxSpeed), 0));
    }
    public void runShooterL(double desiredVelocity) {
        motorL.setPower(
                Math.max(Math.min(
                    (shooterController.calculate(motorL.getVelocity(), desiredVelocity))
                            + shooterFF.calculate(desiredVelocity),
                    Configs.shooterConfigs.maxSpeed), 0));
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
        shooterController.setPID(Configs.shooterConfigs.shooterP, Configs.shooterConfigs.shooterI, Configs.shooterConfigs.shooterD);
//        setSetpoint(Configs.shooterConfigs.manualShooterVelocity);
        runShooterL(setpoint);
        runShooterR(setpoint);
        setSetpoint(Configs.shooterConfigs.manualShooterVelocity);
//        telemetry.addData("setpoint", setpoint);
        telemetry.addData("Left Motor Power: ", getPower("sMotorL"));
        telemetry.addData("Right Motor Power: ", getPower("sMotorR"));
        telemetry.addData("ticks L", motorL.getVelocity());
        telemetry.addData("ticks R", motorR.getVelocity());
//        telemetry.addData("Avg Motor RPM: ", (getLeftRPM()+getRightRPM())/2);
//        telemetry.addData("Right Motor RPM: ", getRightRPM());
//        telemetry.addData("right at setpoint?", RatSetpoint());
//        this.telemetry.addData("left at setpoint", LatSetpoint());
//        this.telemetry.addData("manual Shooter speed: ", Configs.shooterConfigs.manualShooterSpeed);

    }

}
