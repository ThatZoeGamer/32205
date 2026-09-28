package org.firstinspires.ftc.teamcode.OpModes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Delta", group="Auto")
public class Autonomous extends LinearOpMode {

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