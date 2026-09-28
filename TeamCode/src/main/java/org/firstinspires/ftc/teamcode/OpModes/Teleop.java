package org.firstinspires.ftc.teamcode.OpModes;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry;

@TeleOp(name="Delta", group="Teleop")
public class Teleop extends LinearOpMode {

    GamepadEx gamepad;
    boolean aPressed;
    boolean xPressed;

    Drivetrain s_drivetrain;
    MultipleTelemetry m_telemetry;
    Odometry o_odometry;

    @Override
    public void runOpMode() {
        gamepad = new GamepadEx(gamepad1);

        s_drivetrain = new Drivetrain(hardwareMap);
        m_telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        o_odometry = new Odometry(m_telemetry, hardwareMap);

        waitForStart();

        while (opModeIsActive()) {

            s_drivetrain.drive(
                    gamepad.getLeftY(),
                    -gamepad.getLeftX(),
                    gamepad.getRightX()
            );

            aPressed = gamepad.isDown(GamepadKeys.Button.A);
            xPressed = gamepad.isDown(GamepadKeys.Button.X);

            gamepad.readButtons();

            if (xPressed) { 
                s_drivetrain.resetYaw();
            }

            if (aPressed) {
                o_odometry.resetPosition();
            }

            s_drivetrain.periodic(telemetry);
            o_odometry.periodic(telemetry);
            m_telemetry.update();


        }
    }
}