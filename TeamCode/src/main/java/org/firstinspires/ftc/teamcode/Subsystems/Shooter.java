package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

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
    private final MultipleTelemetry telemetry;
    public Shooter (HardwareMap hardwareMap, MultipleTelemetry telemetry) {
        motorL = hardwareMap.get(DcMotorEx.class, Constants.shooterConstants.shooterMotorLid);
        motorR = hardwareMap.get(DcMotorEx.class, Constants.shooterConstants.shooterMotorRid);
//        axisMotor = hardwareMap.get(ServoEx.class, Constants.axisServoId);

        shooterController = new PIDController(
                Configs.shooterConfigs.shooterP,
                Configs.shooterConfigs.shooterI,
                Configs.shooterConfigs.shooterD);
//        shooterController.enableContinuousInput(-1, 1);

        shooterFF = new SimpleMotorFeedforward(
                Configs.shooterConfigs.shooterKs,
                Configs.shooterConfigs.shooterKv);


        motorL.setDirection(DcMotorSimple.Direction.FORWARD);
        motorL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorR.setDirection(DcMotorSimple.Direction.REVERSE);
        motorR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.telemetry = telemetry;
    }
    public void setSetpoint(double desiredSetpoint) {
        setpoint = desiredSetpoint;
    }
    public double getSetpoint() {
        return setpoint;
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
        if(motor.equals("sMotorL")) {
            return motorL.getPower();
        } else if (motor.equals("sMotorR")) {
            return motorR.getPower();
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
    /**
     * @param motor "sMotorL" or "sMotorR"
     * @return RPM of inputted motor
     */
    public double getRPM(String motor) {
        if(motor.equals("sMotorL")) {
            return motorL.getVelocity()/Constants.shooterConstants.ticksPerRev*60;
        } else if (motor.equals("sMotorR")) {
            return motorR.getVelocity()/Constants.shooterConstants.ticksPerRev*60;
        } else {
            return 6767;
        }
    }

    /**
     * @return avg RPM of both motors
     */
    public double getRPM() {
        return (getRPM("sMotorL")+getRPM("sMotorR"))/2;
    }

    public void runShooters(double desiredVelocity) {
        double currentVelocity = getRPM();

        setPower(Math.max(Math.min(
                (shooterController.calculate(currentVelocity, desiredVelocity)) + shooterFF.calculate(desiredVelocity),
                Configs.shooterConfigs.maxPower), 0));
    }
    /**
     * @param motor "sMotorL" or "sMotorR"
     * @return Current of inputted motor
     */
    public double getCurrent(String motor) {
        if (motor.equals("sMotorL")) {
            return motorL.getCurrent(CurrentUnit.AMPS);
        } else if (motor.equals("sMotorR")) {
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
        telemetry.addData("Left Motor Power: ", getPower("sMotorL"));
        telemetry.addData("Right Motor Power: ", getPower("sMotorR"));
        telemetry.addData("Avg Motor Power: ", getPower());
        telemetry.addData("Left Motor Current: ", getCurrent("sMotorL"));
        telemetry.addData("Right Motor Current: ", getCurrent("sMotorR"));
        telemetry.addData("Avg Motor Current: ", getCurrent());
        telemetry.addData("Left Motor RPM: ", getRPM("sMotorL"));
        telemetry.addData("Avg Motor RPM: ", getRPM());
        telemetry.addData("Right Motor RPM: ", getRPM("sMotorR"));
        telemetry.addData("manual Shooter speed: ", Configs.shooterConfigs.manualShooterSpeed);
//        runShooters(Configs.shooterConfigs.manualShooterSpeed/100);
        setPower(Configs.shooterConfigs.manualShooterSpeed);
        Configs.minMaxMan(Configs.shooterConfigs.manualShooterSpeed);
    }

}
