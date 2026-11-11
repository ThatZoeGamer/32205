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

//TODO: Gonna add Limelight apriltag detection for the turret to turn the turret in the correct direction.
//TODO: For now this is just basic turret code, should work, hasn't been tested.
//TODO: Configs and Constants.
//TODO: Add to Teleop once Limelight stuff has been added.
public class Turret extends SubsystemBase {
    //Initialization BS
    private final PIDController turret;
    private final DcMotorEx turretMotor;

    private double desiredVelocity;

    public Turret(HardwareMap hardwareMap) {
        turretMotor = hardwareMap.get(DcMotorEx.class, Constants.turretConstants.turretMotorid);
        turret = new PIDController(
                Configs.turretConfigs.turretP,
                Configs.turretConfigs.turretI,
                Configs.turretConfigs.turretD);
        turret.enableContinuousInput(-180, 180);
        turretMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    //PID Methods, setpoint stuff and currentposition
    public void setSetpoint(double desiredSetpoint) {
        Configs.turretConfigs.setpoint = desiredSetpoint;
    }
    public double getSetpoint() {
        return Configs.turretConfigs.setpoint;
    }
    public double getPosition() {
        return turretMotor.getCurrentPosition();
    }
    public boolean atSetpoint() {
        return Math.abs(Configs.turretConfigs.setpoint - getRobotTurretAngle()) <= Configs.turretConfigs.positionTolerance;
    }

    //TODO: Need to add a controller button to set setpoint to zero.
    //TODO: Unless there is a button we can use for all zeroing???
    public void zero() {
        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Configs.turretConfigs.setpoint = 0.0;
    }

    //idk why I added this, thought we should stop a robot cause robot should be stopped :)
    public void stop() {
        turretMotor.setPower(0.0);
    }

    public double getRobotTurretAngle() {
        double rotations = getPosition()/(24*3.7*10.55);
        return rotations * 360;
    }

//    public double wrapAngle(double angleD) {
//        angleD = angleD % 360;
//
//        if(angleD > 180) {
//            angleD -= 360;
//        }
//        if(angleD < -180) {
//            angleD += 360;
//        }
//        return angleD;
//    }

    public void setTurretPosition(double desiredAngle) {
        if(!atSetpoint()) {
            double power = turret.calculate(getRobotTurretAngle(), desiredAngle);
            turretMotor.setPower(power);
        } else {
            stopTurret();
        }
    }

    public void justSetPower() {
        turretMotor.setPower(0.3);
    }

    public void stopTurret() {
        turretMotor.setPower(0);
    }

    public void periodic(MultipleTelemetry telemetry) {
        //Calculating where the turret is currently positioned and turning the Turret to a desired setPoint.
//        int power = turret.calculate(currentPosition, Configs.turretConfigs.setpoint);
//        power = Math.max(-Configs.turretConfigs.maxSpeed, Math.min(Configs.turretConfigs.maxSpeed, power));
//        turretMotor.setTargetPosition((int) turret.calculate(currentPosition, getSetpoint()));

        //Telemetry
        telemetry.addData("At setpoint?", atSetpoint());
        telemetry.addData("Current Position", turretMotor.getCurrentPosition());
        telemetry.addData("SetPoint", getSetpoint());
        telemetry.addData("Turret Angle", getRobotTurretAngle());
    }



}
