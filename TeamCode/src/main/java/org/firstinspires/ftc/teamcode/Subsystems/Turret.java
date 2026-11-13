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

public class Turret {
    private final DcMotorEx turretMotor;
    private final MultipleTelemetry m_telemtry;

    public Turret(HardwareMap hardwareMap, MultipleTelemetry telemetry) {
        turretMotor = hardwareMap.get(DcMotorEx.class, Constants.turretConstants.turretMotorId);
        m_telemtry = telemetry;

//        turretMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void turnLeft() {
        turretMotor.setPower(0.5);
    }
    public void turnRight() {
        turretMotor.setPower(0.5);
    }
    public void zero() {
        turretMotor.setPower(0);
    }
}
