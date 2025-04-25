/* Copyright 2021 jonatanjonsson
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
package com.github.jontejj.cell.ui.fouriertransform;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.function.DoubleConsumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Slider extends JPanel implements ChangeListener
{
	private static final long serialVersionUID = 1L;
	private final DoubleConsumer cpsChangedFunction;

	public Slider(DoubleConsumer cpsChangedFunction)
	{
		this.cpsChangedFunction = cpsChangedFunction;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// Create the label.
		JLabel sliderLabel = new JLabel("Cycles Per Second", SwingConstants.CENTER);
		sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Create the slider.
		JSlider cyclesPerSecond = new JSlider(SwingConstants.HORIZONTAL, 1, 100, 3);

		cyclesPerSecond.addChangeListener(this);

		// Turn on labels at major tick marks.

		cyclesPerSecond.setMajorTickSpacing(10);
		cyclesPerSecond.setMinorTickSpacing(1);
		cyclesPerSecond.setPaintTicks(true);
		cyclesPerSecond.setPaintLabels(true);
		cyclesPerSecond.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		// Put everything together.
		add(sliderLabel);
		add(cyclesPerSecond);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	/** Listen to the slider. */
	public void stateChanged(ChangeEvent e)
	{
		JSlider source = (JSlider) e.getSource();
		if(!source.getValueIsAdjusting())
		{
			int cps = source.getValue();
			cpsChangedFunction.accept(cps / 10.0);
		}
	}

	/**
	 * Create the GUI and show it. For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	private static void createAndShowGUI(DoubleConsumer cpsChangedFunction)
	{
		// Create and set up the window.
		JFrame frame = new JFrame("SliderDemo");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Slider slider = new Slider(cpsChangedFunction);

		// Add content to the window.
		frame.add(slider, BorderLayout.CENTER);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void create(DoubleConsumer cpsChangedFunction)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				createAndShowGUI(cpsChangedFunction);
			}
		});
	}
}
