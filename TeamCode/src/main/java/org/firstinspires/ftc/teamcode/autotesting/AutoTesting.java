package org.firstinspires.ftc.teamcode.autotesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "AutoTest")
public class AutoTesting extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(0,0,0);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(startPose);

        //Should have robot move up 20 inches
        Trajectory moveForward = drive.trajectoryBuilder(startPose)
                .forward(20)
                .build();

        //Should have robot move back to starting position
        Trajectory strafeToStart = drive.trajectoryBuilder(moveForward.end())
                .strafeTo(new Vector2d(0,0))
                .build();

        drive.followTrajectory(moveForward);
        drive.followTrajectory(strafeToStart);
    }
}
