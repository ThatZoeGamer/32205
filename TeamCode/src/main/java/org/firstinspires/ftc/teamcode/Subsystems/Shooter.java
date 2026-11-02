package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

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
    private boolean lastState;
    private boolean currentState;
    public Shooter (HardwareMap hardwareMap) {
        motorL = hardwareMap.get(DcMotorEx.class, Constants.shooterConstants.shooterMotorLid);
        motorR = hardwareMap.get(DcMotorEx.class, Constants.shooterConstants.shooterMotorRid);
//        axisMotor = hardwareMap.get(ServoEx.class, Constants.axisServoId);

        shooterController = new PIDController(
                Configs.shooterConfigs.shooterP,
                Configs.shooterConfigs.shooterI,
                Configs.shooterConfigs.shooterD);

        shooterFF = new SimpleMotorFeedforward(
                Configs.shooterConfigs.shooterKs,
                Configs.shooterConfigs.shooterKv);

        motorL.setDirection(DcMotorSimple.Direction.FORWARD);
        motorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorR.setDirection(DcMotorSimple.Direction.REVERSE);
        motorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
    /**
     * @param motor "sMotorL" or "sMotorR".
     * @return the power of inputted motor
     */
    public double getPower(String motor) {
        if(motor.equals(motorL.getDeviceName())) {
            return motorL.getPower();
        } else if (motor.equals(motorR.getDeviceName())) {
            return motorR.getPower();
        } else {
            return 6767;
        }
    }
    /**
     * @param motor "sMotorL" or "sMotorR"
     * @return Velocity of inputted motor in ticks
     */
    public double getVelocity(String motor) {
        if(motor.equals(motorL.getDeviceName())) {
            return motorL.getVelocity();
        } else if (motor.equals(motorR.getDeviceName())) {
            return motorL.getVelocity();
        } else {
            return 6767;
        }
    }
    /**
     * @return average power of both motors
     */
    public double getPower() {
        return (motorL.getPower()+ motorR.getPower())/2;
    }
//    /**
//     * @param motor "sMotorL" or "sMotorR"
//     * @return RPM of inputted motor
//     */
//    public double getRPM(String motor) {
//        if(motor.equals(motorL.getDeviceName())) {
//            return (getVelocity(motor)/Constants.shooterConstants.ticksPerRev*60);
//        } else if (motor.equals(motorR.getDeviceName())) {
//            return (getVelocity(motor)/Constants.shooterConstants.ticksPerRev*60);
//        } else {
//            return 500;
//        }
//    }

    /**
     * @return avg RPM of both motors
     */
    public double getRightRPM() {
        return (motorR.getVelocity()/Constants.shooterConstants.ticksPerRev*60);
    }
    public double getLeftRPM() {
        return motorL.getVelocity()/Constants.shooterConstants.ticksPerRev*60;
    }

    public void runShooterR(double desiredVelocity) {
        double currentVelocity = getRightRPM();

        motorR.setPower(Math.max(Math.min(
                    (shooterController.calculate(currentVelocity, desiredVelocity))
                            + shooterFF.calculate(desiredVelocity),
                    Configs.shooterConfigs.maxSpeed), 0));
    }
    public void runShooterL(double desiredVelocity) {
        double currentVelocity = getLeftRPM();

        motorL.setPower(
                Math.max(Math.min(
                    (shooterController.calculate(currentVelocity, desiredVelocity))
                            + shooterFF.calculate(desiredVelocity),
                    Configs.shooterConfigs.maxSpeed), 0));
    }

    public void setDesiredVelocity(double targetRange) {
//        double desiredVelocity = (-0.0036901 * Math.pow(targetRange, 2)) + (7.60414 * targetRange) + 1982.12807;
//        setSetpoint(desiredVelocity * Configs.shooterConfigs.shooterKs);
    }
    /**
     *
     * @param motor "sMotorL" or "sMotorR"
     * @return Current of inputted motor
     */
    public double getCurrent(String motor) {
        if (motor.equals(motorL.getDeviceName())) {
            return motorL.getCurrent(CurrentUnit.AMPS);
        } else if (motor.equals(motorR.getDeviceName())) {
            return motorR.getCurrent(CurrentUnit.AMPS);
        } else {
            return 6767;
        }
    }
    /**
     * @return Avg current of both motors
     */
    public double getCurrent() {
        return (motorL.getCurrent(CurrentUnit.AMPS) + motorR.getCurrent(CurrentUnit.AMPS))/2;
    }

    public boolean justShotBall() {
        lastState = currentState;
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

    public void periodic(MultipleTelemetry telemetry) {
//        setSetpoint(Configs.shooterConfigs.manualShooterVelocity);
//        runShooterL(Configs.shooterConfigs.manualShooterVelocity);
        runShooterR(Configs.shooterConfigs.manualShooterVelocity);
//        telemetry.addData("Left Motor Power: ", getPower("sMotorL"));
//        telemetry.addData("Right Motor Power: ", getPower("sMotorR"));
//        telemetry.addData("Avg Motor Power: ", getPower());
//        telemetry.addData("Left Motor Current: ", getCurrent("sMotorL"));
//        telemetry.addData("Right Motor Current: ", getCurrent("sMotorR"));
//        telemetry.addData("Avg Motor Current: ", getCurrent());
//        telemetry.addData("Left Motor RPM: ", getLeftRPM());
//        telemetry.addData("Avg Motor RPM: ", getRPM());
        telemetry.addData("Right Motor RPM: ", getRightRPM());
        telemetry.addData("right at setpoint?", RatSetpoint());
//        telemetry.addData("left at setpoint", LatSetpoint());
//        telemetry.addData("manual Shooter speed: ", Configs.shooterConfigs.manualShooterSpeed);

//        runShooterR(setpoint);
//        setPower(Configs.shooterConfigs.manualShooterSpeed);
//        Configs.minMaxMan(Configs.shooterConfigs.manualShooterSpeed);

    }

}
