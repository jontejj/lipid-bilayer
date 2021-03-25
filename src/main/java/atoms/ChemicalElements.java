/* Copyright 2018 jonatanjonsson
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
package atoms;

/**
 * All known atoms (as of 2018) <a href="https://en.wikipedia.org/wiki/Periodic_table">wiki</a>
 */
public enum ChemicalElements
{
	H("Hydrogen", 1, Amu.of(1.00794)),
	He("Helium", 2, Amu.of(4.002602)),
	Li("Lithium", 3, Amu.of(6.941)),
	Be("Beryllium", 4, Amu.of(9.012182)),
	B("Boron", 5, Amu.of(10.811)),
	C("Carbon", 6, Amu.of(12.011)),
	N("Nitrogen", 7, Amu.of(14.0067)),
	O("Oxygen", 8, Amu.of(15.9994)),
	F("Fluorine", 9, Amu.of(18.9984032)),
	Ne("Neon", 1, Amu.of(20.1797)),
	Na("Sodium", 11, Amu.of(22.98976928)),
	Mg("Magnesium", 12, Amu.of(24.3050)),
	Al("Aluminium", 13, Amu.of(26.9815386)),
	Si("Silicon", 14, Amu.of(28.0855)),
	P("Phosphorus", 15, Amu.of(30.973762)),
	S("Sulfur", 16, Amu.of(32.065)),
	Cl("Chlorine", 17, Amu.of(35.453)),
	Ar("Argon", 18, Amu.of(39.948)),
	K("Potassium", 19, Amu.of(39.0983)),
	Ca("Calcium", 20, Amu.of(40.078)),
	Sc("Scandium", 21, Amu.of(44.955912)),
	Ti("Titanium", 22, Amu.of(47.867)),
	V("Vanadium", 23, Amu.of(50.9415)),
	Cr("Chromium", 24, Amu.of(51.9961)),
	Mn("Manganese", 25, Amu.of(54.938045)),
	Fe("Iron", 26, Amu.of(55.845)),
	Co("Cobolt", 27, Amu.of(58.933195)),
	Ni("Nickel", 28, Amu.of(58.6934)),
	Cu("Copper", 29, Amu.of(63.546)),
	Zn("Zinc", 30, Amu.of(65.38)),
	Ga("Gallium", 31, Amu.of(69.723)),
	Ge("Germanium", 32, Amu.of(72.64)),
	As("Arsenic", 33, Amu.of(74.92160)),
	Se("Selenium", 34, Amu.of(78.96)),
	Br("Bromine", 35, Amu.of(79.904)),
	Kr("Krypton", 36, Amu.of(83.798)),
	Rb("Rubidium", 37, Amu.of(85.4678)),
	Sr("Strontium", 38, Amu.of(87.62)),
	Y("Yttrium", 39, Amu.of(88.90585)),
	Zr("Zirconium", 40, Amu.of(91.224)),
	Nb("Niobium", 41, Amu.of(92.906)),
	Mo("Molybdenum", 42, Amu.of(95.96)),
	Tc("Technitium", 43, Amu.of(97.9072), Stable.NO),
	Ru("Ruthenium", 44, Amu.of(101.07)),
	Rh("Rhodium", 45, Amu.of(102.90550)),
	Pd("Palladium", 46, Amu.of(106.42)),
	Ag("Silver", 47, Amu.of(107.8682)),
	Cd("Cadmium", 48, Amu.of(112.411)),
	In("Indium", 49, Amu.of(114.818)),
	Sn("Tin", 50, Amu.of(118.710)),
	Sb("Antimony", 51, Amu.of(121.760)),
	Te("Tellerium", 52, Amu.of(127.60)),
	I("Iodine", 53, Amu.of(126.90447)),
	Xe("Xenon", 54, Amu.of(131.293)),
	Cs("Caesium", 55, Amu.of(132.9054519)),
	Ba("Barium", 56, Amu.of(137.327)),
	La("Lanthanum", 57, Amu.of(138.90547)),
	Ce("Cerium", 58, Amu.of(140.116)),
	Pr("Prasedynium", 59, Amu.of(140.90765)),
	Nd("Neodymium", 60, Amu.of(144.242)),
	Pm("Promethium", 61, Amu.of(145), Stable.NO),
	Sm("Samarium", 62, Amu.of(150.36)),
	Eu("Europium", 63, Amu.of(151.964)),
	Gd("Gadolinium", 64, Amu.of(157.25)),
	Tb("Terbium", 65, Amu.of(158.92535)),
	Dy("Dysprosium", 66, Amu.of(162.500)),
	Ho("Holmium", 67, Amu.of(164.93032)),
	Er("Erbium", 68, Amu.of(167.259)),
	Tm("Thulium", 69, Amu.of(168.93421)),
	Yb("Ytterbium", 70, Amu.of(173.054)),
	Lu("Lutetium", 71, Amu.of(174.9668)),
	Hf("Hafnium", 72, Amu.of(178.49)),
	Ta("Tantalum", 73, Amu.of(180.94788)),
	W("Tungsten", 74, Amu.of(183.84)),
	Re("Rhenium", 75, Amu.of(186.207)),
	Os("Osmium", 76, Amu.of(193.23)),
	Ir("Iridium", 77, Amu.of(192.217)),
	Pt("Platinum", 78, Amu.of(195.084)),
	Au("Gold", 79, Amu.of(196.966569)),
	Hg("Mercury", 80, Amu.of(200.59)),
	Tl("Thallium", 81, Amu.of(204.3833)),
	Pb("Lead", 82, Amu.of(207.2)),
	Bi("Bismuth", 83, Amu.of(208.98040)),
	Po("Polonium", 84, Amu.of(208.9824), Stable.NO),
	At("Astatine", 85, Amu.of(209.9871), Stable.NO),
	Rn("Radon", 86, Amu.of(222.0176), Stable.NO),
	Fr("Francium", 87, Amu.of(223), Stable.NO),
	Ra("Radium", 88, Amu.of(226), Stable.NO),
	Ac("Actinium", 89, Amu.of(227), Stable.NO),
	Th("Thorium", 90, Amu.of(232.03806)),
	Pa("Protactinium", 91, Amu.of(231.03588)),
	U("Uranium", 92, Amu.of(238.02891)),
	Np("Neptunium", 93, Amu.of(237), Stable.NO),
	Pu("Plutonium", 94, Amu.of(244), Stable.NO),
	Am("Americium", 95, Amu.of(243), Stable.NO),
	Cm("Curium", 96, Amu.of(247), Stable.NO),
	Bk("Berkelium", 97, Amu.of(247), Stable.NO),
	Cf("Californium", 98, Amu.of(251), Stable.NO),
	Es("Einsteinium", 99, Amu.of(252), Stable.NO),
	Fm("Fermium", 100, Amu.of(257), Stable.NO),
	Md("Mendelevium", 101, Amu.of(258), Stable.NO),
	No("Nobelium", 102, Amu.of(259), Stable.NO),
	Lr("Lawrencium", 103, Amu.of(262), Stable.NO),
	Rf("Rutherfordium", 104, Amu.of(261), Stable.NO),
	Db("Dubnium", 105, Amu.of(262), Stable.NO),
	Sg("Seaborgium", 106, Amu.of(266), Stable.NO),
	Bh("Bohrium", 107, Amu.of(264), Stable.NO),
	Hs("Hassium", 108, Amu.of(277), Stable.NO),
	Mt("Meitnerium", 109, Amu.of(268), Stable.NO),
	Ds("Damstadtium", 110, Amu.of(271), Stable.NO),
	Rg("Roentgenium", 111, Amu.of(272), Stable.NO),
	Cn("Copernicum", 112, Amu.of(285), Stable.NO),
	Nh("Nihonium", 113, Amu.of(286), Stable.NO),
	Fl("Flerovium", 114, Amu.of(289), Stable.NO),
	Mc("Moscovium", 115, Amu.of(290), Stable.NO),
	Lv("Livermorium", 116, Amu.of(293), Stable.NO),
	Ts("Tennessine", 117, Amu.of(294)),
	Og("Oganesson", 118, Amu.of(294));

	private final String name;
	private final int protons;
	private final Amu mass;
	private final Stable stable;
	// private final ChemicalGroup group;

	ChemicalElements(String name, int protons, Amu mass) // ChemicalGroup group)
	{
		// group
		this(name, protons, mass, Stable.YES);
	}

	ChemicalElements(String name, int protons, Amu mass, Stable stable)// ChemicalGroup group,)
	{
		this.name = name;
		this.protons = protons;
		this.mass = mass;
		// this.group = group;
		this.stable = stable;
	}

	public int protons()
	{
		return protons;
	}

	public String shortName()
	{
		return name();
	}

	public String longName()
	{
		return name;
	}

}
