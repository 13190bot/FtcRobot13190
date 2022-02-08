package org.firstinspires.ftc.teamcode.autotesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


@Autonomous(name = "ArmAutoTest")


public class ArmAutoTest extends LinearOpMode {

    public DcMotor armRotationMotor;
    public DcMotor intakeMotor;
    public Servo directionServo;

    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(0, 0, 0);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        armRotationMotor = hardwareMap.get(DcMotor.class, "rotationMotor");
        armRotationMotor.setDirection(DcMotor.Direction.REVERSE);
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        directionServo = hardwareMap.get(Servo.class, "directionServo");



        drive.setPoseEstimate(startPose);

        armRotationMotor.setTargetPosition(1);
        sleep(1000);
        armRotationMotor.setTargetPosition(0);




    }
}
