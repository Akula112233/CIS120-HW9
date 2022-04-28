=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Concept 1 - Collections/Maps: Instead of undo list, I used collections for the body of the
  snake. Because the snake can grow, I decided to use an ArrayList specifically. Whenever the snake
  moves, the components of the body all move forward within the snake. This is easy to do with
  ArrayLists because of their get functionality and normal array indexing.

  2. Concept 2 - File I/O: This feature is implemented as an auto-save feature whenever the user
  pauses the game with their spacebar. If they exit the game or decide to unpause the game, the
  save still remains the same, only if they pause the game again will a new save occur. Upon loading
  if there is a save file that exists, then it is loaded, however if there are any issues whatsoever
  with the file's formatting / existence of the file, then a default game reset is performed. The
  game continues as normal from there. This is appropriate for the fast-paced game to make saving
  occur at the press of a spacebar instead of a mouselick on a button. Additionally, saving
  occurs by loading out the food block, then speed block, then the velocity / position of the snake
  and all of its body components. In the same order it is saved, it is also read back in upon file
  opening.

  3. Concept 3 - Inheritance and Subtyping: This inhertiance and subtyping is used to implement
  the two types of blocks the snake can eat within my game. The foodblock and speedblock both
  extend to an abstract edibleblock class. Because the implmentation for "randomizeMove" is passed
  to both subclasses, an abstract class was used here. The "eatInteraction" function on the other
  hand is overridden by each specific block because the foodblock causes the snake to grow and the
  speedblock causes teh snake to go faster. Dynamic dispatch was used in both blocks being of type
  EdibleBlock when declared but of their respective types when instantiated, so their dynamic
  function call of "eatInteraction" is specific to their block type at runtime.

  4. Concept 4 - JUnit Testable Components: For this part, the game can be run without the GUI
  by simply creating new snake objects as if they were being instantiated from a file, then testing
  different game logic by setting edible block positions, making the snake move with the move
  function, then seeing if expected output/interaction with the blocks occurs. The core functions
  I tested were for the snake eating food vs speed blocks, eating both blocks, bumping into wall,
  and bumping into self. For edge cases, I tested if movement worked after hitting wall (it doesn't)
  , tested if the snake can bump into its tail (will die if it does, as expected), and what
  happens with block overlap (if food and speed were directly overlapped with each other).

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  RunSnake: This class instantiates the board and resets the game, only change I made here
  was to make sure I showed game instructions before proceeding to the game window.

  Edible Block: Abstract class for food & speed blocks to extend to. This class offers
  implementation for how the blocks are moved after they are eaten, and requires an implementation
  for how sub-class blocks handle being eaten. It also offers implementation for file saving each
  of the blocks (essentially returning the correct string to be saved in the output file).

  Food Block: Class for food block that helps snake grow, extends edible block and overrides
  eatInteraction function to grow snake when snake "eats" the block. Only gets eaten when
  the snake intersects with the block via snake.intersect function

  Speed Block: Class for speed block that helps snake grow, extends edible block and overrides
  eatInteraction function to increase snake speed "eats" the block. Only gets eaten when
  the snake intersects with the block via snake.intersect function

  GameCourt: This class initializes the board positioning. If a save file is present, all positions
  are read from that save file, but if not, a default board setup is used. This file takes care of
  whether or not the game should be running, and also implements a lot of the game logic for if
  the snake dies, when it eats, what blocks it eats, and what functions should be called upon eating
  This also helps to draw the components out within the board. It also takes care of a lot of
  game input logic like not taking inputs when snake is dead, not taking opposite key inputs
  (ex: north then south, or east then west), and other such tasks not specific to the game objects.

  GameObj: Not many changes made from starter code, however instead of treating velocity as vx / vy
  I changed to an approach of storing the direction (making use of Direction enum already provided)
  and storing the velocity of the game objects. This included the snake and the edible blocks since
  they all shared features of game object (mainly position, max width/height, and intersection
  functionality).

  Pair: This is a simple class implemented to treat each body of the snake as x,y coordinates for
  the top-left corner of the snake's body. This helped keep track of and move components of the
  snake whenever the snake itself "moved"

  Snake: Main snake, stores values for the head of the snake in the default GameObj fields, but
  also has the additional field of arraylist of pairs making up the body of the snake. It
  overrides the move function, and also provides public functionality for increasing its speed
  and body size for the food / speed blocks. It also has a filesaveinfo function to provide
  the perfect file output when saving to file this snake.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  One particular struggle I had was figuring out how to make the snake move in the correct way,
  which I found a bit difficult to do with 2D arrays. However, once I realized that the snake
  essentially moved by translating up one block (each block takes a higher blocks position) it
  made it easier to implement the specific type of movement and treat the snake as a single entity
  by using a collection (arraylist) instead.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  There is good separation of functionality, no object references are passed around, and only the
  required variables are accessible by external classes/files. If given the chance to refactor, I
  would work on implementing more types of blocks and making them timer based instead of
  action based (ex: blocks that disappear / move even when they aren't eaten by the snake).


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
 Star Image: https://affaso.com/5-point-stars-png-star-icon-flat-11562958768wpf63hu4tq/
 Mushroom Image: **from starter code package**