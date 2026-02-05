# Food-Chain-Through-Time
Food Chain Through Time is a dynamic ecosystem simulation and strategy game built with Java Swing. It features a sophisticated AI system, time-travel mechanics (Eras), and a robust technical architecture based on Advanced Programming principles.

*Key Features*
Temporal Eras: Choose between Past, Present, and Future. Each era dynamically loads unique assets (backgrounds, animal sprites, and food types) via external configuration files.

Three-Tier Ecosystem:

Predator (Player): Controlled via keyboard with a unique "Dash" special ability.

Prey (AI): Uses a heuristic-based safety algorithm to avoid predators and find food.

Apex Predator (AI): A relentless hunter that uses a Manhattan-distance chase algorithm to track the player and prey.

Dynamic Configuration: Grid sizes (10x10 to 20x20) and round limits are fully customizable at startup.

Persistence: Full Save/Load functionality and detailed action logging (gameLog.txt).

*Technical Architecture*
The project is structured into modular packages to ensure separation of concerns and maintainability.

1. Character System (Inheritance & Polymorphism)

At the core is an Abstract Animal Class. This allows for a unified way to handle different entities while enforcing specific behaviors through subclasses.

Polymorphism: The useSpecial() and move() methods are overridden to provide role-specific logic (e.g., the Predator's 2-cell horizontal dash vs. the Apex Predator's 3-cell dash).

Encapsulation: Animal states (position, score, cooldowns) are managed through protected attributes and public getters/setters.

2. AI Implementation

The ai package encapsulates complex decision-making logic:

PreyAI (Evaluation Heuristics): Analyzes 8-directional movement. It calculates a "Safety Score" by penalizing proximity to predators and rewarding proximity to food.

ApexPredatorAI (Targeting): Implements a "Greedy" approach, identifying the nearest target and calculating the optimal X/Y step to close the gap.

3. Graphical User Interface (GUI)

Built with Java Swing, the UI follows a structured rendering pipeline:

GameFrame: The main container that synchronizes the GameController with the visual GamePanel.

Rendering Pipeline: Uses paintComponent to layer the background, grid, food, and animals. It includes a HUD (Heads-Up Display) for real-time score tracking and a legend for player guidance.

4. Data Management & I/O

FoodChainLoader: Demonstrates data-driven design by reading .txt configuration files to spawn entities without hardcoding values.

Serialization: The SaveLoadManager serializes the GameState into a structured text format, allowing players to resume sessions perfectly.

*Controls & Mechanics*
Action	Control	Description
Movement	Arrow Keys	Move 1 cell per turn.
Special Ability	SPACE / SHIFT	Perform a Dash (2-turn cooldown).
Scoring	Interaction	+3 for eating prey/food, -1 for being caught.
Saving	Auto/End	Game state is logged to save.txt and gameLog.txt.

*Getting Started*
Prerequisites

Java Development Kit (JDK) 11 or higher.

Any Java IDE (IntelliJ IDEA, Eclipse, VS Code).

Installation

Clone the repository:

Bash
git clone https://github.com/yourusername/food-chain-through-time.git
Open the project in your IDE.

Run the Main.java class located in the app package.
