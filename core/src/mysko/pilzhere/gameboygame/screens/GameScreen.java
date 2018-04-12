/**
 * 
 */
package mysko.pilzhere.gameboygame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import mysko.pilzhere.gameboygame.GameboyGame;
import mysko.pilzhere.gameboygame.assets.AssetsManager;
import mysko.pilzhere.gameboygame.entities.Entity;
import mysko.pilzhere.gameboygame.entities.Player;
import mysko.pilzhere.gameboygame.entities.blocks.DirtBlock;
import mysko.pilzhere.gameboygame.physics.CollisionContactListener;

/**
 * @author PilzHere
 *
 */
public class GameScreen implements Screen {
	public GameboyGame game;
	
	public World b2dWorld;
	public Box2DDebugRenderer b2dDebugRenderer;
	public CollisionContactListener contactListener;
	
	private OrthographicCamera cam;
	private Viewport viewport;
	
	private TiledMap map01;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	private boolean dbgBodies = false;
	
	public Array<Entity> entities = new Array<Entity>();
	
	public final int PPM = 16;
	
	public final short PLAYER_COL = 0x1;
	public final short TERRAIN_COL = 0x1 << 1;
	public final short BLOCK_COL = 0x1 << 2;
	
	public final short PLAYER_COLLIDES_WITH = (short) TERRAIN_COL | BLOCK_COL;
	public final short TERRAIN_COLLIDES_WITH = (short) PLAYER_COL;
	public final short BLOCK_COLLIDES_WITH = (short) PLAYER_COL;
	
	Player testE;
	
	public GameScreen(GameboyGame game) {
		this.game = game;
		
		initPhysics();
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM);
		cam.far = 1;
		cam.position.set(new Vector2(160 / PPM * 8, 144 / PPM * 8), 0);
		cam.update();
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
		
		map01 = AssetsManager.MANAGER.get(AssetsManager.MAP01);
		mapRenderer = new OrthogonalTiledMapRenderer(map01);
		
		BodyDef mapWallsBodyDef = new BodyDef();
		PolygonShape mapWallsShape = new PolygonShape();
		FixtureDef mapWallsFixtureDef = new FixtureDef();
		Body mapWallsBody;
		
//		Wall collisions
		int offset = 1;
		for (MapObject object : map01.getLayers().get("collision").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			mapWallsBodyDef.type = BodyDef.BodyType.StaticBody;
			mapWallsBodyDef.position.set(rect.getX() + offset + rect.getWidth() / 2, rect.getY() - offset + rect.getHeight() / 2);
			mapWallsFixtureDef.filter.categoryBits = TERRAIN_COL;
			mapWallsFixtureDef.filter.maskBits = TERRAIN_COLLIDES_WITH;
			mapWallsBody = b2dWorld.createBody(mapWallsBodyDef);
			mapWallsShape.setAsBox(rect.getWidth() / 2, rect.height / 2);
			mapWallsFixtureDef.shape = mapWallsShape;
			mapWallsBody.createFixture(mapWallsFixtureDef);
//			mapBody.setUserData("Wall");
		}
		mapWallsShape.dispose();
		
//		BodyDef mapWallsBodyDef2 = new BodyDef();
//		PolygonShape mapWallsShape2 = new PolygonShape();
//		FixtureDef mapWallsFixtureDef2 = new FixtureDef();
//		Body mapWallsBody2;
		
//		Wall collisions
		int offset2 = 1;
		for (MapObject object : map01.getLayers().get("blocks").getObjects()) {
//			Rectangle rect = ((RectangleMapObject) object).getRectangle();
//			mapWallsBodyDef2.type = BodyDef.BodyType.StaticBody;
//			mapWallsBodyDef2.position.set(rect.getX() + offset2 + rect.getWidth() / 2, rect.getY() - offset2 + rect.getHeight() / 2);
//			mapWallsFixtureDef2.filter.categoryBits = TERRAIN_COL;
//			mapWallsFixtureDef2.filter.maskBits = TERRAIN_COLLIDES_WITH;
//			mapWallsBody2 = b2dWorld.createBody(mapWallsBodyDef2);
//			mapWallsShape2.setAsBox(rect.getWidth() / 2, rect.height / 2);
//			mapWallsFixtureDef2.shape = mapWallsShape2;
//			mapWallsBody2.createFixture(mapWallsFixtureDef2);
			
			if (object.getName() != null) {
				if (object.getName().equals("blockDirt")) {
//					System.err.println(object.getName());
//					float tempX = (Float) object.getProperties().get("x");
					entities.add(new DirtBlock(this, (Float)object.getProperties().get("x"), (Float)object.getProperties().get("y")));
//					object.getProperties().
					
				}
			}
			
//			mapBody.setUserData("Wall");
		}
//		mapWallsShape2.dispose();
		
