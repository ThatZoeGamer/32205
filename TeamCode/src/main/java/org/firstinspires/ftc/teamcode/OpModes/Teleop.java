package org.firstinspires.ftc.teamcode.OpModes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;

@TeleOp(name="Delta", group="Teleop")
public class Teleop extends LinearOpMode {

    GamepadEx gamepad;
    boolean aPressed;
    boolean xPressed;
    double intakeRun;
    double outtakeRun;

    Drivetrain s_drivetrain;

    Intake s_intake;
    MultipleTelemetry m_telemetry;
    Odometry o_odometry;

    @Override
    public void runOpMode() {
        gamepad = new GamepadEx(gamepad1);

        s_drivetrain = new Drivetrain(hardwareMap, m_telemetry);
        s_intake = new Intake(hardwareMap, m_telemetry);
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
                s_intake.runOuttake(0);
            } else if (gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) == 0 && gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0) {
                s_intake.runOuttake(gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER));
                s_intake.runIntake(0);
            } else {
                s_intake.stop();
            }

            aPressed = gamepad.isDown(GamepadKeys.Button.A);
            xPressed = gamepad.isDown(GamepadKeys.Button.X);

            gamepad.readButtons();

            if (xPressed) { 
                o_odometry.resetHeading();
            }

            if (aPressed) {
                o_odometry.resetPosition();
            }

            s_drivetrain.periodic();
            s_intake.periodic();
            o_odometry.periodic();
            m_telemetry.update();
        }
    }
}