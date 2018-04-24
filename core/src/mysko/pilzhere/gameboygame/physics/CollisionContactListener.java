/**
 * 
 */
package mysko.pilzhere.gameboygame.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import mysko.pilzhere.gameboygame.entities.Player;
import mysko.pilzhere.gameboygame.entities.enemies.Ant;
import mysko.pilzhere.gameboygame.entities.enemies.Rat;
import mysko.pilzhere.gameboygame.entities.enemies.Spider;

/**
 * @author PilzHere
 *
 */
public class CollisionContactListener implements ContactListener {

	// Collision category bits.
	public static final short PLAYER_COL = 0x1;
	public static final short TERRAIN_COL = 0x1 << 1;
	public static final short BLOCK_COL = 0x1 << 2;
	public static final short ENEMY_COL = 0x1 << 3;
	public static final short LADDER_COL = 0x1 << 4;
	public static final short RESOURCE_COL = 0x1 << 5;
	public static final short EXIT_COL = 0x1 << 5;

	// Collision mask bits.
	public static final short PLAYER_COLLIDES_WITH = (short) TERRAIN_COL | BLOCK_COL | ENEMY_COL | LADDER_COL
			| RESOURCE_COL;
	public static final short TERRAIN_COLLIDES_WITH = (short) PLAYER_COL | ENEMY_COL | RESOURCE_COL;
	public static final short BLOCK_COLLIDES_WITH = (short) PLAYER_COL | ENEMY_COL | RESOURCE_COL;
	public static final short ENEMY_COLLIDES_WITH = (short) PLAYER_COL | BLOCK_COL | TERRAIN_COL;
	public static final short LADDER_COLLIDES_WITH = (short) PLAYER_COL;
	public static final short RESOURCE_COLLIDES_WITH = (short) PLAYER_COL | TERRAIN_COL | BLOCK_COL;
	public static final short EXIT_COLLIDES_WITH = (short) PLAYER_COL;

	@Override
	public void beginContact(Contact contact) {
		if (contact.getFixtureA().getFilterData().categoryBits == CollisionContactListener.PLAYER_COL) {
			((Player) contact.getFixtureA().getBody().getUserData()).onCollisionBegin(contact.getFixtureB());
			// System.err.println("CollisionBEGIN_A");
		}

		// Fetch entity from Enemies.
		if (contact.getFixtureA().getFilterData().categoryBits == CollisionContactListener.ENEMY_COL) {
			// System.err.println("CollisionBEGIN_A");
		}

		if (contact.getFixtureB().getFilterData().categoryBits == CollisionContactListener.PLAYER_COL) {
			((Player) contact.getFixtureB().getBody().getUserData()).onCollisionBegin(contact.getFixtureA());
			// System.err.println("CollisionBEGIN_B");
		}

		// Fetch entity from Enemies.
		if (contact.getFixtureB().getFilterData().categoryBits == CollisionContactListener.ENEMY_COL) {
			// System.err.println("CollisionBEGIN_B");
		}

		// Fetch entity from Enemies.
		// if (contact.getFixtureA().getFilterData().categoryBits ==
		// CollisionContactListener.RESOURCE_COL) {
		// if (contact.getFixtureA().getBody().getUserData() instanceof Wood) {
		//
		// }
		//// System.err.println("CollisionBEGIN_B");
		// }
	}

	@Override
	public void endContact(Contact contact) {
		if (contact.getFixtureA().getFilterData().categoryBits == CollisionContactListener.PLAYER_COL) {
			((Player) contact.getFixtureA().getBody().getUserData()).onCollisionEnd(contact.getFixtureB());
			// System.err.println("CollisionEND_A");
		}

		// Fetch entity from Enemies.
		if (contact.getFixtureA().getFilterData().categoryBits == CollisionContactListener.ENEMY_COL) {
			// System.err.println("CollisionEND_A");
		}

		if (contact.getFixtureB().getFilterData().categoryBits == CollisionContactListener.PLAYER_COL) {
			((Player) contact.getFixtureB().getBody().getUserData()).onCollisionEnd(contact.getFixtureA());
			// System.err.println("CollisionEND_B");
		}

		// Fetch entity from Enemies.
		if (contact.getFixtureB().getFilterData().categoryBits == CollisionContactListener.ENEMY_COL) {
			// System.err.println("CollisionEND_B");
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// Fetch entity from Enemies.
		if (contact.getFixtureA().getFilterData().categoryBits == CollisionContactListener.ENEMY_COL) {
			if (contact.getFixtureA().getBody().getUserData() instanceof Spider) {
				((Spider) contact.getFixtureA().getBody().getUserData()).onCollisionContinuos(contact.getFixtureB());
			} else if (contact.getFixtureA().getBody().getUserData() instanceof Rat) {
				((Rat) contact.getFixtureA().getBody().getUserData()).onCollisionContinuos(contact.getFixtureB());
			} else if (contact.getFixtureA().getBody().getUserData() instanceof Ant) {
				((Ant) contact.getFixtureA().getBody().getUserData()).onCollisionContinuos(contact.getFixtureB());
			}
		}

		if (contact.getFixtureB().getFilterData().categoryBits == CollisionContactListener.ENEMY_COL) {
			if (contact.getFixtureB().getBody().getUserData() instanceof Spider) {
				((Spider) contact.getFixtureB().getBody().getUserData()).onCollisionContinuos(contact.getFixtureA());
			} else if (contact.getFixtureB().getBody().getUserData() instanceof Rat) {
				((Rat) contact.getFixtureB().getBody().getUserData()).onCollisionContinuos(contact.getFixtureA());
			} else if (contact.getFixtureB().getBody().getUserData() instanceof Ant) {
				((Ant) contact.getFixtureB().getBody().getUserData()).onCollisionContinuos(contact.getFixtureA());
			}
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
