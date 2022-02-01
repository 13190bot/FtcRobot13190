package org.firstinspires.ftc.teamcode.objectDetection;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

@Disabled
@TeleOp
public class FindDuckPlace extends ConceptTensorFlowObjectDetectionWebcam {
    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first
        initVuforia();
        initTfod();

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (getTfod() != null) {
                    getTfod().activate();
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = getTfod().getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        // step through the list of recognitions and display boundary info.
                        int i = 0;
                        boolean isDuckDetected = false;
                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    recognition.getRight(), recognition.getBottom());
                            i++;

                            if (recognition.getLabel().equals("Duck")) {
                                isDuckDetected = true;
                                telemetry.addData("Object detected", "Duck");
                            } else {
                                isDuckDetected = false;
                            }
                        }
                        telemetry.update();
                    }
                }
            }
        }
    }
}
