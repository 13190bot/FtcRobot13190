package org.firstinspires.ftc.teamcode.AutoRegionals;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Disabled
@Autonomous(name = "ParkRegional")
public class ParkRegional extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(startPose);

        Trajectory park = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(0, -80, Math.toRadians(-109)))
                .build();

        drive.followTrajectory(park);
    }
}
