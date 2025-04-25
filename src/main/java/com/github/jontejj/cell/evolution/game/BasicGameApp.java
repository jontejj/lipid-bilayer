/* Copyright 2025 jonatanjonsson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.jontejj.cell.evolution.game;

import static com.almasb.fxgl.dsl.FXGL.onKey;

import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class BasicGameApp extends GameApplication
{

	@Override
	protected void initSettings(GameSettings settings)
	{
		settings.setWidth(600);
		settings.setHeight(600);
		settings.setTitle("Basic Game App");
		settings.setVersion("0.1");
	}

	public enum EntityType
	{
		PLAYER,
		COIN
	}

	private Entity player;

	@Override
	protected void initGame()
	{
		player = FXGL.entityBuilder().type(EntityType.PLAYER).at(300, 300).viewWithBBox("brick.png")// .view(new Rectangle(5, 5,
																									// Color.BLUE))
				.with(new CollidableComponent(true)).buildAndAttach();

		FXGL.entityBuilder().type(EntityType.COIN).at(500, 200).viewWithBBox(new Circle(15, 15, 15, Color.YELLOW)).with(new CollidableComponent(true))
				.buildAndAttach();
	}

	@Override
	protected void initGameVars(Map<String, Object> vars)
	{
		vars.put("pixelsMoved", 0);
	}

	@Override
	protected void initInput()
	{
		onKey(KeyCode.D, () -> {
			player.translateX(5); // move sdright 5 pixels
			FXGL.inc("pixelsMoved", +5);
		});

		onKey(KeyCode.A, () -> {
			player.translateX(-5); // move left 5 pixels
			FXGL.inc("pixelsMoved", +5);
		});

		onKey(KeyCode.W, () -> {
			player.translateY(-5); // move up 5 pixels
			FXGL.inc("pixelsMoved", +5);
		});

		onKey(KeyCode.S, () -> {
			player.translateY(5); // move down 5 pixels
			FXGL.inc("pixelsMoved", +5);
		});
	}

	@Override
	protected void initPhysics()
	{
		FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.COIN){

			// order of types is the same as passed into the constructor
			@Override
			protected void onCollisionBegin(Entity player, Entity coin)
			{
				coin.removeFromWorld();
			}
		});
	}

	@Override
	protected void initUI()
	{
		Text textPixels = new Text();
		textPixels.setTranslateX(50); // x = 50
		textPixels.setTranslateY(100); // y = 100
		textPixels.textProperty().bind(FXGL.getWorldProperties().intProperty("pixelsMoved").asString());

		FXGL.getGameScene().addUINode(textPixels); // add to the scene graph

		var brickTexture = FXGL.getAssetLoader().loadTexture("brick.png");
		brickTexture.setTranslateX(50);
		brickTexture.setTranslateY(450);

		FXGL.getGameScene().addUINode(brickTexture);
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
