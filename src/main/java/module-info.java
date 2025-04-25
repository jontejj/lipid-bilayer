module com.github.jontejj.cell
{
	requires jdk.incubator.vector;
	requires javafx.base;
	requires java.sql; // Because of nd4j.api??
	// requires javafx.beans;
	requires javafx.graphics;
	requires javafx.controls;
	// requires javafx.fxml;
	requires java.desktop;
	requires com.google.common;
	// requires nd4j.api;
	requires org.apache.commons.io;

	// Testing until maven supports it properly...
	requires org.assertj.core;
	requires org.junit.jupiter.api;
	requires jmh.core;
	requires com.almasb.fxgl.all;
	requires com.almasb.fxgl.entity;
	requires com.almasb.fxgl.core;

	exports com.github.jontejj.cell.ui to javafx.graphics;
	exports com.github.jontejj.cell.evolution.game to com.almasb.fxgl.core;
}
