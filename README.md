# Artificial Intelligence Game - GangiHo
Artificial Intelligence Game using Minimax algorithm

##1) The Game

###Game Rules

1. 2 Player game.
2. Game board nxn where 6 > n > 16.
3. Each player must chose a token color (Black or White).
4. Tokens should be placed on the board 2 by 2 in consecutive cells.
5. Black tokens should be placed horizontally. White tokens should be placed vertically.
6. No token can be placed on top of another token of any color.
7. No token can be moved onced placed on the board.
8. AI move must take at most 3 seconds.

###Play Modes

1. Manual entry for both/one player(s)
2. Automatic entry for both/one player(s)

###AI algorithm

The algorithm used for the AI move is <a href="http://en.wikipedia.org/wiki/Minimax" target="_blank">Minimax</a>. Since the AI computations are limited to 3 seconds only. The total number of games generated in the Minimax is 300,000 nodes.

The number of game nodes generated and maximum depth possible at each AI turn is estimated before being created using the following formula:

    N(n,m) = n x (n-1) - k x m

Where `n` is the board size, `m` is the total number of moves since the start of the game and `k` is a constant `1.5` chosen after analyzing the maximum number of moves possible after each player turn on different board sizes.

###Heuristics

1. Count the total number of moves of the Max player
2. Count the total number of moves of the Min player
3. Check for patterns that will avoid placing in the guaranteed positions (Details in the code).
4. Check for patterns that will give Max advantage on maximizing his/her moves and minimizing Min's moves (Details in the code).
5. Check for Gameover or Tie

##2) Graghical User Interface (GUI)

###Start the GIU (GangiHo.jar)

<img src="https://raw.githubusercontent.com/amirbawab/Artificial-Intelligence-Game-GangiHo/master/Screenshots/1.png">

###Human vs AI

<img src="https://raw.githubusercontent.com/amirbawab/Artificial-Intelligence-Game-GangiHo/master/Screenshots/2.png">

###Debug mode

<img src="https://raw.githubusercontent.com/amirbawab/Artificial-Intelligence-Game-GangiHo/master/Screenshots/3.png">

