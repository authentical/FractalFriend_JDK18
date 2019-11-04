FractalFriend_JDK18
--------------------
This is a JDK 1.8 version of FractalFriend which has proven to be more reliable when deploying to other users systems.

**Build**
mvn install

**Run**
java -jar FractalFriend_JDK1.8-1.0-SNAPSHOT.jar


**Feature Requests**
Implement Randomness checkbox which will allow removal of randomness during getNextIterationAngle
and getNextSegmentLength.

Users had some difficulty determining which control changed which number in UI. Need to add spacing or dividers or
rearrange.

Would really rather each line be drawn on canvas before calculation for the next segment is made. -> Try putting
makeFractal call in thread pool.

Implement Save

Implement Auto-Clear canvas when drawing again (AS OPTIONAL)

Implement auto-resize of canvas when user changes window size.

Todo endOfLastSegment should be equal to 1/2 the canvas size

getNextColour numbers should be performed after user changes iterations or ColorPickers?

Auto-maximize

Auto-detecting maximum canvas size and use that canvas size
