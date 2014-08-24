package sk.hackcraft.spacestation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Gauge
{
	private static final Texture gaugeTexture = new Texture(Gdx.files.internal("sprite/gauges.png"));
	
	public static Gauge create(GoodsType goodsType, int gaugeAreaHeight)
	{
		int gaugeType = fromGoodsType(goodsType);
		
		return new Gauge(gaugeTexture, gaugeType, gaugeAreaHeight);
	}
	
	public interface GaugeIndex
	{
		public static final int
			METALS = 0,
			HYDROGEN = 1,
			WATER = 2,
			FERTILIZER = 3,
			GOODS = 4;
	}
	
	private static int fromGoodsType(GoodsType type)
	{
		switch (type)
		{
			case FERTILIZERS:
				return GaugeIndex.FERTILIZER;
			case GOODS:
				return GaugeIndex.GOODS;
			case HYDROGEN:
				return GaugeIndex.HYDROGEN;
			case METALS:
				return GaugeIndex.METALS;
			case WATER:
				return GaugeIndex.WATER;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	private static final int
		WIDTH = 10,
		HEIGHT = 9;
	
	private TextureRegion borderTipTop, borderTipBottom, borderCenter;
	private TextureRegion gaugeTipTop, gaugeTipBottom, gaugeCenter;

	private int max;
	
	private int gaugeAreaHeight;
	private int gaugeHeight;
	
	private ValueProvider provider;
	
	public Gauge(Texture texture, int gaugeType, int gaugeAreaHeight)
	{
		int x = WIDTH * gaugeType;
		int y = 0;

		borderTipTop = new TextureRegion(texture, x, y, WIDTH, 4);
		borderTipBottom = new TextureRegion(texture, x, y + 5, WIDTH, 4);
		borderCenter = new TextureRegion(texture, x, y + 4, WIDTH, 1);
		
		y = HEIGHT;
		gaugeTipTop = new TextureRegion(texture, x, y, WIDTH, 4);
		gaugeTipBottom = new TextureRegion(texture, x, y + 5, WIDTH, 4);
		gaugeCenter = new TextureRegion(texture, x, y + 4, WIDTH, 1);
		
		this.gaugeAreaHeight = gaugeAreaHeight;
	}
	
	public void setMax(int max)
	{
		this.max = max;
	}
	
	public void setValueProvider(ValueProvider provider)
	{
		this.provider = provider;
	}
	
	public void draw(Batch batch, float x, float y)
	{		
		batch.draw(borderTipBottom, x, y);
		batch.draw(borderCenter, x, y + 4, WIDTH, gaugeAreaHeight);
		batch.draw(borderTipTop, x, y + 4 + gaugeAreaHeight);

		if (provider != null)
		{
			float value = provider.getValue();			
			float ratio = (float)value / max;
			gaugeHeight = (int)(gaugeAreaHeight * ratio);
			
			if (value > 0)
			{
				batch.draw(gaugeTipBottom, x, y + 1);
				batch.draw(gaugeCenter, x, y + 4, WIDTH, gaugeHeight);
				batch.draw(gaugeTipTop, x, y + 3 + gaugeHeight);
			}
		}
	}
	
	public interface ValueProvider
	{
		float getValue();
	}
}
