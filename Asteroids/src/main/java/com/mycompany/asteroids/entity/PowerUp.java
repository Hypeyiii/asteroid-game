//Clase para la creación del Power Up.
//Isaac Alessandro Frias Salinas Matrícula: 2005483

/**
 *
 * @author isaac
 */

package org.psnbtech.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import org.psnbtech.Game;
import org.psnbtech.util.Vector2;

public class PowerUp extends Entity {

    private static final int NUM_POINTS = 10; //Número de puntos para dibujar en el poligono la forma de estrella que tiene el Power Up
    
    private static final double RADIUS = 15; //Tamaño del radio del power up
    
    private static final Polygon SHAPE = generatePolygon(RADIUS); //Relleno del dibujo.
   
    private Vector2 playerPosition; // Variable para mantener la posición del jugador.
    
    private static final int DURATION = 300; //Duración de la estrella en el juego.
    
    private int durationTimer = DURATION;
    
    private final Shield shield = null;
  
    public boolean isShieldActive = false; //Se inicializa la bandera para marcar cuando el escudo está activo.
   
    //Método para saber cuando el escudo está activo.
    public void setShieldActive(boolean isActive) {
        this.isShieldActive = isActive;
    }
    
    //Entity le pasa los valores establecidos.
    public PowerUp(Vector2 position, Vector2 velocity, int killScore) {
    super(position, velocity, RADIUS, killScore);
}
     // Método para calcular la posicion del jugador.
    public void setPlayerPosition(Vector2 position) {
        this.playerPosition = position;
    }
    
    //Método para dibujar el poligono(estrella).
    private static Polygon generatePolygon(double radius) {
        int[] x = new int[NUM_POINTS];
        int[] y = new int[NUM_POINTS];

        double angle = (2 * Math.PI / NUM_POINTS);
        for (int i = 0; i < NUM_POINTS; i++) {
            x[i] = (int) (radius * Math.sin(i * angle));
            y[i] = (int) (radius * Math.cos(i * angle));
            if (i % 2 == 0) {
                x[i] *= 0.5;
                y[i] *= 0.5;
            }
        }
        return new Polygon(x, y, NUM_POINTS);
    }

    //Método para actualizar frame x frame el escudo.
@Override
    public void update(Game game) {
        durationTimer--; // Reducir el temporizador del PowerUp en cada ciclo
        if (durationTimer <= 0) {
            // Si el tiempo ha transcurrido, marcar el PowerUp para su eliminación
            flagForRemoval();
        }
    }
    //Método para dibujar la estrella.
    @Override
    public void draw(Graphics2D g, Game game) {
          if (playerPosition != null) {
            g.setColor(Color.WHITE);
            g.fillPolygon(generatePolygon(RADIUS));
        }
        g.setColor(Color.WHITE);
        g.fillPolygon(SHAPE);
    }
    
    //Método para manejar la colision de la estrella.
    @Override
    public void handleCollision(Game game, Entity other) {
        if (other instanceof Player) {
            Player player = (Player) other;
            if (player != null) {
                // Realizar acciones relacionadas con el PowerUp para el jugador
                player.setShieldActive(true);
                flagForRemoval(); // Eliminar la estrella después de la colisión con el jugador
            }
        }
    }
}