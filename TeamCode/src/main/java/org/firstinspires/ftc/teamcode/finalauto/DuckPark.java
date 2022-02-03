package org.firstinspires.ftc.teamcode.finalauto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "DuckTest")
public class DuckPark extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(startPose);

        Trajectory duck = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(0, 30, Math.toRadians(135)))
                .build();

        drive.followTrajectory(duck);
    }
}
