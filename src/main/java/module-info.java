module com.github.jontejj.cell
{
	requires jdk.incubator.vector;
	requires java.sql; // Because of nd4j.api??
	// requires javafx.beans;
	requires transitive javafx.graphics;
	// requires javafx.fxml;
	requires transitive java.desktop;
	requires com.google.common;
	// requires nd4j.api;
	requires org.apache.commons.io;

	// Testing until maven supports it properly...
	requires org.assertj.core;
	requires org.junit.jupiter.api;
	requires jmh.core;
	requires com.google.errorprone.annotations;
	requires org.dyn4j;

}
