package com.example.data.repository

import com.example.data.model.BookReference
import com.example.data.model.SchoolClass
import com.example.data.model.Subject

object CurriculumData {

    val classes: List<SchoolClass> = (1..12).map { grade ->
        when (grade) {
            1 -> SchoolClass(
                classNumber = 1,
                title = "Class 1",
                description = "Foundational Literacy, Numeracy & Discovery",
                badgeColor = 0xFF38BDF8,
                subjects = listOf(
                    Subject(
                        id = "c1_math",
                        name = "Mathematics (Joyful Math)",
                        category = "Numeracy",
                        iconName = "Calculate",
                        accentColor = 0xFF38BDF8,
                        books = listOf(
                            BookReference(
                                id = "b1_1",
                                title = "Joyful Mathematics",
                                authorOrBoard = "NCERT Edition",
                                edition = "Latest NEP",
                                summary = "Foundational number concepts, counting up to 20, patterns and shapes.",
                                chaptersCount = 13,
                                sampleChapters = listOf("Finding the Furry Cat (Spatial)", "What is Long, What is Round?", "How Many? (Numbers 1-9)", "Making 10", "Shapes Around Us")
                            )
                        )
                    ),
                    Subject(
                        id = "c1_eng",
                        name = "English (Mridang)",
                        category = "Language",
                        iconName = "MenuBook",
                        accentColor = 0xFF818CF8,
                        books = listOf(
                            BookReference(
                                id = "b1_2",
                                title = "Mridang Textbook in English",
                                authorOrBoard = "NCERT Edition",
                                edition = "Latest Edition",
                                summary = "Rhymes, phonics, storytelling and joyful vocabulary.",
                                chaptersCount = 9,
                                sampleChapters = listOf("My Family and Me", "Life Around Us", "Food We Eat", "Seasons", "Our Beautiful Birds")
                            )
                        )
                    ),
                    Subject(
                        id = "c1_evs",
                        name = "Environmental Exploration",
                        category = "Science",
                        iconName = "Eco",
                        accentColor = 0xFF34D399,
                        books = listOf(
                            BookReference(
                                id = "b1_3",
                                title = "World Around Me",
                                authorOrBoard = "Foundational Series",
                                edition = "Standard Edition",
                                summary = "Awareness of surroundings, plants, family and nature.",
                                chaptersCount = 8,
                                sampleChapters = listOf("My School", "Green Trees", "Water and Sun", "Animals Around Us")
                            )
                        )
                    )
                )
            )
            2 -> SchoolClass(
                classNumber = 2,
                title = "Class 2",
                description = "Primary Reading, Number Operations & Natural Wonder",
                badgeColor = 0xFF60A5FA,
                subjects = listOf(
                    Subject(
                        id = "c2_math",
                        name = "Mathematics (Joyful Math 2)",
                        category = "Numeracy",
                        iconName = "Calculate",
                        accentColor = 0xFF60A5FA,
                        books = listOf(
                            BookReference(
                                id = "b2_1",
                                title = "Joyful Mathematics Book 2",
                                authorOrBoard = "NCERT Edition",
                                edition = "Latest NEP",
                                summary = "Two-digit numbers, addition & subtraction, weight and capacity.",
                                chaptersCount = 11,
                                sampleChapters = listOf("A Day at the Beach (Shapes)", "Picture Reading", "Fun with Numbers (10 to 99)", "Shadows and Reflections")
                            )
                        )
                    ),
                    Subject(
                        id = "c2_eng",
                        name = "English (Mridang 2)",
                        category = "Language",
                        iconName = "MenuBook",
                        accentColor = 0xFFA78BFA,
                        books = listOf(
                            BookReference(
                                id = "b2_2",
                                title = "Mridang Book 2",
                                authorOrBoard = "NCERT Edition",
                                edition = "Latest Edition",
                                summary = "Short stories, guided expression and interactive verses.",
                                chaptersCount = 10,
                                sampleChapters = listOf("Welcome to School", "Picture Reading with Friends", "The Clever Frog", "Monsoon Rain")
                            )
                        )
                    )
                )
            )
            3 -> SchoolClass(
                classNumber = 3,
                title = "Class 3",
                description = "Analytical Thinking, Language Proficiency & Nature",
                badgeColor = 0xFF2DD4BF,
                subjects = listOf(
                    Subject(
                        id = "c3_math",
                        name = "Mathematics (Math-Magic)",
                        category = "Numeracy",
                        iconName = "Calculate",
                        accentColor = 0xFF2DD4BF,
                        books = listOf(
                            BookReference(
                                id = "b3_1",
                                title = "Math-Magic Class 3",
                                authorOrBoard = "NCERT",
                                edition = "Revised",
                                summary = "3-digit numerals, mental math, basic multiplication and division concepts.",
                                chaptersCount = 14,
                                sampleChapters = listOf("Where to Look From", "Fun with Numbers", "Give and Take", "Shapes and Designs", "Time Goes On...")
                            )
                        )
                    ),
                    Subject(
                        id = "c3_evs",
                        name = "Environmental Studies (Looking Around)",
                        category = "Science",
                        iconName = "Forest",
                        accentColor = 0xFF10B981,
                        books = listOf(
                            BookReference(
                                id = "b3_2",
                                title = "Looking Around Class 3",
                                authorOrBoard = "NCERT",
                                edition = "Standard",
                                summary = "Community living, flora and fauna, shelter and water resources.",
                                chaptersCount = 24,
                                sampleChapters = listOf("Poonam's Day Out", "The Plant Fairy", "Water O' Water!", "Our First School", "Chhotu's House")
                            )
                        )
                    )
                )
            )
            4 -> SchoolClass(
                classNumber = 4,
                title = "Class 4",
                description = "Mathematical Reasoning, Geography & Science",
                badgeColor = 0xFF3B82F6,
                subjects = listOf(
                    Subject(
                        id = "c4_math",
                        name = "Mathematics",
                        category = "Numeracy",
                        iconName = "Calculate",
                        accentColor = 0xFF3B82F6,
                        books = listOf(
                            BookReference(
                                id = "b4_1",
                                title = "Math-Magic Book 4",
                                authorOrBoard = "NCERT",
                                edition = "Standard",
                                summary = "Building with bricks, tick-tick-tick time, fractions and tables.",
                                chaptersCount = 14,
                                sampleChapters = listOf("Building with Bricks", "Long and Short", "A Trip to Bhopal", "Tick-Tick-Tick", "The Way The World Looks")
                            )
                        )
                    ),
                    Subject(
                        id = "c4_evs",
                        name = "Environmental Studies",
                        category = "Science",
                        iconName = "Public",
                        accentColor = 0xFF10B981,
                        books = listOf(
                            BookReference(
                                id = "b4_2",
                                title = "Looking Around Book 4",
                                authorOrBoard = "NCERT",
                                edition = "Standard",
                                summary = "Cultural diversity, ecosystems, animal ears and senses.",
                                chaptersCount = 27,
                                sampleChapters = listOf("Going to School", "Ear to Ear", "A Day with Nandu", "The Story of Amrita", "Anita and the Honeybees")
                            )
                        )
                    )
                )
            )
            5 -> SchoolClass(
                classNumber = 5,
                title = "Class 5",
                description = "Primary Mastery & Preparation for Middle School",
                badgeColor = 0xFF6366F1,
                subjects = listOf(
                    Subject(
                        id = "c5_math",
                        name = "Mathematics",
                        category = "Numeracy",
                        iconName = "Calculate",
                        accentColor = 0xFF6366F1,
                        books = listOf(
                            BookReference(
                                id = "b5_1",
                                title = "Math-Magic Book 5",
                                authorOrBoard = "NCERT",
                                edition = "Standard",
                                summary = "Fish tale (large numbers), shapes & angles, parts & wholes.",
                                chaptersCount = 14,
                                sampleChapters = listOf("The Fish Tale", "Shapes and Angles", "How Many Squares?", "Parts and Wholes", "Does it Look the Same?")
                            )
                        )
                    ),
                    Subject(
                        id = "c5_evs",
                        name = "Environmental Studies",
                        category = "Science",
                        iconName = "Biotech",
                        accentColor = 0xFF06B6D4,
                        books = listOf(
                            BookReference(
                                id = "b5_2",
                                title = "Looking Around Book 5",
                                authorOrBoard = "NCERT",
                                edition = "Standard",
                                summary = "Super senses, seeds, historical monuments and space expeditions.",
                                chaptersCount = 22,
                                sampleChapters = listOf("Super Senses", "A Snake Charmer's Story", "From Tasting to Digesting", "Mangoes Round the Year", "Seeds and Seeds")
                            )
                        )
                    )
                )
            )
            6 -> SchoolClass(
                classNumber = 6,
                title = "Class 6",
                description = "Middle Stage: Rigorous Science, Algebra & World History",
                badgeColor = 0xFF8B5CF6,
                subjects = listOf(
                    Subject(
                        id = "c6_math",
                        name = "Mathematics",
                        category = "Core Math",
                        iconName = "Calculate",
                        accentColor = 0xFF8B5CF6,
                        books = listOf(
                            BookReference(
                                id = "b6_1",
                                title = "Mathematics for Class 6",
                                authorOrBoard = "NCERT",
                                edition = "Standard Curriculum",
                                summary = "Integers, fractions, decimals, algebra introduction, basic geometry.",
                                chaptersCount = 12,
                                sampleChapters = listOf("Knowing Our Numbers", "Whole Numbers", "Playing with Numbers", "Basic Geometrical Ideas", "Integers", "Algebra")
                            )
                        )
                    ),
                    Subject(
                        id = "c6_sci",
                        name = "Curiosity (Science)",
                        category = "Science",
                        iconName = "Science",
                        accentColor = 0xFF06B6D4,
                        books = listOf(
                            BookReference(
                                id = "b6_2",
                                title = "Curiosity Textbook of Science",
                                authorOrBoard = "NCERT New Framework",
                                edition = "Latest Edition",
                                summary = "Components of food, sorting materials, motion, light and living organisms.",
                                chaptersCount = 12,
                                sampleChapters = listOf("The Wonderful World of Science", "Diversity in the Living World", "Mindful Eating: Food Nutrients", "Exploring Materials", "Measurement of Length and Motion")
                            )
                        )
                    ),
                    Subject(
                        id = "c6_soc",
                        name = "Social Science (Exploring Society)",
                        category = "Social Studies",
                        iconName = "Language",
                        accentColor = 0xFFF59E0B,
                        books = listOf(
                            BookReference(
                                id = "b6_3",
                                title = "Exploring Society: India and Beyond",
                                authorOrBoard = "NCERT",
                                edition = "Latest Edition",
                                summary = "Our planet, early civilizations, local governance and democratic life.",
                                chaptersCount = 10,
                                sampleChapters = listOf("Locating Places on Earth", "Oceans and Continents", "Landforms and Life", "Timeline and Sources of History", "India: Roots of Civilization")
                            )
                        )
                    )
                )
            )
            7 -> SchoolClass(
                classNumber = 7,
                title = "Class 7",
                description = "Middle Stage: Physical Science, Medieval History & Equations",
                badgeColor = 0xFFA855F7,
                subjects = listOf(
                    Subject(
                        id = "c7_math",
                        name = "Mathematics",
                        category = "Core Math",
                        iconName = "Calculate",
                        accentColor = 0xFFA855F7,
                        books = listOf(
                            BookReference(
                                id = "b7_1",
                                title = "Mathematics for Class 7",
                                authorOrBoard = "NCERT",
                                edition = "Standard Edition",
                                summary = "Fractions and decimals, simple equations, lines and angles, triangles.",
                                chaptersCount = 13,
                                sampleChapters = listOf("Integers", "Fractions and Decimals", "Data Handling", "Simple Equations", "Lines and Angles", "The Triangle and its Properties")
                            )
                        )
                    ),
                    Subject(
                        id = "c7_sci",
                        name = "Science",
                        category = "Science",
                        iconName = "Science",
                        accentColor = 0xFF14B8A6,
                        books = listOf(
                            BookReference(
                                id = "b7_2",
                                title = "Science for Class 7",
                                authorOrBoard = "NCERT",
                                edition = "Standard Edition",
                                summary = "Nutrition in plants & animals, heat, acids & bases, physical & chemical changes.",
                                chaptersCount = 13,
                                sampleChapters = listOf("Nutrition in Plants", "Nutrition in Animals", "Heat and Temperature", "Acids, Bases and Salts", "Physical and Chemical Changes", "Respiration in Organisms")
                            )
                        )
                    )
                )
            )
            8 -> SchoolClass(
                classNumber = 8,
                title = "Class 8",
                description = "Middle Stage: Linear Equations, Cellular Biology & Modern History",
                badgeColor = 0xFFD946EF,
                subjects = listOf(
                    Subject(
                        id = "c8_math",
                        name = "Mathematics",
                        category = "Core Math",
                        iconName = "Calculate",
                        accentColor = 0xFFD946EF,
                        books = listOf(
                            BookReference(
                                id = "b8_1",
                                title = "Mathematics for Class 8",
                                authorOrBoard = "NCERT",
                                edition = "Standard Edition",
                                summary = "Linear equations in one variable, quadrilaterals, squares and cubes, algebraic identities.",
                                chaptersCount = 13,
                                sampleChapters = listOf("Rational Numbers", "Linear Equations in One Variable", "Understanding Quadrilaterals", "Data Handling", "Square and Square Roots", "Mensuration")
                            )
                        )
                    ),
                    Subject(
                        id = "c8_sci",
                        name = "Science",
                        category = "Science",
                        iconName = "Science",
                        accentColor = 0xFF0EA5E9,
                        books = listOf(
                            BookReference(
                                id = "b8_2",
                                title = "Science for Class 8",
                                authorOrBoard = "NCERT",
                                edition = "Standard Edition",
                                summary = "Cell structure, combustion & flame, force & pressure, friction, sound.",
                                chaptersCount = 13,
                                sampleChapters = listOf("Crop Production and Management", "Microorganisms: Friend and Foe", "Coal and Petroleum", "Combustion and Flame", "Cell Structure and Functions", "Force and Pressure")
                            )
                        )
                    )
                )
            )
            9 -> SchoolClass(
                classNumber = 9,
                title = "Class 9",
                description = "Secondary Stage: Coordinate Geometry, Mechanics & Matter",
                badgeColor = 0xFF3B82F6,
                subjects = listOf(
                    Subject(
                        id = "c9_math",
                        name = "Mathematics",
                        category = "Core Math",
                        iconName = "Calculate",
                        accentColor = 0xFF3B82F6,
                        books = listOf(
                            BookReference(
                                id = "b9_1",
                                title = "Mathematics for Class 9",
                                authorOrBoard = "NCERT",
                                edition = "Latest Edition",
                                summary = "Number systems, polynomials, coordinate geometry, lines and angles, triangles, surface areas.",
                                chaptersCount = 12,
                                sampleChapters = listOf("Number Systems", "Polynomials", "Coordinate Geometry", "Linear Equations in Two Variables", "Lines and Angles", "Triangles", "Quadrilaterals", "Circles", "Heron's Formula", "Surface Areas and Volumes")
                            )
                        )
                    ),
                    Subject(
                        id = "c9_sci",
                        name = "Science",
                        category = "Science",
                        iconName = "Science",
                        accentColor = 0xFF10B981,
                        books = listOf(
                            BookReference(
                                id = "b9_2",
                                title = "Science for Class 9",
                                authorOrBoard = "NCERT",
                                edition = "Latest Edition",
                                summary = "Matter in our surroundings, atoms & molecules, tissues, laws of motion, gravitation, work & energy.",
                                chaptersCount = 12,
                                sampleChapters = listOf("Matter in Our Surroundings", "Is Matter Around Us Pure?", "Atoms and Molecules", "Structure of the Atom", "The Fundamental Unit of Life", "Tissues", "Motion", "Force and Laws of Motion", "Gravitation", "Work and Energy")
                            )
                        )
                    ),
                    Subject(
                        id = "c9_eng",
                        name = "English (Beehive)",
                        category = "Literature",
                        iconName = "MenuBook",
                        accentColor = 0xFFF97316,
                        books = listOf(
                            BookReference(
                                id = "b9_3",
                                title = "Beehive Textbook in English",
                                authorOrBoard = "NCERT",
                                edition = "Standard Edition",
                                summary = "Prose and poetry focusing on critical thought and literary appreciation.",
                                chaptersCount = 11,
                                sampleChapters = listOf("The Fun They Had", "The Sound of Music", "The Little Girl", "A Truly Beautiful Mind", "The Snake and the Mirror", "My Childhood")
                            )
                        )
                    )
                )
            )
            10 -> SchoolClass(
                classNumber = 10,
                title = "Class 10",
                description = "Board Year: Quadratic Equations, Trigonometry, Light & Electricity",
                badgeColor = 0xFF6366F1,
                subjects = listOf(
                    Subject(
                        id = "c10_math",
                        name = "Mathematics",
                        category = "Core Math",
                        iconName = "Calculate",
                        accentColor = 0xFF6366F1,
                        books = listOf(
                            BookReference(
                                id = "b10_1",
                                title = "Mathematics for Class 10",
                                authorOrBoard = "NCERT Board Curriculum",
                                edition = "Latest Board Edition",
                                summary = "Real numbers, polynomials, pair of linear equations, quadratic equations, arithmetic progressions, trigonometry, statistics.",
                                chaptersCount = 14,
                                sampleChapters = listOf("Real Numbers", "Polynomials", "Pair of Linear Equations in Two Variables", "Quadratic Equations", "Arithmetic Progressions", "Triangles", "Coordinate Geometry", "Introduction to Trigonometry", "Some Applications of Trigonometry", "Circles", "Surface Areas and Volumes", "Statistics", "Probability")
                            )
                        )
                    ),
                    Subject(
                        id = "c10_sci",
                        name = "Science",
                        category = "Science",
                        iconName = "Science",
                        accentColor = 0xFF06B6D4,
                        books = listOf(
                            BookReference(
                                id = "b10_2",
                                title = "Science Class 10",
                                authorOrBoard = "NCERT Board Curriculum",
                                edition = "Latest Edition",
                                summary = "Chemical reactions, acids & bases, metals, carbon compounds, life processes, light reflection & refraction, electricity.",
                                chaptersCount = 13,
                                sampleChapters = listOf("Chemical Reactions and Equations", "Acids, Bases and Salts", "Metals and Non-metals", "Carbon and its Compounds", "Life Processes", "Control and Coordination", "How do Organisms Reproduce?", "Heredity", "Light – Reflection and Refraction", "The Human Eye and the Colourful World", "Electricity", "Magnetic Effects of Electric Current")
                            )
                        )
                    ),
                    Subject(
                        id = "c10_soc",
                        name = "Social Science",
                        category = "Social Studies",
                        iconName = "Public",
                        accentColor = 0xFFEAB308,
                        books = listOf(
                            BookReference(
                                id = "b10_3",
                                title = "India and the Contemporary World – II",
                                authorOrBoard = "NCERT",
                                edition = "Standard Edition",
                                summary = "Nationalism in Europe & India, making of a global world, print culture.",
                                chaptersCount = 5,
                                sampleChapters = listOf("The Rise of Nationalism in Europe", "Nationalism in India", "The Making of a Global World", "The Age of Industrialisation", "Print Culture and the Modern World")
                            )
                        )
                    )
                )
            )
            11 -> SchoolClass(
                classNumber = 11,
                title = "Class 11",
                description = "Senior Secondary: Advanced Physics, Organic Chemistry, Calculus",
                badgeColor = 0xFF8B5CF6,
                subjects = listOf(
                    Subject(
                        id = "c11_phy",
                        name = "Physics (Part I & II)",
                        category = "Physical Science",
                        iconName = "Bolt",
                        accentColor = 0xFF3B82F6,
                        books = listOf(
                            BookReference(
                                id = "b11_1",
                                title = "Physics Class 11 (Parts 1 & 2)",
                                authorOrBoard = "NCERT Senior Secondary",
                                edition = "Latest Edition",
                                summary = "Kinematics, laws of motion, work energy power, rotational dynamics, gravitation, thermodynamics, waves.",
                                chaptersCount = 14,
                                sampleChapters = listOf("Units and Measurements", "Motion in a Straight Line", "Motion in a Plane", "Laws of Motion", "Work, Energy and Power", "System of Particles and Rotational Motion", "Gravitation", "Mechanical Properties of Solids", "Thermodynamics", "Oscillations", "Waves")
                            )
                        )
                    ),
                    Subject(
                        id = "c11_chem",
                        name = "Chemistry (Part I & II)",
                        category = "Chemical Science",
                        iconName = "Science",
                        accentColor = 0xFF10B981,
                        books = listOf(
                            BookReference(
                                id = "b11_2",
                                title = "Chemistry Class 11",
                                authorOrBoard = "NCERT Senior Secondary",
                                edition = "Latest Edition",
                                summary = "Atomic structure, chemical bonding, thermodynamics, equilibrium, organic principles.",
                                chaptersCount = 9,
                                sampleChapters = listOf("Some Basic Concepts of Chemistry", "Structure of Atom", "Classification of Elements and Periodicity in Properties", "Chemical Bonding and Molecular Structure", "Thermodynamics", "Equilibrium", "Redox Reactions", "Organic Chemistry: Some Basic Principles and Techniques", "Hydrocarbons")
                            )
                        )
                    ),
                    Subject(
                        id = "c11_math",
                        name = "Mathematics",
                        category = "Mathematics",
                        iconName = "Calculate",
                        accentColor = 0xFFEC4899,
                        books = listOf(
                            BookReference(
                                id = "b11_3",
                                title = "Mathematics Class 11",
                                authorOrBoard = "NCERT Senior Secondary",
                                edition = "Latest Edition",
                                summary = "Sets, relations and functions, trigonometric functions, complex numbers, permutations & combinations, limits & derivatives.",
                                chaptersCount = 14,
                                sampleChapters = listOf("Sets", "Relations and Functions", "Trigonometric Functions", "Complex Numbers and Quadratic Equations", "Linear Inequalities", "Permutations and Combinations", "Binomial Theorem", "Sequences and Series", "Straight Lines", "Conic Sections", "Introduction to Three Dimensional Geometry", "Limits and Derivatives", "Statistics", "Probability")
                            )
                        )
                    ),
                    Subject(
                        id = "c11_bio",
                        name = "Biology",
                        category = "Life Science",
                        iconName = "Biotech",
                        accentColor = 0xFF14B8A6,
                        books = listOf(
                            BookReference(
                                id = "b11_4",
                                title = "Biology Class 11",
                                authorOrBoard = "NCERT Senior Secondary",
                                edition = "Standard Edition",
                                summary = "Diversity in the living world, structural organisation, cell structure & cycle, plant & human physiology.",
                                chaptersCount = 19,
                                sampleChapters = listOf("The Living World", "Biological Classification", "Plant Kingdom", "Animal Kingdom", "Morphology of Flowering Plants", "Cell: The Unit of Life", "Photosynthesis in Higher Plants", "Neural Control and Coordination")
                            )
                        )
                    )
                )
            )
            12 -> SchoolClass(
                classNumber = 12,
                title = "Class 12",
                description = "Graduation Year: Electromagnetism, Quantum & Wave Optics, Calculus",
                badgeColor = 0xFFA855F7,
                subjects = listOf(
                    Subject(
                        id = "c12_phy",
                        name = "Physics (Part I & II)",
                        category = "Physical Science",
                        iconName = "Bolt",
                        accentColor = 0xFF3B82F6,
                        books = listOf(
                            BookReference(
                                id = "b12_1",
                                title = "Physics Class 12 (Comprehensive)",
                                authorOrBoard = "NCERT Senior Secondary Board",
                                edition = "Latest Edition",
                                summary = "Electric charges and fields, electrostatic potential, current electricity, moving charges and magnetism, electromagnetic induction, AC circuits, optics, semiconductor electronics.",
                                chaptersCount = 14,
                                sampleChapters = listOf("Electric Charges and Fields", "Electrostatic Potential and Capacitance", "Current Electricity", "Moving Charges and Magnetism", "Magnetism and Matter", "Electromagnetic Induction", "Alternating Current", "Electromagnetic Waves", "Ray Optics and Optical Instruments", "Wave Optics", "Dual Nature of Radiation and Matter", "Atoms", "Nuclei", "Semiconductor Electronics")
                            )
                        )
                    ),
                    Subject(
                        id = "c12_chem",
                        name = "Chemistry (Part I & II)",
                        category = "Chemical Science",
                        iconName = "Science",
                        accentColor = 0xFF10B981,
                        books = listOf(
                            BookReference(
                                id = "b12_2",
                                title = "Chemistry Class 12",
                                authorOrBoard = "NCERT Senior Secondary Board",
                                edition = "Latest Edition",
                                summary = "Solutions, electrochemistry, chemical kinetics, d and f block elements, coordination compounds, haloalkanes, aldehydes, amines, biomolecules.",
                                chaptersCount = 10,
                                sampleChapters = listOf("Solutions", "Electrochemistry", "Chemical Kinetics", "The d- and f-Block Elements", "Coordination Compounds", "Haloalkanes and Haloarenes", "Alcohols, Phenols and Ethers", "Aldehydes, Ketones and Carboxylic Acids", "Amines", "Biomolecules")
                            )
                        )
                    ),
                    Subject(
                        id = "c12_math",
                        name = "Mathematics (Part I & II)",
                        category = "Mathematics",
                        iconName = "Calculate",
                        accentColor = 0xFFEC4899,
                        books = listOf(
                            BookReference(
                                id = "b12_3",
                                title = "Mathematics Class 12",
                                authorOrBoard = "NCERT Senior Secondary Board",
                                edition = "Latest Edition",
                                summary = "Relations and functions, matrices and determinants, continuity & differentiability, integrals, differential equations, vectors, 3D geometry, linear programming.",
                                chaptersCount = 13,
                                sampleChapters = listOf("Relations and Functions", "Inverse Trigonometric Functions", "Matrices", "Determinants", "Continuity and Differentiability", "Application of Derivatives", "Integrals", "Application of Integrals", "Differential Equations", "Vector Algebra", "Three Dimensional Geometry", "Linear Programming", "Probability")
                            )
                        )
                    ),
                    Subject(
                        id = "c12_cs",
                        name = "Computer Science (Python)",
                        category = "Computing",
                        iconName = "Terminal",
                        accentColor = 0xFF8B5CF6,
                        books = listOf(
                            BookReference(
                                id = "b12_4",
                                title = "Computer Science with Python",
                                authorOrBoard = "NCERT / Board Reference",
                                edition = "Latest Edition",
                                summary = "Computational thinking, data structures, computer networks, SQL databases and cybersecurity.",
                                chaptersCount = 11,
                                sampleChapters = listOf("Python Revision Tour", "Functions and Modules", "Data File Handling", "Text and Binary Files", "Data Structures: Stacks", "Computer Networks", "Database Management and SQL", "Interface Python with SQL", "Society, Law and Ethics")
                            )
                        )
                    )
                )
            )
            else -> SchoolClass(
                classNumber = grade,
                title = "Class $grade",
                description = "Standard Curriculum Reference",
                badgeColor = 0xFF3B82F6,
                subjects = emptyList()
            )
        }
    }
}
