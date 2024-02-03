package org.psnbtech.entity;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.psnbtech.Game;
import org.psnbtech.WorldPanel;
import org.psnbtech.util.Vector2;

public class Player extends Entity {
	
	private static final double DEFAULT_ROTATION = -Math.PI / 2.0;
	
	/**
	 * The magnitude of our ship's thrust.
	 */
	private static final double THRUST_MAGNITUDE = 0.0385;
	
	/**
	 * The maximum speed at which our ship can travel.
	 */
	private static final double MAX_VELOCITY_MAGNITUDE = 6.5;
	
	/**
	 * The speed at which the ship rotates.
	 */
	private static final double ROTATION_SPEED = 0.052;
	
	/**
	 * The factor at which our ship slows down.
	 */
	private static final double SLOW_RATE = 0.995;
	
	/**
	 * The maximum number of bullets that can be fired at once.
	 */
	private static final int MAX_BULLETS = 4;
	
	/**
	 * The number of cycles that must elapse between shots.
	 */
	private static final int FIRE_RATE = 4;
	
	/**
	 * The maximum number of shots that can be fired consecutively before
	 * overheating.
	 */
	private static final int MAX_CONSECUTIVE_SHOTS = 8;
	
	/**
	 * The number of cycles that must elapse before we stop overheating.
	 */
	private static final int MAX_OVERHEAT = 30;
	
	/**
	 * Whether the ship should apply thrust when it updates.
	 */
	private boolean thrustPressed;
	
	/**
	 * Whether the ship should rotate to the left when it updates.
	 */
	private boolean rotateLeftPressed;
	
	/**
	 * Whether the ship should rotate to the right when it updates.
	 */
	private boolean rotateRightPressed;
	
	/**
	 * Whether the ship should fire a bullet when it updates.
	 */
	private boolean firePressed;
		
	/**
	 * Whether the ship is allowed to fire a bullet.
	 */
	private boolean firingEnabled;
	
	/**
	 * The number of consecutive shots fired.
	 */
	private int consecutiveShots;
	
	/**
	 * The cooldown timer for firing.
	 */
	private int fireCooldown;
	
	/**
	 * The cooldown timer for overheating.
	 */
	private int overheatCooldown;
	
	/**
	 * The current animation frame.
	 */
	private int animationFrame;
	
	/**
	 * The bullets that have been fired.
	 */
	private List<Bullet> bullets;
        
        
        private int shieldDuration = 0;//Se inicia la variable de duracion del escudo
        
        private Shield shield; // Instance of the Shield class
        
        public boolean isShieldActive = false;
        
	private Vector2 position;
        
        private int shieldTimer=900;
        
        
	/**
	 * Initializes a new Player instance.
	 */
        
          
        public void setShieldActive(boolean isActive) {
        this.isShieldActive = isActive;
    }
        
        // Method to set the player's position
    public void setPosition(Vector2 newPosition) {
        this.position = newPosition;
    }
    
         // Method to retrieve the player's position for the shield
    public Vector2 getPlayerPosition() {
         return position;
    }
        
 
	public Player() {
		super(new Vector2(WorldPanel.WORLD_SIZE / 2.0, WorldPanel.WORLD_SIZE / 2.0), new Vector2(0.0, 0.0), 10.0, 0);
		this.bullets = new ArrayList<>();
		this.rotation = DEFAULT_ROTATION;
		this.thrustPressed = false;
		this.rotateLeftPressed = false;
		this.rotateRightPressed = false;
		this.firePressed = false;
		this.firingEnabled = true;
		this.fireCooldown = 0;
		this.overheatCooldown = 0;
		this.animationFrame = 0;
                this.position = new Vector2(WorldPanel.WORLD_SIZE/ 2.0, WorldPanel.WORLD_SIZE/ 2.0);
                this.shield = new Shield();
	}
        
         // Method to set the shield duration when the player collects the power-up
        public void setShieldDuration(int duration) {
        shieldDuration = duration;
}

    //  Method to check if the shield is active
        public boolean hasShield() {
        return shieldDuration > 0;
    }
        
	/**
	 * Sets whether this player should apply thrust when it updates.
	 * @param state Whether to apply thrust.
	 */
	public void setThrusting(boolean state) {
		this.thrustPressed = state;
	}
	
	/**
	 * Sets whether this player should rotate left when it updates.
	 * @param state Whether to rotate left.
	 */
	public void setRotateLeft(boolean state) {
		this.rotateLeftPressed = state;
	}
	
	/**
	 * Sets whether this player should rotate right when it updates.
	 * @param state Whether to rotate right.
	 */
	public void setRotateRight(boolean state) {
		this.rotateRightPressed = state;
	}
	
	/**
	 * Sets whether this player should fire when it updates.
	 * @param state Whether to fire.
	 */
	public void setFiring(boolean state) {
		this.firePressed = state;
	}
		
	/**
	 * Sets whether this player can fire when it updates.
	 * @param state Whether this player can fire.
	 */
	public void setFiringEnabled(boolean state) {
		this.firingEnabled = state;
	}
	
	/**
	 * Resets the player to it's default spawn position, speed, and rotation,
	 * and clears the list of bullets.
	 */
	public void reset() {
		this.rotation = DEFAULT_ROTATION;
		position.set(WorldPanel.WORLD_SIZE / 2.0, WorldPanel.WORLD_SIZE / 2.0);
		velocity.set(0.0, 0.0);
		bullets.clear();
                
	}
		
