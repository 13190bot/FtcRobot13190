package org.firstinspires.ftc.teamcode.clawstuff;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

@Disabled
@TeleOp
public class ClawTeleOp extends template {
    public TouchSensor limit;
    @Override
    public void runOpMode() {

        initialize();
        limit = hardwareMap.get(TouchSensor.class, "limit");
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        controlMotorPower();
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("rotationPosition", (armRotationMotor.getCurrentPosition()*360)/(COUNTS_PER_MOTOR_REV*GEAR_CHANGE));
            telemetry.addData("intakeMotorPower", intakeMotor.getPower());
            telemetry.addData("limitSwitch", limit.isPressed());
            telemetry.addData("servoPos", directionServo.getPosition());
            telemetry.addData("armPower", armRotationMotor.getPower());
            if(limit.isPressed()){
                rotationPower = 0;
                telemetry.addData("ERROR: ", "too far");
            }
            if(gamepad2.y && !isRotationBusy()){
                toTopLevel();
            }
            if(gamepad2.b && !isRotationBusy()){
                toMiddleLevel();
            }
            if(gamepad2.a && !isRotationBusy()){
                toBottomLevel();
            }
            if(gamepad2.x && !isRotationBusy()){
                toPickupPosition();
            }
            if(gamepad2.right_trigger>0.2){
                intakeMotor.setPower(1);
            }
            if(gamepad2.left_trigger>0.2){
                intakeMotor.setPower(-1);
            }
            if(gamepad2.right_trigger<=0.2 && intakeMotor.getPower() > 0){
                intakeMotor.setPower(0);
            }
            if(gamepad2.left_trigger<=0.2 && intakeMotor.getPower() < 0){
                intakeMotor.setPower(0);
            }
            double x = gamepad2.left_stick_x;
            if((x>0.2) && !isRotationBusy()){
                manual = true;
                rotationPower = 1;
            }else if((x<-0.2)&&!isRotationBusy()){
                manual = true;
                rotationPower = -1;
            }else{
                manual = false;
                if(!isRotationBusy()){
                    rotationPower = 0;
                }
            }
            if(gamepad2.left_bumper){
                armRotationMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                armRotationMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            double servoInput = gamepad2.right_stick_x;
            if(servoInput>0.2){
                directionServo.setPosition(directionServo.getPosition()+0.01);
            }
            if(servoInput<-0.2){
                directionServo.setPosition(directionServo.getPosition()-0.01);
            }
            if(gamepad2.dpad_down){
                armRotationMotor.setTargetPosition(armRotationMotor.getCurrentPosition());
            }
            telemetry.update();
        }
    }

}

