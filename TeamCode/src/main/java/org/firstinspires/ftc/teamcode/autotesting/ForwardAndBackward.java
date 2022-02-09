package org.firstinspires.ftc.teamcode.autotesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "ForwardAndBackward")
public class ForwardAndBackward extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(0, 0, 0);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(startPose);

        Trajectory forward = drive.trajectoryBuilder(startPose)
                .forward(55)
                .build();

        Trajectory backward = drive.trajectoryBuilder(forward.end())
                .back(55)
                .build();

        while (true) {
            drive.followTrajectory(forward);
            drive.followTrajectory(backward);
        }
    }

}
