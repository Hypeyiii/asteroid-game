package org.psnbtech.util;

/**
 * Vector2 represents a 2-dimensional vector.
 * @author Brendan Jones
 */
public class Vector2 {
	
	/**
	 * The x value of the vector.
	 */
	public double x;
	
	/**
	 * The y value of the vector.
	 */
	public double y;
	
	/**
	 * Creates a new Vector from an angle. The length of this vector will be 1.
	 * @param angle The angle.
	 */
	public Vector2(double angle) {
		this.x = Math.cos(angle);
		this.y = Math.sin(angle);
	}
	
        public float distance(Vector2 other) {
    float dx = (float) (other.x - this.x);
    float dy = (float) (other.y - this.y);
    return (float) Math.sqrt(dx * dx + dy * dy);
}
        
        public double getDistanceTo(Vector2 other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
        
	/**
	 * Creates a new Vector with the desired values.
	 * @param x The x value.
	 * @param y The y value.
	 */
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Creates a new Vector and copies the components from the old.
	 * @param vec The vector to copy.
	 */
	public Vector2(Vector2 vec) {
		this.x = vec.x;
		this.y = vec.y;
	}
	
	/**
	 * Sets the components of this vector.
	 * @param x The x component.
	 * @param y The y component.
	 * @return This vector for chaining.
	 */
	public Vector2 set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	/**
	 * Adds the components of a vector to this one.
	 * @param vec The vector to add.
	 * @return This vector for chaining.
	 */
	public Vector2 add(Vector2 vec) {
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}
	
	/**
	 * Scales the components of this vector.
	 * @param scalar The scalar value.
	 * @return This vector for chaining.
	 */
	public Vector2 scale(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}
	
	/**
	 * Normalizes this Vector (giving it a length of 1.0).
	 * @return This vector for chaining.
	 */
	public Vector2 normalize() {
		double length = getLengthSquared();
		if(length != 0.0f && length != 1.0f) {
			length = Math.sqrt(length);
			this.x /= length;
			this.y /= length;
		}
		return this;
	}
	
	/**
	 * Gets the squared length of this Vector.
	 * @return The squared length.
	 */
	public double getLengthSquared() {
		return (x * x + y * y);
	}

	/**
	 * Gets the squared distance to another Vector.
	 * @param vec The other vector.
	 * @return The squared distance.
	 */
	public double getDistanceToSquared(Vector2 vec) {
		double dx = this.x - vec.x;
		double dy = this.y - vec.y;
		return (dx * dx + dy * dy);
	}

    public void setShieldActive(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
        
	public class Player {
    private Vector2 position;

    // Constructor
    public Player(double x, double y) {
        position = new Vector2(x, y);
    }

    // Getters and setters for position
    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 newPosition) {
        this.position = newPosition;
        }
    }
}
