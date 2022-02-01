package org.firstinspires.ftc.teamcode.autotesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "CoordinateTesting")
public class CoordinateTesting extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(0,0);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(startPose);

        //Should have robot move up 40 to position (40,0)
        Trajectory testCoordinate = drive.trajectoryBuilder(startPose)
                .strafeTo(new Vector2d(40, 0))
                .build();

        Trajectory testCordNewPosition = drive.trajectoryBuilder(testCoordinate.end())
                .strafeTo(new Vector2d(40, 20))
                .build();

        drive.followTrajectory(testCoordinate);
        drive.followTrajectory(testCordNewPosition);
    }
}
