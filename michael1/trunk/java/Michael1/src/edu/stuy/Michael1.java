/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.stuy;


import edu.wpi.first.wpilibj.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Michael1 extends SimpleRobot {

    Joystick left_stick;
    Joystick right_stick;
    Joystick shooter_stick;
    Joystick gamepad;
    RobotDrive drivetrain;
    Victor intake;
    Victor shooter;
    Servo paddle;

    /* USB PORTS */
    final int LEFT_STICK_PORT = 1;
    final int RIGHT_STICK_PORT = 2;
    final int SHOOTER_STICK_PORT = 3;
    final int GAMEPAD_PORT = 4;

    /* PWM OUT */
    final int LEFT_DRIVE_CHANNEL = 1;
    final int RIGHT_DRIVE_CHANNEL = 2;
    final int SHOOTER_CHANNEL = 3;
    final int INTAKE_CHANNEL = 4;
    final int PADDLE_CHANNEL = 9;

    // Set this to true if using the gamepad rather than joysticks for driving.
    // The gamepad still functions as an alternative to the shooter stick regardless of this setting.
    final boolean USE_GAMEPAD = true;

    public Michael1() {
        left_stick = new Joystick(LEFT_STICK_PORT);
        right_stick = new Joystick(RIGHT_STICK_PORT);
        shooter_stick = new Joystick(SHOOTER_STICK_PORT);
        gamepad = new Joystick(GAMEPAD_PORT);

        drivetrain = new RobotDrive(LEFT_DRIVE_CHANNEL, RIGHT_DRIVE_CHANNEL);
        shooter = new Victor(SHOOTER_CHANNEL);
        intake = new Victor(INTAKE_CHANNEL);
        paddle = new Servo(PADDLE_CHANNEL);

        // Set these to true if the drive motors run backwards.
        drivetrain.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drivetrain.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drivetrain.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drivetrain.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);

        System.out.println("We're alive!");
        if (USE_GAMEPAD == true) {
            System.out.println("Using gamepad control.");
        }
        else {
            System.out.println("Using joystick control.");
        }
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        drivetrain.tankDrive(1,1);
        
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        getWatchdog().setEnabled(false);

        boolean shooter_status = false;
        boolean intake_status = false;

        while (isOperatorControl() && isEnabled()) {
            if (USE_GAMEPAD == true) {
                drivetrain.tankDrive(gamepad, 2, gamepad, 4);
            }
            else {
                drivetrain.tankDrive(left_stick, right_stick);
            }

            /* Shooter control */
            if (shooter_stick.getTrigger() || gamepad.getRawButton(8)) {
                shooter_status = true;
                shooter.set(-0.75);
                intake.set(1);
            }
            else {
                shooter_status = false;
                if (intake_status == false) {
                    shooter.set(0);
                    intake.set(0);
                }
            }

            /* Intake control */
            if (shooter_stick.getRawButton(2) || gamepad.getRawButton(7)) {
                intake_status = true;
                intake.set(1);
                shooter.set(0.75);
            }
            else if (shooter_stick.getRawButton(8) || shooter_stick.getRawButton(11) || gamepad.getRawButton(5)) {
                intake_status = true;
                intake.set(-1);
            }
            else {
                intake_status = false;
                if (shooter_status == false) {
                    intake.set(0);
                    shooter.set(0);
                }
            }

            /* Paddle control */
            if (shooter_stick.getRawButton(7) || shooter_stick.getRawButton(10) || gamepad.getRawButton(2)) {
                paddle.set(0);
            }
            else {
                paddle.set(1);
            }
        }
    }
}