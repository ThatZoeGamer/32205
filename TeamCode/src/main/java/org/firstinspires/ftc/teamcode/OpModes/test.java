package org.firstinspires.ftc.teamcode.OpModes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Subsystems.ColourSensor;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.Limelight;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;

@TeleOp(name="test", group="Teleop")
public class test extends LinearOpMode{
    Shooter s_shooter;
    MultipleTelemetry m_telemetry;
    GamepadEx gamepad;

    @Override
    public void runOpMode() {
        gamepad = new GamepadEx(gamepad1);
        m_telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        s_shooter = new Shooter(hardwareMap, m_telemetry);
//        s_shooter.getLeftMotor().getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        while (opModeIsActive()) {
//            s_shooter.getLeftMotor().setVelocityPIDFCoefficients(Configs.shooterConfigs.shooterP,Configs.shooterConfigs.shooterI,Configs.shooterConfigs.shooterD,Configs.shooterConfigs.shooterKv);
//            s_shooter.getLeftMotor().setVelocity(gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)*100);
//            s_shooter.getLeftMotor().setVelocity(Configs.shooterConfigs.manualShooterVelocity);
//            m_telemetry.addData("left trigger", gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER));
            m_telemetry.addData("pid", s_shooter.getLeftMotor().getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
            m_telemetry.addData("ticks", s_shooter.getLeftMotor().getVelocity());
//            m_telemetry.addData("power", s_shooter.getLeftMotor().getPower());
            m_telemetry.update();
        }
    }
}
