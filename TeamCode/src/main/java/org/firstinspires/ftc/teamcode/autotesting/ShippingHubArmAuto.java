package org.firstinspires.ftc.teamcode.autotesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "ShippingHubArmAuto")
public class ShippingHubArmAuto extends LinearOpMode {

    public DcMotor armRotationMotor;
    public DcMotor intakeMotor;
    public Servo directionServo;

    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(0, 0, 0);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        armRotationMotor = hardwareMap.get(DcMotor.class, "rotationMotor");
        armRotationMotor.setDirection(DcMotor.Direction.REVERSE);
        armRotationMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        directionServo = hardwareMap.get(Servo.class, "directionServo");

        drive.setPoseEstimate(startPose);

        Trajectory shippingHub = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(40, -10, 108))
                .build();

        drive.followTrajectory(shippingHub);
        //Arm moving up
        armRotationMotor.setTargetPosition(armRotationMotor.getCurrentPosition()+190*7);
        armRotationMotor.setPower(0.5);
        while(armRotationMotor.isBusy()){
            continue;
        }
        armRotationMotor.setPower(0);
        //Arm box thing getting in position
        directionServo.setPosition(.50);
        //Intake or output stuff
        intakeMotor.setPower(-1);
        sleep(2000);
        intakeMotor.setPower(0);


    }
}
