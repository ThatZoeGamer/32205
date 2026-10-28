package org.firstinspires.ftc.teamcode.OpModes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.teamcode.Configs;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;

@TeleOp(name="Delta", group="Teleop")
public class Teleop extends LinearOpMode {

    GamepadEx gamepad;
    boolean aPressed;
    boolean xPressed;
    boolean yPressed;
    boolean lbPressed;
    boolean rbPressed;
    boolean lDpadPressed;
    boolean rDpadPressed;

    Timing.Timer indexTimer;

    Drivetrain s_drivetrain;
    Intake s_intake;
    Shooter s_shooter;
    MultipleTelemetry m_telemetry;
    Odometry o_odometry;

    @Override
    public void runOpMode() {
        gamepad = new GamepadEx(gamepad1);

        s_drivetrain = new Drivetrain(hardwareMap, m_telemetry);
        s_intake = new Intake(hardwareMap, m_telemetry);
        s_shooter = new Shooter(hardwareMap, m_telemetry);
        m_telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        o_odometry = new Odometry(m_telemetry, hardwareMap);

        waitForStart();

        while (opModeIsActive()) {

            s_drivetrain.drive(
                    gamepad.getLeftY(),
                    -gamepad.getLeftX(),
                    gamepad.getRightX()
            );

            if (gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0 && gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) == 0) {
                s_intake.runIntake(gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER));
//                s_intake.runOuttake(0);
            }
            else if (gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) == 0 && gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0) {
                s_intake.runOuttake(gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER));
//                s_intake.runIntake(0);
            }
            else {
                s_intake.stop();
            }
//            if (s_shooter.getCurrent()>Configs.shooterConfigs.pollenCurrentThresh) {
//
//
//
//            }

            aPressed = gamepad.isDown(GamepadKeys.Button.A);
            xPressed = gamepad.isDown(GamepadKeys.Button.X);
            yPressed = gamepad.isDown(GamepadKeys.Button.Y);
            lDpadPressed = gamepad.wasJustPressed(GamepadKeys.Button.DPAD_LEFT);
            rDpadPressed = gamepad.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT);
            lbPressed = gamepad.isDown(GamepadKeys.Button.LEFT_BUMPER);
            rbPressed = gamepad.isDown(GamepadKeys.Button.RIGHT_BUMPER);

            gamepad.readButtons();

            if (lDpadPressed) {
                Configs.shooterConfigs.manualShooterSpeed -= 0.1;
            } else if (rDpadPressed) {
                Configs.shooterConfigs.manualShooterSpeed += 0.1;
            } else if (yPressed) {
                Configs.shooterConfigs.manualShooterSpeed = 0;
            }

            if (xPressed) { 
                o_odometry.resetHeading();
            }

            if (aPressed) {
                o_odometry.resetPosition();
            }

            s_drivetrain.periodic();
            s_intake.periodic(m_telemetry);
            o_odometry.periodic();
            s_shooter.periodic(m_telemetry);
            m_telemetry.update();
        }
    }
}