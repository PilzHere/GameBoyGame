/**
 * 
 */
package mysko.pilzhere.gameboygame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
import mysko.pilzhere.gameboygame.entities.blocks.Block;
import mysko.pilzhere.gameboygame.entities.blocks.CoalBlock;
import mysko.pilzhere.gameboygame.entities.blocks.DirtBlock;
import mysko.pilzhere.gameboygame.entities.blocks.IronBlock;
import mysko.pilzhere.gameboygame.entities.blocks.UnbreakableBlock;
import mysko.pilzhere.gameboygame.entities.blocks.WoodBlock;
import mysko.pilzhere.gameboygame.entities.enemies.Ant;
import mysko.pilzhere.gameboygame.entities.enemies.Rat;
import mysko.pilzhere.gameboygame.entities.enemies.Spider;
import mysko.pilzhere.gameboygame.entities.resources.Mushroom;
import mysko.pilzhere.gameboygame.entities.resources.Wood;
import mysko.pilzhere.gameboygame.gui.PlayerGUI;
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
	
	public ShapeRenderer shapeRenderer;
	
	private TiledMap map01;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	private boolean renderB2DBodies = false;
	
	public Array<Entity> players = new Array<Entity>(); 
	public Array<Entity> blocks = new Array<Entity>();
	public Array<Entity> enemies = new Array<Entity>();
	public Array<Entity> placedTools = new Array<Entity>();
	public Array<Entity> resources = new Array<Entity>();
	
	public int objectsRendered;
	
	public static final int PPM = 16;
	
	private PlayerGUI playerGUI;
	
	public GameScreen(GameboyGame game) {
		this.game = game;
		
		initPhysicsWorld();
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, Gdx.graphics.getWidth() / GameScreen.PPM, Gdx.graphics.getHeight() / GameScreen.PPM);
		cam.far = 1;
		cam.position.set(new Vector2(GameboyGame.GAMEBOY_WINDOW_WIDTH / GameScreen.PPM * 8, GameboyGame.GAMEBOY_WINDOW_HEIGHT / GameScreen.PPM * 8), 0);
		cam.update();
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
		
//		Update game window size.
		Gdx.graphics.setWindowedMode(GameboyGame.GAMEBOY_WINDOW_WIDTH * GameboyGame.DEFAULT_WINDOW_SCALE, GameboyGame.GAMEBOY_WINDOW_HEIGHT * GameboyGame.DEFAULT_WINDOW_SCALE);
		
		map01 = AssetsManager.MANAGER.get(AssetsManager.MAP01);
		
		mapRenderer = new OrthogonalTiledMapRenderer(map01);
//		mapRenderer.getMap().getLayers().get(3).
		
		shapeRenderer = new ShapeRenderer();
		
		BodyDef mapWallsBodyDef = new BodyDef();
		PolygonShape mapWallsShape = new PolygonShape();
		FixtureDef mapWallsFixtureDef = new FixtureDef();
		Body mapWallsBody;
		
//		Map collisions		
		for (MapObject object : map01.getLayers().get("collision").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			mapWallsBodyDef.type = BodyDef.BodyType.StaticBody;
			mapWallsBodyDef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
			mapWallsFixtureDef.filter.categoryBits = CollisionContactListener.TERRAIN_COL;
			mapWallsFixtureDef.filter.maskBits = CollisionContactListener.TERRAIN_COLLIDES_WITH;
			mapWallsFixtureDef.friction = 0.5f;
			mapWallsBody = b2dWorld.createBody(mapWallsBodyDef);
			mapWallsShape.setAsBox(rect.getWidth() / 2, rect.height / 2);
			mapWallsFixtureDef.shape = mapWallsShape;
			mapWallsBody.createFixture(mapWallsFixtureDef);
		}
		mapWallsShape.dispose();
		
//		Place blocks
		for (MapObject object : map01.getLayers().get("blocks").getObjects()) {
			
			if (object.getName() != null && !object.getName().isEmpty()) {
				if (object.getName().equals("blockDirt")) {
					blocks.add(new DirtBlock(this, (Float)object.getProperties().get("x"), (Float)object.getProperties().get("y")));
				} else if (object.getName().equals("blockWood")) {
					blocks.add(new WoodBlock(this, (Float)object.getProperties().get("x"), (Float)object.getProperties().get("y")));
				} else if (object.getName().equals("blockCoal")) {
					blocks.add(new CoalBlock(this, (Float)object.getProperties().get("x"), (Float)object.getProperties().get("y")));
				} else if (object.getName().equals("blockIron")) {
					blocks.add(new IronBlock(this, (Float)object.getProperties().get("x"), (Float)object.getProperties().get("y")));
				} else if (object.getName().equals("blockMushroom")) {
					resources.add(new Mushroom(this, (Float)object.getProperties().get("x") + 8, (Float)object.getProperties().get("y") + 8));
				} else if (object.getName().equals("blockUnbreakable")) {
					blocks.add(new UnbreakableBlock(this, (Float)object.getProperties().get("x"), (Float)object.getProperties().get("y")));
				}
			}
		}
		