		for (Entity entity : entities) {
			((DirtBlock) entity).checkForNeighbours();
			((DirtBlock) entity).updateSprite(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class));
		}
		
		testE = new Player(this);
	}
	
	private void initPhysics() {
		Box2D.init();
		
		Vector2 stdGravity = new Vector2(0, -90); // Y: -30.
		contactListener = new CollisionContactListener();
		b2dWorld = new World(new Vector2(stdGravity), true);
		b2dWorld.setContactListener(contactListener);
		
		b2dDebugRenderer = new Box2DDebugRenderer();
	}
	
	@Override
	public void show() {
		
	}
	
	private void handleInput(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
			dbgBodies = !dbgBodies;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
			for (Entity entity : entities) {
				((DirtBlock) entity).checkForNeighbours();
				((DirtBlock) entity).updateSprite(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class));
			}
		}
	}
	
	private void tick(float delta) {
		testE.tick(delta);
		
		for (Entity entity : entities) {
			entity.tick(delta);
		}
		
//		moveCamera(cam, 0.15f);
		
		cam.position.x = (MathUtils.round(testE.cameraPos.x * PPM) / PPM);
		cam.position.y = (MathUtils.round(testE.cameraPos.y * PPM) / PPM) - 0.75f * PPM;
//		cam.position.lerp(new Vector3((MathUtils.round(testE.cameraPos.x * PPM) / PPM), (MathUtils.round(testE.cameraPos.y * PPM) / PPM) - 0.75f * PPM, 0), 2.4f/PPM);
	}
	
//	private void moveCamera(OrthographicCamera camera, float speed) {
//		float ispeed = 1.0f - speed;
//		cam.position.scl(ispeed);
//		Vector2 target = testE.cameraPos.scl(speed);
//		cam.position.add(new Vector3(target.x, target.y - 1.8f, 0));
//	}
	
	public boolean isInFrustum(Vector3 center, Vector3 dimensions) {
		if (cam.frustum.boundsInFrustum(center, dimensions)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void render(float delta) {
		handleInput(delta);
		tick(delta);

		mapRenderer.setView(cam);
		mapRenderer.render();
		
		game.batch.setProjectionMatrix(cam.combined);
		game.batch.begin();
		
		for (Entity entity : entities) {
			entity.render(delta);
		}
		
		testE.render(delta);
		
		game.batch.end();
		
		if (dbgBodies)
			b2dDebugRenderer.render(b2dWorld, cam.combined);
		
		cam.update();
		
		tickPhysicsCollisions();
	}
	
	private float accumulator = 0;
	private final float physTick = 1f/60f; // OLD: 1f/45f
	private final int physVelIter = 6;
	private final int physPosIter = 2;
	private void tickPhysicsCollisions() {
		float frameTime = Math.min(Gdx.graphics.getDeltaTime(), 0.25f);
		accumulator += frameTime;
		while (accumulator >= physTick) {
			b2dWorld.step(physTick, physVelIter, physPosIter);
			accumulator -= physTick;
		}
	}

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width;
		cam.viewportHeight = height;		
		viewport.update(width, height);
        cam.update();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		mapRenderer.dispose();
		map01.dispose();
	}

}
