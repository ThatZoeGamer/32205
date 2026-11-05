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

    private double setpoint = 0.0;
    public Turret(HardwareMap hardwareMap) {
        turretMotor = hardwareMap.get(DcMotorEx.class, Constants.turretConstants.turretMotorid);
        turret = new PIDController(
                Configs.turretConfigs.turretP,
                Configs.turretConfigs.turretI,
                Configs.turretConfigs.turretD);


        turretMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    //PID Methods, setpoint stuff and currentposition
    public void setSetpoint(double desiredSetpoint) {
        setpoint = desiredSetpoint;
    }
    public double getSetpoint() {
        return setpoint;
    }
    public double getPosition() {
        return turretMotor.getCurrentPosition();
    }
    public boolean atSetpoint() {
        return Math.abs(setpoint - getPosition()) <= Configs.turretConfigs.positionTolerance;
    }

    //TODO: Need to add a controller button to set setpoint to zero.
    //TODO: Unless there is a button we can use for all zeroing???
    public void zero() {
        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        setpoint = 0.0;
    }

    //idk why I added this, thought we should stop a robot cause robot should be stopped :)
    public void stop() {
        turretMotor.setPower(0.0);
    }



    public void periodic(MultipleTelemetry telemetry) {
        //Calculating where the turret is currently positioned and turning the Turret to a desired setPoint.
        double currentPosition = turretMotor.getCurrentPosition();
        double power = turret.calculate(currentPosition, setpoint);
        power = Math.max(-Configs.turretConfigs.maxSpeed, Math.min(Configs.turretConfigs.maxSpeed, power));
        turretMotor.setPower(power);
        //Telemetry
        telemetry.addData("At setpoint?", atSetpoint());
    }



}
