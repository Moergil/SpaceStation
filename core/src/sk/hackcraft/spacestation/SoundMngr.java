package sk.hackcraft.spacestation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Timer;

public class SoundMngr
{
	public enum MusicType
	{
		INTRO(0),
		MUSIC1(1),
		MUSIC2(2);
		
		private int value;
		
		private MusicType(int value)
		{
			this.value = value;
		}
		
	}
	
	MusicType nextMusic;
	
	private Music mp3Intro;
	
	private Music mp3Music1;
	
	private Music mp3Music2;
	
	private Timer timerMusic;
	
	private boolean locker;
	
	public SoundMngr()
	{
	    nextMusic = MusicType.MUSIC1;
		
		mp3Intro = Gdx.audio.newMusic(Gdx.files.internal("sounds/intro.mp3"));
		
		mp3Music1 = Gdx.audio.newMusic(Gdx.files.internal("sounds/music1.mp3"));
		
		mp3Music2 = Gdx.audio.newMusic(Gdx.files.internal("sounds/music2.mp3"));
		
		locker = false;
		
		
	}

	public void runIntro()
	{
		mp3Intro.play();
	}
	
	public void stopIntro()
	{
		mp3Intro.stop();
	}
	
	public void runMusicGame()
	{			
		timerMusic = new Timer();
		timerMusic.scheduleTask(new Timer.Task()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub				
				if(true == locker)
				{
					if(!((mp3Intro.isPlaying()) || (mp3Music1.isPlaying()) || (mp3Music2.isPlaying())))
					{
						locker = false;
					}
				}
				else
				{
					switch (nextMusic)
					{

						case INTRO:
							nextMusic = MusicType.MUSIC1;
							mp3Intro.play();
							break;
						case MUSIC1:
							nextMusic = MusicType.MUSIC2;
							mp3Music1.setVolume(0.5f);
							mp3Music1.play();
							
							break;						
						case MUSIC2:
							nextMusic = MusicType.INTRO;
							mp3Music2.play();
							break;
						default:
							return;
					}
					
					locker = true;
				}
			}
		},0,5);
		
	}
}
