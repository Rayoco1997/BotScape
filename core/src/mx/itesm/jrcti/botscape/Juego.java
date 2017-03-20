package mx.itesm.jrcti.botscape;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Juego extends Game {
	SpriteBatch batch;
	private final AssetManager assetManager;

	public Juego(){
		assetManager = new AssetManager();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	@Override
	public void create () {
		setScreen(new PantallaSplashTec(this));
	}

	/*@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}*/
	
	@Override
	public void dispose () {
		super.dispose();
		assetManager.clear();
	}
}
