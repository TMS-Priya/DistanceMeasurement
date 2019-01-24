# DistanceMeasurement
Find the walking distance and no of steps taken by user  also calculate calories burn.


1.sensor manager to get default TYPE_ACCELEROMETER 

to measure the no of steps taken.

2.find the velocity based on accelaration in x,y,z axis

3.check the velocity > previousvelocity && velocity>threshold

&&(timeNs - lastStepTimeNs > STEP_DELAY_NS) if this condition 
true increase the step count.

based on the step count calculate the distance and calories burn.
