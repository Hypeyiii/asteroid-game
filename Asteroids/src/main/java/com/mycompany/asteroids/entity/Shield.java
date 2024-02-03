//Clase para la creación del Escudo que protegerá al jugador (Satélite).
//Isaac Alessandro Frias Salinas Matrícula: 2005483

/**
 *
 * @author isaac
 */

package org.psnbtech.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import org.psnbtech.Game;
import org.psnbtech.util.Vector2;

public class Shield extends Entity {
    
    private Vector2 playerPosition;
    private static final double shieldRadius = 50; //El tamaño de radio del escudo.
    private Player player;
    
    public Shield() {
        super(new Vector2(0, 0), new Vector2(0, 0), shieldRadius, 0);
        this.player = player;
    }
    
    //Método para calcular la posicion del jugador.
    public void setPlayerPosition(Vector2 position) {
        this.playerPosition = position;//La clase player le pasa el valor de posicion del jugador.
    }
    
    //Métdo para actualizar frame x frame el escudo.
    public void update(Game game) {
        super.update(game);
    }
    
    //Método para dibujar el escudo.
   @Override
public void draw(Graphics2D g, Game game) {
    g.setColor(Color.WHITE);
    double x = position.x - shieldRadius / 2.0;
    double y = position.y - shieldRadius / 2.0;
    double radius = shieldRadius;
    g.drawArc((int) Math.round(x), (int) Math.round(y), (int) Math.round(radius), (int) Math.round(radius), 270, 180); //Forma de mediocirulo.
}

    //Método para manejar la colision del escudo.
    @Override
    public void handleCollision(Game game, Entity other) {
        if (player != null && player.isShieldActive && other instanceof Asteroid) {
            flagForRemoval(); // Elimina el asteroide al colisionar con el escudo activo
        }
    }
}