import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;

public class SoundCheck extends Thread
{
	private static final int	BUFFER_SIZE = 40960;

	private TargetDataLine	inputLine;
	private SourceDataLine	outputLine;
	private boolean		isRecording;

	/*
	 *	AudioFormat tells the format in which the data
	 *	is recorded/played
	 */
	public SoundCheck(AudioFormat format,
			 int bufferSize) throws LineUnavailableException{
	
		/*
		 *	Get the input/output lines
		 */
		DataLine.Info	targetInfo = new DataLine.Info(TargetDataLine.class, format,
				bufferSize);
		DataLine.Info	sourceInfo = new DataLine.Info(SourceDataLine.class, format,
				bufferSize);

		inputLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
		outputLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
		/*
		 *  Open the input/output lines
		 */
		inputLine.open(format, bufferSize);
		outputLine.open(format, bufferSize);
	}

	public void start()
	{
	    inputLine.start();
		outputLine.start();

		super.start();
	}

	public void run()
	{
		byte[]	myBuffer = new byte[BUFFER_SIZE];
		isRecording = true;
		while (isRecording)
		{
			/*
			 *	read data from input line
			 */
			int	numBytesCaptured = inputLine.read(myBuffer, 0, myBuffer.length);

			/*
			 *	write data to output line
			 */
			outputLine.write(myBuffer, 0, numBytesCaptured);
		}
	}

	public static void main(String[] args)
	{
		float frameRate = (float) 44100.0;
		
		AudioFormat	audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
			frameRate, 16, 2, 4, frameRate, false);
		SoundCheck myAudioLoop = null;
		try
		{
			myAudioLoop = new SoundCheck(audioFormat,
						  BUFFER_SIZE);
		}
		catch (LineUnavailableException e)
		{
			System.exit(1);
		}
		myAudioLoop.start();
	}
}