	@Override
	public void update(Game game) {
            // Decrement the shield duration each frame
		super.update(game);
                // Decrement the shield duration each frame
            if (isShieldActive) {
            shieldTimer--;
            if (shieldTimer <= 0) {
                isShieldActive = false;
                shieldTimer=900; // Desactivar el escudo
            }
        }
        
		this.animationFrame++;
		
		/*
		 * Rotate the ship if only one of the rotation flags are true, as doing
		 * one rotation will cancel the effect of doing the other.
		 * 
		 * The conditional statement can alternatively be written like this:
		 * 
		 * if(rotateLeftPressed) {
		 *     rotate(-ROTATION_SPEED);
		 * } else {
		 *     rotate(ROTATION_SPEED);
		 * }
		 */
		if(rotateLeftPressed != rotateRightPressed) {
			rotate(rotateLeftPressed ? -ROTATION_SPEED : ROTATION_SPEED);
		}
		
		/*
		 * Apply thrust to our ship's velocity, and ensure that the ship is not
		 * going faster than the maximum magnitude.
		 */
		if(thrustPressed) {
			/*
			 * Here we create a new vector based on our ship's rotation, and scale
			 * it by our thrust's magnitude. Then we add that vector to our velocity.
			 */
			velocity.add(new Vector2(rotation).scale(THRUST_MAGNITUDE));
			
			/*
			 * Here we determine whether our ship is going faster than is
			 * allowed. Like when checking for collisions, we check the squared
			 * magnitude because it is quicker to square a value than it is to
			 * take the square root.
			 * 
			 * If our velocity exceeds our maximum allowed velocity, we normalize
			 * it (giving it a magnitude of 1.0), and scale it to be he maximum.
			 */
			if(velocity.getLengthSquared() >= MAX_VELOCITY_MAGNITUDE * MAX_VELOCITY_MAGNITUDE) {
				velocity.normalize().scale(MAX_VELOCITY_MAGNITUDE);
			}
		}
		
		/*
		 * If our ship is moving, slow it down slightly, which causes the ship
		 * to some to a gradual stop.
		 */
		if(velocity.getLengthSquared() != 0.0) {
			velocity.scale(SLOW_RATE);
		}
		
		/*
		 * Loop through each bullet and remove it from the list if necessary.
		 */
		Iterator<Bullet> iter = bullets.iterator();
		while(iter.hasNext()) {
			Bullet bullet = iter.next();
			if(bullet.needsRemoval()) {
				iter.remove();
			}
		}
		
		/*
		 * Decrement the fire and overheat cooldowns, and determine if we can fire another
		 * bullet.
		 */
		this.fireCooldown--;
		this.overheatCooldown--;
		if(firingEnabled && firePressed && fireCooldown <= 0 && overheatCooldown <= 0) {
			/*
			 * We can only create a new bullet if we haven't yet exceeded the
			 * maximum number of bullets that we can have fired at once.
			 * 
			 * If a new bullet can be fired, we reset the fire cooldown, and
			 * register a new bullet to the game world.
			 */
			if(bullets.size() < MAX_BULLETS) {
				this.fireCooldown = FIRE_RATE;
				
				Bullet bullet = new Bullet(this, rotation);
				bullets.add(bullet);
				game.registerEntity(bullet);
			}
			
			/*
			 * Since we're attempting to fire a bullet, we increment the number
			 * of consecutive shots and determine if we should set the overheat
			 * flag.
			 * 
			 * This prevents us from being able to wipe out entire groups of
			 * asteroids in one burst if we're accurate enough, and will prevent
			 * us from firing a continuous stream of bullets until we start missing.
			 */
			this.consecutiveShots++;
			if(consecutiveShots == MAX_CONSECUTIVE_SHOTS) {
				this.consecutiveShots = 0;
				this.overheatCooldown = MAX_OVERHEAT;
			}
		} else if(consecutiveShots > 0) {
			//Decrement the number of consecutive shots, since we're not trying to fire.
			this.consecutiveShots--;
		}
	}
	
        // Método para obtener el área de colisión del semicírculo
    
@Override
public void handleCollision(Game game, Entity other) {
    if (other instanceof Asteroid) {
        if (!isShieldActive) {
            game.killPlayer(); // Elimina al jugador si no hay escudo activo y colisiona con un asteroide
        } else {
            this.radius = 25; // Reducir el radio del jugador si el escudo está activo
            other.flagForRemoval(); // Elimina el asteroide al colisionar con el escudo activo
        }
    }
}

	@Override
	public void draw(Graphics2D g, Game game) {
            if(isShieldActive)
                {shield.draw(g, game);//Si el escudo está activo se llama al método draw de la clase Shield para dibujarlo.
        }
    /*
     * Resto del código para dibujar el jugador...
     * Añade aquí el código existente para dibujar al jugador.
     */
    
		/*
		 * When the player recently spawned, it will flash for a few seconds to indicate
		 * that it is invulnerable. The player will not flash if the game is paused.
		 */
		if(!game.isPlayerInvulnerable() || game.isPaused() || animationFrame % 20 < 10) {
			/*
			 * Draw the ship. The nose will face right (0.0 on the unit circle). All
			 * transformations will be handled by the WorldPanel before calling the draw
			 * function.
			 */
			g.drawLine(-10, -8, 10, 0);
			g.drawLine(-10, 8, 10, 0);
			g.drawLine(-6, -6, -6, 6);
		
			//Draw the flames behind the ship if we thrusting, and not paused.
			if(!game.isPaused() && thrustPressed && animationFrame % 6 < 3) {
				g.drawLine(-6, -6, -12, 0);
				g.drawLine(-6, 6, -12, 0);
            }
        }        
    }
}
	