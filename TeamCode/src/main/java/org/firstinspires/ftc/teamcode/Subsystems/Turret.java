package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Configs.TurretConfigs;
import org.firstinspires.ftc.teamcode.Constants.TurretConstants;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.Limelight;
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

    Limelight s_limelight;
    MultipleTelemetry m_telemetry;

    public Turret(HardwareMap hardwareMap) {
        s_limelight = new Limelight(hardwareMap, m_telemetry);
        turretMotor = hardwareMap.get(DcMotorEx.class, TurretConstants.turretMotorId);
        turret = new PIDController(
                TurretConfigs.turretP,
                TurretConfigs.turretI,
                TurretConfigs.turretD);
        turretMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setSetpoint(double desiredSetpoint) {
        TurretConfigs.setpoint = Math.max(Math.min(desiredSetpoint, TurretConstants.maxAngle), TurretConstants.minAngle);
    }
    public double getSetpoint() {
        return TurretConfigs.setpoint;
    }
    public double getPosition() {
        return turretMotor.getCurrentPosition();
    }
    public boolean atSetpoint() {
        return Math.abs(TurretConfigs.setpoint - getRobotTurretAngle()) <= TurretConfigs.positionTolerance;
    }

    public void zero() {
        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TurretConfigs.setpoint = 0.0;
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //idk why I added this, thought we should stop a robot cause robot should be stopped :)
    public void stop() {
        turretMotor.setPower(0.0);
    }

    public double getRobotTurretAngle() {
        double rotations = getPosition()/(24*3.7*13.57);
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
//        int power = turret.calculate(currentPosition, Configs.turretConfigs.setpoint);
//        power = Math.max(-Configs.turretConfigs.maxSpeed, Math.min(Configs.turretConfigs.maxSpeed, power));
//        turretMotor.setTargetPosition((int) turret.calculate(currentPosition, getSetpoint()));
        setTurretPosition(getSetpoint());
        if(telemetry != null) {
            telemetry.addData("T :----------TURRET----------", "T");
            telemetry.addData("T: At setpoint?", atSetpoint());
            telemetry.addData("T: Current Position", turretMotor.getCurrentPosition());
            telemetry.addData("T: SetPoint", getSetpoint());
            telemetry.addData("T: Power Turret", turretMotor.getPower());
            telemetry.addData("T: Turret Angle", getRobotTurretAngle());
        }
    }



}