//		Update blocks after placing to check for neighbors for adjusting texture(s).
		for (Entity entity : blocks) {
			((Block) entity).checkForNeighbours();
			((Block) entity).updateSprite(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class));
		}
		
		for (MapObject object : map01.getLayers().get("enemies").getObjects()) {
			
			if (object.getName() != null && !object.getName().isEmpty()) {
				if (object.getName().equals("spider")) {
					enemies.add(new Spider(this, (Float)object.getProperties().get("x"), (Float)object.getProperties().get("y")));
				} else if (object.getName().equals("rat")) {
					enemies.add(new Rat(this, (Float)object.getProperties().get("x"), (Float)object.getProperties().get("y")));
				} else if (object.getName().equals("ant")) {
					enemies.add(new Ant(this, (Float)object.getProperties().get("x"), (Float)object.getProperties().get("y")));
				}
			}
		}
		
		players.add(new Player(this, 10 * 16, 45 * 16));
		
		playerGUI = new PlayerGUI(this, (Player) players.get(0));
	}
	
	private void initPhysicsWorld() {		
		Vector2 stdGravity = new Vector2(0, -90); // Y: -30.
		contactListener = new CollisionContactListener();
		b2dWorld = new World(new Vector2(stdGravity), true);
		b2dWorld.setContactListener(contactListener);
		
		if (GameboyGame.USING_B2D_DEBUG_RENDERER) {
			b2dDebugRenderer = new Box2DDebugRenderer();
		}
	}
	
	@Override
	public void show() {
		
	}
	
	private void handleInput(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			enemies.add(new Spider(this, 11 * 16, 44 * 16));
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
			enemies.add(new Rat(this, 11 * 16, 44 * 16));
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
			enemies.add(new Ant(this, 11 * 16, 44 * 16));
		}
		
//		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
//			Gdx.graphics.setWindowedMode(160 * 4, 144 * 4);
//		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
			resources.add(new Wood(this, 10 * 16, 45 * 16));
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
			renderB2DBodies = !renderB2DBodies;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
			for (Entity entity : blocks) {
				((Block) entity).checkForNeighbours();
				((Block) entity).updateSprite(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class));
			}
		}
	}
	
	private void tick(float delta) {
		playerGUI.tick(delta);
		
		for (Entity entity : players) {
			entity.tick(delta);
		}
		
		for (Entity entity : blocks) {
			entity.tick(delta);
		}
		
		for (Entity entity : placedTools) {
			entity.tick(delta);
		}
		
		for (Entity entity : enemies) {
			entity.tick(delta);
		}
		
		for (Entity entity : resources) {
			entity.tick(delta);
		}
		
//		moveCamera(cam);
	}
	
	private void moveCamera(OrthographicCamera camera) {
		camera.position.x = (MathUtils.round(((Player)players.get(0)).cameraPos.x * GameScreen.PPM) / GameScreen.PPM);
		camera.position.y = (MathUtils.round(((Player)players.get(0)).cameraPos.y * GameScreen.PPM) / GameScreen.PPM) - 0.81f * GameScreen.PPM;
//		Interpolation.smooth.apply(camera.position.x);
//		Interpolation.smooth.apply(camera.position.y);
	}
	
	public boolean isInFrustum(Vector3 center, Vector3 dimensions) {
		if (cam.frustum.boundsInFrustum(center, dimensions)) {
			return true;
		} else {
			return false;
		}
	}	
	
	@Override
	public void render(float delta) {
		objectsRendered = 0;
		
		handleInput(delta);
		tick(delta);

		mapRenderer.setView(cam);
		avoidMapTearingLeft(161);		
		mapRenderer.render();
		
		game.batch.setProjectionMatrix(cam.combined);
		game.batch.begin();
		
		for (Entity entity : blocks) {
			entity.render(delta);
			if (entity.render) {
				objectsRendered++;
			}
		}
		
		for (Entity entity : placedTools) {
			entity.render(delta);
			if (entity.render) {
				objectsRendered++;
			}
		}
		
		for (Entity entity : resources) {
			entity.render(delta);
			if (entity.render) {
				objectsRendered++;
			}
		}
		
		for (Entity entity : enemies) {
			entity.render(delta);
			if (entity.render) {
				objectsRendered++;
			}
		}
		
		for (Entity entity : players) {
			entity.render(delta);
			if (entity.render) {
				objectsRendered++;
			}
		}
		
		game.batch.end();
		
		if (GameboyGame.USING_B2D_DEBUG_RENDERER) {
			if (renderB2DBodies) {
				b2dDebugRenderer.render(b2dWorld, cam.combined);
			}
		}
		
//		cam.update();
		
		playerGUI.render(delta);
		
//		For debugging
		for (Entity entity : enemies) {
			entity.drawRays();
		}

//		For debugging
//		players.get(0).drawRays();
		
		tickPhysicsWorld();
		moveCamera(cam);
		cam.update(); // Fixed sometimes camera/player body jitter  @ startup(?)
	}
	
	private Vector2 tempScreenPos = new Vector2();
	private final Vector2 screenPosOffset = new Vector2(-1, 0);
	private void avoidMapTearingLeft(float newWidth) {
		mapRenderer.getViewBounds().setWidth(newWidth);
		mapRenderer.getViewBounds().setPosition(mapRenderer.getViewBounds().getPosition(tempScreenPos).add(screenPosOffset));
	}
	
	public void drawRays(Vector2 rayBegin, Vector2 rayEnd) {
		shapeRenderer.setProjectionMatrix(cam.combined);
//		System.out.println("B " + shapeRenderer.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.line(rayBegin, rayEnd);
		shapeRenderer.end();
	}
	
	private float accumulator = 0;
	private final float physTick = 1f/60f; // OLD: 1f/45f
	private final int physVelIter = 6;
	private final int physPosIter = 2;
	private void tickPhysicsWorld() {
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
		map01.dispose();
		mapRenderer.dispose();
		
		b2dDebugRenderer.dispose();
		b2dWorld.dispose();
		
		shapeRenderer.dispose();
	}

}
