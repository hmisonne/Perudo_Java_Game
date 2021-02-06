
Initialize the game:
1. Set players
2. Set currentPlayer (randomly)
3. Set newRound = true
4. Set IsRunning = true

Start the game:
1. Shuffle dice
2. If not newRound, set currentPlayer to next player
3. Current player choose :
   - Make a Bet 
   - Reveal Dice (not available is new round is true)
4. If chose "Make a Bet": 
   - If not newRound: compare with previous bet to check if bet is valid
   - Set newRound to false. Go back to step 2.
5. Else:
   - If Bet is correct: set LooserIndex: currentPlayer
   - Else: set LooserIndex: previousPlayer
7. If Looser has only one die left:
   - Remove Looser from game
   - Else Looser loose a dice
8. If more than 1 player remaining: 
   - Set new round to true
   - Set currentPlayer to LooserIndex
(if dead, replaced by next User in list)
   - Go back to 1
8. Else: set IsRunning to false. End of the Game

