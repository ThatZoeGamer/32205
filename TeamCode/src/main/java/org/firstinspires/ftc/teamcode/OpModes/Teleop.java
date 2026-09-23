package org.firstinspires.ftc.teamcode.OpModes;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;

@TeleOp(name="Delta", group="Teleop")
public class Teleop extends LinearOpMode {

    GamepadEx gamepad;

    boolean aPressed;
    boolean xPressed;

    Drivetrain s_drivetrain;

    @Override
    public void runOpMode() {

        gamepad = new GamepadEx(gamepad1);

        s_drivetrain = new Drivetrain(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {

            s_drivetrain.runMoter();

//            s_drivetrain.drive(
//                    gamepad.getLeftY(),
//                    -gamepad.getLeftX(),
//                    gamepad.getRightX()
//            );

//            aPressed = gamepad.isDown(GamepadKeys.Button.A);
//            xPressed = gamepad.isDown(GamepadKeys.Button.X);

//            gamepad.readButtons();

            if (xPressed) {
                s_drivetrain.resetYaw();
            }

            s_drivetrain.periodic(telemetry);
            telemetry.addData("A button: ", aPressed);
            telemetry.update();
        }
    }
}